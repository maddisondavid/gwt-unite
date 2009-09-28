package gwtunite.testing;

import gwtunite.testing.framework.TestCase;
import gwtunite.testing.framework.TestCaseRegistry;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Creates the {@link TestCaseRegistry} 
 */
public class TestCaseRegistryGenerator  extends Generator{
	private final String packageName = TestCaseRegistry.class.getPackage().getName();
	private final String className = "GeneratedTestCaseExecutorRegistry";

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		JClassType testCaseClass = context.getTypeOracle().findType(TestCase.class.getName());
		JClassType testCasesInstances[] = testCaseClass.getSubtypes();
	
		// Generate the TestCaseExecutors
		Map<String, String> testCaseExecutorTypes = new HashMap<String, String>();
		for (JClassType testCaseType : testCasesInstances) {
			String testExecutorName = TestCaseExecutorGenerator.generateTestCaseExecutor(logger, context, testCaseType);
			testCaseExecutorTypes.put(testExecutorName, testCaseType.getQualifiedSourceName());
		}
		
		// Generate the TestCaseInfos
		Collection<String> testCaseInfos = new ArrayList<String>();
		for (JClassType testCaseType : testCasesInstances) {
			testCaseInfos.add(TestCaseInfoGenerator.generateTestCaseInfo(logger, context, testCaseType));
		}
		
		// Write the actual class file
		SourceWriter sourceWriter = emitClassDefinition(logger, context);
		if (sourceWriter != null) {
			emitGetTestCaseInfosMethod(logger, sourceWriter, testCaseInfos);
			emitNewExecutorInstanceMethod(logger, sourceWriter, testCaseExecutorTypes);
			sourceWriter.commit(logger);
		}
		return packageName+"."+className;
	}
	
	private SourceWriter emitClassDefinition(TreeLogger logger, GeneratorContext context) throws UnableToCompleteException{
		PrintWriter printWriter =  context.tryCreate(logger,packageName, className);
		if (printWriter == null) 
			return null;
		
	    ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory( packageName, className);
	    composerFactory.addImplementedInterface(TestCaseRegistry.class.getName());
	        
	    return composerFactory.createSourceWriter(context, printWriter);
	}
	
	private void emitGetTestCaseInfosMethod(TreeLogger logger, SourceWriter sourceWriter, Collection<String> testCaseInfos) {
		sourceWriter.println("public TestCaseInfo[] getTestCaseInfos() {");
		sourceWriter.indent();
			sourceWriter.println("return new TestCaseInfo[]{");
			sourceWriter.indent();
				for (String name : testCaseInfos) 
					sourceWriter.println("new "+name+"(),");
			sourceWriter.outdent();
			sourceWriter.println("};");
		sourceWriter.outdent();
		sourceWriter.println("}");	
	}
	
	private void emitNewExecutorInstanceMethod(TreeLogger logger, SourceWriter sourceWriter, Map<String,String> testCaseExecutors) {
		sourceWriter.println("public TestCaseExecutor newExecutorInstance(String testCaseName) {");
		sourceWriter.indent();
			for (Map.Entry<String, String> entry : testCaseExecutors.entrySet()) {
				sourceWriter.println("if (testCaseName.equals(\"" + entry.getValue()  + "\"))");
				sourceWriter.indent();
					sourceWriter.println("return new "+entry.getKey()+"();");
				sourceWriter.outdent();
				sourceWriter.println();
			}
			sourceWriter.println("throw new RuntimeException(testCaseName+\" Not found\");");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}
}

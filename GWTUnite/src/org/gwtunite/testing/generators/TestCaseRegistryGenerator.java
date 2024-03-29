package org.gwtunite.testing.generators;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.gwtunite.testing.client.framework.TestCase;
import org.gwtunite.testing.client.framework.TestCaseRegistry;

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
	
		// Generate the TestCaseWrappers Map<Name, WrapperClass>
		Map<String, String> testCaseWrapperTypes = new HashMap<String, String>();
		for (JClassType testCaseType : testCasesInstances) {
			String testWrapperName = TestCaseWrapperGenerator.generateTestCaseWrapper(logger, context, testCaseType);
			testCaseWrapperTypes.put(testWrapperName, testCaseType.getQualifiedSourceName());
		}
		
		// Generate the TestCaseInfos Collection<TestCaseInfoClass>
		Collection<String> testCaseInfos = new ArrayList<String>();
		for (JClassType testCaseType : testCasesInstances) {
			testCaseInfos.add(TestCaseInfoGenerator.generateTestCaseInfo(logger, context, testCaseType));
		}
		
		// Write the actual class file
		SourceWriter sourceWriter = emitClassDefinition(logger, context);
		if (sourceWriter != null) {
			emitConstructor(logger, sourceWriter, testCaseInfos);
			emitNewTestCaseInstanceMethod(logger, sourceWriter, testCaseWrapperTypes);
			sourceWriter.commit(logger);
		}
		return packageName+"."+className;
	}
	
	private SourceWriter emitClassDefinition(TreeLogger logger, GeneratorContext context) throws UnableToCompleteException{
		PrintWriter printWriter =  context.tryCreate(logger,packageName, className);
		if (printWriter == null) 
			return null;
		
	    ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory( packageName, className);
	    composerFactory.setSuperclass(TestCaseRegistry.class.getName());
	        
	    return composerFactory.createSourceWriter(context, printWriter);
	}

	private void emitConstructor(TreeLogger logger, SourceWriter sourceWriter, Collection<String> testInfoClassNames) {
		sourceWriter.println("public "+className+"() {");
		sourceWriter.indent();
			for (String infoClassName: testInfoClassNames) {
				sourceWriter.println("registerInfo(new "+infoClassName+"());");
			}
		sourceWriter.outdent();
		sourceWriter.println("}");
	}
	
	private void emitNewTestCaseInstanceMethod(TreeLogger logger, SourceWriter sourceWriter, Map<String,String> testCaseExecutors) {
		sourceWriter.println("public TestCase newTestCaseInstance(String testCaseClass) {");
		sourceWriter.indent();
			for (Map.Entry<String, String> entry : testCaseExecutors.entrySet()) {
				sourceWriter.println("if (testCaseClass.equals(\"" + entry.getValue()  + "\"))");
				sourceWriter.indent();
					sourceWriter.println("return new "+entry.getKey()+"();");
				sourceWriter.outdent();
				sourceWriter.println();
			}
			sourceWriter.println("throw new RuntimeException(\"Test case class \"+testCaseClass+\" Not found whilst creating a new instance\");");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}
}

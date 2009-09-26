package gwtunite.testing;

import gwtunite.testing.client.TestCase;
import gwtunite.testing.client.TestCaseExecutorRegistry;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class TestCaseExecutorRegistryGenerator  extends Generator{
	private final String packageName = TestCaseExecutorRegistry.class.getPackage().getName();
	private final String className = "GeneratedTestCaseExecutorRegistry";

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		TypeOracle typeOracle = context.getTypeOracle();
		
		JClassType testCaseClass = typeOracle.findType(TestCase.class.getName());
		JClassType testCases[] = testCaseClass.getSubtypes();
		
		Collection<String> testCaseExecutorTypes = new ArrayList<String>();
		for (JClassType testCaseType : testCases) {
			testCaseExecutorTypes.add(TestExecutorGenerator.generateTestCaseExecutor(logger, context, testCaseType));
		}
		
		SourceWriter sourceWriter = emitClass(logger, context);
		if (sourceWriter != null) {
			emitConstructor(logger, sourceWriter, testCaseExecutorTypes);
			sourceWriter.commit(logger);
			
			System.out.println("Finished "+packageName+"."+className);
		}
		return packageName+"."+className;
	}
	
	private SourceWriter emitClass(TreeLogger logger, GeneratorContext context) throws UnableToCompleteException{
		PrintWriter printWriter =  context.tryCreate(logger,packageName, className);
		if (printWriter == null) 
			return null;
		
	    ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory( packageName, className);
	    composerFactory.setSuperclass(TestCaseExecutorRegistry.class.getName());
	        
	    return composerFactory.createSourceWriter(context, printWriter);
	}
	
	private void emitConstructor(TreeLogger logger, SourceWriter sourceWriter, Collection<String> testCaseExecutorNames) {
		sourceWriter.println("public "+className+"() {");
		sourceWriter.indent();
			for (String testExecutorName : testCaseExecutorNames) {
				sourceWriter.println("registerTestExecutor(new "+testExecutorName+"());");
			}
		sourceWriter.outdent();
		sourceWriter.println("}");
	}
}

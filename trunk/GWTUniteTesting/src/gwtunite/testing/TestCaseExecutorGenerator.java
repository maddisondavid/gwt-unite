package gwtunite.testing;

import gwtunite.testing.framework.TestCase;
import gwtunite.testing.framework.TestCaseExecutor;
import gwtunite.testing.framework.TestResult;
import gwtunite.testing.framework.TestCaseExecutor.TestCompleteHandler;

import java.io.PrintWriter;
import java.util.Collection;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Creates the {@link TestCaseExecutor}s for all given TestCases.
 */
public class TestCaseExecutorGenerator {
	private static final String EXECUTOR_NAME_PREFIX = "TestCaseExecutor_";
	private static final String EXECUTE_METHOD_PREFIX = "execute_";

	public static String generateTestCaseExecutor(TreeLogger logger, GeneratorContext context, JClassType testCaseType) {
		String testCaseName = testCaseType.getName();
		String className = getExecutorClassName(testCaseType);
		String packageName = getExecutorPackageName(testCaseType);
		
		Collection<String> testNames = ReflectionUtils.findTestMethodNames(testCaseType);
		
		SourceWriter sourceWriter = emitClassDefinition(logger, context, packageName, className);
		if (sourceWriter != null) {
			emitGetTestCaseNameMethod(logger, sourceWriter, testCaseType.getQualifiedSourceName());
			emitGetTestsMethod(logger, sourceWriter, testNames);
			emitExecuteTestMethod(logger, sourceWriter, testNames);
			emitExecuteAllTests(logger, sourceWriter, testNames);
			
			for (String testName : testNames) 
				emitTestExecutorMethod(logger, sourceWriter,testCaseName, testName);
			
			sourceWriter.commit(logger);
		}
		
		return packageName +"."+className;
	}

	private static SourceWriter emitClassDefinition(TreeLogger logger, GeneratorContext context, String packageName, String className) {
		PrintWriter printWriter =  context.tryCreate(logger,packageName, className);
		if (printWriter == null) {
			return null;
		}
		
	    ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory( packageName, className);
	    composerFactory.setSuperclass(TestCaseExecutor.class.getName());
	    composerFactory.addImport(TestCase.AssertionFailureException.class.getName().replace('$', '.'));
	    composerFactory.addImport(TestResult.class.getName());
	    
	    return composerFactory.createSourceWriter(context, printWriter);
	}

	private static void emitExecuteAllTests(TreeLogger logger, SourceWriter sourceWriter, Collection<String> testNames) {
		sourceWriter.println("public void executeAllTests(TestCompleteHandler handler) throws Exception{");
		
		sourceWriter.indent();
			sourceWriter.println("runTestsAsync(new String[]{");
			
			for (String testName : testNames) 
				sourceWriter.println("\""+testName+"\",");
			
			sourceWriter.println("}, handler);");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	private static void emitTestExecutorMethod(TreeLogger logger, SourceWriter sourceWriter, String testCaseName, String testName) {
		sourceWriter.println("private void "+EXECUTE_METHOD_PREFIX+testName+"() throws Exception {");
		sourceWriter.indent();
			sourceWriter.println(testCaseName+" test = new "+testCaseName+"();");
			sourceWriter.println("test.setUp();");
			sourceWriter.println();
			sourceWriter.println("try {");
			sourceWriter.indent();
					sourceWriter.println("test."+testName+"();");
					sourceWriter.println("registerResult(\""+testName+"\", new TestResult(\""+testName+"\",TestResult.Result.PASSED));");
			sourceWriter.outdent();
			sourceWriter.println("} catch(Exception e) {");
			sourceWriter.indent();
					sourceWriter.println("if (e instanceof "+TestCase.AssertionFailureException.class.getName().replace('$', '.')+") {");
					sourceWriter.indent();
							sourceWriter.println("registerResult(\""+testName+"\", new TestResult(\""+testName+"\",TestResult.Result.FAILED, e));");
					sourceWriter.outdent();
					sourceWriter.println("} else {");
					sourceWriter.indent();
							sourceWriter.println("registerResult(\""+testName+"\", new TestResult(\""+testName+"\",TestResult.Result.ERROR, e));");
					sourceWriter.outdent();
					sourceWriter.println("}");
			sourceWriter.outdent();
			sourceWriter.println("} finally {");
			sourceWriter.indent();
				sourceWriter.println("test.tearDown();");
			sourceWriter.outdent();
			sourceWriter.println("}");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	private static void emitExecuteTestMethod(TreeLogger logger, SourceWriter sourceWriter, Collection<String> testNames) {
		sourceWriter.println("public void executeTest(String testName, TestCompleteHandler handler) throws Exception {");
		sourceWriter.indent();
			for (String testName : testNames) {
				sourceWriter.println("if (testName.equals(\""+testName+"\")) {");
				sourceWriter.indent();
					sourceWriter.println(EXECUTE_METHOD_PREFIX+testName+"();");
					sourceWriter.println("handler.onTestComplete(this);");
					// If we're in Async mode here then we need to make sure we call the OnComplete!
					sourceWriter.println("return;");
				sourceWriter.outdent();
				sourceWriter.println("}");
			}
			sourceWriter.println("throw new Exception(\"Test \"+testName+\" not found\");");
		sourceWriter.outdent();
		sourceWriter.println("}");		
	}

	private static void emitGetTestsMethod(TreeLogger logger, SourceWriter sourceWriter, Collection<String> testNames) {
		sourceWriter.println("public String[] getTestNames() {");
		sourceWriter.indent();
			sourceWriter.println("return new String[]{");
			sourceWriter.indent();
				for (String name : testNames) 
					sourceWriter.println("\""+name+"\",");
			sourceWriter.outdent();
			sourceWriter.println("};");
		sourceWriter.outdent();
		sourceWriter.println("}");		
	}

	private static void emitGetTestCaseNameMethod(TreeLogger logger, SourceWriter sourceWriter, String testCaseName) {
		sourceWriter.println("public String getTestCaseName() {");
		sourceWriter.indent();
			sourceWriter.println("return \""+testCaseName+"\";");
		sourceWriter.outdent();
		sourceWriter.println("}");		
	}

	private static String getExecutorClassName(JClassType type) {
		return EXECUTOR_NAME_PREFIX+type.getName();
	}
	
	private static String getExecutorPackageName(JClassType type) {
		return type.getPackage().getName();
	}
}

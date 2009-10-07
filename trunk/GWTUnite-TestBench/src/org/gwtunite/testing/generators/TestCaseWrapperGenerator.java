package org.gwtunite.testing.generators;

import java.io.PrintWriter;
import java.util.Collection;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Creates a wrapper around the original test case that facilitates calling specific methods
 */
public class TestCaseWrapperGenerator {
	private static final String WRAPPER = "_Wrapper";

	public static String generateTestCaseWrapper(TreeLogger logger, GeneratorContext context, JClassType testCaseType) {
		String className = getGeneratedClassName(testCaseType);
		String packageName = getPackageName(testCaseType);
		
		Collection<String> testNames = TestReflectionUtils.findTestMethodNames(testCaseType);
		
		SourceWriter sourceWriter = emitClassDefinition(logger, context, packageName, className, testCaseType);
		if (sourceWriter != null) {
			emitConstructor(logger, sourceWriter, className);
			emitExecuteTestMethod(logger, sourceWriter, testNames);
						
			sourceWriter.commit(logger);
		}
		
		return packageName +"."+className;
	}

	private static SourceWriter emitClassDefinition(TreeLogger logger, GeneratorContext context, String packageName, String className, JClassType baseType) {
		PrintWriter printWriter =  context.tryCreate(logger,packageName, className);
		if (printWriter == null) {
			return null;
		}
		
	    ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory( packageName, className);
	    composerFactory.setSuperclass(baseType.getQualifiedSourceName());
	    
	    return composerFactory.createSourceWriter(context, printWriter);
	}
	
	private static void emitConstructor(TreeLogger logger, SourceWriter sourceWriter, String className) {
		sourceWriter.println("public "+className+"() {");
		sourceWriter.indent();
			sourceWriter.println("super();");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}


	private static void emitExecuteTestMethod(TreeLogger logger, SourceWriter sourceWriter, Collection<String> testNames) {
		sourceWriter.println("@Override protected void executeTestMethod(String testName) throws Exception {");
		sourceWriter.indent();
			for (String testName : testNames) {
				sourceWriter.println("if (testName.equals(\""+testName+"\")) {");
				sourceWriter.indent();
					sourceWriter.println("super."+testName+"();");
					sourceWriter.println("return;");
				sourceWriter.outdent();
				sourceWriter.println("}");
				sourceWriter.println();
			}
		
			sourceWriter.println("throw new Exception(\"Test \"+testName+\" not found\");");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}


	private static String getGeneratedClassName(JClassType type) {
		return type.getName()+WRAPPER;
	}
	
	private static String getPackageName(JClassType type) {
		return type.getPackage().getName();
	}
}

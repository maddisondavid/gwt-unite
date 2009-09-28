package gwtunite.testing;

import gwtunite.testing.framework.TestCaseInfo;

import java.io.PrintWriter;
import java.util.Collection;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Creates the {@link TestCaseInfo} classes for all TestCases found
 * 
 * TestCase info objects will be places in the same package as the original TestCase and be called TestInfo_<TestCaseName>
 */
public class TestCaseInfoGenerator {
	private static final String TESTINFO_NAME_PREFIX = "TestInfo_";

	public static String generateTestCaseInfo(TreeLogger logger, GeneratorContext context, JClassType testCaseType) {
		String className = getExecutorClassName(testCaseType);
		String packageName = getExecutorPackageName(testCaseType);
		
		SourceWriter sourceWriter = emitClassDefinition(logger, context, packageName, className);
		if (sourceWriter != null) {
			emitGetNameMethod(logger, sourceWriter, testCaseType.getQualifiedSourceName());
			emitGetTestsMethod(logger, sourceWriter, ReflectionUtils.findTestMethodNames(testCaseType));
			
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
	    composerFactory.addImplementedInterface(TestCaseInfo.class.getName());

	    return composerFactory.createSourceWriter(context, printWriter);
	}

	private static void emitGetTestsMethod(TreeLogger logger, SourceWriter sourceWriter, Collection<String> testNames) {
		sourceWriter.println("public String[] getTests() {");
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

	private static void emitGetNameMethod(TreeLogger logger, SourceWriter sourceWriter, String testCaseName) {
		sourceWriter.println("public String getName() {");
		sourceWriter.indent();
			sourceWriter.println("return \""+testCaseName+"\";");
		sourceWriter.outdent();
		sourceWriter.println("}");		
	}

	private static String getExecutorClassName(JClassType type) {
		return TESTINFO_NAME_PREFIX+type.getName();
	}
	
	private static String getExecutorPackageName(JClassType type) {
		return type.getPackage().getName();
	}
}

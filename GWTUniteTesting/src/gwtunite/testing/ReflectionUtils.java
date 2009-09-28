package gwtunite.testing;

import gwtunite.testing.framework.Test;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;

class ReflectionUtils {

	/** Returns the names of all test methods */
	public static Collection<String> findTestMethodNames(JClassType type) {
		Collection<String> testNames = new ArrayList<String>();
		for (JMethod method : type.getMethods()) {
			if (method.getAnnotation(Test.class) != null)
				testNames.add(method.getName());
		}
		
		return testNames;
	}

}

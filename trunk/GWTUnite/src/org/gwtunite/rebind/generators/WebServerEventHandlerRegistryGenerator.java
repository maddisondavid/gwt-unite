package org.gwtunite.rebind.generators;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.gwtunite.client.net.PathFragment;
import org.gwtunite.client.net.WebServerEventHandler;
import org.gwtunite.client.net.WebServerEventHandlerRegistry;
import org.gwtunite.testing.client.framework.TestCaseRegistry;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class WebServerEventHandlerRegistryGenerator extends Generator {
	private static final String GENERATED_REGISTRY_NAME = WebServerEventHandlerRegistry.class.getName()+"_Generated";
	private static final String GENERATED_PACKAGE = WebServerEventHandlerRegistry.class.getPackage().getName();
	
	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		TypeOracle typeOracle = context.getTypeOracle();
		
		JClassType eventHandlerType = typeOracle.findType(WebServerEventHandler.class.getName());
		Map<String, JClassType> automaticEventHandlers = new HashMap<String, JClassType>();
		for (JClassType eventHandler : eventHandlerType.getSubtypes()) {
			if (eventHandler.getAnnotation(PathFragment.class) != null) {
				String pathFragment = eventHandler.getAnnotation(PathFragment.class).name();
				automaticEventHandlers.put(pathFragment, eventHandler);
			}
		}

		if (!automaticEventHandlers.isEmpty()) {
			SourceWriter srcWriter = getSourceWriter(logger, context);
			if (srcWriter != null) {
				emitConstructor(logger, srcWriter, automaticEventHandlers);
				
				srcWriter.commit(logger);
			}
			
			return GENERATED_PACKAGE+"."+GENERATED_REGISTRY_NAME;
			
		} else {
			return null;
		}
	}

	public SourceWriter getSourceWriter(TreeLogger logger, GeneratorContext context) {  
		PrintWriter printWriter =  context.tryCreate(logger,GENERATED_PACKAGE, GENERATED_REGISTRY_NAME);
		if (printWriter == null) 
			return null;
		
	    ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory( GENERATED_PACKAGE, GENERATED_REGISTRY_NAME);
	    composerFactory.setSuperclass(WebServerEventHandlerRegistry.class.getName());
	        
	    return composerFactory.createSourceWriter(context, printWriter);
	}
	
	public void emitConstructor(TreeLogger logger, SourceWriter sourceWriter, Map<String, JClassType> automaticEventHandlers) {
		sourceWriter.println("public "+GENERATED_REGISTRY_NAME+"() {");
		sourceWriter.indent();
			for (Map.Entry<String, JClassType> entry : automaticEventHandlers.entrySet()) {
				sourceWriter.println("registerHandler(\""+entry.getKey()+"\",new "+entry.getValue()+"());");
			}
		sourceWriter.outdent();
		sourceWriter.println("}");
	}
}

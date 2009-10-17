package org.gwtunite.generators;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.gwtunite.client.rpc.RemoteService;
import org.gwtunite.client.rpc.RemoteServiceRegistry;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Generates the registry will (at runtime) will hold all details about RemoteServices that need to be registered
 * with the WebServer
 */
public class RemoteServiceRegistryGenerator extends Generator {
	private final String packageName = RemoteServiceRegistry.class.getPackage().getName();
	private final String className = "GeneratedRemoteServiceRegistry";

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		TypeOracle typeOracle = context.getTypeOracle();

		SourceWriter sourceWriter = emitClassDefinition(logger, context);
		if (sourceWriter == null) {
			return packageName + "."+className;
		}
		
		// Generate All the RemoteServiceWrappers
		Map<String, String> remoteServiceWrappers = new HashMap<String,String>();
		try {
			JClassType[] subTypes = typeOracle.getType(RemoteService.class.getName()).getSubtypes();
			
			for (JClassType remoteService : subTypes) {
				remoteServiceWrappers.putAll(RemoteServiceWrapperCreator.create(logger, context, remoteService));
			}
		} catch (NotFoundException e) {
			logger.log(Type.ERROR, "Unable to find type "+RemoteService.class.getName());
			throw new UnableToCompleteException();
		}
		
		// Write the actual class file
		emitConstructor(logger, sourceWriter, remoteServiceWrappers);
		sourceWriter.commit(logger);
		return packageName+"."+className;
	}
	
	private SourceWriter emitClassDefinition(TreeLogger logger, GeneratorContext context) throws UnableToCompleteException{
		PrintWriter printWriter =  context.tryCreate(logger,packageName, className);
		if (printWriter == null) 
			return null;
		
	    ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory( packageName, className);
	    composerFactory.setSuperclass(RemoteServiceRegistry.class.getName());
	        
	    return composerFactory.createSourceWriter(context, printWriter);
	}

	private void emitConstructor(TreeLogger logger, SourceWriter sourceWriter, Map<String, String> remoteServices) {
		sourceWriter.println("public "+className+"() {");
		sourceWriter.indent();
			for (Map.Entry<String, String> entry : remoteServices.entrySet()) {
				sourceWriter.println("registerService(\""+entry.getKey()+"\", new "+entry.getValue()+"());");
			}
		sourceWriter.outdent();
		sourceWriter.println("}");
	}
}

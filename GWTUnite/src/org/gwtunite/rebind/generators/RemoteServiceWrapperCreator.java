package org.gwtunite.rebind.generators;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;


/**
 * Generates the WebServiceEventHandler for a RemoteService
 *
 */
public class RemoteServiceWrapperCreator{
	private static final String WRAPPER_PREFIX = "_Wrapper";
	
	public static Map<String, String> create(TreeLogger logger, GeneratorContext context, JClassType service) throws UnableToCompleteException {
		JClassType remoteServiceInterf = context.getTypeOracle().findType(com.google.gwt.user.client.rpc.RemoteService.class.getName());
		
		Collection<JClassType> serviceInterfaces = new ArrayList<JClassType>();
		for (JClassType interf : service.getImplementedInterfaces()) {
			System.out.println("Checking is "+interf.getQualifiedSourceName()+" assignableTo "+remoteServiceInterf.getQualifiedSourceName());
			if (interf.isAssignableTo(remoteServiceInterf)) {
				serviceInterfaces.add(interf);
			}
		}

		if (serviceInterfaces.isEmpty()) {
			logger.log(Type.ERROR, "Remote service "+service.getQualifiedSourceName()+" does not implement any RemoteService interface");
			throw new UnableToCompleteException();
		}
		
		JClassType serviceInterface = serviceInterfaces.iterator().next();
		RemoteServiceRelativePath remotePath = serviceInterface.getAnnotation(RemoteServiceRelativePath.class);
		if (remotePath==null) {
			logger.log(Type.ERROR,"Remove service interface "+serviceInterface.getQualifiedSourceName()+" requires the annotation "+RemoteServiceRelativePath.class.getName());
			throw new UnableToCompleteException();
		}
			
		// Generate serializer for it
		RemoteServiceSerializerCreator typeCreator = new RemoteServiceSerializerCreator();
		String serializerName = typeCreator.create(logger, context, serviceInterface);
	
		// Generate Wrapper
		String packageName = service.getPackage().getName();
		String className = service.getSimpleSourceName()+WRAPPER_PREFIX;
		
		Map<String, String> endPoints = new HashMap<String, String>();
		SourceWriter srcWriter = emitClassDefinition(logger, context, service.getPackage().getName(), className, service);
		if (srcWriter != null) {
			emitConstructor(logger, srcWriter, className);
			emitGetSerializerMethod(logger, srcWriter, serializerName);
			emitCallMethod(logger, srcWriter, serviceInterfaces);
			emitGetReturnTypeMethod(logger, srcWriter, serviceInterfaces);
			srcWriter.commit(logger);
			
			return Collections.singletonMap(remotePath.value(), packageName+"."+className);
		} else {
			return Collections.emptyMap();
		}
	}
	
	private static SourceWriter emitClassDefinition(TreeLogger logger, GeneratorContext context, String packageName, String className, JClassType baseType) {
		PrintWriter printWriter =  context.tryCreate(logger,packageName, className);
		if (printWriter == null) {
			return null;
		}
		
	    ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory( packageName, className);
	    composerFactory.setSuperclass(baseType.getQualifiedSourceName());
	    composerFactory.addImport(com.google.gwt.user.client.rpc.impl.Serializer.class.getName());
	    
	    return composerFactory.createSourceWriter(context, printWriter);
	}
	
	private static void emitConstructor(TreeLogger logger, SourceWriter sourceWriter, String className) {
		sourceWriter.println("public "+className+"() {");
		sourceWriter.indent();
			sourceWriter.println("super();");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	private static void emitGetSerializerMethod(TreeLogger logger, SourceWriter sourceWriter, String serializerName) {
		sourceWriter.println("@Override protected Serializer getSerializer() {");
		sourceWriter.indent();
			sourceWriter.println("return new "+serializerName+"();");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	private static void emitCallMethod(TreeLogger logger, SourceWriter sourceWriter, Collection<JClassType> serviceInterfaces) {
		sourceWriter.println("@Override protected Object callMethod(String methodName, Object[] parameters) throws Exception {");
		sourceWriter.indent();
			for (JClassType intfType : serviceInterfaces) {
				for (JMethod method : intfType.getMethods()) {
					sourceWriter.println("if (methodName.equals(\""+method.getName()+"\")) {");
						sourceWriter.indent();
							if (method.getReturnType().equals(JPrimitiveType.VOID)) {
								sourceWriter.println("super."+method.getName()+"(");
							} else {
								sourceWriter.println("return super."+method.getName()+"(");
							}
						
							sourceWriter.indent();
								int paramPos = 0;
								for (JParameter param : method.getParameters()) {
									if (paramPos > 0)
										sourceWriter.print(",");
									
									sourceWriter.println("("+param.getType().getQualifiedSourceName()+")parameters["+(paramPos++)+"]");
								}
							sourceWriter.outdent();						
							sourceWriter.println(");");
							
							if (method.getReturnType().equals(JPrimitiveType.VOID)) {
								sourceWriter.println("return VOID_RETURN;");
							}
						sourceWriter.outdent();
					sourceWriter.println("}");
					sourceWriter.println();
				}
			}
		
			sourceWriter.println("throw new Exception(\"Method \"+methodName+\" not found\");");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}
	
	private static void emitGetReturnTypeMethod(TreeLogger logger, SourceWriter sourceWriter, Collection<JClassType> serviceInterfaces) {
		sourceWriter.println("@Override protected Class<?> getReturnType(String methodName) throws Exception {");
		sourceWriter.indent();
			for (JClassType intfType : serviceInterfaces) {
				for (JMethod method : intfType.getMethods()) {
					sourceWriter.println("if (methodName.equals(\""+method.getName()+"\")) {");
						sourceWriter.indent();
								sourceWriter.println("return "+method.getReturnType().getQualifiedSourceName()+".class;");
						sourceWriter.outdent();
					sourceWriter.println("}");
					sourceWriter.println();
				}
			}
		
			sourceWriter.println("throw new Exception(\"Method \"+methodName+\" not found\");");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}
}

package org.gwtunite.rebind.generators;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.util.Util;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.rebind.rpc.SerializableTypeOracle;
import com.google.gwt.user.rebind.rpc.SerializableTypeOracleBuilder;
import com.google.gwt.user.rebind.rpc.TypeSerializerCreator;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;

/** 
 * Creates the Serializer for a remote service interface 
 */
public class RemoteServiceSerializerCreator {

	private static final Map<JPrimitiveType, ResponseReader> JPRIMITIVETYPE_TO_RESPONSEREADER = new HashMap<JPrimitiveType, ResponseReader>();
	
	//private final String packageName = TestCaseRegistry.class.getPackage().getName();
//	private final String className = "GeneratedTestCaseExecutorRegistry";

	private JClassType serviceInterface;

  {
    JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.BOOLEAN,
        ResponseReader.BOOLEAN);
    JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.BYTE,
        ResponseReader.BYTE);
    JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.CHAR,
        ResponseReader.CHAR);
    JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.DOUBLE,
        ResponseReader.DOUBLE);
    JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.FLOAT,
        ResponseReader.FLOAT);
    JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.INT, ResponseReader.INT);
    JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.LONG,
        ResponseReader.LONG);
    JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.SHORT,
        ResponseReader.SHORT);
    JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.VOID,
        ResponseReader.VOID);
  }

  public String create(TreeLogger logger, GeneratorContext context, JClassType serviceInterface) throws UnableToCompleteException {
	  this.serviceInterface = serviceInterface;
		TypeOracle typeOracle = context.getTypeOracle();
		
		// Determine the set of serializable types
		SerializableTypeOracleBuilder stob = new SerializableTypeOracleBuilder(logger, context.getPropertyOracle(), typeOracle);
		try {
		  addRequiredRoots(logger, typeOracle, stob);
		
		  addRemoteServiceRootTypes(logger, typeOracle, stob, serviceInterface);
		} catch (NotFoundException e) {
		  logger.log(TreeLogger.ERROR, "", e);
		  throw new UnableToCompleteException();
		}
		
		// Create a resource file to receive all of the serialization information
		// computed by STOB and mark it as private so it does not end up in the
		// output.
		OutputStream pathInfo = context.tryCreateResource(logger,
		    serviceInterface.getQualifiedSourceName() + ".rpc.log");
		stob.setLogOutputStream(pathInfo);
		SerializableTypeOracle sto = stob.build(logger);
		if (pathInfo != null) {
		  context.commitResource(logger, pathInfo).setPrivate(true);
		}
		
		TypeSerializerCreator tsc = new TypeSerializerCreator(logger, sto, context, sto.getTypeSerializerQualifiedName(serviceInterface));
		tsc.realize(logger);
		
		//String serializationPolicyStrongName = writeSerializationPolicyFile(logger,context, sto);
		
		
		return sto.getTypeSerializerQualifiedName(serviceInterface);
	}
	
  /**
   * Adds a root type for each type that appears in the RemoteService interface
   * methods.
   */
  private static void addRemoteServiceRootTypes(TreeLogger logger,
      TypeOracle typeOracle, SerializableTypeOracleBuilder stob,
      JClassType remoteService) throws NotFoundException {
    logger = logger.branch(TreeLogger.DEBUG, "Analyzing '"
        + remoteService.getParameterizedQualifiedSourceName()
        + "' for serializable types", null);

    JMethod[] methods = remoteService.getOverridableMethods();

    JClassType exceptionClass = typeOracle.getType(Exception.class.getName());

    TreeLogger validationLogger = logger.branch(TreeLogger.DEBUG,
        "Analyzing methods:", null);
    for (JMethod method : methods) {
      TreeLogger methodLogger = validationLogger.branch(TreeLogger.DEBUG,
          method.toString(), null);
      JType returnType = method.getReturnType();
      if (returnType != JPrimitiveType.VOID) {
        TreeLogger returnTypeLogger = methodLogger.branch(TreeLogger.DEBUG,
            "Return type: " + returnType.getParameterizedQualifiedSourceName(),
            null);
        stob.addRootType(returnTypeLogger, returnType);
      }

      JParameter[] params = method.getParameters();
      for (JParameter param : params) {
        TreeLogger paramLogger = methodLogger.branch(TreeLogger.DEBUG,
            "Parameter: " + param.toString(), null);
        JType paramType = param.getType();
        stob.addRootType(paramLogger, paramType);
      }

      JType[] exs = method.getThrows();
      if (exs.length > 0) {
        TreeLogger throwsLogger = methodLogger.branch(TreeLogger.DEBUG,
            "Throws:", null);
        for (JType ex : exs) {
          if (!exceptionClass.isAssignableFrom(ex.isClass())) {
            throwsLogger = throwsLogger.branch(
                TreeLogger.WARN,
                "'"
                    + ex.getQualifiedSourceName()
                    + "' is not a checked exception; only checked exceptions may be used",
                null);
          }

          stob.addRootType(throwsLogger, ex);
        }
      }
    }
  }

  /**
   * Add the implicit root types that are needed to make RPC work. These would
   * be {@link String} and {@link IncompatibleRemoteServiceException}.
   */
  private static void addRequiredRoots(TreeLogger logger,
      TypeOracle typeOracle, SerializableTypeOracleBuilder stob)
      throws NotFoundException {
    logger = logger.branch(TreeLogger.DEBUG, "Analyzing implicit types");

    // String is always instantiable.
    JClassType stringType = typeOracle.getType(String.class.getName());
    stob.addRootType(logger, stringType);

    // IncompatibleRemoteServiceException is always serializable
    JClassType icseType = typeOracle.getType(IncompatibleRemoteServiceException.class.getName());
    stob.addRootType(logger, icseType);
  }
  
  private String writeSerializationPolicyFile(TreeLogger logger,
	      GeneratorContext ctx, SerializableTypeOracle sto)
	      throws UnableToCompleteException {
	try {
	  ByteArrayOutputStream baos = new ByteArrayOutputStream();
	  OutputStreamWriter osw = new OutputStreamWriter(baos,
	      SerializationPolicyLoader.SERIALIZATION_POLICY_FILE_ENCODING);
	  PrintWriter pw = new PrintWriter(osw);
	
	  JType[] serializableTypes = sto.getSerializableTypes();
	  for (int i = 0; i < serializableTypes.length; ++i) {
	    JType serializableType = serializableTypes[i];
	    String binaryTypeName = sto.getSerializedTypeName(serializableType);
	    boolean maybeInstantiated = sto.maybeInstantiated(serializableType);
	    pw.print(binaryTypeName + ", " + Boolean.toString(maybeInstantiated)
	        + '\n');
	  }
	
	  // Closes the wrapped streams.
	  pw.close();
	
	  byte[] serializationPolicyFileContents = baos.toByteArray();
	  String serializationPolicyName = Util.computeStrongName(serializationPolicyFileContents);
	
	  String serializationPolicyFileName = SerializationPolicyLoader.getSerializationPolicyFileName(serializationPolicyName);
	  OutputStream os = ctx.tryCreateResource(logger,
	      serializationPolicyFileName);
	  if (os != null) {
	    os.write(serializationPolicyFileContents);
	    ctx.commitResource(logger, os);
	  } else {
	    logger.log(TreeLogger.TRACE,
	        "SerializationPolicy file for RemoteService '"
	            + serviceInterface.getQualifiedSourceName()
	            + "' already exists; no need to rewrite it.", null);
	  }
	
	  return serializationPolicyName;
	} catch (UnsupportedEncodingException e) {
	  logger.log(TreeLogger.ERROR,
	      SerializationPolicyLoader.SERIALIZATION_POLICY_FILE_ENCODING
	          + " is not supported", e);
	  throw new UnableToCompleteException();
	} catch (IOException e) {
	  logger.log(TreeLogger.ERROR, null, e);
	  throw new UnableToCompleteException();
	}
  }
}

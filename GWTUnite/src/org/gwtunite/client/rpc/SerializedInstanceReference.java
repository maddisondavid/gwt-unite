package org.gwtunite.client.rpc;
/**
 * Interface for describing a serialized instance reference reference.
 */
public interface SerializedInstanceReference {
  String SERIALIZED_REFERENCE_SEPARATOR = "/";

  /**
   * @return name of the type
   */
  String getName();

  /**
   * @return signature of the instance reference
   */
  String getSignature();
}
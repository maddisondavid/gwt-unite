package org.gwtunite.client;

/** 
 * Indicates a particular feature hasn't been installed.  
 * 
 * <p>This is thrown in instances such as using the File IO when the feature hasn't been specified
 * in the Applications config.xml</p>
 */
public class FeatureUnavailableException extends RuntimeException {
	private static final long serialVersionUID = -3724934282568336650L;

	public FeatureUnavailableException(String feature) {
		super("Feature " +feature+" has not been installed.  Please specify it in your config.xml");
	}
}

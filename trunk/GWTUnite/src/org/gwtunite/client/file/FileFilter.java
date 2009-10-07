package org.gwtunite.client.file;

/** 
 * Describes the callback object that will be consulted during {@link File#listFiles(FileFilter)}
 * 
 * <p>The following code shows how this is used to retrieve all .txt files in the MyFiles directory:
 * 
 * <pre><code>
 * File artifacts = mountPoint.resolve("MyFiles");
 * File[] files = artifacts.listFiles(new FileFilter() {
 *						public boolean accept(File pathname) {
 *							return pathname.getName().endsWith(".txt");
 *						}
 *					});
 *</code></pre>
 */
public interface FileFilter {
	boolean accept(File file);
}

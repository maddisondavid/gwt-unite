package org.gwtunite.client.commons;

public class StringEscapeUtils {
	
	/**
	 * Escapes all the HTML tags in a given string 
	 * 
	 * @param html A string containg the HTML to be encoded
	 * @return A string with all HTML tags escaped (i.e. <Html> &lt;Html&gt;)
	 */
	public static native String escapeHTML(String html) /*-{
		return html.replace( /&/g, '&amp;' )
				   .replace( /%20/g, '&nbsp;' )
				   .replace( /</g, '&lt;' )
				   .replace( />/g, '&gt;' );
	}-*/;
	
	/**
	 * Unescapes any HTML within the given string, i.e &lt;Html&gt; = <Html> 
	 * 
	 * @param text The text containing the escaped HTML 
	 * @return the text with the HTML unescaped
	 */
	public static native String unescapeHTML(String text) /*-{
		return text.replace( '&amp;', '&' )
				   .replace( '&nbsp;', ' ')
				   .replace( '&lt;', '<')
				   .replace( '&gt;', '>');	
	}-*/;
}

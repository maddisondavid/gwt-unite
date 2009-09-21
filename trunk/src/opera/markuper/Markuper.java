package opera.markuper;

import com.google.gwt.core.client.JavaScriptObject;

final public class Markuper extends JavaScriptObject {

	protected Markuper() {
	}
	
	native static public Markuper getInstance() /*-{
		return markuper;
	}-*/;
	
	public native HTMLHelper getHtmlHelper() /*-{
		return this.HTMLHelper;
	}-*/;
	
	public native ExtendedElement getExtendedElement() /*-{
		return this.extendedElement;
	}-*/;
}

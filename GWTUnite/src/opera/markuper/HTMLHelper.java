package opera.markuper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;

public final class HTMLHelper extends JavaScriptObject {

	protected HTMLHelper() {
	}
	
	public native Document parseFromString(String html) /*-{
		return this.parseFromString(html);
	}-*/;
	
	public native String serializeToString(Document doc) /*-{
		return this.serializeToString(doc);
	}-*/;
}

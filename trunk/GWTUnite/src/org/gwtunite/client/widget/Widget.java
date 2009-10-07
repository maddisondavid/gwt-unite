package org.gwtunite.client.widget;

import org.gwtunite.client.FeatureUnavailableException;

import com.google.gwt.core.client.JavaScriptObject;

public final class Widget extends JavaScriptObject {
	protected Widget() {
	}

	public static native boolean isAvailable() /*-{
		if (widget) 
			return true
		else 
			return false;
	}-*/;
	
	public static Widget getInstance() {
		if (!isAvailable())
			throw new FeatureUnavailableException("Widget feature not installed");
		
		return jsniGetInstance();
	}
	
	private native static Widget jsniGetInstance() /*-{
		return widget;
	}-*/;
	
	public native String getIdentifier() /*-{
		return this.identifier;
	}-*/;

	public native String getOriginURL() /*-{
		return this.originURL;
	}-*/;
	
	public native String getWidgetMode() /*-{
		return this.widgetMode;
	}-*/;
	
	public native void setOnShowCallback(OnShowCallback callback) /*-{
		widget.onShow = function() {
			try {
				callback.@opera.widget.Widget.OnShowCallback::onShow()();
			} catch(exception) {
				@opera.io.Utils::handleException(Ljava/lang/Exception;)(exception);
			}
		};
	}-*/;

	public native void setOnHideCallback(OnHideCallback callback) /*-{
		widget.onHide = function() {
			try {
				callback.@opera.widget.Widget.OnHideCallback::onHide()();
			} catch(exception) {
				@opera.io.Utils::handleException(Ljava/lang/Exception;)(exception);
			}
		};
	}-*/;
	
	
	public native void showNotification(String message) /*-{
		this.showNotification(message);
	}-*/;
	
	public native void showNotification(String message, NotificationCallback callback) /*-{
		this.showNotification(message, function() {
			callback.@opera.widget.Widget.NotificationCallback::onNotificationClicked()();
		});
	}-*/;
	
	public native void getAttention() /*-{
		this.getAttention();
	}-*/;
	
	public native void hide() /*-{
		this.hide();
	}-*/;
	
	public native void openURL(String url) /*-{
		this.openURL(url);
	}-*/;
	
	public native String preferenceForKey(String key) /*-{
		return this.preferenceForKey(key);
	}-*/;
	
	public native void setPreferenceForKey(String key, String value) /*-{
		this.setPreferenceForKey(key, value);
	}-*/;
	
	public native void show() /*-{
		this.show();
	}-*/;
	
	public interface NotificationCallback {
		void onNotificationClicked();
	}

	public interface OnShowCallback {
		void onShow();
	}
	
	public interface OnHideCallback {
		void onHide();
	}
}

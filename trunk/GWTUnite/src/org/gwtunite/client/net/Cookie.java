package org.gwtunite.client.net;

import java.util.Date;

import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

public class Cookie {
	private static final String SEPARATOR = "; ";
	private final String name;
	private String value;
	private String path = null;
	private String domain = null;
	private Date expires = null;
	private boolean secure = false;
	
	public Cookie(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public void setExpires(Date date) {
		this.expires = date;
	}
	
	public void setSecure(boolean secure) {
		this.secure = secure;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getValue() {
		return value;
	}
	
	public Date getExpires() {
		return expires;
	}
	
	public boolean isSecure() {
		return secure;
	}
	
	@Override
	public String toString() {
		return URL.encode(getName())+"="+URL.encode(getValue())+SEPARATOR+
			   (expires != null? "expires="+expires+SEPARATOR : "") +
			   (path != null?"path="+getPath()+SEPARATOR : "") +
			   (domain != null?"domain="+getDomain()+SEPARATOR:"")+
			   isSecure();
	}
}

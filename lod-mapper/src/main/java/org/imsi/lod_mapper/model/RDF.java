package org.imsi.lod_mapper.model;

import java.io.Serializable;

public class RDF implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4897164969107241732L;
	private String id;
	private String property;
	private String value;
	
	public RDF() {}
	
	public RDF(String id, String property, String value) {
		this.id = id;
		this.property = property;
		this.value = value;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public String getProperty() {
		return property;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "RDF [id=" + id + ", property=" + property + ", value=" + value + "]";
	}

	
}

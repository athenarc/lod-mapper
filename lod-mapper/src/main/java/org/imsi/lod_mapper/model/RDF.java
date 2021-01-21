package org.imsi.lod_mapper.model;

import java.io.Serializable;

public class RDF implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4897164969107241732L;
	protected String id;
	protected String property;
	protected String value;
	
	public RDF(String id, String property, String value) {
		super();
		this.id = id;
		this.property = property;
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

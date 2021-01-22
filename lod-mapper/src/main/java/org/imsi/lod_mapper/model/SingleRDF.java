package org.imsi.lod_mapper.model;

import java.io.Serializable;

public class SingleRDF implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 289665857896176903L;

	private String rdf;
	public SingleRDF() {}

	public SingleRDF(String rdf) {this.rdf = rdf;}

	public SingleRDF(String id, String property, String value) {
		this.rdf = "<" + id + ">" + " " + "<"  + property + ">" + " " + value + " .";
	}
	public String getRdf() {
		return rdf;
	}

	public void setRdf(String rdf) {
		this.rdf = rdf;
	}

	@Override
	public String toString() {
		return  rdf;
	}
	
	
}

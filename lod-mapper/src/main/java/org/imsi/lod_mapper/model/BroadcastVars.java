package org.imsi.lod_mapper.model;

import java.io.Serializable;
import java.util.List;

public class BroadcastVars implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -530968049932378342L;
	
	private List<String> columns;

	public BroadcastVars(List<String> columns) {
		// TODO Auto-generated constructor stub
		this.columns = columns;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	
	

}

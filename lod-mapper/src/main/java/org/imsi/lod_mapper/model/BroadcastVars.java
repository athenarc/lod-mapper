package org.imsi.lod_mapper.model;

import java.io.Serializable;
import java.util.List;

public class BroadcastVars implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -530968049932378342L;
	
	private List<String> columns;
	private String propertyMap;
	private String valueMap;
	
	public BroadcastVars(List<String> columns) {
		// TODO Auto-generated constructor stub
		this.columns = columns;
	}

	
	public BroadcastVars(List<String> columns, String propertyMap, String valueMap) {
		super();
		this.columns = columns;
		this.propertyMap = propertyMap;
		this.valueMap = valueMap;
	}


	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public String getPropertyMap() {
		return propertyMap;
	}

	public void setPropertyMap(String propertyMap) {
		this.propertyMap = propertyMap;
	}

	public String getValueMap() {
		return valueMap;
	}

	public void setValueMap(String valueMap) {
		this.valueMap = valueMap;
	}
	
	

}

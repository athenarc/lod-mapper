package org.imsi.lod_mapper.model;

import java.io.Serializable;
import java.util.List;

public class BroadcastVars implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -530968049932378342L;
	
	private List<String> columnsDS;
	private List<String> columnsOrg;
	private List<String> columnsPrj;
	private List<String> columnsRes;

	private String propertyMap;
	private String valueMap;
	private String idMap;
	

	public BroadcastVars(List<String> columnsDS, List<String> columnsOrg, List<String> columnsPrj,
			List<String> columnsRes, String propertyMap, String valueMap, String idMap) {
		super();
		this.columnsDS = columnsDS;
		this.columnsOrg = columnsOrg;
		this.columnsPrj = columnsPrj;
		this.columnsRes = columnsRes;
		this.propertyMap = propertyMap;
		this.valueMap = valueMap;
		this.idMap = idMap;
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


	public String getIdMap() {
		return idMap;
	}


	public void setIdMap(String idMap) {
		this.idMap = idMap;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<String> getColumnsDS() {
		return columnsDS;
	}

	public List<String> getColumnsOrg() {
		return columnsOrg;
	}

	public List<String> getColumnsPrj() {
		return columnsPrj;
	}

	public List<String> getColumnsRes() {
		return columnsRes;
	}
	
	

}

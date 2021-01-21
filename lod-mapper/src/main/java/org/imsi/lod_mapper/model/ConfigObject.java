package org.imsi.lod_mapper.model;

import java.io.Serializable;
import java.util.HashMap;

public class ConfigObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6460630846589017696L;
	private String warehouseLocation;
	private String dbName;
	private String appName;
	private String query;
	private String propertyMap;
	private Integer dataset;
	private HashMap<String, String> mappings;
	
	public String getWarehouseLocation() {
		return warehouseLocation;
	}
	public void setWarehouseLocation(String warehouseLocation) {
		this.warehouseLocation = warehouseLocation;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getPropertyMap() {
		return propertyMap;
	}
	public Integer getDataset() {
		return dataset;
	}
	public void setDataset(Integer dataset) {
		this.dataset = dataset;
	}
	
}

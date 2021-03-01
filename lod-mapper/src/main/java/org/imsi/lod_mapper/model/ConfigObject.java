package org.imsi.lod_mapper.model;

import java.io.Serializable;
import java.util.HashMap;

public class ConfigObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6460630846589017696L;
	protected String warehouseLocation;
	protected String dbName;
	protected String appName;
	protected String queryDS;
	protected String queryOrg;
	protected String queryPrj;
	protected String queryRes;

	protected String propertyMap;
	protected String valueMap;
	protected String idMap;
	protected Integer dataset;
	protected  Integer numPartitions;
	protected String datapath;
	protected HashMap<String, String> mappings;

	public Integer getNumPartitions() {
		return numPartitions;
	}
	public void setNumPartitions(Integer numPartitions) {
		this.numPartitions = numPartitions;
	}
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
	public String getPropertyMap() {
		return propertyMap;
	}
	public Integer getDataset() {
		return dataset;
	}
	public void setDataset(Integer dataset) {
		this.dataset = dataset;
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
	public String getDatapath() {
		return datapath;
	}
	public void setDatapath(String datapath) {
		this.datapath = datapath;
	}
	public String getQueryDS() {
		return queryDS;
	}
	public void setQueryDS(String queryDS) {
		this.queryDS = queryDS;
	}
	public String getQueryOrg() {
		return queryOrg;
	}
	public void setQueryOrg(String queryOrg) {
		this.queryOrg = queryOrg;
	}
	public String getQueryPrj() {
		return queryPrj;
	}
	public void setQueryPrj(String queryPrj) {
		this.queryPrj = queryPrj;
	}
	public String getQueryRes() {
		return queryRes;
	}
	public void setQueryRes(String queryRes) {
		this.queryRes = queryRes;
	}
}

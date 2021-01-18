package org.imsi.lod_mapper;

import java.io.Serializable;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.SparkSession.Builder;
import org.imsi.lod_mapper.model.ConfigObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Mapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2135269757431236587L;
	private static String pathToPropertiesFile = "config.json";
	private static ConfigObject configObject;
	
	
			
    public static void main( String[] args ){
        readProperties();
        String id = "";
        SparkSession sparkSession = setupSparkSession();
        Dataset<Row> records = sparkSession.sql(configObject.getQuery());
        Dataset<Row> recordsGrouped = records.groupBy(id).
        Dataset<Row> rdds = records.map((MapFunction<Row, Row>) row -> {
        	
        	return row;
        });
    }

	private static SparkSession setupSparkSession() {
		return new Builder()
				  .appName(configObject.getAppName())
				  .config("spark.sql.warehouse.dir", configObject.getWarehouseLocation())
				  .enableHiveSupport()
				  .getOrCreate();

	}

	private static void readProperties() {
		ObjectMapper objectMapper = new ObjectMapper();
		configObject = new ConfigObject();
		try {
			configObject = objectMapper.readValue(pathToPropertiesFile, ConfigObject.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}
}


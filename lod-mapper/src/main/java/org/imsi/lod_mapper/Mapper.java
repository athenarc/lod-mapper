package org.imsi.lod_mapper;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.flatten;
import static org.apache.spark.sql.functions.collect_set;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession.Builder;
import org.apache.spark.sql.SparkSession;
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
	private static Map<String, List<String>> params;

	
			
    public static void main( String[] args ) throws IOException{
        readProperties(args);
        String id = "id";
        SparkSession sparkSession = setupSparkSession();
        Dataset<Row> records = sparkSession.sql(configObject.getQuery());
        
        //datasource group
        Dataset<Row> datasourceRecords = records.groupBy(col("id")).agg((collect_set(col("id"))),
        		(collect_set(col("originalid"))),
        		(collect_set(col("englishname"))),
        		(collect_set(col("officialname"))),
        		(collect_set(col("dateofcollection"))),
        		(collect_set(col("dateoftransformation"))),
        		(collect_set(col("journal"))),
        		(collect_set(col("datasourcetype"))),
        		(collect_set(col("collectedfrom"))),
        		flatten(collect_set(col("pid"))),
        		(collect_set(col("longitude"))),
        		(collect_set(col("latitude"))),
        		flatten(collect_set(col("subjects"))),
        		(collect_set(col("description"))),
        		(collect_set(col("websiteurl"))),
        		(collect_set(col("logourl"))),
        		(collect_set(col("accessinfopackage"))),
        		(collect_set(col("namespaceprefix"))),
        		(collect_set(col("versioning"))),
        		(collect_set(col("target"))),
        		(collect_set(col("reltype"))),
        		(collect_set(col("subreltype"))));

        Dataset<Row> rdds = datasourceRecords.map((MapFunction<Row, Row>) row -> {
        	System.err.println(row);
        	return row;
        }, Encoders.bean(Row.class));
        rdds.cache();
    }

	private static SparkSession setupSparkSession() {
		return new Builder()
				  .appName(configObject.getAppName())
				  .config("spark.sql.warehouse.dir", configObject.getWarehouseLocation())
				  .enableHiveSupport()
				  .getOrCreate();

	}

	private static void readProperties(String[] args) throws IOException {
		setInputParameters(args);
		if (params.containsKey("properties"))
			pathToPropertiesFile = params.get("properties").get(0);
		else
			pathToPropertiesFile = "config.json";

		ObjectMapper objectMapper = new ObjectMapper();
		configObject = new ConfigObject();
		try {
			configObject = objectMapper.readValue(new File(pathToPropertiesFile), ConfigObject.class);
			System.err.println(configObject.getWarehouseLocation());
			System.err.println(configObject.getDbName());
			System.err.println(configObject.getQuery());


		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

	private static void setInputParameters(String[] args) {
		params = new HashMap<>();

		List<String> options = null;
		for (int i = 0; i < args.length; i++) {
			final String a = args[i];

			if (a.charAt(0) == '-') {
				if (a.length() < 2) {
					System.err.println("Error at argument " + a);
					return;
				}

				options = new ArrayList<>();
				params.put(a.substring(1), options);
			} else if (options != null) {
				options.add(a);
			} else {
				System.err.println("Illegal parameter usage");
				return;
			}
		}
		System.err.println(params);
		
	}
}


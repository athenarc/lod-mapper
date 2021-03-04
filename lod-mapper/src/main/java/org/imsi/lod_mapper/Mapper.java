package org.imsi.lod_mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.SparkSession.Builder;
import org.apache.spark.sql.types.DataTypes;
import org.imsi.lod_mapper.model.*;
import org.imsi.lod_mapper.util.MapCountries;
import org.imsi.lod_mapper.util.MapLanguages;
import org.imsi.lod_mapper.util.SingleRDFPartitioner;
import scala.Tuple2;
import scala.reflect.ClassTag;
import scala.reflect.ClassTag$;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import static org.apache.spark.sql.functions.*;

public class Mapper implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2135269757431236587L;
    private static String pathToPropertiesFile = "config.json";
    private static ConfigObject configObject;
    private static Map<String, List<String>> params;
    private static MapLanguages mapLanguages;
    private static MapCountries mapCountries;

    static {
        mapLanguages = new MapLanguages();
    }

    static {
        mapCountries = new MapCountries();
    }


    public static void main(String[] args) throws IOException {

        // Read config properites
        readProperties(args);

        SparkSession sparkSession = setupSparkSession();
        sparkSession.conf().set("spark.sql.shuffle.partitions", configObject.getShufflePartitions());
        sparkSession.conf().set("spark.reducer.maxReqsInFlight", 1);
        sparkSession.conf().set("spark.shuffle.io.retryWait", "60s");
        sparkSession.conf().set("spark.shuffle.io.maxRetries", 10);
        sparkSession.conf().set("spark.network.timeout", "800s");
        sparkSession.conf().set("spark.dynamicAllocation.executorIdleTimeout", 1200);
        sparkSession.conf().set("spark.dynamicAllocation.enabled", "false");
        sparkSession.conf().set("spark.dynamicAllocation.enabled", false);


        // Delete data if already exists
        FileSystem fs = FileSystem.get(sparkSession.sparkContext().hadoopConfiguration());
        Path outPutPath = new Path(configObject.getDatapath());
        if (fs.exists(outPutPath))
            fs.delete(outPutPath, true);

        Dataset<Row> dsRecords = sparkSession.sql(configObject.getQueryDS());//.repartition(configObject.getNumPartitions(),col("id"));
        Dataset<Row> orgRecords = sparkSession.sql(configObject.getQueryOrg());//.repartition(configObject.getNumPartitions(),col("id"));
        Dataset<Row> prjRecords = sparkSession.sql(configObject.getQueryPrj());//.repartition(configObject.getNumPartitions(),col("id"));
        Dataset<Row> resRecords = sparkSession.sql(configObject.getQueryRes());//.repartition(configObject.getNumPartitions(),col("id"));

        Dataset<Row> groupedRecordsDS = dsRecords.withColumn("versioning", col("versioning").cast(DataTypes.StringType))
                .groupBy(col("id")).agg(
                        (collect_set(col("originalid"))).alias("originalid"),
                        (collect_set(col("englishname"))).alias("englishname"),
                        (collect_set(col("officialname"))).alias("officialname"),
                        (collect_set(col("dateofcollection"))).alias("dateofcollection"),
                        (collect_set(col("dateoftransformation"))).alias("dateoftransformation"),
                        (collect_set(col("journal"))).alias("journal"),
                        (collect_set(col("datasourcetype"))).alias("datasourcetype"),
                        (collect_set(col("collectedfrom"))).alias("collectedfrom"),
                        flatten(collect_set(col("pid"))).alias("pid"),
                        (collect_set(col("longitude"))).alias("longitude"),
                        (collect_set(col("latitude"))).alias("latitude"),
                        flatten(collect_set(col("subjects"))).alias("subjects"),
                        (collect_set(col("description"))).alias("description"),
                        (collect_set(col("websiteurl"))).alias("websiteurl"),
                        (collect_set(col("logourl"))).alias("logourl"),
                        (collect_set(col("accessinfopackage"))).alias("accessinfopackage"),
                        (collect_set(col("namespaceprefix"))).alias("namespaceprefix"),
                        (collect_set(col("versioning"))).alias("versioning"),
                        (collect_list(col("target"))).alias("target"),
                        (collect_list(col("reltype"))).alias("reltype"),
                        (collect_list(col("subreltype"))).alias("subreltype"));

        Dataset<Row> groupedRecordsOrg = orgRecords.groupBy(col("id")).agg(
                collect_set(col("originalid")).alias("originalid"),
                collect_set(col("legalname")).alias("legalname"),
                collect_set(col("legalshortname")).alias("legalshortname"),
                collect_set(col("alternativenames")).alias("alternativenames"),
                collect_set(col("country")).alias("country"),
                collect_set(col("dateofcollection")).alias("dateofcollection"),
                collect_set(col("dateoftransformation")).alias("dateoftransformation"),
                collect_set(col("collectedfrom")).alias("collectedfrom"),
                flatten(collect_set(col("pid"))).alias("pid"),
                collect_set(col("websiteurl")).alias("websiteurl"),
                collect_set(col("logourl")).alias("logourl"),
                collect_list(col("target")).alias("target"),
                collect_list(col("reltype")).alias("reltype"),
                collect_list(col("subreltype")).alias("subreltype"));

        Dataset<Row> groupedRecordsPrj = prjRecords.withColumn("fundedamount", col("fundedamount").cast(DataTypes.StringType))
                .withColumn("totalcost", col("totalcost").cast(DataTypes.StringType))
                .groupBy(col("id"))
                .agg(
                        collect_set(col("acronym")).alias("acronym"),
                        collect_set(col("callidentifier")).alias("callidentifier"),
                        collect_set(col("contracttype")).alias("contracttype"),
                        collect_set(col("contactfullname")).alias("contactfullname"),
                        collect_set(col("duration")).alias("duration"),
                        collect_set(col("enddate")).alias("enddate"),
                        collect_set(col("dateofcollection")).alias("dateofcollection"),
                        collect_set(col("dateoftransformation")).alias("dateoftransformation"),
                        collect_set(col("collectedfrom")).alias("collectedfrom"),
                        flatten(collect_set(col("pid"))).alias("pid"),
                        collect_set(col("websiteurl")).alias("websiteurl"),
                        collect_set(col("currency")).alias("currency"),
                        collect_set(col("fundedamount")).alias("fundedamount"),
                        collect_set(col("h2020classification")).alias("h2020classification"),
                        collect_set(col("h2020programme")).alias("h2020programme"),
                        collect_set(col("fundinglevel1")).alias("fundinglevel1"),
                        collect_set(col("fundinglevel2")).alias("fundinglevel2"),
                        collect_set(col("fundinglevel3")).alias("fundinglevel3"),
                        collect_set(col("keywords")).alias("keywords"),
                        collect_set(col("subjects")).alias("subjects"),
                        collect_set(col("title")).alias("title"),
                        collect_set(col("totalcost")).alias("totalcost"),
                        collect_set(col("summary")).alias("summary"),
                        collect_set(col("startdate")).alias("startdate"),
                        collect_list(col("target")).alias("target"),
                        collect_list(col("reltype")).alias("reltype"),
                        collect_list(col("subreltype")).alias("subreltype"));

//        Dataset<Row> groupedRecordsRes = resRecords
//                .withColumn("id", col("id"))
////                .groupBy(col("id"))
//                .agg(
//                        collect_set(col("originalid")).alias("originalid"),
//                        collect_set(col("dateofcollection")).alias("dateofcollection"),
//                        collect_set(col("title")).alias("title"),
//                        collect_set(col("publisher")).alias("publisher"),
//                        collect_set(col("bestaccessright")).alias("bestaccessright"),
//                        collect_set(col("collectedfrom")).alias("collectedfrom"),
//                        flatten(collect_set(col("pid"))).alias("pid"),
//                        flatten(collect_set(col("author"))).alias("author"),
//                        collect_set(col("resulttype")).alias("resulttype"),
//                        collect_set(col("language")).alias("language"),
//                        collect_set(col("country")).alias("country"),
//                        flatten(collect_set(col("subject"))).alias("subject"),
//                        collect_set(col("description")).alias("description"),
//                        collect_set(col("dateofacceptance")).alias("dateofacceptance"),
//                        collect_set(col("embargoenddate")).alias("embargoenddate"),
//                        collect_set(col("resourcetype")).alias("resourcetype"),
//                        flatten(collect_set(col("externalreference"))).alias("externalreference"),
//                        collect_list(col("target")).alias("target"),
//                        collect_list(col("reltype")).alias("reltype"),
//                        collect_list(col("subreltype")).alias("subreltype"));

        Dataset<Row> groupedRecordsRes = resRecords
                .withColumn("id", col("id"))
                .withColumn("originalid", col("originalid"))
                .withColumn("dateofcollection", col("dateofcollection"))
                .withColumn("title", col("title"))
                .withColumn("publisher", col("publisher"))
                .withColumn("bestaccessright", col("bestaccessright"))
                .withColumn("collectedfrom", col("collectedfrom"))
                .withColumn("resulttype", col("resulttype"))
                .withColumn("language", col("language"))
                .withColumn("country", col("country"))
                .withColumn("description", col("description"))
                .withColumn("dateofacceptance", col("dateofacceptance"))
                .withColumn("embargoenddate", col("embargoenddate"))
                .withColumn("resourcetype", col("resourcetype"))
                .withColumn("target", col("target"))
                .withColumn("reltype", col("reltype"))
                .withColumn("subreltype", col("subreltype"))
                .withColumn("pid", flatten(col("pid")))
                .withColumn("author", flatten(col("author")))
                .withColumn("subject", flatten(col("subject")))
                .withColumn("externalreference", flatten(col("externalreference")));


        List<String> columnsDS = Arrays.asList(groupedRecordsDS.columns());
        List<String> columnsOrg = Arrays.asList(groupedRecordsOrg.columns());
        List<String> columnsPrj = Arrays.asList(groupedRecordsPrj.columns());
        List<String> columnsRes = Arrays.asList(groupedRecordsRes.columns());

        // Broadcast the variables needed by the workers
        ClassTag<BroadcastVars> classTagBroadcastVars = ClassTag$.MODULE$.apply(BroadcastVars.class);


        Broadcast<BroadcastVars> broadcastColumns = sparkSession.sparkContext()
                .broadcast(new BroadcastVars(columnsDS, columnsOrg, columnsPrj, columnsRes,
                        configObject.getPropertyMap(), configObject.getValueMap(), configObject.getIdMap()), classTagBroadcastVars);

        /* This is the beef region of the code, where we transform each column into an RDF.
         * For each row we get each of its values (List<String>) and then for each one of those we create and RDF
         * The id, is the id of the row, the property is the name of the column while the value is the value from the List.
         */

        /* DS */
        Dataset<TTL> rdfDatasetDS = groupedRecordsDS.flatMap((FlatMapFunction<Row, TTL>) row -> {
            List<RDF> rdfs = new ArrayList<>();
            List<TTL> ttls = new ArrayList<>();
            TTL ttl = new TTL();
            List<String> columnsI = broadcastColumns.getValue().getColumnsDS();
            String propertyVal = broadcastColumns.getValue().getPropertyMap();
            String valueVal = broadcastColumns.getValue().getValueMap();
            String idVal = broadcastColumns.getValue().getIdMap();

            String rowId = row.get(0).toString();
            List<String> target = new ArrayList<>();
            List<String> relType = new ArrayList<>();
            if (!rowId.contains("dedup")) {
                ttl.setId(idVal + "datasource/" + rowId);
                ttl.setRdfType("http://lod.openaire.eu/vocab/DatasourceEntity");
                RDF rdfH = new RDF(idVal + "datasource/" + rowId, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "<http://lod.openaire.eu/vocab/DatasourceEntity>");
                rdfs.add(rdfH);
                for (int i = 1; i < columnsI.size(); i++) {
                    List<String> col = row.getList(i);
                    String colName = columnsI.get(i);
                    if (colName.contentEquals("target")) {
                        if (!col.isEmpty()) target = col;
                    } else if (colName.contentEquals("reltype")) {
                        if (!col.isEmpty()) relType = col;
                    } else if (colName.contentEquals("subreltype")) {
                        if (col != null)
                            for (int j = 0; j < target.size(); j++) {
                                String val = col.get(j).toString();
                                if (val.contains("NULL")) continue;
                                String relVal = "<http://lod.openaire.eu/data/";
                                String rel = relType.get(j).toLowerCase();
                                if (rel.contains("Result")) relVal = relVal.concat("result/");
                                else if (rel.contains("Organization")) relVal = relVal.concat("organization/");
                                else if (rel.contains("Project")) relVal = relVal.concat("project/");
                                ttl.setPredicateObject(propertyVal + val, relVal + target.get(j)+">");
                                RDF rdf = new RDF(idVal + "datasource/" + rowId, propertyVal + val, relVal + target.get(j) + ">");
                                rdfs.add(rdf);
                            }
                    } else {
                        if (col != null)
                            for (int j = 0; j < col.size(); j++) {
                                String val = col.get(j).toString();
                                if(val.equals("")) continue;
                                if (val.contains("NULL")) continue;
                                if (val.contains("http://") || val.contains("https://")) val = "<" + val + ">";
                                else val = '"' + val + '"';
//                                System.out.println("PO "+propertyVal + columnsI.get(i)+"   "+val);
                                ttl.setPredicateObject(propertyVal + columnsI.get(i), val);
                                RDF rdf = new RDF(idVal + "datasource/" + rowId, propertyVal + columnsI.get(i), val);
                                rdfs.add(rdf);
                            }
                    }

                }
                ttls.add(ttl);
            }

            return ttls.iterator();//rdfs.iterator();
        }, Encoders.bean(TTL.class));

        /* ORG */
        Dataset<TTL> rdfDatasetOrg = groupedRecordsOrg.flatMap((FlatMapFunction<Row, TTL>) row -> {
            List<RDF> rdfs = new ArrayList<>();
            List<TTL> ttls = new ArrayList<>();
            TTL ttl = new TTL();
            List<String> columnsI = broadcastColumns.getValue().getColumnsOrg();
            String propertyVal = broadcastColumns.getValue().getPropertyMap();
            String valueVal = broadcastColumns.getValue().getValueMap();
            String idVal = broadcastColumns.getValue().getIdMap();

            String rowId = row.get(0).toString();
            List<String> target = new ArrayList<>();
            List<String> relType = new ArrayList<>();
            if (!rowId.contains("dedup")) {
                ttl.setId(idVal + "organization/" + rowId);
                ttl.setRdfType("http://lod.openaire.eu/vocab/OrganizationEntity");
                RDF rdfH = new RDF(idVal + "organization/" + rowId, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "<http://lod.openaire.eu/vocab/OrganizationEntity>");
                rdfs.add(rdfH);
                
                for (int i = 1; i < columnsI.size(); i++) {
                    List<String> col = row.getList(i);
                    String colName = columnsI.get(i);
                    if (colName.contentEquals("target")) {
                        if (!col.isEmpty()) target = col;
                    } else if (colName.contentEquals("reltype")) {
                        if (!col.isEmpty()) relType = col;
                    } else if (colName.contentEquals("subreltype")) {
                        if (col != null)
                            for (int j = 0; j < target.size(); j++) {
                                String val = col.get(j).toString();
                                if (val.contains("NULL")) continue;
                                String relVal = "<http://lod.openaire.eu/data/";
                                String rel = relType.get(j).toLowerCase();
                                if (rel.contains("Result")) relVal = relVal.concat("result/");
                                else if (rel.contains("Datasource")) relVal = relVal.concat("datasource/");
                                else if (rel.contains("Project")) relVal = relVal.concat("project/");
                                ttl.setPredicateObject(propertyVal + val, relVal + target.get(j)+">");
                                RDF rdf = new RDF(idVal + "organization/" + rowId, propertyVal + val, relVal + target.get(j) + ">");
                                rdfs.add(rdf);
                            }
                    } else {
                        if (col != null)
                            for (int j = 0; j < col.size(); j++) {
                                String val = col.get(j);
                                if (colName.contentEquals("country")) {
//                                    System.out.println("1: " + val);
                                    val = mapCountries.getCountryURI(col.get(j).trim());
//                                    System.out.println("2: " + val);
                                }
                                if (val.contains("NULL")) continue;
                                if(val.equals("")) continue;
                                if (val.contains("http://") || val.contains("https://")) val = "<" + val + ">";
                                else val = '"' + val + '"';
                                ttl.setPredicateObject(propertyVal + columnsI.get(i), val);
                                RDF rdf = new RDF(idVal + "organization/" + rowId, propertyVal + columnsI.get(i), val);
                                rdfs.add(rdf);
                            }
                    }
                }
                ttls.add(ttl);
            }
            return ttls.iterator();
        }, Encoders.bean(TTL.class));


        /* PRJ */
        Dataset<TTL> rdfDatasetPrj = groupedRecordsPrj.flatMap((FlatMapFunction<Row, TTL>) row -> {
            List<RDF> rdfs = new ArrayList<>();
            List<TTL> ttls = new ArrayList<>();
            TTL ttl = new TTL();
            List<String> columnsI = broadcastColumns.getValue().getColumnsPrj();
            String propertyVal = broadcastColumns.getValue().getPropertyMap();
            String valueVal = broadcastColumns.getValue().getValueMap();
            String idVal = broadcastColumns.getValue().getIdMap();

            String rowId = row.get(0).toString();
            List<String> target = new ArrayList<>();
            List<String> relType = new ArrayList<>();
            if (!rowId.contains("dedup")) {
                ttl.setId(idVal + "project/" + rowId);
                ttl.setRdfType("http://lod.openaire.eu/vocab/ProjectEntity");
                RDF rdfH = new RDF(idVal + "project/" + rowId, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "<http://lod.openaire.eu/vocab/ProjectEntity>");
                rdfs.add(rdfH);
                for (int i = 1; i < columnsI.size(); i++) {
                    List<String> col = row.getList(i);
                    String colName = columnsI.get(i);
                    if (colName.contentEquals("target")) {
                        if (!col.isEmpty()) target = col;
                    } else if (colName.contentEquals("reltype")) {
                        if (!col.isEmpty()) relType = col;
                    } else if (colName.contentEquals("subreltype")) {
                        if (col != null)
                            for (int j = 0; j < target.size(); j++) {
                                String val = col.get(j).toString();
                                if (val.contains("NULL")) continue;
                                String relVal = "<http://lod.openaire.eu/data/";
                                String rel = relType.get(j).toLowerCase();
                                if (rel.contains("Result")) relVal = relVal.concat("result/");
                                else if (rel.contains("Organization")) relVal = relVal.concat("organization/");
                                else if (rel.contains("Datasource")) relVal = relVal.concat("datasource/");
                                ttl.setPredicateObject(propertyVal + val, relVal + target.get(j)+">");
                                RDF rdf = new RDF(idVal + "project/" + rowId, propertyVal + val, relVal + target.get(j) + ">");
                                rdfs.add(rdf);
                            }
                    } else {
                        if (col != null)
                            for (int j = 0; j < col.size(); j++) {
                                String val = col.get(j).toString();
                                if (val.contains("NULL")) continue;
                                if(val.equals("")) continue;
                                if (val.contains("http://") || val.contains("https://")) val = "<" + val + ">";
                                else val = '"' + val + '"';
                                ttl.setPredicateObject(propertyVal + columnsI.get(i), val);
                                RDF rdf = new RDF(idVal + "project/" + rowId, propertyVal + columnsI.get(i), val);
                                rdfs.add(rdf);
                            }
                    }
                }
                ttls.add(ttl);
            }
            return ttls.iterator();
        }, Encoders.bean(TTL.class));

        /* RES */
        Dataset<TTL> rdfDatasetRes = groupedRecordsRes.flatMap((FlatMapFunction<Row, TTL>) row -> {
            List<RDF> rdfs = new ArrayList<>();
            List<TTL> ttls = new ArrayList<>();
            TTL ttl = new TTL();
            List<String> columnsI = broadcastColumns.getValue().getColumnsRes();
            String propertyVal = broadcastColumns.getValue().getPropertyMap();
            String valueVal = broadcastColumns.getValue().getValueMap();
            String idVal = broadcastColumns.getValue().getIdMap();

            String rowId = row.get(0).toString();
            List<String> target = new ArrayList<>();
            List<String> relType = new ArrayList<>();
            if (!rowId.contains("dedup")) {
                ttl.setId(idVal + "result/" + rowId);
                ttl.setRdfType("http://lod.openaire.eu/vocab/ResultEntity");
                RDF rdfH = new RDF(idVal + "result/" + rowId, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "<http://lod.openaire.eu/vocab/ResultEntity>");
                rdfs.add(rdfH);
                for (int i = 1; i < columnsI.size(); i++) {
                    List<String> col = row.getList(i);
                    String colName = columnsI.get(i);
                    if (colName.contentEquals("target")) {
                        if (!col.isEmpty()) target = col;
                    } else if (colName.contentEquals("reltype")) {
                        if (!col.isEmpty()) relType = col;
                    } else if (colName.contentEquals("subreltype")) {
                        if (col != null)
                            for (int j = 0; j < target.size(); j++) {
                                String val = col.get(j).toString();
                                if (val.contains("NULL")) continue;
                                String relVal = "<http://lod.openaire.eu/data/";

                                String rel = relType.get(j).toLowerCase();
                                if (rel.contains("Datasource")) relVal = relVal.concat("datasource/");
                                else if (rel.contains("Organization")) relVal = relVal.concat("organization/");
                                else if (rel.contains("Project")) relVal = relVal.concat("project/");
                                ttl.setPredicateObject(propertyVal + val, relVal + target.get(j)+">");
//                                RDF rdf = new RDF(idVal + "result/" + rowId, propertyVal + val, relVal + target.get(j) + ">");
//                                rdfs.add(rdf);

                            }
                    } else {
                        if (col != null)
                            for (int j = 0; j < col.size(); j++) {
                                try {
                                    String val = col.get(j);
                                    if (colName.contentEquals("language")) {
                                        val = mapLanguages.getLangURI(col.get(j));
                                    }
                                    if(val.equals("")) continue;
                                    if (val.contains("NULL")) continue;
                                    if (val.contains("http://") || val.contains("https://")) val = "<" + val + ">";
                                    else val = '"' + val + '"';
                                    ttl.setPredicateObject(propertyVal + columnsI.get(i), val);
//                                    RDF rdf = new RDF(idVal + "result/" + rowId, propertyVal + columnsI.get(i), val);
//                                    rdfs.add(rdf);
                                } catch (Exception e) {
                                    System.err.println(e.getMessage());
                                    continue;
                                }
                            }
                    }
                }
                ttls.add(ttl);
            }
            return ttls.iterator();
        }, Encoders.bean(TTL.class));

        // Create a single dataset of RDFS.
        Dataset<SingleTTL> rdfsDS = rdfDatasetDS.map((MapFunction<TTL, SingleTTL>) row -> {
//            String rid = row.getId();
//            String property = row.getProperty();
//            String value = row.getValue();
//            SingleRDF singleRDF = new SingleRDF(rid, property, value);
            return new SingleTTL(row);
        }, Encoders.bean(SingleTTL.class));

        Dataset<SingleTTL> rdfsOrg = rdfDatasetOrg.map((MapFunction<TTL, SingleTTL>) row -> {
//            String rid = row.getId();
//            String property = row.getProperty();
//            String value = row.getValue();
//            SingleRDF singleRDF = new SingleRDF(rid, property, value);
            return new SingleTTL(row);
        }, Encoders.bean(SingleTTL.class));

        Dataset<SingleTTL> rdfsPrj = rdfDatasetPrj.map((MapFunction<TTL, SingleTTL>) row -> {
//            String rid = row.getId();
//            String property = row.getProperty();
//            String value = row.getValue();
//            SingleRDF singleRDF = new SingleRDF(rid, property, value);
            return new SingleTTL(row);
        }, Encoders.bean(SingleTTL.class));

//        Dataset<SingleRDF> rdfsRes = rdfDatasetRes.map((MapFunction<RDF, SingleRDF>) row -> {
//            String rid = row.getId();
//            String property = row.getProperty();
//            String value = row.getValue();
//            SingleRDF singleRDF = new SingleRDF(rid, property, value);
//            return singleRDF;
//        }, Encoders.bean(SingleRDF.class));

        JavaRDD<TTL> rdfRDDres = rdfDatasetRes.javaRDD();
        JavaRDD<SingleTTL> srResRDD = rdfRDDres.map((Function<TTL, SingleTTL>) row -> {
            SingleTTL singleTTL = new SingleTTL(row);
            return singleTTL;
        });


        JavaRDD<SingleTTL> rdfsDSRDD = rdfsDS.javaRDD();
        rdfsDSRDD.saveAsTextFile(configObject.getDatapath() + "/datasource/");

        JavaRDD<SingleTTL> rdfsOrgRDD = rdfsOrg.javaRDD();
        rdfsOrgRDD.saveAsTextFile(configObject.getDatapath() + "/organization/");


        JavaRDD<SingleTTL> rdfsPrjOrg = rdfsPrj.javaRDD();
        rdfsPrjOrg.saveAsTextFile(configObject.getDatapath() + "/project/");

        //repartition
//        JavaPairRDD<SingleRDF, Integer> test = srResRDD.mapToPair((PairFunction<SingleRDF, SingleRDF, Integer>) s -> new Tuple2<>(s, s.getRdf().length()));
//        test.partitionBy(new SingleRDFPartitioner(configObject.getNumPartitions()));
//        JavaRDD<SingleRDF> outputRdd = test.map(x -> x._1);
        srResRDD.saveAsTextFile(configObject.getDatapath() + "/result/");


//        fs = FileSystem.get(sparkSession.sparkContext().hadoopConfiguration());
//        List<FileStatus> files = Arrays.asList(fs.globStatus(new Path(configObject.getDatapath() + "/datasource/" + "/part*")));
//        for(FileStatus file : files) {
//        	fs.rename(new Path(file.toString()), new Path(file.toString() + ".nt"));
//        }
//        
//        files = Arrays.asList(fs.globStatus(new Path(configObject.getDatapath() + "/organization/" + "/part*")));
//        for(FileStatus file : files) {
//        	fs.rename(new Path(file.toString()), new Path(file.toString() + ".nt"));
//        }
//        
//        files = Arrays.asList(fs.globStatus(new Path(configObject.getDatapath() + "/project/" + "/part*")));
//        for(FileStatus file : files) {
//        	fs.rename(new Path(file.toString()), new Path(file.toString() + ".nt"));
//        }
//        
//        files = Arrays.asList(fs.globStatus(new Path(configObject.getDatapath() + "/result/" + "/part*")));
//        for(FileStatus file : files) {
//        	fs.rename(new Path(file.toString()), new Path(file.toString() + ".nt"));
//        }
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


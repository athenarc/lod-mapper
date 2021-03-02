package org.imsi.lod_mapper.util;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.Serializable;


/**
 * Method: getCoutryURI
 * 
 * @author Giorgos Alexiou
 *
 */

public class MapCountries implements Serializable {

	private static final long serialVersionUID = 289662857896176908L;
	/*
 * This Method takes ISO ISO_3166_1 codes for Countries as input and returns the respective URI from dbpedia.org as output.
 * For example iso639_3 for Germany is "DE" and the respective URI from dbpedia.org is <http://dbpedia.org/resource/Germany>
 *
 * @param iso_3166_1_Code
 * @return country URI <String>
*/


	private InputStream is =  this.getClass().getClassLoader().getResourceAsStream("dbpedia_Countries.rdf");
	private   Model dbpediaCountries;

	{
		dbpediaCountries = ModelFactory.createDefaultModel().read(is, "RDF/XML");
	}

	public MapCountries(){
		
	}
	
	private Logger log = Logger.getLogger(this.getClass());
	public  String getCountryURI(String iso_3166_1_Code){
		String countryURI="";
		try{
			iso_3166_1_Code = iso_3166_1_Code.trim();
			org.apache.jena.query.ARQ.init();
			
			if(iso_3166_1_Code.equals("UK") || iso_3166_1_Code.equals("GB")) return "http://dbpedia.org/resource/United_Kingdom";
//			Model dbpediaCountries =  ModelFactory.createDefaultModel();
//            InputStream is =  ClassLoader.getSystemResourceAsStream("dbpedia_Countries.rdf");
//			dbpediaCountries.read(is, "RDF/XML");
			String isoURI = "http://dbpedia.org/resource/ISO_3166-1:"+iso_3166_1_Code;
			String query = "PREFIX dbpedia-owl:<http://dbpedia.org/ontology/> select distinct ?s ?r where {<"+isoURI+"> dbpedia-owl:wikiPageRedirects ?s}";
	//		System.out.println(query);
			QueryExecution queryExecution = QueryExecutionFactory.create(query,dbpediaCountries);
			ResultSet resultSet = queryExecution.execSelect();
			
			while (resultSet.hasNext()) {
				 QuerySolution qs = resultSet.next();
				 String uriString = qs.getResource("s").toString().trim();
				 countryURI=uriString;
			}
		}catch(Exception e){
			log.error("MapCountries error"+e.toString(),e);
			return  iso_3166_1_Code.trim();
		}
		return countryURI;	
		
	}

}

package org.imsi.lod_mapper.model;

import java.io.Serializable;
import java.util.Map;

public class TTL implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4897764969105211732L;
    private String id;
    private Map<String, String> predicateObject;
    private  String rdfType;

    public String getRdfType() {
        return rdfType;
    }

    public void setRdfType(String rdfType) {
        this.rdfType = rdfType;
    }



    public TTL() {}

    public TTL(String id, String predicate, String object) {
        this.id = id;
        this.predicateObject.put(predicate,object);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPredicateObject(String predicate, String object) {
        this.predicateObject.put(predicate,object);
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getPredicaateObject() {
        return predicateObject;
    }


    @Override
    public String toString() {
        String ttl = "<"+id+"> a <"+rdfType+">; ";
        for(String key : predicateObject.keySet()){
            if(predicateObject.keySet().iterator().hasNext()){
                ttl+="<"+key+">"+" <"+predicateObject.get(key)+">; ";
            }else{
                ttl+="<"+key+">"+" <"+predicateObject.get(key)+">.";
            }

        }
        return ttl;
    }


}

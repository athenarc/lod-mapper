package org.imsi.lod_mapper.model;

import java.io.Serializable;
import java.util.Map;

public class SingleTTL implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 289667890896176903L;


    private String ttl;
    public SingleTTL() {}

    public SingleTTL(String rdf) {this.ttl = ttl;}

    public SingleTTL(TTL ttlObject) {
        String ttlString = "<"+ttlObject.getId()+"> a <"+ttlObject.getRdfType()+">; ";
        Map<String,String> predicateObject = ttlObject.getPredicaateObject();
        for(String key : predicateObject.keySet()){
            if(predicateObject.keySet().iterator().hasNext()){
                ttlString+="<"+key+">"+" <"+predicateObject.get(key)+">; ";
            }else{
                ttlString+="<"+key+">"+" <"+predicateObject.get(key)+">.";
            }

        }

        this.ttl = ttlString;
    }
    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    @Override
    public String toString() {
        return  ttl;
    }
}
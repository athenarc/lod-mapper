package org.imsi.lod_mapper.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class SingleTTL implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 289667890896176903L;


    private String ttl;

    public SingleTTL() {
    }

    public SingleTTL(String ttl) {
        this.ttl = ttl;
    }

    public SingleTTL(TTL ttlObject) {
        String ttlString = "<" + ttlObject.getId() + "> a <" + ttlObject.getRdfType() + ">; ";
        Map<String, ArrayList<String>> predicateObject = ttlObject.getPredicaateObject();
        int counter = 0;
        int size = predicateObject.size();
        for (String key : predicateObject.keySet()) {
            ArrayList<String> objects = predicateObject.get(key);
            int obSize = objects.size();
            int obCounter = 0;
            for (String object : objects) {
                if (counter < size - 1 && obCounter < obSize - 1 && obSize > 1) {
                    if (counter < size - 1) {
                        ttlString += "<" + key + "> " + object + "; ";
                    } else {
                        ttlString += "<" + key + "> " + object + ".";
                    }
                }
                obCounter++;
            }
            counter++;
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
        return ttl;
    }
}
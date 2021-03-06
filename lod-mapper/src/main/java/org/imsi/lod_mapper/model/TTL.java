package org.imsi.lod_mapper.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TTL implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4897764969105211732L;
    private String id;
    private Map<String, ArrayList<String>> predicateObject = new HashMap<>();
    private String rdfType;

    public TTL() {
    }

    public TTL(String id, String predicate, ArrayList<String> object) {
        this.id = id;
        this.predicateObject.put(predicate, object);
    }

    public String getRdfType() {
        return rdfType;
    }

    public void setRdfType(String rdfType) {
        this.rdfType = rdfType;
    }

    public void setPredicateObject(String predicate, String object) {
        ArrayList<String> objects = new ArrayList<>();
        if (predicateObject.get(predicate) != null) {
            objects = predicateObject.get(predicate);
            objects.add(object);
            this.predicateObject.put(predicate, objects);
        } else {
            ArrayList<String> newObjects = new ArrayList<String>();
            newObjects.add(object);
            this.predicateObject.put(predicate, newObjects);
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, ArrayList<String>> getPredicaateObject() {
        return predicateObject;
    }


    @Override
    public String toString() {
        String ttl = "<" + id + "> a <" + rdfType + ">; ";
        int counter = 0;
        int size = predicateObject.size();
        for (String key : predicateObject.keySet()) {
            ArrayList<String> objects = predicateObject.get(key);
            int obSize = objects.size();
            int obCounter = 0;
            for (String object : objects) {
                if (counter < size - 1 && obCounter < obSize - 1) {
                    ttl += "<" + key + "> " + object + "; ";
                } else {
                    ttl += "<" + key + "> " + object + ".";
                }
                obCounter++;
            }
            counter++;
        }
        return ttl;
    }


}

package org.imsi.lod_mapper.util;

import org.apache.spark.Partitioner;

public class CustomPartitioner extends Partitioner {

    private final int numParts;
    public CustomPartitioner(int numParts) {
        this.numParts = numParts;
    }

    @Override
    public int numPartitions() {
        return numParts;
    }

    @Override
    public int getPartition(Object key) {

        int j =    ((String) key).indexOf("result/")+7;
        int i = ((String) key).indexOf(">");
        String id = ((String) key).substring(j,i);
        return  Math.abs(id.hashCode() % numParts);
    }
}

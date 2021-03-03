package org.imsi.lod_mapper.util;

import org.apache.spark.Partitioner;
import org.imsi.lod_mapper.model.SingleRDF;

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

        int j =    ((SingleRDF) key).getRdf().indexOf("result/")+7;
        int i = ((SingleRDF) key).getRdf().indexOf(">");
        String id = ((SingleRDF) key).getRdf().substring(j,i);
        return  Math.abs(id.hashCode() % numParts);
    }
}

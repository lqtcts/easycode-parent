package com.stream.storm.opaque;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec._InsDel;

import storm.trident.operation.TridentCollector;
import storm.trident.state.BaseQueryFunction;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

public class QueryLocation extends BaseQueryFunction<LocationDB, String> {  
    public List<String> batchRetrieve(LocationDB state, List<TridentTuple> inputs) {  
        List<Long> userIds = new ArrayList<Long>();  
        for(TridentTuple input: inputs) {  
            userIds.add(input.getLong(0));  
            System.out.println("QueryLocation.batchRetrieve()"+input);
        }  
        return state.bulkGetLocations(userIds);  
    }  
  
    public void execute(TridentTuple tuple, String location, TridentCollector collector) {  
        collector.emit(new Values(location));  
    }      
}

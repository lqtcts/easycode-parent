package com.stream.storm.opaque;

import java.util.List;

import storm.trident.state.State;

public class LocationDB implements State {  
    public void beginCommit(Long txid) {    
    	System.out.println("LocationDB.beginCommit()");
    }  
      
    public void commit(Long txid) {      
    	System.out.println("LocationDB.commit()");
    }  
      
    public void setLocationsBulk(List<Long> userIds, List<String> locations) {
    	System.out.println("LocationDB.setLocationsBulk()");
      // set locations in bulk  
    }  
      
    public List<String> bulkGetLocations(List<Long> userIds) {
    	System.out.println("LocationDB.bulkGetLocations()");
		return null;  
      // get locations in bulk  
    }  
}
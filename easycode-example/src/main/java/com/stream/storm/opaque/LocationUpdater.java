package com.stream.storm.opaque;

import java.util.ArrayList;
import java.util.List;

import storm.trident.operation.TridentCollector;
import storm.trident.state.BaseStateUpdater;
import storm.trident.tuple.TridentTuple;

public class LocationUpdater extends BaseStateUpdater<LocationDB> {
	public void updateState(LocationDB state, List<TridentTuple> tuples, TridentCollector collector) {
		List<Long> ids = new ArrayList<Long>();
		List<String> locations = new ArrayList<String>();
		for (TridentTuple t : tuples) {
			ids.add(t.getLong(0));
			locations.add(t.getString(1));
			System.out.println(t);
		}
		state.setLocationsBulk(ids, locations);
	}
}
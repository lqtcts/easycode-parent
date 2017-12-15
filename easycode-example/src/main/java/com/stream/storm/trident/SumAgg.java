package com.stream.storm.trident;

import storm.trident.operation.CombinerAggregator;
import storm.trident.tuple.TridentTuple;

public class SumAgg implements CombinerAggregator<Integer> {

	private static final long serialVersionUID = -6764153182395797637L;
	
	@Override
	public Integer init(TridentTuple tuple) {
		return tuple.getInteger(0);
	}

	@Override
	public Integer combine(Integer val1, Integer val2) {
		return val1 + val2;
	}

	@Override
	public Integer zero() {
		return 0;
	}

}
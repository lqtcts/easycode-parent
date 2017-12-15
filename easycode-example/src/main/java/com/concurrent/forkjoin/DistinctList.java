package com.concurrent.forkjoin;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

/**

 * 描述有返回值的任务

 * @author Administrator

 *

 */
public class DistinctList extends RecursiveTask<List<String>> {
/**
	 * 
	 */
	private static final long serialVersionUID = -1454629422929487537L;
//100-1000
	private static final int THRESHOLD = 1000;
	private int start;
	private int end;
	List<String> list;

	public DistinctList(List<String> list) {
		this.start = 0;
		this.end = list.size();
		this.list = list;
	}

	@Override
	protected List<String> compute() {
		if ((start - end) < THRESHOLD) {
			List<String> result =new ArrayList<>();
			for (String string : list) {
				if (!result.contains(string)) {
					result.add(string);
				}
			}
			return result;
		} else {
			int middle = (start + end) / 2;
			DistinctList left = new DistinctList(this.list.subList(start, middle));
			DistinctList right = new DistinctList(this.list.subList(middle + 1, end));
			List<String> a;
			List<String> b;
			try {
				a = left.fork().get();
				b = right.fork().get();
				a.addAll(b);
				return a;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
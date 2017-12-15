package com.lam;

import backtype.storm.command.list;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.l;

/**
 * java8
 * stream  流处理
 * Created by 0 on 2017/3/28.
 */
public class StreamTest {

    public static void main(String[] args) {

        List<Integer> test = new ArrayList<>();
        test.add(null);
        test.add(1);
        test.add(5);
        test.add(2);
        Stream<Integer> stream = test.stream();

        //过滤掉值为null的数据
        stream
               .filter(t -> t !=null)
               .forEach(aa -> System.out.println(aa));

        stream
                .distinct()
                .sorted()
                .filter(t -> t !=null)
                .map(he-> he +1)
                .forEach(aaa-> System.out.println(aaa));

        List<Integer> collect = stream.distinct().collect(Collectors.toList());



    }
}

package com.lam;

import java.util.HashMap;

/**
 * Created by 0 on 2017/2/20.
 */
public class Test
{

    public static void main(String[] args) {

        HashMap<String, Integer> a  = new HashMap<String, Integer>();
        a.put("111",222);
        new Test().test(a);


    }



    public <A extends String, B extends Object> HashMap<A, B> test(HashMap<A,B>  rest) {
        for (A a : rest.keySet()) {
            System.out.println(a);
        }
        return null;
    }
}

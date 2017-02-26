package com.djx;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dave on 2017/1/7.
 */
public class SingleTest
{

    @Test
    public void testJava() {
        double x  = 10.0;
        //41log((x+9)/10)+1
        double value = 500*Math.log10( (Math.pow(x, 0.25)+29)/30) + 5;
        System.out.println(value);

        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        System.out.println(list.subList(0,1));
        System.out.println(list.subList(0,2));
        System.out.println(list.subList(0, Math.min(list.size(), 10)));
    }
}

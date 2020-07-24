/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluxtion.articles.fxportfolio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author greg higgins <greg.higgins@v12technology.com>
 */
public class GenericsTest {
    
    
    @Test
    public void test1(){
        ArrayList<? super Object> list1 = new ArrayList<>();
        Object d = list1.get(1);
        list1.add(1);
        list1.add("test");
        
        
        ArrayList<ArrayList<?>> list = new ArrayList<ArrayList<?>>();
        
        
        list.add(list1);
    }
    
    @Test
    public void testNumber(){
        ArrayList<? super A> list1 = new ArrayList<>();
        list1.add(new B());
        Object get = list1.get(0);
        
        
        ArrayList<A> aList = new ArrayList<A>();
        aList.add(new B());
        A a = aList.get(0);
        System.out.println("A:" + a);
        
        
//        list1.add(new Object());
//        list1.add(1);
//        list1.add(Double.valueOf(3));
//        Object eee = list1.get(0);
//        
//        List<? super Number> nums;
//        nums = list1;
//        nums.add(6);
//        
//        Number d = list1.get(0);
//        Object n = nums.get(0);
//
//        
//        List<Integer> ints = new ArrayList<Integer>();
//        ints.add(2);
//
//        
//        List<? extends Number> nl = new ArrayList<>();
//        Number n1 = nl.get(0);
//        nl.add(new Integer(2));
        
        
    
    }
    
    
    public class A{}
    public class B extends A{}
}

package com.unj.dubbotest.provider.impl;  
  
import java.util.ArrayList;  
import java.util.List;  
  
import com.unj.dubbotest.provider.DemoService;
import com.unj.dubbotest.provider.User;

public class DemoServiceImpl implements DemoService {  
  
    public String sayHello(String name) {  
        return "Hello " + name;  
    }  
  
    public List getUsers() {  
        List list = new ArrayList();  
        User u1 = new User();
        u1.setName("hejingyuan");  
        u1.setAge(20);  
        u1.setSex("f");  
  
        User u2 = new User();  
        u2.setName("xvshu");  
        u2.setAge(21);  
        u2.setSex("m");  
  
          
        list.add(u1);  
        list.add(u2);  
          
        return list;  
    }  
}  
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.junit;

import com.util.func.GsonUtils;
import com.util.func.ReflectUtils;
import com.util.junit.bean.ReflectInnerObj;
import com.util.junit.bean.ReflectObj;

/**
 * @author nguyenpk
 * @since 2021-10-21
 */
public class ReflectTest {
    public static void main(String[] args) throws Exception {
        ReflectObj reflectObj = new ReflectObj();
        reflectObj.setName("name 1");
        ReflectInnerObj reflectInnerObj = new ReflectInnerObj();
        reflectInnerObj.setNameInner("name inner 1");
        reflectObj.setReflectInnerObj(reflectInnerObj);

        ReflectObj reflectObjClone = (ReflectObj) ReflectUtils.getInstance().clone(reflectObj);

        //change value of reflectObj
        reflectObj.setName("name 2");
        reflectInnerObj.setNameInner("name inner 2");

        System.out.println("reflectObj: " + GsonUtils.getInstance().to(reflectObj));//reflectObj: {"name":"name 2","reflectInnerObj":{"nameInner":"name inner 2"}}
        System.out.println("reflectObjClone: " + GsonUtils.getInstance().to(reflectObjClone));//reflectObjClone: {"name":"name 1","reflectInnerObj":{"nameInner":"name inner 2"}}


    }
}

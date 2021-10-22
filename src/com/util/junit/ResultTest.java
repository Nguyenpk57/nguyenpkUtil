/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.junit;

import com.util.junit.bean.ResponseObj;
import com.util.func.GsonUtil;
import com.util.result.Result;

/**
 * @author nguyenpk
 * @since 2021-10-21
 */
public class ResultTest {

    public static void main(String[] args) {
        Result result = new Result(true, "R0000", "R0000");

        //check message config in Language file
        result = new Result(false, "R0002", "R0002");
        Result prRs1 = new Result("en", result);
        System.out.println("Result prRs1: " + GsonUtil.getInstance().to(prRs1));

        //check argument in message content
        result = new Result(false, "R0002", "R0002", "param_1", "param_2");
        Result prRs2 = new Result("en", result);
        System.out.println("Result prRs2: " + GsonUtil.getInstance().to(prRs2));

        //response object in last layer
        result.put("className", "TestResponse");
        ResponseObj res = new ResponseObj(result);
        res.setName((String) result.get("className"));
        System.out.println("TestResponse res: " + GsonUtil.getInstance().to(res));
        
    }
}

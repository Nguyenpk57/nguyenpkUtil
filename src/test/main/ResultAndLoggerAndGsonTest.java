package main;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.util.bean.test.ResponseObj;
import com.util.func.GsonUtils;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;
import com.util.func.response.Result;

/**
 * @author nguyenpk
 * @since 2021-10-21
 */
public class ResultAndLoggerAndGsonTest {

    private ILogger logger = LoggerImpl.getInstance(this.getClass());

    public ResultAndLoggerAndGsonTest() {
    }

    public static void main(String[] args) {
        ResultAndLoggerAndGsonTest resultTest = new ResultAndLoggerAndGsonTest();
        resultTest.test();
    }

    public void test() {
        Result result = new Result(true, "R0000", "R0000");

        //check message config in Language file
        result = new Result(false, "R0002", "R0002");
        Result prRs1 = new Result("en", result);
        logger.infos("Result prRs1: ", GsonUtils.getInstance().to(prRs1));

        //check argument in message content
        result = new Result(false, "R0002", "R0002", "param_1", "param_2");
        Result prRs2 = new Result("en", result);
        logger.infos("Result prRs2: ", GsonUtils.getInstance().to(prRs2));

        //response object in last layer
        result.put("className", "responseLastLayer");
        ResponseObj res = new ResponseObj(result);
        res.setName((String) result.get("className"));
        logger.infos("TestResponse res: ", GsonUtils.getInstance().to(res));
    }
}

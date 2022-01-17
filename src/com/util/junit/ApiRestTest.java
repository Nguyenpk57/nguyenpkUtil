/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.junit;

import com.util.func.api.rest.IRequest;
import com.util.func.api.rest.RequestImpl;
import com.util.func.GsonUtils;
import com.util.junit.bean.ApiRestTestResponse;

import java.util.HashMap;

/**
 * @author nguyenpk
 * @since 2021-10-21
 */
public class ApiRestTest {

    /**
     * URL: http://10.121.14.195:8054/mibitel/anni7Th/buyPackageLife
     */
    /**
     * REQUEST: {"msisdn": "910248192", "packageCode" : "ANI10", "privateKey" : "gSmP5[k'j3S(A%z4" }
     */
    /**
     * RESPONSE: { "responseCode": 0, "responseMessage":"Success", "responseError": [] }
     */
    public static void main(String[] args) throws Exception {
        IRequest request = new RequestImpl();
        HashMap entity = new HashMap();
        entity.put("msisdn", "910248192");
        entity.put("packageCode", "ANI10");
        entity.put("privateKey", "gSmP5[k'j3S(A%z4");

        String response = request
                .setUrl("http://10.121.14.195:8054/mibitel/anni7Th/buyPackageLife")
                .setMethod(IRequest.POST)
                .setHeader("Content-Type", "application/json")
                .setEntity(GsonUtils.getInstance().to(entity))
                .execute();
        ApiRestTestResponse resObj = (ApiRestTestResponse) GsonUtils.getInstance().from(response, ApiRestTestResponse.class);
        System.out.println("ApiRestTest responseCode: " + GsonUtils.getInstance().to(resObj));
    }
}

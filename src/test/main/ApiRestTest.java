package main;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.util.func.api.rest.IRequest;
import com.util.func.api.rest.RequestImpl;
import com.util.func.GsonUtils;

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
//        IRequest request = new RequestImpl();
//        HashMap entity = new HashMap();
//        entity.put("msisdn", "910248192");
//        entity.put("packageCode", "ANI10");
//        entity.put("privateKey", "gSmP5[k'j3S(A%z4");
//
//        String response = request
//                .setUrl("http://10.121.14.195:8054/mibitel/anni7Th/buyPackageLife")
//                .setMethod(IRequest.POST)
//                .setHeader("Content-Type", "application/json")
//                .setEntity(GsonUtils.getInstance().to(entity))
//                .execute();
//        ApiRestTestResponse resObj = (ApiRestTestResponse) GsonUtils.getInstance().from(response, ApiRestTestResponse.class);
//        System.out.println("main.ApiRestTest responseCode: " + GsonUtils.getInstance().to(resObj));
//        setUrl("http://api.ipapi.com/api/132.157.131.238?access_key=b87d31f32f07eaf55579b3c089a2c9ba&format=1c")

        IRequest request = new RequestImpl();
        String response = request.setUrl("http://api.ipapi.com/api/132.157.131.238")
                .setMethod(IRequest.GET)
                .setHeader("Content-Type", "application/json")
                .setProxy("10.121.127.204", "3128")
                .addParam("access_key", "b87d31f32f07eaf55579b3c089a2c9ba")
                .addParam("format", "1c")
                .execute();

        System.out.println("main.ApiRestTest responseCode: " + GsonUtils.getInstance().to(response));

    }
}

package com.util.logger.export;

import com.util.func.api.rest.IRequest;
import com.util.func.api.rest.RequestImpl;
import com.util.func.DateTimeUtils;
import com.util.func.GsonUtils;
import com.util.bean.test.ApiRestTestResponse;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;

import java.util.Date;
import java.util.HashMap;

public class TransactionLogExport {

    private static ILogger logger = LoggerImpl.getInstance(TransactionLogExport.class);

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
        HashMap entity = new HashMap();
        entity.put("msisdn", "910248192");
        entity.put("packageCode", "ANI10");
        entity.put("privateKey", "gSmP5[k'j3S(A%z4");

        String request = GsonUtils.getInstance().to(entity);
        String startDate = DateTimeUtils.getInstance().formatddMMyyyyHHmmss(new Date());
        Long startTime = System.currentTimeMillis();

        String response = new RequestImpl()
                .setUrl("http://10.121.14.195:8054/mibitel/anni7Th/buyPackageLife")
                .setMethod(IRequest.POST)
                .setHeader("Content-Type", "application/json")
                .setEntity(GsonUtils.getInstance().to(entity))
                .execute();
        ApiRestTestResponse resObj = (ApiRestTestResponse) GsonUtils.getInstance().from(response, ApiRestTestResponse.class);

        String endDate = DateTimeUtils.getInstance().formatddMMyyyyHHmmss(new Date());
        Long elapsedTime = System.currentTimeMillis() - startTime;

        exportTransactionLogs(request, response, startDate, endDate, elapsedTime);
    }

    public static void exportTransactionLogs(String request, String response, String startDate, String endDate, Long elapsedTime) {
        logger.infosTransaction(request, response, startDate, endDate, elapsedTime);
    }
}

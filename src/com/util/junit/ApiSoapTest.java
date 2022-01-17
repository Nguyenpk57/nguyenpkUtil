package com.util.junit;

import com.util.func.api.soap.CommonWebService;
import com.util.func.GsonUtils;
import com.util.junit.bean.ApiSoapTestResponse;

/**
 * @author nguyenpk
 * @since 2021-10-22
 */
public class ApiSoapTest {

    public static void main(String[] args) throws Exception {
        CommonWebService commonWebService = new CommonWebService();
        StringBuilder request = new StringBuilder()
                .append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.register.cm.viettel.com/\">")
                .append("   <soapenv:Header/>")
                .append("   <soapenv:Body>")
                .append("      <ws:getShopOfStaff>")
                .append("         <staffCode>THANHNV</staffCode>")
                .append("         <wsUsername>test</wsUsername>")
                .append("         <wsPassword>test</wsPassword>")
                .append("      </ws:getShopOfStaff>")
                .append("   </soapenv:Body>")
                .append("</soapenv:Envelope>");

        String url = "http://10.121.14.195:8358/VAS_CM_WS/SubscriberBusinessWS?wsdl";
        int recvTimeout = 60000; //milisecond
        int connectTimeout = 60000; //milisecond
        String response = commonWebService.sendRequest(request.toString(), url, recvTimeout, connectTimeout);
        System.out.println(response);
        response = response.replaceAll("<ns2:getShopOfStaffResponse xmlns:ns2=\"http://ws.register.cm.viettel.com/\">", "<getShopOfStaffResponse>")
                .replaceAll("</ns2:getShopOfStaffResponse>", " </getShopOfStaffResponse>");
        ApiSoapTestResponse res = (ApiSoapTestResponse) CommonWebService.unmarshal(response, ApiSoapTestResponse.class);

        System.out.println("ApiSoapTestResponse res: " + GsonUtils.getInstance().to(res));

        /*** <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
        *   <S:Body>
        *      <ns2:getShopOfStaffResponse xmlns:ns2="http://ws.register.cm.viettel.com/">
        *         <return>
        *            <code>R000</code>
        *            <message>SUCCESS</message>
        *            <shopBO>
        *               <channelTypeId>3</channelTypeId>
        *               <shopAddress>3 2 1 - LIMA LIMA LIMA</shopAddress>
        *               <shopCode>BR_TEST</shopCode>
        *               <shopId>1002076</shopId>
        *               <shopName>BR_TEST_NAME</shopName>
        *            </shopBO>
        *         </return>
        *      </ns2:getShopOfStaffResponse>
        *   </S:Body>
        *</S:Envelope>
        */
    }
}

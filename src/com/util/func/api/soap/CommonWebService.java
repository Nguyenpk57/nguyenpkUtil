package com.util.func.api.soap;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.dom.DOMSource;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class CommonWebService {

    public static String marshal(Object object) throws Exception {
        if (object == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
        Marshaller unmarshaller = jaxbContext.createMarshaller();
        unmarshaller.marshal(object, sw);
        return sw.toString();
    }

    public static Object unmarshal(String responseBody, Class aClass) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(aClass);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return unmarshaller.unmarshal(new StringReader(responseBody));
    }

    public static String marshals(Object object) throws Exception {
        if (object == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
        Marshaller unmarshaller = jaxbContext.createMarshaller();
        unmarshaller.marshal(object, sw);
        return sw.toString().replaceAll("<\\?xml version=\"1\\.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>", "");
    }

    public String sendRequest(String requestContent, String wsdlUrl, int recvTimeout, int connectTimeout) throws Exception {
        HttpClient httpclient = new HttpClient();
        httpclient.getParams().setParameter("http.socket.timeout", recvTimeout);
        httpclient.getParams().setParameter("http.connection.timeout", connectTimeout);
        PostMethod post = new PostMethod(wsdlUrl);
        String result = "";
        try {
            RequestEntity entity = new StringRequestEntity(requestContent, "text/xml", "UTF-8");
            post.setRequestEntity(entity);
            httpclient.executeMethod(post);
            post.getResponseBodyAsString();
            result = parseResult(post.getResponseBodyAsString());
        } catch (Exception ex) {
            throw ex;
        } finally {
        }
        return result;
    }

    public String sendRequestHttps(String requestContent, String wsdlUrl, int recvTimeout, int connectTimeout, boolean isParse) throws Exception {
        HttpClient httpclient = new HttpClient();
        httpclient.getParams().setParameter("http.socket.timeout", recvTimeout);
        httpclient.getParams().setParameter("http.connection.timeout", connectTimeout);
        PostMethod post = new PostMethod(wsdlUrl);
        String result = "";
        String responseContent = null;
        try {
            String scheme = "https";
            Protocol baseHttps = Protocol.getProtocol(scheme);
            int defaultPort = baseHttps.getDefaultPort();

            ProtocolSocketFactory baseFactory = baseHttps.getSocketFactory();
            ProtocolSocketFactory customFactory = new CustomHttpsSocketFactory(baseFactory);

            Protocol customHttps = new Protocol(scheme, customFactory, defaultPort);
            Protocol.registerProtocol(scheme, customHttps);
            RequestEntity entity = new StringRequestEntity(requestContent, "text/xml", "UTF-8");
            post.setRequestEntity(entity);
            httpclient.executeMethod(post);
            responseContent = post.getResponseBodyAsString();

            if (isParse) {
                result = parseResult(responseContent);
            } else {
                result = responseContent;
            }

        } catch (Exception ex) {
            throw ex;
        }
        return result;
    }

    public String parseResult(String response) throws Exception {
        String result = "";
        try {
            MessageFactory mf = MessageFactory.newInstance();
            SOAPMessage soapMsg = mf.createMessage();
            SOAPPart part = soapMsg.getSOAPPart();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(response));
            Document document = builder.parse(is);
            DOMSource domSource = new DOMSource(document);
            part.setContent(domSource);
            SOAPElement resResultSet = (SOAPElement) soapMsg.getSOAPBody().getChildElements().next();
            DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();
            LSSerializer serializer = domImplLS.createLSSerializer();
            result = serializer.writeToString(resResultSet);
        } catch (Exception ex) {
            throw ex;
        }
        return result;
    }

    /** template example
    <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
       <S:Body>
          <ns2:gwOperationResponse xmlns:ns2="http://webservice.bccsgw.viettel.com/">
             <Result>
                <error>0</error>
                <description>success</description>
                <return/>
                <original><![CDATA[<?xml version='1.0' encoding='UTF-8'?><S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"><S:Body><ns2:CheckDebtOfStaffResponse xmlns:ns2="http://com.viettel.smws/"><return><deuda><detalle /><moneda>SOLES</moneda><monto>0.0</monto><sistema>BCCS</sistema></deuda><deuda><detalle>Detalle: Máy tính xách tay HP 15-DB0009LA bộ xử lý RIZEN 3 - RAM 8GB, HDD 1T, 15.6"|Laptop HP 15-DB0009LA Processor RIZEN 3 - RAM 8GB , HDD 1T , 15.6 Inch. Cantidad: 1.0. Estado: Đang sử dụng.
                </detalle><sistema>SmartOffice</sistema></deuda><errorCode>R002</errorCode><errorMsg>EXIST DEBT IN SYSTEM</errorMsg><staffCode>THUANNH_VTP</staffCode><staffName>Nguyen Huu Thuan</staffName></return></ns2:CheckDebtOfStaffResponse></S:Body></S:Envelope>]]></original>
             </Result>
          </ns2:gwOperationResponse>
       </S:Body>
    </S:Envelope>
    */

//    public String parseBccsGwResult(String response) throws Exception {
//        String result = "";
//        try {
//            MessageFactory mf = MessageFactory.newInstance();
//            // Create a message.  This example works with the SOAPPART.
//            SOAPMessage soapMsg = mf.createMessage();
//            SOAPPart part = soapMsg.getSOAPPart();
//
//            //InputStream is = new ByteArrayInputStream(response.getBytes());
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            dbf.setNamespaceAware(true);
//            DocumentBuilder builder = dbf.newDocumentBuilder();
//            InputSource is = new InputSource(new StringReader(response));
//            Document document = builder.parse(is);
//            DOMSource domSource = new DOMSource(document);
//            part.setContent(domSource);
//
//            DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();
//            LSSerializer serializer = domImplLS.createLSSerializer();
//            Object element = soapMsg.getSOAPBody().getChildElements().next();
//            if (element instanceof com.sun.xml.messaging.saaj.soap.impl.TextImpl) {
//                com.sun.xml.messaging.saaj.soap.impl.TextImpl resResultSet = (com.sun.xml.messaging.saaj.soap.impl.TextImpl) soapMsg.getSOAPBody().getChildElements().next();
//                result = serializer.writeToString(resResultSet);
//            } else {
//                SOAPElement resResultSet = (SOAPElement) soapMsg.getSOAPBody().getChildElements().next();
//                result = serializer.writeToString(resResultSet);
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return result;
//    }
}

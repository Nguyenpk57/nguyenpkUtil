/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func.api.soap;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.net.SocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class CustomHttpsSocketFactory implements SecureProtocolSocketFactory {

    private final SecureProtocolSocketFactory base;

    public CustomHttpsSocketFactory(ProtocolSocketFactory base) {
        if (base == null || !(base instanceof SecureProtocolSocketFactory)) {
            throw new IllegalArgumentException();
        }
        this.base = (SecureProtocolSocketFactory) base;
    }

    private Socket stripSSLv3(Socket socket) {
        if (!(socket instanceof SSLSocket)) {
            return socket;
        }
        SSLSocket sslSocket = (SSLSocket) socket;
        List<String> list = new ArrayList<String>();
        for (String s : sslSocket.getSupportedProtocols()) {
            if (!s.startsWith("SSLv2") && !s.startsWith("SSLv3")) {
                list.add(s);
            }
        }
        sslSocket.setEnabledProtocols(list.toArray(new String[list.size()]));
        return sslSocket;
    }

    private TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };
        return trustAllCerts;
    }

    public Socket createSocket(String host, int port) throws IOException,
            UnknownHostException {
        TrustManager[] trustAllCerts = getTrustManager();
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            SocketFactory socketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
            return socketFactory.createSocket(host, port);
        } catch (Exception ex) {
            throw new UnknownHostException("Problems to connect " + host + ex.toString());
        }
    }

    public Socket createSocket(Socket socket, String host, int port,
            boolean flag) throws IOException, UnknownHostException {
        TrustManager[] trustAllCerts = getTrustManager();
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            SocketFactory socketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
            return socketFactory.createSocket(host, port);
        } catch (Exception ex) {
            throw new UnknownHostException("Problems to connect " + host + ex.toString());
        }
    }

    public Socket createSocket(String host, int port, InetAddress clientHost,
            int clientPort) throws IOException, UnknownHostException {
        TrustManager[] trustAllCerts = getTrustManager();
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            SocketFactory socketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
            return socketFactory.createSocket(host, port, clientHost, clientPort);
        } catch (Exception ex) {
            throw new UnknownHostException("Problems to connect " + host + ex.toString());
        }
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress,
            int localPort, HttpConnectionParams arg4) throws IOException,
            UnknownHostException, ConnectTimeoutException {
        TrustManager[] trustAllCerts = getTrustManager();
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            SocketFactory socketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
            return socketFactory.createSocket(host, port);
        } catch (Exception ex) {
            throw new UnknownHostException("Problems to connect " + host + ex.toString());
        }
    }
}

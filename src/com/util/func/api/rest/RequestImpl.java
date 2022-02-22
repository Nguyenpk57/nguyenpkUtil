/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func.api.rest;

import com.util.func.StringUtils;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class RequestImpl extends BaseRequest {

    protected HttpRequestBase request;
    protected CloseableHttpClient client;
    protected boolean initialization;

    public RequestImpl() {
        initialization();
    }

    public RequestImpl(ILogger logger) {
        this.logger = logger;
        initialization();
    }

    private void initialization() {
        try {
            if (logger == null) {
                this.logger = LoggerImpl.getInstance(this.getClass());
            }
            if (properties == null) {
                properties = new HashMap();
            }

            try {
                if (client == null) {
                    client = HttpClients.createDefault();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            initialization = true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public IRequest clear() {
        properties.clear();
        return this;
    }

    @Override
    public IRequest setProxy(String host, String port) {
        HashMap proxy = (HashMap) properties.get(PROXY);
        if (proxy == null) {
            proxy = new HashMap();
        }
        proxy.put("host", host);
        proxy.put("port", port);
        properties.put(PROXY, proxy);
        return this;
    }

    @Override
    public IRequest setUrl(String url) {
        properties.put(URL, url);
        return this;
    }

    @Override
    public IRequest setMethod(String method) {
        properties.put(METHOD, method);
        return this;
    }

    @Override
    public IRequest setHeader(String name, String value) {
        HashMap headers = (HashMap) properties.get(HEADERS);
        if (headers == null) {
            headers = new HashMap();
        }
        headers.put(name, value);
        properties.put(HEADERS, headers);
        return this;
    }

    @Override
    public IRequest setEntity(String entity) {
        properties.put(ENTITY, entity);
        return this;
    }

    @Override
    public IRequest addPathParams(String... values) {
        List<String> lstValue = Arrays.asList(values);
        if (lstValue == null || lstValue.isEmpty()) {
            return this;
        }
        List<String> params = (List<String>) properties.get(PATH_PARAMS);
        if (params == null) {
            params = new ArrayList();
        }
        params.addAll(lstValue);
        properties.put(PATH_PARAMS, params);
        return this;
    }

    @Override
    public IRequest addParam(String name, String value) {
        HashMap params = (HashMap) properties.get(PARAMS);
        if (params == null) {
            params = new HashMap();
        }
        params.put(name, value);
        properties.put(PARAMS, params);
        return this;
    }

    //<editor-fold defaultstate="collapsed" desc="execute">
    @Override
    public String execute() throws Exception {
        if (!initialization) {
            initialization();
        }
        buildRequest();
        HttpResponse response = client.execute(request);
        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }

    //<editor-fold defaultstate="collapsed" desc="buildRequest">
    private void buildRequest() {
        if (GET.equals(properties.get(METHOD))) {
            request = new HttpGet(buildUrl());
            buildProxy();
            buildHeaders();
        }
        if (POST.equals(properties.get(METHOD))) {
            request = new HttpPost(buildUrl());
            buildProxy();
            buildHeaders();
            buildEntity();
        }
        if (PUT.equals(properties.get(METHOD))) {
            request = new HttpPut(buildUrl());
            buildProxy();
            buildHeaders();
            buildEntity();
        }
        if (DELETE.equals(properties.get(METHOD))) {
            request = new HttpDelete(buildUrl());
            buildProxy();
            buildHeaders();
        }
    }

    private String buildUrl() {
        String url = buildPath();
        if (url == null) {
            return url;
        }
        url = url.trim();
        if (url.isEmpty()) {
            return url;
        }
        HashMap params = (HashMap) properties.get(PARAMS);
        if (params == null || params.isEmpty()) {
            return url;
        }
        int i = 0;
        String joiner = "&";
        StringBuilder sb = new StringBuilder("?");
        List<String> values = new ArrayList<String>();
        for (Object name : params.keySet()) {
            if (i > 0) {
                sb.append(joiner);
            }
            sb.append(name).append("={").append(i++).append("}");
            values.add((String) params.get(name));
        }
        url = StringUtils.getInstance().getFormat(url + sb.toString()).format(values.toArray());
        return url;
    }

    private String buildPath() {
        String url = (String) properties.get(URL);
        if (url == null) {
            return url;
        }
        url = url.trim();
        if (url.isEmpty()) {
            return url;
        }
        List<String> params = (List<String>) properties.get(PATH_PARAMS);
        if (params == null || params.isEmpty()) {
            return url;
        }
        url = StringUtils.getInstance().getFormat(url).format(params.toArray());
        return url;
    }

    private void buildProxy() {
        HashMap proxy = (HashMap) properties.get(PROXY);
        if (proxy == null) {
            return;
        }
        String host = org.apache.commons.lang3.StringUtils.trim((String) proxy.get("host"));
        if (host == null || host.isEmpty()) {
            return;
        }

        int port = Integer.valueOf((String) proxy.get("port"));
        request.setConfig(RequestConfig.custom().setProxy(new HttpHost(host, port, "http")).build());
    }

    private void buildHeaders() {
        HashMap headers = (HashMap) properties.get(HEADERS);
        if (headers == null) {
            return;
        }
        for (Object name : headers.keySet()) {
            request.setHeader((String) name, (String) headers.get(name));
        }
    }

    private void buildEntity() {
        String entity = org.apache.commons.lang3.StringUtils.trim((String) properties.get(ENTITY));
        if (entity == null || entity.isEmpty()) {
            return;
        }
        if (POST.equals(properties.get(METHOD))) {
            ((HttpPost) request).setEntity(new StringEntity(entity, ContentType.APPLICATION_JSON));
        }
        if (PUT.equals(properties.get(METHOD))) {
            ((HttpPut) request).setEntity(new StringEntity(entity, ContentType.APPLICATION_JSON));
        }
    }
    //</editor-fold>
    //</editor-fold>

    @Override
    public void close() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            client = null;
        }
        try {
            if (request != null) {
                request.abort();
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            request = null;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func.api.rest;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public interface IRequest {

    //<editor-fold defaultstate="collapsed" desc="keys">
    String PROXY = "proxy";
    String URL = "url";
    String METHOD = "method";
    String HEADERS = "headers";
    String ENTITY = "entity";
    String PATH_PARAMS = "path_params";
    String PARAMS = "params";
    String GET = "get";
    String POST = "post";
    String PUT = "put";
    String DELETE = "delete";
    //</editor-fold>

    IRequest clear();

    IRequest setProxy(String host, Integer port);

    IRequest setUrl(String url);

    IRequest setMethod(String method);

    IRequest setHeader(String name, String value);

    IRequest setEntity(String entity);

    IRequest addPathParams(String... values);

    IRequest addParam(String name, String value);

    String execute() throws Exception;

    void close();

    String getRequest();
}

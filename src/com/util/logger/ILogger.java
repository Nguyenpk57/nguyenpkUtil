/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.logger;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */

public interface ILogger {

    Object getLogger();

    String getName();

    //<editor-fold defaultstate="collapsed" desc="log">
    void error(Object obj);

    void error(Object obj, Throwable t);

    void info(Object obj);

    void info(Object obj, Throwable t);

    void warn(Object obj);

    void warn(Object obj, Throwable t);

    void debug(Object obj);

    void debug(Object obj, Throwable t);

    void fatal(Object obj);

    void fatal(Object obj, Throwable t);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="logs">
    void errors(Object... values);

    void errors(Throwable t, Object... values);

    void infos(Object... values);

    void infos(Throwable t, Object... values);

    void warns(Object... values);

    void warns(Throwable t, Object... values);

    void debugs(Object... values);

    void debugs(Throwable t, Object... values);

    void fatals(Object... values);

    void fatals(Throwable t, Object... values);
    //</editor-fold>
}


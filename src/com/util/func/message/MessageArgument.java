/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func.message;

import com.util.func.message.MessageUtils;
import com.util.logger.ILogger;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class MessageArgument {
    
    /**
     * true: in file Language; false: String message
     */
    protected boolean isMessage;
    /**
     * is String or Object MessageArgument
     */
    private Object message;
    private Object[] arguments;

    public MessageArgument(boolean isMessage, Object message, Object... arguments) {
        this.isMessage = isMessage;
        this.message = message;
        this.arguments = arguments;
    }

    public Object getMessage(ILogger logger, String language) {
        if (isMessage) {
            return MessageUtils.getInstance(logger).getMessage(language, (String) message, arguments);
        }
        return message;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}

package com.util.junit;

import com.util.bean.Constants;
import com.util.func.message.MessageUtils;

public class MessageUtilsTest {
    public static void main(String[] args) throws Exception {
        String message = MessageUtils.getInstance().getMessage(Constants.LANGUAGE_EN, "R0000");
        System.out.println("MessageUtils message: " + message);

        String messageArguments = MessageUtils.getInstance().getMessage(Constants.LANGUAGE_EN, "R0002", "param_1", "param_2");
        System.out.println("MessageUtils messageArguments: " + messageArguments);
    }
}

package com.util.logger.export.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLogBean {

    private String request;
    private String response;
    private String startDate; //yyyy-MM-dd HH:mm:ss
    private String endDate; //yyyy-MM-dd HH:mm:ss
    private Long elapsedTime; //milisecond

}

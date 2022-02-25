/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.bean.test;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author nguyenpk
 * @since 2021-10-21
 */
@Getter
@Setter
public class ApiRestTestResponse {
    String responseCode;
    String responseMessage;
    List<Object> responseError;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.junit.bean;

import com.util.func.response.Result;
import lombok.Getter;
import lombok.Setter;

/**
 * @author nguyenpk
 * @since 2021-10-21
 */
@Getter
@Setter
public class ResponseObj extends Result {

    String name;

    public ResponseObj() {
    }

    public ResponseObj(Result rs) {
        super(rs);
    }
}

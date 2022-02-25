/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.bean.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author nguyenpk
 * @since 2021-10-22
 */
@Getter
@Setter

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "shopOfStaffResponse", propOrder = {
    "code",
    "message",
    "shopBO"
})
public class ShopOfStaffResponse {

    protected String code;
    protected String message;
    protected ShopBO shopBO;
}

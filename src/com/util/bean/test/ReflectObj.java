/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.bean.test;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author nguyenpk
 */
@Getter
@Setter
public class ReflectObj {
    private String name;
    private ReflectInnerObj reflectInnerObj;
}

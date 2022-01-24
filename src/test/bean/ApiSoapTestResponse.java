/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
@XmlType(name = "getShopOfStaffResponse", propOrder = {
    "_return"
})
@XmlRootElement(name = "getShopOfStaffResponse")
public class ApiSoapTestResponse {
    
    @XmlElement(name = "return")
    protected ShopOfStaffResponse _return;
}

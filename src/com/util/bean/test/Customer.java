package com.util.bean.test;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private transient String address;

    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + ", address=" + address + "]";
    }

}

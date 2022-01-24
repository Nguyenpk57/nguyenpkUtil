package com.util.func.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

/**
 * @author nguyenpk
 * @since 2021-12-23
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Param extends BaseEntity {
    private Integer paramId;
    private String paramGroup;
    private String paramCode;
    private String paramName;
    private String paramValue;
    private Integer status;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
}

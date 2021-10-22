package com.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nguyenpk
 * @since 2021-10-22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {

    String table() default "";

    /**
     * Ten Table trung voi keyword cua Database
     *
     * @return
     */
    boolean keyword() default false;
}

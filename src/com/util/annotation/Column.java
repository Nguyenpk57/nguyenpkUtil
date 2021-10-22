package com.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nguyenpk
 * @since 2021-10-22
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * Column name
     *
     * @return
     */
    String name() default "";

    /**
     * La column Primary key hay khong
     *
     * @return
     */
    boolean id() default false;

    /**
     * Gia tri duy nhat
     *
     * @return
     */
    boolean unique() default false;

    /**
     * Duoc phep NULL khong
     *
     * @return
     */
    boolean nullAble() default true;

    /**
     * Do dai toi da
     *
     * @return
     */
    int length() default 0;

    /**
     * So ky tu truoc dau .
     *
     * @return
     */
    int precision() default 0;

    /**
     * So ky tu sau dau .
     *
     * @return
     */
    int scale() default 0;

    /**
     * Ten sequence dung de lay gia tri cho cac column number
     *
     * @return
     */
    String sequence() default "";

    /**
     * Column trung voi keyword cua Database
     *
     * @return
     */
    boolean keyword() default false;

    /**
     * Mo ta ve column
     *
     * @return
     */
    String description() default "";

    boolean log() default true;
}

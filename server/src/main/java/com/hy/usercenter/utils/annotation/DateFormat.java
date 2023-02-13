package com.hy.usercenter.utils.annotation;

import java.lang.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JsonFormat(pattern = "yyyy-MM-dd")
public @interface DateFormat {
    String[] value();
}
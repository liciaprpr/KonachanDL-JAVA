package com.konachan.bean;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class BeanModel implements Serializable {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

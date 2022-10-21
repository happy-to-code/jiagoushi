package com.yida.customer;


import com.yida.customer.annotation.InitialResolver;

public interface InitialParser {

    boolean isMatch(Class clazz);


    Object getDefaultValue(Class clazz, InitialResolver initialResolver);
}

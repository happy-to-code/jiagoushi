package com.yida.customer.realize;


import com.yida.customer.InitialParser;
import com.yida.customer.RequestUtils;
import com.yida.customer.annotation.InitialResolver;
import com.yida.customer.constant.HtichConstants;

public class CurrentUserInitialParser implements InitialParser {
    @Override
    public boolean isMatch(Class clazz) {
        return clazz.isAssignableFrom(String.class);
    }

    @Override
    public Object getDefaultValue(Class clazz, InitialResolver initialResolver) {
        return RequestUtils.getRequestHeader(HtichConstants.HEADER_ACCOUNT_KEY);
    }
}

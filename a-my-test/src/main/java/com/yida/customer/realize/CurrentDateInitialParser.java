package com.yida.customer.realize;




import com.yida.customer.InitialParser;
import com.yida.customer.annotation.InitialResolver;

import java.util.Date;

public class CurrentDateInitialParser implements InitialParser {
    @Override
    public boolean isMatch(Class clazz) {
        return clazz.isAssignableFrom(Date.class);
    }

    @Override
    public Object getDefaultValue(Class clazz, InitialResolver initialResolver) {
        return new Date();
    }
}

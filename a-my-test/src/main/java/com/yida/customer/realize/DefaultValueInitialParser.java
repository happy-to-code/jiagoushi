package com.yida.customer.realize;


import com.yida.customer.InitialParser;
import com.yida.customer.annotation.InitialResolver;
import com.yida.util.ReflectUtils;

public class DefaultValueInitialParser implements InitialParser {
    @Override
    public boolean isMatch(Class clazz) {
        return ReflectUtils.isBasicTypes(clazz);
    }

    @Override
    public Object getDefaultValue(Class clazz, InitialResolver initialResolver) {
        Object defValue = null;
        try {
            defValue = ReflectUtils.getDefValue(clazz, initialResolver.def());
        } catch (Exception e) {

        }
        return defValue;
    }
}

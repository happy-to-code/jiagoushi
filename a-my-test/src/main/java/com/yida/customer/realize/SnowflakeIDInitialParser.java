package com.yida.customer.realize;


import com.yida.customer.InitialParser;
import com.yida.customer.SnowflakeIdWorker;
import com.yida.customer.annotation.InitialResolver;

/**
 * 雪花ID生成器
 */
public class SnowflakeIDInitialParser implements InitialParser {
    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(10, 11);

    @Override
    public boolean isMatch(Class clazz) {
        return clazz.isAssignableFrom(String.class);
    }

    @Override
    public Object getDefaultValue(Class clazz, InitialResolver initialResolver) {
        return String.valueOf(snowflakeIdWorker.nextId());
    }
}

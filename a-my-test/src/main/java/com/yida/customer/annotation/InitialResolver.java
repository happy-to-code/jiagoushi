package com.yida.customer.annotation;



import com.yida.enums.InitialResolverType;
import com.yida.groups.Group;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Documented
public @interface InitialResolver {
    InitialResolverType resolver();

    Class<?>[] groups() default Group.All.class;

    //默认值
    String def() default "";
}

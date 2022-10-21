package com.yida.annotation_d;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MapperAutoConfigureRegistrar.class)
public class MapperAutoConfig {
}
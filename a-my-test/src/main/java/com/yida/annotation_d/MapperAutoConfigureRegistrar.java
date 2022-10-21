package com.yida.annotation_d;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class MapperAutoConfigureRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
	
	private ResourceLoader resourceLoader;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		MapperBeanDefinitionScanner scanner = new MapperBeanDefinitionScanner(registry, false);
		scanner.setResourceLoader(resourceLoader);
		scanner.registerFilters();
		scanner.addIncludeFilter(new AnnotationTypeFilter(Mapper.class));
		scanner.doScan("com.yida.annotation_d");
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
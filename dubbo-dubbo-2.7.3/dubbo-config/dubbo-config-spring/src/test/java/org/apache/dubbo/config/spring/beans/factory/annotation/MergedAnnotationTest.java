/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.config.spring.beans.factory.annotation;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.config.spring.annotation.merged.MergedReference;
import org.apache.dubbo.config.spring.annotation.merged.MergedService;
import org.apache.dubbo.config.spring.api.DemoService;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class MergedAnnotationTest {
	
	@Test
	public void testMergedReference() {
		Field field = ReflectionUtils.findField(MergedAnnotationTest.TestBean1.class, "demoService");
		Reference reference = AnnotatedElementUtils.getMergedAnnotation(field, Reference.class);
		Assert.assertEquals("dubbo", reference.group());
		Assert.assertEquals("1.0.0", reference.version());
		
		Field field2 = ReflectionUtils.findField(MergedAnnotationTest.TestBean2.class, "demoService");
		Reference reference2 = AnnotatedElementUtils.getMergedAnnotation(field2, Reference.class);
		Assert.assertEquals("group", reference2.group());
		Assert.assertEquals("2.0", reference2.version());
	}
	
	@Test
	public void testMergedService() {
		Service service1 = AnnotatedElementUtils.getMergedAnnotation(MergedAnnotationTest.DemoServiceImpl1.class, Service.class);
		Assert.assertEquals("dubbo", service1.group());
		Assert.assertEquals("1.0.0", service1.version());
		
		Service service2 = AnnotatedElementUtils.getMergedAnnotation(MergedAnnotationTest.DemoServiceImpl2.class, Service.class);
		Assert.assertEquals("group", service2.group());
		Assert.assertEquals("2.0", service2.version());
	}
	
	@MergedService
	public static class DemoServiceImpl1 {
	}
	
	@MergedService(group = "group", version = "2.0")
	public static class DemoServiceImpl2 {
	}
	
	private static class TestBean1 {
		@MergedReference
		private DemoService demoService;
	}
	
	private static class TestBean2 {
		@MergedReference(group = "group", version = "2.0")
		private DemoService demoService;
	}
	
}

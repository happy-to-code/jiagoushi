package com.yida.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

@SpringBootTest
class RedisServiceTest {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Test
	public void findByKey() {
		Object s = redisTemplate.opsForValue().get("1");
		System.out.println("s = " + s);
	}
	
	@Test
	public void setValue() {
		redisTemplate.opsForValue().set("key123", "abc");
	}
	
	@Test
	public void hgetValue() {
		Object resultMap = redisTemplate.opsForHash().entries("boy");
		System.out.println("o = " + resultMap);
	}
	
	@Test
	public void hashTest() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("key1", "value1");
		map.put("key2", "value2");
		map.put("key3", "value3");
		map.put("key4", "value4");
		map.put("key5", "value5");
		redisTemplate.opsForHash().putAll("map1", map);
		
		Map<Object, Object> resultMap = redisTemplate.opsForHash().entries("map1");
		System.out.println("resultMap:" + resultMap);
		
		List<Object> reslutMapList = redisTemplate.opsForHash().values("map1");
		System.out.println("resulreslutMapListtMap:" + reslutMapList);
		
		Set<Object> resultMapSet = redisTemplate.opsForHash().keys("map1");
		System.out.println("resultMapSet:" + resultMapSet);
		
		String value = (String) redisTemplate.opsForHash().get("map1", "key1");
		System.out.println("value:" + value);
	}
	
	@Test
	public void testList() {
		List<String> list1 = new ArrayList<String>();
		list1.add("a1");
		list1.add("a2");
		list1.add("a3");
		list1.add("b1");
		
		List<String> list2 = new ArrayList<String>();
		list2.add("b1");
		list2.add("b2");
		list2.add("b3");
		list2.add("a2");
		
		redisTemplate.opsForList().leftPush("listkey1", list1);
		redisTemplate.opsForList().rightPush("listkey2", list2);
		// List<String> resultList1 = (List<String>) redisTemplate.opsForList().leftPop("listkey1");
		// List<String> resultList2 = (List<String>) redisTemplate.opsForList().rightPop("listkey2");
		// System.out.println("resultList1:" + resultList1);
		// System.out.println("resultList2:" + resultList2);
	}
}
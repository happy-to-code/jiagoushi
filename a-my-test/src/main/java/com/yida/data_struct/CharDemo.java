package com.yida.data_struct;

import java.util.*;

public class CharDemo {
	public static void main(String[] args) {
		
		List<String> strs = Arrays.asList("bdddddddddd", "bbbbbbbbbbc");
		if (strs == null || strs.size() == 0) {
			// return new ArrayList<List<String>>();
		}
		
		Map<String, List<String>> map = new HashMap<>();
		for (String str : strs) {
			// 针对每个字符串生成一个key,保证同样的字符组成的字符串生成的key一样
			String key = genKey(str);
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList());
			}
			map.get(key).add(str); // map.get(key) 获取集合，然后将str增加到集合中
		}
		// return new ArrayList(map.values());
		
	}
	
	
	public static String genKey(String s) {
		int[] table = new int[26];
		
		char[] charList = s.toCharArray();
		// System.out.println("charList = " + charList.toString());
		for (char c : charList) {
			// System.out.println("char = " + c);
			table[c - 'a']++;
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 26; i++) {
			sb.append(table[i]);
		}
		System.out.println(s+"------>" + sb.toString()+table);
		return sb.toString();
	}
	
	
}

//

package com.yida.data_struct;

import java.util.HashMap;
import java.util.Map;

public class CharDemo2 {
	public static void main(String[] args) {
		String s = "AABBC";
		int[] dict = new int[128];
		
		for (int i = 0; i < s.length(); i++) {
			System.out.println("s.charAt(i) = " + s.charAt(i));
			dict[s.charAt(i)]++;
		}
		
		System.out.println("dict = " + dict);
		for (int i : dict) {
			System.out.print(i+" ");
		}
		
		
	}
}

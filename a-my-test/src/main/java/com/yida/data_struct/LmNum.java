package com.yida.data_struct;

public class LmNum {
	public static void main(String[] args) {
		String s = "LVIII";
		int i = new LmNum().romanToInt(s);
		System.out.println("i = " + i);
	}
	public int romanToInt(String s) {
		int sum = 0;
		int preNum = getValue(s.charAt(0)); // 字符串第一个字符
		for (int i = 1; i < s.length(); i++) {
			int currentNum = getValue(s.charAt(i));
			if (preNum < currentNum) {
				sum -= preNum;
			} else {
				sum += preNum;
			}
			preNum = currentNum;
		}
		sum += preNum;
		return sum;
	}
	
	private int getValue(char c) {
		switch (c) {
			case 'I':
				return 1;
			case 'V':
				return 5;
			case 'X':
				return 10;
			case 'L':
				return 50;
			case 'C':
				return 100;
			case 'D':
				return 500;
			case 'M':
				return 1000;
			default:
				return 0;
		}
	}
}

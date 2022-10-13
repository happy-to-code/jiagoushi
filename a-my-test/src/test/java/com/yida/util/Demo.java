package com.yida.util;

public class Demo {
	public static void main(String[] args) {
		int x = 121;
		System.out.println(ishw(x));
	}
	
	public static boolean ishw(int x) {
		if(x<0||( x % 10 == 0 && x != 0)){
			return false;
		}
		
		// 将x的后半段翻转，构造一个新的数和x前半段比较
		int revertNum = 0;
		
		while(x > revertNum){
			revertNum = revertNum * 10 + x % 10;
			
			x = x / 10;
		}
		
		return x == revertNum || x == revertNum / 10;
	}
}

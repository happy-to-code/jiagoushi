package com.yida.data_struct;

public class Num2List {
	public static void main(String[] args) {
		Integer a = 12789;
		while (a > 0) {
			Integer b = a % 10;
			System.out.print(b + "\t");
			
			a = a / 10;
		}
	}
}

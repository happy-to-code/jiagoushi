package com.yida;

public class Min {
	public static void main(String[] args) {
		int a = 1;
		int b = 11;
		
		System.out.println("Math.min(a, b) = " + Math.min(a, b));
		
		int[] height = new int[3];
		System.out.println(height.length);
	}
}

class Solution {
	public int maxArea(int[] height) {
		int left = 0;
		int right = height.length - 1;
		
		int area = 0;
		
		while (left < right) {
			int temp = (right - left) * Math.min(height[left], height[right]);
			if (temp > area) {
				area = temp;
			}
			if (height[left] > height[right]) {
				right--;
			} else {
				left++;
			}
		}
		return area;
	}
}

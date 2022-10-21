package com.yida.data_struct;

import java.util.PriorityQueue;

public class Middle {
	public static void main(String[] args) {
		int[] nums1 = new int[]{1, 5, 7};
		int[] nums2 = new int[]{3, 9};
		
		Middle middle = new Middle();
		
		double arrays = middle.findMedianSortedArrays(nums1, nums2);
		System.out.println("arrays = " + arrays);
	}
	
	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
		//m 构造新的排序数组
		int[] data = new int[nums1.length + nums2.length];
		int k = 0;//新数组下标,填充值
		// 定义两个指针分别对应两个数组的下标
		int m = 0;
		int n = 0;
		while (m < nums1.length && n < nums2.length) {
			data[k++] = nums1[m] > nums2[n] ? nums2[n++] : nums1[m++];
		}
		while (m < nums1.length) {
			data[k++] = nums1[m++];
		}
		while (n < nums2.length) {
			data[k++] = nums2[n++];
		}
		int len = data.length;
		if (len % 2 == 0) {
			return (data[(len / 2) - 1] + data[len / 2]) / 2.0;
		} else {
			return data[len / 2];
		}
	}
	
	
	
}

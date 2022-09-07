package com.yida.util;

class Solution2 {
	public static void merge(int[] nums1, int m, int[] nums2, int n) {
		// 双指针
		// 先将nums1数据拷贝一份出来
		int[] nums1_copy = new int[m];
		System.arraycopy(nums1, 0, nums1_copy, 0, m);
		
		// 循环nums1 和nums2  两个指针依次向后移动并且比较大小
		int i = 0;  // nums1_copy
		
		int j = 0; // nums2
		int p = 0; // nums1
		
		while (i < m && j < n) {
			nums1[p++] = nums1_copy[i] > nums2[j] ? nums2[j++] : nums1_copy[i++];
		}
		// 看哪个数组还有剩余
		if (i < m) {
			// nums1_copy 还有剩余
			System.arraycopy(nums1_copy, i, nums1, p, m - i);
		}
		if (j < n) {
			System.arraycopy(nums2, j, nums1, p, n - j);
		}
	}
	
	public static void main(String[] args) {
		int[] nums1 = new int[]{1, 2, 3, 0, 0, 0};
		int[] nums2 = new int[]{2, 5, 6};
		
		Solution2.merge(nums1, 6, nums2, 3);
		
		
		
	}
}
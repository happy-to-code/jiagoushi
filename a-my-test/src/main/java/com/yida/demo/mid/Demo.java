package com.yida.demo.mid;

public class Demo {
	public static void main(String[] args) {
		int[] nums = new int[]{1, 3, 5, 6};
		int target = 7;
		int i = searchInsert(nums, target);
		System.out.println("i = " + i);
	}
	
	public static int searchInsert(int[] nums, int target) {
		int max = nums.length - 1;
		int min = 0;
		int mid;
		int ans = max + 1;
		while (min <= max) {
			mid = (min + max) >> 1;
			if (target <= nums[mid]) {
				ans = mid;
				max = mid - 1;
			} else {
				min = mid + 1;
			}
		}
		return ans;
		
	}
}

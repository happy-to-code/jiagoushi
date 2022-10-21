package com.yida.annotation_d3;

import java.util.*;
import java.util.stream.Collectors;

public class Demo {
	public static void main(String[] args) {
		int[] nums = new int[]{0,1};
		Demo d = new Demo();
		int i = d.missingNumber(nums);
		System.out.println("i = " + i);
		
	}
	
	public int missingNumber(int[] nums) {
		int length = nums.length;
		List<Integer> ints = new ArrayList<>(length);
		for (int num : nums) {
			ints.add(num);
		}
		
		for (int i = 0; i <= length; i++) {
			if (!ints.contains(i)) {
				return i;
			}
		}
		
		return 0;
	}
	
	
	public int[] intersection(int[] nums1, int[] nums2) {
		List<Integer> result = new ArrayList<>();
		
		List<Integer> temp2 = Arrays.stream(nums2).boxed().collect(Collectors.toList());
		HashSet<Integer> h = new HashSet<>(temp2);
		
		
		Map<Integer,Integer> map1 = new HashMap<>();
		for(int num : nums1){
			map1.put(num,num);
		}
		
		for(Integer i:h){
			if(map1.containsKey(i)){
				result.add(i);
			}
		}
		
		return result.stream().mapToInt(Integer::valueOf).toArray();
	}
}

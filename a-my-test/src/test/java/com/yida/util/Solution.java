package com.yida.util;

class ListNode {
	int val;
	ListNode next;
	
	ListNode(int x) {
		val = x;
		next = null;
	}
}


public class Solution {
	public static void main(String[] args) {
		
		ListNode node3 = new ListNode(-4);
		
		
		ListNode node2 = new ListNode(0);
		node2.next = node3;
		
		ListNode node1 = new ListNode(2);
		node1.next = node2;
		
		
		ListNode head = new ListNode(3);
		head.next = node1;
		
		node3.next = node1;
		
		boolean b = Solution.hasCycle(head);
		System.out.println(b);
		
		
	}
	
	public static boolean hasCycle(ListNode head) {
		// 快慢指针
		// 快指针每次走两步   慢指针每次走一步
		// 如果有换  那么 快指针和慢指针必定会相遇
		
		if (head == null || head.next == null) { // 空节点  or  节点只有一个  那么必定形成不了环
			return false;
		}
		
		// 快慢指针都是从头节点开始走
		ListNode fast = head;
		ListNode slow = head;
		
		while (true) {
			fast = fast.next.next;
			slow = slow.next;
			
			if (fast == null || fast.next == null) { // 等于null 肯定不等于slow
				return false;
			}
			
			if (fast == slow) { // 相遇
				return true;
			}
		}
		
	}
}
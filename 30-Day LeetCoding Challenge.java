/*************
	Week 1
*************/

/* Single Number */

// Use XOR
class Solution {
    public int singleNumber(int[] nums) {
        int single = 0;
		for (int i : nums)
		{
			single = single ^ i;
		}
		return single;
    }
}

/* Happy Number */

// Brute Calculate
class Solution {
    public boolean isHappy(int n)
	{
		int[] tmp = new int[730];
		while (n != 1)
		{
			int t = 0;
			while (n > 0)
			{
				t += (n % 10) * (n % 10);
				n /= 10;
			}
			n = t;
			if (tmp[n] == 0)
				tmp[n] = 1;
			else
				return false;
		}
		return true;
	}
}

/* Maximum Subarray */

// The array may has no positive interger
class Solution {
    public int maxSubArray(int[] nums)
	{
		int sum = nums[0];
		int i = 0;
		for (i = 0; i < nums.length; i++)
		{
			if (nums[i] < 0 && nums[i] > sum)
				sum = nums[i];
			else if (nums[i] >= 0)
			{
				sum = nums[i];
				int tmp = nums[i];
				for (int j = i + 1; j < nums.length; j++)
				{
					tmp += nums[j];
					if (tmp > sum)
						sum = tmp;
					else if (tmp < 0)
						tmp = 0;
				}
				return sum;
			}
		}
		return sum;
	}
}

/* Move Zeroes */

// Just go through the array, put the number in its position
class Solution {
    public void moveZeroes(int[] nums)
	{
		int flag = 0;
		for (int i = 0; i < nums.length; i++)
		{
			if (nums[i] != 0)
			{
				nums[flag] = nums[i];
				flag++;
			}
		}
		for (int j = flag; j < nums.length; j++)
		{
			nums[j] = 0;
		}
	}
}

/* Best Time to Buy and Sell Stock II */

// Calculate profit between every two days and add up the positve profits
class Solution {
    public int maxProfit(int[] prices)
	{
		int profit = 0;
		for (int i = 0; i < prices.length - 1; i++)
		{
			int tmp = prices[i + 1] - prices[i];
			if (tmp > 0)
				profit += tmp;
		}
		return profit;
	}
}

/* Group Anagrams */

// Sort every string to group anagrams, use the sorted string as the key of the hash map
class Solution {
    public List<List<String>> groupAnagrams(String[] strs)
	{
		List<List<String>> group = new ArrayList<List<String>>();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String s : strs)
		{
			char[] chs = s.toCharArray();
			Arrays.sort(chs);
			String tmps = new String(chs);
			List<String> list = map.get(tmps);
			if (list == null)
			{
				list = new ArrayList<String>();
				list.add(s);
				map.put(tmps, list);
			}
			else
				list.add(s);
		}
		for (List<String> l : map.values())
		{
			group.add(l);
		}
		return group;
	}
}

/* Counting Elements */

// Because of the constraints, we can use simple array to record
class Solution {
    public int countElements(int[] arr)
	{
		int[] flag = new int[1002];
		int count = 0;
		for (int i : arr)
		{
			flag[i + 1]++;
		}
		for (int j : arr)
		{
			if (flag[j] != 0)
			{
				count += flag[j];
				flag[j] = 0;
			}
		}
		return count;
	}
}

/*************
	Week 2
*************/

/* Middle of the Linked List */

// Use differential step
class Solution {
    public ListNode middleNode(ListNode head) {
		ListNode ans = head;
		boolean flag = true;
		while(head.next != null)
		{
			head = head.next;
			if(flag)
				ans = ans.next;
			flag = !flag;
		}
        return ans;
    }
}

/* Backspace String Compare */

// From back to front check the equivalence, every time check one character
class Solution {
    public boolean backspaceCompare(String S, String T)
	{
		boolean b = true;
		int indexS = S.length() - 1, indexT = T.length() - 1;
		while (indexS >= 0 || indexT >= 0)
		{
			char chS = '#';
			int countS = 0;
			if (indexS >= 0)
			{
				chS = S.charAt(indexS--);
				while (chS == '#' || countS > 0)
				{
					if (chS == '#')
						countS++;
					else
						countS--;
					if (indexS < 0)
					{
						chS = '#';
						break;
					}
					else
						chS = S.charAt(indexS--);
				}
			}
			char chT = '#';
			int countT = 0;
			if (indexT >= 0)
			{
				chT = T.charAt(indexT--);
				while (chT == '#' || countT > 0)
				{
					if (chT == '#')
						countT++;
					else
						countT--;
					if (indexT < 0)
					{
						chT = '#';
						break;
					}
					else
						chT = T.charAt(indexT--);
				}
			}
			if (chS != chT)
				return false;
		}
		return b;
	}
}
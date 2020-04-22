/*************
	Week 1
*************/

/* Single Number */
// Use XOR
class Solution
{
	public int singleNumber(int[] nums)
	{
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
class Solution
{
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
class Solution
{
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
class Solution
{
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
class Solution
{
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
class Solution
{
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
class Solution
{
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
class Solution
{
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
class Solution
{
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

/* Min Stack */
// Every element has a min value record
class MinStack
{
	private Stack<Integer[]> data;
	private int min;

	public MinStack()
	{
		data = new Stack<Integer[]>();
		min = Integer.MAX_VALUE;
	}

	public void push(int x)
	{
		if (x < min)
			min = x;
		Integer[] tmp = { x, min };
		data.add(tmp);
	}

	public void pop()
	{
		data.pop();
		if (data.size() == 0)
			min = Integer.MAX_VALUE;
		else
			min = data.lastElement()[1];
	}

	public int top()
	{ return data.lastElement()[0]; }

	public int getMin()
	{ return data.lastElement()[1]; }
}

/* Diameter of Binary Tree */
// Every path can be apart into two parts on the highest node of the path,
// so for every node, calculate the longest part of left an right,
// and return to its father node the longer part + 1,
// calculate the left + right which is the longest path go through this node,
// and after run for all nodes, we have the longest path value
class Solution
{
	private int max = 0;

	public int diameterOfBinaryTree(TreeNode root)
	{
		if (root != null)
			maxPath(root);
		return max;
	}
	
	public int maxPath(TreeNode node)
	{
		int tmpLeft = (node.left == null) ? 0 : (maxPath(node.left) + 1);
		int tmpRight = (node.right == null) ? 0 : (maxPath(node.right) + 1);
		int tmp = tmpLeft + tmpRight;
		if(tmp > max)
			max = tmp;
		return Math.max(tmpLeft, tmpRight);
	}
}

/* Last Stone Weight */
// Sort and find the two heaviest stones
class Solution
{
	public int lastStoneWeight(int[] stones)
	{
		int length = stones.length;
		if (length == 0)
			return 0;
		else if (length == 1)
			return stones[0];
		else
		{
			Arrays.sort(stones);
			while (stones[length - 2] != 0)
			{
				int x = stones[length - 1];
				int y = stones[length - 2];
				stones[length - 1] = Math.abs(x - y);
				stones[length - 2] = 0;
				Arrays.sort(stones);
			}
			return stones[length - 1];
		}
	}
}

/* Contiguous Array */
// Optimized brute search, not good
class Solution
{
	public int findMaxLength(int[] nums)
	{
		int t = 0;
		for (int n : nums)
		{
			t += n;
		}
		int length = Math.min(t, nums.length - t) * 2;
		while (length > 1)
		{
			int p = 0, q = length - 1, tmp = 0;
			for(int i = p; i < q; i++)
			{
				tmp += nums[i];
			}
			while (q < nums.length)
			{
				tmp += nums[q];
				if (length == tmp * 2)
					return length;
				tmp -= nums[p];
				p++;
				q++;
			}
			length -= 2;
		}
		return 0;
	}
}

/* Perform String Shifts */
// Suppose we have a window slide on the string,
// then we can decide the final string based on the window position
class Solution
{
	public String stringShift(String s, int[][] shift)
	{
		int length = s.length(), index = 0;
		for (int[] ii : shift)
		{
			if (ii[0] == 0)
				index += ii[1];
			else
				index -= ii[1];
		}
		if (index < 0)
			index += (Math.abs(index) / length + 1) * length;
		else if (index >= length)
			index -= (index / length) * length;
		s += s;
		return s.substring(index, index + length);
	}
}

/*************
	Week 3
*************/

/* Product of Array Except Self */
// The output[i] can be product of two parts,
// the product of the left numbers and
// the product of the roght numbers,
// so scan two times
class Solution
{
	public int[] productExceptSelf(int[] nums)
	{
		int[] output = new int[nums.length];
		int tmp = 1;
		for (int i = 0; i < nums.length; i++)
		{
			output[i] = tmp;
			tmp *= nums[i];
		}
		tmp = 1;
		for (int i = nums.length - 1; i >= 0; i--)
		{
			output[i] *= tmp;
			tmp *= nums[i];
		}
		return output;
	}
}

/* Valid Parenthesis String */
// Check if the string is valid from the first char,
// and every time add one char then check again
class Solution
{
	public boolean checkValidString(String s)
	{
		if (s.length() == 0)
			return true;
		else if (s.length() == 1)
		{
			if (s.charAt(0) == '*')
				return true;
			else
				return false;
		}
		else
		{
			int leftStar = 0, rightStar = 0, countLeft = 0;
			for (int i = 0; i < s.length(); i++)
			{
				char ch = s.charAt(i);
				if (ch == '(')
					countLeft++;
				else if (ch == '*')
				{
					if (countLeft > 0)
					{
						countLeft--;
						leftStar++;
					}
					else
						rightStar++;
				}
				else
				{
					if (countLeft > 0)
						countLeft--;
					else if (leftStar > 0)
					{
						leftStar--;
						rightStar++;
					}
					else if (rightStar > 0)
						rightStar--;
					else
						return false;
				}
			}
			if (countLeft > 0)
				return false;
			else
				return true;
		}
	}
}

/* Number of Islands */
// When meet a island, find all its land and mark it as water
class Solution
{
	public int numIslands(char[][] grid)
	{
		if (grid.length == 0)
			return 0;
		int count = 0, h = grid.length, v = grid[0].length;
		for (int i = 0; i < h; i++)
		{
			for (int j = 0; j < v; j++)
			{
				if (grid[i][j] == '1')
				{
					count++;
					grid[i][j] = '0';
					ArrayList<Integer[]> l = new ArrayList<Integer[]>();
					l.add(new Integer[] { i, j });
					while (!l.isEmpty())
					{
						Integer[] ii = l.remove(0);
						int p = ii[0], q = ii[1];
						if ((p - 1 >= 0) && (grid[p - 1][q] == '1'))
						{
							grid[p - 1][q] = '0';
							l.add(new Integer[] { p - 1, q });
						}
						if ((q - 1 >= 0) && (grid[p][q - 1] == '1'))
						{
							grid[p][q - 1] = '0';
							l.add(new Integer[] { p, q - 1 });
						}
						if ((p + 1 < h) && (grid[p + 1][q] == '1'))
						{
							grid[p + 1][q] = '0';
							l.add(new Integer[] { p + 1, q });
						}
						if ((q + 1 < v) && (grid[p][q + 1] == '1'))
						{
							grid[p][q + 1] = '0';
							l.add(new Integer[] { p, q + 1 });
						}
					}
				}
			}
		}
		return count;
	}
}

/* Minimum Path Sum */
// Greedy step by step
class Solution
{
	public int minPathSum(int[][] grid)
	{
		int x = grid.length, y = grid[0].length;
		for (int i = 0; i < x; i++)
		{
			for (int j = 0; j < y; j++)
			{
				if (i == 0)
				{
					if (j != 0)
						grid[i][j] += grid[i][j - 1];
				}
				else
				{
					if (j == 0)
						grid[i][j] += grid[i - 1][j];
					else
						grid[i][j] += (grid[i - 1][j] > grid[i][j - 1]) ? grid[i][j - 1] : grid[i - 1][j];
				}
			}
		}
		return grid[x - 1][y - 1];
	}
}
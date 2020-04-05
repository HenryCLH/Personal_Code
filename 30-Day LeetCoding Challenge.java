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
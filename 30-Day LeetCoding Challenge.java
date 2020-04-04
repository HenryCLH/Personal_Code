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
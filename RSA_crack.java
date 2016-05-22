package hello;

import java.math.BigInteger;

public class hello {

	public static void main(String[] args) {
		BigInteger N = new BigInteger("123456");
		BigInteger e1 = new BigInteger("123");
		BigInteger e2 = new BigInteger("1");
		
	}
	public static BigInteger reverse1(BigInteger e, BigInteger n){
		if((e.compareTo(BigInteger.ONE)==1)&&(n.compareTo(BigInteger.ONE)==1)){
			BigInteger k = reverse2(e, n.remainder(e));
			BigInteger d = k.multiply(n).add(BigInteger.ONE).divide(e);
			return d;
		}
		else{
			BigInteger tmp = new BigInteger("1");
			return tmp;
		}
	}
	public static BigInteger reverse2(BigInteger e, BigInteger n){
		if((n.compareTo(BigInteger.ONE)==1)&&(e.compareTo(BigInteger.ONE)==1)){
			BigInteger d = reverse1(e.remainder(n), n);
			BigInteger k = d.multiply(e).subtract(BigInteger.ONE).divide(n);
			return k;
		}
		else{
			BigInteger tmp = new BigInteger("1");
			return tmp;
		}
	}
}

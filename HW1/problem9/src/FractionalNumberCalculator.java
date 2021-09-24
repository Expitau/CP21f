public class FractionalNumberCalculator {
	public static void printCalculationResult(String equation) {
		// DO NOT change the skeleton code.
		// You can add codes anywhere you want
		String[] parameters = equation.split(" ");
		FractionalNumber num1 = FractionalNumber.stringToNumber(parameters[0]);
		FractionalNumber num2 = FractionalNumber.stringToNumber(parameters[2]);
		char opnd = parameters[1].charAt(0);
		FractionalNumber ret = calculate(num1, num2, opnd);
		System.out.println(ret);
	}

	private static FractionalNumber calculate(FractionalNumber n1, FractionalNumber n2, char opnd){
		switch(opnd){
			case '+':
				return n1.add(n2);
			case '-':
				return n1.sub(n2);
			case '*':
				return n1.mul(n2);
			case '/':
				return n1.div(n2);
		}
		return new FractionalNumber();
	}
}

class FractionalNumber {
	private int numerator;
	private int denominator;

	public static FractionalNumber stringToNumber(String expr){
		expr = expr.strip();
		expr += " ";
		int stk_size=0, int_temp=0;
		int[] stk = {0, 1};
		for(char it : expr.toCharArray()){
			if('0' <= it && it <= '9') int_temp = int_temp * 10 + it - '0';
			else{
				stk[stk_size++] = int_temp;
				int_temp = 0;
			}
		}
		return new FractionalNumber(stk[0], stk[1]);
	}

	FractionalNumber(){
		numerator = 0;
		denominator = 1;
	}

	FractionalNumber(int n, int d){
		numerator = n;
		denominator = d;
		simplifyNumber();
	}

	private int gcd(int a, int b){
		if(a < 0) a = -a;
		if(b < 0) b = -b;

		if(a == 0 || b == 0){
			if(a != 0) return a;
			else if(b != 0) return b;
			return 0;
		}

		while(b != 0){
			int tmp = a%b;
			a = b;
			b = tmp;
		}
		return a;
	}

	private void simplifyNumber(){
		int g = gcd(numerator, denominator);
		if(g == 0) return;
		numerator /= g;
		denominator /= g;
		if(denominator < 0){
			numerator *= -1;
			denominator *= -1;
		}
	}

	@Override
	public String toString(){
		if(denominator != 1)
			return String.format("%d/%d", numerator, denominator);
		else
			return String.format("%d", numerator);
	}

	public FractionalNumber add(FractionalNumber p){
		int n = numerator*p.denominator + p.numerator*denominator;
		int d = denominator*p.denominator;
		return new FractionalNumber(n, d);
	}
	public FractionalNumber sub(FractionalNumber p){
		int n = numerator*p.denominator - p.numerator*denominator;
		int d = denominator*p.denominator;
		return new FractionalNumber(n, d);
	}
	public FractionalNumber mul(FractionalNumber p){
		int n = numerator*p.numerator;
		int d = denominator*p.denominator;
		return new FractionalNumber(n, d);
	}
	public FractionalNumber div(FractionalNumber p){
		int n = numerator*p.denominator;
		int d = denominator*p.numerator;
		return new FractionalNumber(n, d);
	}
}

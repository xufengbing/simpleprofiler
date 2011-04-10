package demo.enums;

public enum Operation {
	PLUS {
		double eval(double x, double y) {
			return x + y;
		}
	},
	MINUS {
		double eval(double x, double y) {
			return x - y;
		}
	},
	TIMES {
		double eval(double x, double y) {
			return x * y;
		}
	},
	DIVIDE {
		double eval(double x, double y) {
			return x / y;
		}
	};

	// Do arithmetic op represented by this constant
	abstract double eval(double x, double y);

	public static void main(String args[]) {
		double x = 4;
		double y = 2;
		for (Operation op : Operation.values())
			System.out.printf("%f %s %f = %f%n", x, op, y, op.eval(x, y));
	}

}
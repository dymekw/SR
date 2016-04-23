package sr.ice.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Demo.CalcPrx;
import Demo.CalcPrxHelper;
import Demo.Operation;
import Ice.AsyncResult;

public class Client {
	private static final List<Operation> VALUES = Collections.unmodifiableList(Arrays.asList(Operation.values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();

	public static void main(String[] args) {
		int status = 0;
		Ice.Communicator communicator = null;

		try {
			communicator = Ice.Util.initialize(args);

			// Ice.ObjectPrx base = communicator.propertyToProxy("Calc1.Proxy");
			Ice.ObjectPrx base1 = communicator.stringToProxy(
					"calc/calc11:tcp -h localhost -p 10000:udp -h localhost -p 10000:ssl -h localhost -p 10001");

			CalcPrx calc1 = CalcPrxHelper.checkedCast(base1);
			if (calc1 == null)
				throw new Error("Invalid proxy");


			for(int i=0; i<10; i++) {
				simpleTask(calc1);
			}

		} catch (Ice.LocalException e) {
			e.printStackTrace();
			status = 1;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			status = 1;
		}
		if (communicator != null) {
			// Clean up
			//
			try {
				communicator.destroy();
			} catch (Exception e) {
				System.err.println(e.getMessage());
				status = 1;
			}
		}
		System.exit(status);
	}

	private static void simpleTask(CalcPrx calc) {
		Operation op = VALUES.get(RANDOM.nextInt(SIZE));
		int a = RANDOM.nextInt(10);
		int b = RANDOM.nextInt(10);
		
		System.out.print(a + " " + op.toString() + " " + b + " = ");
		
		double result = calc.compute(a, b, op);

		System.out.println(result);
	}
}

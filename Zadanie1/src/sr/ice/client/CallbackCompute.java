package sr.ice.client;

import Demo.Callback_Calc_compute;
import Ice.LocalException;

public class CallbackCompute extends Callback_Calc_compute{

	@Override
	public void response(String arg0) {
		System.out.println(arg0);
	}

	@Override
	public void exception(LocalException arg0) {
		System.err.println(arg0.getMessage());
	}

}

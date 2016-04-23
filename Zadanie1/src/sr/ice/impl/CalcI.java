// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package sr.ice.impl;

import Demo.Operation;
import Demo._CalcDisp;
import Ice.Current;

public class CalcI extends _CalcDisp {
	private static final long serialVersionUID = -2448962912780867770L;

	@Override
	public double compute(int a, int b, Operation op, Current __current) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		double result = 0;

		switch (op) {
		case Add:
			result = a + b;
			break;
		case Dev:
			if (b != 0) {
				result = a / (double) b;
			}
			break;
		case Mod:
			result = a % b;
			break;
		case Mul:
			result = a * b;
			break;
		case Pow:
			result = Math.pow(a, b);
			break;
		case Sub:
			result = a - b;
			break;
		default:
			break;
		}

		return result;
	}

	/*@Override
	public void add2_async(AMD_Calc_add2 __cb, float a, float b, Current __current) throws RequestCanceledException {
		// TODO Auto-generated method stub

	}*/

}

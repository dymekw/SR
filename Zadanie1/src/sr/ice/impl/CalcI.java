// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package sr.ice.impl;

import java.util.concurrent.atomic.AtomicInteger;

import Demo.Operation;
import Demo._CalcDisp;
import Ice.Current;

public class CalcI extends _CalcDisp {
	private static final long serialVersionUID = -2448962912780867770L;
	private static AtomicInteger id = new AtomicInteger(0);
	private int ID;
	private String cat;

	public CalcI() {
		System.out.println("New CalcI ID: " + id);
		ID = id.getAndIncrement();
	}

	public CalcI(int ID) {
		ID++;
		System.out.println("Loaded CalcI ID: " + ID);
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}
	
	public void setCategory(String cat) {
		this.cat = cat;
	}

	@Override
	public String compute(int a, int b, Operation op, Current __current) {
		System.out.println("CalcI.compute()\tID: " + ID + " category: " + cat);
		try {
			Thread.sleep(100);
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
			if (b != 0) {
				result = a % b;
			}
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

		String resultStr = a + " " + op.toString() + " " + b + " = " + result;
		System.out.println(resultStr);
		return resultStr;
	}

	/*
	 * @Override public void add2_async(AMD_Calc_add2 __cb, float a, float b,
	 * Current __current) throws RequestCanceledException { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 */

}

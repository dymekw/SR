// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package sr.ice.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import Demo.Operation;
import Demo._CalcDisp;
import Ice.Current;

public class CalcI extends _CalcDisp {
	private static final long serialVersionUID = -2448962912780867770L;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

	private static AtomicInteger id = new AtomicInteger(0);
	private int ID;
	private String cat;
	private long creationTime;

	public CalcI(String cat) {
		System.out.println("New CalcI ID: " + id);
		ID = id.getAndIncrement();
		creationTime = Calendar.getInstance().getTimeInMillis();
		this.cat = cat;
	}

	public CalcI(String cat, long creationTime) {
		System.out.println("New CalcI ID: " + id);
		ID = id.getAndIncrement();
		this.creationTime = creationTime;
		this.cat = cat;
	}

	public int getID() {
		return ID;
	}

	public long getCreationTime() {
		return creationTime;
	}

	@Override
	public String compute(int a, int b, Operation op, Current __current) {
		System.out.println(
				"CalcI.compute()\tID: " + ID + " category: " + cat + " creation time: " + sdf.format(creationTime));
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

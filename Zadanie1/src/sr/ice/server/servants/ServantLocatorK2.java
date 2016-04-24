package sr.ice.server.servants;

import Ice.Current;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.UserException;
import sr.ice.impl.CalcI;

public class ServantLocatorK2 implements Ice.ServantLocator{

	@Override
	public void deactivate(String arg0) {
		System.out.println("ServantLocatorK2.deactivate()");
	}

	@Override
	public void finished(Current arg0, Object arg1, java.lang.Object arg2) throws UserException {
		System.out.println("ServantLocatorK2.finished()");
	}

	@Override
	public Object locate(Current arg0, LocalObjectHolder arg1) throws UserException {
		System.out.println("ServantLocatorK2.locate()");
		CalcI result = new CalcI("K2");
		return result;
	}

}

package sr.ice.server.servants;

import java.util.LinkedList;
import java.util.List;

import Ice.Current;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.ServantLocator;
import Ice.UserException;
import sr.ice.impl.CalcI;

/*
 * gra internetowa - ka�dy z serwnt�w reprezentuje np pojedy�czy serwer, 
 * na kt�rym mog� si� rejestrowa� gracze
 */
public class ServantLocatorK3 implements ServantLocator{
	private List<CalcI> calcs = new LinkedList<>();
	private static final int N = 5;
	
	public ServantLocatorK3() {
		System.out.println("ServantLocatorK3.ServantLocatorK3()");
		for (int i=0; i<N; i++) {
			CalcI tmp = new CalcI("K3");
			calcs.add(tmp);
		}
	}

	@Override
	public void deactivate(String arg0) {
		System.out.println("ServantLocatorK3.deactivate()");
	}

	@Override
	public void finished(Current arg0, Object arg1, java.lang.Object arg2) throws UserException {
		System.out.println("ServantLocatorK3.finished()");
	}

	@Override
	public Object locate(Current arg0, LocalObjectHolder arg1) throws UserException {
		System.out.println("\nServantLocatorK3.locate()");
		CalcI result = calcs.get(0);
		calcs.remove(result);
		calcs.add(result);
		return result;
	}

}

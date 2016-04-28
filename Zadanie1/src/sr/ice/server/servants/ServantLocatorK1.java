package sr.ice.server.servants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import Ice.Current;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.UserException;
import sr.ice.impl.CalcI;

/*
 * internetowa gra dla dwóch graczy - odczytujemy poprzedni stan rozgrywki, w dowolnym momencie 
 * mo¿emy zakoñczyæ i zapisaæ 
 */
public class ServantLocatorK1 implements Ice.ServantLocator {
	private Ice.ObjectAdapter adapter;
	private BufferedReader br = null;

	public ServantLocatorK1(Ice.ObjectAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public void deactivate(String arg0) {
		System.out.println("ServantLocatorK1.deactivate()");
	}

	@Override
	public void finished(Current arg0, Object arg1, java.lang.Object arg2) throws UserException {
		System.out.println("ServantLocatorK1.finished()");
		CalcI calc = (CalcI) arg1;
		PrintWriter writer;
		try {
			writer = new PrintWriter(System.getProperty("user.dir") + "\\K1" + arg0.id.name + ".txt");
			writer.println(calc.getID() + " " + calc.getCreationTime());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Object locate(Current arg0, LocalObjectHolder arg1) throws UserException {
		System.out.println("\nServantLocatorK1.locate()");
		CalcI calc = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\K1" + arg0.id.name + ".txt"));
			sCurrentLine = br.readLine();
			String[] parts = sCurrentLine.split(" ");
			int id = Integer.parseInt(parts[0]);
			sCurrentLine = br.readLine();
			long creationTime = Long.parseLong(parts[1]);
			calc = new CalcI("K1", creationTime, id);
		} catch (Exception e) {
			e.printStackTrace();
			calc = new CalcI("K1");
		}
		adapter.add(calc, arg0.id);
		return calc;
	}

}

package sr.ice.server.servants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import Ice.Current;
import Ice.Identity;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.UserException;
import sr.ice.impl.CalcI;

public class ServantLocatorK1 implements Ice.ServantLocator{
	private CalcI calc;
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
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(System.getProperty("user.dir") + "\\servant.txt");
			writer.println(calc.getCreationTime());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Object locate(Current arg0, LocalObjectHolder arg1) throws UserException {
		System.out.println("ServantLocatorK1.locate()");
		if (calc == null) {
			try {
				String sCurrentLine;
				br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\servant.txt"));
				sCurrentLine = br.readLine();
				long creationTime = Long.parseLong(sCurrentLine);
				calc = new CalcI("K1", creationTime);
			} catch (Exception e) {
				e.printStackTrace();
				calc = new CalcI("K1");
			}
		}
		adapter.add(calc, new Identity("calcK1", "K1"));
		return calc;
	}

}

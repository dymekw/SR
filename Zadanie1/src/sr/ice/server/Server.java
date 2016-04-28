// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package sr.ice.server;


import sr.ice.server.servants.ServantLocatorK1;
import sr.ice.server.servants.ServantLocatorK2;
import sr.ice.server.servants.ServantLocatorK3;
import sr.ice.server.servants.ServantLocatorK4;
import sr.ice.server.servants.ServantLocatorK5;

public class Server
{
	public void t1(String[] args)
	{
		int status = 0;
		Ice.Communicator communicator = null;

		try
		{
			communicator = Ice.Util.initialize(args); 
			Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Adapter1", 
					"tcp -h localhost -p 10000:udp -h localhost -p 10000");
			
			Ice.ServantLocator servantK1 = new ServantLocatorK1(adapter);
			Ice.ServantLocator servantK2 = new ServantLocatorK2();
			Ice.ServantLocator servantK3 = new ServantLocatorK3();
			Ice.ServantLocator servantK4 = new ServantLocatorK4();
			Ice.ServantLocator servantK5 = new ServantLocatorK5();
			adapter.addServantLocator(servantK1, "K1");
			adapter.addServantLocator(servantK2, "K2");
			adapter.addServantLocator(servantK3, "K3");
			adapter.addServantLocator(servantK4, "K4");
			adapter.addServantLocator(servantK5, "K5");
	        
			adapter.activate();
			System.out.println("Entering event processing loop...");
			communicator.waitForShutdown();
		}
		catch (Exception e)
		{
			System.err.println(e);
			status = 1;
		}
		if (communicator != null)
		{
			// Clean up
			try
			{
				communicator.destroy();
			}
			catch (Exception e)
			{
				System.err.println(e);
				status = 1;
			}
		}
		System.exit(status);
	}


	public static void main(String[] args)
	{
		Server app = new Server();
		app.t1(args);
	}
}

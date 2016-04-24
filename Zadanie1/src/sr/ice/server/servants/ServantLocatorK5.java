package sr.ice.server.servants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import Ice.Current;
import Ice.Identity;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.ServantLocator;
import Ice.UserException;
import sr.ice.impl.CalcI;

public class ServantLocatorK5 implements ServantLocator {
	private class EvictorEntry {
		Ice.Object servant;
		int useCount;
	}

	private static final int N = 2;
	private Map<Ice.Identity, EvictorEntry> map = new HashMap<>();
	private BufferedReader br;

	public ServantLocatorK5() {
	}

	@Override
	synchronized public void deactivate(String arg0) {
		evictServants(0);
	}

	@Override
	synchronized public void finished(Current arg0, Object arg1, java.lang.Object cookie) throws UserException {
		EvictorEntry entry = (EvictorEntry) cookie;

		--(entry.useCount);
		evictServants();
	}

	@Override
	synchronized public Object locate(Current c, LocalObjectHolder cookie) throws UserException {
		EvictorEntry entry = map.get(c.id);
		if (entry == null) {
			entry = new EvictorEntry();
			Ice.LocalObjectHolder cookieHolder = new Ice.LocalObjectHolder();
			entry.servant = add(c, cookieHolder);
			if (entry.servant == null) {
				return null;
			}
			entry.useCount = 0;
			evictServants(N - 1);
			map.put(c.id, entry);
		}

		++(entry.useCount);

		cookie.value = entry;
		return entry.servant;
	}

	private Object add(Current c, LocalObjectHolder cookieHolder) {
		String fileName = c.id.category + c.id.name;
		CalcI result = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\" + fileName + ".txt"));
			sCurrentLine = br.readLine();
			long creationTime = Long.parseLong(sCurrentLine);
			result = new CalcI("K5", creationTime);
		} catch (Exception e) {
			// e.printStackTrace();
			result = new CalcI("K5");
		}
		return result;
	}

	private void evictServants() {
		evictServants(N);
	}

	private void evictServants(int num) {
		System.out.println("ServantLocatorK5.evictServants()");
		int excessEntries = map.size();
		EvictorEntry minEntry = null;
		int min = Integer.MAX_VALUE;

		for (int i = 0; i < excessEntries && map.size() > num; ++i) {
			minEntry = null;
			min = Integer.MAX_VALUE;
			for (EvictorEntry entry : map.values()) {
				if (entry.useCount < min) {
					min = entry.useCount;
					minEntry = entry;
				}
			}

			for (Identity identity : map.keySet()) {
				if (map.get(identity).equals(minEntry)) {
					evict(identity, map.get(identity));
					map.remove(identity);
					break;
				}
			}
		}
	}

	private void evict(Identity identity, EvictorEntry evictorEntry) {
		System.out.println("ServantLocatorK5.evict()");
		String fileName = identity.category + identity.name;
		PrintWriter writer;
		try {
			writer = new PrintWriter(System.getProperty("user.dir") + "\\" + fileName + ".txt");
			writer.println(((CalcI) evictorEntry.servant).getCreationTime());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}

package sr.ice.server.servants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import Ice.Current;
import Ice.Identity;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.ServantLocator;
import Ice.UserException;
import sr.ice.impl.CalcI;

/*
 * obiekt reprezentuj¹cy grupê u¿ytkowników z³o¿onego systemu - u¿ytkownicy wykonuj¹ operacje na grupie 
 * do której nale¿¹, grupy mniej u¿ywane s¹ usuwane z pamiêci
 */
public class ServantLocatorK5 implements ServantLocator {
	private class EvictorEntry {
		CalcI servant;
		int useCount;
	}

	private static final int N = 3;
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
	}

	@Override
	synchronized public Object locate(Current c, LocalObjectHolder cookie) throws UserException {
		System.out.println("\nServantLocatorK5.locate()");
		EvictorEntry entry = map.get(c.id);
		if (entry == null) {
			entry = new EvictorEntry();
			entry.useCount = 0;
			entry.servant = add(c, entry);
			if (entry.servant == null) {
				return null;
			}
			evictServants(N - 1);
			map.put(c.id, entry);
		}

		++(entry.useCount);

		cookie.value = entry;
		return entry.servant;
	}

	private CalcI add(Current c, EvictorEntry entry) {
		String fileName = c.id.category + c.id.name;
		CalcI result = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\" + fileName + ".txt"));
			sCurrentLine = br.readLine();
			String parts[] = sCurrentLine.split(" ");
			int id = Integer.parseInt(parts[0]);
			long creationTime = Long.parseLong(parts[1]);
			result = new CalcI("K5", creationTime, id);
			entry.useCount = Integer.parseInt(parts[2]);
		} catch (Exception e) {
			// e.printStackTrace();
			result = new CalcI("K5");
		}
		return result;
	}

	private void evictServants(int num) {
		int excessEntries = map.size();
		EvictorEntry minEntry = null;
		int min = Integer.MAX_VALUE;

		for (int i = 0; i < excessEntries && map.size() > num; ++i) {
			minEntry = null;
			min = Integer.MAX_VALUE;
			for (EvictorEntry entry : map.values()) {
				if (entry.useCount <= min){
					if (entry.useCount==min) {
						if (new Random().nextBoolean()) {
							continue;
						}
					}
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
		String fileName = identity.category + identity.name;
		System.out.println("ServantLocatorK5.evict() " + fileName + "\t" + ((CalcI)evictorEntry.servant).getID());
		PrintWriter writer;
		try {
			writer = new PrintWriter(System.getProperty("user.dir") + "\\" + fileName + ".txt");
			writer.println(evictorEntry.servant.getID() + " " + (evictorEntry.servant).getCreationTime() + " " + evictorEntry.useCount);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}

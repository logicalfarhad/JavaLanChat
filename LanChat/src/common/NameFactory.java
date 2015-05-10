package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.JOptionPane;

public class NameFactory {
	public static String[] getAllNames() {
		String userLoc = "Files/names.info";

		Scanner scanf = null;
		try {
			scanf = new Scanner(new File(userLoc));
		} catch (FileNotFoundException e) {
			JOptionPane
					.showMessageDialog(null, "The file '" + userLoc
							+ "' is missing", "File Missing",
							JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		TreeMap<Integer, String> nameMap = new TreeMap<Integer, String>();
		try {
			while (scanf.hasNext()) {
				nameMap.put(scanf.nextInt(), scanf.next());
			}
		} catch (NoSuchElementException e) {
			JOptionPane.showMessageDialog(null, "The file '" + userLoc
					+ "' is incomplete", "File Incomplete",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		int max = Collections.max(nameMap.keySet());
		String[] nameArray = new String[max + 1];
		for (Integer roll : nameMap.keySet()) {
			nameArray[roll] = nameMap.get(roll);
		}
		
		return nameArray;
	}
}

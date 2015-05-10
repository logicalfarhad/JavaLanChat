package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Converter {
	public static final String YAHOO_BASE = "emotes/yahoo/",
			GNOME_BASE = "emotes/gnome/";

	/*
	 * The symbols which will be converted
	 */
	private static String[] smileySymbols;

	/*
	 * The patters created from the symbols, these will be used to search the
	 * strings.
	 */
	private static Pattern newlinePattern = Pattern.compile("\n");
	private static Pattern[] smileyPatterns;

	/*
	 * The html replacement of the patterns.
	 */
	private static String[] smileyReplacements;
	private static String newlineReplacement = "<br>";

	/*
	 * The base directory of the image files
	 */
	private static String baseDirectory;

	/*
	 * This static block makes the patterns and their replacements ready
	 */
	private static void loadPatterns(String selectedEmote) {
		String metaFileName = selectedEmote + "emote_symbols.meta";

		try {
			Scanner scanf = new Scanner(new File(metaFileName));
			int length = scanf.nextInt();
			smileySymbols = new String[length];
			smileyReplacements = new String[length];
			smileyPatterns = new Pattern[length];

			for (int i = 0; i < length; i++) {
				smileySymbols[i] = scanf.next();
				smileyReplacements[i] = "<img src = \"" + baseDirectory
						+ scanf.next() + "\"\\>";
				smileyPatterns[i] = Pattern.compile(smileySymbols[i],
						Pattern.CASE_INSENSITIVE);
			}
		} catch (FileNotFoundException e) {
			System.err.println("file not found: " + baseDirectory
					+ metaFileName);
		} catch (NoSuchElementException e) {
			System.out.println("file is faulty: '" + baseDirectory
					+ metaFileName);
		}
	}

	/**
	 * Converts a message with emoticons and other symbols to equivalent HTML
	 * tag
	 * 
	 * @param original
	 *            the original string
	 * @return HTML conversion of the input
	 */
	public static String convert(String original) {
		String converted = newlinePattern.matcher(original).replaceAll(
				newlineReplacement);

		for (int s = 0; s < smileyPatterns.length; s++) {
			converted = smileyPatterns[s].matcher(converted).replaceAll(
					smileyReplacements[s]);
		}

		return converted;
	}

	public static void setImageBase(String imageBase) {
		baseDirectory = ClassLoader.getSystemResource(imageBase)
				.toExternalForm();
		loadPatterns(imageBase);
	}
}

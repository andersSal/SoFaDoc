package utilities;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import main.Console;

public abstract class Format {
	
	
	public static void printLibrary(List<Phrase> phrases) {
		PrintWriter printWriter = null; 
		File file = new File(Console.getLibraryPath().toString());
		Collections.sort(phrases);
		try {
			printWriter = new PrintWriter(new BufferedWriter((new FileWriter(file,false))));
			Iterator<Phrase> it = phrases.iterator(); 
			while (it.hasNext()) {
				Phrase p = it.next(); 
				printWriter.println("[" + p.getCount() + "]" + p.toString());
			}
			printWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (printWriter != null) printWriter.close();
		}
	}
	
	public static void printAbbreviations(Map<String, String> abbreviations) {
		PrintWriter printWriter = null; 
		File file = new File(Console.getAbbreviationPath().toString());
		try {
			printWriter = new PrintWriter(new BufferedWriter((new FileWriter(file,false))));
			printWriter.println("#Startup#" + Console.autoCorrectON);
			for (String key : abbreviations.keySet()) {
				printWriter.println(key + "//" + abbreviations.get(key));
			}
			printWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (printWriter != null) printWriter.close();
		}
	}
	
	public static void printColorLinks(Map<Color, String> colorLinks) {
		PrintWriter printWriter = null; 
		File file = new File(Console.getColorLinksPath().toString());
		try {
			printWriter = new PrintWriter(new BufferedWriter((new FileWriter(file,false))));
			printWriter.println("#Startup#" + Console.colorLinksON);
			for (Color c : colorLinks.keySet()) {
				printWriter.print(c.getRed() + ":" + c.getGreen() + ":" + c.getBlue() + ":" + c.getAlpha());
				printWriter.println(colorLinks.get(c));
			}
			printWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (printWriter != null) printWriter.close();
		}
	}
	
	public static void printWordLinks(List<List<LinkedString>> linkedlists) {
		PrintWriter printWriter = null; 
		File file = new File(Console.getWordLinksPath().toString());
		try {
			printWriter = new PrintWriter(new BufferedWriter((new FileWriter(file,false))));
			printWriter.println("#Startup#" + Console.wordLinksON);
			for (List<LinkedString> list : Console.wordLinks) {
				printWriter.println(list);
			}
			printWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (printWriter != null) printWriter.close();
		}
	}
}

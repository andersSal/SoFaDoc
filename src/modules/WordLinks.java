package modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import main.Console;
import utilities.LinkedString;

public class WordLinks {
	
	public WordLinks() {
		Scanner scanner = null; 
		File file = new File(Console.getWordLinksPath().toString());
		try {
			scanner = new Scanner(file);
			List<List<LinkedString>> linkedLists = new ArrayList<>();
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine(); 
				if (line.contains("#Startup#")) {
					if (line.substring(line.lastIndexOf("#") +1).equals("false")) {
						Console.wordLinksON = false; 
					} else {
						Console.wordLinksON = true; 
					}
				}
				else if (! line.isEmpty()) {
					String s = line.substring(1, line.length() -1);
					String[] words = s.split(",");
					List<String> links = new ArrayList<>();
					for (String word : words) {
						String w = word.trim();
						links.add(w);
					}
					Console.allWordsInLinks.addAll(links);
					linkedLists.add(createLinkedList(links)); 
				}
			}
			Console.wordLinks = linkedLists; 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public boolean addWordLink(String words, JComponent location) {
		boolean canInsert = true; 
		while (words.contains("  ")) {
			words = words.replaceAll("  ", " ");
		}
		words = words.replaceAll(" ", "");
		String[] array = words.split(",");
		if (words.equals("")) canInsert = false; 
		if (! Console.wordLinks.isEmpty() && canInsert) {
			for (String string : array) {
				LinkedString ls = isInList(string);
				if (ls != null) {
					JOptionPane.showConfirmDialog(location,
							"Det finnes allerede en link for:\n'" + ls.toString() + "'" , "Link ble ikke opprettet!",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					canInsert = false;
					break; 
				}
			}
		}
		if (canInsert) {
			Console.wordLinks.add(createLinkedList(Arrays.asList(array)));
			Console.allWordsInLinks.addAll(Arrays.asList(array));						
		}
		return canInsert;
	}

	
	public static LinkedString isInList(String s) {
		LinkedString linkedString = null; 
		for (List<LinkedString> list : Console.wordLinks) {
			linkedString = list.get(0);
			if (linkedString.toString().equals(s.trim())) return linkedString;
			linkedString = linkedString.getNext(); 
			while (linkedString.getPrev() != null) {
				if (linkedString.toString().equals(s.trim())) return linkedString;
				linkedString = linkedString.getNext(); 
			}
		}
		return null; 		
	}
	
	public static List<LinkedString> createLinkedList(List<String> array) {
		List<LinkedString> temp = new ArrayList<>(); 
		for (String s : array) {
			temp.add(new LinkedString(s.trim().toLowerCase())); 
		}
		for (int i = 0; i < temp.size(); i++) {
			int j = i + 1; 
			if (j != temp.size()) {
				temp.get(i).setNext(temp.get(j));
				temp.get(j).setPrev(temp.get(i));
			} else {
				temp.get(i).setNext(temp.get(0));
			}
		}
		return temp; 
	}
	
}

package modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;

import main.Console;

public class Abbreviation {
	
	public Abbreviation() {
		Map<String, String> map = new HashMap<>();
		Scanner scanner = null; 
		File file = new File(Console.getAbbreviationPath().toString());
		try {
			scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine(); 
				if (line.contains("#Startup#")) {
					if (line.substring(line.lastIndexOf("#") +1).equals("false")) {
						Console.autoCorrectON = false;
					} else {
						Console.autoCorrectON = true; 
					}
				}
				else if (! line.isEmpty() && line.contains("//")) {
					String key = line.substring(0, line.indexOf("//"));	
					String value = line.substring(line.lastIndexOf("/") + 1);
					map.put(key, value); 
				}
			}
			Console.autoCorrectMap = map;
			
		} catch (FileNotFoundException e) {
			JOptionPane.showConfirmDialog(null, "Søk følgende i windows start meny:\n"
					+ "%appdata% \nKlikk deg inn i mappen 'SoFa', slett tekstfilen 'abbreviations og kjør programmet på nytt", "%appdata% AutoCorrect error",JOptionPane.DEFAULT_OPTION, 
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public boolean addAbbreviation(String shortString, String longString) {
		String k1 = shortString;
		String k2 = "";
		String v1 = longString;
		String v2 = "";
		if (Character.isLowerCase(k1.charAt(0))) {
			k2 = (k1.charAt(0) + "").toUpperCase() + k1.substring(1);
		} else {
			k2 = (k1.charAt(0) + "").toLowerCase() + k1.substring(1);
		}
		if (Character.isLowerCase(v1.charAt(0))) {
			v2 = (v1.charAt(0) + "").toUpperCase() + v1.substring(1);
		} else {
			v2 = (v1.charAt(0) + "").toLowerCase() + v1.substring(1);
		}
		Map<String, String> map = Console.autoCorrectMap;
		if (map.containsKey(k1) || map.containsKey(k2)) {
			return false; 
		} else {
			Console.autoCorrectMap.put(k1, v1);
			Console.autoCorrectMap.put(k2, v2);
			return true; 
		}
	}
	
	

}

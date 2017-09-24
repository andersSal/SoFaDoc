package modules;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import main.Console;

public class ColorLinks {
	
	public ColorLinks() {
		Scanner scanner = null; 
		File file = new File(Console.getColorLinksPath().toString());
		try {
			scanner = new Scanner(file);
			Map<Color, String> links = new HashMap<>(); 
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine(); 
				if (line.contains("#Startup#")) {
					if (line.substring(line.lastIndexOf("#") +1).equals("false")) {
						Console.colorLinksON = false; 
					} else {
						Console.colorLinksON = true; 
					}
				}
				else if (! line.isEmpty()) {
					String[] rgb = line.substring(0, line.indexOf('(')).split(":");
					int red = Integer.parseInt(rgb[0]); int green = Integer.parseInt(rgb[1]); int blue = Integer.parseInt(rgb[2]); int alpha = Integer.parseInt(rgb[3]);
					Color color = new Color(red, green, blue, alpha);
					String matchCode = line.substring(line.indexOf("("));
					links.put(color, matchCode);
				}
			}
			Console.colorLinks = links; 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void addLink(Color color, String string) {
		List<String> words = new ArrayList<>(); 
		if (Console.colorLinks.containsKey(color)) {
			String pattern = Console.colorLinks.get(color);
			pattern = pattern.substring(1, pattern.length() - 1);
			pattern = pattern.replace("\\b|\\b", "\\b");
			pattern = pattern.substring(2);
			pattern = pattern.replace("\\b", ",");
			String[] existing = pattern.split(",");
			words.addAll(Arrays.asList(existing));
			Console.colorLinks.remove(color);
		} 
		String[] wordsToAdd = string.split(",");
		for (String s : wordsToAdd) {
			s = s.trim(); 
			String lower = s.toLowerCase();
			String upper = (lower.charAt(0)+"").toUpperCase() + lower.substring(1);
			words.add(lower);
			words.add(upper);
		}
		StringBuilder builder = new StringBuilder("(");
		for (String word : words) {
			builder.append("\\b").append(word).append("\\b").append("|");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append(")");
		Console.colorLinks.put(color, builder.toString());
	}	
}

package utilities;

import java.util.ArrayList;
import java.util.List;

public abstract class Gender {
	
	public static List<String> changeDisplay(List<String> listToChange, char gender) {
		List<String> out = new ArrayList<>(); 
		for (String line : listToChange) {
			String newLine = ""; 
			String[] words = line.split(" ");
			for (String word : words) {
				if (word.contains("//")) {
					String endOfWord = word.charAt(word.length() - 1) + "";
					if (! ".,!?:;".contains(endOfWord + "")) {
						endOfWord = "";
					} else {
						word = word.substring(0, word.length()-1);
					}	
					switch (word) {
					case "Hun//Han":
						if (gender == 'F') {
							word = "Hun";
						} else {
							word = "Han";
						}
						break;
					case "hun//han":
						if (gender == 'F') {
							word = "hun";
						} else {
							word = "han";
						}
						break;
					case "henne//ham":
						if (gender == 'F') {
							word = "henne";
						} else {
							word = "ham";
						}
						break;
					case "Hennes//Hans":
						if (gender == 'F') {
							word = "Hennes";
						} else {
							word = "Hans";
						}
						break;
					case "hennes//hans":
						if (gender == 'F') {
							word = "hennes";
						} else {
							word = "hans";
						}
						break;
				
					default:
						break;
					}
					word += endOfWord; //legger til .,?! osv
				}
				newLine += word + " ";	
			}
			out.add(newLine);
		}
		return out; 
	}
	
	public static String changeText(String text, char gender) {
		String[] words = text.split(" ");
		String out = ""; 
		int counter = 0; 
		for (String word : words) {
			if (word.equals("Hun") || word.equals("Han")) {
				word = (gender == 'F') ? "Hun" : "Han";
			} else if (word.equals("hun") || word.equals("han")) {
				word =(gender == 'F') ? "hun" : "han";
			} else if (word.equals("henne")) {
				word = (gender == 'F') ? "henne" : "han";
			} else if (word.equals("hennes") || word.equals("hans")) {
				word = (gender == 'F') ? "hennes" : "hans";
			} else if (word.equals("Hennes") || word.equals("Hans")) {
				word = (gender == 'F') ? "Hennes" : "Hans";
			}
			if (counter < words.length -1) {
				out += word + " ";
				counter++; 
			} else {
				out += word;
			}
		}		
		return out; 
	}
	

	
	
	public static String addHeOrSheToPhrase(String phrase) {
		String[] words = phrase.split(" ");
		String out = ""; 
		int wordCount = 0;
		for (String word : words) {
			if (word.equals("")) { //doublespace
				break; 
			}
			String endOfWord = word.charAt(word.length() -1) + "";
			if (! ".,!?:;".contains(endOfWord + "")) {
				endOfWord = "";
			} else {
				word = word.substring(0, word.length()-1);
			}	
			switch (word) {
				case "Han":
					word = "Hun//Han";
					break;
				case "Hun":
					word = "Hun//Han";
					break;	
				case "han":
					word = "hun//han";
					break;
				case "hun":
					word = "hun//han";	
					break;
				case "Hans":
					word = "Hennes//Hans";		
					break;
				case "Hennes":
					word = "Hennes//Hans";		
					break;
				case "hans":
					word = "hennes//hans";		
					break;
				case "hennes":
					word = "hennes//hans";		
					break;
				case "henne":
					word = "henne//ham";		
					break;
				case "ham":
					word = "henne//ham";		
					break;
				default:
					break;
				}
			word += endOfWord;
			if (wordCount != words.length-1) {
				out += word + " "; 
				wordCount += 1;
			} else {
				out += word;
			}
		}
		return out;
	}

	
}

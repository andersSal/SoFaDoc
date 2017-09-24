package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import main.Console;

public class Search implements Iterator<Phrase> {

	private List<Phrase> phrases = new ArrayList<>(); 
	private int index; 
	
	public Search() {
		File file = new File(Console.getLibraryPath().toString()); 
		Scanner scanner = null; 
		try {
			scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine().trim(); 
				if (line.startsWith("[") && line.contains("]")) {
					try {
						int num = Integer.parseInt(line.substring(1, line.indexOf("]")));
						line = line.substring(line.indexOf("]") + 1);
						line = Gender.addHeOrSheToPhrase(line);
						if (! isInLibrary(line)) {
							phrases.add(new Phrase(line, num));
						}
					} catch (NumberFormatException ne) {}
				} else {
					if (! line.equals("") && !line.contains("//")) {
						if (!isInLibrary(line)){
							line = Gender.addHeOrSheToPhrase(line);
							phrases.add(new Phrase(line, 0));
						}
					}
				}
			}			
		} catch (FileNotFoundException e) {
			
		}
	}
	
	public List<String> searchKeyWords(String...strings) {
		Collections.sort(phrases);
		List<String> out = new ArrayList<>(); 
		List<String> keyWords = new ArrayList<>(); 
		keyWords.addAll(Arrays.asList(strings));
		Iterator<Phrase> iterator = phrases.iterator();
		out.addAll(phrases.stream().map(Phrase::toString).collect(Collectors.toList()));		
		while (iterator.hasNext()) {
			String line = iterator.next().toString(); 
			for (String keyWord : keyWords) {
				if (! line.toLowerCase().contains(keyWord.toLowerCase())) {
					out.remove(line);
					break; 
				}
			}
		}
		return out;
	}
	public void addChosencount(String string) {
		for (Phrase phrase : this.phrases) {
			if (phrase.toString().equals(Gender.addHeOrSheToPhrase((string)))) {
				phrase.addCount();
			}
		}
	}
	
	public int getPhraseCount(String string) {
		for (Phrase phrase : phrases) {
			if (phrase.toString().equals(Gender.addHeOrSheToPhrase(string))) {
				return phrase.getCount();
			}
		}
		return 0; 
	}
	
	public List<Phrase> getPhrases() {
		Collections.sort(phrases);
		return phrases; 
	}
	
	public void addPhrase(String string) {
		if (isInLibrary(string)) {
			throw new IllegalArgumentException("Frasen finnes i biblioteket fra før");
		}
		phrases.add(new Phrase(Gender.addHeOrSheToPhrase(string).trim(), 0));
		
	}
	
	public void addPhrase(Phrase phrase) {
		if (isInLibrary(phrase.toString())) {
			throw new IllegalArgumentException("Kunne ikke angre");
		}
		phrases.add(phrase);

	}
	
	public void removePhrase(String string) {
		if (! isInLibrary(string)) {
			throw new IllegalArgumentException("Frasen finnes ikke i biblioteket");
		}
		index = -1; 
		while(hasNext()) {
			Phrase p = next(); 
			if (p.toString().trim().equals(Gender.addHeOrSheToPhrase(string.trim()))) {
				phrases.remove(p);
			}
		}
	}
	
	private boolean isInLibrary(String string) {
		for (Phrase phrase : phrases) {
			if (Gender.addHeOrSheToPhrase(phrase.toString()).trim().equals(Gender.addHeOrSheToPhrase(string.trim()))) {
				return true;
			}
		}
		return false; 
	}

	
	@Override
	public boolean hasNext() {
		return index < phrases.size() -1; 
	}

	@Override
	public Phrase next() {
		index++; 
		return phrases.get(index); 
	}
}

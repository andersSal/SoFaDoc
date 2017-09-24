package utilities;

public class Phrase implements Comparable<Phrase> {
	
	private String string; 
	private int count = 0;
	private String firstWord;

	
	public Phrase(String string, int count) {
		this.string = string; 
		this.count = count; 
		if(string.contains(" ")) {
			this.firstWord = string.substring(0, string.indexOf(" "));
		} else {
			this.firstWord = string;
		}
	}
	
	public void addCount() {
		this.count += 1; 
	}

	@Override
	public int compareTo(Phrase other) {
		if (other.count == this.count) {
			return this.firstWord.compareTo(other.firstWord);		
		}
		if (other.count > this.count) {
			return 1;
		} 
		else if (this.count > other.count) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return string;
	}

	public int getCount() {
		return count; 
	}
}


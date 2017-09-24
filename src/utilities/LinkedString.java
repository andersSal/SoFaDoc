package utilities;

public class LinkedString {
	
	private LinkedString next; 
	private LinkedString prev; 
	private String value; 
	
	public LinkedString(String value) {
		this.value = value; 
		this.next = null; 
		prev = null; 
	}
	
	public void setNext(LinkedString next) {
		this.next = next; 
	}
	
	public void setPrev(LinkedString prev) {
		this.prev = prev; 
	}
	
	public LinkedString getNext() {
		return this.next; 
	}
	
	public LinkedString getPrev() {
		return this.prev; 
	}
	
	public String getValue() {
		return this.value; 
	}
	
	public String toString() {
		return this.value; 
	}
	

}

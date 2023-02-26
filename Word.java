package Test;

import java.util.ArrayList;
import java.util.LinkedList;

public class Word {
	String name;
	int frequency;
	String  textName;
	LinkedList<String> ll;
	String foldername;
	public Word(String name, int frequency, String textName,String foldername) {
		
		this.name = name;
		this.frequency = frequency;
		this.textName = textName;
		this.ll = new LinkedList<String>();
		this.foldername = foldername;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public String getTextName() {
		return textName;
	}
	public void setTextName(String textName) {
		this.textName = textName;
	}
	public LinkedList<String> getLl() {
		return ll;
	}
	public void setLl(LinkedList<String> ll) {
		this.ll = ll;
	}
	public String getFoldername() {
		return foldername;
	}
	public void setFoldername(String foldername) {
		this.foldername = foldername;
	}



	

	
	
	

}

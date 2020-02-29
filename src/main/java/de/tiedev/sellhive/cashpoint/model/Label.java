package de.tiedev.sellhive.cashpoint.model;

public class Label {
	String firstLine;
	String secondLine;
	String thirdLine;


	public Label(String firstLine) {
		this.firstLine = firstLine;
	}

	public Label(String firstLine, String secondLine, String thridLine) {
		this.firstLine = firstLine;
		this.secondLine = secondLine;
		this.thirdLine = thridLine;
	}

	public String getFirstLine() {
		return firstLine;
	}

	public void setFirstLine(String firstLine) {
		this.firstLine = firstLine;
	}
	
	public String getSecondLine() {
		return secondLine;
	}
	
	public void setSecondLine(String secondLine) {
		this.secondLine = secondLine;
	}
	
	public String getThirdLine() {
		return thirdLine;
	}
	
	public void setThirdLine(String thirdLine) {
		this.thirdLine = thirdLine;
	}
	
}

package Notes;

import java.awt.image.BufferedImage;

import Board.Coordinate;

public class Entry {

	private final Coordinate entryCoordinates;
	private EntryType type;
	
	
	public Entry(int x, int y, EntryType type){
		entryCoordinates = new Coordinate(x,y, false); //creates an instance of entryCoordinates
		this.type = type;
	}
	
	//returns coordinate
	public Coordinate getCoordinates() {
		return entryCoordinates;
	}
	
	public void setEntry(EntryType type) { //this method sets the entry type
		this.type = type;
	}
	
	public EntryType getEntry() {//this method returns the type of the entry
		return type;
	}
	
}

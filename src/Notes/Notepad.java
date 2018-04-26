package Notes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Game.Cluedo;

import java.io.IOException;
import java.io.InputStream;

public class Notepad extends JPanel {
	private final int ENTRY_BOX_SIZE = 21; //tile size same as board to make it fit smoothly beside it
	private final int HORIZONTAL_OFFSET = 30;
	private final int VERTICAL_OFFSET = 31;
	private final int FIRST_COLUMN_OFFSET = 117;
	private final int FIRST_ROW_OFFSET = 5;
	private final int ENTRY_CENTER_OFFSET = 10;
	
	public static final int NOTEPAD_HEIGHT = 774; //
	public static final int NOTEPAD_WIDTH = 265;
	private Entry[][] NOTEPAD;
	private BufferedImage notepadImg; //will store the notepad img
	private final String[] rowNames = new String[22];
	private final int playerInd; //stores the index of the owner of this notepad
	private final int playerCount;
	
	public Notepad(int playerInd, int playerCount) {
		this.playerInd = playerInd;
		this.playerCount = playerCount;
		NOTEPAD = new Entry[22][playerCount+1];
		String notepad = "assets/notepad.png";
		InputStream in = this.getClass().getResourceAsStream(notepad);
		
		try {
			notepadImg = ImageIO.read(in);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		//cheking every position in the NOTEPAD and setting the entry to the correct position
		for(int i=0; i<NOTEPAD.length; i++) {
			for(int j=0; j<NOTEPAD[0].length; j++) {
				//diffrent cases(i=0,i<7 etc etc)
				if(i==0) {
					NOTEPAD[i][j] = new Entry(FIRST_COLUMN_OFFSET+(j*HORIZONTAL_OFFSET),
							FIRST_ROW_OFFSET,
							null);
				}
				else if(i < 7) {
					NOTEPAD[i][j] = new Entry(FIRST_COLUMN_OFFSET+(j*HORIZONTAL_OFFSET),
							FIRST_ROW_OFFSET+((i+1)*VERTICAL_OFFSET),
							EntryType.EMPTY);
				}
				else if(i < 13) {
					NOTEPAD[i][j] = new Entry(FIRST_COLUMN_OFFSET+(j*HORIZONTAL_OFFSET),
							FIRST_ROW_OFFSET+((i+2)*VERTICAL_OFFSET),
							EntryType.EMPTY);
				}
				else {
					NOTEPAD[i][j] = new Entry(FIRST_COLUMN_OFFSET+(j*HORIZONTAL_OFFSET),
							FIRST_ROW_OFFSET+((i+3)*VERTICAL_OFFSET),
							EntryType.EMPTY);
				}
			}
		}
		
		//each rowName is set to a noun(person,place..weapon etc)
		rowNames[0] = null;
		rowNames[1] = "Mustard";
		rowNames[2] = "Plum";
		rowNames[3] = "Green";
		rowNames[4] = "Peacock";
		rowNames[5] = "Scarlett";
		rowNames[6] = "White";
		rowNames[7] = "Candlestick";
		rowNames[8] = "Knife";
		rowNames[9] = "Poison";
		rowNames[10] = "Revolver";
		rowNames[11] = "Rope";
		rowNames[12] = "Pipe";
		rowNames[13] = "Kitchen";
		rowNames[14] = "Ball Room";
		rowNames[15] = "Conservatory";
		rowNames[16] = "Billiard Room";
		rowNames[17] = "Library";
		rowNames[18] = "Study";
		rowNames[19] = "Hall";
		rowNames[20] = "Lounge";
		rowNames[21] = "Dining Room";
		
		setLayout(null);
		int count = 0;
		
		for(int i=0; i<6; i++) {
			int j = (playerInd+1+i)%6; //6 players
			if(j != playerInd && Cluedo.players[j].isInGame()) {
				JLabel label = new JLabel(new ImageIcon(Cluedo.players[j].tokenImg));
				add(label);
				label.setSize(ENTRY_BOX_SIZE, ENTRY_BOX_SIZE);
				label.setLocation(NOTEPAD[0][count++].getCoordinates().getX(), NOTEPAD[0][count].getCoordinates().getY());
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(notepadImg, 0, 0, NOTEPAD_WIDTH, NOTEPAD_HEIGHT, null);
		int i;
		
		//creating notepad appearance,colour etc
		for(i=1; i<NOTEPAD.length; i++) {
			//drawing red line if card is owned by player
			if(NOTEPAD[i][playerCount].getEntry() == EntryType.OWNED) {
				g2.setColor(Color.RED);
				g2.setStroke(new BasicStroke(3));
				g2.drawLine(5, NOTEPAD[i][playerCount].getCoordinates().getY()+ENTRY_CENTER_OFFSET, NOTEPAD_WIDTH-5, NOTEPAD[i][playerCount].getCoordinates().getY()+ENTRY_CENTER_OFFSET);
			}
			//drawing green line if card is seen by all players
			else if(NOTEPAD[i][playerCount].getEntry() == EntryType.ALL_SEE) {
				g2.setColor(Color.GREEN);
				g2.setStroke(new BasicStroke(3));
				g2.drawLine(5, NOTEPAD[i][playerCount].getCoordinates().getY()+ENTRY_CENTER_OFFSET, NOTEPAD_WIDTH-5, NOTEPAD[i][playerCount].getCoordinates().getY()+ENTRY_CENTER_OFFSET);
			}
			else {
				for(int j=0; j<playerCount-1; j++) {
					EntryType currEntry = NOTEPAD[i][j].getEntry();
					//drawing the X
					if(currEntry == EntryType.CROSS) {
						int x = NOTEPAD[i][j].getCoordinates().getX();
						int y = NOTEPAD[i][j].getCoordinates().getY();
						g2.setColor(Color.RED);
						g2.setStroke(new BasicStroke(1));
						g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
						g2.drawLine(x, y, x + ENTRY_BOX_SIZE, y + ENTRY_BOX_SIZE);
						g2.drawLine(x + ENTRY_BOX_SIZE, y, x, y + ENTRY_BOX_SIZE);
					}
					//drawing the tick
					else if(currEntry == EntryType.TICK) {
						int x = NOTEPAD[i][j].getCoordinates().getX();
						int y = NOTEPAD[i][j].getCoordinates().getY();
						g2.setColor(Color.GREEN);
						g2.setStroke(new BasicStroke(1));
						g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
						g2.drawLine(x, y + ENTRY_BOX_SIZE/2, x + ENTRY_BOX_SIZE/2, y + ENTRY_BOX_SIZE);
						g2.drawLine(x + ENTRY_BOX_SIZE, y, x + ENTRY_BOX_SIZE/2, y + ENTRY_BOX_SIZE);
					}
				}
			}
		}
	}
	
	//this method allows us to change an initial entry
	public int fillEntry(EntryType entry, int column, String cardName, int searchStart) {
		for(int i=searchStart; i<22; i++) {
			if(cardName.equalsIgnoreCase(rowNames[i])) {
				if(NOTEPAD[i][playerCount].getEntry() != EntryType.EMPTY)
					return i; //return if we do not need to update the row
				
				//if a tick is being filled in, we automatically cross of the cells which cannot be a tick
				if(entry == EntryType.TICK) {
					NOTEPAD[i][column].setEntry(entry);
					for(int j=0; j<playerCount-1; j++) {
						if(j != column)
							NOTEPAD[i][j].setEntry(EntryType.CROSS);
					}
				}
				else {
					NOTEPAD[i][column].setEntry(entry);
				}
				repaint();
				return i;
			}
		}
		return -1; //if cardName was wrong
	}
	
	//this method crosses the cards off of the notepad
	public void crossOff(String cardName, EntryType type) {
		if(type != EntryType.OWNED && type != EntryType.ALL_SEE)
			throw new WrongEntryTypeException(); //exception if the incorrect entry is found
		
		for(int i=1; i<22; i++) {
			if(cardName.equals(rowNames[i])) {
				NOTEPAD[i][playerCount].setEntry(type);
				repaint();
				return;
			}
		}
	}
}

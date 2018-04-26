package PlayerTokens;


/*
	MCINO - Kamil Cierpisz, Nikolaj Jasenko, Temitope Akinwale
*/

import Board.*;
import Card.*;
import Notes.EntryType;
import Notes.Notepad;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class Player extends JLabel{


    private Coordinate playerCoordinates;
	public static final int TOKEN_SIZE = 20;

    private boolean inGame = false; // boolean to check if player is in game
    private boolean playerRemoved = false;
    private boolean inRoom = false;
    private JLabel token; // player's token
    public final BufferedImage tokenImg; //player's token image
    private final String tokenName;
    private String playerName;
    private int currRoom; // keeps track of the current Room that the player is in
    private int prevRoom; // keeps track of the previous Room entered by the player so that he does not re-enter it in next turn
    
    private ArrayList<Card> cards = new ArrayList<Card>();
    
    private Notepad notes;
    
	//player constructor containing x and y co-ordinate aswell as the tokenPath
    public Player(int initial_x, int initial_y, String tokenPath, String tokenName) throws IOException {
        playerCoordinates = new Coordinate(initial_x,initial_y, false);
        InputStream in2 = this.getClass().getResourceAsStream(tokenPath); //this returns the tokenPath
        tokenImg = ImageIO.read(in2);
        this.prevRoom = 15; // random numbers which will be assigned properly throughout the game
        this.currRoom = 16; // random numbers which will be assigned properly throughout the game
        this.token = new JLabel(new ImageIcon(tokenImg));
        token.setSize(TOKEN_SIZE, TOKEN_SIZE); //Setting the size of the token using x and y co-ordinates
        token.setLocation(Board.BOARD[playerCoordinates.getX()][playerCoordinates.getY()].getTileCoordinates().getX() + Board.TILE_OFFSET,
                            Board.BOARD[playerCoordinates.getX()][playerCoordinates.getY()].getTileCoordinates().getY() + Board.TILE_OFFSET);
        Board.BOARD[playerCoordinates.getX()][playerCoordinates.getY()].changeTaken(true);
        this.tokenName = tokenName;
    }

    public Coordinate getPlayerCoordinates() {
        return playerCoordinates;
    }

    /**
     * Changes the players position by translating it based on input arguments, it also changes the tokens display location
     * @param x_movement 
     * @param y_movement
     */
    public void move(int x_movement, int y_movement) {
    	Board.BOARD[playerCoordinates.getX()][playerCoordinates.getY()].changeTaken(false);
    	playerCoordinates.changeCoordinates(x_movement,y_movement);
    	token.setLocation(Board.BOARD[playerCoordinates.getX()][playerCoordinates.getY()].getTileCoordinates().getX()+Board.TILE_OFFSET,
                            Board.BOARD[playerCoordinates.getX()][playerCoordinates.getY()].getTileCoordinates().getY()+Board.TILE_OFFSET);
    	Board.BOARD[playerCoordinates.getX()][playerCoordinates.getY()].changeTaken(true);
    }

    /**
     * Removes player from the game by setting the inGame boolean to false
     */
    public void addPlayer(String name) {
    	inGame = true;
    	playerName = name;
    }
    
    public void initNotepad(int index, int playerCount) {
    	notes = new Notepad(index, playerCount);
    	notes.setPreferredSize(new Dimension(Notepad.NOTEPAD_WIDTH, Notepad.NOTEPAD_HEIGHT));
    }
    
    public void removePlayer()
    {
        inGame = false;
        playerRemoved = true;
    }

    /**
     * @return true if player is still in game, false otherwise
     */
    public boolean isInGame() {
    	return inGame;
    }
    /**
     * @return the token of the player
     */
    public JLabel getToken() {
        return token;
    }
    
    public String getTokenName() {
    	return tokenName;
    }
    
    public String getPlayerName() {
    	return playerName;
    }
    
    public int getCurrRoomIndex() {
    	return currRoom;
    }
    
    //implement so that it also takes a room middle coordinate maybe and places piece there if taken, move right until empty slot found
    public int toRoom(int x, int y, int roomIndex) {

        if(prevRoom != roomIndex) {
            currRoom = roomIndex;
            while (Board.BOARD[x][y].isTaken()) {
                y++;
            }
            move(x - this.playerCoordinates.getX(), y - this.playerCoordinates.getY());
            this.inRoom = true;
        }
        else
        {
            return -1;
        }
    	return 1;
    }
    
    public void leaveRoom(Coordinate moveTo) {
        Board.BOARD[playerCoordinates.getX()][playerCoordinates.getY()].changeTaken(false);
        this.playerCoordinates.setCoordinates(moveTo.getX(), moveTo.getY());
        this.token.setLocation(Board.BOARD[playerCoordinates.getX()][playerCoordinates.getY()].getTileCoordinates().getX()+Board.TILE_OFFSET,
                                Board.BOARD[playerCoordinates.getX()][playerCoordinates.getY()].getTileCoordinates().getY()+Board.TILE_OFFSET);
        Board.BOARD[playerCoordinates.getX()][playerCoordinates.getY()].changeTaken(true);
        this.currRoom = 15;
        this.inRoom = false;
    }

    public void setInRoom(boolean bool)
    {
        this.inRoom = bool;
    }
    
    public boolean isInRoom() {
    	return inRoom;
    }

    public void assignCard(Card card)
    {
        cards.add(card);
        notes.crossOff(card.getCardName(), EntryType.OWNED);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
    
    public Notepad getNotes() {
    	return notes;
    }

    public int getPrevRoomIndex()
    {
        return this.prevRoom;
    }
    
    public void setPrevRoom(int x) {
    	prevRoom = x;
    }

    public boolean isPlayerRemoved()
    {
        return playerRemoved;
    }
}

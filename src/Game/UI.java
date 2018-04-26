package Game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import Board.Board;
import Card.Card;
import Command.CommandPanel;
import Command.InfoPanel;
import Notes.Notepad;
import PlayerTokens.Player;
import Weapon.Weapon;

public class UI {
	//declaring instance variables
	private final CommandPanel commandPanel = new CommandPanel();
	private final InfoPanel infoPanel = new InfoPanel();
	private final BoardLayeredPane layeredPane;
	private final JFrame frame;
	private int currNotes;
	
	//this constructor sets the board and adds our desired look to the board(size,colour etc)
	public UI(Board board, Player[] players, Weapon[] weapons, Card[] cards) {
		layeredPane = new BoardLayeredPane(board, players, weapons, cards);
		frame = new JFrame();
        frame.setBackground(Color.black);
        frame.setLayout(new BorderLayout());
        frame.add(layeredPane, BorderLayout.CENTER);
        frame.add(commandPanel, BorderLayout.PAGE_END);
        frame.add(infoPanel, BorderLayout.LINE_END);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}

	public String getCommand() {
        return commandPanel.getCommand();
    }

    public void display() {
        layeredPane.refresh();
    }

    public void displayText(String string) {
        infoPanel.addText(string);
    }
    
    public void clearInfoPanel() {
    	infoPanel.clearTextArea();
    }
    
    public void removePlayer(Player player) {
    	player.removePlayer();
    	layeredPane.removeToken(player);
    }
    
    public void addPlayer(Player player, String name) {
    	player.addPlayer(name);
    	layeredPane.addToken(player);
    }
    
    public void openNotes(Player player,int i) {
    	frame.add(player.getNotes(), BorderLayout.LINE_START);
    	frame.revalidate();
    	frame.repaint();
        frame.pack();
        frame.setLocationRelativeTo(null);
        currNotes = i;
    }
    
    public void closeNotes(Player player, int i) {
    	if(i == currNotes) {
    		frame.getContentPane().remove(player.getNotes());
    		frame.revalidate();
        	frame.repaint();
    		frame.pack();
    		frame.setLocationRelativeTo(null);
    	}
    }
}

package Game;
import java.awt.Dimension;

import javax.swing.JLayeredPane;
import Board.Board;
import Card.Card;
import PlayerTokens.Player;
import Weapon.Weapon;

public class BoardLayeredPane extends JLayeredPane {
	
	public BoardLayeredPane(Board board, Player[] players, Weapon[] weapons, Card[] cards) {
		super(); //calling constructor from superclass
		setPreferredSize(new Dimension(board.getWidth(), board.getHeight()));
		add(board, JLayeredPane.DEFAULT_LAYER);
		
		for(int i=0; i<weapons.length; i++) { 
			add(weapons[i].getToken(), JLayeredPane.DRAG_LAYER); //adding weapon tokens to the board
		}
		
	}
	
	public void refresh() {
		revalidate();
		repaint();
	}
	
	//removes and refreshes player token
	public void removeToken(Player player) {
		remove(player.getToken());
		refresh();
	}
	
	//adds a token gto the game and refreshes it
	public void addToken(Player player) {
		add(player.getToken(), JLayeredPane.DRAG_LAYER);
		refresh();
	}
}

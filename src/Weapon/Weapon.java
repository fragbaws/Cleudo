package Weapon;
import Board.*;

/*
	MCINO - Kamil Cierpisz, Nikolaj Jasenko, Temitope Akinwale
*/

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Weapon extends JLabel {

    public static final int TOKEN_SIZE_2 = 55; //token size will remain the same throughout
    private Coordinate weaponCoordinates;
    private String weapon_name;
    private JLabel token; // weapon's token image
    private int currRoom;

    //Weapon constructor containing the current co-ordinates
    public Weapon(int initial_x, int initial_y, String path, String name, int currRoom) throws IOException {
        //letting the current co-ordinates equal the actual co-ordinates 
        weaponCoordinates = new Coordinate(initial_x,initial_y, true);
        this.currRoom = currRoom;
        weapon_name = name;
        InputStream in2 = this.getClass().getResourceAsStream(path);
        BufferedImage tokenImg = ImageIO.read(in2); //reading in our image
        this.token = new JLabel(new ImageIcon(tokenImg));
        token.setSize(TOKEN_SIZE_2, TOKEN_SIZE_2); //setting the size of token
        token.setLocation(Board.BOARD[weaponCoordinates.getX()][weaponCoordinates.getY()].getTileCoordinates().getX() + Board.TILE_OFFSET, Board.BOARD[weaponCoordinates.getX()][weaponCoordinates.getY()].getTileCoordinates().getY() + Board.TILE_OFFSET);
        Board.BOARD[weaponCoordinates.getX()][weaponCoordinates.getY()].changeTaken(true);
    }
    /**
     * Changes the weapons position by translating it based on input arguments, it also changes the tokens display location
     * @param x_movement
     * @param y_movement
     */
    public void move(int x_movement, int y_movement) {
        Board.BOARD[weaponCoordinates.getX()][weaponCoordinates.getY()].changeTaken(false);
        weaponCoordinates.changeCoordinates(x_movement,y_movement);
        token.setLocation(Board.BOARD[weaponCoordinates.getX()][weaponCoordinates.getY()].getTileCoordinates().getX()+Board.TILE_OFFSET, Board.BOARD[weaponCoordinates.getX()][weaponCoordinates.getY()].getTileCoordinates().getY()+Board.TILE_OFFSET); //setting exacr location of the move
        Board.BOARD[weaponCoordinates.getX()][weaponCoordinates.getY()].changeTaken(true);
    }

    public void moveToRoom(int x, int y,int roomIndex)
    {
        this.currRoom = roomIndex;
        move(x-this.weaponCoordinates.getX(), y-this.weaponCoordinates.getY());

    }

    public String getName() {
    	return weapon_name;
    }
    
    //gets token
    public JLabel getToken() {
        return token;
    }

    public Coordinate getWeaponCoordinates() {
        return weaponCoordinates;
    }

    public int getCurrRoom()
    {
        return this.currRoom;
    }

}



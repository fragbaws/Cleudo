package Board;

/*
	MCINO - Kamil Cierpisz, Nikolaj Jasenko, Temitope Akinwale
*/

import javax.imageio.ImageIO;
import javax.swing.*;

import PlayerTokens.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.InputStream;
import java.util.Scanner;

/**
 * The Board class which initialises and paints the board
 * @author Mcino
 *
 */
public class Board extends JPanel {

    //instance variables 
    private static final int TILE_SIZE = 29; //tile size set as final as it wont change throughout the program
    public static final int TILE_OFFSET = (TILE_SIZE-Player.TOKEN_SIZE)/2; //tile offset is the offset for tokens to paint them in the centre of a tile
    public static final int BOARD_WIDTH = TILE_SIZE*26; //Board has to match the tile grid so hence it's a multiple of the tile size
    public static final int BOARD_HEIGHT = TILE_SIZE*27;
    public static final Tile[][] BOARD = new Tile[25][24]; //The backend of the board is stored in an array of Tiles
    private BufferedImage boardImg; //will store the board img

    //Board constructor
    public Board() {
        String boardTxt = "assets/board string.txt"; //the path for the text file containing tile layout
        String board = "assets/board.jpg"; //the path for the board image
        InputStream in = this.getClass().getResourceAsStream(boardTxt); //opening the text file
        InputStream in2 = this.getClass().getResourceAsStream(board); //opening the board image
        Scanner input = null; //scanner to read the board string
        
        //try catch exception handling
        try {
            input = new Scanner(in); //initialising the input scanner to the board string text file
            boardImg = ImageIO.read(in2); 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        //reading the text file and initialising the Tile array
        for(int i=0; i<BOARD.length; i++) { 
            for(int j=0; j<BOARD[0].length; j++) {
                BOARD[i][j] = new Tile((j+1)*TILE_SIZE, (i+1)*TILE_SIZE, input.nextInt()); //creating an instance of a tile using the tile type provided by input
            }
        }
        input.close();

    }

    public void paintComponent(Graphics g) {
        //Create Graphics2D object, cast g as a Graphics2D
        super.paintComponent(g); //overriding the definition of paintComponent
        Graphics2D g2 = (Graphics2D) g; //casting to graphics2d
        g2.drawImage(boardImg, 0, 0, BOARD_WIDTH, BOARD_HEIGHT, null); //drawing the board image
    }

    /**
     * @return the width of the board
     */
    public int getWidth() {
        return BOARD_WIDTH;
    }
    
    /**
     * @return the height of the board
     */
    public int getHeight() {
        return BOARD_HEIGHT;
    }
}


//TODO TRY CHANGE PUBLIC INSTANCE VARIABLES TO PRIVATE IF POSSIBLE
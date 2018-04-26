package Game;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import Board.*;

import Card.Card;
import Notes.EntryType;
import PlayerTokens.*;
import Weapon.*;

public class Cluedo {

	public static Player[] players = new Player[6];
	private Weapon[] weapons = new Weapon[6];
	private static UI ui;
	private static int playerCount;
	private int playersInGame;
	private static int cards_pp; //cards per player
	private String commands = "";
	private static Room[] rooms = new Room[9];
	private static Card[] cards = new Card[21];
	private static Card[] murderEnvelope = new Card[3];
	private static String winner = null;

	private boolean gameOver = false;
	
	//string which will store the questions and responses throughout the game
	private static String gameLog = "Game Log:" +
			"\n---------------\n";

	Cluedo() {
		Board board = new Board();

		//setting up the rooms with weapon slots and player slots as well as number of exits
		rooms[0] = new Room("Kitchen", 1,0, 4,0, 1, true);
		rooms[1] = new Room("Ball Room", 1,11, 5,9, 4, false);
		rooms[2] = new Room("Conservatory", 1,22, 3,19, 1, true);
		rooms[3] = new Room("Billiard Room", 8,22, 10,19, 2, true);
		rooms[4] = new Room("Library", 14,21, 16,19, 2, true);
		rooms[5] = new Room("Study", 23,22, 22,18, 1, false);
		rooms[6] = new Room("Hall", 23,11, 21,10, 3, true);
		rooms[7] = new Room("Lounge", 23,0, 21,1, 1, false);
		rooms[8] = new Room("Dining Room", 9,0, 12,1, 2, true);


		//setting up the exits coordinates for each room
		rooms[0].exitCoordinate[0] = new Coordinate(7,4, false);
		rooms[1].exitCoordinate[0] = new Coordinate(5,7, false);
		rooms[1].exitCoordinate[1] = new Coordinate(8,9, false);
		rooms[1].exitCoordinate[2] = new Coordinate(8,14, false);
		rooms[1].exitCoordinate[3] = new Coordinate(5,16, false);
		rooms[2].exitCoordinate[0] = new Coordinate(5,18, false);
		rooms[3].exitCoordinate[0] = new Coordinate(9,17, false);
		rooms[3].exitCoordinate[1] = new Coordinate(13,22, false);
		rooms[4].exitCoordinate[0] = new Coordinate(13,20, false);
		rooms[4].exitCoordinate[1] = new Coordinate(16,16, false);
		rooms[5].exitCoordinate[0] = new Coordinate(20,17, false);
		rooms[6].exitCoordinate[0] = new Coordinate(20,15, false);
		rooms[6].exitCoordinate[1] = new Coordinate(17,12, false);
		rooms[6].exitCoordinate[2] = new Coordinate(17,11, false);
		rooms[7].exitCoordinate[0] = new Coordinate(18,6, false);
		rooms[8].exitCoordinate[0] = new Coordinate(12,8, false);
		rooms[8].exitCoordinate[1] = new Coordinate(16,6, false);


		try {
			//setting up the cards
			cards[0] = new Card("Scarlett");
			cards[1] = new Card("Mustard");
			cards[2] = new Card("Green");
			cards[3] = new Card("White");
			cards[4] = new Card("Peacock");
			cards[5] = new Card("Plum");

			cards[6] = new Card("Revolver");
			cards[7] = new Card("Rope");
			cards[8] = new Card("Pipe");
			cards[9] = new Card("Poison");
			cards[10] = new Card("Knife");
			cards[11] = new Card("Candlestick");

			cards[12] = new Card("Dining Room");
			cards[13] = new Card("Kitchen");
			cards[14] = new Card("Hall");
			cards[15] = new Card("Conservatory");
			cards[16] = new Card("Library");
			cards[17] = new Card("Study");
			cards[18] = new Card("Ball Room");
			cards[19] = new Card("Billiard Room");
			cards[20] = new Card("Lounge");


			//each weapon occupies a slot in the array so as to easily rotate between them for move demo
			weapons[0] = new Weapon(rooms[0].getWeaponSlotCoordinates().getX(), rooms[0].getWeaponSlotCoordinates().getY(), "assets/candlestick.png", "Candlestick", 0);
			weapons[1] = new Weapon(rooms[2].getWeaponSlotCoordinates().getX(), rooms[2].getWeaponSlotCoordinates().getY(), "assets/knife.png", "Knife", 2);
			weapons[2] = new Weapon(rooms[4].getWeaponSlotCoordinates().getX(), rooms[4].getWeaponSlotCoordinates().getY(), "assets/poison.png", "Poison", 4);
			weapons[3] = new Weapon(rooms[6].getWeaponSlotCoordinates().getX(), rooms[6].getWeaponSlotCoordinates().getY(), "assets/revolver.png", "Revolver", 6);
			weapons[4] = new Weapon(rooms[8].getWeaponSlotCoordinates().getX(), rooms[8].getWeaponSlotCoordinates().getY(), "assets/rope.png", "Rope", 8);
			weapons[5] = new Weapon(rooms[3].getWeaponSlotCoordinates().getX(), rooms[3].getWeaponSlotCoordinates().getY(), "assets/pipe.png", "Pipe", 3);


			//each player given a position in the players array so as to easily keep track of turns
			players[0] = new Player(0, 9, "assets/white.png", "White");
			players[1] = new Player(0, 14, "assets/green.png", "Green");
			players[2] = new Player(17, 0, "assets/mustard.png", "Mustard");
			players[3] = new Player(6, 23, "assets/peacock.png", "Peacock");
			players[4] = new Player(19, 23, "assets/plum.png", "Plum");
			players[5] = new Player(24, 7, "assets/scarlett.png", "Scarlett");

			//exception handling
		} catch (IOException e) {
			e.printStackTrace();
		}


		Scanner input = new Scanner(this.getClass().getResourceAsStream("assets/commands.txt"));
		while(input.hasNext())
			commands += input.nextLine() + "\n";
		input.close();
		ui = new UI(board, players, weapons, cards);
	}

	
	
	
	
	//Sets up the players and tokens for the game
	public void setup() throws InterruptedException {
		//getting a number of players from the user between 2 and 6
		while(playerCount < 2 || playerCount > 6) {
			ui.displayText("Please enter the number of players between 2 and 6.");

			//catching an exception if input is not an integer
			try {
				playerCount = Integer.parseInt(ui.getCommand());
				playersInGame = playerCount;
			}
			catch(NumberFormatException e) {
				ui.displayText("The input must be an integer value.");
			}
		}
		
		cards_pp = 18 / playerCount;
		ui.displayText("The number of players is " + playerCount);

		String input; //String variable reserved for player input
		String name; //holds the name of each player until passed to a Player type

		//Looping through each player to get everyone's details
		for(int i=0; i<playerCount; i++) {
			ui.displayText("Enter the name of Player " + (i+1));
			name = ui.getCommand(); //name cannot be quit as it terminates game!
			ui.displayText("Player "+(i+1)+ "'s name is " + name);
			ui.displayText("Select Player " +(i+1)+ "'s token. Enter 'tokens' to see your options.");

			boolean selected = false; //boolean value that is set to true when a player selects a valid token, controls the following while loop
			while(!selected) {
				input = ui.getCommand();

				if(input.equalsIgnoreCase("tokens"))
					ui.displayText("White\n"
							+ "Green\n"
							+ "Mustard\n"
							+ "Peacock\n"
							+ "Plum\n"
							+ "Scarlett\n"
							+ "Please select on of the above options.\n");
				else {

					//loop through all player objects until a matching token is found
					for(int j=0; j<players.length; j++) {

						if(input.equalsIgnoreCase(players[j].getTokenName())) {
							if(!players[j].isInGame()) {
								ui.addPlayer(players[j], name);
								ui.displayText(players[j].getPlayerName() + " chose " + players[j].getTokenName());

								//putting the chosen player in the correct order in the array by swapping
								if(i != j) {
									Player temp = players[i];
									players[i] = players[j];
									players[j] = temp;
								}

								selected = true;
								j=players.length; //token selected so we set j to size of array in order to exit loop
							}
							else if(players[j].isInGame()) {
								ui.displayText("This token has been taken by " + players[j].getPlayerName() + ". Please choose a different one.");
								j=players.length; //token has been matched but is taken, j set to size of array in order to exit loop
							}
						}
					}

					//if token not selected we let the player know
					if(!selected)
						ui.displayText("Please enter a valid or not taken token. Enter 'tokens' to see token names.");
				}
			}
		}

		orderRoll(); //must be called before cards are dealt since notepads aren't initialised until then
		handOutCards();
	}

	
	
	
	
	//rolls for players to decide on first turn
	private void orderRoll() throws InterruptedException {
		ui.displayText("Rolling the die to determine the first player.");
		Random rand = new Random();
		int[] playerInds = new int[playerCount]; //an array holding indices of players with equal roll
		int size = 0; //the number of players who have rolled the largest roll
		int temp_size = playerCount; //this is the size we use for the loop so that as int size changes, it doesn't affect the for loop
		int result;
		int max = 0; //stores the maximum roll

		//initially playerInds correspond to their own indices as all players are tied at the start
		for(int i=0; i<playerCount; i++) {
			playerInds[i] = i;
		}

		do {
			//looping through the playerInds
			for(int i=0; i<temp_size; i++) {
				result = rand.nextInt(6) + rand.nextInt(6) + 2; //roll
				TimeUnit.SECONDS.sleep(1); //wait a second so as to prevent instantaneous printing of results
				ui.displayText("Player " + (playerInds[i]+1) + " rolled " + result);

				//if new max is found, we set the first index to the index of the player who rolled it and set size to 1
				if(result > max) {
					max = result;
					playerInds[0] = playerInds[i];
					size = 1;
				}
				//if another player rolls the max, we include him in the next array slot
				else if(result == max)
					playerInds[size++] = playerInds[i];

			}

			temp_size = size; //set temp_size to the new size of the array so as to loop through correct players
			max = 0; //max must be reset for re-rolls
			if(size > 1)
				ui.displayText("Re-rolling tied players");
		}while(size > 1);

		//placing the player with highest roll at top of array while maintaining the remaining order
		Player temp;
		for(int i=playerInds[0]; i>0; i--) {
			temp = players[i];
			players[i] = players[i-1];
			players[i-1] = temp;
		}
		
		//after order is finally set we must set Notepads to store the correct playerIndex
		for(int i=0; i<playerCount;i++) {
			players[i].initNotepad(i, playerCount);
		}
	}

	
	
	
	
	//allocates cards to murder envelope and to each player
	public void handOutCards() throws InterruptedException{

		ui.displayText("Dealing cards into the murder envelope...");
		TimeUnit.SECONDS.sleep(1); //wait a second so as to prevent instantaneous printing of results

		Random rnd = new Random();
		//Places cards into murder Envelope
		int randomPlayer = rnd.nextInt(6);
		int randomWeapon = rnd.nextInt(11-6+1) +6;
		int randomRoom = rnd.nextInt(20-12+1)+12;

		murderEnvelope[0] = cards[randomPlayer];
		murderEnvelope[1] = cards[randomWeapon];
		murderEnvelope[2] = cards[randomRoom];
		cards[randomPlayer].setAssigned();
		cards[randomRoom].setAssigned();
		cards[randomWeapon].setAssigned();



		ui.displayText("Dealing cards to the players...");
		TimeUnit.SECONDS.sleep(1); //wait a second so as to prevent instantaneous printing of results

		for(int i =0; i<players.length; i++)
		{

			//ensures cards are dealt to players which are in game
			if(players[i].isInGame()) {

				//cards to be dealt per player
				int cardsPerPlayer = (cards.length-3) / playerCount;
				for(int count = 0;count<cardsPerPlayer;count++)
				{
					boolean assigned = false;
					do{
						int random = rnd.nextInt(cards.length);
						if(!cards[random].isAssigned())
						{
							players[i].assignCard(cards[random]);
							cards[random].setAssigned();
							if(cards[random].isAssigned())
							{
								assigned = true;
							}
						}
					}while(!assigned);

				}
			}
		}

		//Shows which cards have not been assigned
		for(int i=0;i<cards.length;i++)
		{
			if(!cards[i].isAssigned())
			{
				ui.displayText(cards[i].getCardName() + " has not been assigned to a player.");
				for(int j=0; j<6; j++) {
					if(players[j].isInGame())
						players[j].getNotes().crossOff(cards[i].getCardName(), EntryType.ALL_SEE);
				}
			}
		}
	}

	
	
	
	
	//Method in charge of a player's turn
	public void makeTurn(Player player, int index) {
		String input; //String variable reserved for player input

		//holding player's current position
		int row = player.getPlayerCoordinates().getX();
		int column = player.getPlayerCoordinates().getY();

		//set true if the player is in a room with a secret passage
		boolean passage = false;

		if(Board.BOARD[row][column].hasSecretPassage()) {
			ui.displayText(player.getPlayerName() + " (" + player.getTokenName() + "), it's your turn. You're in a room with a secret passage. "
					+ "Enter 'passage' to use it, 'roll' to roll the die instead, 'cheat' to peek into the murder envelope or 'log' to view the game log. For a list of commands "
					+ "and what they do simply enter 'help'");
			passage = true;
		}
		else {
			ui.displayText(player.getPlayerName() + " (" + player.getTokenName() + "), it's your turn. Enter 'roll' to roll the die, 'log' to view the game log or 'cheat' to peek into the murder envelope. For a list of commands and what they do"
					+ " simply enter 'help'");
		}

		boolean proceed = false;
		int moves = 0; //variable holding the number of moves the player has left
		int start_row = row;
		int start_column = column;
		
		//keeping track of whether player asked a question or not
		boolean q_asked = false;

		//Ensuring that player does not enter same room twice
		int check = 1;
			
		while(!proceed) {
			input=ui.getCommand();
			if(input.equalsIgnoreCase("help")) {
				ui.displayText(commands);
			}
			else if((passage && input.equalsIgnoreCase("passage"))) { //each passage room has a corresponding passage room which we must move to
				int roomIndex = Board.BOARD[row][column].getTileType();

				if (roomIndex == 0) {
					check = player.toRoom(rooms[5].getPlayerSlotCoordinates().getX(), rooms[5].getPlayerSlotCoordinates().getY(), 5);
				} else if (roomIndex == 5) {
					check = player.toRoom(rooms[0].getPlayerSlotCoordinates().getX(), rooms[0].getPlayerSlotCoordinates().getY(), 0);
				} else if (roomIndex == 2) {
					check = player.toRoom(rooms[7].getPlayerSlotCoordinates().getX(), rooms[7].getPlayerSlotCoordinates().getY(), 7);
				} else if (roomIndex == 7) {
					check = player.toRoom(rooms[2].getPlayerSlotCoordinates().getX(), rooms[2].getPlayerSlotCoordinates().getY(), 2);
				}

				if (check == -1) {
					ui.displayText("Cannot enter the room you already made a guess in twice in a row");
				} else {
					proceed = true;

					ui.displayText("You used a secret passage. If you wish to ask a question enter 'question' else enter 'done'\n"
							+ "Type 'help' to display the command list.\n");
					//giving the player who used the passage an option to ask question
					while (!(input = ui.getCommand()).equalsIgnoreCase("done")) {
						if (input.equalsIgnoreCase("question") && !q_asked) { // && player.getCurrRoomIndex() != player.getPrevRoomIndex()
							guess(player, index, player.getPlayerCoordinates().getX(), player.getPlayerCoordinates().getY(), player.getCurrRoomIndex());
							ui.displayText("The screen has been returned to you, " + player.getPlayerName() + " (" + player.getTokenName() + ").");
							player.setPrevRoom(player.getCurrRoomIndex());
							q_asked = true;
						} else if (input.equalsIgnoreCase("help"))
							ui.displayText(commands);
						else if (input.equalsIgnoreCase("cheat")) {
							ui.displayText("Murder Details:\n" +
									"---------------\n" +
									"Player: " + murderEnvelope[0].getCardName() + "\n" +
									"Weapon: " + murderEnvelope[1].getCardName() + "\n" +
									"Room: " + murderEnvelope[2].getCardName());
						}
						else if(input.equalsIgnoreCase("question") && q_asked) {
							ui.displayText("Already asked a question on this turn...");
						}
						
						ui.displayText("You used a secret passage. If you wish to ask a question enter 'question' else enter 'done'\n"
								+ "Type 'help' to display the command list.\n");
					}
				}
			}
			else if(input.equalsIgnoreCase("roll")) {

				if(player.isInRoom())
				{
					int currRoom = Board.BOARD[row][column].getTileType();
					if(rooms[currRoom].exitCount > 1) {
						do {
							ui.displayText("Please choose the exit you want to take (1-" + rooms[currRoom].exitCount + ")");
							input = ui.getCommand();
						}while(input.compareToIgnoreCase("1") < 0 || input.compareToIgnoreCase(String.valueOf(rooms[currRoom].exitCount)) > 0);
						int exitToTake = Integer.parseInt(input) - 1;
						player.leaveRoom(rooms[currRoom].exitCoordinate[exitToTake]);
					}
					else
						player.leaveRoom(rooms[currRoom].exitCoordinate[0]);

					//change reset position so that we don't reset into the room and prevent player from moving
					start_row = player.getPlayerCoordinates().getX();
					start_column = player.getPlayerCoordinates().getY();
				}


				proceed = true;
				Random rand = new Random();
				moves = rand.nextInt(6) + rand.nextInt(6) + 2;
				ui.displayText("You have rolled a " + moves
						+"\nPlease enter the set of moves you wish to make. Enter 'help' to look up the move commands.");


			}
			else if(input.equalsIgnoreCase("cheat")){
				ui.displayText("Murder Details:\n" +
						"---------------\n"+
						"Player: "+ murderEnvelope[0].getCardName()+"\n"+
						"Weapon: "+murderEnvelope[1].getCardName()+"\n"+
						"Room: "+murderEnvelope[2].getCardName());

				ui.displayText("Once you're done, type 'roll' to roll the dice or 'help' to see list of commands.");


			}
			else if(input.equalsIgnoreCase("log"))
			{
				ui.displayText(gameLog);
			}
			else
				ui.displayText("The command you entered does not exist or is not effective at the moment.\n");
		}

		//storing the player's initial number of moves for the reset command
		int original_moves = moves;
		
		//player moves around if he chose to roll, if passage used this is skipped
		while(moves != 0 && !player.isInRoom()) {
			input = ui.getCommand();
			if(input.equalsIgnoreCase("help")) {
				ui.displayText(commands + "\nEnter your set of moves now.");
				continue;
			}
			else if(input.equalsIgnoreCase("cheat"))
			{
				ui.displayText("Murder Details:\n" +
						              "---------------\n"+
								      "Player: "+ murderEnvelope[0].getCardName()+"\n"+
				                      "Weapon: "+murderEnvelope[1].getCardName()+"\n"+
				                      "Room: "+murderEnvelope[2].getCardName());
			}

			String[] moveSequence = input.split("\\s+"); //player has the choice of entering a sequence of moves in one go so we must split the input string up

			//looping through the sequence of moves the player entered
			for(int i=0; i<moveSequence.length && moves>0; i++) {
				row = player.getPlayerCoordinates().getX();
				column = player.getPlayerCoordinates().getY();

				String direction = null;
				int r_move=0, c_move=0, entrance_type=0;
				boolean inBounds = false;
				if(moveSequence[i].equalsIgnoreCase("u")) {
					direction = "up";
					r_move = -1;
					entrance_type = 11; //up entrance
					inBounds = row > 0;
				}
				else if(moveSequence[i].equalsIgnoreCase("d")) {
					direction = "down";
					r_move = 1;
					entrance_type = 12; //down entrance
					inBounds = row < 24;
				}
				else if(moveSequence[i].equalsIgnoreCase("r")) {
					direction = "right";
					c_move = 1;
					entrance_type = 13; //right entrance
					inBounds = column < 23;
				}
				else if(moveSequence[i].equalsIgnoreCase("l")) {
					direction = "left";
					c_move = -1;
					entrance_type = 10; //left entrance
					inBounds = column > 0;
				}
				else {
					ui.displayText("'" + moveSequence[i] + "' is not a valid move command.");
					break;
				}

				if(Board.BOARD[row][column].getTileType() == entrance_type ) {
					int roomIndex = Board.BOARD[row+r_move][column+c_move].getTileType();
					if(roomIndex == 14)
					{
						player.move(2, 0);
						accuse(player);
						return;
					}
					if(player.getPrevRoomIndex() == roomIndex)
					{
						ui.displayText("Cannot enter the same room twice in a row");
						break;
					}
					moves=0;
					player.toRoom(rooms[roomIndex].getPlayerSlotCoordinates().getX(), rooms[roomIndex].getPlayerSlotCoordinates().getY(), roomIndex);
					ui.displayText("You entered a room");
					break;
				}
				else if(inBounds && !Board.BOARD[row+r_move][column+c_move].isTaken() && Board.BOARD[row+r_move][column+c_move].getTileType() > 8
						&& Board.BOARD[row+r_move][column+c_move].getTileType() != 14) {
					moves--;
					player.move(r_move, c_move);
				}
				else {
					ui.displayText("Cannot move " +direction+ ", please enter the remaining moves again.");
					break;
				}
			}
			ui.displayText("Moves left: " + moves);

			do {
				ui.displayText("If you would like to reset your position and make different moves, enter 'reset'."
						+ "\nIf you are in a room and would like to ask a question enter 'question',"
						+ "\nIf you're done with your turn enter 'done',"
						+ "\nIf you wish to view the command list enter 'help',"
						+ "\nIf you would like to peek into the murder envelope enter 'cheat'"
						+ "\nIf you would like to check the game log enter 'log'"
						+ "\nOtherwise, press Enter anything else. (Staying in a room ends turn)");
				if((input=ui.getCommand()).equalsIgnoreCase("reset")) {
					if(!q_asked) {
						moves = original_moves;
						player.move(start_row-player.getPlayerCoordinates().getX(), start_column-player.getPlayerCoordinates().getY());
						player.setInRoom(false); //Ensures that if player was in room then resets the required booleans that tracks whether player is in Room or not.
						ui.displayText("You have reset your position. Moves left: " + moves);
					}
					else {
						ui.displayText("Cannot reset position after a question has been asked.");
					}
				}
				else if(input.equalsIgnoreCase("done"))
					return;
				else if(input.equalsIgnoreCase("help"))
					ui.displayText(commands);
				else if(input.equalsIgnoreCase("cheat"))
				{
					ui.displayText("Murder Details:\n" +
							"---------------\n"+
							"Player: "+ murderEnvelope[0].getCardName()+"\n"+
							"Weapon: "+murderEnvelope[1].getCardName()+"\n"+
							"Room: "+murderEnvelope[2].getCardName());
				}
				else if(input.equalsIgnoreCase("question")) {
					if(!q_asked && player.isInRoom()) {
						ui.clearInfoPanel();
						guess(player,index,player.getPlayerCoordinates().getX(), player.getPlayerCoordinates().getY(), player.getCurrRoomIndex());
						player.setPrevRoom(player.getCurrRoomIndex());
						q_asked = true;
						moves = 0;
					}
					else {
						ui.displayText("Cannot ask question as player is either not in a room or already asked one");
					}
				}
				else if(input.equalsIgnoreCase("log"))
				{
					ui.displayText(gameLog);
				}
			}while(!input.equalsIgnoreCase(""));


			if(!player.isInRoom() && moves != 0)
				ui.displayText("Enter your move commands or enter 'help' for list of move commands.");
		}
	}
	
	
	
	
	
	//method which asks the user for question input and goes around in order until a card is shown or it goes back to the player asking the question
	public void guess(Player player, int index, int row, int column, int roomIndex) {
		String input, suspect = "", weapon = "", room = rooms[players[index].getCurrRoomIndex()].name;
		boolean chosen = false;
		Weapon weaponSwap = null;
		
		//choosing suspect
		while(!chosen) {
			ui.displayText("Choose a suspect. For list of suspects enter 'suspects'.");
			input = ui.getCommand();
			if(input.equalsIgnoreCase("suspects"))
				ui.displayText("White\n"
							+ "Green\n"
							+ "Mustard\n"
							+ "Peacock\n"
							+ "Plum\n"
							+ "Scarlett");
			else {
				for(int i=0; i<6; i++) {
					if(input.equalsIgnoreCase(players[i].getTokenName())) {
						suspect = players[i].getTokenName();
						chosen = true;
						if(players[i].isInGame() && i != index)
						{
							players[i].toRoom(row, column, roomIndex); // moving suspect to same room as current player
						}
						break;
					}
				}
			}
		}
		
		//choosing weapon
		chosen = false;
		while(!chosen) {
			ui.displayText("Choose a weapon. For list of suspects enter 'weapons'.");
			input = ui.getCommand();
			if(input.equalsIgnoreCase("weapons"))
				ui.displayText("Revolver\n"
							+ "Pipe\n"
							+ "Rope\n"
							+ "Poison\n"
							+ "Candlestick\n"
							+ "Knife");
			else {
				for(int i=0; i<6; i++) {
					if(input.equalsIgnoreCase(weapons[i].getName())) {
						weapon = weapons[i].getName();
						chosen = true;
						Weapon weaponQuestioned = weapons[i];
						if(!rooms[player.getCurrRoomIndex()].getWeaponSlotCoordinates().getSlotTaken()) // moving weapons to the room the guess is being made from
						{
							rooms[weaponQuestioned.getCurrRoom()].getWeaponSlotCoordinates().removeSlotTaken();
							weaponQuestioned.moveToRoom(rooms[player.getCurrRoomIndex()].getWeaponSlotCoordinates().getX(), rooms[player.getCurrRoomIndex()].getWeaponSlotCoordinates().getY(), player.getCurrRoomIndex());
							rooms[player.getCurrRoomIndex()].getWeaponSlotCoordinates().setSlotTaken();
						}
						else {

							for (int j = 0; j < weapons.length; j++) {
								if (player.getCurrRoomIndex() == weapons[j].getCurrRoom()) { // trying to find the weapon that is in player's room
									weaponSwap = weapons[j];
									break;
								}
							}
							Coordinate tmp = new Coordinate(weaponSwap.getWeaponCoordinates().getX(), weaponSwap.getWeaponCoordinates().getY(), false); // temporary variables made to keep track of initial weapon coordinates
							int roomIndex_tmp = weaponSwap.getCurrRoom();

							weaponSwap.moveToRoom(weaponQuestioned.getWeaponCoordinates().getX(), weaponQuestioned.getWeaponCoordinates().getY(), weaponQuestioned.getCurrRoom());
							weaponQuestioned.moveToRoom(tmp.getX(), tmp.getY(), roomIndex_tmp);
						}

						break;
					}
				}
			}
		}
		
		notes(false, index); //close notes so other players don't see them durin questioning
		
		//updating the game log
		String guess = players[index].getPlayerName() + " (" + players[index].getTokenName() + ") guesses it was " + suspect + " with a " + weapon + " in the " + room + ".\n";
		gameLog += guess;
		
		int playerIndex;
		int count = 0; //counts which player is being questioned relative to the player making the guess to use for column in the players' notepad
		for(int i=0; i<6; i++) {
			playerIndex = (index + i + 1) % 6;
			if(!players[playerIndex].isInGame() && !players[playerIndex].isPlayerRemoved())
				continue; //skip over players who aren't in game
			
			ui.clearInfoPanel();
			ui.displayText("Press enter to confirm screen has been passed to " + players[playerIndex].getPlayerName() + " (" + players[playerIndex].getTokenName() + ").");
			ui.getCommand();
			notes(true, playerIndex); //open notes of player who's being questioned
			
			//if we return to the original player inform them and return
			if(playerIndex == index) {
				ui.displayText("None of the other players had the cards you asked about.");
				return;
			}
			ui.displayText(guess); //display guess to the player questioned
			
			ArrayList<Card> playerCards = players[playerIndex].getCards();
			boolean hasSuspect = false, hasWeapon = false, hasRoom = false;
			
			//determining whether the current player has any of the cards asked for
			for(int j=0; j<cards_pp; j++) {
				if(suspect.equalsIgnoreCase(playerCards.get(j).getCardName()))
					hasSuspect = true;
				else if(weapon.equalsIgnoreCase(playerCards.get(j).getCardName()))
					hasWeapon = true;
				else if(room.equalsIgnoreCase(playerCards.get(j).getCardName()))
					hasRoom = true;
			}
			
			//if player has at least one of the cards asked for we ask them to enter it
			if(hasSuspect || hasWeapon || hasRoom) {
				chosen = false;
				gameLog +=players[playerIndex].getPlayerName()  + " (" + players[playerIndex].getTokenName() + ") showed a card to " + players[index].getPlayerName()+"\n"; //updating gameLog
				
				//loop runs until player chooses a correct card to show
				while(!chosen) {
					ui.displayText("You have one of the cards asked for. Enter its name to show it to them. Your cards are the ones crossed off in red on your notepad. If you wish to see the guess again, enter 'guess'.");
					input = ui.getCommand();
					if(input.equalsIgnoreCase("guess")) { //display guess again
						ui.displayText(guess);
					}
					else if(input.equalsIgnoreCase(suspect) && hasSuspect) { //putting a tick for the suspect
						players[index].getNotes().fillEntry(EntryType.TICK, count, suspect, 1);
						guess = players[playerIndex].getPlayerName() + " showed you they have " + suspect;
						chosen = true;
					}
					else if(input.equalsIgnoreCase(weapon) && hasWeapon) { //putting a tick for the weapon
						players[index].getNotes().fillEntry(EntryType.TICK, count, weapon, 7);
						guess = players[playerIndex].getPlayerName() + " showed you they have " + weapon;
						chosen = true;
					}
					else if(input.equalsIgnoreCase(room) && hasRoom) { //putting a tick for the room
						players[index].getNotes().fillEntry(EntryType.TICK, count, room, 13);
						guess = players[playerIndex].getPlayerName() + " showed you they have " + room;
						chosen = true;
					}
					else { //invalid input msg
						ui.displayText("You do not have that card or it wasn't asked for.");
					}
				}
				notes(false, playerIndex); //close questioned player's notes before passing the device back to owner
				ui.clearInfoPanel(); //clear panel to hide any potential details
				ui.displayText("Return the screen to " + players[index].getPlayerName() + " (" + players[index].getTokenName() + "). Press Enter when screen has been passed.");
				ui.getCommand();
				notes(true, index); //open the guesser's notepad
				return;
			}
			else { //if player has none of the cards we mark ALL notepads but that player's
				int temp_count = playerCount-2; //used to mark the correct column in everyone's notepad
				int j;
				//go through all players but questioned player, starting from the player next in line
				for(int k=playerIndex+1; k<playerIndex+6; k++) {
					j = k%6;
					if(!players[j].isInGame()) { //skip if player is not in game
						continue;
					}
					players[j].getNotes().fillEntry(EntryType.CROSS, temp_count, suspect, 1);
					players[j].getNotes().fillEntry(EntryType.CROSS, temp_count, weapon, 7);
					players[j].getNotes().fillEntry(EntryType.CROSS, temp_count, room, 13);
					temp_count--; //decrement temp_count to correct it for the next player in line
				}
				gameLog += players[playerIndex].getPlayerName()  + " (" + players[playerIndex].getTokenName() + ") doesn't have " + suspect + ", " + weapon + " or " + room +"\n"; //updating gamelog
				do { //telling the user they have no cards and asking them to end turn
					ui.displayText("You do not have any of the cards asked for. Please enter 'done' to switch to next player.");
				}while(!(input = ui.getCommand()).equalsIgnoreCase("done"));
			}	
			notes(false, playerIndex); //close questioned player's notepad
			count++;
		}



	}


	public void accuse(Player player)
	{
		ui.clearInfoPanel(); // clears the panel

		ui.displayText("You entered the basement, make your accusations.");
		boolean chosen = false;
		String[] accusations = new String[3]; // 0 - suspect, 1 - weapon, 2 - room
		ui.displayText("To peek the murder envelope, enter 'cheat'");

		while(!chosen) { // asks user to enter a suspect
			ui.displayText("Choose a suspect. For list of suspects enter 'suspects'.");
			String input = ui.getCommand();
			if (input.equalsIgnoreCase("suspects"))
				ui.displayText("White\n"
						+ "Green\n"
						+ "Mustard\n"
						+ "Peacock\n"
						+ "Plum\n"
						+ "Scarlett");
			else if(input.equalsIgnoreCase("cheat")) {
				ui.displayText("Murder Details:\n" +
						"---------------\n" +
						"Player: " + murderEnvelope[0].getCardName() + "\n" +
						"Weapon: " + murderEnvelope[1].getCardName() + "\n" +
						"Room: " + murderEnvelope[2].getCardName());
			}
			else{
				for(int i =0;i<6;i++)
				{
					accusations[0] = players[i].getTokenName();
					if(accusations[0].equalsIgnoreCase(input))// breaks loop if input is valid
					{
						chosen = true;
						break;
					}
				}
			}
		}

		chosen = false;
		while(!chosen) { // asks user to enter a weapon
			ui.displayText("Choose a weapon. For list of weapons enter 'weapons'.");
			String input = ui.getCommand();
			if (input.equalsIgnoreCase("weapons"))
				ui.displayText("Revolver\n"
						+ "Pipe\n"
						+ "Rope\n"
						+ "Poison\n"
						+ "Candlestick\n"
						+ "Knife");
			else if(input.equalsIgnoreCase("cheat")) {
				ui.displayText("Murder Details:\n" +
						"---------------\n" +
						"Player: " + murderEnvelope[0].getCardName() + "\n" +
						"Weapon: " + murderEnvelope[1].getCardName() + "\n" +
						"Room: " + murderEnvelope[2].getCardName());
			}
			else{
				for(int i =0;i<6;i++)
				{
					accusations[1] = weapons[i].getName();
					if(accusations[1].equalsIgnoreCase(input)) // breaks loop once input is valid
					{
						chosen = true;
						break;
					}
				}
			}
		}


		chosen = false;
		while(!chosen) { // asks user to enter a room
			ui.displayText("Choose a room. For list of rooms enter 'rooms'.");
			String input = ui.getCommand();
			if (input.equalsIgnoreCase("rooms"))
				ui.displayText("Kitchen\n"
						+ "Ball Room\n"
						+ "Conservatory\n"
						+ "Billiard Room\n"
						+ "Library\n"
						+ "Study\n"
						+ "Hall\n"
						+ "Lounge\n"
						+ "Dining Room");
			else if(input.equalsIgnoreCase("cheat")) {
				ui.displayText("Murder Details:\n" +
						"---------------\n" +
						"Player: " + murderEnvelope[0].getCardName() + "\n" +
						"Weapon: " + murderEnvelope[1].getCardName() + "\n" +
						"Room: " + murderEnvelope[2].getCardName());
			}
			else{
				for(int i =0;i<9;i++)
				{
					accusations[2] = rooms[i].name;
					if(accusations[2].equalsIgnoreCase(input)) // breaks loop once input is valid
					{
						chosen = true;
						break;
					}
				}
			}
		}

		for(int i=0; i<murderEnvelope.length; i++) // used to  check user input with the murder envelope
		{
			if(!accusations[i].equalsIgnoreCase(murderEnvelope[i].getCardName())) // if input is not equal to the card in the murder envelope
			{
				ui.displayText(player.getPlayerName() +", "+accusations[i]+" is not in the murder envelope\n"+
																					"You have lost the game. Press enter to continue...");
				ui.removePlayer(player);
				playersInGame--;
				if(playersInGame == 1) // if one player left in the game, game ends
				{
					ui.clearInfoPanel();
					ui.displayText("Only one player left in game.");
					gameOver = true;
					return;
				}
				ui.getCommand();
				return;
			}
		}
		gameOver = true;
		winner = player.getPlayerName() + " (" + player.getTokenName() + ")";
	}
	//function which takes care of closing and opening the notes to avoid clutter in the rest of the code code
	public static void notes(boolean open, int index) {
		if(open) {
			SwingUtilities.invokeLater(new Runnable() {
		    	public void run() {
		    		ui.openNotes(players[index], index);
		    	}
			});
		}
		else {
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			    	ui.closeNotes(players[index], index);
			    }
			});
		}
	}

	public void congratsMsg(){
		ui.displayText(
				"You have won the game " + winner +
				"\n\nThank you for playing Cleudo by MCINO.\n"+
				"Created by: Kamil Cierpisz,\n Nikolaj Jasenko,\n Temitope Akinwale.");
	}
	
	public static void main(String[] args) throws InterruptedException{
		Cluedo game = new Cluedo(); //create the game object
		ui.displayText("WELCOME TO CLUEDO BY M.C.I.N.O");
		game.setup(); //setting up the game and getting first player's index
		ui.displayText("Press enter to continue...");
		ui.getCommand();
		ui.clearInfoPanel();
		ui.displayText("The game is starting. There are " + playerCount + " players in the game.");
		while(!game.gameOver) { //loops over TURNS

			for(int i=0; i<6 && !game.gameOver; i++) {
				if(game.playersInGame == 1)
				{
					game.congratsMsg();
					game.gameOver = true;
				}
				else if(players[i].isInGame()) {
					ui.displayText("Pass the screen to " + players[i].getPlayerName() + "(" + players[i].getTokenName() + "). Press enter to confirm screen has been passed on");
					ui.getCommand();
					notes(true, i);
					game.makeTurn(players[i], i);
					notes(false, i);
					ui.clearInfoPanel();
				}
			}
		}

		ui.clearInfoPanel();
		game.congratsMsg();
		ui.displayText("-------GAME OVER-------");
	}

}

//TODO DO NOT SHOW COMMANDS THAT ARE ONLY AVAILABLE TO USE WHEN IN THE ROOM
package Game;
import Board.Coordinate;

public class Room {

	public final String name;
	public final int exitCount;
	public final Coordinate[] exitCoordinate;
	private final Coordinate weaponSlotCoordinates;
	private final Coordinate playerSlotCoordinates;
	
	public Room(String name, int weaponSlotI, int weaponSlotJ, int playerSlotI, int playerSlotJ, int exitCount, boolean weaponInRoom){
		this.name = name;
		this.weaponSlotCoordinates = new Coordinate(weaponSlotI,weaponSlotJ, weaponInRoom);
		this.playerSlotCoordinates = new Coordinate(playerSlotI,playerSlotJ, false);
		this.exitCount = exitCount;
		this.exitCoordinate = new Coordinate[exitCount];
	}

	//returns weapon slot co-ordinate
	public Coordinate getWeaponSlotCoordinates() {
		return weaponSlotCoordinates;
	}

	//returns player slot co-ordinate
	public Coordinate getPlayerSlotCoordinates() {
		return playerSlotCoordinates;
	}


}

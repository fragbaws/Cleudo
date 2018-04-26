package Board;

/*
	MCINO - Kamil Cierpisz, Nikolaj Jasenko, Temitope Akinwale
*/

public class Tile {

    private final Coordinate tileCoordinates;
    private final int tileType; //integer representing tile type used for movement of tokens
    private boolean taken = false; // boolean to check if tile is occupied by player or not

    //Tile constructor
    public Tile(int x, int y, int tileType) {
        this.tileCoordinates = new Coordinate(x,y, false);
        this.tileType = tileType;
    }

    public Coordinate getTileCoordinates()
    {
        return tileCoordinates;
    }
    /**
     * @return the integer relating to the tileType, check "board string.txt" for tile types
     */
    public int getTileType() {
        return tileType;
    }

    /**
     * @return true is the tile is a room, false otherwise
     */
    public boolean isRoom() {
        return tileType < 9;
    }

    /**
     * @return true if the tile is occupied, false otherwise
     */
    public boolean isTaken(){
    	return taken;
    }

    public boolean hasSecretPassage() {
    	if(tileType == 0 || tileType == 2 || tileType == 5 || tileType == 7)
    		return true;
    	else
    		return false;
    }
    /**
     * Changes the taken status of the tile
     * @param change the boolean to which taken is to be set
     */
    public void changeTaken(boolean change)
    {
        this.taken = change; //(can be changed to take no parameters and simply have: this.taken = !this.taken)
    }
}
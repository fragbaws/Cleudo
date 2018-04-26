package Board;

public class Coordinate {

    private int x; // x-coordinate which used for row location
    private int y; // y-coordinate which used for column location
    private boolean slotTaken; // used for weapon class


    /**
     * Constructor for an objects coordinates on the board
     * @param x coordinate
     * @param y coordinate
     */
    public Coordinate(int x, int y, boolean slotTaken)
    {
        this.x = x;
        this.y = y;
        this.slotTaken = slotTaken;
    }


    /**
     * Accessors which return the objects x and y coordinates
     * @return
     */
    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    /**
     * Used when moving token across tiles on board
     * @param change_x
     * @param change_y
     */
    public void changeCoordinates(int change_x, int change_y)
    {
        this.x+=change_x;
        this.y+=change_y;
    }

    /**
     * Used when player token leaves a room after rolling a die
     * @param new_x
     * @param new_y
     */
    public void setCoordinates(int new_x, int new_y)
    {
        this.x = new_x;
        this.y = new_y;
    }

    public void setSlotTaken()
    {
        this.slotTaken = true;
    }
    public void removeSlotTaken()
    {
        this.slotTaken = false;
    }

    public boolean getSlotTaken()
    {
        return this.slotTaken;
    }
}

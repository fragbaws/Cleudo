package Card;

public class Card {

    private boolean assigned = false;
    private String cardName;

    public Card(String name)
    {
        cardName = name;
    }

    public boolean isAssigned(){ 
    	return assigned; 
    }

    public void setAssigned() {
        assigned = true; }

    public String getCardName() {
        return cardName;
    }
}

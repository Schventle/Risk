package risk;

import java.util.ArrayList;

/**
 *
 * @author jkleifges9087
 */
public class Player {
    ArrayList<Card> hand;
    int ID;
    boolean lost;
    
    public Player(int i){
        this.ID = i;
        this.hand = new ArrayList();
        this.lost = false;
    }
    
    public void addCard(Card c){
        hand.add(c);
    }
    public void removeCard(Card c){
        hand.remove(c);
    }
    @Override
    public String toString(){
      String temp = "Player " + this.ID;
      return temp;
    }
}

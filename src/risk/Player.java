package risk;

import java.util.ArrayList;

/**
 *
 * @author jkleifges9087
 */
public class Player {
    ArrayList<Card> hand;
    
    public Player(){
        
    }
    
    public void addCard(Card c){
        hand.add(c);
    }
    public void removeCard(Card c){
        hand.remove(c);
    }
    
}

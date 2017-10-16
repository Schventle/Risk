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
    public Card discard(Card c){
        return hand.remove(hand.indexOf(c));
    }
    public boolean hasCardSet(){
      int inf = 0;
      int cav = 0;
      int art = 0;
      
      for(Card c: hand){
        if(c.type == CardType.Infantry || c.type == CardType.Wild){
          inf++;//count num inf + num wild
        }
        if(c.type == CardType.Cavalry || c.type == CardType.Wild){
          cav++;//count num cav + num wild
        }
        if(c.type == CardType.Artillery || c.type == CardType.Wild){
          art++;//count num art + num wild
        }
      }
      if(inf > 2){//if there are three infantry
        moveToEnd(CardType.Infantry);//move those cards to the end of the hand
        return true;
      }
      if(cav > 2){//if there are three cavalry
        moveToEnd(CardType.Cavalry);//move those cards to the end of the hand
        return true;
      }
      if(art > 2){//if there are three artillery
        moveToEnd(CardType.Artillery);//move those cards to the end of the hand
        return true;
      }
      return false;//if there are no sets of three, return false,
    }
    private void moveToEnd(CardType c){
      int[] temp = new int[3];
      int k = 0;
      for(int i = 0; i < hand.size();++i){
        if(hand.get(i).type == c || hand.get(i).type == CardType.Wild){//if in marked set
          temp[k] = i;//mark its index
          k++;
        }
      }
      for(int i = 3; i > 0; --i){
        hand.add(hand.remove(temp[0]));//move the marked cards to the end, end to front
      }
    }
    public void discardSet(){
      hand.remove(hand.size());//remove the last three cards in the hand
      hand.remove(hand.size());
      hand.remove(hand.size());
    }
    @Override
    public String toString(){
      String temp = "Player " + this.ID;
      return temp;
    }
    
}

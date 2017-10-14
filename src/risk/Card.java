package risk;
/**
 *
 * @author jkleifges9087
 */

public class Card {
    int ID; //corresponds to the region name, 42 and 43 are wild
    CardType type;

    public Card(int i){
      this.ID = i;
      if(i < 14){
        this.type = CardType.Infantry;
      }else if(i < 28){
        this.type = CardType.Cavalry;
      }else if(i < 42){
        this.type = CardType.Artillery;
      }else{
        this.type = CardType.Wild;
      }
    }
}

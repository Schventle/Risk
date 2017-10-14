package risk;
/**
 *
 * @author jkleifges9087
 */
enum CardType {Artillery, Cavalry, Infantry, Wild}
public class Card {
    int ID; //corresponds to the region name, 43 and 44 are wild
    CardType type;

    public card(int i){
      this.ID = i;
      if(i < 14){
        this.type = Infantry;
      }else if(i < 28){
        this.type = Cavalry;
      }else if(i < 42){
        this.type = Artillery;
      }else{
        this.type = Wild;
      }
    }
}

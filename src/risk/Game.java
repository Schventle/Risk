package risk;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jkleifges9087
 */
public class Game {
    private Player[] table;
    private Region[] map;
    private int numPlayers;
    private ArrayList<Card> deck;

    public Game(){
        this.numPlayers = getNumPlayers();
        this.table = makeTable(this.numPlayers);
        this.map = makeMap();
        this.deck = makeDeck();
    }
    public void play(){
      
    }
    private ArrayList<Card> makeDeck(){
      ArrayList temp = new ArrayList();
      for(int i = 0; i < 44; ++i){
        // dad's change for git
        temp.add(new Card(i));
      }
      temp = shuffle(temp);
      return temp;
    }
    private ArrayList<Card> shuffle(ArrayList<Card> in){
      ArrayList<Card> temp = new ArrayList();
      for(int i = 0; i < 44; ++i){
        int rand = (int) (Math.random() * in.size());
        temp.add(in.get(rand));
        in.remove(rand);
      }
      return temp;
    }
    private Player[] makeTable(int n){
        Player[] temp = new Player[n];
        for(int i = 0; i < n; n++){
            temp[i] = new Player(i);
        }
        return temp;
    }
    private Region[] makeMap(){
        Region[] temp = new Region[42];
        Scanner sc;
        String fileName = "H:\\Senior_Year\\Risk\\Risk\\Run_Memory";
        try {
            sc = new Scanner(new File(fileName));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println();
            sc = new Scanner(System.in);
        }

        for(Region r: map){
            r = new Region(sc.next(), sc.next());
        }
        return temp;
    }
    private int getNumPlayers(){
        System.out.println("Enter number of players, 2 - 6");
        int temp = enterInt(2,6);
        return temp;
    }//returns an entered number of players
    private int enterInt(int l, int h){
        String wrong = "What have you done?";
        Scanner sc = new Scanner(System.in);
        int temp = 0;
        while(true){
            if(sc.hasNextInt()){
                temp = sc.nextInt();
            }else{
                sc.next();
            }
            if(temp >= l || temp <= h){
                break;
            }
            System.out.println(wrong);
        }
        return temp;
    }//returns int between l and h
    private void reinforce(){
        for(Player p: this.table){
            int reinforcements = getReinforcements(p);
            while(reinforcements > 0){
                reinforcements -= distributeTroops( enterInt(0,41), enterInt(1, reinforcements));
            }
        }
    }
    private int getReinforcements(Player p){
        int temp = 0;
        int reinforcements = 0;

        for(Region r: map){
            if(r.owner == p){
                temp++;
            }
        }

        reinforcements += (temp / 3);

        return reinforcements;
    }
    }
    private int distributeTroops(int targetRegion, int amount){
        map[targetRegion] += amount;
        return amount;
    }
    public Region getRegion(int regionID){
      return map[regionID];
    }
}

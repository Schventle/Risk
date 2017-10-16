package risk;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
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
    int numCardTroops = 6;

    public Game(){
      this.numPlayers = getNumPlayers();
      this.table = makeTable(this.numPlayers);
      this.map = makeMap();
      this.deck = makeDeck();
    }
    
    public void setup(){
      int i = 0;
      for(Card c: deck){
        if(c.type != CardType.Wild){
          map[c.ID].owner = table[i % table.length];
        }
        i++;
      }
      for(Region r: map){
        r.garrison = 1;
      }
      for(Player p: table){
        System.out.println(p);
        printOwnedRegions(p);
        setupTroops(p);
      }
    }
    
    public void play(){
        for(Player p: table){
          System.out.println(p);
          System.out.println("You own the following regions:");
          printOwnedRegions(p);
          
          boolean flag = true;
          int startRegions = countRegions(p);
          while(flag){
          int action;
          System.out.println("Enter your action:" + "\n"
                  + "1 to attack "
                  + "2 for intel "
                  + "3 to end turn");
          action = enterInt(1,3);
          switch(action){
            case 1:
              attack(p);
              break;
            case 2:
              intel();
              break;
            case 3:
              if(endTurn()){
                flag = false;
              }
              break;
          }
          int endRegions = countRegions(p);
          if(startRegions < endRegions){
            drawCard(p);
          }
        }
        troopTransfer(p);
      }
      reinforce();
    }
    
    private void printOwnedRegions(Player p){
      for(int i = 0; i < 41; ++i){
        if(map[i].owner == p){
          System.out.println(i +  " " + map[i].name);
        }
      }
    }
    private void setupTroops(Player p){
      int numTroops = 50 - (5 * numPlayers);
      while(numTroops > 0){
        System.out.println("Enter a region to drop troops into\n"
                + "you have " + numTroops + "troops remaining");
        numTroops -= dropTroops(numTroops, p);
      }
    }
    private int dropTroops(int maxTroops, Player p){
      System.out.println("Enter a number of troops to be moved");
      int amount = enterInt(1,maxTroops);
      System.out.println("Enter a target Region");
      int target;
      while(true){
        target = enterInt(0,41);
        if(map[target].owner == p){
          break;
        }else{
          System.out.println("You don't own that region");
        }
      }
      distributeTroops(target, amount);
      return amount;
    }
    private void drawCard(Player p){
      p.addCard(pickUpCard());
    }
    private Card pickUpCard(){
      return deck.remove(0);
    }
    private int countRegions(Player p){
      int numRegions = 0;
      for(Region r: map){
        if(r.owner == p){
          ++numRegions;
        }
      }
      return numRegions;
    }
    private void troopTransfer(Player p){
      int fromRegion;
      int toRegion;
      int amount;
      while(true){
        System.out.println("You may now transfer troops between regions.\n"
              + "select a region to move from, move to, "
              + "and the amount of troops,\n"
              + "or enter 0 for the amount to skip");
        fromRegion = enterInt(0,41);
        toRegion = enterInt(0, 41);
        amount = enterInt(0, map[fromRegion].garrison);
      System.out.println(map[fromRegion].owner == p);
      System.out.println(map[toRegion].owner == p);
      System.out.println(hasPath(fromRegion, toRegion, p));
        if(map[fromRegion].owner == p && map[toRegion].owner == p && hasPath(fromRegion, toRegion, p)){
          System.out.println("Path found");
          break;
        }else{
          System.out.println("What have you done?");
        }
      }
      map[fromRegion].garrison -= amount;
      map[toRegion].garrison += amount;
    }
    private boolean hasPath(int f, int t, Player p){//breadth first path search
      ArrayList<Integer> a = new ArrayList();
      a.add(f);
      Region from = map[f];
      Region to =   map[t];
      for(int i = 0; i < a.size(); ++i){
        Region temp = map[i];
        if(temp.owner == p){
          return true;
        }
        
        for(int k = 0; k < temp.borders.length; ++k){
          if(map[temp.borders[k]].owner == p){
            if(!a.contains(k)){
              a.add(k);
              System.out.println("Added " + map[k].name);
            }
          }
        }
      }
      
      return false;
    }
    private void attack(Player p){
      System.out.println("You are attacking;\n"
              + "enter 2 numbers, "
              + "one indicating the attacking region, "
              + "the other the defending region");
      int atk = enterInt(0,41);
      int def = enterInt(0,41);
      
      if(validAttack(atk,def, p)){
        battle(atk,def);
        System.out.println(map[atk]);
        System.out.println(map[def]);
      }else{
        System.out.println("Invalid attack");
      }
      
      
      if(map[def].garrison <= 0){//if the defender loses
        occupy(atk,def,p);       //the territory changes hands
      }
    }
    private void occupy(int atk, int def, Player attaker){
      System.out.println(map[def].name + " has changed hands.\n"
              + "How many troops do you want to occupy with?\n"
              + "Enter a number between 1 and " + (map[atk].garrison - 1));
      int amount = enterInt(1, map[atk].garrison - 1);
      
      map[def].owner = attaker;
      
      map[atk].garrison -= amount;
      map[def].garrison = amount;
    }
    private void intel(){
      System.out.println("Regional Intel:\n"
              + "enter a region ID to be observed, 0 - 41");
      int view  = enterInt(0, 41);
      System.out.println(map[view]);
    }
    private boolean endTurn(){
      System.out.println("Are you sure you want to end your turn?\n"
              + "Yes of No");
      boolean temp = YNConfirm();
      return temp;
    }
    private boolean YNConfirm(){
      Scanner sc = new Scanner(System.in);
      String temp;
      while(true){
        temp = sc.next();
        if(temp.compareToIgnoreCase("yes") == 0 || temp.compareToIgnoreCase("no") == 0){
          break;
        }else{
          System.out.println("What have you done?");
        }
      }
      if(temp.compareToIgnoreCase("yes") == 0){
        return true;
      }else{
        return false;
      }
    }
    private boolean validAttack(int atk, int def, Player p){
      /*
      four things can invalidate an attack:
        attacker doesn't own the attacking region
        attacker owns defending region
        attacker doesn't have enough troops to attack
        attacking and defending region don't border
      */
      if(map[atk].owner != p){
        return false;
      }
      if(map[atk].owner == map[def].owner){
        return false;
      }
      if(map[atk]. garrison < 2){
        return false;
      }
      if(!bordering(atk,def)){
        return false;
      }
      return true;
    }
    private boolean bordering(int r1, int r2){
      for(int i = 0; i < map[r1].borders.length; ++i){
        if(map[r1].borders[i] == r2)
          return true;
      }
      return false;
    }
    private void battle(int atk, int def){
      int atkTroops = map[atk].garrison;
      int defTroops = map[def].garrison;
      
      int numAtkDice = getAtkDice(atkTroops);
      int numDefDice = getDefDice(defTroops);
      
      int[] atkRolls = rollDice(numAtkDice);
      int[] defRolls = rollDice(numDefDice);
      
      map[atk].garrison -= atkLoss(atkRolls, defRolls);
      map[def].garrison -= defLoss(atkRolls, defRolls); 
    }
    private int getAtkDice(int troops){
      if(troops == 2){
        return 1;
      }else if(troops == 3){
        return 2;
      }else{
        return 3;
      }
    }
    private int getDefDice(int troops){
      if(troops == 1){
        return 1;
      }else{
        return 2;
      }
    }
    private int[] rollDice(int numDice){
      int[] temp = new int[numDice];
      
      for(int i = 0; i < temp.length; ++i){
        temp[i] = (int) (Math.random() * 6);
      }
      temp = intSort(temp);
      return temp;
    }
    private int[] intSort(int[] in){
      boolean flag = true;
      int[] out = in;
      while(flag){
        flag = false;
        for(int i = 0; i < out.length - 1; ++i){
          if(out[i] < out[i + 1]){
            int temp = out[i];
            out[i] = out[i + 1];
            out[i + 1] = temp;
            flag = true;
          }
        }
      }
      return out;
    }
    private int atkLoss(int[] atk, int[] def){
      int loss = 0;
      for(int i = 0; i < atk.length && i < def.length; ++i){
        if(atk[i] <= def[i]){
          loss++;
        }
      }
      return loss;
    }
    private int defLoss(int[] atk, int[] def){
      int loss = 0;
      for(int i = 0; i < atk.length && i < def.length; ++i){
        if(def[i] < atk[i]){
          loss++;
        }
      }
      return loss;
    }
    
    private ArrayList<Card> makeDeck(){
      ArrayList temp = new ArrayList();
      for(int i = 0; i < 44; ++i){
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
        for(int i = 0; i < n; i++){
            temp[i] = new Player(i);
        }
        return temp;
    }
    private Region[] makeMap(){
        Region[] temp = new Region[42];
        File in;
        String fileName = "Memory";
        in = new File(fileName);
        Scanner sc;
      try {
        sc = new Scanner(in);
      } catch (FileNotFoundException ex) {
        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        sc = new Scanner(System.in);
      }
        for(int i = 0; i < 42; ++i){
            temp[i] = new Region(sc.next(), sc.next());
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
            if(temp >= l && temp <= h){
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
        
        if(p.hasCardSet()){//hasCardSet moves a set of cards to the end of the hand
          p.discardSet();
          reinforcements += this.numCardTroops;
          iterateCardTroops();
        }

        reinforcements += (temp / 3);

        return reinforcements;
    }
    private int distributeTroops(int targetRegion, int amount){
        map[targetRegion].garrison += amount;
        return amount;
    }
    public Region getRegion(int regionID){
      return map[regionID];
    }
    private void iterateCardTroops(){
      if(this.numCardTroops < 15){
        this.numCardTroops += 3;
      }else{
        this.numCardTroops += 5;
      }
    }
}
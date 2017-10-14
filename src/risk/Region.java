package risk;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author jkleifges9087
 */
public class Region {
    
    int[] borders;
    String name;
    Player owner;
    
    public Region(String n, String d){
        this.name = n;
        this.borders = mungeData(d);
    }
    private int[] mungeData(String dat){
        int[] temp = new int[dat.length() / 2];
        
        for(int i = 0; i < temp.length; ++i){
            temp[i] = Integer.parseInt(dat.substring(2 * i, 2 * i + 2));
        }
        
        return temp;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spellchecker;

import java.util.*;
import java.util.regex.*;
import java.io.*;
/**
 *
 * @author varun
 */
public class BigCount {
    private final HashMap<String, Integer> bigfile = new HashMap<String, Integer>();
	
    BigCount(String filename) throws Exception{
        BufferedReader in = new BufferedReader(new FileReader(filename));
        Pattern p = Pattern.compile("\\w+");
        String line;
        while((line=in.readLine())!=null){
            Matcher m = p.matcher(line.toLowerCase());
            while(m.find()){
                String s = m.group();
                if (bigfile.containsKey(s)) {
                    bigfile.put(s, bigfile.get(s) + 1);
                } else {
                    bigfile.put(s, 1);
                }
            }
        }
        /*File file = new File(filename);
        Scanner sc = new Scanner(new FileReader(file));
        String s;
        while(sc.hasNext())
        {
            s=sc.next();
            if(bigfile.containsKey(s)){bigfile.put(s, bigfile.get(s)+1);}
            else{bigfile.put(s, 1);}
        }*/
    }

    int giveCountFor(String s){
        if(bigfile.containsKey(s)){return bigfile.get(s);}
        else{return 0;}
    }

}

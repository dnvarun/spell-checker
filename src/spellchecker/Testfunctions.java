/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testfunctions;

import java.util.Vector;

/**
 *
 * @author code
 */
public class Testfunctions {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //System.out.print(qwerty_neighbours('a'));
        Vector <String> v = get_neighbour_words("siddu");
        for(int j=0;j<v.size();j++){
            System.out.print(v.elementAt(j)+" \n");
        }
    }
    
    static Vector<String> get_neighbour_words(String s){
        Vector<String> v = new Vector();
        for (int i=0;i<s.length();i++){
            String possible_replacements = qwerty_neighbour_chars(s.charAt(i));
            for(int j=0;j<possible_replacements.length();j++){
                String s1 = s.substring(0, i)+possible_replacements.charAt(j)+s.substring(i+1);
                v.add(s1);
            }
        }
        return v;
    }
    
    static String qwerty_neighbour_chars(char c){
        switch(c){
            case 'a': return "qwsz";
            case 'b': return "vghn";
            case 'c': return "xdfv";
            case 'd': return "sxcfer";
            case 'e': return "wsdr";
            case 'f': return "rtgvcd";
            case 'g': return "tyhbvf";
            case 'h': return "yujnbg";
            case 'i': return "ujko";
            case 'j': return "uikmnh";
            case 'k': return "iolmj";
            case 'l': return "opk";
            case 'm': return "njk";
            case 'n': return "bhjm";
            case 'o': return "iklp";
            case 'p': return "ol";
            case 'q': return "wa";
            case 'r': return "edft";
            case 's': return "wedxza";
            case 't': return "rfgy";
            case 'u': return "yhji";
            case 'v': return "cfgb";
            case 'w': return "qase";
            case 'x': return "zsdc";
            case 'y': return "tghu";
            case 'z': return "asx";
            default: return "";
        }
    }
    
}

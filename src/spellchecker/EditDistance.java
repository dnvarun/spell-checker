/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spellchecker;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import java.io.*;
import java.util.*;
import java.util.regex.*;
/**
 *
 * @author varun
 */
public class EditDistance {
    
    BigCount bc;
    WordNetDatabase database;
    
    EditDistance() throws Exception{
        bc = new BigCount("/home/varun/Academics/NLP/SpellCheck/big.txt");
        System.setProperty("wordnet.database.dir", "/home/varun/hacku/WordNet-3.0/dict/");
        database = WordNetDatabase.getFileInstance();
    }
        
    ArrayList<String> possibleSpellings(String word){
        ArrayList<String> result = new ArrayList<String>();
        /*Deletion*/
        for (int i = 0; i < word.length(); ++i) {
            result.add(word.substring(0, i) + word.substring(i + 1));
        }
        /*Swapping*/
        for (int i = 0; i < word.length() - 1; ++i) {
            result.add(word.substring(0, i) + word.substring(i + 1, i + 2) + word.substring(i, i + 1) + word.substring(i + 2));
        }
        /*Substitution*/
        for (int i = 0; i < word.length(); ++i) {
            for (char c = 'a'; c <= 'z'; ++c) {
                result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i + 1));
            }            
        }
        /*Insertion*/
        for (int i = 0; i <= word.length(); ++i) {
            for (char c = 'a'; c <= 'z'; ++c) {
                result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
            }
        }
        return result;
    }
    
    ArrayList<String> qwerty_neighbour_words(String s){
        ArrayList<String> v = new ArrayList();
        for (int i=0;i<s.length();i++){
            String possible_replacements = qwerty_neighbour_chars(s.charAt(i));
            for(int j=0;j<possible_replacements.length();j++){
                String s1 = s.substring(0, i)+possible_replacements.charAt(j)+s.substring(i+1);
                v.add(s1);
            }
        }
        return v;
    }
    
    String qwerty_neighbour_chars(char c){
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
    
    String correctSpelling(String word) throws Exception{
        word = word.toLowerCase().replaceAll("[-\';,0-9]", "");
        System.out.print("Replaced word: "+word+" ");
        int count = bc.giveCountFor(word);
        if(count>0)return word;
        else if(count==0){
            Synset[] synsets = database.getSynsets(word);
            if (synsets.length > 0)return word;
        }
        
        /*HashMap<Integer, String> qwerty_candidates = new HashMap<Integer, String>();
        ArrayList<String> qwerty_possible_clues = qwerty_neighbour_words(word);
        for(String possible : qwerty_possible_clues){
            //if(database.getSynsets(possible).length > 0){
                count = bc.giveCountFor(possible);
                if(count>0)qwerty_candidates.put(count, possible);
                else if(database.getSynsets(possible).length > 0)qwerty_candidates.put(1, possible);
            //}
        }
        if(qwerty_candidates.size()>0)return qwerty_candidates.get(Collections.max(qwerty_candidates.keySet()));*/
        
        //else return "Cannot determine correct spelling";
        
        
        //System.out.print("Given: "+word+" ");
        HashMap<Integer, String> candidates = new HashMap<Integer, String>();
        
        ArrayList<String> AllPossible = possibleSpellings(word);
        for(String possible : AllPossible){
            //if(database.getSynsets(possible).length > 0){
                count = bc.giveCountFor(possible);
                if(count>0)candidates.put(count, possible);
                //else if(database.getSynsets(possible).length > 0)candidates.put(1, possible);
            //}
        }
        if(candidates.size()>0)return candidates.get(Collections.max(candidates.keySet()));
        
        for(String possible : AllPossible){
            ArrayList<String> secondPossible = possibleSpellings(possible);
            for(String possible2 : secondPossible){
                //if (database.getSynsets(possible2).length > 0) {
                    count = bc.giveCountFor(possible2);
                    if(count>0)candidates.put(count, possible2);             
                    //else if(database.getSynsets(possible2).length > 0)candidates.put(1, possible2);
                //}
            }
        }
        if(candidates.size()>0)return candidates.get(Collections.max(candidates.keySet()));
        else return "Cannot determine correct spelling";
     
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spellchecker;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author varun
 */

public class Kerninghan {
    
    int[][] pairchar_counts;
    int[] char_counts;
    int[][] del,add,sub,rev;
    
    HashMap<String, Integer> bigfile;
    long total_words;
    //HashMap<Double,String> candidate_heap;
    WordNetDatabase database;
    
    Kerninghan() throws Exception{
        pairchar_counts = new int[27][26];
        char_counts = new int[27];
        del = new int[27][26];
        add = new int[27][26];
        sub = new int[26][26];
        rev = new int[26][26];
        bigfile = new HashMap<String, Integer>();
        total_words=0;
        
        for(int i=0;i<27;i++){
            for(int j=0;j<26;j++){
                pairchar_counts[i][j]=0;
                del[i][j]=0;
                add[i][j]=0;
                if(i<26){
                    sub[i][j]=0;
                    rev[i][j]=0;
                }
            }
            char_counts[i]=0;
        }
        
        BufferedReader br = new BufferedReader(new FileReader("/home/varun/Academics/NLP/SpellCheck/big.txt"));
        Pattern p = Pattern.compile("\\w+");
        String line;
        while((line=br.readLine())!=null){
            Matcher m = p.matcher(line.toLowerCase());
            while(m.find()){
                String s = m.group();
                if (bigfile.containsKey(s)) {
                    bigfile.put(s, bigfile.get(s) + 1);
                } else {
                    bigfile.put(s, 1);
                }
                total_words++;
                char_counts[26]++;                
                if(s.charAt(0)>96 && s.charAt(0)<123)pairchar_counts[26][s.charAt(0)-97]++;
                for(int i=0;i<s.length()-1;i++){
                    if(s.charAt(i)>96 && s.charAt(i)<123){
                        char_counts[s.charAt(i)-97]++;
                        if(s.charAt(i+1)>96 && s.charAt(i+1)<123)pairchar_counts[s.charAt(i)-97][s.charAt(i+1)-97]++;
                    }                    
                }
                if(s.charAt(s.length()-1)>96 && s.charAt(s.length()-1)<123)char_counts[s.charAt(s.length()-1)-97]++;
            }
        }
        
        br = new BufferedReader(new FileReader("/home/varun/Academics/NLP/SpellCheck/ConfusionTables/deletiontable.csv"));
        line = br.readLine();
        int row = 0;
        while((line = br.readLine())!=null){
            String[] count_data = line.split(",");
            for(int i=1;i<count_data.length;i++){
                del[row][i-1] = Integer.parseInt(count_data[i]);
            }
            row++;
        }
        
        br = new BufferedReader(new FileReader("/home/varun/Academics/NLP/SpellCheck/ConfusionTables/insertionstable.csv"));
        line = br.readLine();
        row = 0;
        while((line = br.readLine())!=null){
            String[] count_data = line.split(",");
            for(int i=1;i<count_data.length;i++){
                add[row][i-1] = Integer.parseInt(count_data[i]);
            }
            row++;
        }
        
        br = new BufferedReader(new FileReader("/home/varun/Academics/NLP/SpellCheck/ConfusionTables/substitutionstable.csv"));
        line = br.readLine();
        row = 0;
        while((line = br.readLine())!=null){
            String[] count_data = line.split(",");
            for(int i=1;i<count_data.length;i++){
                sub[row][i-1] = Integer.parseInt(count_data[i]);
            }
            row++;
        }
        
        br = new BufferedReader(new FileReader("/home/varun/Academics/NLP/SpellCheck/ConfusionTables/transpositionstable.csv"));
        line = br.readLine();
        row = 0;
        while((line = br.readLine())!=null){
            String[] count_data = line.split(",");
            for(int i=1;i<count_data.length;i++){
                rev[row][i-1] = Integer.parseInt(count_data[i]);
            }
            row++;
        }
        System.out.println("Total words: "+total_words);
        System.setProperty("wordnet.database.dir", "/home/varun/hacku/WordNet-3.0/dict/");
        database = WordNetDatabase.getFileInstance();
    }
    
    String correctSpelling(String word){
        int count = giveCountFor(word);
        if(count>0)return word;
        else if(count==0){
            Synset[] synsets = database.getSynsets(word);
            if (synsets.length > 0)return word;
        }
        word = word.toLowerCase();
        word = word.replaceAll("[-\';0-9]", "");
        return possibleSpellings(word);
        
        //System.out.println("Max prob: "+Collections.max(candidate_heap.keySet()));
        //if(candidate_heap.size()>0)return candidate_heap.get(Collections.max(candidate_heap.keySet()));
        //else return "Cannot determine correct spelling";
    }
    
    int giveCountFor(String s){
        if(bigfile.containsKey(s)){return bigfile.get(s);}
        else{return 0;}
    }
    
    String possibleSpellings(String word){
        //candidate_heap = new HashMap<Double,String>();
        String mostprob_spelling="Cannot determine correct spelling";
        Double max_prob=0.0;
        /*Insertion*/
        for (int i = 0; i < word.length(); ++i) {
            String mod_word = word.substring(0, i) + word.substring(i + 1);
            double probc = new Double(giveCountFor(mod_word))/total_words;
            double probtc=0.0;
            try{probtc = new Double(add[(i>0)?mod_word.charAt(i-1)-97:26][word.charAt(i)-97])/char_counts[(i>0)?mod_word.charAt(i-1)-97:26];}
            catch(Exception e){System.err.println("Exception word: "+word+" "+mod_word);}
            double prob = probc*probtc;
            //candidate_heap.put(prob, mod_word);
            if(prob>max_prob){
                mostprob_spelling = mod_word;
                max_prob = prob;
            }
            if(mod_word.equals("correct"))System.out.println("Probabilities: "+probc+" "+probtc);
        }
        /*Swapping*/
        for (int i = 0; i < word.length() - 1; ++i) {
            String mod_word = word.substring(0, i) + word.substring(i + 1, i + 2) + word.substring(i, i + 1) + word.substring(i + 2);
            double probc = new Double(giveCountFor(mod_word))/total_words;
            double probtc = new Double(rev[mod_word.charAt(i)-97][mod_word.charAt(i+1)-97])/pairchar_counts[mod_word.charAt(i)-97][mod_word.charAt(i+1)-97];
            double prob = probc*probtc;
            //candidate_heap.put(prob, mod_word);
            if(prob>max_prob){
                mostprob_spelling = mod_word;
                max_prob = prob;
            }
            if(mod_word.equals("correct"))System.out.println("Probabilities: "+probc+" "+probtc);
        }
        /*Substitution*/
        for (int i = 0; i < word.length(); ++i) {
            for (char c = 'a'; c <= 'z'; ++c) {
                String mod_word = word.substring(0, i) + String.valueOf(c) + word.substring(i + 1);
                double probc = new Double(giveCountFor(mod_word)) / total_words;
                double probtc = new Double(sub[word.charAt(i)-97][mod_word.charAt(i)-97]) / char_counts[mod_word.charAt(i)-97];
                double prob = probc * probtc;
                //candidate_heap.put(prob, mod_word);
                if (prob > max_prob) {
                    mostprob_spelling = mod_word;
                    max_prob = prob;
                }
                if(mod_word.equals("correct"))System.out.println("Probabilities: "+probc+" "+probtc);
            }            
        }
        /*Deletion*/
        for (int i = 0; i <= word.length(); ++i) {
            for (char c = 'a'; c <= 'z'; ++c) {
                String mod_word = word.substring(0, i) + String.valueOf(c) + word.substring(i);
                //if(i==word.length())System.out.println(mod_word);
                double probc = new Double(giveCountFor(mod_word)) / total_words;                
                double probtc = new Double(del[(i>0)?mod_word.charAt(i-1)-97:26][mod_word.charAt(i)-97]) / pairchar_counts[(i>0)?mod_word.charAt(i-1)-97:26][mod_word.charAt(i)-97];
                double prob = probc * probtc;
                //candidate_heap.put(prob, mod_word);
                if (prob > max_prob) {
                    mostprob_spelling = mod_word;
                    max_prob = prob;
                }
                if(mod_word.equals("correct"))System.out.println("Probabilities: "+probc+" "+probtc);
                /*if(mod_word.equals("kkorrect")){
                    System.out.println("Probabilities: "+probc+" "+probtc);                    
                    System.out.println("Del count: "+del[(i>0)?mod_word.charAt(i-1)-97:26][mod_word.charAt(i)-97]);
                    System.out.println("Char count: "+pairchar_counts[(i>0)?mod_word.charAt(i-1)-97:26][mod_word.charAt(i)-97]);
                }*/
            }
        }
        System.out.println("Most probable spelling: "+mostprob_spelling+" "+max_prob);
        return mostprob_spelling;
    }
    
    int getSubCost(char c1,char c2){
        if(c1==c2)return 0;
        else return 1;
    }
    
    /*ArrayList<EditType> getEdits(String correct, String wrong){
        ArrayList<EditType> result = new ArrayList<EditType>();
        int ins_cost = 1,del_cost = 1;
                
        int m = correct.length();
        int n = wrong.length();
        DistanceNode[][] distance = new DistanceNode[m+1][n+1];
        
        distance[0][0] = new DistanceNode(0,-1,-1);
        for(int i=1;i<=m;i++){
            distance[i][0] = new DistanceNode(distance[i-1][0].dist + del_cost,i-1,0);
        }
        for(int i=1;i<=n;i++){
            distance[0][i] = new DistanceNode(distance[0][i-1].dist + ins_cost,0,i-1);
        }
        
        for(int i=1;i<=m;i++){
            for(int j=1;j<=n;j++){
                if(correct.charAt(i) == wrong.charAt(j))distance[i][j] = new DistanceNode(distance[i-1][j-1].dist,i-1,j-1);                
                else{
                    int min_distance = distance[i][j-1].dist+ins_cost;
                    int pr=i,pc=j-1;
                    int temp_cost = distance[i-1][j-1].dist + getSubCost(correct.charAt(i-1),wrong.charAt(j-1));
                    if(temp_cost<min_distance){min_distance = temp_cost;pr=i-1;pc=j-1;}
                    temp_cost = distance[i-1][j].dist + del_cost;
                    if(temp_cost<min_distance){min_distance = temp_cost;pr=i-1;pc=j;}
                    distance[i][j] = new DistanceNode(min_distance,pr,pc);
                }
            }
        }
        
        DistanceNode cur_node = distance[m][n];
        int cur_row = m,cur_col = n;
        while(cur_node.parent_column != -1 && cur_node.parent_row !=-1){
            if(cur_node.parent_row==cur_row-1 && cur_node.parent_column==cur_col-1){
                result.add(new EditType(wrong.charAt(cur_col),correct.charAt(cur_row),3));
            }
            else if(cur_node.parent_row==cur_row-1){
                result.add(new EditType(correct.charAt(cur_row-1),correct.charAt(cur_row),2));
            }
            else if(cur_node.parent_column==cur_col-1){
                result.add(new EditType(correct.charAt(cur_row),wrong.charAt(cur_col),1));
            }
        }
        
        return result;
    }*/
   
}


/*class EditType{
    char x,y;
    int type;//1 for ins, 2 for del, 3 for sub, 4 for trans
    EditType(char c1,char c2, int t){
        x=c1;y=c2;type=t;
    }
}

class DistanceNode{
    int dist;
    int parent_row,parent_column;
    DistanceNode(int d,int r,int c){
        dist = d;
        parent_row = r;
        parent_column =c;
    }
}*/
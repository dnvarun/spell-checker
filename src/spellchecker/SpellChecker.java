/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spellchecker;

import java.util.*;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import java.io.*;

/**
 *
 * @author varun
 */
public class SpellChecker {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader("/home/varun/Academics/NLP/SpellCheck/commonerrors.txt"));
        String line;
        //String[] args2 = new String[20];args2[0]="1"; args2[1]="sgine"; args=args2;
        if(args[0].equals("1")){
            EditDistance ed = new EditDistance();
            int total_count=0,correct_count=0;
            long start_time = new Date().getTime();
            while((line=br.readLine())!=null){
                //System.out.println(line);
                String[] spellings = line.split("\t");
                String correct = ed.correctSpelling(spellings[0]);
                System.out.println("Wrong: "+spellings[0]+", Obtained: "+correct+", Actual: "+spellings[1]);
                if(correct.equals(spellings[1]))correct_count++;
                total_count++;
            }
            long time_taken = new Date().getTime() - start_time;
            System.out.println("Total and correct: "+total_count+" "+correct_count);
            System.out.println("Time taken in milliseconds: "+time_taken);
            //System.out.println(ed.correctSpelling(args[1]));
        //if(args.length > 0) System.out.println((new EditDistance()).correctSpelling(args[0]));
        }
        /*else if(args[0].equals("2")){
            Kerninghan k  = new Kerninghan();
            int total_count=0,correct_count=0;
            long start_time = new Date().getTime();
            while((line=br.readLine())!=null){
                String[] spellings = line.split(" ");
                String correct = k.correctSpelling(spellings[0]);                
                if(correct.equals(spellings[1]))correct_count++;
                total_count++;
            }
            long time_taken = new Date().getTime() - start_time;
            System.out.println("Total and correct: "+total_count+" "+correct_count);            
            System.out.println("Time taken in milliseconds: "+time_taken);
            //System.out.println(k.correctSpelling("crrect"));
        }*/
        
        else if(args[0].equals("3")){
            ArrayList<String> wrong_spellings = new ArrayList<String>();
            ArrayList<String> correct_spellings = new ArrayList<String>();
            int total_count=0;
            while((line=br.readLine())!=null){
                String[] spellings = line.split("\t");
                wrong_spellings.add(spellings[0]);
                correct_spellings.add(spellings[1]);                
                total_count++;
            }
            System.out.println("Total count: "+total_count);
            int test_start=Integer.parseInt(args[1])*total_count/5;
            System.out.println("Test start: "+test_start);
            ArrayList<String> test_wrong = new ArrayList<String>();
            ArrayList<String> test_correct = new ArrayList<String>();
            for(int i=test_start;i<test_start+total_count/5;i++){
                test_wrong.add(wrong_spellings.get(i));
                test_correct.add(correct_spellings.get(i));
            }
            System.out.println("Test sizes: "+test_wrong.size()+" "+test_correct.size());
            System.out.println("Starting of testing: "+test_wrong.get(0)+" "+test_correct.get(0));
            for(int i=test_start;i<test_start+total_count/5;i++){
                wrong_spellings.remove(test_start);
                correct_spellings.remove(test_start);
            }
            System.out.println("Training sizes: "+wrong_spellings.size()+" "+correct_spellings.size());
            System.out.println("Starting of training: "+wrong_spellings.get(0)+" "+correct_spellings.get(0));
            int correct_count=0;
            NewKernighan k  = new NewKernighan(wrong_spellings,correct_spellings);
            /*long start_time = new Date().getTime();
            for(int i=0;i<test_wrong.size();i++){
                String correct = k.correctSpelling(test_wrong.get(i));
                System.out.println("Wrong: "+test_wrong.get(i)+", Obtained: "+correct+", Actual: "+test_correct.get(i));
                if(correct.equals(test_correct.get(i)))correct_count++;
                total_count++;
            }
            //System.out.println(test_correct.get(0)+" "+test_wrong.get(0));
            k.getEdits(test_correct.get(0),test_correct.get(0));
            long time_taken = new Date().getTime() - start_time;
            System.out.println("Total and correct: "+test_wrong.size()+" "+correct_count);            
            System.out.println("Time taken in milliseconds: "+time_taken);*/
            //System.out.println(k.correctSpelling("crrect"));
        }
    }
}

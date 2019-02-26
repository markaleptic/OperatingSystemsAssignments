import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Runtime;


public class ParallelPi{
    private int numDigits = 1000;
    private Queue <Integer> TaskQueue;
    private HashMap <Integer,Integer> ResultTable;
    private int processors = 1;



    public ParallelPi(int digitPrecision){
        if(digitPrecision != 1000){
            this.numDigits = digitPrecision;
        }
        
        // Create threads equal to the number of available processors
        processors = Runtime.getRuntime().availableProcessors();
        
        // Create randomized task queue by generating a range from 1 to numDigits, 
        // construct TaskQueue with randomized range. Used List and ArrayList to
        // work easily with Collections.shuffle().
        ArrayList<Integer> rng = new ArrayList<Integer>(this.numDigits);
        for(int i = 1; i < this.numDigits; i++){
            rng.add(i);
        }
        Collections.shuffle(rng);
        this.TaskQueue = new LinkedList(rng);
        this.ResultTable = new HashMap<Integer,Integer>(this.numDigits);
    }


    public void printQueue(){
        System.out.println(this.TaskQueue);
    }
}
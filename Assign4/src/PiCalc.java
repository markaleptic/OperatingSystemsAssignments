import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.java.Runtime;


public class PiCalc{
    private int numDigits = 1000;
    private Queue <Integer> TaskQueue = new LinkedList <Integer>();
    private HashMap <Integer,Integer> ResultTable = new HashMap<Integer,Integer>(numDigits);

    


    public void PiCalc(int digitPrecision){
        if(digitPrecision != 1000){
            this.numDigits = digitPrecision;
        }
            
    }
}
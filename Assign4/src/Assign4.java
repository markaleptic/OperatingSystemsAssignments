import java.util.HashMap;

import com.sun.net.httpserver.Authenticator.Result;

public class Assign4{
    public static void main(String[] args){

        // Digits of π to compute
        digitsOfPi = 1000;
        
        // Create FIFO queue that holds shuffled integers that represents the digit to compute in pi.
        TaskQueue digitsToCompute = new TaskQueue(digitsOfPi);
        // Create hash table that holds the value of pi where the key is its digit position. 
        ResultTable computedDigits = new ResultTable(digitsOfPi);

        // Dynamically create threads based upon available processors
        int procs = Runtime.getRuntime().availableProcessors();
        
        Thread[] threads = new Thread[procs];

        for(int i = 0; i < procs; i++){
            threads[thread] = new Thread(new ParallelPi(String.format("Thread %d", thread + 1), digitsToCompute, computedDigits));
            threads[thread].start();
        }

    } 

}
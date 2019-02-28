import java.util.HashMap;

public class Assign4{
    public static void main(String[] args){

        // Digits of π to compute
        int digitsOfPi = 1000;
        
        // Create FIFO queue that holds shuffled integers that represents the digit to compute in pi.
        TaskQueue<Integer> digitsToCompute = new TaskQueue<Integer>(digitsOfPi);
        // Create hash table that holds the value of pi where the key is its digit position. 
        ResultTable<Integer> computedDigits = new ResultTable<Integer>(digitsOfPi);

        // Dynamically create threads based upon available processors
        int procs = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[procs];

        // Track time to display to user
        long startTime = System.currentTimeMillis();
        
        for(int thread = 0; thread < procs; thread++){
            threads[thread] = new Thread(new ParallelPi(digitsToCompute, computedDigits));
            threads[thread].start();
        }
        long endTime = System.currentTimeMillis();

        System.out.printf("Computing %d digits of Pi took %d ms", digitsOfPi, endTime - startTime);

    } 

}
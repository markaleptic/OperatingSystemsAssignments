import java.util.HashMap;


/**
 * Synchronized HashMap that holds each digit of pi (value) based on its digit position (key).
 * Reports progress in terminal with '.' for every 10th digit computed.
 */
public class ResultTable<E>{
    private HashMap<Integer,Integer> digits;
    private int taskCount = 0;
    private int countsPerLine = 0;

    public ResultTable(Integer size){
        this.digits = new HashMap<Integer,Integer>(size);
    }

    public synchronized void insert(Integer k, Integer val){
        Integer result = this.digits.put(k, val);

        if(result != null){
            System.out.printf("Error in inserting key: %d, val: %d", k, val);
            System.out.printf("Redundant result for key");
            return;
        }

        taskCount++;

        if(taskCount % 10 == 0) printDot();
        if(taskCount == 1000) displayValues();
    }

    /**
     * Report '3.' remaining digits of pi by cycling through hash map.
     */
    public synchronized void displayValues(){
        System.out.printf("\n3.");
        for(int key = 1; key <= digits.size(); key++){
            System.out.printf(digits.get(key).toString());
        }
        System.out.println();
    }
    
    /**
     * Function displays a dot for each tenth computation with
     * twenty dots per line.
     */
    public void printDot(){
        System.out.printf(".");
        countsPerLine++;
        if(countsPerLine == 20) {
            System.out.println();
            countsPerLine = 0;
        }
        System.out.flush();
    }
}
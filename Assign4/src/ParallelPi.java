import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Runtime;


public class ParallelPi implements Runnable{
    private TaskQueue<Integer> digitTasksQueue;
    private ResultTable<Integer> resultDigitsTable;
    private Bpp pi;
    private Boolean stopFlag = false;


    public ParallelPi(TaskQueue<Integer> sharedTaskQueue, ResultTable<Integer> sharedResultTable){
        this.digitTasksQueue = sharedTaskQueue;
        this.resultDigitsTable = sharedResultTable;
        this.pi = new Bpp();
    }

    /**
     * Runnable interface implementation
     */
    public void run(){
        Optional<Integer> digit;
        Integer value;

        while(!stopFlag){
            // Get next digit to compute
            digit = digitTasksQueue.pop();

            // Break loop when the task queue is empty
            if(digit.isEmpty()) {
                stopFlag = true;
                continue;
            }
            // Get the decimal value plus 8 digits
            value = pi.getDecimal(digit.get().longValue());
            
            // Strip trailing 8 digits to keep actual digit value
            value = Integer.parseInt(Integer.toString(value).substring(0, 1));

            // Put digit, value into Hash Table
            resultDigitsTable.insert(digit.get(), value);
        }
    }

    public Boolean getState(){
        return stopFlag;
    }

    
}
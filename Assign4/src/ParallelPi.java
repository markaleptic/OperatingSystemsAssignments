import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Runtime;


public class ParallelPi implements Runnable{
    private String threadName;
    private TaskQueue digitTasks;
    private ResultTable resultDigits;
    private Bpp pi;


    public ParallelPi(String name, TaskQueue sharedTaskQueue, ResultTable sharedResultTable){
        this.threadName = name;
        this.piTasks = sharedTaskQueue;
        this.ResultTable = sharedResultTable;
        this.pi = new Bpp();
    }

    public void run(){

    }


    
}
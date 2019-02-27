import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Runtime;


public class ParallelPi implements Runnable{
    private String threadName;
    private Queue <Integer> TaskQueue;
    private HashMap <Integer,Integer> ResultTable;


    public ParallelPi(String name, Queue sharedTaskQueue, HashMap sharedResultTable){
        this.threadName = name;
        this.TaskQueue = sharedTaskQueue;
        this.ResultTable = sharedResultTable;
    }


    
}
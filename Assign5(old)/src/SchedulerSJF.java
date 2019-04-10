import java.util.LinkedList;
import java.util.Queue;

public class SchedulerSJF extends SchedulerBase implements Scheduler{
    private Platform m_platform;
    private Queue<Process> readyQueue;

    
    public SchedulerSJF(Platform platform){
        
    }

    public void notifyNewProcess(Process p){

    }

    public Process update(Process cpu){
        
    }
}
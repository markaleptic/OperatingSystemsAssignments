import java.util.PriorityQueue;
import java.util.Comparator;


/**
 * Shortest Job First Algorithm to handle incoming process by their remaining burst time.
 */
public class SchedulerSJF extends SchedulerBase implements Scheduler{
    private Platform m_platform;                // Platform object that allows for logging to simulation
    private PriorityQueue<Process> readyQueue;  // FIFO Queue for handling incoming processes, sorted by processes' remaining burst time
    private Process activeProcess;              // The process the cpu is actively computing 

    
    /**
     * Base class variable - increase context switch when process is removed or scheduled
     */
    private void contextIncrement(){ contextSwitches += 1; }


    /**
     * Constructor that receives platform object for logging, creates processing FIFO queue
     * @param platform 
     */
    public SchedulerSJF(Platform platform){
        this.m_platform = platform;
        readyQueue = new PriorityQueue<Process>(1, new CompareSJF());
        
    }


    /**
     * Add new process to queue, and get head of queue. Queue is sorted by remaining burst time
     */
    public void notifyNewProcess(Process p){
        readyQueue.add(p);
        activeProcess = readyQueue.peek();
    }


    /**
     * Updates the ready queue if the active process is done computing.
     * 
     * @param cpu
     * @return the process the cpu is actively processing
     */
    public Process update(Process cpu){
        // Check for null - the first cpu passed is null
        if(cpu == null){
            m_platform.log("Scheduled: " + activeProcess.getName());
            return activeProcess;
        }

        // Error Checking - verify active process is the same as the cpu process
        if(cpu.getName() != activeProcess.getName()){
            m_platform.log("State error! CPU = " + cpu.getName() + "\tActive = " + activeProcess.getName());
        }

        // Check if the active running process completed its burst and execution
        if(cpu.isBurstComplete()){
            m_platform.log("Process " + activeProcess.getName() + " burst complete");
            contextIncrement();

            // Check if process is completed executing
            if(cpu.isExecutionComplete()){
                m_platform.log("Process " + activeProcess.getName() + " execution complete");
                // Remove burst and execution completed process
                readyQueue.remove(cpu);
            } else {
                // Move burst completed process to end to continue processing
                readyQueue.remove(cpu);
                readyQueue.add(cpu);
            }

            // Get the next process to execute and count the context switch
            activeProcess = readyQueue.peek();
            contextIncrement();

            if(activeProcess != null){
                m_platform.log("Scheduled " + activeProcess.getName());
            }
        }

        return activeProcess;
    }
}

/**
 * Sort processes in ascending order based on process burst time.
 */
class CompareSJF implements Comparator<Process>{
    public int compare(Process p1, Process p2){
        if(p1.getBurstTime() < p2.getBurstTime()){
            return -1;
        }
        else if(p1.getBurstTime() > p2.getBurstTime()){
            return 1;
        }
        return 0;
    }
}
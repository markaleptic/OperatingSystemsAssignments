import java.util.PriorityQueue;
import java.util.Comparator;


/**
 * Pre-emptive Shortest Job First Algorithm to handle incoming processes via FIFO queue
 */
public class SchedulerSRTF extends SchedulerBase implements Scheduler{
    private Platform m_platform;                // Platform object that allows for logging to simulation
    private PriorityQueue<Process> readyQueue;  // FIFO Queue for handling incoming processes
    private Process activeProcess;              // The process the cpu is actively computing 
    private Process lastActiveProcess;          // The last process the cpu was actively computing


    /**
     * Base class variable - increase context switch when process is removed or scheduled
     */
    private void contextIncrement(){ contextSwitches += 1; }


    /**
     * Constructor that receives platform object for logging, creates processing FIFO queue
     * @param platform 
     */
    public SchedulerSRTF(Platform platform){
        this.m_platform = platform;
        readyQueue = new PriorityQueue<Process>(1, new CompareSJF());
    }


    /**
     * Add new process to queue. See if the added process is different from the last active process.
     * If the added process is different, preempt the last active process.
     */
    public void notifyNewProcess(Process p){
        lastActiveProcess = readyQueue.peek();
        readyQueue.add(p);
        activeProcess = readyQueue.peek();

        if(lastActiveProcess == null) return;

        if(activeProcess.getName() != lastActiveProcess.getName()){
            m_platform.log("Preemptively removed: " + lastActiveProcess.getName());
            contextIncrement();
            contextIncrement();
            m_platform.log("Scheduled: " + activeProcess.getName());
        }
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
class CompareSRTF implements Comparator<Process>{
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

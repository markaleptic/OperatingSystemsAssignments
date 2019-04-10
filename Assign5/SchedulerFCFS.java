import java.util.LinkedList; 
import java.util.Queue;


/**
 * First Come, First Served Scheduling Algorithm to handle incoming processes via FIFO queue
 */
public class SchedulerFCFS extends SchedulerBase implements Scheduler{

    private Platform m_platform;        // Platform object that allows for logging to simulation
    private Queue<Process> readyQueue;  // FIFO Queue for handling incoming processes
    private Process activeProcess;      // The process the cpu is actively computing 

    /**
     * Base class variable - increase context switch when process is removed or scheduled
     */
    private void contextIncrement(){ contextSwitches += 1; }

    /**
     * Constructor that receives platform object for logging, creates processing FIFO queue
     * @param platform 
     */
    public SchedulerFCFS(Platform platform){
        this.m_platform = platform;
        readyQueue = new LinkedList<Process>();
    }


    /**
     * Add new process to end of ready queue for processing
     * @param p - a new process to add to the queue
     */
    public void notifyNewProcess(Process p){
        readyQueue.add(p);
        if(readyQueue.size() == 1){
             activeProcess = p;
        }
    }


    /**
     * Updates the FIFO queue if the active process is done computing.
     * 
     * @param cpu
     * @return the process the cpu is actively processing
     */
    public Process update(Process cpu){
        // Check for null - the first cpu passed is null
        if(cpu == null){
            m_platform.log("Scheduled " + activeProcess.getName());
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
                readyQueue.remove();
            } else {
                // Move burst completed process to end to continue processing
                readyQueue.add(readyQueue.remove());
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
import java.util.LinkedList; 
import java.util.Queue;


/**
 * Round Robin Scheduling Robin that processes until they're complete or until the time quantum is hit
 */
public class SchedulerRR extends SchedulerBase implements Scheduler{

    private Platform m_platform;        // Platform object that allows for logging to simulation
    private int m_quantum;              // Max time allowed for each process to compute
    private int quantCount;             // Time spent processing
    private Queue<Process> readyQueue;  // FIFO Queue for handling incoming processes, sorted by natural order
    private Process activeProcess;      // The process the cpu is actively computing 


    /**
     * Base class variable - increase context switch when process is removed or scheduled
     */
    private void contextIncrement(){ contextSwitches += 1; }


    /**
     * Constructor that receives platform object for logging, creates processing FIFO queue
     * @param platform 
     */
    public SchedulerRR(Platform platform, int quantum){
        this.m_platform = platform;
        this.m_quantum = quantum;
        
        readyQueue = new LinkedList<Process>();
        quantCount = 0;
    }


    /**
     * Add new process to queue
     * @param p
     */
    public void notifyNewProcess(Process p){
        readyQueue.add(p);
        if(readyQueue.size() == 1){
             activeProcess = p;
        }
    }


    /**
     * Updates the ready queue if the active process is done computing or the max time
     * quantum is hit.
     * 
     * @param cpu
     * @return the process the cpu is actively processing
     */
    public Process update(Process cpu){
        // Check for null - the first cpu passed is null
        if(cpu == null){
            m_platform.log("Scheduled " + activeProcess.getName());
            contextIncrement();
            return activeProcess;
        }
        
        // Error Checking
        if(cpu.getName() != activeProcess.getName()){
            m_platform.log("State error! CPU = " + cpu.getName() + "\tActive = " + activeProcess.getName());
        }

        // Check if process is completed executing
        if(cpu.isExecutionComplete()){
            m_platform.log("Process " + activeProcess.getName() + " execution complete");
            // Remove burst and execution completed process
            readyQueue.remove();
            activeProcess = readyQueue.peek();
            contextIncrement();
            
            if(activeProcess != null){
                contextIncrement();
                m_platform.log("Scheduled " + activeProcess.getName());
                quantCount = 0;
                return activeProcess;
            } else {
                return activeProcess;
            }
        }

        quantCount += 1;

        // Time quantum is hit - move the active process to the end and reset the ongoing quantum count
        if(quantCount == m_quantum){
            quantCount = 0;
            m_platform.log("Time quantum complete for process " + activeProcess.getName());
            readyQueue.add(readyQueue.remove());
            contextIncrement();
            activeProcess = readyQueue.peek();
            contextIncrement();
            m_platform.log("Scheduled " + activeProcess.getName());
        }
        
        return activeProcess;
    }
}
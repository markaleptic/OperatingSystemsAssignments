import java.util.LinkedList; 
import java.util.Queue;

public class SchedulerFCFS extends SchedulerBase implements Scheduler{

    private Platform m_platform;
    private Queue<Process> readyQueue;
    private Process activeProcess;

    private void contextIncrement(){ contextSwitches += 1; }

    public SchedulerFCFS(Platform platform){
        this.m_platform = platform;
        readyQueue = new LinkedList<Process>();
    }

    public void notifyNewProcess(Process p){
        readyQueue.add(p);
        if(readyQueue.size() == 1){
             activeProcess = p;
        }
    }

    public Process update(Process cpu){
        if(cpu == null){
            m_platform.log("Scheduled " + activeProcess.getName());
            return activeProcess;
        }
        // Error Checking
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
            
            activeProcess = readyQueue.peek();
            contextIncrement();

            if(activeProcess != null){
                m_platform.log("Scheduled " + activeProcess.getName());
            }
        }

        return activeProcess;
    }
}
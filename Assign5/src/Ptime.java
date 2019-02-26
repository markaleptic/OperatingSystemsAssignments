/**
 * Helper class of CommandLineInterpreter to track child-process time.
 */
public class Ptime{
    private float m_ptime;
    private long m_processStartTime;
    private long m_additionalTime;
    public static final float millisToSecConvert = 1000;

    public Ptime(){
        m_ptime = 0;
        m_additionalTime = 0;
    }

    public float getPtime(){ return m_ptime; }

    private void resetTime(){
        m_processStartTime = 0;
        m_additionalTime = 0;
    }
    public void start(boolean isTimed){
        if(isTimed){
            m_processStartTime = System.currentTimeMillis();
        }
    }

    public void end(){
        if(m_processStartTime == 0){ return; }
        m_additionalTime = System.currentTimeMillis() - m_processStartTime;
        m_ptime += (float) m_additionalTime / millisToSecConvert;
        resetTime();
    }

    public void printPtime(){
        System.out.printf("Total time in child processes: %.4f seconds\n", getPtime());
    }
}
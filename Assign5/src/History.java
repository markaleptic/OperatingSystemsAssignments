import java.util.ArrayList;

/**
 * Helper class for CommandLineInterpreter class that maintains the history of commands entered
 * and access to reenter those commands.
 */
public class History{
    private ArrayList<String> m_history;
    public static final int initHistorySize = 50;
    private int m_position;

    public History(){
        m_history = new ArrayList<String>(initHistorySize);
        m_position = 0;
    }

    public void logToHistory(String command){
        m_history.add(m_position, command);
        m_position++;
    }

    public String getFromHistory(int getPosition){
        if (getPosition < 0 || getPosition > m_history.size()){
            System.out.printf("Item %d is not available in history");
            return "";
        }
        return m_history.get(getPosition - 1);
    }

    public void printHistory(){
        String log = "-- Command History --\n";

        for(int position = 0; position < m_position; position++){
            log += String.valueOf(position + 1) + " : " + m_history.get(position) + "\n";
        }
        
        System.out.printf(log);
    }
}
import java.util.HashMap;


public class ResultTable extends HashMap{
    private HashMap<Integer,Integer> digits;

    public ResultTable(int size){
        this.digits = new HashMap(size);
    }

    public synchronized void insert(int k, int val){
        Integer result = this.digits.put(k, val);
        
        if(result != null){
            System.out.printf("Error in inserting key: %d, val: %d", k, val);
            System.out.printf("Redundant result for key");
        }
    }
}
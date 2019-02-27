import java.util.LinkedList;
import java.util.Queue;

public class TaskQueue extends Queue{
    private Queue<Integer> tq;


    // Create randomized task queue by generating a range from 1 to size, 
    // construct TaskQueue with randomized range. Used List and ArrayList to
    // work easily with Collections.shuffle().
    public TaskQueue(int size){
        ArrayList<Integer> rng = new ArrayList<Integer>(size);
        for(int i = 1; i < size; i++){
            rng.add(i);
        }
        Collections.shuffle(rng);
        this.tq = new LinkedList(rng);
    }

    /**
     * Synchronized interfaced for returning head value of the FIFO
     * queue. Returns an Optional if the Queue is empty.
     */
    public synchronized Integer pop(){
        Integer digit = tq.poll();
        if(digit == null){
            Optional<Integer> empty = Optional.isEmpty();
            return empty;
        }

        return digit;
    }
}
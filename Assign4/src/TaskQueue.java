import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Optional;
import java.util.Collections;

public class TaskQueue<E>{
    private Queue<Integer> tq;


    // Create randomized task queue by generating a range from 1 to size, 
    // construct TaskQueue with randomized range. Used List and ArrayList to
    // work easily with Collections.shuffle().
    public TaskQueue(int size){
        ArrayList<Integer> rng = new ArrayList<Integer>(size);
        for(int i = 0; i < size; i++){
            Integer val = Integer.valueOf(i + 1);
            rng.add(i, val);
        }
        Collections.shuffle(rng);
        this.tq = new LinkedList<Integer>(rng);
    }

    /**
     * Synchronized interfaced for returning head value of the FIFO
     * queue. Returns an Optional if the Queue is empty.
     */
    public synchronized Optional<Integer> pop(){
        Integer digit = tq.poll();
        if(digit == null){
            return Optional.empty();
        }

        return Optional.of(digit);
    }

}
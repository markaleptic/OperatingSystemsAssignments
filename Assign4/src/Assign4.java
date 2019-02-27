public class Assign4{
    public static void main(String[] args){

        // Digits of Pi to compute
        digitsOfPi = 1000;
        
        // Create randomized task queue by generating a range from 1 to numDigits, 
        // construct TaskQueue with randomized range. Used List and ArrayList to
        // work easily with Collections.shuffle().
        ArrayList<Integer> rng = new ArrayList<Integer>(digitsOfPi);
        for(int i = 1; i < digitsOfPi; i++){
            rng.add(i);
        }
        Collections.shuffle(rng);
        Queue <Integer> TaskQueue = new LinkedList(rng);

        // Dynamically create threads based upon available processors
        int procs = Runtime.getRuntime().availableProcessors();

    } 

}
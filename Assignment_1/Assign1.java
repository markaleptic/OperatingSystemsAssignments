import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import static java.lang.Math.sqrt;
import static java.lang.Math.pow;

/**
 *
 * @author      Mark Allred, A01647260 <mark.allred@aggiemail.usu.edu>
 *
 */

public class Assign1 {
	public static final int ERROR = Integer.MIN_VALUE;

	public static void main(String[] args) {
    
		// verify correct number of inputs provided
		if(args.length % 2 != 0){
			System.out.println("Incorrect number of input parameters");
			help();
			return;
		}

		// Display help-menu if no arguments provided
		if(args.length == 0){
			help();
		}
		
		// Iterate over every other input to iterate over function calls
		for(int i = 0; i < args.length; i += 2){
		
			// Convert each function parameter to int 
			// or notify incorrect input and continue
			int n = convertInput(args[i + 1]);
			if(n == ERROR){
				System.out.printf("Incorrect input for %s: %s\n", args[i], args[i + 1]);
				continue;
			}
			
			// Direct console arguments to requisite function.
			switch(args[i]){
			
				// Each computation is performed and result is displayed to user.
				// If errors occur, each functions will report specifics and return -1
				case "-fib": 
					int fib = Fibonacci(n);
					
					if(fib != ERROR){
						System.out.printf("Fibonacci of %d is %d\n", n, fib);
					}
					break;

				case "-fac":
					BigInteger fac = Factorial(n);
					if(!fac.equals(BigInteger.valueOf(ERROR))){
						System.out.printf("Factorial of %d is %d\n", n, fac);
					}

					break;
					
				case "-e":
					double eVal = e(n);
					
					if((int) eVal != ERROR){
						System.out.printf("Value of e using %d iterations is %s\n", n, Double.toString(eVal));
					}
					break;
					
				default: 
					System.out.printf("Unknown command line argument: %s\n", args[i]);
					break;
			}
		}
	}
  
  /**
   * Function that displays the command line inputs for this program
   */
  private static void help(){
  	String helpMsg = "--- Assign 1 Help ---\n"
  				   + "-fib [n] : Compute the Fibonacci of [n]; valid range [0, 40]\n"
  				   + "-fac [n] : Compute the factorial of [n]; valid range, [0, ∞)\n"
  				   + "-e [n]   : Compute the value of 'e' using [n] iterations; valid range [1, 2147483647]\n";
  					
  	System.out.print(helpMsg);
  }

  /**
   * Converts function paramter String to int or returns -1
   */
  private static int convertInput(String input){
  	try {
		int n = Integer.parseInt(input);
		return n;
	} 
	catch(NumberFormatException ex) {
  		return ERROR;
  	}
  }
  
  /**
   * Produces the (n + 1)th Fibonacci number (sequence starts at 1) using the closed form solution
   */
  private static int Fibonacci(int n){
  	// We're ignoring zero in the Fibonacci sequence, closed form includes 0 so increment n + 1
	n++;
  	
	// Verify input is within valid range or exit function
  	if(n > 40 || n < 0){
  		System.out.println("Fibonacci valid range is [0, 40]");
  		return ERROR;
  	}
  	
  	// Closed form solution for the nth Fibonacci Number
  	double leftHandSide  = pow(((1 + sqrt(5)) / 2), n);
  	double rightHandSide = pow(((1 - sqrt(5)) / 2), n);
  	leftHandSide -= rightHandSide;
  	leftHandSide *= (1 / sqrt(5));
  	int result = (int) leftHandSide;
  	
  	return result;
  }
  
  /**
   * Calculates BigInteger factorials for an input number n
   */
  private static BigInteger Factorial(int n){
  
  	// Verify input is within valid range or exit function
  	if(n < 0){
  		System.out.println("Factorial valid range is [0, ∞)");
  		BigInteger rangeError = BigInteger.valueOf(ERROR);
  		return rangeError;
  	}
  
  	// Initialize result
  	BigInteger result = BigInteger.ONE;
  	
  	// fFctorial calculations
  	for(int i = 2; i <= n; i++){
  		result = result.multiply(BigInteger.valueOf(i));
  	}
  	
  	return result;
  }
  
  
  /**
   * Function to estimate the value 'e' using the Taylor Series from 1 to n.
   */
  private static double e(int n){
  	// Verify input is within valid range or exit function
  	if(n > Integer.MAX_VALUE || n < 1){
  		System.out.println("Valid e iterations is [1, 2147483647]");
  		return (double) ERROR;
  	}
  	
  	// Initialize 3 BigDecimal values to perform sum of 'e',
  	// factorial computation in the denominator, and the 
  	// quotient of 1 / i!
  	BigDecimal e = BigDecimal.ONE;
	BigDecimal denominator = BigDecimal.ONE;
	BigDecimal quotient    = BigDecimal.ONE;
		
	// Perform factorial and quotient operations, then add to 'e'
  	for(int i = 1; i <= n; i++){
  		denominator = new BigDecimal(Factorial(i));
  		quotient    = BigDecimal.ONE.divide(denominator, 1000, RoundingMode.HALF_UP);
  		e = e.add(quotient);
  	}
  	
  	// Convert the estimated 'e' to double to print required precision
  	double result = e.doubleValue();
  	return result;  	
  }
}
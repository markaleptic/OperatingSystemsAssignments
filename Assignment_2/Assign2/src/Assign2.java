import java.lang.Runtime;
import java.lang.System;
import java.util.Properties;
import java.lang.reflect.Method;

/**
 * @author      Mark Allred, A01647260 <mark.allred@aggiemail.usu.edu>
 */
public class Assign2 {
	public static void main(String[] args) {

    // Parameter Descriptions
    String[] cpuDescriptions  = new String[]{"Processors"};
    String[] memDescriptions  = new String[]{"Free Memory", "Total Memory", "Max Memory"};
    String[] dirDescriptions  = new String[]{"Working Directory", "User Home Directory"};
    String[] osDescriptions   = new String[]{"OS Name", "OS Version"};
    String[] javaDescriptions = new String[]{"Java Vendor", "Java Runtime", "Java Version", 
                                             "Java VM Version", "Java VM Name"};

    // Parameter Call Properties for System.getProperty or Runtime.getRuntime
    String[] cpuProperties  = new String[]{"availableProcessors"};
    String[] memProperties  = new String[]{"freeMemory", "totalMemory", "maxMemory"};
    String[] dirProperties  = new String[]{"user.dir", "user.home"};
    String[] osProperties   = new String[]{"os.name", "os.version"};
    String[] javaProperties = new String[]{"java.vm.vendor", "java.runtime.name", "java.version",
                                           "java.vm.specification.version", "java.vm.name"};

		// Display help-menu if no arguments provided
		if(args.length == 0) help();
		
		// Iterate over console arguments and direct to requisite function
		for(int i = 0; i < args.length; i++){

			// Direct console arguments to requisite function.
			switch(args[i]){
			
				case "-cpu": 
					getValues(args[i], cpuProperties, cpuDescriptions);
					break;

				case "-mem":
          getValues(args[i], memProperties, memDescriptions);
					break;
        
        case "-dirs":
          getValues(args[i], dirProperties, dirDescriptions);
          break;
        
        case "-os":
          getValues(args[i], osProperties, osDescriptions);
					break;

				case "-java":
          getValues(args[i], javaProperties, javaDescriptions);
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
  	String helpMsg = "------------------------------ Assign 2 Help ------------------------------\n"
  				   + "-cpu  : Reports the number of CPUs (physical and logical) available.\n"
  				   + "-mem  : Reports the available free memory, total memory, and max memory.\n"
  				   + "-dirs : Reports the process working directory and the user's home directory.\n"
				     + "-os   : Reports the OS name and OS version.\n"
				     + "-java : Reports the following items about the JVM\n"
				     + "\t- Java Vendor\n"
				     + "\t- Java Runtime Name\n"
				     + "\t- Java Version\n"
				     + "\t- Java VM Version\n"
				     + "\t- Java VM Name\n";
  					
  	System.out.print(helpMsg);
  }


  /**
   * Method to get system, runtime, memory, directory properties based on console user input.
   * Received properties are passed to printer function with descriptions to display results to user.
   * 
   * @param flag User input from console that is used to report the requested properties.
   * @param callProperties Properties for each flag that are used to query Runtime / System.
   * @param descriptions The description or name for each property.
   */
  private static void getValues(String flag, String[] callProperties, String[] descriptions){

    // Verify descriptions and values are same length
    assert callProperties.length == descriptions.length : "getValues: Unequal array lengths";

    // Dynamically initialize values array
    String[] values = new String[descriptions.length];

    // Set values based on System.getProperty or Runtime.getRuntime()
    if(flag.equals("-cpu") || flag.equals("-mem")){
      String strFormat = new String();
      if(flag.equals("-cpu")){
        strFormat = "%d";
      } else {
        strFormat = "%,15d";
      }
      // Dynamically call the Runtime function to get the correct cpu, memory property
      try {
        // Get only instance of Runtime to invoke
        Object run = Runtime.getRuntime();

        // Call each function and cast to string
        for(int i = 0; i < callProperties.length; i++){

          Method method = Runtime.class.getMethod(callProperties[i], (Class<?>[]) null);
          Object returnVal = method.invoke(run);
          values[i] = String.format(strFormat, returnVal);

        }
      } catch (Exception e) {
        System.out.println(e.toString());
      }
    } else {
      // Call the System property using the property name passed to the function
      for(int i = 0; i < callProperties.length; i++){
        values[i] = System.getProperty(callProperties[i]);
      }
    }

    // Pass descriptions and values to print pretty function
    printer(descriptions, values);
  }
  

  /**
   * Print Pretty function to report descriptions and values to the console.
   * 
   * @param descriptions The description or name for each property.
   * @param values The reported value from the Runtime / System property.
   */
  private static void printer(String[] descriptions, String[] values){
    // Verify descriptions and values are same length
    assert descriptions.length == values.length : "Unequal array lengths";

    // Used to set space between descriptions and values--2 tabs for long, 1 for short
    int singleIndent = 15;

    // Print descriptions and value inline with carriage return
    for(int i = 0; i < descriptions.length; i++){
      System.out.printf(descriptions[i] + ":");

      if(descriptions[i].length() >= singleIndent){
        System.out.printf("\t");
      } else {
        System.out.printf("\t\t");
      }
      System.out.println(values[i]);
    }
  }
}
import java.lang.System;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader;
import java.util.regex.*;
import java.util.Arrays;
import java.lang.Process;
import java.lang.ProcessBuilder;
import java.text.SimpleDateFormat;


public class CommandLineInterpreter{
    private History m_commandHistory;
    private Ptime m_childProcessTime;
    private String m_currentDirPath;
    private File m_currentDirFolder;
    private Boolean m_exitFlag = false;
    public static final SimpleDateFormat datePattern = new SimpleDateFormat("MMM dd, yyyy HH:mm");

    
    public CommandLineInterpreter(){
        m_currentDirPath = System.getProperty("user.dir");
        m_currentDirFolder = new File(m_currentDirPath);
        m_commandHistory = new History();
        m_childProcessTime = new Ptime();
    }


    /**
     * Reset the current path folder based on the state path.
     */
    private void setDirFolder(){
        // Update member working directory Folder object
        m_currentDirFolder = new File(m_currentDirPath);
        
        // Update system working directory for inheritance
        try {
            System.setProperty("user.dir", m_currentDirFolder.getCanonicalPath());
        } catch (Exception e) {
            System.out.println("Unable to change working directory");
        }
    }

    /**
     * Function to udpated set the current path and folder.
     */
    private void setCurrentDirPath(String path){
        m_currentDirPath = path;
        setDirFolder();
    }

    /**
     * Function to call from main that initiates the command line to accept and direct arguments. Interpreter
     * continues to run while class exitFlag is false. Commands and parameters are logged to History before
     * executing the command.
     */
    public void startInterpreter(){

        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in));
        String readCommands = "";
        String[] commandAndParams;

        while(!m_exitFlag){
            System.out.printf("[%s]:", m_currentDirPath);
            try {    
                readCommands = reader.readLine();
            } catch (Exception ex) {
                System.out.println("Illegal Command");
                System.out.printf("Exception: %s", ex.toString());
            }
            m_commandHistory.logToHistory(readCommands);
            executeCommand(readCommands);
        }
    }

    /**
     * Function to direct commands to a specified internal command or attempt external command through created processes.
     * 
     * @commands - First item in commands array is the command, remaining items are parameters for the command.
     */
    private void executeCommand(String commands){
        String[] params = splitCommand(commands);
        String command = params[0];

        switch(command.toLowerCase()){
            case "exit":
                exit();
                break;
            case "history":
                m_commandHistory.printHistory();
                break;
            case "^":
                // Assume no values beyond 0, 1 in array
                try {
                    int itemPos = Integer.parseInt(params[1]);
                    executeCommand(m_commandHistory.getFromHistory(itemPos));
                } catch (Exception e) {
                    System.out.printf("Unable to select %s item from history", params[1]);
                }
                break;
            case "list":
                list();
                break;
            case "cd":
                cd(params);
                break;
            case "ptime":
                ptime();
                break;
            default:
                int index = -1;
                for(int i = 0; i < params.length; i++){
                    if(params[i].endsWith("|")){
                        pipedExternalCommand(Arrays.copyOfRange(params, 0, i), Arrays.copyOfRange(params, i + 1, params.length));
                        return;
                    }
                }
                externalCommand(params);
                break;
        }
        return;
    }
    

    /**
     * Built-in function to exit interpreter by resetting exit flag.
     */
    private void exit(){ m_exitFlag = true; }


    /**
     * Built-in function that displays the total time spent waiting and executing child processes
     * in seconds through Ptime class.
     */
    private void ptime(){ m_childProcessTime.printPtime(); }


    /**
     * Built-in function that displays the history of commands executed through the History class.
     */
    private void history(){ m_commandHistory.printHistory(); }


    /**
     * Built-in function that displays the current items in a directory (similar to ls -l)
     * by accessing the class level current directory folder and path.
     */
    private void list(){
        setDirFolder();
        File[] files = m_currentDirFolder.listFiles();

        System.out.printf("total %d\n", files.length);

        for(File file : files){
            System.out.printf("%s", file.isDirectory() ? 'd' : '-');
            System.out.printf("%s", file.canRead() ? 'r' : '-');
            System.out.printf("%s", file.canWrite() ? 'w' : '-');
            System.out.printf("%s", file.canExecute() ? 'e' : '-');
            System.out.printf("%10d ", file.length());
            System.out.printf("%s ", datePattern.format(file.lastModified()));
            System.out.println(file.getName());
        }
    }


    /**
     * Built-in function to provide change-directory functionality by setting the current path and folder.
     */
    private void cd(String[] newDirPath){
        String[] params;
        String newDir = "";
        File checkDir;

        // No parameters with cd command, set directory to home path
        if (newDirPath.length == 1){
            setCurrentDirPath(System.getProperty("user.home"));
            return;
        }
        
        // Set path with included parameters. Deconstruct file path if possible by splitting each parameter
        // with the forward slash.
        for(int i = 1; i < newDirPath.length; i++){
            
            params = newDirPath[i].split("/");
            
            for(String param : params){
                if(param.equals("..")){
                    setCurrentDirPath(m_currentDirFolder.getParent());
                } else {
                    newDir = m_currentDirPath + "/" + param;
                    checkDir = new File(newDir);
                    if(checkDir.isDirectory()){
                        setCurrentDirPath(newDir);
                    } else {
                        System.out.printf("Subdirectory '%s' does not exist\n", param);
                    }
                }

            }
        }
    }



    /**
     * Attempt to execute command externally through or process or notify user of illegal command.
     */
    private void externalCommand(String[] commands){
        Boolean isTimed = true;

        if(commands[commands.length - 1].equals("&")){
            isTimed = false;
            commands = Arrays.copyOfRange(commands, 0, commands.length - 1);
        }

        // Create builder to execute commands
        ProcessBuilder builder = new ProcessBuilder(commands);

        // Inherit parent process directory
        builder.directory(m_currentDirFolder);

        // Set builder to inherit the input and output stream of the user's process.
        builder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

        try {
            // childProcessTime will not actually start timing if "&" is not provided.
            m_childProcessTime.start(isTimed);
            Process process = builder.start();

            // Wait for process of waiting is specified or exit
            if(isTimed){
                process.waitFor();
                m_childProcessTime.end();
            }

        } catch (Exception e) {
            System.out.println("Illegal Command");
        }
    }


    /**
     * Function attempts to execute and combined separate processes or notifies user of illegal command.
     * Processes are combined by connecting output and input streams and reading to the buffer.
     */
    private void pipedExternalCommand(String[] firstCommands, String[] secondCommands){
        // Set the builders so the first commands are processed and passed to the second commands
        ProcessBuilder builder1 = new ProcessBuilder(firstCommands);
        ProcessBuilder builder2 = new ProcessBuilder(secondCommands);

        // Redirect builder1's output to the pipe so builder2 can receive it
        // and set builder2's output to the current process to display output
        // to user's shell.
        builder1.redirectInput(ProcessBuilder.Redirect.INHERIT);
        builder1.redirectOutput(ProcessBuilder.Redirect.PIPE);
        builder2.redirectOutput(ProcessBuilder.Redirect.INHERIT);


        try {
            // Start both processes
            m_childProcessTime.start(true);
            Process process1 = builder1.start();
            Process process2 = builder2.start();

            java.io.InputStream   in = process1.getInputStream();
            java.io.OutputStream out = process2.getOutputStream();

            int byt;
            while((byt = in.read()) != -1){
                out.write(byt);
            }

            out.flush();
            out.close();

            process1.waitFor();
            process2.waitFor();
            m_childProcessTime.end();

        } catch (Exception e) {
            System.out.println("Unable to complete the piped commands");
            m_childProcessTime.end();
        }

    }


    /**
     * Split the user command by spaces, but preserving them when inside double-quotes.
     * Code Adapted from: https://stackoverflow.com/questions/366202/regex-for-splitting-a-string-using-space-when-not-surrounded-by-single-or-double
     */
    public static String[] splitCommand(String command) {
        java.util.List<String> matchList = new java.util.ArrayList<>();
        
        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        if(command == null){
            command = "error";
        }
        Matcher regexMatcher = regex.matcher(command);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                // Add double-quoted string without the quotes
                matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                // Add single-quoted string without the quotes
                matchList.add(regexMatcher.group(2));
            } else {
                // Add unquoted word
                matchList.add(regexMatcher.group());
            }
        }
        
        return matchList.toArray(new String[matchList.size()]);
    }

}

import java.io.File;
import java.nio.file.Paths;

//Class Replacer 
//This class create a backup, write into a log file and replace some every text with a given route for every .txt file and its subdirectories.

class replacer {
    String dir, text_pattern, text_replace
    String fileText
    File file, log_file

    //Constructor
    replacer(dir,text_pattern,text_replace, fileText, file, log_file){
      this.dir = dir
      this.text_pattern = text_pattern
      this.text_replace = text_replace
      this.fileText = fileText
      this.file = file
      this.log_file = log_file
    }
    //Create the backup files
    public void backup(file, fileText){
      def backupFile = new File(file.path + ".bak");
      backupFile.write(fileText);
    }
    //Try to write the logs
    public void write_log(log_file, pattern, replaceText, filePath){
      log_file.append("\n Coincidence found in ${filePath} for the word ${pattern} replaced with ${replaceText}")
    }
    //Replace the given pattern with another text
    public void replace_text(file, fileText, pattern, replaceText, log_file){
      if(fileText.contains(pattern)){
        def filePath = file.path
        try{
          write_log(log_file, pattern, replaceText, filePath);
        }catch(Exception ex){
          println "No log file provided"
        }
        def timer = new Date().toString()
        println "\n Coincidence found in ${filePath} for the word ${pattern} replaced with ${replaceText} at ${timer}"  
        fileText = fileText.replaceAll(pattern, replaceText);
        file.write(fileText);
        }
    }
}

//This is the main class that cointain all the logics and main method
//Also we validate that the given inputs are correct

class program{
  //Checks if the file exists
  public static boolean isFileDirectoryExists(File file){
    if (file.exists())
    {
      return true;
    }
    println "The file Directory do not Exists "
    return false;
  }
  //Checks if the given route is a valid Directory
  public static boolean isDirectoryExists(String directoryPath){
    if (Paths.get(directoryPath).toFile().isDirectory())
    {
      return true;
    }
    println "The route is not a Directory "
    return false;
  }

  //Main method of the program
  public void main(args) {
    // Variables from the System
    def dir = System.console().readLine "Please enter a path to a directory containing some files ";
    def pattern = System.console().readLine "The original text or pattern to use for searching in the files ";
    def replaceText = System.console().readLine "A new text string which will replace the original text ";
    def log_dir = System.console().readLine "path to a file for outputting a list of which files were modified "
    def currentDir = new File(dir);
    def log_file = new File(log_dir);
    def arg1 = false;
    def arg2 = false;

    // The second route, the log file is an optional input.
    if(log_dir == ""){
      arg1 = isFileDirectoryExists(currentDir) && isDirectoryExists(currentDir.path);
      arg2 = true;
    }else{
      arg1 = isFileDirectoryExists(currentDir) && isDirectoryExists(currentDir.path);
      arg2 = isFileDirectoryExists(currentDir) && isDirectoryExists(currentDir.path);
    }

    //Both arg1 and arg2 must be true to start running the program
    //arg1 and arg2 help identify if route 1 and route 2 exists
    if(arg1 && arg2){
      //Method from Class File who loop across every file in a Directory and its Subdirectories
      currentDir.eachFileRecurse(
        {file ->
            if (file.name.endsWith(".txt")) {
              def fileText = file.text;
              def replacer = new replacer(dir, pattern, replaceText, fileText, file, log_file);
              replacer.backup(file, fileText);
              replacer.replace_text(file, fileText, pattern, replaceText, log_file);
          }
        }
      )
    }
    else{
      println "No valid inputs provided"
    }
  }
}

//Entry Point
//Where instance the program class
//Tracking time elapsed
def start_time = new Date().toString()
def program = new program()
program.main()
def end_time = new Date().toString()
println "Finished at ${end_time} starting at ${start_time}"
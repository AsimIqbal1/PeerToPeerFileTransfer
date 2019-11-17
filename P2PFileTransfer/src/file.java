import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class file{

    public static void main(String[] args) {

        try
        {
            // Save original out stream.
            PrintStream originalOut = System.out;
            // Save original err stream.
            PrintStream originalErr = System.err;

            // Create a new file output stream.
            PrintStream fileOut = new PrintStream("./dog.txt");
            // Create a new file error stream. 
            PrintStream fileErr = new PrintStream("./err.txt");

            // Redirect standard out to file.
            System.setOut(fileOut);
            // Redirect standard err to file.
            System.setErr(fileErr);

            // Wrapped Scanner to get user input.
            Scanner scanner = new Scanner(System.in);


            // Print data in command console.
            originalOut.println("Please input your email. ");

            // Read string line.
            String line = scanner.nextLine();

            for(int i=0;i<250;i++){
                String cat = "/home/asimvm/traindata/train/dog."+i+".jpg\t1";
                // If user input 'quit' then break the loop.
                originalOut.println(cat);
                System.out.println(cat);
            }

            System.setOut(originalOut);
            System.setErr(originalErr);

        }catch(FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
    }

    /* Check whether the string is an email address or not. */
    private static boolean isValidEmail(String email)
    {
        boolean ret = true;

        if(email==null || email.trim().length()==0)
        {
            ret = false;
        }else
        {
            int index = email.indexOf("@");
            if(index == -1)
            {
                ret = false;
            }
        }

        return ret;
    }

}
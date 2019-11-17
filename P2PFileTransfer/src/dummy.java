import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class dummy {

    private static TimerTask task = new TimerTask()
    {
        public void run()
        {
            System.out.println("You did nothing");
        }
    };

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(task, 5000);
        Scanner scan = new Scanner(System.in);
        System.out.println(scan.next());
        timer.cancel();

        System.out.println("Your input is taken.");
    }
}

import java.util.Scanner;
import java.util.Random;

public class RockPaperScissorsGame {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        //Display game rules
        System.out.println("Welcome to the Rock-Paper-Scissors Game!");
        System.out.println("Rules: Rock beats Scissors, Scissors beats Paper, Paper beats Rock.");

        //Game Loop
        while(true) {
            //Get user's choice
            System.out.print("Enter your choice (rock, paper, scissors): ");
            String userChoice = sc.nextLine().toLowerCase();    //Convert to lowercase

            //Validate user's choice
            if(!userChoice.equals("rock") && !userChoice.equals("paper") && !userChoice.equals("scissors")) {
                System.out.println("Invalid choice. Please enter rock, paper, or scissors.");
                continue;   //Go to the next iteration of the loop
            }

            //Generate computer's choice
            int computerChoiceIndex = rand.nextInt(3);   //Generate a random number between 0 and 2
            String[] choices = {"rock", "paper", "scissors"};
            String computerChoice = choices[computerChoiceIndex];   //Get the choice at the index

            //Display computer's choice
            System.out.println("User's choice: " + userChoice);
            System.out.println("Computer's choice: " + computerChoice);

            //Determine the winner
            String result;

            if (userChoice.equals(computerChoice)) {
                result = "It's a tie!";
            } else if ((userChoice.equals("rock") && computerChoice.equals("scissors")) || (userChoice.equals("scissors") && computerChoice.equals("paper")) || (userChoice.equals("paper") && computerChoice.equals("rock"))) {
                result = "You win!";
            } else {
                result = "Computer wins!";
            }

            //Display the result
            System.out.println(result);

            //Ask if the user wants to play again
            System.out.print("Do you want to play again? (yes/no): ");
            String playAgain = sc.nextLine().toLowerCase();    //Convert to lowercase

            if(!playAgain.equals("yes")) {
                break;  //Exit the loop
            }
            System.out.println();
        }//end of while loop
        System.out.println("Thank you for playing Rock-Paper-Scissors Game!");
        sc.close();
    }//end of main method
}//end of class

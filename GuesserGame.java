import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * GuesserGame - A simple console-based number guessing game
 * The computer generates a random number, and the player tries to guess it
 * within a limited number of attempts.
 */
public class GuesserGame {
    private Scanner scanner;
    private Random random;
    private int numberToGuess;
    private int maxAttempts;
    private int attemptCount;
    private int minRange;
    private int maxRange;
    private int gamesPlayed;
    private int gamesWon;

    /**
     * Constructor to initialize the game
     */
    public GuesserGame() {
        scanner = new Scanner(System.in);
        random = new Random();
        minRange = 1;
        maxRange = 100;
        gamesPlayed = 0;
        gamesWon = 0;
    }

    /**
     * Starts the game and handles replay logic
     */
    public void startGame() {
        System.out.println("Welcome to the Guesser Game!");

        boolean playAgain = true;
        while (playAgain) {
            setDifficulty();
            generateNumber();
            playRound();
            gamesPlayed++;

            playAgain = askReplay();
        }

        // Display game statistics before exiting
        System.out.println("\nGame Statistics:");
        System.out.println("Games Played: " + gamesPlayed);
        System.out.println("Games Won: " + gamesWon);
        System.out.println("Win Rate: " + (gamesPlayed > 0 ? (gamesWon * 100 / gamesPlayed) + "%" : "0%"));
        System.out.println("\nThanks for playing!");

        // Close the scanner to prevent resource leak
        scanner.close();
    }

    /**
     * Sets the difficulty level which determines the number of attempts
     */
    private void setDifficulty() {
        System.out.println("\nSelect difficulty level:");
        System.out.println("1. Easy (10 attempts)");
        System.out.println("2. Medium (5 attempts)");
        System.out.println("3. Hard (3 attempts)");
        System.out.print("Enter your choice (1-3): ");

        int choice = 2; // Default to medium difficulty

        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    maxAttempts = 10;
                    System.out.println("Easy mode selected. You have 10 attempts.");
                    break;
                case 2:
                    maxAttempts = 5;
                    System.out.println("Medium mode selected. You have 5 attempts.");
                    break;
                case 3:
                    maxAttempts = 3;
                    System.out.println("Hard mode selected. You have 3 attempts.");
                    break;
                default:
                    maxAttempts = 5;
                    System.out.println("Invalid choice. Medium difficulty selected by default. You have 5 attempts.");
            }
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Consume invalid input
            maxAttempts = 5;
            System.out.println("Invalid input. Medium difficulty selected by default. You have 5 attempts.");
        }

        setCustomRange();
    }

    /**
     * Allows the user to set a custom range for the number to guess
     */
    private void setCustomRange() {
        System.out.println("\nWould you like to set a custom range? (yes/no)");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes") || response.equals("y")) {
            try {
                System.out.print("Enter minimum value: ");
                minRange = scanner.nextInt();

                System.out.print("Enter maximum value: ");
                maxRange = scanner.nextInt();

                scanner.nextLine(); // Consume newline

                if (minRange >= maxRange) {
                    System.out.println("Invalid range! Using default range (1-100).");
                    minRange = 1;
                    maxRange = 100;
                }
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Consume invalid input
                System.out.println("Invalid input! Using default range (1-100).");
                minRange = 1;
                maxRange = 100;
            }
        } else {
            minRange = 1;
            maxRange = 100;
        }

        System.out.println("You'll be guessing a number between " + minRange + " and " + maxRange + ".");
    }

    /**
     * Generates a random number within the specified range
     */
    private void generateNumber() {
        numberToGuess = random.nextInt(maxRange - minRange + 1) + minRange;
        attemptCount = 0;
    }

    /**
     * Plays a complete round of the game
     */
    private void playRound() {
        System.out.println("\nI've chosen a number between " + minRange + " and " + maxRange + ".");
        System.out.println("You have " + maxAttempts + " attempts to guess it.");

        long startTime = System.currentTimeMillis();
        boolean hasWon = false;

        while (attemptCount < maxAttempts) {
            int guess = getUserGuess();

            if (guess == -1) {
                // Invalid input was entered
                continue;
            }

            attemptCount++;

            if (guess == numberToGuess) {
                hasWon = true;
                break;
            } else {
                giveHint(guess);
            }
        }

        long endTime = System.currentTimeMillis();
        double timeElapsed = (endTime - startTime) / 1000.0;

        if (hasWon) {
            System.out.println("\n🎉 Congratulations! You guessed the number " + numberToGuess +
                    " in " + attemptCount + " attempt" + (attemptCount > 1 ? "s" : "") + "!");
            System.out.printf("Time taken: %.2f seconds\n", timeElapsed);
            gamesWon++;
        } else {
            System.out.println("\n😢 Sorry, you've run out of attempts!");
            System.out.println("The number was: " + numberToGuess);
        }
    }

    /**
     * Gets a valid guess from the user
     * 
     * @return the user's guess, or -1 if input was invalid
     */
    private int getUserGuess() {
        System.out.print("\nAttempt " + (attemptCount + 1) + "/" + maxAttempts + ": Enter your guess: ");

        try {
            int guess = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (guess < minRange || guess > maxRange) {
                System.out.println("Please enter a number between " + minRange + " and " + maxRange + ".");
                return -1;
            }

            return guess;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Consume invalid input
            System.out.println("Invalid input! Please enter a number.");
            return -1;
        }
    }

    /**
     * Provides feedback to the user about their guess
     * 
     * @param guess the user's guess
     */
    private void giveHint(int guess) {
        if (guess < numberToGuess) {
            System.out.println("Too low! Try again.");
        } else {
            System.out.println("Too high! Try again.");
        }

        // Calculate and show distance as a percentage for better guidance
        int range = maxRange - minRange;
        int distance = Math.abs(guess - numberToGuess);
        int percentage = (distance * 100) / range;

        if (percentage < 5) {
            System.out.println("You're very close! 🔥");
        } else if (percentage < 10) {
            System.out.println("You're getting closer! 👍");
        }
    }

    /**
     * Asks the user if they want to play again
     * 
     * @return true if the user wants to play again, false otherwise
     */
    private boolean askReplay() {
        System.out.print("\nDo you want to play again? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("yes") || response.equals("y");
    }

    /**
     * Main method to run the game
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        GuesserGame game = new GuesserGame();
        game.startGame();
    }
}
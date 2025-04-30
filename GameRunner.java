/**
 * Simple runner class to start the Guesser Game
 */
public class GameRunner {
    public static void main(String[] args) {
        System.out.println("Starting Guesser Game...");
        GuesserGame game = new GuesserGame();
        game.startGame();
    }
}
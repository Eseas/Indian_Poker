public class Main {
    public static void main(String[] args) {
        Player PlayerA = new Player(1, 100);
        Player PlayerB = new Player(2, 100);

        Game game = new Game();

        game.LetsGame(PlayerA, PlayerB);
    }
}
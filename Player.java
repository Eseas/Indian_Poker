public class Player {
    int id;
    int money;
    Card haveCard = new Card();
    boolean isCall = false;
    boolean isDie = false;

    public Player() {
    }

    public Player(int money) {
        this.money = money;
    }

    public Player(int id, int money) {
        this.id = id;
        this.money = money;
    }

    public Player(boolean isCall, boolean isDie) {
        this.isCall = isCall;
        this.isDie = isDie;
    }
}
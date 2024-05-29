public class Card {
    int number;
    boolean burn = false;

    public Card(int number) {
        this.number = number;
    }

    public Card(int number, boolean burn) {
        this.number = number;
        this.burn = burn;
    }

    public Card() {
    }
}
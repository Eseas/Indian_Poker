import java.util.Scanner;

/**


 *
 * turns 메서드
 * 1. 카드를 나눠받는다.
 */

public class Game {
    int money, callMoney, turns = 1;
    Deck deck = new Deck();

    /**
     * * 게임 시작 메서드
     *  * 1. 카드 덱을 초기화한다.
     *  * 1-1. 카드는 1부터 10까지 2세트씩 20장이 준비된다.
     *  * 2. player의 money에서 callmoney를 빼고 money에 call money만큼 더한다.
     *  * 2-1. callMoney는 턴/2를 가진다.
     */

    void GameInit(Player PlayerA, Player PlayerB) {
        for (int i = 0; i < 20; i++) {
            deck.Cards[i] = new Card(i % 10 + 1, false);
        }
        deck.CanUseCard = 20;
        CardShuffle(deck);
        PlayerA.money = 100;
        PlayerB.money = 100;

    }

    /**
     *  *
     *  * 카드 셔플 매서드
     *  * 카드 덱을 매개변수로 받는다.
     *  * 카드 덱의 원래 장수만큼 for문을 돌려 카드를 섞는다.
     *  * 카드 덱을 리턴한다.
     *  *
     */

    Deck CardShuffle(Deck deck) {

        for (int i = 0; i < deck.CanUseCard; i++) {
            int a = (int) (Math.random() * deck.CanUseCard - 1);

            Card tmp = deck.Cards[i];
            deck.Cards[i] = deck.Cards[a];
            deck.Cards[a] = tmp;
        }

        return deck;
    }

    /**
     *  * 카드 드로우 메서드
     *  * 1. max : 카드 덱의 매수, 만큼 random을 돌린다.
     *  * 2. Player의 hasCard를 랜덤을 돌려 뽑은 카드로 바꾼다.
     *  * 3. Player가 뽑은 카드의 burn 변수를 true로 바꾼다.
     *  * 4. 랜덤을 돌려 뽑은 카드와 덱의 남은 장수 -1 번째 매서드를 바꾼다.
     *  * 5. deck의 CanUseCards를 -1 한다.
     */

    void CardDraw(Player PlayerA) {
        int drawnum = (int) (Math.random() * (deck.CanUseCard - 1));
        PlayerA.haveCard = deck.Cards[drawnum];
        deck.Cards[drawnum].burn = true;

        Card tmp = deck.Cards[drawnum];
        deck.Cards[drawnum] = deck.Cards[deck.CanUseCard-1];
        deck.Cards[deck.CanUseCard-1] = tmp;

        deck.CanUseCard = deck.CanUseCard - 1;
    }

    /**
     * 플레이어 A의 배팅을 진행한다.
     *
     * 3개의 선택지가 존재한다.
     * 1. Call - Call money만큼 돈을 뺴고, isCall을 true로 바꾼다.
     * 2. Raise - Call money + raise만큼 돈을 빼고 상대 플레이어의 isCall을 false로 바꾼다.
     * 3. Die - Player의 isDie를 true로 바꾸고 턴을 종료한다.
     */

    void Bat(Player PlayerA, Player PlayerB) {
        boolean BatEnd = false;
        System.out.println();
        System.out.println("플레이어 " + PlayerA.id + "의 배팅입니다.");
        System.out.println("상대 플레이어의 카드는 " + PlayerB.haveCard.number + "입니다.");
        System.out.println("현재 플레이어가 가진 돈 : " + PlayerA.money + ", 현재 Call Money : " + callMoney + ", 현재 판의 돈 : " + money);
        System.out.println("1~3번의 숫자를 입력해주세요. 1 : Call, 2 : Raise, 3 : Die");
        System.out.println("배팅을 진행해주세요. : ");
        Scanner scanner = new Scanner(System.in);
        int Batting = scanner.nextInt();

        while(!BatEnd) {
            switch(Batting) {
                case 1:
                    System.out.println("Call 하셨습니다.");
                    if(PlayerA.money < callMoney) {
                        System.out.println("올인하셨습니다.");
                        PlayerA.money = 0;
                        money = money + PlayerA.money;
                    } else {
                        PlayerA.money = PlayerA.money - callMoney;
                        money = money + callMoney;
                    }
                    PlayerA.isCall = true;
                    BatEnd = true;
                    break;
                case 2:
                    System.out.println("Raise 하셨습니다.");
                    System.out.println("Call Money : " + callMoney + "입니다. 얼마를 올리시겠습니까?");
                    int raise = scanner.nextInt();
                    if(PlayerA.money < raise + callMoney) {
                        System.out.println("올인하셨습니다.");
                        callMoney = callMoney + PlayerA.money;
                        money = money + callMoney;
                        PlayerA.money = 0;
                    } else {
                        callMoney = callMoney + raise;
                        PlayerA.money = PlayerA.money - callMoney;
                        money = money + callMoney;
                    }
                    PlayerB.isCall = false;
                    PlayerA.isCall = true;
                    BatEnd = true;
                    break;
                case 3:
                    System.out.println("Die를 선택하셨습니다.");
                    PlayerA.isDie = true;
                    BatEnd = true;
                    break;
                default:
                    System.out.println("숫자를 잘못 입력하셨습니다.");
            }
        }
    }

    /**
     * whoWinner 메서드
     *
     * 1. 한 쪽이 죽어있는지 확인한다.
     * 1-1. 플레이어 한 명이 죽었다면 상대의 승리로 한다.
     * 2. 플레이어가 둘 다 살아있다면, 카드를 공개해 더 높은 숫자를 가진 쪽이 승리한다.
     * 2-1. 이긴 쪽에 money를 더하고 종료한다.
     * 2-2. 둘이 같은 숫자의 카드를 가지고 있다면, money를 절반씩 나누어 가지고 종료한다.
     */

    void whoWinner(Player PlayerA, Player PlayerB) {
        if(PlayerA.isDie) {
            winner(PlayerB);
            return;
        } else if(PlayerB.isDie) {
            winner(PlayerA);
            return;
        }
        System.out.printf("플레이어 A의 카드는 %d입니다. 플레이어 B의 카드는 %d입니다.%n", PlayerA.haveCard.number, PlayerB.haveCard.number);
        if(PlayerA.haveCard.number == PlayerB.haveCard.number) {
            System.out.println("비겼습니다.");
            System.out.printf("이 판의 돈 : %d을 %d만큼 나눠갖습니다.%n", money, money / 2);
            PlayerA.money = PlayerA.money + (int) (money / 2);
            PlayerB.money = PlayerB.money + (int) (money / 2);
        } else if(PlayerA.haveCard.number > PlayerB.haveCard.number) {
            winner(PlayerA);
        } else {
            winner(PlayerB);
        }
    }

    void winner(Player PlayerA) {
        System.out.println((turns - 1) + "번째 턴을 플레이어 " + PlayerA.id + "가 이겼습니다.");
        PlayerA.money = PlayerA.money + money;
        System.out.println("플레이어 " + PlayerA.id + "의 돈 : " + PlayerA.money);
    }

    /**
     * turnStart 메서드
     */

    void turnStart(Player PlayerA, Player PlayerB) {
        System.out.println();
        System.out.printf("%d번째 턴을 시작합니다.%n", turns);
        turns = turns + 1;

        CardDraw(PlayerA);
        CardDraw(PlayerB);

        callMoney = ((turns / 2 + 1));
        PlayerA.money = PlayerA.money - callMoney;
        PlayerB.money = PlayerB.money - callMoney;

        money = callMoney * 2;
    }

    /**
     * turnEnd 메서드
     *
     * player의 haveCard룰 0으로 바꾼다.
     * money를 0으로 바꾼다.
     * 카드를 섞는다.
     */

    void turnEnd(Player PlayerA, Player PlayerB) {
        System.out.println("턴이 끝났습니다.");
        System.out.printf("플레이어 A의 돈 : %d, 플레이어 B의 돈 : %d%n", PlayerA.money, PlayerB.money);
        PlayerA.haveCard.number = 0;
        PlayerB.haveCard.number = 0;
        PlayerA.isDie = false;
        PlayerB.isDie = false;
        PlayerA.isCall = false;
        PlayerB.isCall = false;

        money = 0;
        CardShuffle(deck);
    }

    /**
     *  Bat 메서드
     *
     *  턴에 따라 카드 드로우 순서와 배팅 순서가 정해진다.
     *  짝수 턴일 경우 플레이어 A가 먼저, 홀수 턴일 경우 플레이어 B가 먼저 시작한다.
     *  플레이어 A가 먼저 진행하는 것 기준으로 설명한다.
     *
     *  1. 카드를 1장씩 뽑는다.
     *  2. 배팅을 진행한다.
     *  2-1. 한 명이 죽었다면, 배팅페이즈를 종료한다.
     *  2-2. 다른 쪽이 Raise를 했다면, 다시 상대는 다시 배팅을 진행한다.
     *  2-3. 둘 다 콜을 했다면 배팅 페이즈를 종료한다.
     *  3. whoWinner 페이즈를 실행한다.
     *  4. turn end 페이즈를 실행한다.
     */

    void turns(Player PlayerA, Player PlayerB) {
        if(turns % 2 == 0) {
            turnStart(PlayerA, PlayerB);
            while(!(PlayerA.isCall && PlayerB.isCall)) {
                if(!PlayerA.isCall)
                    Bat(PlayerA, PlayerB);
                if(PlayerA.isDie) {
                    break;
                }
                if(!PlayerB.isCall)
                    Bat(PlayerB, PlayerA);
                if(PlayerB.isDie) {
                    break;
                }
            }
        } else {
            turnStart(PlayerB, PlayerA);
            while(!(PlayerA.isCall && PlayerB.isCall)) {
                if(!PlayerB.isCall)
                    Bat(PlayerB, PlayerA);
                if(PlayerB.isDie) {
                    break;
                }
                if(!PlayerA.isCall)
                    Bat(PlayerA, PlayerB);
                if(PlayerA.isDie) {
                    break;
                }

            }
        }
        whoWinner(PlayerA, PlayerB);
        turnEnd(PlayerA, PlayerB);
    }

    void LetsGame(Player PlayerA, Player PlayerB) {
        GameInit(PlayerA, PlayerB);
        while(turns < 11) {
            if(PlayerA.money <= 0 || PlayerB.money <= 0) {
                System.out.println("한 명이 파산했습니다. 게임을 종료합니다.");
                break;
            }
            turns(PlayerA, PlayerB);
        }
        System.out.printf("플레이어 A의 money : %d, 플레이어 B이 money : %d%n", PlayerA.money, PlayerB.money);

        if(PlayerA.money == PlayerB.money) {
            System.out.println("비겼습니다.");
        } else if(PlayerA.money > PlayerB.money) {
            System.out.println("플레이어 A가 이겼습니다.");
        } else {
            System.out.println("플레이어 B가 이겼습니다.");
        }
    }
}

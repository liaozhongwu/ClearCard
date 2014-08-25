package clearcard.game;

import java.util.Random;

public enum CardLife {
	ACE(1),TWO(2),THREE(3),FOUR(4),FIVE(5),SIX(6),SEVEN(7),EIGHT(8),NINE(9),TEN(10),JACK(11),QUEUE(12),KING(13);
	
	private static Random random = new Random();
	private int life;
	
	private CardLife(int life){
		this.life = life;
	}
	public int getLife() {
		return life;
	}
	public static CardLife random(){
		return values()[random.nextInt(values().length)];
	}
}

package clearcard.game;

import java.util.Random;

public enum Variety {
	HEART,SPADE,DIAMOND,CLUB;
	private static Random random = new Random();
	
	public static Variety random(){
		return values()[random.nextInt(values().length)];
	}
}

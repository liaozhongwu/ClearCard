package clearcard.game;

import com.example.clearcard.R;

public class Card extends ElementType{
	private CardLife life;
	
	public Card(Variety variety,CardLife life){
		super(variety);
		setLife(life);
	}
	public CardLife getLife() {
		return this.life;
	}
	public void setLife(CardLife life) {
		this.life = life;
	}
	@Override
	public int[] images() {
		int[] images = new int[2];
		switch(variety){
		case CLUB: images[0] = R.drawable.club;break;
		case DIAMOND: images[0] = R.drawable.diamond;break;
		case HEART: images[0] = R.drawable.heart;break;
		case SPADE: images[0] = R.drawable.spade;break;
		}
		switch(life){
		case ACE: images[1] = R.drawable.ace;break;
		case EIGHT: images[1] = R.drawable.eight;break;
		case FIVE: images[1] = R.drawable.five;break;
		case FOUR: images[1] = R.drawable.four;break;
		case JACK: images[1] = R.drawable.jack;break;
		case KING: images[1] = R.drawable.king;break;
		case NINE: images[1] = R.drawable.nine;break;
		case QUEUE: images[1] = R.drawable.ace;break;
		case SEVEN: images[1] = R.drawable.seven;break;
		case SIX: images[1] = R.drawable.six;break;
		case TEN: images[1] = R.drawable.ten;break;
		case THREE: images[1] = R.drawable.three;break;
		case TWO: images[1] = R.drawable.two;break;
		}
		return images;
	}
}

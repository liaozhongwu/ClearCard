package clearcard.game;

import com.example.clearcard.R;

public class Bomb extends ElementType{
	private BombType type;

	public Bomb(Variety variety,BombType type){
		super(variety);
		setType(type);
	}
	public BombType getType() {
		return type;
	}

	public void setType(BombType type) {
		this.type = type;
	}
	
	@Override
	public int[] images() {
		return new int[]{R.drawable.bomb_club_0d};
	}
}

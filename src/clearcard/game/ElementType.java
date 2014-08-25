package clearcard.game;

public abstract class ElementType {
	protected Variety variety;
	public ElementType(Variety variety){
		setVariety(variety);
	}
	public void setVariety(Variety variety) {
		this.variety = variety;
	}
	public Variety getVariety() {
		return this.variety;
	}
	public abstract int[] images();
	public static ElementType random(){
		return new Card(Variety.random(), CardLife.random());
	}
}

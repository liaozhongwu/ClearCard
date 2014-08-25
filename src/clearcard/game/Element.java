package clearcard.game;


public class Element implements Comparable<Element>{
	protected int x;
	protected int y;
	protected int dropOffset;
	protected ElementType type;

	public Element(int x,int y,int dropOffset, ElementType type){
		setX(x);
		setY(y);
		setDropOffset(dropOffset);
		setType(type);
	}
	public void setX(int x){
		this.x = x;
	}
	public int getX(){
		return this.x;
	}
	public void setY(int y){
		this.y = y;
	}
	public int getY(){
		return this.y;
	}
	public int getDropOffset() {
		return dropOffset;
	}
	public void setDropOffset(int dropOffset) {
		this.dropOffset = dropOffset;
	}	
	public ElementType getType() {
		return type;
	}
	public void setType(ElementType type) {
		this.type = type;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof Element){
			int x = ((Element) o).getX();
			int y = ((Element) o).getY();
			if(this.x == x && this.y == y) return true;
			else return false;
		}
		else{
			return false;
		}
	}
	@Override
	public int compareTo(Element that) {
		if(this.x+this.y != that.x+that.y)
			return (this.x+this.y) - (that.x+that.y);
		else
			return this.x-that.x;
	}
	public boolean near(Element that) {
		if(Math.abs(x - that.getX()) == 1 && y == that.getY()) return true;
		else if(Math.abs(y - that.getY()) == 1 && x == that.getX()) return true;
		else return false;
	}
	public boolean varietyEquals(Element that){
		return type.getVariety() == that.getType().getVariety();
	}
	public static Element random(int x,int y){
		return new Element(x, y, 0, ElementType.random());
	}
}
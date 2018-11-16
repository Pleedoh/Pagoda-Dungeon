

public class Trap extends Entity {
	boolean rising;
	int native_y,native_h;// what it was before it started rising
	int i = 0;//timer for rising
	public Trap(int x,int y,int w,int h){
		super(x, y, w, h);
		this.native_y = y;
		this.native_h = h;
		state = 2;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void reset(){
		y = native_y;
		height = native_h;
	}
}

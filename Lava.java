

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Lava extends Trap{
	
	public Lava(int x, int y, int w, int h,boolean rising) {
		super(x, y, w, h);
		ID = 8;
		this.rising = rising;//whether the lava rises up
	}
	@Override
	public void draw(Graphics2D g2D){
		g2D.setPaint(new GradientPaint(x, y, new Color(209, 138, 31,150),x,y+height,new Color(206, 47, 4)));
		g2D.fillRect(x, y, width, height);
//		g2D.setColor(Color.green);
//		g2D.draw(hitBox());
	}
	@Override
	public Rectangle hitBox(){
		return new Rectangle(x,y,width,height);
	}
}

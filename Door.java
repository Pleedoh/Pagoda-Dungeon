

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;

//brings you to next level
public class Door extends Wall{
	boolean red;
	public Door(int x, int y, int w, int h) {
		super(x, y, w, h);
		ID = 7;
	}
	public Door(int x, int y, int w, int h,boolean red) {
		super(x, y, w, h);
		ID = 7;
		this.red = true;
	}
	@Override
	public void draw_wall(Graphics2D g2D){
		if(red){
			g2D.setColor(new Color(51,51,51));
			g2D.fillRect(x,y,width,height);
			g2D.setPaint(new GradientPaint(x, y, new Color(51,51,51,100),x+width-50,y,new Color(255,0,0,150)));
			g2D.fillRect(x, y, width, height);
		}
		else{
			g2D.setColor(new Color(51,51,51));
			g2D.fillRect(x,y,width,height);
			g2D.setPaint(new GradientPaint(x, y, new Color(51,51,51,100),x+width-50,y,new Color(255,225,47,150)));
			g2D.fillRect(x, y, width, height);
		}
		
	}
	@Override
	public Rectangle hitBox(){
		return new Rectangle(x+100,y,width/2,height);
	}

}

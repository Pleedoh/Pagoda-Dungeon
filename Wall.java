

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Wall extends Entity  {
	public Wall(int x, int y,int w,int h) {
		super(x, y,w,h);
	//	setState(-10);
		this.ID = 1;
	}
	//to be used by wall objects only
	public void draw_wall(Graphics2D g2D){
		//g2D.translate(camera.getX(),camera.getY());
		g2D.setColor(new Color(25,25,25));
		g2D.fillRect(x,y,width,height);
		//g2D.translate(-camera.getX(),-camera.getY());
	}
	@Override
	public Rectangle hitBox(){
		return new Rectangle(x,y,width,height);
	}
	//for platforms, will be overridden
	public void setHi(int hi){
		
	}
	public void setLo(int lo){
		
	}
	public int getHi(){
		return 0;
	}
	public int getLo(){
		return 0;
	}
}

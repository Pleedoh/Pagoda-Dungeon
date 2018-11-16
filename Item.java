

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

abstract public class Item extends Entity{
	boolean exists = true;
	int durability;
	Item(int x, int y, int w, int h, int T) {
		super(x, y, w, h, T);
	}
	Item(int x, int y, int w, int h, int T,int Vx,int Vy) {
		super(x, y, w, h, T);
		this.Vx = Vx;
		this.Vy = Vy;
	}
//	public Rectangle hitBox(){
//		return new Rectangle(x,y,width,height);
//	}
	@Override
	public Rectangle hitBox_top(){
		return new Rectangle(x+width/4,y+3,width/2,height/2);
	}
	@Override
	public Rectangle hitBox_right(){
		return new Rectangle(x+width-5,y+5,5,height-10);
	}
	@Override
	public Rectangle hitBox_left(){
		return new Rectangle(x,y+5,5,height-10);
	}
	@Override
	public Rectangle hitBox(){
		return new Rectangle(x+20, y+10, width-40, height-10);
	}
	@Override
	public Rectangle hitBox_bot(){
		return new Rectangle(x+width/4 ,y+height-10,width/2,height/5+5);
	}
	public void draw_health(Graphics2D g2D){
		g2D.setColor(new Color((int)(255-255*(health/maxHP)),(int)(255*(health/maxHP)),0));
		g2D.fillRect(x,y+10,(int)(100*(health/maxHP)),5);
	}
	public void offMap(){
		this.exists = false;
	}
	ImageIcon inv_sprite;
}

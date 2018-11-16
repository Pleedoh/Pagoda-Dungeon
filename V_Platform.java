

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class V_Platform extends Wall{
	int hi,lo;//highpoint from the y & lowpoint from the y
	public V_Platform(int x,int y,int w,int h,int hi,int lo){
		super(x,y,w,h);
		this.hi = hi;
		this.lo = lo;
		setState(-1);
		Vy = 1;
		ID = 6;
	}
	
	void move(){
		if(Main.screen_state != 1) return;
		if(y > hi){
			Vy*=-1;
		}
		if(y < lo){
			Vy*=-1;
		}
		y+=Vy;
	}
	@Override
	public void draw_wall(Graphics2D g2D){
		move();
		g2D.setColor(new Color(25,25,25));
		g2D.fillRect(x,y,width,height);
	}
	@Override
	public void setHi(int hi){
		this.hi = hi;
	}
	@Override
	public void setLo(int lo){
		this.lo = lo;
	}
	@Override
	public int getHi(){
		return hi;
	}
	@Override
	public int getLo(){
		return lo;
	}
	@Override
	public Rectangle hitBox(){
		return new Rectangle(x,y+5,width,height-5);
	}
	
}

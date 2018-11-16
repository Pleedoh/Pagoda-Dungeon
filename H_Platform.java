

import java.awt.Color;
import java.awt.Graphics2D;

public class H_Platform extends Wall{
	int hi,lo;//highpoint from the x & lowpoint from the x
	public H_Platform(int x,int y,int w,int h,int hi,int lo){
		super(x,y,w,h);
		this.hi = hi;
		this.lo = lo;
		setState(-1);
		Vx = 1;
		ID = 5;
	}
	
	void move(){
		if(Main.screen_state != 1) return;
		if(x > hi){
			Vx*=-1;
		}
		if(x < lo){ 
			Vx*=-1;
		}
		x+=Vx;
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

}

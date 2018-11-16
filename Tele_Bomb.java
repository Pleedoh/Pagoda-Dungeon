

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Tele_Bomb extends Projectile{

	
	public Tele_Bomb(int x,int y,int w,int h,int T,int Vx,int Vy,boolean friendly){//regular
		super(x,y,w,h,T,friendly);
		sprites.add(loadImage("/stuff/pbomb1.png"));
		sprites.add(loadImage("/stuff/pbomb2.png"));
		sprites.add(loadImage("/stuff/pbomb3.png"));
		sprites.add(loadImage("/stuff/pbomb4.png"));
		sprites.add(loadImage("/stuff/pbomb5.png"));
		sprites.add(loadImage("/stuff/pbomb6.png"));
		sprites.add(loadImage("/stuff/pbomb7.png"));
		sprites.add(loadImage("/stuff/pbomb8.png"));
		state = 2;
		ID = 13;
		this.Vx = Vx;
		this.Vy = Vy;
		death.add(loadImage("/stuff/smoke_1.png"));
		death.add(loadImage("/stuff/smoke_2.png"));
		death.add(loadImage("/stuff/smoke_4.png"));
		dT = 10;
	}
	public Tele_Bomb(int x,int y,int w,int h,int T,boolean friendly){//player smoke effect
		super(x,y,w,h,T,friendly);
		state = -3;
		ID = 13;
		death.add(loadImage("/stuff/smoke_1.png"));
		death.add(loadImage("/stuff/smoke_2.png"));
		death.add(loadImage("/stuff/smoke_4.png"));
		dT = 10;
	}
	public Tele_Bomb(int x,int y,int w,int h,int T,boolean friendly,int a){//boss smoke effect
		super(x,y,w,h,T,friendly);
		state = -3;
		ID = 13;
		death.add(loadImage("/stuff/boss_smoke_1.png"));
		death.add(loadImage("/stuff/boss_smoke_2.png"));
		death.add(loadImage("/stuff/boss_smoke_3.png"));
		death.add(loadImage("/stuff/boss_smoke_4.png"));
		dT = 5;
	}
	
//	@Override
//	public void draw(Graphics2D g2D){
//		g2D.drawImage(sprites.get(i).getImage(),x,y,null);
//		g2D.setColor(Color.green);
//		g2D.draw(hitBox_bot());
//		g2D.draw(hitBox_left());
//		g2D.draw(hitBox_right());
//		g2D.draw(hitBox_top());
//	}
	@Override
	public void update_movement(){
		if(!friendly) return;
		if(Vy > 15) Vy = 15;//terminal velocity
		if(stopped) return;
		if(!grounded){
			Ay = 0.5;
		}
		else{
			if(!moving) Vx = default_Vx;
			Ay = 0;
		}
	}
	@Override
	public Rectangle hitBox_bot(){
		return new Rectangle(x+6 ,y+height+10,width-12,height/7);
	}
	@Override
	public Rectangle hitBox_top(){
		return new Rectangle(x+6,y-10,width-12,height/7+3);
	}
	@Override
	public Rectangle hitBox_right(){
		return new Rectangle(x+width,y+5,8,height-5);
	}
	@Override
	public Rectangle hitBox_left(){
		return new Rectangle(x-10,y+5,8,height-5);
	}
	@Override
	public Rectangle hitBox(){
		return new Rectangle(x+20, y+10, width-40, height-10);
	}
	@Override
	public void die(){
		Animation_Handler.remove(this,level);
	}

}

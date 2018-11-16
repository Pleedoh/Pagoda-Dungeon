

import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Bat extends Entity {
	int Vel;
	int range = (int)(Math.random()*100+50);
	Bat(int x, int y, int w, int h,int Vel,int T) {
		super(x, y, w, h);
		this.nX = x;
		this.nY = y;
		sprites_left.add(loadImage("/stuff/bat_left1.png"));
		sprites_left.add(loadImage("/stuff/bat_left2.png"));
		sprites_right.add(loadImage("/stuff/bat_right1.png"));
		sprites_right.add(loadImage("/stuff/bat_right2.png"));
		death.add(loadImage("/stuff/death_1.png"));
		death.add(loadImage("/stuff/death_2.png"));
		death.add(loadImage("/stuff/death_3.png"));
		this.Vel = Vel;
		ID = 15;
		dT =5;
		this.T = T;
		if(Math.random() > 0.5) i = 1;//randomize starting frame	
	}
	@Override
	public Rectangle hitBox(){
		return new Rectangle(x+20, y+10, width-40, height-10);
	}
	public void seek(int px,int py){
		if(state == -3) return;
		if(x+50 < px-range){
			Vx = Math.abs(Vel);
			state = 0;
		}
		if(x+50 > px+range){
			Vx = -Math.abs(Vel);
			state = 1;
		}
		if(y+50 < py-range) Vy = Math.abs(Vel);
		if(y+50 > py+range) Vy = -Math.abs(Vel);
	}
	@Override
	public void update_movement(){
		seek(Main.player.x,Main.player.y);
	}
	@Override
	public void die(){
		Animation_Handler.remove(this,level);
	}

}

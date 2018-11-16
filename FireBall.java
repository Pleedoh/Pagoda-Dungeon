

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class FireBall extends Projectile{
	int vel;
	public FireBall(int x, int y, int w, int h, int T, boolean friendly,int state,int vel) {
		super(x, y, w, h, T, friendly);
		this.state = state;
		sprites_right.add(loadImage("/stuff/FB_R1.png"));
		sprites_right.add(loadImage("/stuff/FB_R2.png"));
		sprites_left.add(loadImage("/stuff/FB_L1.png"));
		sprites_left.add(loadImage("/stuff/FB_L2.png"));
		death.add(loadImage("/stuff/e1.png"));
		death.add(loadImage("/stuff/e2.png"));
		death.add(loadImage("/stuff/e3.png"));
		death.add(loadImage("/stuff/e4.png"));
		this.vel = vel;
		ID = 9;
		dT = 8;
	}
	public Rectangle hitBox(){
		return new Rectangle(x,y+25,width,height);
	}
	public void die(){
		Animation_Handler.remove(this,level);
	}
	@Override
	public void draw(Graphics2D g2D){  
		switch(state){
		case 0://right
			if(t == T){
				i++;
				t = 0;
			}
			else t++;
			if(i >= sprites_right.size()) i = 0;
			g2D.drawImage(sprites_right.get(i).getImage(),x+=vel,y,null);
			break;
		case 1://left
			if(t == T){
				i++;
				t = 0;
			}
			else t++;
			if(i >= sprites_left.size()) i = 0;
			g2D.drawImage(sprites_left.get(i).getImage(),x-=vel,y,null);
			break;
		case -3:
			if(t == T){
				i++;
				t = 0;
			}
			else t++;
			if(i < death.size())g2D.drawImage(death.get(i).getImage(),x+=Vx,y+=Vy+=Ay,null);
			//done executing it's death animation
			if(i == death.size()){
				die();//based from object to remove itself from list
			}
			break;
		}
//		g2D.setColor(Color.green);
//		g2D.draw(hitBox());
	}
}

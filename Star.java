

import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Star extends Projectile {
	
	public Star(int x,int y,int w,int h,int T,int Vx,int Vy,boolean friendly){
		super(x,y,w,h,T,friendly);
		this.Vx = Vx;
		this.Vy = Vy;
		//stars dont change sprites based on direction
		setState(2);
		sprites.add(loadImage("/stuff/star_1.png"));
		sprites.add(loadImage("/stuff/star_2.png"));
		sprites.add(loadImage("/stuff/star_3.png"));
		ID = 3;
	}
	@Override
	public Rectangle hitBox(){
		return new Rectangle(x+10,y+10,width-20,height-20);
	}

}

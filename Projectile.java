

import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;

abstract public class Projectile extends Entity{
	//it has left and right from entity
	//for projectiles that have different sprites for different directions
	boolean friendly;
	public Projectile(int x,int y,int w,int h,int T,boolean friendly){
		super(x,y,w,h,T);
		this.friendly = friendly;
	}
	boolean stopped;
	public void stop(){
		Ay = 0;
		Vx = 0;
		Vy = 0;
		this.stopped = true;
	}
	public boolean is_Stopped(){
		return stopped;
	}
	public Rectangle BIG_SHAQ(){//AGH BOOUM
		return new Rectangle(x,y,200,200);
	}
}



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Bomba extends Projectile{

	public Bomba(int x,int y,int w,int h,int T,int Vx,int Vy,boolean friendly){
		super(x,y,w,h,T,friendly);
		sprites.add(loadImage("/stuff/bbomb1.png"));
		sprites.add(loadImage("/stuff/bbomb2.png"));
		sprites.add(loadImage("/stuff/bbomb3.png"));
		sprites.add(loadImage("/stuff/bbomb4.png"));
		sprites.add(loadImage("/stuff/bbomb5.png"));
		sprites.add(loadImage("/stuff/bbomb6.png"));
		sprites.add(loadImage("/stuff/bbomb7.png"));
		sprites.add(loadImage("/stuff/bbomb8.png"));
	
		
		state = 2;
		ID = 14;
		this.Vx = Vx;
		this.Vy = Vy;
		dT = 8;
		death.add(loadImage("/stuff/e1.png"));
		death.add(loadImage("/stuff/e2.png"));
		death.add(loadImage("/stuff/e3.png"));
		death.add(loadImage("/stuff/e4.png"));
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
	public Rectangle hitBox_bot(){
		return new Rectangle(x+width/4 ,y+height-10,width/2,height/5);
	}
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
	public void die(){
		Animation_Handler.remove(this,level);
	}
	@Override
	public void update_movement(){
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
	@Override	public void draw(Graphics2D g2D){
		switch(state){
		case 0:
			if(t == T){
				i++;
				t = 0;
			}
			else t++;
			if(i >= sprites_right.size()) i = 0;
			update_movement();
			g2D.drawImage(sprites_right.get(i).getImage(),x+=Vx,(int)(y+=Vy+=Ay),null);
			break;
		case 1:
			if(t == T){
				i++;
				t = 0;
			}
			else t++;
			if(i >= sprites_left.size()) i = 0;
			update_movement();
			g2D.drawImage(sprites_left.get(i).getImage(),x+=Vx,(int)(y+=Vy+=Ay),null);
			break;
			
		case 2://for non direction specific
			if(t == T){
				i++;
				t = 0;
			}
			else t++;
			if(i >= sprites.size()) i = 0;
			update_movement();
			g2D.drawImage(sprites.get(i).getImage(),x+=Vx,y+=Vy+=Ay,null);
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

	
}


}

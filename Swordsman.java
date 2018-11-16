

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Swordsman extends Entity {
	int upb,lpb,uab,lab,dash,aSpan,span;//Upper patrol bound, lower patrol bound (right & left of x)
	boolean attacking = false;
	boolean lockedOn = false;
	boolean ableDamage = true;

	Swordsman(int x,int y,int w,int h,int T,int state,int span,int aSpan,int speed,int dash) { //aSpan <= span
		super(x, y, w, h,T);
		
		setState(state);
		ID = 19;
		this.dash = dash + speed;
		addSprites();
		nVx = speed;
		nS = state;
		//death.add(new ImageIcon(getClass().getResource("death_4.png")));
		//death.add(new ImageIcon(getClass().getResource("death_5.png")));
		dT = 10;
		Vx = speed;
		upb = x + span;
		lpb = x - span;
		this.aSpan = aSpan;
		this.span = span;
		maxHP = 400;
		health = 400;
		ID = 19;
		default_Vx = speed;
		holdingSword = true;
	}	
	public void addSprites(){
		sprites_left.add(loadImage("/stuff/swordsmanL1.png"));
		sprites_right.add(loadImage("/stuff/swordsmanR1.png"));
		sprites_right.add(loadImage("/stuff/swordsmanR2.png"));
		sprites_left.add(loadImage("/stuff/swordsmanL2.png"));
		
		naked_left.add(loadImage("/stuff/swordsmanL1.png"));
		naked_right.add(loadImage("/stuff/swordsmanR1.png"));
		naked_right.add(loadImage("/stuff/swordsmanR2.png"));
		naked_left.add(loadImage("/stuff/swordsmanL2.png"));
		
		death.add(loadImage("/stuff/death_1.png"));
		death.add(loadImage("/stuff/death_2.png"));
		death.add(loadImage("/stuff/death_3.png"));
		
		SWORD_swing_left.add(loadImage("/stuff/Lswing1.png"));
		SWORD_swing_left.add(loadImage("/stuff/Lswing2.png"));
		SWORD_swing_left.add(loadImage("/stuff/Lswing3.png"));
		SWORD_swing_left.add(loadImage("/stuff/Lswing4.png"));
		SWORD_swing_left.add(loadImage("/stuff/Lswing5.png"));
		SWORD_swing_left.add(loadImage("/stuff/Lswing6.png"));
		SWORD_swing_left.add(loadImage("/stuff/Lswing7.png"));
		SWORD_swing_left.add(loadImage("/stuff/Lswing8.png"));
		SWORD_swing_left.add(loadImage("/stuff/Lswing7.png"));
		SWORD_swing_left.add(loadImage("/stuff/Lswing6.png"));
		SWORD_swing_left.add(loadImage("/stuff/Lswing5.png"));
		SWORD_swing_left.add(loadImage("/stuff/Lswing4.png"));
		SWORD_swing_left.add(loadImage("/stuff/Lswing3.png"));
		SWORD_swing_left.add(loadImage("/stuff/Lswing2.png"));
		SWORD_swing_left.add(loadImage("/stuff/Lswing1.png"));
		
		SWORD_swing_right.add(loadImage("/stuff/Rswing1.png"));
		SWORD_swing_right.add(loadImage("/stuff/Rswing2.png"));
		SWORD_swing_right.add(loadImage("/stuff/Rswing3.png"));
		SWORD_swing_right.add(loadImage("/stuff/Rswing4.png"));
		SWORD_swing_right.add(loadImage("/stuff/Rswing5.png"));
		SWORD_swing_right.add(loadImage("/stuff/Rswing6.png"));
		SWORD_swing_right.add(loadImage("/stuff/Rswing7.png"));
		SWORD_swing_right.add(loadImage("/stuff/Rswing8.png"));
		SWORD_swing_right.add(loadImage("/stuff/Rswing7.png"));
		SWORD_swing_right.add(loadImage("/stuff/Rswing6.png"));
		SWORD_swing_right.add(loadImage("/stuff/Rswing5.png"));
		SWORD_swing_right.add(loadImage("/stuff/Rswing4.png"));
		SWORD_swing_right.add(loadImage("/stuff/Rswing3.png"));
		SWORD_swing_right.add(loadImage("/stuff/Rswing2.png"));
		SWORD_swing_right.add(loadImage("/stuff/Rswing1.png"));
	}
	@Override
	public void die(){
		Animation_Handler.remove(this,level);
	}
	@Override
	public void update_movement(){
		if(!(Main.player.hitBox().intersects(attackLeft()) || Main.player.hitBox().intersects(attackRight()))) holdingSword = true;
		if(Main.screen_state == 1){
			if(Vx > 0) state = 0;
			if(Vx < 0) state = 1;
			uab = x + aSpan;
			lab = x - aSpan;

			if(!lockedOn){
				if (Main.player.hitBox().intersects(trigRight())) {
					Vx = dash;
					lockedOn = true;
				} 
				else if (Main.player.hitBox().intersects(trigLeft())) {
					Vx = -dash;
					lockedOn = true;
				} 
			}
			else if(!(Main.player.hitBox().intersects(trigLeft()) || Main.player.hitBox().intersects(trigRight()))) lockedOn = false;
			
						if(Main.player.hitBox().intersects(attackRight())){
							if(Main.player.x > x) state = 0;
							state = 0;
							attacking = true;
							Vx -=0.5;
							if(Vx < 0) Vx = 0;
						}
						if(Main.player.hitBox().intersects(attackLeft())){
							if(Main.player.x < x) state = 1;
							attacking = true;
							Vx +=0.5;
							if(Vx > 0) Vx = 0;
						}
						else if(attacking){
							if(Main.player.x > x+50) Vx = nVx*0.5;
							else Vx = -nVx*0.5;
							attacking = false;
						}

					
				if(!grounded){
					Ay = 0.5;
				}
				else{
					Ay = 0;
				}
				if(!lockedOn && !attacking){//normal behaviour
					if(!Main.player.hitBox().intersects(doMain())){
						if(state == 0) Vx = nVx;
						else Vx = -nVx;
					}
					if(x+aSpan+50 > upb){//going right
						Vx=-nVx;
					}
					if(x-aSpan+50 < lpb){
						Vx=nVx;
					}
				}
		}
	}
	@Override
	public Rectangle hitBox_bot(){
		return new Rectangle(x+width/4 ,y+height/2,width/2,height/2+5);
	}
	@Override
	public Rectangle hitBox_top(){
		return new Rectangle(x+width/2-3,y+10,width/10,height/10);
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
		return new Rectangle(x+20, y+15, width-40, height-10);
	}
	public Rectangle trigRight(){
		return new Rectangle(x+50,y-50,aSpan,100);
	}
	public Rectangle trigLeft(){
		return new Rectangle(lab+50,y-50,aSpan,100);
	}
	public Rectangle doMain(){
		return new Rectangle(lpb,y-50,upb-lpb,100);
	}
	public Rectangle attackLeft(){
		return new Rectangle(x-20,y,40,100);
	}
	public Rectangle attackRight(){
		return new Rectangle(x+100,y,40,100);
	}
//	@Override
//	public void draw(Graphics2D g2D){
//		g2D.setColor(Color.CYAN);
//		g2D.draw(trigLeft());
//		g2D.setColor(Color.ORANGE);
//		g2D.draw(trigRight());
//		g2D.setColor(Color.pink);
//		g2D.draw(attackRight());
//		g2D.setColor(Color.green);
//		g2D.draw(attackLeft());
//		g2D.setColor(Color.red);
//		g2D.draw(doMain());
//		switch(state){
//		case 0:
//			if(t == T){
//				i++;
//				t = 0;
//			}
//			else t++;
//			if(i >= sprites_right.size()) i = 0;
//			 update_movement();
//				if(ID == 0){
//					g2D.setColor(c);
//					g2D.fillRect((int)(x+Vx+20),(int)(y+Vy+Ay+12),62,88);
//				}
//			g2D.drawImage(sprites_right.get(i).getImage(),x+=Vx,(int)(y+=Vy+=Ay),null);
//			if((ID == 4 || ID == 16) && health < maxHP) draw_health(g2D);
//			break;
//		case 1:
//			if(t == T){
//				i++;
//				t = 0;
//			}
//			else t++;
//			if(i >= sprites_left.size()) i = 0;
//			update_movement();
//			if(ID == 0){
//				g2D.setColor(c);
//				g2D.fillRect((int)(x+Vx+20),(int)(y+Vy+Ay+12),62,88);
//			}
//			g2D.drawImage(sprites_left.get(i).getImage(),x+=Vx,(int)(y+=Vy+=Ay),null);
//			if((ID == 4 || ID == 16) && health < maxHP) draw_health(g2D);
//			break;
//			
//		case 2://for non direction specific
//			if(t == T){
//				i++;
//				t = 0;
//			}
//			else t++;
//			if(i >= sprites.size()) i = 0;
//			update_movement();
//			if(ID == 0){
//				g2D.setColor(c);
//				g2D.fillRect(x+20,y+12,62,88);
//			}
//			g2D.drawImage(sprites.get(i).getImage(),x+=Vx,y+=Vy+=Ay,null);
//			break;
//		case -3:
//			if(t == T){
//				i++;
//				t = 0;
//			}
//			else t++;
//			if(i < death.size())g2D.drawImage(death.get(i).getImage(),x+=Vx,y+=Vy+=Ay,null);
//			//done executing it's death animation
//			if(i == death.size()){
//				die();//based from object to remove itself from list
//			}
//			break;
//		}
//		
//	if(ID ==0 || ID == 4){
//		g2D.draw(hitBox_bot());
//		g2D.draw(hitBox_left());
//		g2D.draw(hitBox_right());
//		g2D.draw(hitBox_top());
//		g2D.draw(hitBox());
//	}
//	g2D.setColor(Color.red);
//	if(ID == 9) g2D.draw(hitBox());
	
//}
}

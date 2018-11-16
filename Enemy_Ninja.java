

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Enemy_Ninja extends Entity{
	Timer shoot_timer;
	int frequency;
	int upb,lpb;//Upper patrol bound, lower patrol bound (right & left of x)
	boolean patrols;//true if patroling
	int x_comp,y_comp;//x and y components of their star toss
	public Enemy_Ninja(int x,int y,int w,int h,int T,int state,int frequency,int x_comp,int y_comp,boolean patrols,int span,int speed){
		super(x,y,w,h,T);
		this.state = state;
		ID = 4;
		this.x_comp = x_comp;
		this.y_comp = y_comp;
		this.frequency = frequency;
		this.patrols = patrols;
		addSprites();
		nVx = speed;
		nS = state;
		nX = x;
		nY = y;
		//death.add(loadImage("/stuff/death_4.png")));
		//death.add(loadImage("/stuff/death_5.png")));
		dT = 10;
		if(patrols){
			Vx = state == 0 ? speed : -speed;
			upb = x + span;
			lpb = x - span;
		}
		maxHP = 300;
		health = 300;
	}
	public void addSprites(){
		sprites_right.add(loadImage("/stuff/enemy_right_1.png"));
		sprites_left.add(loadImage("/stuff/enemy_left_1.png"));
		sprites_right.add(loadImage("/stuff/enemy_right_2.png"));
		sprites_left.add(loadImage("/stuff/enemy_left_2.png"));
		death.add(loadImage("/stuff/death_1.png"));
		death.add(loadImage("/stuff/death_2.png"));
		death.add(loadImage("/stuff/death_3.png"));
	}
	@Override
	public void stop(){
		shoot_timer.stop();
	}
	@Override
	public void die(){
		shoot_timer.stop();
		Animation_Handler.remove(this,level);
	}
	@Override
	public void update_movement(){
		if(Main.screen_state == 1){
			if(patrols){
				if(x > upb){//going right
					Vx*=-1;
				}
				if(x < lpb){
					Vx*=-1;
				}
				if(Vx > 0) state = 0;
				if(Vx < 0) state = 1;
			}
				if(!grounded){
					Ay = 0.5;
				}
				else{
					if(default_Vx != 0) Vx = default_Vx;
					Ay = 0;
				}
		}
	}

	public void start(){
		shoot_timer = new Timer(this.frequency,new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//only shoot if gameplay, this is to prevent weird stuff during loading
				if(Main.screen_state == 1){
					if(state == 0){//lookin right;
						shoot(new Star(x+width-60,y+10,50,50,1,x_comp,-y_comp,false));
					}
					//lookin left
					else shoot(new Star(x+10,y+20,50,50,1,-x_comp,-y_comp,false));
				}
			}
		});
		shoot_timer.start();
	}
	@Override
	public Rectangle hitBox_bot(){
		return new Rectangle(x+width/4 ,y+height/2+10,width/2,height/2+5);
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
}

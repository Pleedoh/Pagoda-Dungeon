import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;

public class Boss extends Entity{
	short f,F;//fireball count
	short b,B;//bat count
	short a,A;//army count
	short p,P;//swordsman count
	boolean firing,spawning,creating,PCing;//Pink Creating
	Timer fade_ticker;
	boolean first=true,Q3=true,HALF=true,THIRD=true,Q4=true,near=true;//boolean markers for actions performed based on health
	Boss(int x, int y, int w, int h,int T) {
		super(x, y, w, h,T);
		sprites_left.add(loadImage("/stuff/boss_left1.png"));
		sprites_left.add(loadImage("/stuff/boss_left2.png"));
		sprites_right.add(loadImage("/stuff/boss_right1.png"));
		sprites_right.add(loadImage("/stuff/boss_right2.png"));
		
		death.add(loadImage("/stuff/boss_death1.png"));
		death.add(loadImage("/stuff/boss_death2.png"));
		death.add(loadImage("/stuff/boss_death3.png"));
		death.add(loadImage("/stuff/boss_death4.png"));
		death.add(loadImage("/stuff/boss_death5.png"));
		ID = 21;
		health = 4000;
		maxHP = 4000;
		dT = 10;
		createRedArmy();
		createPinkArmy();
		spawnBatSwarm();
	}
	public void launchFireVolley(){
		f = 0;
		F = 1;
		firing = true;
	}
	public void teleport(){
		Animation_Handler.add(new Tele_Bomb(x+100,y,50,50,10,false,0), level);
		x = (int)(Math.random()*2050 + 250);
		y -=100;
		Animation_Handler.add(new Tele_Bomb(x+100,y,50,50,10,false,0), level);
	}
	public void spawnBatSwarm(){
		b = 0;
		B = 0;
		spawning = true;
	}
	public void createRedArmy(){//SOVIET RUSSIA
		a = 0;
		A = 0;
		creating = true;
	}
	public void createPinkArmy(){
		p = 0;
		P = 0;
		PCing = true;
	}
	public int getRandomX(){
		return (int)(Math.random()*2500 + 200);
	}
	public int assignState(){
		if(Main.player.x < x) return 1;
		else return 0;
	}
	@Override
	public void damage(int d){
		health-=d;
		if(health < 0) health = 0;
		if(Math.random() < 0.3) teleport();
		if(first){
			createRedArmy();
			teleport();
			first = false;
		}
		if(health/maxHP < 0.75 && Q3){
			createPinkArmy();
			Q3 = false;
		}
		if(health/maxHP < 0.5 && HALF){
			createRedArmy();
			spawnBatSwarm();
			teleport();
			HALF = false;
		}
		if(health/maxHP < 0.3 && THIRD){
			createRedArmy();
			createPinkArmy();
			teleport();
			THIRD = false;
		}
		if(health/maxHP < 0.25 && Q4){
			spawnBatSwarm();
			teleport();
			Q4 = false;
		}
		if(health < 200 && near){
			createRedArmy();
			createPinkArmy();
			teleport();
			near = false;
		}
	}
	@Override
	public void draw_health(Graphics2D g2D,int x,int y){
		g2D.setColor(new Color(119, 11, 11));
		g2D.fillRect(x+125,y-50,(int)(500*(health/maxHP)),50);
	}
	@Override
	public void update_movement(){
		if(Main.player.x-200 < x) state = 1;
		if(Main.player.x-400 > x) state = 0;//fix
		if(!grounded){
			Ay = 0.5;
		}
		else{
			if(!moving) Vx = default_Vx;
			Ay = 0;
		}
	}
	@Override
	public void draw(Graphics2D g2D){
//		g2D.setColor(Color.cyan);
//		g2D.draw(hitBox_bot());
//		g2D.draw(hitBox());

		switch(state){
		case 0://right
			if(t == T){
				i++;
				t = 0;
			}
			else t++;
			if(i >= sprites_right.size()){
				if(Math.random() < 0.1) launchFireVolley();
				if(Math.random() < 0.008) spawnBatSwarm();
				if(Math.random() < 0.007) createRedArmy();
				//mn if(Math.random() < 0.003) createPinkArmy();
				i = 0;
			}
			update_movement();
			if(firing){
				if(f == 10){
					shoot(new FireBall(x+550,y+50*F,50,50,20,false,state,5));
					F++;
					f = 0;
				}
				f++;
				if(F > 4) firing = false;
			}			
			if(spawning){
				if(b == 10){
					Animation_Handler.add(new Bat(x+200,y,100, 100, 2, 10),level);
					B++;
					b = 0;
				}
				b++;
				if(B > 10) spawning = false;
			}
			if(creating){
				if(a == 20){
					int x = getRandomX();
					Enemy_Ninja e = new Enemy_Ninja(x,930,100,100,20,assignState(),(int)(Math.random()*2000+1000),(int)(Math.random()*20)+5,(int)(Math.random()*10)+5,Math.random() > 0.5,200,4);
					e.start();
					Animation_Handler.add(e,level);
					Animation_Handler.add(new Tele_Bomb(x,930,50,50,10,false), level);
					A++;
					a = 0;
				}
				a++;
				if(A > 3) creating = false;
			}
			if(PCing){
				if(p == 20){
					int x = getRandomX();
					Swordsman e = new Swordsman(x,930,100,100,20,assignState(),500,400,(int)(Math.random()*4+2),8);
					Animation_Handler.add(e,level);
					Animation_Handler.add(new Tele_Bomb(x,930,50,50,10,false), level);
					P++;
					p = 0;
				}
				p++;
				if(P > 3) PCing = false;
			}
			g2D.drawImage(sprites_right.get(i).getImage(),x+=Vx,(int)(y+=Vy+=Ay),null);
			draw_health(g2D,x,y);
			break;
		case 1://left
			if(t == T){
				i++;
				t = 0;
			}
			else t++;
			if(i >= sprites_left.size()){
				if(Math.random() < 0.1) launchFireVolley();
				if(Math.random() < 0.008) spawnBatSwarm();
				if(Math.random() < 0.007) createRedArmy();
				//if(Math.random() < 0.003) createPinkArmy();
				i = 0;
			}
			update_movement();
			if(firing){
				if(f == 10){
					shoot(new FireBall(x,y+50*F,50,50,20,false,state,5));
					F++;
					f = 0;
				}
				f++;
				if(F > 4) firing = false;
			}
			if(spawning){
				if(b == 10){
					Animation_Handler.add(new Bat(x+200,y-10*b+40*B,100, 100, 2, 10),level);
					B++;
					b = 0;
				}
				b++;
				if(B > 10) spawning = false;
			}
			if(creating){
				if(a == 20){
					int x = getRandomX();
					Enemy_Ninja e = new Enemy_Ninja(x,930,100,100,20,assignState(),(int)(Math.random()*2000+1000),(int)(Math.random()*20)+5,(int)(Math.random()*10)+5,Math.random() > 0.5,200,4);
					e.start();
					Animation_Handler.add(e,level);
					Animation_Handler.add(new Tele_Bomb(x,930,50,50,10,false), level);
					A++;
					a = 0;
				}
				a++;
				if(A > 3) creating = false;
			}
			if(PCing){
				if(p == 20){
					int x = getRandomX();
					Swordsman e = new Swordsman(x,930,100,100,20,assignState(),500,400,(int)(Math.random()*4+2),8);
					Animation_Handler.add(e,level);
					Animation_Handler.add(new Tele_Bomb(x,930,50,50,10,false), level);
					P++;
					p = 0;
				}
				p++;
				if(P > 3) PCing = false;
			}
			g2D.drawImage(sprites_left.get(i).getImage(),x+=Vx,(int)(y+=Vy+=Ay),null);
			draw_health(g2D,x,y);
			break;
		case -3:
			if(t == T){
				i++;
				t = 0;
			}
			else t++;
			if(i < death.size())g2D.drawImage(death.get(i).getImage(),(x+=Vx)+250,y+=Vy+=Ay,null);
			//done executing it's death animation
			if(i == death.size()){
				victory_fade();
			}
			break;
		}
	}
	@Override
	public Rectangle hitBox_bot(){
		return new Rectangle(x+270,y+height-50,250,100);
	}
	@Override
	public Rectangle hitBox(){
		return new Rectangle(x+270, y+15, width-150, height-10);
	}
	void victory_fadeIN(int x){//fade 
		Main.g2D.setColor(new Color(212,175,55,x));
		Main.g2D.fillRect(0, 0, Main.screenSize.width,Main.screenSize.height);
		Main.start_icon = new ImageIcon(Main.bI);
		Main.start_label.setIcon(Main.start_icon);
		Main.F.add(Main.start_label);// this being the frame
		Main.F.setVisible(true);
	}
	public void victory_fade(){
		Main.screen_state = -1;
		   fade_ticker = new Timer(20, new ActionListener() {
			    int x = 0;
			    boolean black = true;
			    public void actionPerformed(ActionEvent e) {
			     if(black){
			      if(x > 250){
			       black = false;
			       x = 0;
			      }
			      else victory_fadeIN(x+=5);
			     }
			     else{
			    	  Main.screen_state = 7;
			    	  fade_ticker.stop();
			     }
			    }
			   });
			   fade_ticker.start();
	}
}

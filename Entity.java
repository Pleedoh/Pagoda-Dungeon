

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.lang.model.element.Element;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

public abstract class Entity {
	int bombs,tele_bombs;
	boolean hasSword;
	Color c;
	protected int x,y,width,height;
	protected int nX,nY,nS;
	protected double Ay,Vx,Vy,default_Vx,nVx,nVy;//used to add velocity when moving on platform
	protected double health,maxHP,lives;//dealt with in contructor
	boolean swingL,swingR,holdingSword;
	int upb,lpb,uab,lab,dash,aSpan,span;//Upper patrol bound, lower patrol bound (right & left of x)
	int s;//for swing timing
	int swingCD;//for swordsman cd
	ArrayList<ImageIcon> sprites_left = new ArrayList<ImageIcon>();
	ArrayList<ImageIcon> sprites_right = new ArrayList<ImageIcon>();
	
	ArrayList<ImageIcon> naked_left = new ArrayList<ImageIcon>();
	ArrayList<ImageIcon> naked_right = new ArrayList<ImageIcon>();
	
	ArrayList<ImageIcon> SWORD_swing_left = new ArrayList<ImageIcon>();
	ArrayList<ImageIcon> SWORD_swing_right = new ArrayList<ImageIcon>();
	
	ImageIcon swordIdleLeft = loadImage("/stuff/Lswing1.png");
	ImageIcon swordIdleRight = loadImage("/stuff/Rswing1.png");
	//for non direction specific animation
	ArrayList<ImageIcon> sprites = new ArrayList<ImageIcon>();
	ArrayList<ImageIcon> death = new ArrayList<ImageIcon>();
	int i = 0;//keeps track of the current phase in animation
	int T,t,dT;//animation threshold, set to zero to not idle on a sprite
	//dT death animation speed
	//for example if t is 1, it will idle on that one sprite in the set for an extra tick
	//little t is to check to be under the threshold
	boolean left,right;//true if going left or right
	protected int state = 0;/*
	*0 for right, 
	* 1 for left,
	* 2 for non direction dependant sprites
	* 3 for left with sword
	* 4 for right with sword
	* 5 for swing left
	* 6 for swing right
	* -3 for generic death, not direction dependant
	* */
	int level;//what level the entity is in
	//moving denotes currently recieving active keyboard movement
	public boolean grounded,moving;//in the air,able to jump
	int ID;
	/*1 = player
	 * 2 = wall
	 * 3 = star
	 * 4 = enemy throwing ninja
	 * 5 = horizontal platform
	 * 6 = vertical platform
	 * 7 = door
	 * 8 = lava
	 * 9 = fireball
	 * 10 = fire cannon
	 * 11 = heal pot
	 * 12 = heart
	 * 13 = telebomb
	 * 14 = bomba
	 * 15 = bat
	 * 16 = bomber
	 * 17 = bomb item
	 * 18 = telebomb item
	 */ 
	int ammo,ammo_max;//how many stars the player currently has
	public void reload(){
		if(ammo < ammo_max) ammo++;
	}
	public void damage(int d){
		health-=d;
		if(health < 0) health = 0;
		
	}
	public void heal(int h){
		health+=h;
		if(health > maxHP) health = maxHP;
	}
	public void setHealth(int health){
		this.health = health;
	}
	public int getHealth(){
		return (int) health;
	}
	public void setT(int T){
		this.T = T;
	}
	public int getID(){
		return ID;
	}
	public void setDefVx(double def){
		default_Vx = def;
	}
	public double getDefVx(){
		return default_Vx;
	}
	public void setState(int state){
		this.state = state;
		if(state == -3){
			i = 0;
			T = dT;
			t = 0;
		}
	}
	public int getState(){
		return this.state;
	}
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	public void setVx(double Vx){
		this.Vx = Vx;
	}
	public void setVy(double Vy){
		this.Vy = Vy;
	}
	public void addVx(double Vx){
		this.Vx += Vx;
	}
	public void setWidth(int width){
		this.width = width;
	}
	public void setHeight(int height){
		this.height = height;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public double getVx(){
		return Vx;
	}
	public double getVy(){
		return Vy;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public void setAy(double Ay){
		//set the vertical acceleration
		this.Ay = Ay;
	}
//	public void move(){
//		this.moving = true;
//	}
//	public void stop(){
//		this.moving = false;
//	}
	
	/*HitBoxes*/
	/*Place holders: these will be overridden by individual objects, 
	 * like the player. Allows for custom hit boxes on different
	 * entities.
	 */
	public Rectangle hitBox_bot(){
		return null;
	}
	public Rectangle hitBox_top(){
		return null;
	}
	public Rectangle hitBox_right(){
		return null;
	}
	public Rectangle hitBox_left(){
		return null;
	}
	//standard, one only hitbox
	public Rectangle hitBox(){
		return null;
	}
	public void die(){
		
	}
	public void shoot(Projectile p){
		Animation_Handler.add(p,level);
		if(ID == 0){
			if(p.ID == 14) bombs--;
			if(p.ID == 13) tele_bombs--;
		}
	}
	//stop shooting
	public void stop(){
	}
	//start shooting
	public void start(){
	}
	public void reset_pos(){
		x = nX;
		y = nY;
		
	}
	//an object knows what level he's on
	public void setLevel(int level){
		this.level = level;
	}
	//mainly for patrolling ninjas
	public void turn(){
		Vx*=-1;
		state = state == 1 ? 0 : 1;
	}

	//Animated
	Entity(int x,int y,int w,int h,int T){
		this.x = x;
		this.y = y;
		this.T = T;
		width = w;
		height = h;
		nX = x;
		nY = y;
	}
	//Non animated(for walls)
	Entity(int x,int y,int w,int h){
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		nX = x;
		nY = y;
	}
	 protected ImageIcon loadImage(String name){
		 
		 try {
			return new ImageIcon(ImageIO.read(Main.class.getResourceAsStream(name)));
		} 
		 catch (IOException e) {
			System.out.println("Couldn't load " + name);
			e.printStackTrace();
			return null;
		}
	 }
	public void update_movement(){
		
		if(left){
			Vx = default_Vx-5;
			state = 1;
			moving = true;
		}
		if(right){
			Vx = default_Vx+5;
			state = 0;
			moving = true;
		}
		if(!grounded){
			Ay = 0.5;
		}
		else{
			if(!moving) Vx = default_Vx;
			Ay = 0;
		}
	}
	public void draw_health(Graphics2D g2D){
		g2D.setColor(new Color((int)(255-255*(health/maxHP)),(int)(255*(health/maxHP)),0));
		g2D.fillRect(x,y-10,(int)(100*(health/maxHP)),10);
	}
	public void draw_health(Graphics2D g2D,int x,int y){
		g2D.setColor(new Color((int)(255-255*(health/maxHP)),(int)(255*(health/maxHP)),0));
		g2D.fillRect(x,y+53,(int)(50*(health/maxHP)),3);
	}
	public void reset_move(){
		state = nS;
		Vx = nVx;
		Vy = nVy;
		if(nVx == 0) return;
		if(nVx > 0) state = 0;
		else state = 1;
	}
	public void swingLeft(){
		s = 0;
		holdingSword = false;
		swingR = false;
		swingL = true;
	}
	public void swingRight(){
		s = 0;
		holdingSword = false;
		swingL = false;
		swingR = true;
	}
	public Rectangle trigRight(){
		return new Rectangle(x+50,y-50,aSpan,100);
	}
	public Rectangle trigLeft(){
		return new Rectangle(lab+50,y-50,aSpan,100);
	}
	public boolean isSwingingLeft(){
		return swingL;
	}
	public boolean isSwingingRight(){
		return swingR;
	}
	public boolean isHoldingSword(){
		return holdingSword;
	}
	public Rectangle attackLeft(){
		return new Rectangle(x-20,y,40,100);
	}
	public Rectangle attackRight(){
		return new Rectangle(x+100,y,40,100);
	}
	public void refresh(){
		swingR = false;
		swingL = false;
		holdingSword = true;
	}
	public int getSwingFrame(){
		return s;
	}
	public boolean hasSword(){
		return hasSword;
	}
	public void putSword(boolean b){
		holdingSword = b;
	}
	public void dropSword(){
		hasSword = false;
	}
	public void draw(Graphics2D g2D){
//		g2D.setColor(Color.red);
//		g2D.draw(attackLeft());
//		g2D.draw(attackRight());
//		if(ID == 19){
//			g2D.setColor(Color.CYAN);
//			g2D.draw(trigLeft());
//			g2D.setColor(Color.ORANGE);
//			g2D.draw(trigRight());
//			g2D.setColor(Color.pink);
//			g2D.draw(attackRight());
//			g2D.setColor(Color.green);
//			g2D.draw(attackLeft());
//			g2D.setColor(Color.red);
//		}
		switch(state){
				case 0://right
					if(t == T){
						i++;
						t = 0;
					}
					else t++;
					if(i >= sprites_right.size()) i = 0;
					 update_movement();
						if(ID == 0){
							g2D.setColor(c);
							g2D.fillRect((int)(x+Vx+20),(int)(y+Vy+Ay+12),62,88);
						}

					if(holdingSword && !swingR){

						g2D.drawImage(naked_right.get(i).getImage(),x+=Vx,(int)(y+=Vy+=Ay),null);
						g2D.drawImage(swordIdleRight.getImage(),(int)(x+Vx)-50,(int)(y+Vy+Ay)+10*i,null);
					}
					if(swingR){
						
						g2D.drawImage(naked_right.get(i).getImage(),x+=Vx,(int)(y+=Vy+=Ay),null);
						g2D.drawImage(SWORD_swing_right.get(s).getImage(),(int)(x+Vx)-50,(int)(y+Vy+Ay),null);
						s++;
						if(s == SWORD_swing_right.size()){ 
							swingR = false;
							holdingSword = true;
						}
					}
					if(!(holdingSword || swingR)) g2D.drawImage(sprites_right.get(i).getImage(),x+=Vx,(int)(y+=Vy+=Ay),null);
					if((ID == 4 || ID == 16 || ID == 19) && health < maxHP) draw_health(g2D);
					break;
				case 1://left
					if(t == T){
						i++;
						t = 0;
					}
					else t++;
					if(i >= sprites_left.size()) i = 0;
					update_movement();
					if(ID == 0){
						g2D.setColor(c);
						g2D.fillRect((int)(x+Vx+20),(int)(y+Vy+Ay+12),62,88);
					}
	
					if(holdingSword && !swingL){
						
						g2D.drawImage(naked_left.get(i).getImage(),x+=Vx,(int)(y+=Vy+=Ay),null);
						g2D.drawImage(swordIdleLeft.getImage(),(int)(x+Vx)-50,(int)(y+Vy+Ay)+10*i,null);
					}
					if(swingL ){
						g2D.drawImage(naked_left.get(i).getImage(),x+=Vx,(int)(y+=Vy+=Ay),null);
						g2D.drawImage(SWORD_swing_left.get(s).getImage(),(int)(x+Vx)-50,(int)(y+Vy+Ay),null);
						s++;
						if(s == SWORD_swing_left.size()){ 
							swingL = false;
							holdingSword = true;
						}
					}
					if(!(holdingSword || swingL)) g2D.drawImage(sprites_left.get(i).getImage(),x+=Vx,(int)(y+=Vy+=Ay),null);
					if((ID == 4 || ID == 16 || ID == 19) && health < maxHP) draw_health(g2D);
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
				//g2D.drawImage(sprites.get(i).getImage(),x,y,null);
//				if(ID == 13 ){
//					g2D.setColor(Color.green);
//					g2D.draw(hitBox_bot());
//					g2D.draw(hitBox_left());
//					g2D.draw(hitBox_right());
//					g2D.draw(hitBox_top());
//				}
//			if(ID == 0){
//				g2D.setColor(Color.blue);
//				g2D.drawString(x + " " + y, 200,200);
//			}
				
//				if(ID ==4){
//				g2D.setColor(Color.red);
//				g2D.draw(hitBox_bot());
//				g2D.draw(hitBox_left());
//				g2D.draw(hitBox_right());
//				g2D.draw(hitBox_top());
//				g2D.draw(hitBox());
//			}
//		
//			if(ID == 9) g2D.draw(hitBox());
			
	}
	


	
}
	

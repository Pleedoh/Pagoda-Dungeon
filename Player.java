

import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Player extends Entity {
	
	public Player(int x, int y,int w,int h,int T) {
		//t determines the animation threshold, 
		//how many ticks it goes with one sprite
		super(x, y,w,h,T);
		sprites_right.add(loadImage("/stuff/player_right1.png"));
		sprites_left.add(loadImage("/stuff/player_left1.png"));
		sprites_right.add(loadImage("/stuff/player_right2.png"));
		sprites_left.add(loadImage("/stuff/player_left2.png"));
		
		naked_right.add(loadImage("/stuff/player_naked_right1.png"));
		naked_right.add(loadImage("/stuff/player_naked_right2.png"));
		naked_left.add(loadImage("/stuff/player_naked_left1.png"));
		naked_left.add(loadImage("/stuff/player_naked_left2.png"));
		
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
		this.ID = 0;
		ammo = 3;
		ammo_max = 3;
		c = new Color(63,71,204);		
		maxHP = 255;
	}
	//other player specific stuff goes here
	public void setAmmoMax(int x){
		ammo_max = x;
	}
	@Override
	public void stop(){
		left = false;
		right = false;
		Vx = 0;
		Vy = 0;
	}
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
		lives--;
		health = 255;
		x = Animation_Handler.spawns.get(level).x;
		y = Animation_Handler.spawns.get(level).y;
	}
	public void addTeleBombs(int b){
		tele_bombs += b;
	}
	public void addBombs(int b){
		bombs += b;
	}
	public boolean hasItems(){
		return tele_bombs > 0 || bombs > 0 || hasSword;
	}
	public int getBombs(){
		return bombs;
	}
	public int getTeleBombs(){
		return tele_bombs;
	}
	public void setColor(Color c){
		this.c = c;
	}
	public Color getColor(){
		return c;
	}

}

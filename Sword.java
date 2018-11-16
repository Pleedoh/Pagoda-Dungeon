

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Sword extends Item {

	Sword(int x, int y, int w, int h, int T,int d) {
		super(x, y, w, h, T);
		sprites.add(loadImage("/stuff/katana.png"));
		ID = 20;
		health = d;
		maxHP = 255;
	}
	Sword(int x, int y, int w, int h, int T,int d,int MHP) {
		super(x, y, w, h, T);
		sprites.add(loadImage("/stuff/katana.png"));
		ID = 20;
		health = d;
		maxHP = MHP;
	}
	Sword(int x, int y, int w, int h, int T,int Vx,int Vy,int d) {
		super(x, y, w, h, T,Vx,Vy);
		sprites.add(loadImage("/stuff/katana.png"));
		ID = 20;
		health = d;
		maxHP = 255;
	}
	Sword(int x, int y, int w, int h, int T,int Vx,int Vy,int d,int MHP) {
		super(x, y, w, h, T,Vx,Vy);
		sprites.add(loadImage("/stuff/katana.png"));
		ID = 20;
		health = d;
		maxHP = MHP;
	}
	@Override
	public Rectangle hitBox_top(){
		return new Rectangle(x+width/4,y-10,width/2,10);
	}
	@Override
	public Rectangle hitBox_right(){
		return new Rectangle(x+width-10,y+5,5,height-10);
	}
	@Override
	public Rectangle hitBox_left(){
		return new Rectangle(x+5,y+5,5,height-10);
	}
	@Override
	public Rectangle hitBox(){
		return new Rectangle(x+20, y+10, width-40, height-10);
	}
	@Override
	public Rectangle hitBox_bot(){
		return new Rectangle(x+width/2 ,y+height-10,width/2,height/5+5);
	}
	public void draw(Graphics2D g2D){

		if(exists){
//			g2D.setColor(Color.green);
//			g2D.draw(hitBox_bot());
//			g2D.draw(hitBox_left());
//			g2D.draw(hitBox_right());
//			g2D.setColor(Color.pink);
//			g2D.draw(hitBox_top());
			update_movement();
			if(t == T){
				i++;
				t = 0;
			}
			else t++;
			if(i >= sprites.size()) i = 0;
			g2D.drawImage(sprites.get(i).getImage(),x+=Vx,y+=Vy+=Ay,null);
		}
	}
}

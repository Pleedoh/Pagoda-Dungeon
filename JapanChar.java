

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;


public class JapanChar {
	double dir = Math.random();
	Color greenish = new Color(128,240,119);
	Color bluish = new Color(66,139,202);
	Color redish = new Color(130, 29, 24);
	double angle,speed = Math.random()*0.04+0.01;
	int px,py;//orbit point
	int x,y,r;
	Font font;
	String letter;
	JapanChar(int x,int y,Font font,String letter,int r){
		px = x;
		py = y;
		this.font = font;
		this.letter = letter;
		this.r = r;
		if(dir > 0.5) speed*=-1;
	}
	void move(){
		x = (int) (r*Math.cos(angle));
		y = (int) (r*Math.sin(angle));
		angle+=speed;
	}
	void draw(Graphics2D g2D){
		g2D.setColor(bluish);
		g2D.setFont(font);
		g2D.drawString(letter, px+x, py+y);
	}
}



import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Slider {
	int x,y,width;
	int pointX;
	boolean active;
	Color c;
	public Slider(int x,int y,int width,int startValue,Color c){
		this.x = x;
		this.y = y;
		this.width = width;
		pointX = x + startValue*4;
		this.c = c;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getValue(){
		return (pointX - x)/4;
	}
	public void draw(Graphics2D g2D){
		g2D.setColor(c);
		g2D.setStroke(new BasicStroke(10));
		g2D.drawLine(x, y, x+width*2,y);
		g2D.setStroke(new BasicStroke(1));
		g2D.fillRect(pointX,y-25,50,50);
//		g2D.setColor(Color.cyan);
//		g2D.drawString((pointX-x)/4+"", 200,y/2);
	}
	public boolean mouse_on(Point mp) {
		return mp.x >= pointX && mp.y >= y-25 && mp.x <= pointX + 50 && mp.y <= y-25 + 50;
	}
	//set the point of the slider tick thingy
	public void setPoint(int nx){
		//System.out.println(x + " " + nx + " " + (x+width));
		if(nx < x) nx = x;
		if(nx > x+width*2) nx = x+width*2;
		pointX = nx;
	}
	public void activate(){
		active = true;
	}
	public void deActivate(){
		active = false;
	}
	public boolean isActive(){
		return active;
	}
}

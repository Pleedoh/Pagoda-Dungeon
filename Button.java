 

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

//a rectangular button
//default stroke is 10
public class Button {
	int x,y,width,height;
	Color colour,selected = new Color(66,139,202);
	int screen_width = Main.screenSize.width;
	int screen_height = Main.screenSize.height;
	String text;
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	Button(int x,int y,int width,int height,Color colour){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.colour = colour;
	}
	Button(int x,int y,int width,int height,Color colour,Color selected){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.colour = colour;
		this.selected = selected;
	}
	void setText(String text){
		this.text = text;
	}
	void setColor(Color c){
		colour = c;
	}
	void setSelect(Color c){
		selected = c;
	}
	void draw_button(Graphics2D g2D,boolean cursor_on,FontMetrics fm){
		g2D.setFont(Main.font);
		int string_width = fm.stringWidth(text);
		g2D.setColor(cursor_on ? selected : colour);
		g2D.fillRect(x,y,width,height);
		g2D.setColor(Color.black);
		g2D.setStroke(new BasicStroke(10));
		g2D.drawRect(x,y,width,height);
		g2D.drawString(text,x+width/2 - string_width/2,y+height/2+10);
	}
	//little text
	void draw_button_sml(Graphics2D g2D,boolean cursor_on,FontMetrics fm){
		g2D.setFont(Main.sml_font);
		int string_width = fm.stringWidth(text);
		g2D.setColor(cursor_on ? selected : colour);
		g2D.fillRect(x,y,width,height);
		g2D.setColor(Color.black);
		g2D.setStroke(new BasicStroke(10));
		g2D.drawRect(x,y,width,height);
		g2D.drawString(text,x+width/2 - string_width/2,y+height/2+10);
	}
	//just for death screen
	void draw_button(Graphics2D g2D,boolean cursor_on,FontMetrics fm,Color cc){
		g2D.setFont(Main.font);
		int string_width = fm.stringWidth(text);
		g2D.setColor(cursor_on ? selected : colour);
		g2D.fillRect(x,y,width,height);
		g2D.setColor(Color.black);
		g2D.setStroke(new BasicStroke(10));
		g2D.drawRect(x,y,width,height);
		g2D.setColor(cc);
		g2D.drawString(text,x+width/2 - string_width/2,y+height/2+10);
	}
	public boolean mouse_in(Point mp) {
		// mp = mouse postion
		// b = button obj
		return mp.x >= this.x && mp.y >= this.y && mp.x <= this.x + this.width && mp.y <= this.y + this.height;
	}
}

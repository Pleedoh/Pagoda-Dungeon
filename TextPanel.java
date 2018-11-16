

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class TextPanel {
	String text;
	int x,y,width,height;
	Color c;
	boolean once;
	Color greenish = new Color(128,240,119);
	Font f;
	FontMetrics fm;
	ImageIcon scroll = loadImage("/stuff/scroll.png");
	TextPanel(int x,int y,int w,int h,Color c,Font f){
		this.x = x;
		this.y = y;
		this.height = h;
		this.width = w;
		this.c = c;
		this.f = f;
		
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
	public void setText(String text){
		this.text = text;
		once = true;
	}
	public void drawText(Graphics g2D){
		String[] lines = text.split("<<");
		//g2D.setColor(new Color(26,55,60));
		g2D.setColor(Color.black);
		for(int i = 0; i < lines.length;i++){
			g2D.drawString(lines[i],550,330+fm.getHeight()*(i)*2);
		}
		
	}
	public void display_panel(Graphics2D g2D){
		g2D.setFont(f);
		fm = g2D.getFontMetrics(f);
	if(once){
			g2D.setColor(new Color(51,51,51,165));
			g2D.fillRect(0, 0, Main.F.getWidth(), Main.F.getHeight());
			once = false;
		}
		g2D.setColor(c);
		g2D.drawImage(scroll.getImage(),0,0,1920,1080,null);
		drawText(g2D);
		g2D.setFont(Main.font);
		fm =  g2D.getFontMetrics();
		g2D.drawString("Press any key to continue.",Main.F.getWidth()/2-fm.stringWidth("Press any key to continue.")/2,1000);
	}
}

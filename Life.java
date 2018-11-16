

import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Life extends Item {

	Life(int x, int y, int w, int h, int T) {
		super(x, y, w, h, T);
		sprites.add(loadImage("/stuff/hp_1.png"));
		sprites.add(loadImage("/stuff/hp_2.png"));
		ID = 12;
	}
	Life(int x, int y, int w,int h,int T,int Vx,int Vy) {
		super(x, y, w, h, T,Vx,Vy);
		sprites.add(loadImage("/stuff/hp_1.png"));
		sprites.add(loadImage("/stuff/hp_2.png"));
		ID = 12;
	}
	@Override
	public void draw(Graphics2D g2D){
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

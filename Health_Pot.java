

import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Health_Pot extends Item {

	Health_Pot(int x, int y, int w, int h, int T) {
		super(x, y, w, h, T);
		sprites.add(loadImage("/stuff/heal_1.png"));
		sprites.add(loadImage("/stuff/heal_2.png"));
		ID = 11;
	}
	Health_Pot(int x, int y, int w,int h,int T,int Vx,int Vy) {
		super(x, y, w, h, T,Vx,Vy);
		sprites.add(loadImage("/stuff/heal_1.png"));
		sprites.add(loadImage("/stuff/heal_2.png"));
		ID = 11;
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

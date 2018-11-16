

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Bomb_Item extends Item{
	Bomb_Item(int x, int y, int w, int h, int T) {
		super(x, y, w, h, T);
		sprites.add(loadImage("/stuff/bomb_1.png"));
		ID = 17;
	}
	Bomb_Item(int x, int y, int w,int h,int T,int Vx,int Vy) {
		super(x, y, w, h, T,Vx,Vy);
		sprites.add(loadImage("/stuff/bomb_1.png"));
		ID = 17;
	}

	@Override
	public void draw(Graphics2D g2D){
		
		if(exists){
			update_movement();
			if(t == T){
				i++;
				t = 0;
			}
			else t++;
			if(i >= sprites.size()) i = 0;
			g2D.drawImage(sprites.get(i).getImage(),x+=Vx,y+=Vy+=Ay,null);
		}
		
//		g2D.setColor(Color.red);
//		g2D.draw(hitBox_bot());
//		g2D.draw(hitBox_left());
//		g2D.draw(hitBox_right());
//		g2D.draw(hitBox_top());
//		g2D.draw(hitBox());
	}
}

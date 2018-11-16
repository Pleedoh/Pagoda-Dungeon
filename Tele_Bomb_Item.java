

import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Tele_Bomb_Item extends Item {

	Tele_Bomb_Item(int x, int y, int w, int h, int T) {
		super(x, y, w, h, T);
		sprites.add(loadImage("/stuff/tbomb_1.png"));
		ID = 18;
		state = 2;
	}
	Tele_Bomb_Item(int x, int y, int w, int h, int T,int Vx,int Vy) {
		super(x, y, w, h, T,Vx,Vy);
		sprites.add(loadImage("/stuff/tbomb_1.png"));
		ID = 18;
		state = 2;
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
	}

}

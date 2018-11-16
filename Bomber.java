

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;

public class Bomber extends Enemy_Ninja{

	public Bomber(int x, int y, int w, int h, int T, int state, int frequency, int x_comp, int y_comp, boolean patrols,
			int span, int speed) {
		super(x, y, w, h, T, state, frequency, x_comp, y_comp, patrols, span, speed);
		ID = 16;
		maxHP = 200;
		health = 200;
	}
	@Override
	public void addSprites(){
		sprites_right.add(loadImage("/stuff/bomber_right_1.png"));
		sprites_left.add(loadImage("/stuff/bomber_left_1.png"));
		sprites_right.add(loadImage("/stuff/bomber_right_2.png"));
		sprites_left.add(loadImage("/stuff/bomber_left_2.png"));
		death.add(loadImage("/stuff/death_1.png"));
		death.add(loadImage("/stuff/death_2.png"));
		death.add(loadImage("/stuff/death_3.png"));
	}
	@Override
	public void start(){
		shoot_timer = new Timer(this.frequency,new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//only shoot if gameplay, this is to prevent weird stuff during loading
				if(Main.screen_state == 1){
					if(state == 0){//lookin right;
						shoot(new Bomba(x+width-60,y+10,50,50,1,x_comp,-y_comp,false));
					}
					//lookin left
					else shoot(new Bomba(x+10,y+20,50,50,1,-x_comp,-y_comp,false));
				}
			}
		});
		shoot_timer.start();
	}

}

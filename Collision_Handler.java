

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Timer;

import org.omg.CORBA.TRANSACTION_MODE;

public class Collision_Handler extends Animation_Handler {
	// has access to the lists of objects
	/*
	 * responsible for checking collisions and setting stopped values which the
	 * entity class deals with when providing cords to draw
	 */
	// o_ denotes object collision
	
	/* player health and lives in animation handler parent
	 * just for the transition
	 * then the Main version takes over
	 */
	Font f,inv_font;
	Collision_Handler(ArrayList<Item> inventory){
		this.inventory = inventory;
	}
	public void setFont(Font f){
		this.f = f;
	}
	public void addInvFont(Font f){
		inv_font = f;
	}
	public ArrayList<Item> inventory = new ArrayList<Item>();
	Timer fade_ticker;
	ImageIcon heart = loadImage("/stuff/heart.png");
	ImageIcon star = loadImage("/stuff/star_1.png");
	ImageIcon inv_katana = loadImage("/stuff/inv_katana.png");
	boolean playerSwingCD;// used to time sword damage, swing damage cooldown
	public void o_check_left(Entity e) {
		if(e.ID == 21) return;
		for (int i = 0; i < walls.get(level).size(); i++) {
			Wall w = walls.get(level).get(i);
	
			if (e.hitBox_left().intersects(w.hitBox()) && w.ID != 8) {
				if (e.ID == 4 || e.ID == 19) {
					e.turn();
				}
				e.setX(w.getX() + w.width);
				return;
			}
		}

	}

	public void o_check_right(Entity e) {
		if(e.ID == 21) return;
		for (int i = 0; i < walls.get(level).size(); i++) {
			Wall w = walls.get(level).get(i);
			//ignore lava
			if (e.hitBox_right().intersects(w.hitBox()) && w.ID != 8) {
				if (w.ID == 7 && e.ID == 0) {//player hit the door
					e.stop();
					playDoorSound();
					fade(false);//non death fade
					transition_enemies();
					Main.screen_state = -1;
					Animation_Handler.setLevel(level+1);
					Animation_Handler.add(e,level);
					e.setX(spawns.get(level).x);
					e.setY(spawns.get(level).y);
					if(Main.tuts) update_panel_text(level);
					return;
				}
				if (e.ID == 4 || e.ID == 19) {
					e.turn();
					return;
				}
				e.setX(w.getX() - e.width);


				return;
			}

		}
	}

	public void o_check_top(Entity e) {
		if(e.ID == 21) return;
		for (int i = 0; i < walls.get(level).size(); i++) {
			Wall w = walls.get(level).get(i);
			if (e.hitBox_top().intersects(w.hitBox()) && w.ID != 8) {
				e.setVy(0);
				e.setY(w.getY() + w.getHeight());
				return;
			}
		}
	}
	public void o_check_bot(Entity e) {
		for (int i = 0; i < walls.get(level).size(); i++) {
			Wall w = walls.get(level).get(i);
			//skip lava
			if (e.hitBox_bot().intersects(w.hitBox()) && w.ID != 8) {
				e.setVy((int) w.getVy());
				if (w.ID == 5) {
					// e.moving = true;
					e.setDefVx(w.getVx());
				} else
					e.setDefVx(0);
				if (!e.grounded)
					e.setY(w.getY() - e.getHeight());
				e.grounded = true;
				return;
			}

		}

		e.grounded = false;
	}
	public void i_check_left(Item e) {
		for (int i = 0; i < walls.get(level).size(); i++) {
			Wall w = walls.get(level).get(i);
			if (e.hitBox_left().intersects(w.hitBox()) && w.ID != 8) {
				if (e.ID == 4) {
					e.turn();
				}
				e.setX(w.getX() + w.width);
				return;
			}
		}

	}

	public void i_check_right(Item e) {
		for (int i = 0; i < walls.get(level).size(); i++) {
			Wall w = walls.get(level).get(i);
			//ignore lava
			if (e.hitBox_right().intersects(w.hitBox()) && w.ID != 8) {
				e.setX(w.getX() - e.width);
				return;
			}

		}
	}

	public void i_check_top(Item e) {

		for (int i = 0; i < walls.get(level).size(); i++) {
			Wall w = walls.get(level).get(i);
			if(w.ID == 4) continue;
			if (e.hitBox_top().intersects(w.hitBox()) && w.ID != 8) {
				e.setVy(0);
				e.setY(w.getY() + w.getHeight());
				return;
			}
		}
	}
	public void i_check_bot(Item e) {
		for (int i = 0; i < walls.get(level).size(); i++) {
			Wall w = walls.get(level).get(i);
			//skip lava
			if (e.hitBox_bot().intersects(w.hitBox()) && w.ID != 8) {
				e.setVy((int) w.getVy());
				if (w.ID == 5) {
					// e.moving = true;
					e.setDefVx(w.getVx());
				} else
					e.setDefVx(0);
				if (!e.grounded)
					e.setY(w.getY() - e.getHeight());
				e.grounded = true;
				return;
			}

		}

		e.grounded = false;
	}
	/* PROJECTILES */
	public boolean p_check(Projectile p) {
		if(p.getState() == -3) return false;
		for (int i = 0; i < walls.get(level).size(); i++) {
			Wall w = walls.get(level).get(i);
			if (p.hitBox().intersects(w.hitBox())) {
				if(p.ID == 9){
					p.setState(-3);
					return false;
				}
				return true;
			}
		}
		for (int j = 0; j < objects.get(level).size(); j++) {
			Entity e = objects.get(level).get(j);
			if (p.hitBox().intersects(e.hitBox())){
				if (e.ID == 0) {//e = player
					if (!p.friendly) {
						if(p.ID == 3){
							if(Main.diff ==0||Main.diff==2)e.damage(20);
							if(Main.diff ==1)e.damage(30);
							playPlayerHurt();
							checkDeath(e);
							return true;
						}
						if(p.ID == 9){
							e.damage(50);
							playPlayerHurt();
							checkDeath(e);
							p.setState(-3);
							return false;
						}
						if(p.ID == 14){
							return true;
						}
					}
				}
				if ((e.ID == 4 || e.ID == 15 || e.ID == 16 || e.ID == 19 || e.ID == 21) && e.getState() != -3) {//hit an enemy
					if (p.friendly) {
						if(e.ID == 21){//BOSS
							if(Main.diff == 0) e.damage(125);
							if(Main.diff == 1) e.damage(100);
							if(Main.diff == 2) e.damage(70);
							if(e.health > 0) playEnemyHurt();
							checkEnemyDeath(e);
							return true;
						}
						else{
							if(Main.diff == 0) e.damage(75);
							if(Main.diff == 1) e.damage(50);
							if(Main.diff == 2) e.damage(30);
							if(e.health > 0) playEnemyHurt();
							checkEnemyDeath(e);
							return true;
						}
					}

				}
				

			}
	
		}

		return false;
	}

	public void check_all() {
		if(!(Main.player.isSwingingLeft() || Main.player.isSwingingRight())) playerSwingCD = true;
		
		for (int j = 0; j < objects.get(level).size(); j++) {
			Entity e = objects.get(level).get(j);
			if(e.ID == 15){//if a bat
				if(e.hitBox().intersects(Main.player.hitBox()) && e.getState() != -3){
					playEnemyDeath();
					if(Main.diff ==0 || Main.diff ==2)Main.player.damage(20);
					if(Main.diff ==1)Main.player.damage(50);
					checkDeath(Main.player);
					e.setState(-3);// initiate death sequence
					e.setVx(0);
					e.setVy(0);
				}
				if(Main.player.isSwingingLeft() && playerSwingCD){
					if(Main.player.attackLeft().intersects(e.hitBox())&& e.getState() != -3){
						for(int i = 0; i < inventory.size();i++){
							Item it = inventory.get(i);
							if(it.ID == 20){
								it.damage(17);
								if(it.getHealth() == 0){
									Main.player.dropSword();
								}
								
							}
						}
							playerSwingCD = false;
							playEnemyHurt();
							e.damage(80);
							checkEnemyDeath(e);
					}
				}
				if(Main.player.isSwingingRight()&& playerSwingCD){
					if(Main.player.attackRight().intersects(e.hitBox())&& e.getState() != -3){
						for(int i = 0; i < inventory.size();i++){
							Item it = inventory.get(i);
							if(it.ID == 20){
								it.damage(17);
								if(it.getHealth() == 0){
									Main.player.dropSword();
								}
								
							}
						}
						playerSwingCD = false;
						playEnemyHurt();
						e.damage(80);
						checkEnemyDeath(e);
					}
				}
				continue;
			}
			if(e.ID == 10) continue;//don't do collisions for the cannons
			
			if(e.ID == 19 && e.getState() != -3){
				if(e.swingCD < 21) e.swingCD++;
				
				//if in the middle
					if(e.attackLeft().intersects(Main.player.hitBox())){
						if(!e.isSwingingLeft() && e.swingCD > 20){
							e.swingLeft();
							e.swingCD = 0;
						}
						else if(e.getSwingFrame() == 3){
							playPlayerHurt();
							if(Main.diff > 0) Main.player.damage(20);
							else Main.player.damage(10);
							checkDeath(Main.player);
						}
					}
					if(e.attackRight().intersects(Main.player.hitBox())){
						if(!e.isSwingingRight()&& e.swingCD > 20){
							e.swingRight();
							e.swingCD = 0;
						}
						else if(e.getSwingFrame() == 3){
							playPlayerHurt();
							if(Main.diff > 0) Main.player.damage(20);
							else Main.player.damage(10);
							checkDeath(Main.player);
						}
					}
			}
			
			if(e.ID == 4 || e.ID == 15 || e.ID==16 || e.ID == 19 || e.ID == 21){
				if(Main.player.isSwingingLeft() && playerSwingCD){
					if(Main.player.attackLeft().intersects(e.hitBox())&& e.getState() != -3){
						for(int i = 0; i < inventory.size();i++){
							Item it = inventory.get(i);
							if(it.ID == 20){
								it.damage(17);
								if(it.getHealth() == 0){
									Main.player.dropSword();
								}
								
							}
							
						}
						playerSwingCD = false;
							playEnemyHurt();
							if(Main.diff == 0) e.damage(200);
							if(Main.diff == 1) e.damage(130);
							if(Main.diff == 2) e.damage(100);
							checkEnemyDeath(e);
					}
				}
				if(Main.player.isSwingingRight()&& playerSwingCD){
					if(Main.player.attackRight().intersects(e.hitBox())&& e.getState() != -3){
						for(int i = 0; i < inventory.size();i++){
							Item it = inventory.get(i);
							if(it.ID == 20){
								it.damage(17);
								if(it.getHealth() == 0){
									Main.player.dropSword();
								}
								
							}
						}
						playerSwingCD = false;
						playEnemyHurt();
						if(Main.diff == 0) e.damage(200);
						if(Main.diff == 1) e.damage(130);
						if(Main.diff == 2) e.damage(100);
						checkEnemyDeath(e);
					}
				}
			}
			o_check_top(e);
			o_check_bot(e);
			o_check_right(e);
			o_check_left(e);
			check_traps(e);
		}
		
		for (int j = 0; j < projectiles.get(level).size(); j++) {
			Projectile p = projectiles.get(level).get(j);
			if(p.ID == 13 && p.getState() != -3){
				check_explode(p);
			}
			else if (p_check(p)) {
				if(p.ID == 14) {
					playExplosionSound();
					p.stop();
					p.setState(-3);
					p.setX(p.getX()-50);
					p.setY(p.getY()-100);
					if(p.friendly){
						for (int k = 0; k < objects.get(level).size(); k++) {
							Entity b = objects.get(level).get(k);// b is the enemy
								if(b.ID == 15 || b.ID == 4 || b.ID == 16 || b.ID == 19 || b.ID == 21){
									if(p.BIG_SHAQ().intersects(b.hitBox())){
										if(Main.diff == 0) b.damage(250);
										if(Main.diff == 1) b.damage(150);
										if(Main.diff == 2) b.damage(100);
										if(b.health > 0) playEnemyHurt();
										checkEnemyDeath(b);
									}
								}
							}
					}
					else{
						if(p.BIG_SHAQ().intersects(Main.player.hitBox())){
							if(Main.diff == 0)   Main.player.damage(60);
							if(Main.diff == 1 || Main.diff == 2)Main.player.damage(120);
							playPlayerHurt();
							checkDeath(Main.player);
						}
					}
				}
				else if(projectiles.get(level).size()>0) projectiles.get(level).remove(j);
			}
			
		}
		
		for(int i = 0;i< items.get(level).size();i++){

			Item item = items.get(level).get(i);
			i_check_bot(item);
			i_check_top(item);
			i_check_left(item);
			i_check_right(item);
			if(Main.player.hitBox().intersects(item.hitBox())){
				if(item.ID == 11){
					Main.player.heal(100);
					items.get(level).remove(i);
					playPowerSound();
				}
				if(item.ID == 12){
					Main.player.lives++;
					items.get(level).remove(i);
					playPowerSound();
				}
				
				if(item.ID == 17 && item.exists){
					boolean there = false;
					playBombPickup();
					item.offMap();
					Main.player.addBombs(1);
					for(int it = 0; it < inventory.size();it++){//if the item is not in your inventory, add the sprite basically
							if(item.ID == inventory.get(it).ID){
								there = true;
							}
					}
					if(!there) inventory.add(item);
				}
				if(item.ID == 18 && item.exists){
					boolean there  = false;
					playBombPickup();
					item.offMap();
					Main.player.addTeleBombs(1);
					for(int it = 0; it < inventory.size();it++){
							if(item.ID == inventory.get(it).ID){
								there = true;
							}
					}
					if(!there) inventory.add(item);
				}
				if(item.ID == 20 && item.exists){
					boolean there  = false;
					playBombPickup();
					item.offMap();
					for(int it = 0; it < inventory.size();it++){
							if(item.ID == inventory.get(it).ID){
								there = true;
								if(item.maxHP > inventory.get(it).maxHP){
									inventory.set(it, item);
								}
								inventory.get(it).heal(item.getHealth());
							}
					}
					if(!there) inventory.add(item);
					if(inventory.get(Main.item_select).ID == 20){
						Main.player.putSword(true);//if holding sword
					}
					Main.player.hasSword = true;
				}
			}
		}
		
	}
	//for bombas
	void check_explode(Projectile p){
		for (int i = 0; i < walls.get(level).size(); i++) {
			Wall w = walls.get(level).get(i);

			if (p.hitBox_right().intersects(w.hitBox()) && !p.is_Stopped()) {
				Animation_Handler.add(new Tele_Bomb(Main.player.x,Main.player.y-50,50,50,10,false), level);
				Main.player.setX(p.getX() - 50);
				Main.player.setY(p.getY() - 50);
				Main.player.setVy(0);
				p.stop();
				p.setX(p.getX() - 50);
				p.setY(p.getY() - 100);
				p.setState(-3);
			}
			if (p.hitBox_left().intersects(w.hitBox()) && !p.is_Stopped()) {
				Animation_Handler.add(new Tele_Bomb(Main.player.x,Main.player.y-50,50,50,10,false), level);
				Main.player.setX(p.getX() + 10);
				Main.player.setY(p.getY() - 50);
				Main.player.setVy(0);
				p.stop();
				p.setX(p.getX() - 50);
				p.setY(p.getY() - 100);
				p.setState(-3);
			}
			if (p.hitBox_top().intersects(w.hitBox()) && !p.is_Stopped()) {
				Animation_Handler.add(new Tele_Bomb(Main.player.x,Main.player.y-50,50,50,10,false), level);
				Main.player.setX(p.getX());
				Main.player.setY(p.getY());
				Main.player.setVy(0);
				p.stop();
				p.setX(p.getX() - 50);
				p.setY(p.getY() - 100);
				p.setState(-3);
			}
			if(w.ID == 6 && p.hitBox_bot().intersects(w.hitBox()) && !p.is_Stopped() ){
				Animation_Handler.add(new Tele_Bomb(Main.player.x,Main.player.y-50,50,50,10,false),level);
				Main.player.setX(p.getX()-30);
				Main.player.setY(p.getY() - 130);
				p.stop();
				p.setX(p.getX() - 50);
				p.setY(p.getY() - 100);
				p.setState(-3);
			}
			else if (p.hitBox_bot().intersects(w.hitBox()) && !p.is_Stopped()) {
				Animation_Handler.add(new Tele_Bomb(Main.player.x,Main.player.y-50,50,50,10,false),level);
				Main.player.setX(p.getX());
				Main.player.setY(p.getY() - 100);
				p.stop();
				p.setX(p.getX() - 50);
				p.setY(p.getY() - 100);
				p.setState(-3);
			}
		}
	}
	void check_traps(Entity e){
		for(int i = 0; i < traps.get(level).size();i++){
			Trap t = traps.get(level).get(i);
			if(e.hitBox().intersects(t.hitBox())){
				switch(t.ID){
				case 8:
					if(e.ID == 0){
						if(Main.diff ==0)e.damage(1);
						if(Main.diff ==1|| Main.diff == 2)e.damage(3);
						checkDeath(e);
					}
					if(e.ID == 4 || e.ID == 15 || e.ID == 16 || e.ID == 19){
						e.damage(3);
						checkEnemyDeath(e);
					}
					
				break;
				}
			}
	
		}
	}
	//mimics what happens in Main to allow transitions between levels
	void fade_from_black(boolean death,int x) {
		/*
		 * this is needed to reset the frame and draw a slightly lighter color
		 * each time
		 */
		Main.bI = new BufferedImage(Main.screenSize.width, Main.screenSize.height, BufferedImage.TYPE_INT_ARGB);
		Main.g = Main.bI.getGraphics();
		Main.g2D = (Graphics2D) Main.g;
		check_all();
		animate_all(Main.g2D);
		draw_health(Main.g2D);
		draw_hearts(Main.g2D);
		draw_ammo(Main.g2D);
		draw_inventory(Main.g2D);
		Main.g2D.setColor( death ? new Color(255, 0, 0, 255 - x) : new Color(0, 0, 0, 255 - x));
		Main.g2D.fillRect(0, 0, Main.screenSize.width, Main.screenSize.width);
		Main.start_icon = new ImageIcon(Main.bI);
		Main.start_label.setIcon(Main.start_icon);
		Main.F.add(Main.start_label);// this being the frame
		Main.F.setVisible(true);
	}

	void fade_to_black(boolean death,int x) {
		Main.g2D.setColor( death ? new Color(255,0,0,x) : new Color(0, 0, 0,x));
		Main.g2D.fillRect(0, 0, Main.screenSize.width,Main.screenSize.height);
		Main.start_icon = new ImageIcon(Main.bI);
		Main.start_label.setIcon(Main.start_icon);
		Main.F.add(Main.start_label);// this being the frame
		Main.F.setVisible(true);
	}

	void fade(boolean death) {
		Main.screen_state = -1;
		fade_ticker = new Timer(20, new ActionListener() {
			int x = 0;
			boolean black = true;

			public void actionPerformed(ActionEvent e) {
				if (black) {
					if (x > 250) {
						black = false;
						x = 0;
					} else
						fade_to_black(death,x += 5);
				} 
				else if(Main.player.lives > 0){
					if (x > 250) {
						fade_ticker.stop();
					if(Main.tuts && !death){
						Main.screen_state = 5; 
					}
					else{
						for(Entity g : objects.get(level)){
							if(g.ID == 15 || g.ID == 4 || g.ID == 16 || g.ID == 19){
								g.reset_move();
							}
						}
						Main.screen_state = 1;
					}
					} 
					else {
						fade_from_black(death,x += 5);
					}
				}
				else{
					Main.screen_state = -2;
					reset();
					fade_ticker.stop();
				}
			}
		});
		fade_ticker.start();
	}
	void transition_enemies(){
		//stop the enemies from previous level
		//start the enemies of the next level
		
		for(int i = 0;i< objects.get(level).size();i++){
			Entity e = objects.get(level).get(i);
			if(e.ID == 4 || e.ID == 10 || e.ID == 16){
				e.stop();
			}
		}
		for(int i = 0;i< objects.get(level+1).size();i++){
			Entity e = objects.get(level+1).get(i);
			if(e.ID == 4 || e.ID == 10 || e.ID == 16){
				e.start();
			}
		}
	}
	void draw_hearts(Graphics2D g2D){
		for(int i = 0; i < Main.player.lives;i++){
			g2D.drawImage(heart.getImage(),330+50*i,50,null);
		}
	}
	void draw_ammo(Graphics2D g2D){
		for(int i = 0; i < Main.player.ammo;i++){
			g2D.drawImage(star.getImage(),50+50*i,120,null);
		}
	}
	void draw_inventory(Graphics2D g2D){
		for(int i = 0; i < inventory.size();i++){
				Item it = inventory.get(i);
				g2D.setColor(Color.gray);
				g2D.setStroke(new BasicStroke(5));
				g2D.drawRect(40+70*(Main.item_select), 170, 70, 70);
				g2D.setStroke(new BasicStroke(1));
				g2D.setColor(Color.white);
				g2D.setFont(inv_font);
				if(it.ID == 17){//bomb item
					if(Main.player.getBombs() == 0){
						if(Main.item_select == inventory.size()-1) Main.item_select--;
						inventory.remove(i);
						if(Main.item_select < 0) Main.item_select = 0;
					}
					else{
						g2D.drawImage(it.sprites.get(0).getImage(), 50+70*i, 170,null);
						g2D.drawString(""+Main.player.bombs,80+70*i,230);
					}
				}
				if(it.ID == 18){//tele bomb item
					if(Main.player.getTeleBombs() == 0){
						if(Main.item_select == inventory.size()-1) Main.item_select--;
						inventory.remove(i);
						if(Main.item_select < 0) Main.item_select = 0;
					}
					else{
						g2D.drawImage(it.sprites.get(0).getImage(), 50+70*i, 170,null);
						g2D.drawString(""+Main.player.tele_bombs,80+70*i,230);
					}
				}
				if(it.ID == 20){
					if(!Main.player.hasSword()){
						if(Main.item_select == inventory.size()-1) Main.item_select--;
						inventory.remove(i);
						if(Main.item_select < 0) Main.item_select = 0;
					}
					else{
						g2D.drawImage(inv_katana.getImage(), 50+70*i, 180,null);
						it.draw_health(g2D,50+70*i,180);
					}
				}
		}
	}
	void update_panel_text(int lvl){
		switch(level){
		case 1:
		Main.info.setText("It seems some of the defenders"
				+ "<<have found the bombs as well."
				+ "<<There's also Lava."
				+ "<<Clearly they don't want me here."
				+ "<<The prize is worth it. It has to be."
				+ "<<The shinobi warriors are skilled and deadly."
				+ "<<I caution you against those armed with explosives.");
		Main.player.setAmmoMax(4);
		break;
		case 2:
			Main.info.setText("This is where your training counts."
					+ "<<Jump carefully, with precision."
					+ "<<You can easily lose it all."
					+ "<<The room also may be infested with kamikaze bats."
					+ "<<They seem to fly together."
					+ "<<Be quick, don't let them get close."
					+ "<<Remember the bombs?");
			Main.player.setAmmoMax(5);
		break;
		case 3:
			Main.info.setText("Climb with confidence." + "<<The clock is ticking...");
			Main.player.setAmmoMax(6);
		break;
		
		case 4:
			Main.info.setText("Almost there..."
					+ "<<They're everywhere...");	
			Main.player.setAmmoMax(7);
		break;
		case 5:
			Main.info.setText("Something doesn't feel right..."
					+ "<<I sense a dark presence..."
					+ "<<I am going to leave my stuff here "
					+ "<<while I check it out."
					+ "<<..."
					+ "<<It is probably nothing.");
			break;
		case 6:
			Main.info.setText("Oh no...");
			Main.player.setAmmoMax(8);
			break;
		}	
	}
	void checkDeath(Entity e){
		if(e.health <= 0){
			playPlayerDeath();
			for(Wall w : walls.get(level)){
				if(w.ID == 5 || w.ID == 6) w.reset_pos();
			}
			for(Entity g : objects.get(level)){
				if(g.getState() != -3){
					if(g.ID == 15 || g.ID == 4 || g.ID == 16 || g.ID == 19){
						g.setVx(0);
						g.setVy(0);
						g.reset_pos();
					}
				}
			}
			e.setVx(0);
			e.setVy(0);
			projectiles.get(level).clear();
			for(int i = 0; i < traps.get(level).size();i++){
				traps.get(level).get(i).reset();
			}
			e.setState(0);
			e.stop();
			e.die();
			fade(true);//death fade
		}
	}

	void checkEnemyDeath(Entity e){
		if(e.health <= 0 && e.state != -3){
			if(e.ID == 16){//bomber drops
				if(Math.random() > 0.5){
					if(Math.random() > 0.4){
						add(new Bomb_Item(e.getX(),e.getY(),50,50,50),level);
					}
				}
				else{
					if(Math.random() > 0.4){
						add(new Tele_Bomb_Item(e.getX(),e.getY(),50,50,50),level);
					}
				}
			}
			if(e.ID == 4){//red drops (everything)
				if(Math.random() > 0.3){
					if(Math.random() < 0.75 && Main.diff < 2){
						add(new Health_Pot(e.getX(),e.getY(),50,50,50),level);
					}
					else{
						if(Math.random() > 0.4){
							add(new Tele_Bomb_Item(e.getX(),e.getY(),50,50,50),level);
						}
					}
				}
				else{
					if(Math.random() < 0.1 && Main.diff < 2){
						add(new Life(e.getX(),e.getY(),50,50,50),level);
					}
					else if(Math.random() > 0.5){
						if(Math.random() > 0.4){
							add(new Bomb_Item(e.getX(),e.getY(),50,50,50),level);
						}
					}
				}


			}
			if(e.ID == 19){
				if(Math.random() < 0.6){
					add(new Sword(e.getX(),e.getY(),75,75,50,255),level);
				}
			}
			playEnemyDeath();
			e.setState(-3);// initiate death sequence
			e.setVx(0);
			e.stop();
		}
	}
	
}

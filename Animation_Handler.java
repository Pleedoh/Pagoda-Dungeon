

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Animation_Handler{

	/*these must be static so they are not dependent on the 
	 * instance and collision handler gets them too
	 */	
	
	AudioInputStream audioInputStream;
	Clip clip;
	
	public static ArrayList<Point> spawns = new ArrayList<Point>();
	static int level = 0;//level we're on;
	public static ArrayList<ArrayList<Entity>> objects = new ArrayList<ArrayList<Entity>>();
	public static  ArrayList<ArrayList<Wall>> walls = new ArrayList<ArrayList<Wall>>();
	public static ArrayList<ArrayList<Projectile>> projectiles = new ArrayList<ArrayList<Projectile>>();
	public static ArrayList<ArrayList<Item>> items = new ArrayList<ArrayList<Item>>();
	public static ArrayList<ArrayList<Trap>> traps = new ArrayList<ArrayList<Trap>>();
	static int screen_width = Main.screenSize.width;
	static int screen_height = Main.screenSize.height;
	static Camera camera = new Camera(0, 0);  
	
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
	public static void init(){
		//do this for each level, create empty arrays to work with
		//0
		objects.add(new ArrayList<Entity>());
		walls.add(new ArrayList<Wall>());
		projectiles.add(new ArrayList<Projectile>());
		items.add(new ArrayList<Item>());
		traps.add(new ArrayList<Trap>());
		//1
		objects.add(new ArrayList<Entity>());
		walls.add(new ArrayList<Wall>());
		projectiles.add(new ArrayList<Projectile>());
		items.add(new ArrayList<Item>());
		traps.add(new ArrayList<Trap>());
		//2
		objects.add(new ArrayList<Entity>());
		walls.add(new ArrayList<Wall>());
		projectiles.add(new ArrayList<Projectile>());
		items.add(new ArrayList<Item>());
		traps.add(new ArrayList<Trap>());
		//3
		objects.add(new ArrayList<Entity>());
		walls.add(new ArrayList<Wall>());
		projectiles.add(new ArrayList<Projectile>());
		items.add(new ArrayList<Item>());
		traps.add(new ArrayList<Trap>());
		//4
		objects.add(new ArrayList<Entity>());
		walls.add(new ArrayList<Wall>());
		projectiles.add(new ArrayList<Projectile>());
		items.add(new ArrayList<Item>());
		traps.add(new ArrayList<Trap>());
		//5
		objects.add(new ArrayList<Entity>());
		walls.add(new ArrayList<Wall>());
		projectiles.add(new ArrayList<Projectile>());
		items.add(new ArrayList<Item>());
		traps.add(new ArrayList<Trap>());
		//6
		objects.add(new ArrayList<Entity>());
		walls.add(new ArrayList<Wall>());
		projectiles.add(new ArrayList<Projectile>());
		items.add(new ArrayList<Item>());
		traps.add(new ArrayList<Trap>());
		
		spawns.add(new Point(100,780));
		spawns.add(new Point(50,955));
		spawns.add(new Point(60,280));
		spawns.add(new Point(115,1475));
		spawns.add(new Point(25,745));
		spawns.add(new Point(100,620));
		spawns.add(new Point(100,680));
	}
	public static void draw_background(Graphics2D g2D){
		// or whatever the background will consist of
		
		g2D.setColor(new Color(51,51,51));
		g2D.fillRect(-2000,-2000,screen_width+4000,screen_height+4000);
		for(int i = 0; i < walls.get(level).size();i++) {
			//skip foreground objects
			if( walls.get(level).get(i).ID==8 ) continue;
			walls.get(level).get(i).draw_wall(g2D);
		}
		
	}
	//ONLY draw foreground objects
	public static void draw_foreground(Graphics2D g2D){
//		g2D.setColor(Color.red);
//		g2D.drawString(camera.getX() + "," + camera.getY(), 1500, 500);
//		g2D.setColor(Color.blue);
//		g2D.drawString(Main.player.getX() + "," + Main.player.getY(), 1500, 400);
		for(int i = 0; i < traps.get(level).size();i++) {
			Trap t = traps.get(level).get(i);
			if(t.ID==8){
				g2D.translate(camera.getX(),camera.getY());
				t.draw(g2D);
				if(t.rising && Main.screen_state == 1){
					t.i++;
					if(t.i == 20){
						t.i = 0;
						t.setY(t.getY()-1);
						t.setHeight(t.getHeight()+1);
					}
				}
				g2D.translate(-camera.getX(),-camera.getY());
			}
		}
		
	}
	public static void draw_health(Graphics2D g2D){
		
		g2D.setColor(new Color((int)(255-Main.player.health),(int)(Main.player.health),0));
		g2D.fillRect(50, 50,(int)(Main.player.health), 50);
		g2D.setColor(new Color(25,25,25));
		g2D.setStroke(new BasicStroke(10));
		g2D.drawRect(50, 50, 255, 50);
		g2D.setStroke(new BasicStroke(1));

	}
	public static void animate_all(Graphics2D g2D){
		if(!Main.player.hasItems()) Main.player.putSword(false);
		//Run through all in game objects and display them
		/*The objects are responsible for their own draw method
		*indicating what shall be displayed*/
		g2D.translate(camera.getX(),camera.getY());
		draw_background(g2D);
		g2D.translate(-camera.getX(),-camera.getY());
		for(int i = 0; i < objects.get(level).size();i++){
			Entity e = objects.get(level).get(i);
			g2D.translate(camera.getX(),camera.getY());
			e.draw(g2D);
			g2D.translate(-camera.getX(),-camera.getY());
			if(e.ID == 0){
				camera.tick(e,level);
			}
		}
		for(int i = 0; i < projectiles.get(level).size();i++){
			g2D.translate(camera.getX(),camera.getY());
			projectiles.get(level).get(i).draw(g2D);
			g2D.translate(-camera.getX(),-camera.getY());
		}
		for(int i = 0;i< items.get(level).size();i++){
			g2D.translate(camera.getX(),camera.getY());
			items.get(level).get(i).draw(g2D);
			g2D.translate(-camera.getX(),-camera.getY());
		}
		draw_foreground(g2D);
		
	}
	
	public static void add(Entity e,int level){
		objects.get(level).add(e);
		e.setLevel(level);
		
	}
	public static void add(Wall w,int level) {
		walls.get(level).add(w);
		w.setLevel(level);
	}
	public static void add(Projectile p,int level){

		projectiles.get(level).add(p);
		p.setLevel(level);
	}
	public static void add(Item i,int level){
		items.get(level).add(i);
		i.setLevel(level);
	}
	public static void add(Trap t,int level){
		traps.get(level).add(t);
		t.setLevel(level);
	}
	public static int getLevel(){
		return level;
	}
	public static void setLevel(int lvl){
		level = lvl;
	}
	public static void remove(Entity e,int level){
		objects.get(level).remove(objects.get(level).indexOf(e));
	}
	public static void remove(Wall w,int level) {
		walls.get(level).remove(walls.get(level).indexOf(w));
	}
	public static void remove(Projectile p,int level){
		projectiles.get(level).remove(projectiles.get(level).indexOf(p));
	}
	
	public static void reset(){
		for(int i = 0; i < objects.get(level).size();i++){
			Entity e = objects.get(level).get(i);
			if(e.ID == 4 || e.ID == 10 || e.ID == 16){
				e.stop();
			}
		}
		objects = new ArrayList<ArrayList<Entity>>();
		walls = new ArrayList<ArrayList<Wall>>();
		projectiles = new ArrayList<ArrayList<Projectile>>();
		items = new ArrayList<ArrayList<Item>>();
		init();
	//	Main.init_objects();
		Main.addWalls();
		level = 0;
	}
	public void playPlayerHurt(){
	    try {
	        audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(Main.class.getResourceAsStream("/stuff/player_hurt.wav")));
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	public void playPowerSound(){
	    try {
	        audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(Main.class.getResourceAsStream("/stuff/Powerup.wav")));
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	public void playEnemyDeath(){
	    try {
	        audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(Main.class.getResourceAsStream("/stuff/enemy_kill.wav")));
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	public void playDoorSound(){
	    try {
	        audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(Main.class.getResourceAsStream("/stuff/Door.wav")));
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	public void playExplosionSound(){
	    try {
	        audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(Main.class.getResourceAsStream("/stuff/explosion.wav")));
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}	
	
	public void playPlayerDeath(){
	    try {
	    	audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream((Main.class.getResourceAsStream("/stuff/playerDie.wav"))));
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	public void playBombPickup(){
	    try {
	    	audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream((Main.class.getResourceAsStream("/stuff/bomb_pickup.wav"))));
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	public void playEnemyHurt(){
	    try {
	    	audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream((Main.class.getResourceAsStream("/stuff/enemy_hurt.wav"))));
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}


}

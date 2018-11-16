

import java.io.*;
import java.util.*;
import javax.swing.Timer;
import javax.swing.text.html.parser.TagElement;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

@SuppressWarnings("serial")
public class Main{
 static JFrame F;
 static JLabel start_label = new JLabel();
 static ImageIcon start_icon;
 int k,K;//used to time the head bobbing in main menu
 int u,U;//used to time bobbing in player custom
 Button b_start,b_options,b_quit,p_back,p_quit;//b -> start screen buttons, p -> options/pause screen buttons
 Button p_left,p_right,p_shoot,p_jump;//rebind controls
 Button p_tuts,p_throw,p_ez,p_med,p_hard;
 Button restart,d_quit;
 Button vm,vq;//victory buttons
 Button p_music;
 Button p_player;
 Button inv_left,inv_right,inv_drop;
 Slider red,gren,blu;
 static BufferedImage bI;
 static Graphics g;
 static Graphics2D g2D;
 Point mp = new Point(0,0);// mouse position
 Timer frame_ticker,fade_ticker,ammo_timer;// frame ticker & fading ticker for transition
 static Font font;
 Font title_font,JapanChars,f;
 static Font tutor_font,sml_font;
 Font inv_font;

 FontMetrics fm;
 public static int screen_state = 0;
 boolean do_it_once = true;//for pause screen effects
 boolean[] reassign_key = new boolean[8];//if this is true, the indexed key is waiting to be pressed;
 int new_key = 0;
 int pending = 0;//corresponds to which button we are changing, the index in the controls array
 /* 0 for start screen
  * -2 for death screen
  * -1 for loading
  * 1 for gameplay
  * 2 for pause in game
  * 3 for OPTIONS panel in menu
  * 4 for death screen
  * 5 for tutorial display
  * 6 for character customize
  * 7 for victory
  */
 ArrayList<ImageIcon> player_custom = new ArrayList<ImageIcon>();
 public int[] controls = {65,68,82,32,71,81,69,84};//left right shoot jump throw inv_left inv_right drop
 public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
 public static  Player player;
 public static int diff = 1;//0 for ez,1 for med, 2 for hard
// public static Animation_Handler Animation_Handler = new Animation_Handler();
// coli_han coli_han = new coli_han();
 Color greenish = new Color(128,240,119);
 Color bluish = new Color(66,139,202);
 Color redish = new Color(130, 29, 24);
 public ArrayList<Item> inventory = new ArrayList<Item>();
 Collision_Handler coli_han = new Collision_Handler(inventory);//main and coli_han now share the inventory object
 ImageIcon heart = loadImage("/stuff/heart.png");
 ImageIcon star = loadImage("/stuff/star_1.png");
 ImageIcon[] heads = new ImageIcon[2];
 ImageIcon smoke = loadImage("/stuff/smokebg.png");
 ImageIcon inv_katana = loadImage("/stuff/inv_katana.png");
 AudioInputStream audioInputStream;
 Clip clip;
 static boolean music=true,tuts = true;
 static TextPanel info;
 static int item_select = 0;
 JapanChar[] chars = new JapanChar[9];
 boolean MOUSE_PRESSED;
 Boss boss;
 void init() throws FontFormatException, IOException{
  F = new JFrame();
  //font
  
  f = loadFont("/stuff/font.ttf");
  
  font = f.deriveFont(40f);//float
  title_font = f.deriveFont(100f);
  tutor_font = f.deriveFont(20f);
  sml_font = f.deriveFont(30f);
  inv_font = f.deriveFont(15f);
  JapanChars = loadFont("/stuff/japanchars.TTF").deriveFont(80f);
  coli_han.setFont(inv_font);
  info = new TextPanel(200,100,1500,900,new Color(25,25,25),tutor_font);
  chars[0] = new JapanChar(300,200, JapanChars, "ダ",(int) (Math.random()*20+30));
  chars[1] = new JapanChar(500,500, JapanChars, "狗",(int) (Math.random()*20+30));
  chars[2] = new JapanChar(600,900, JapanChars, "者",(int) (Math.random()*20+30));
  chars[3] = new JapanChar(1400,500, JapanChars, "星",(int) (Math.random()*20+30));
  chars[4] = new JapanChar(1700,900, JapanChars, "錯",(int) (Math.random()*20+30));
  chars[5] = new JapanChar(1600,600, JapanChars, "藹",(int) (Math.random()*20+30));
  chars[6] = new JapanChar(100,800, JapanChars, "機",(int) (Math.random()*20+30));
  chars[7] = new JapanChar(1800,100, JapanChars, "計",(int) (Math.random()*20+30));
  chars[8] = new JapanChar(1000,100,JapanChars,"儀",(int) (Math.random()*20+30));
  heads[0] = loadImage("/stuff/head.png");
  heads[1] = loadImage("/stuff/head2.png");
  F.setSize(screenSize.width, screenSize.height);
  F.setResizable(false);
  F.setTitle("Game");
  F.setExtendedState(JFrame.MAXIMIZED_BOTH);
  F.setUndecorated(true);
  F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  bI = new BufferedImage(F.getWidth(), F.getHeight(), BufferedImage.TYPE_INT_ARGB);
  g = bI.getGraphics();
  g2D = (Graphics2D) g;
  
  player_custom.add(loadImage("/stuff/Cplayer_right1.png"));
  player_custom.add(loadImage("/stuff/Cplayer_right2.png"));
  
  
  red = new Slider(200,500,510,63,Color.red);
  gren = new Slider(200,600,510,71,Color.green);
  blu = new Slider(200,700,510,204,Color.blue);
  
  
  //*DIMENSIONS ARE SET IN THE DRAW FUNCTIONS, DON'T TRUST THESE*//
  b_start = new Button(F.getWidth() / 2 - 200, F.getHeight() / 2 - 50, 400, 100, greenish);
  b_start.setText("Start");
  b_options = new Button(F.getWidth() / 2 - 200, F.getHeight() / 2 + 100, 400, 100, greenish);
  b_options.setText("Options");
  b_quit = new Button(F.getWidth() / 2 - 200, F.getHeight() / 2 + 250, 400, 100, greenish);
  b_quit.setText("Quit");
  
  p_left = new Button(F.getWidth()/4-150,F.getHeight()/6-50,300,100,greenish);
  p_left.setText("A");
  p_right = new Button(F.getWidth()/4-150,F.getHeight()*2/6-50,300,100,greenish);
  p_right.setText("D");
  p_shoot = new Button(F.getWidth()/4-150,F.getHeight()*3/6-50,300,100,greenish);
  p_shoot.setText("R");
  p_jump = new Button(F.getWidth()/4-150,F.getHeight()*4/6-50,300,100,greenish);
  p_jump.setText("SPACE");
  p_back = new Button(0,0,300,100,greenish);
  p_back.setText("Back");
  
  p_quit = new Button(F.getWidth()-500,F.getHeight()*5/6-50,300,100,greenish);
  p_quit.setText("Quit");
  restart = new Button(F.getWidth()/2-250, F.getHeight()/2+25, 500, 125, new Color(186, 50, 46),new Color(249, 24, 12));
  restart.setText("Main Menu");
  d_quit = new Button(F.getWidth()/2-250, F.getHeight()/2+250, 500, 125, new Color(186, 50, 46),new Color(249, 24, 12));
  d_quit.setText("Quit");
  p_throw = new Button(F.getWidth()/4-200,F.getHeight()-230,300,100,greenish);
  p_throw.setText("G");
  p_tuts = new Button(F.getWidth()/2-105,F.getHeight()*5/6-50,300,100,greenish);
  p_tuts.setText("ON");
  p_ez = new Button(F.getWidth()-500,F.getHeight()/6-50,300,100,greenish);
  p_ez.setText("Easy");
  p_med = new Button(F.getWidth()-500,F.getHeight()*2/6-50,300,100,greenish);
  p_med.setText("Medium");
  p_med.setColor(redish);
  p_med.setSelect(redish);
  p_hard = new Button(F.getWidth()-500,F.getHeight()*3/6-50,300,100,greenish);
  p_hard.setText("Impossible");
  
  p_music = new Button(0,0,300,100,greenish);
  p_music.setText("ON");
  p_player = new Button(F.getWidth()-500,F.getHeight()*4/6-50,300,100,greenish);
  p_player.setText("Edit Ninja");
  inv_left = new Button(0,0,300,100,greenish);
  inv_left.setText("Q");
  inv_right = new Button(0,0,300,100,greenish);
  inv_right.setText("E");
  inv_drop = new Button(0,0,300,100,greenish);
  inv_drop.setText("T");
  coli_han.addInvFont(inv_font);
  
  vm = new Button(F.getWidth()/2-250, F.getHeight()/2+25, 500, 125, new Color(255,215,0),new Color(240,230,140));
  vm.setText("Main Menu");
  vq = new Button(F.getWidth()/2-250, F.getHeight()/2+250, 500, 125, new Color(255,215,0),new Color(240,230,140));
  vq.setText("Quit");
  Animation_Handler.init();
  addWalls();
  player = new Player(100,780,100,100,20);
  boss = new Boss(1500,600,400,400,30);
  startMusic();
//  translate(0);
//  translate(1);
//  translate(2);
  
 }
 public void playSound(){
     try {
         audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("death.wav"));
         clip = AudioSystem.getClip();
         clip.open(audioInputStream);
         clip.start();
     } catch(Exception ex) {
         System.out.println("Error with playing sound.");
         ex.printStackTrace();
     }
 }
 public Main() throws FontFormatException, IOException {
  init();
  ammo_timer = new Timer(2000,new ActionListener(){
   public void actionPerformed(ActionEvent e){

    player.reload();
   }
  });
  ammo_timer.start();
  /*KEY EVENTS*/
  F.addKeyListener(new KeyAdapter(){
   public void keyPressed(KeyEvent e){
    
    if(screen_state == 1 || screen_state == 2){
     if(e.getKeyCode() == controls[0]){//left
//      player.setVx(player.getDefVx()-5);
//      player.setState(1);
//      player.moving = true;
      player.left = true;
     }
     if(e.getKeyCode() == controls[1]){//right
//      player.setVx(player.getDefVx()+5);
//      player.setState(0);
//      player.moving = true;
      player.right = true;
     }
     if(e.getKeyCode() == controls[2]){//shoot
      
      if (player.isHoldingSword()) {// sword
       
       if (!player.isSwingingLeft() && player.getState() == 1) {
        player.swingLeft();
       }
       if (!player.isSwingingRight() && player.getState() == 0) {
        player.swingRight();
       }

      } 
      else {//stars
       
       if (player.ammo > 0) {
        if (player.getState() == 0) {// lookin right;
         player.shoot(new Star(player.getX() + player.getWidth() - 60, player.getY() + 10,
           50, 50, 1, 30, -5, true));
        }
        // lookin left
        else player.shoot(new Star(player.getX() + 10, player.getY() + 20, 50, 50, 1, -30, -5, true));
         
           
        player.ammo--;
       }
      }
     }

     if(e.getKeyCode() == controls[3]){//jump
     
      if(player.grounded){
       player.grounded = false;
       player.setVy(-14);
      }
       
     }
//     if(e.getKeyCode() == KeyEvent.VK_0){
//      player.setX(Animation_Handler.spawns.get(Animation_Handler.level).x);
//      player.setY(Animation_Handler.spawns.get(Animation_Handler.level).y);
//     }
//     if(e.getKeyCode() == KeyEvent.VK_1){
//      for(Wall w : Animation_Handler.walls.get(Animation_Handler.level)){
//       if(w.ID == 7){
//        player.setX(w.getX()-100);
//        player.setY(w.getY());
//       }
//      }
//     }
     if(e.getKeyCode() == controls[4]){//Use Item
      if(!inventory.isEmpty()){
       switch(inventory.get(item_select).ID){
        case 17://bomb
         if(player.getBombs() > 0){
          if(player.getState() == 0){//lookin right;
           player.shoot(new Bomba(player.getX()+player.getWidth()-60,player.getY()+10,50,50,3,10,-15,true));
          }
          //lookin left
          else player.shoot(new Bomba(player.getX()+10,player.getY()+20,50,50,3,-10,-15,true));
         }
        break;
        case 18:
         if(player.getTeleBombs() > 0){
          if(player.getState() == 0 || player.getState() == 4){//lookin right;
           player.shoot(new Tele_Bomb(player.getX()+player.getWidth()-60,player.getY()+10,50,50,3,10,-15,true));
          }
          //lookin left
          else player.shoot(new Tele_Bomb(player.getX()+10,player.getY()+20,50,50,3,-10,-15,true));
         }
        break;
       }
      }
     }
     if(e.getKeyCode() == controls[5]){//tab left
      if(player.hasItems()){
       if(item_select-1 >= 0){
        item_select--;
       }
       if(inventory.get(item_select).ID == 20) player.putSword(true);//if holding sword
       else player.putSword(false);
      }
     }
     if(e.getKeyCode() == controls[6]){//tab right
      if(player.hasItems()){
       if(item_select+1 < inventory.size()){
        item_select++;
       }
       if(inventory.get(item_select).ID == 20) player.putSword(true);//if holding sword
       else player.putSword(false);
      }
     }
     if(e.getKeyCode() == controls[7]){//drop
      if(player.hasItems()){
       switch(inventory.get(item_select).ID){
       case 17:
        Animation_Handler.add(new Bomb_Item(player.getState() == 0 ? player.getX()+100 : player.getX(),player.getY()-80,50,50,50,player.getState() == 0  ? 10 : -10,8),player.level);
        player.bombs--;
        break;
       case 18:
        Animation_Handler.add(new Tele_Bomb_Item(player.getState() == 0  ? player.getX()+100 : player.getX(),player.getY()-80,50,50,50,player.getState() == 0 ? 10 : -10,8),player.level);
        player.tele_bombs--;
        break;
       case 20:
        int dura = inventory.get(item_select).getHealth();
        int mhp =  (int) inventory.get(item_select).maxHP;
        Animation_Handler.add(new Sword(player.getState() == 0  ? player.getX()+100 : player.getX()-20,player.getY()-80,75,75,50,player.getState() == 0 ? 10 : -10,8,dura,mhp),player.level);
        player.dropSword();
        player.putSword(false);
        break;
       }
      }
     }
     if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
      if(screen_state == 2){
       screen_state = 1;
       do_it_once = true;
       ammo_timer.start();
      }
      else{
       ammo_timer.stop();
       draw_pause_screen();
       apply_graphics();
       screen_state = 2;
      }
     }
    }
    if(screen_state == 2 || screen_state == 3){
     if(reassign_key[0] || reassign_key[1] || reassign_key[2] || reassign_key[3] || reassign_key[4] || reassign_key[5] || reassign_key[6] || reassign_key[7]){
      new_key = e.getKeyCode();
      boolean skip = false;
      //cannot have one key for more than one control
      for(int i=0 ;i < 8;i++){
       if(i == pending) continue;//dont compare to self;
       if(new_key == controls[i]) skip = true;
      }
       if(!skip) controls[pending] = new_key;
       switch(pending){ 
       case 0:
        p_left.setText(KeyEvent.getKeyText(controls[0]));
       break;
       case 1:
        p_right.setText(KeyEvent.getKeyText(controls[1]));
       break;
       case 2:
        p_shoot.setText(KeyEvent.getKeyText(controls[2]));
        
       break;
       case 3:
        p_jump.setText(KeyEvent.getKeyText(controls[3]));
       break;
       case 4:
        p_throw.setText(KeyEvent.getKeyText(controls[4]));
        break;
       case 5:
        inv_left.setText(KeyEvent.getKeyText(controls[5]));
        break;
       case 6:
        inv_right.setText(KeyEvent.getKeyText(controls[6]));
        break;
       case 7:
        inv_drop.setText(KeyEvent.getKeyText(controls[7]));
        break;
      
      }
      }
      reassign_key[pending] = false;
    }
    if(screen_state == 5) screen_state = 1;
   }
   public void keyReleased(KeyEvent e){
       //player.moving = false;
    if(screen_state == 1 || screen_state == 2){
     if(e.getKeyCode() == controls[0]){//left
      player.left = false;
      player.moving = false;
     }
     if(e.getKeyCode() == controls[1]){//right
      player.right = false;
      player.moving = false;
     }
    }
       if(player.grounded) player.setVx(player.getDefVx());
   }
  });
  /*MOUSE EVENTS*/
  F.addMouseListener(new MouseAdapter() {
   
   public void mousePressed(MouseEvent e) {
    MOUSE_PRESSED = true;
    switch (screen_state) {
    case 0://start screen
     if (b_quit.mouse_in(mp)) {//hit the quit button
      System.exit(0);
     }
     if(b_options.mouse_in(mp)){
      screen_state = 3;
     }
     if(b_start.mouse_in(mp)){//hit the start button
      //start up the enemies in the tutorial
      Animation_Handler.add(player, 0);
      Animation_Handler.add(boss, 6);
      player.putSword(false);
      item_select = 0;
      init_objects();
      if(diff == 0){
       player.lives = 4;
       player.health = 155;
      }
      if(diff == 1){
       player.lives = 2;
       player.health = 55;
      }
      if(diff == 2){
       player.lives = 1;
       player.health = 10;
      }
      info.setText("Welcome."
        + "<<I have left several scrolls throughout the castle, "
        + "<<to help you along the way."
        + "<<Hopefully you brought some stars. "
        + "<<If you run out, grab some more from your pockets."
        + "<<There may be a few black bombs around here."
        + "<<The purple ones are very mysterious in nature..."
        + "<<I must say, they do prove useful "
        + "<<to chuck up to those pesky ninjas on the platforms."
        + "<<I'll see if I can spare a few things for you."
        + "<<If you're injured, check to your left."
        + "<<I'll leave you a katana."
        + "<<Get to the end, I'll be waiting.");
      fade();
      for(int i = 0;i< Animation_Handler.objects.get(0).size();i++){
       Entity e1 = Animation_Handler.objects.get(0).get(i);
       if(e1.ID == 4 || e1.ID == 10 || e1.ID == 16){
        e1.start();
       }
      }
      
 
     }
     break;
    case 2://pause
     if(p_quit.mouse_in(mp)){
      System.exit(0);
     }
     if(p_back.mouse_in(mp)){
      screen_state = 1;
     }
     if(p_music.mouse_in(mp)){
      if(music){
       clip.stop();
      // clip.close();
       p_music.setText("OFF");
      }
      else{
       clip.start();
       p_music.setText("ON");
      }
      music = !music;
     }
     check_rebind_left();
     check_rebind_right();
     check_rebind_shoot();
     check_rebind_jump();
     check_rebind_throw();
     check_rebind_iL();
     check_rebind_iR();
     check_rebind_drop();
     break;
    case 3://options (main menu)
     if(p_back.mouse_in(mp)){
      screen_state = 0;
     }
     check_rebind_left();
     check_rebind_right();
     check_rebind_shoot();
     check_rebind_jump();
     check_rebind_throw();
     check_rebind_iL();
     check_rebind_iR();
     check_rebind_drop();
     if(p_tuts.mouse_in(mp)){
      tuts = !tuts;
      p_tuts.setText(tuts?"ON":"OFF");
     }
     if(p_ez.mouse_in(mp)){
      diff = 0;
      p_ez.setColor(redish);
      p_ez.setSelect(redish);
      p_med.setColor(greenish);
      p_hard.setColor(greenish);
      p_hard.setSelect(bluish);
      p_med.setSelect(bluish);
     }
     if(p_med.mouse_in(mp)){
      diff = 1;
      p_med.setColor(redish);
      p_med.setSelect(redish);
      p_ez.setColor(greenish);
      p_hard.setColor(greenish);
      p_hard.setSelect(bluish);
      p_ez.setSelect(bluish);
     }
     if(p_hard.mouse_in(mp)){
      diff = 2;
      p_hard.setColor(redish);
      p_hard.setSelect(redish);
      p_med.setColor(greenish);
      p_ez.setColor(greenish);
      p_med.setSelect(bluish);
      p_ez.setSelect(bluish);
     }
     if(p_music.mouse_in(mp)){
      if(music){
       clip.stop();
       //clip.close();
       p_music.setText("OFF");
      }
      else{
       clip.start();
       p_music.setText("ON");
      }
      music = !music;
     }
     if(p_player.mouse_in(mp)){
      screen_state = 6;
     }
     break;
    case -2:
     if(restart.mouse_in(mp)){
      inventory.clear();
      player.bombs = 0;
      player.tele_bombs = 0;
      screen_state = 0;
      player.setX(100);
      player.setY(780);
      boss = new Boss(1500,600,400,400,30);
     }
     if(d_quit.mouse_in(mp)) System.exit(0);
     break;
    case 6://player customization
     if(p_back.mouse_in(mp)){
      screen_state = 3;
      player.setColor(new Color(red.getValue(),gren.getValue(),blu.getValue()));
     }
     if(red.mouse_on(mp)){
      red.activate();
     }
     if(blu.mouse_on(mp)){
      blu.activate();
     }
     if(gren.mouse_on(mp)){
      gren.activate();
     }
     break;
    case 7:
        if(vm.mouse_in(mp)){
            inventory.clear();
            player.bombs = 0;
            player.tele_bombs = 0;
            screen_state = 0;
            player.setX(100);
            player.setY(780);
            boss = new Boss(1500,600,400,400,30);
           }
           if(vq.mouse_in(mp)) System.exit(0);
           break; 
    }//end of switch
   }
   public void mouseReleased(MouseEvent e){
    MOUSE_PRESSED = false;
    red.deActivate();
    blu.deActivate();
    gren.deActivate();
   }
  });
  /*MAIN FRAME RUNNER*/
  frame_ticker = new Timer(10, new ActionListener() {
  
   public void actionPerformed(ActionEvent evt) {
    if(!clip.isRunning()&&music) startMusic();

//    mp = MouseInfo.getPointerInfo().getLocation();
//    SwingUtilities.convertPointFromScreen(mp, F.getContentPane());
    switch (screen_state) {
    case 0:
     mp = MouseInfo.getPointerInfo().getLocation();
     SwingUtilities.convertPointFromScreen(mp,F.getContentPane());
     draw_start_screen();
     apply_graphics();
     break;
    case 1:
     
     if(player.ammo == player.ammo_max) ammo_timer.stop();
     else ammo_timer.start();
     Animation_Handler.animate_all(g2D);
     Animation_Handler.draw_health(g2D);
     
     //main has to draw the images, cuz the static Animation Handler cannot
     draw_ammo();
     draw_hearts();
     draw_inventory();
     coli_han.check_all();
    
     
     apply_graphics();//plaster the updated graphics to the screen
     break;
    case 2://pause in game
     mp = MouseInfo.getPointerInfo().getLocation();
     SwingUtilities.convertPointFromScreen(mp, F.getContentPane());
     draw_pause_screen();
     apply_graphics();
     break;
    case 3://options in menu
     mp = MouseInfo.getPointerInfo().getLocation();
     SwingUtilities.convertPointFromScreen(mp, F.getContentPane());
     draw_options_screen();
     apply_graphics();
     break;
    case -2://death
     mp = MouseInfo.getPointerInfo().getLocation();
     SwingUtilities.convertPointFromScreen(mp, F.getContentPane());
     draw_death_screen();
     apply_graphics();
     break;
    case 5://tutorials
     mp = MouseInfo.getPointerInfo().getLocation();
     SwingUtilities.convertPointFromScreen(mp, F.getContentPane());
     info.display_panel(g2D);
     apply_graphics();
     break;
    case 6:
     
     mp = MouseInfo.getPointerInfo().getLocation();
     SwingUtilities.convertPointFromScreen(mp, F.getContentPane());
     if(red.isActive()){
      if(mp.x < (red.getX()+red.getWidth()*2)) red.setPoint(mp.x);
      //if(mp.x-red.getRemWidth(mp.x) > red.x) red.setPoint(mp.x);
     }
     if(blu.isActive()){
      if(mp.x < (blu.getX()+blu.getWidth()*2)) blu.setPoint(mp.x);
      //if(mp.x-blu.getRemWidth(mp.x) > blu.x) blu.setPoint(mp.x);
     }
     if(gren.isActive()){
      if(mp.x < (gren.getX()+gren.getWidth()*2)) gren.setPoint(mp.x);
      //if(mp.x-gren.getRemWidth(mp.x) > gren.x) gren.setPoint(mp.x);
     }
     draw_player_custom();
     apply_graphics();
  
     break;
    case 7:
        mp = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mp, F.getContentPane());
        draw_victory_screen();
    	apply_graphics();
    	break;
    }
    //g2D.setColor(Color.green);
//    g2D.drawString(mp.x + "," + mp.y,1500,300);
   }
  });
  frame_ticker.start();
  
 }
 
 void check_rebind_left(){
  if(p_left.mouse_in(mp)){
   p_left.setText("_");
   pending = 0;
   reassign_key[0] = true;
  }
  else{
   p_left.setText(KeyEvent.getKeyText(controls[0]));;
   reassign_key[0] = false;
  }
 }
 void check_rebind_right(){
  if(p_right.mouse_in(mp)){
   p_right.setText("_");
   pending = 1;
   reassign_key[1] = true;
  }
  else{
   p_right.setText(KeyEvent.getKeyText(controls[1]));;
   reassign_key[1] = false;
  }
 }
 void check_rebind_shoot(){
  if(p_shoot.mouse_in(mp)){
   p_shoot.setText("_");
   pending = 2;
   reassign_key[2] = true;
  }
  else{
   p_shoot.setText(KeyEvent.getKeyText(controls[2]));;
   reassign_key[2] = false;
  }
 }
 void check_rebind_jump(){
  if(p_jump.mouse_in(mp)){
   p_jump.setText("_");
   pending = 3;
   reassign_key[3] = true;
  }
  else{
   p_jump.setText(KeyEvent.getKeyText(controls[3]));;
   reassign_key[3] = false;
  }
 }
 void check_rebind_throw(){
  if(p_throw.mouse_in(mp)){
   p_throw.setText("_");
   pending = 4;
   reassign_key[4] = true;
  }
  else{
   p_throw.setText(KeyEvent.getKeyText(controls[4]));;
   reassign_key[4] = false;
  }
 }
 void check_rebind_iL(){
  if(inv_left.mouse_in(mp)){
   inv_left.setText("_");
   pending = 5;
   reassign_key[5] = true;
  }
  else{
   inv_left.setText(KeyEvent.getKeyText(controls[5]));;
   reassign_key[5] = false;
  }
 }
 void check_rebind_iR(){
  if(inv_right.mouse_in(mp)){
   inv_right.setText("_");
   pending = 6;
   reassign_key[6] = true;
  }
  else{
   inv_right.setText(KeyEvent.getKeyText(controls[6]));;
   reassign_key[6] = false;
  }
 }
 void check_rebind_drop(){
  if(inv_drop.mouse_in(mp)){
   inv_drop.setText("_");
   pending = 7;
   reassign_key[7] = true;
  }
  else{
   inv_drop.setText(KeyEvent.getKeyText(controls[7]));;
   reassign_key[7] = false;
  }
 }
 void apply_graphics(){//put the graphics on the screen
  start_icon = new ImageIcon(bI);
  start_label.setIcon(start_icon);
  F.add(start_label);// this being the frame
  F.setVisible(true);
 }
 void draw_options_screen(){
  g2D.setFont(font);
  fm = F.getFontMetrics(font);
  g2D.setColor(new Color(25,25,25));
  g2D.fillRect(0, 0, F.getWidth(), F.getHeight());
  g2D.setColor(new Color(230,230,230));
  g2D.drawString("Controls", F.getWidth()/5+50 - fm.stringWidth("Controls")/2, 100);
  g2D.drawString("Inventory", F.getWidth()/3+365 - fm.stringWidth("Inventory")/2, 100);
  g2D.drawString("Difficulty", F.getWidth()-350 - fm.stringWidth("Difficulty")/2, 100);
  g2D.drawString("Left:",p_left.x-190,p_left.y+65);
  g2D.drawString("Right:",p_right.x-220,p_right.y+65);
  g2D.drawString("Attack:",p_shoot.x-270,p_shoot.y+65);
  g2D.drawString("Jump:",p_jump.x-200,p_jump.y+65);
  g2D.drawString("Throw:",p_throw.x-230,p_throw.y+65);
  g2D.drawString("Tutorials",p_tuts.x-7,p_tuts.y+165);
  g2D.drawString("Music:",p_music.x-200,p_music.y+65);
  g2D.drawString("Drop:",inv_drop.x-185,inv_drop.y+65);
  g2D.drawString("Tab L:",inv_left.x-220,inv_left.y+65);
  g2D.drawString("Tab R:",inv_right.x-220,inv_right.y+65);
  p_left.setX(F.getWidth()/4-200);
  p_right.setX(F.getWidth()/4-200);
  p_jump.setX(F.getWidth()/4-200);
  p_shoot.setX(F.getWidth()/4-200);
  p_throw.setX(F.getWidth()/4-200);
  p_back.setX(F.getWidth()-500);
  p_back.setY(F.getHeight()*5/6-50);
  inv_left.setX(F.getWidth()/2-105);
  inv_left.setY(F.getHeight()/6-50);
  inv_right.setX(F.getWidth()/2-105);
  inv_right.setY(F.getHeight()*2/6-50);
  inv_drop.setX(F.getWidth()/2-105);
  inv_drop.setY(F.getHeight()*3/6-50);
  
  
  
  p_music.setX(F.getWidth()/2-105);
  p_music.setY(F.getHeight()*4/6-50);
  
  p_back.draw_button(g2D, p_back.mouse_in(mp), fm);
  p_left.draw_button(g2D,p_left.mouse_in(mp), fm);;
  p_right.draw_button(g2D,p_right.mouse_in(mp), fm); 
  p_shoot.draw_button(g2D,p_shoot.mouse_in(mp), fm); 
  p_jump.draw_button(g2D,p_jump.mouse_in(mp), fm);
  p_throw.draw_button(g2D,p_throw.mouse_in(mp), fm);
  p_tuts.draw_button(g2D, p_tuts.mouse_in(mp), fm);
  fm = F.getFontMetrics(sml_font);
  p_ez.draw_button_sml(g2D, p_ez.mouse_in(mp), fm);
  p_med.draw_button_sml(g2D, p_med.mouse_in(mp), fm);
  p_hard.draw_button_sml(g2D, p_hard.mouse_in(mp), fm);
  p_player.draw_button_sml(g2D,p_player.mouse_in(mp), fm);
  fm = F.getFontMetrics(font);
  p_music.draw_button(g2D,p_music.mouse_in(mp), fm);
  
  inv_left.draw_button(g2D,inv_left.mouse_in(mp), fm);
  inv_right.draw_button(g2D,inv_right.mouse_in(mp), fm);
  inv_drop.draw_button(g2D,inv_drop.mouse_in(mp), fm);
  g2D.setColor(Color.gray);
  g2D.setFont(tutor_font);
  g2D.drawString("Click to rebind!",30,1050);
 }
 void draw_player_custom(){
  g2D.setColor(new Color(25,25,25));
  g2D.fillRect(0, 0, F.getWidth(), F.getHeight());
  if(u > 30){
   if(U == 0) U = 1;
   else U = 0;
   u = 0;
  }
  u++;
  
  red.draw(g2D);
  blu.draw(g2D);
  gren.draw(g2D);
  
  g2D.setColor(new Color(red.getValue(),gren.getValue(),blu.getValue()));
  g2D.fillRect(1400,361,300,429);
  g2D.drawImage(player_custom.get(U).getImage(),1300,299,null);
  p_back.setX(F.getWidth()*2/3+120);
  p_back.setY(F.getHeight()*5/6+50);
  p_back.draw_button(g2D,p_back.mouse_in(mp), fm);
  g2D.setColor(Color.gray);
  g2D.setFont(font);
  g2D.drawString("Adjust the sliders to edit your ninja.",200,250);
 }
 void draw_pause_screen(){
  
  g2D.setFont(font);
  fm = F.getFontMetrics(font);
  if(do_it_once){
   g2D.setColor(new Color(51,51,51,195));
   g2D.fillRect(0, 0, F.getWidth(), F.getHeight());
   do_it_once = false;
  }
  
  p_left.setX(F.getWidth()/2-600);
  p_right.setX(F.getWidth()/2-600);
  p_jump.setX(F.getWidth()/2-600);
  p_shoot.setX(F.getWidth()/2-600);
  p_throw.setX(F.getWidth()/2-600);
  p_quit.setColor(redish);
  p_quit.setY(F.getHeight()*5/6+50);
  p_quit.setX(F.getWidth()*2/3+200);
  p_back.setX(F.getWidth()*2/3+200);
  p_back.setY(F.getHeight()*4/6+50);
  p_music.setX(F.getWidth()*2/3+200);
  p_music.setY(F.getHeight()*3/6+50);
  
  inv_left.setX(F.getWidth()/2+200 - fm.stringWidth("Inventory")/2);
  inv_left.setY(F.getHeight()/6-50);
  inv_right.setX(F.getWidth()/2+200 - fm.stringWidth("Inventory")/2);
  inv_right.setY(F.getHeight()*2/6-50);
  inv_drop.setX(F.getWidth()/2+200 - fm.stringWidth("Inventory")/2);
  inv_drop.setY(F.getHeight()*3/6-50);
  
  g2D.setColor(new Color(230,230,230));
  g2D.drawString("Controls", F.getWidth()/2-450 - fm.stringWidth("Controls")/2, 100);
  g2D.drawString("Inventory", F.getWidth()/2+200 - fm.stringWidth("Inventory")/2, 100);
  g2D.drawString("Left:",p_left.x-190,p_left.y+65);
  g2D.drawString("Right:",p_right.x-220,p_right.y+65);
  g2D.drawString("Attack:",p_shoot.x-270,p_shoot.y+65);
  g2D.drawString("Jump:",p_jump.x-200,p_jump.y+65);
  g2D.drawString("Throw:",p_throw.x-230,p_throw.y+65);
  g2D.drawString("Drop:",inv_drop.x-185,inv_drop.y+65);
  g2D.drawString("Tab L:",inv_left.x-220,inv_left.y+65);
  g2D.drawString("Tab R:",inv_right.x-220,inv_right.y+65);
  g2D.drawString("Music",F.getWidth()*2/3+340 - fm.stringWidth("Music")/2,F.getHeight()*3/6);
  p_quit.draw_button(g2D, p_quit.mouse_in(mp), fm);
  p_back.draw_button(g2D,p_back.mouse_in(mp), fm);
  p_left.draw_button(g2D,p_left.mouse_in(mp), fm);;
  p_right.draw_button(g2D,p_right.mouse_in(mp),fm); 
  p_shoot.draw_button(g2D,p_shoot.mouse_in(mp), fm); 
  p_jump.draw_button(g2D,p_jump.mouse_in(mp),fm);
  p_throw.draw_button(g2D,p_throw.mouse_in(mp), fm);
  p_music.draw_button(g2D,p_music.mouse_in(mp), fm);
  inv_left.draw_button(g2D,inv_left.mouse_in(mp), fm);
  inv_right.draw_button(g2D,inv_right.mouse_in(mp), fm);
  inv_drop.draw_button(g2D,inv_drop.mouse_in(mp), fm);
  g2D.setColor(Color.gray);
  g2D.setFont(tutor_font);
  g2D.drawString("Click to rebind!",30,1050);
 }
 void fade_to_black(int x) {
  screen_state = -1;
  g2D.setColor(new Color(0, 0, 0, x++));
  g2D.fillRect(0, 0, F.getWidth(), F.getHeight());
  
  start_icon = new ImageIcon(bI);
  start_label.setIcon(start_icon);
  F.add(start_label);// this being the frame
  F.setVisible(true);
 }
 void fade(){
   fade_ticker = new Timer(20, new ActionListener() {
    int x = 0;
    boolean black = true;
    public void actionPerformed(ActionEvent e) {
     if(black){
      if(x > 250){
       black = false;
       x = 0;
      }
      else fade_to_black(x+=5);
     }
     else{
      if(x > 250){
       fade_ticker.stop();
       screen_state = tuts ? 5 : 1;
      
      }
      else{
       fade_from_black(x+=5);
      }
     }
    }
   });
   fade_ticker.start();
 }
 void fade_from_black(int x){
  /*this is needed to reset the frame
   *and draw a slightly lighter color each time
   */
  
  bI = new BufferedImage(F.getWidth(), F.getHeight(), BufferedImage.TYPE_INT_ARGB);
  g = bI.getGraphics();
  g2D = (Graphics2D) g;
  coli_han.check_all();
  Animation_Handler.animate_all(g2D);
  Animation_Handler.draw_health(g2D);
  draw_hearts();
  draw_ammo();
  g2D.setColor(new Color(0, 0, 0, 255-x));
  g2D.fillRect(0, 0, F.getWidth(), F.getHeight());
  start_icon = new ImageIcon(bI);
  start_label.setIcon(start_icon);
  F.add(start_label);// this being the frame
  F.setVisible(true);
 }
 void draw_start_screen() {
  //t is a transition variable, true if currently starting the game
  g2D.setColor(new Color(51,51,51));
  g2D.fillRect(0, 0, F.getWidth(), F.getHeight());
  if(k > 20){
   if(K == 0) K = 1;
   else K = 0;
   k=0;
  }
  k++;
  
  g2D.setColor(player.getColor());
  g2D.fillRect(900,1000-10*K,100,100);
  g2D.drawImage(smoke.getImage(),750,850,null);
  g2D.drawImage(heads[K].getImage(),900,980,null);
  
  for(JapanChar c : chars){
   c.move();
   c.draw(g2D);
  }
  /*
   * draw_button takes the mouse position and comapares it with the button
   * calling it, returning true if it's within the borders of the button
   */
  g2D.setFont(font);
  fm = F.getFontMetrics(font);
  b_start.draw_button(g2D, b_start.mouse_in(mp),fm);
  b_options.draw_button(g2D, b_options.mouse_in(mp),fm);
  b_quit.draw_button(g2D, b_quit.mouse_in(mp),fm);
  g2D.setFont(title_font);
  g2D.setColor(Color.white);
  fm = F.getFontMetrics(title_font);
  g2D.drawString("Pagoda Dungeon",F.getWidth()/2-fm.stringWidth("ninja_game.jar")/2,F.getHeight()/2-fm.getHeight()*2);
  g2D.setColor(Color.gray);
  g2D.setFont(tutor_font);
  g2D.drawString("Music by Kevin MacLeod",30,1050);
 }

 void addObject(Entity e) {
  //adds an object to the current level
  Animation_Handler.add(e,Animation_Handler.getLevel());
 }

 void removeObject(Entity e){
  Animation_Handler.remove(e, Animation_Handler.getLevel());
  
 }

 static void init_objects(){
		// Enemy_Ninja(int x,int y,int w,int h,int T,int state,int frequency,int
		// x_comp,int y_comp,boolean patrols,int span,int speed){
		// public Bomba(int x,int y,int w,int h,int T,int Vx,int Vy,boolean
		// friendly){
		// Swordsman(int x,int y,int w,int h,int T,int state,int span,int
		// aSpan,int speed,int dash) { //aSpan < span
		// Tutorial
		if (diff == 0 || diff == 1) {
			Animation_Handler.add(new Health_Pot(-450, 390, 50, 50, 50), 0);
			Animation_Handler.add(new Life(-300, 590, 50, 50, 50), 0);
			if (diff == 0) {
				Animation_Handler.add(new Life(-150, 690, 50, 50, 50), 0);
				Animation_Handler.add(new Health_Pot(0, 690, 50, 50, 50), 0);
			}
		}

		Animation_Handler.add(new Bomb_Item(600, 830, 50, 50, 1), 0);
		Animation_Handler.add(new Tele_Bomb_Item(650, 830, 50, 50, 1), 0);
		Animation_Handler.add(new Sword(200, 830, 75, 75, 1, 255), 0);
		Animation_Handler.add(new Bomb_Item(700, 830, 50, 50, 1), 0);
		Animation_Handler.add(new Tele_Bomb_Item(750, 830, 50, 50, 1), 0);
		Animation_Handler.add(new Enemy_Ninja(800, 780, 100, 100, 20, 0, 1000, 30, 5, true, 700, 2), 0);
		Animation_Handler.add(new Enemy_Ninja(300, 200, 100, 100, 20, 0, 1000, 30, 5, false, 0, 0), 0);


		// Stage 1
		Animation_Handler.add(new Bomber(225, 270, 100, 100, 20, 0, 4000, 13, 13, false, 0, 0), 1);
		Animation_Handler.add(new Enemy_Ninja(230, 450, 100, 100, 20, 0, 1000, 25, 3, false, 0, 0), 1);
		Animation_Handler.add(new Enemy_Ninja(2175, 80, 100, 100, 20, 1, 1000, 20, 10, false, 0, 0), 1);
		if (diff == 2){
			Animation_Handler.add(new Swordsman(1400, 780, 100, 100, 20, 0, 500, 300, 4, 7), 1);
		}
		Animation_Handler.add(new Swordsman(1200, 780, 100, 100, 20, 0, 500, 300, 2, 5), 1);
		Animation_Handler.add(new Bomb_Item(1500,780,50,50,50),1);
		Animation_Handler.add(new Tele_Bomb_Item(1660,780,50,50,50),1);
		if (diff < 2) {
			Animation_Handler.add(new Health_Pot(100,310,50, 50, 50), 1);
			Animation_Handler.add(new Health_Pot(160,310,50, 50, 50), 1);
		}
		// Stage 2
		Animation_Handler.add(new Enemy_Ninja(1300, 130, 100, 100, 20, 0, 2000, 9, 10, true, 150, 1), 2);
		Animation_Handler.add(new Enemy_Ninja(1440, 480, 100, 100, 20, 1, 1000, 10, 13, false, 0, 0), 2);
		if (diff < 2) {
			Animation_Handler.add(new Life(1200,70,50, 50, 50), 2);
			Animation_Handler.add(new Health_Pot(1450,70,50, 50, 50), 2);
		}
		if (diff >= 1) {
			Animation_Handler.add(new Bat(1200, 600, 100, 100, 2, 10), 2);
			Animation_Handler.add(new Bat(1250, 650, 100, 100, 2, 10), 2);
			
			if (diff == 2) {
				Animation_Handler.add(new Bat(1300, 670, 100, 100, 2, 10), 2);
				Animation_Handler.add(new Bat(1180, 620, 100, 100, 2, 10), 2);
			}
		}
		Animation_Handler.add(new Bat(1100, 600, 100, 100, 2, 15), 2);
		Animation_Handler.add(new Bat(1150, 450, 100, 100, 2, 15), 2);
		Animation_Handler.add(new Bat(1200, 550, 100, 100, 2, 15), 2);
		// Stage 3
		Animation_Handler.add(new Enemy_Ninja(550, 250, 100, 100, 20, 1, 1000, 10, 10, false, 0, 0), 3);
		Animation_Handler.add(new Bomber(550, -50, 100, 100, 20, 1, 6000, 4, 7, false, 0, 0), 3);
		Animation_Handler.add(new Enemy_Ninja(550, -250, 100, 100, 20, 1, 1000, 10, 10, false, 0, 0), 3);

		Animation_Handler.add(new Bat(300, 800, 100, 100, 2, 15), 3);
		Animation_Handler.add(new Bat(500, 300, 100, 100, 2, 15), 3);
		Animation_Handler.add(new Bat(1000, 600, 100, 100, 2, 15), 3);

		Animation_Handler.add(new Bomber(810, -650, 100, 100, 20, 0, 1500, 3, 4, false, 0, 0), 3);
		Animation_Handler.add(new Sword(730,1400,75,75,50,255),3);

		if (diff < 2) {
			Animation_Handler.add(new Health_Pot(585, 800, 50, 50, 50), 3);
			Animation_Handler.add(new Life(1475, 150, 50, 50, 50), 3);
			Animation_Handler.add(new Health_Pot(210, 110, 50, 50, 50), 4);
			Animation_Handler.add(new Life(270, 110, 50, 50, 50), 4);
		}
		Animation_Handler.add(new Bomb_Item(1400,150,50,50,50),3);
		Animation_Handler.add(new Tele_Bomb_Item(1550,150,50,50,50),3);
		Animation_Handler.add(new Tele_Bomb_Item(850, 365, 50, 50, 4), 4);
		Animation_Handler.add(new Bomber(1120, 750, 100, 100, 20, 0, 7000, 26, 14, false, 0, 0), 4);
		Animation_Handler.add(new Enemy_Ninja(1150, 500, 100, 100, 20, 1, 1000, 10, 8,false,0,0), 4);
		Animation_Handler.add(new Bomber(1290, 315, 100, 100, 20, 0, 5000, 17, 4, false, 0, 0), 4);
		Animation_Handler.add(new Bomber(30, 160, 100, 100, 20, 0, 1000, 9, 10, false, 0, 0), 4);
		Animation_Handler.add(new Wall(850, 100, 170, 30), 4);
		Animation_Handler.add(new V_Platform(550, 300, 100, 30, 100, 415), 4);
		Animation_Handler.add(new Wall(1250, 100, 350, 30), 4);
		Animation_Handler.add(new Bat(400, 300, 100, 100, 1, 15), 4);
		Animation_Handler.add(new Bat(1600, 75, 100, 100, 1, 15), 4);
		Animation_Handler.add(new Swordsman(1400, 0, 100, 100, 20, 0, 250, 150, 2, 5), 4);
		
		Animation_Handler.add(new Bomb_Item(1950,590,50,50,50),4);
		Animation_Handler.add(new Bomb_Item(2000,590,50,50,50),4);
		Animation_Handler.add(new Bomb_Item(2050,590,50,50,50),4);
		
		if(diff < 2){
			Animation_Handler.add(new Life(200,650,50,50,50),5);
			Animation_Handler.add(new Life(250,650,50,50,50),5);
			Animation_Handler.add(new Life(300,650,50,50,50),5);
			
			Animation_Handler.add(new Health_Pot(400,650,50,50,50),5);
			Animation_Handler.add(new Health_Pot(450,650,50,50,50),5);
			Animation_Handler.add(new Health_Pot(500,650,50,50,50),5);
		}
		
		Animation_Handler.add(new Bomb_Item(600,650,50,50,50),5);
		Animation_Handler.add(new Bomb_Item(650,650,50,50,50),5);
		Animation_Handler.add(new Bomb_Item(700,650,50,50,50),5);
		
		Animation_Handler.add(new Tele_Bomb_Item(800,650,50,50,50),5);
		Animation_Handler.add(new Tele_Bomb_Item(850,650,50,50,50),5);
		Animation_Handler.add(new Tele_Bomb_Item(900,650,50,50,50),5);
		
		Animation_Handler.add(new Sword(1050,630,75,75,50,6000,6000),5);
		
		Animation_Handler.add(new Bomb_Item(1200,650,50,50,50),5);
		Animation_Handler.add(new Bomb_Item(1250,650,50,50,50),5);
		Animation_Handler.add(new Bomb_Item(1300,650,50,50,50),5);
		
		Animation_Handler.add(new Tele_Bomb_Item(1400,650,50,50,50),5);
		Animation_Handler.add(new Tele_Bomb_Item(1450,650,50,50,50),5);
		Animation_Handler.add(new Tele_Bomb_Item(1500,650,50,50,50),5);
		
		Animation_Handler.add(new Bomb_Item(1600,650,50,50,50),5);
		Animation_Handler.add(new Bomb_Item(1650,650,50,50,50),5);
		Animation_Handler.add(new Bomb_Item(1700,650,50,50,50),5);
		
		


 }
 static void addWalls() {
		// Animation_Handler.add(new Wall(x, y, w, h), level);
		// Animation_Handler.add(new Lava(x,y,w,h),level);
		/* Stage 0 */

		// Border//
		Animation_Handler.add(new Wall(-500, 0, 25, 1080), 0);
		Animation_Handler.add(new Wall(1894, 0, 30, 350), 0);
		Animation_Handler.add(new Wall(1894, 450, 30, 630), 0);
		Animation_Handler.add(new Wall(-500, 0, 2420, 25), 0);
		Animation_Handler.add(new Wall(-500, 740, 600, 200), 0);
		Animation_Handler.add(new Wall(-500, 640, 300, 200), 0);
		// Border^//
		Animation_Handler.add(new Wall(-500, 880, 2420, 200), 0);
		Animation_Handler.add(new Wall(325, 680, 225, 50), 0);
		Animation_Handler.add(new Wall(800, 630, 200, 50), 0);
		Animation_Handler.add(new Wall(1200, 550, 200, 50), 0);
		Animation_Handler.add(new Wall(1600, 450, 300, 100), 0);
		Animation_Handler.add(new Wall(200, 300, 300, 30), 0);
		Animation_Handler.add(new Door(1860, 330, 100, 120), 0);

		/* Stage 1 */

		// Border//
		Animation_Handler.add(new Wall(0, 0, 25, 1080), 1);
		Animation_Handler.add(new Wall(0, 1055, 2320, 25), 1);
		Animation_Handler.add(new Wall(2294, 0, 430, 120), 1);
		Animation_Handler.add(new Door(2260, 120, 100, 120), 1);
		Animation_Handler.add(new Wall(2294, 240, 430, 1080 - 240), 1);
		Animation_Handler.add(new Wall(0, 0, 2300, 25), 1);
		// Obstacles//

		Animation_Handler.add(new Wall(200, 1080 - 150, 30, 150), 1);
		Animation_Handler.add(new Lava(230, 1025, 120, 30, false), 1);
		Animation_Handler.add(new Wall(350, 850, 30, 250), 1);
		Animation_Handler.add(new Lava(380, 1025, 120, 30, false), 1);
		Animation_Handler.add(new Wall(500, 740, 30, 340), 1);
		Animation_Handler.add(new Lava(530, 1025, 170, 30, false), 1);
		Animation_Handler.add(new H_Platform(850, 260, 30, 30, 1115, 675), 1);
		Animation_Handler.add(new Lava(1920, 920, 374, 135, false), 1);
		Animation_Handler.add(new Wall(700, 1080 - 200, 1920 - 700, 200), 1);
		Animation_Handler.add(new H_Platform(1300, 580, 150, 30, 1500, 1200), 1);
		Animation_Handler.add(new Wall(850, 450, 30, 30), 1);
		Animation_Handler.add(new Wall(1500, 1080 - 360, 100, 30), 1);
		Animation_Handler.add(new Wall(1700, 1080 - 510, 30, 80), 1);
		Animation_Handler.add(new Wall(0, 370, 600, 30), 1);
		Animation_Handler.add(new Wall(1190, 270, 30, 30), 1);
		Animation_Handler.add(new Wall(1320, 1080 - 870, 30, 30), 1);
		Animation_Handler.add(new Wall(1440, 1080 - 830, 30, 30), 1);
		Animation_Handler.add(new V_Platform(1600, 230, 200, 30, 1080 - 900, 1080 - 700), 1);
		Animation_Handler.add(new Wall(1100, 500, 30, 30), 1);
		Animation_Handler.add(new Wall(2040, 430, 30, 100), 1);
		Animation_Handler.add(new Wall(2040, 0, 30, 150), 1);
		Animation_Handler.add(new V_Platform(2145, 420, 150, 30, 240, 450), 1);
		Animation_Handler.add(new H_Platform(180, 550, 200, 30, 550, 50), 1);

		/* Stage 2 */
		// Border//
		Animation_Handler.add(new Wall(0, 0, 25, 1080), 2);
		Animation_Handler.add(new Wall(0, 1055, 1920, 25), 2);
		Animation_Handler.add(new Wall(1895, 0, 30, 134), 2);
		Animation_Handler.add(new Wall(25, 0, 1870, 25), 2);
		Animation_Handler.add(new Door(1870, 135, 100, 145), 2);
		// Obstacles//

		Animation_Handler.add(new Wall(0, 1080 - 700, 300, 700), 2);
		Animation_Handler.add(new Lava(300, 1080 - 55, 300, 30, false), 2);
		Animation_Handler.add(new Wall(600, 1080 - 550, 25, 550), 2);
		Animation_Handler.add(new Lava(625, 1080 - 55, 175, 30, false), 2);
		Animation_Handler.add(new Wall(800, 1080 - 650, 25, 650), 2);
		Animation_Handler.add(new Lava(825, 1080 - 55, 375, 30, false), 2);
		Animation_Handler.add(new Wall(1200, 1080 - 300, 25, 300), 2);
		Animation_Handler.add(new Lava(1225, 1080 - 55, 125, 30, false), 2);
		Animation_Handler.add(new Wall(1350, 1080 - 375, 25, 375), 2);
		Animation_Handler.add(new Lava(1375, 1080 - 55, 100, 30, false), 2);
		Animation_Handler.add(new Lava(1500, 1080 - 55, 395, 30, false), 2);
		Animation_Handler.add(new Wall(1475, 1080 - 500, 25, 500), 2);
		Animation_Handler.add(new Wall(1200, 1080 - 850, 300, 35), 2);
		Animation_Handler.add(new Wall(1895, 1080 - 800, 25, 800), 2);
		Animation_Handler.add(new V_Platform(1620, 1080 - 650, 275, 35, 1080 - 700, 1080 - 450), 2);

		/* stage 3 */
		// Border//
		Animation_Handler.add(new Wall(0, -700, 25, 2500), 3);
		Animation_Handler.add(new Wall(0, -700, 2500, 25), 3);
		Animation_Handler.add(new Wall(2475, -700, 25, 300), 3);
		Animation_Handler.add(new Wall(2475, -250, 25, 2050), 3);
		Animation_Handler.add(new Wall(0, 1800, 2500, 25), 3);
		Animation_Handler.add(new Door(2450, -400, 100, 150), 3);
		Animation_Handler.add(new Lava(25, 1700, 2450, 100, true), 3);
		// Obstacles//
		Animation_Handler.add(new Wall(0, 1600, 400, 50), 3);
		Animation_Handler.add(new Wall(650, 1500, 200, 25), 3);
		Animation_Handler.add(new Wall(1050, 1450, 25, 25), 3);
		Animation_Handler.add(new Wall(1300, 1400, 200, 25), 3);
		Animation_Handler.add(new Wall(1800, 1600, 25, 25), 3);
		Animation_Handler.add(new V_Platform(2000, 1250, 200, 25, 1000, 1500), 3);
		Animation_Handler.add(new Wall(1800, 1000, 25, 25), 3);
		Animation_Handler.add(new Wall(1400, 900, 200, 25), 3);
		Animation_Handler.add(new H_Platform(1050, 750, 25, 25, 1200, 600), 3);
		Animation_Handler.add(new Wall(250, 650, 200, 25), 3);
		Animation_Handler.add(new V_Platform(50, 500, 150, 25, -200, 650), 3);
		Animation_Handler.add(new Wall(550, 350, 100, 25), 3);
		Animation_Handler.add(new Wall(550, 150, 100, 25), 3);
		Animation_Handler.add(new Wall(550, -50, 100, 25), 3);
		Animation_Handler.add(new Wall(350, -250, 600, 25), 3);
		Animation_Handler.add(new Wall(1100, -250, 200, 25), 3);
		Animation_Handler.add(new Wall(1300, 200, 400, 25), 3);// bonus life
		Animation_Handler.add(new H_Platform(1300, -250, 200, 25, 1800, 1300), 3);
		Animation_Handler.add(new Wall(2200, -250, 300, 25), 3);
		Animation_Handler.add(new Wall(600, 900, 25, 25), 3);
		Animation_Handler.add(new Wall(800, -550, 100, 25), 3);

		/* STAGE 4 */
		Animation_Handler.add(new Wall(0, -200, 25, 1280), 4);
		Animation_Handler.add(new Wall(0, 1055, 2320, 25), 4);
		Animation_Handler.add(new Wall(2294, -200, 830, 320), 4);
		Animation_Handler.add(new Door(2260, 550, 100, 120), 4);
		// Animation_Handler.add(new Wall(2240,220,100,30),4);
		Animation_Handler.add(new Wall(2294, 650, 100, 1080 - 240), 4);
		Animation_Handler.add(new Wall(2294, 110, 100, 440), 4);
		Animation_Handler.add(new Wall(0, -400, 2300, 225), 4);
		Animation_Handler.add(new Wall(25, 845, 200, 210), 4);
		Animation_Handler.add(new Wall(470, 765, 100, 25), 4);
		Animation_Handler.add(new Wall(850, 765, 100, 25), 4);
		Animation_Handler.add(new Wall(0, 650, 100, 30), 4);
		Animation_Handler.add(new Wall(200, 455, 100, 30), 4);
		Animation_Handler.add(new Wall(0, 260, 100, 30), 4);
		Animation_Handler.add(new Wall(220, 160, 100, 30), 4);
		Animation_Handler.add(new Wall(320, -200, 30, 275), 4);
		Animation_Handler.add(new Lava(225, 900, 2070, 160, false), 4);
		Animation_Handler.add(new Wall(1080, 600, 30, 600), 4);
		Animation_Handler.add(new Wall(1080, 600, 200, 30), 4);
		Animation_Handler.add(new Wall(850, 415, 100, 30), 4);
		Animation_Handler.add(new Wall(1800, 650, 500, 30), 4);
		Animation_Handler.add(new Wall(1080, 850, 150, 30), 4);
		Animation_Handler.add(new Wall(1250, 125, 30, 500), 4);
		Animation_Handler.add(new Wall(1280, 415, 100, 30), 4);

		/* STAGE 5 */
		Animation_Handler.add(new Wall(0, 0, 1920, 360), 5);
		Animation_Handler.add(new Wall(0, 720, 1920, 425), 5);
		Animation_Handler.add(new Wall(0, 0, 25, 1080), 5);
		Animation_Handler.add(new Wall(1895, 0, 25, 560), 5);
		Animation_Handler.add(new Door(1855, 560, 100, 160, true), 5);
		
		/* STAGE 6 (Boss) */
		Animation_Handler.add(new Wall(0,800,300,50), 6);
		Animation_Handler.add(new Wall(0,0, 2920, 50), 6);
		Animation_Handler.add(new Wall(2920,0,50,1080), 6);
		Animation_Handler.add(new Wall(0,1030,2920,50), 6);
		Animation_Handler.add(new Wall(0,0,50,1880), 6);
 }
 void draw_death_screen(){
  g2D.setColor(Color.red);
  g2D.fillRect(0, 0, F.getWidth(), F.getHeight());
  g2D.setFont(font);
  fm = F.getFontMetrics(font);
  restart.draw_button(g2D,restart.mouse_in(mp), fm,new Color(135, 5, 5));
  d_quit.draw_button(g2D,d_quit.mouse_in(mp), fm,new Color(135,5,5));
  g2D.setColor(new Color(130, 29, 24));
  g2D.setFont(title_font);
  fm = F.getFontMetrics(title_font);
  g2D.drawString("Out of lives", F.getWidth()/2-fm.stringWidth("Out of lives")/2, F.getHeight()/3);
 }
 void draw_victory_screen(){
 	g2D.setColor(new Color(255,165,0));
 	g2D.fillRect(0, 0, F.getWidth(), F.getHeight());
 	g2D.setFont(title_font);
 	fm = g2D.getFontMetrics();
 	g2D.setColor(new Color(255,215,0));
 	g2D.drawString("Victory!",F.getWidth()/2-fm.stringWidth("Victory!")/2,F.getHeight()/3);
 	g2D.setFont(font);
 	fm = F.getFontMetrics(font);
 	vm.draw_button(g2D,vm.mouse_in(mp), fm,new Color(255,165,0));
 	vq.draw_button(g2D,vq.mouse_in(mp), fm,new Color(255,165,0));
 }
 void draw_hearts(){
  for(int i = 0; i < player.lives;i++){
   g2D.drawImage(heart.getImage(),330+50*i,50,null);
  }
 }
 void draw_ammo(){
  for(int i = 0; i < player.ammo;i++){
   g2D.drawImage(star.getImage(),50+50*i,120,null);
  }
 }
 void draw_inventory(){
  
  for(int i = 0; i < inventory.size();i++){
   Item it = inventory.get(i);
   g2D.setColor(Color.gray);
   g2D.setStroke(new BasicStroke(5));
   g2D.drawRect(40+70*(item_select), 170, 70, 70);
   g2D.setStroke(new BasicStroke(1));
   g2D.setColor(Color.white);
   g2D.setFont(inv_font);
   
   if(inventory.get(item_select).ID == 20) player.putSword(true);//if holding sword
   else player.putSword(false);
   if(it.ID == 17){//bomb item
    if(player.getBombs() == 0){
     if(item_select == inventory.size()-1) item_select--;
     inventory.remove(i);
     if(item_select < 0) item_select = 0;
    }
    else{
     g2D.drawImage(it.sprites.get(0).getImage(), 50+70*i, 170,null);
     g2D.drawString(""+player.bombs,80+70*i,230);
    }
   }
   if(it.ID == 18){//tele bomb item
    if(player.getTeleBombs() == 0){
     if(item_select == inventory.size()-1) item_select--;
     inventory.remove(i);
     if(item_select < 0) item_select = 0;
    }
    else{
     g2D.drawImage(it.sprites.get(0).getImage(), 50+70*i, 170,null);
     g2D.drawString(""+player.tele_bombs,80+70*i,230);
    }
   }
   if(it.ID == 20){
    if(!player.hasSword()){
     if(item_select == inventory.size()-1) item_select--;
     inventory.remove(i);
     if(item_select < 0) item_select = 0;
    }
    else{
     g2D.drawImage(inv_katana.getImage(), 50+70*i, 180,null);
     it.draw_health(g2D,50+70*i,180);
    }
   }
  }
 }
 void startMusic(){
     try {
      
         audioInputStream =  AudioSystem.getAudioInputStream(new BufferedInputStream((Main.class.getResourceAsStream("/stuff/music.wav"))));
         clip = AudioSystem.getClip();
         clip.open(audioInputStream);
         clip.start();
     } catch(Exception ex) {
         System.out.println("Error with playing sound.");
         ex.printStackTrace();
     } 
 }
 private ImageIcon loadImage(String name){
	 
	 try {
		return new ImageIcon(ImageIO.read(Main.class.getResourceAsStream(name)));
	} 
	 catch (IOException e) {
		System.out.println("Couldn't load " + name);
		e.printStackTrace();
		return null;
	}
 }
// InputStream is = Main.class.getResourceAsStream("font.ttf");
//	return Font.createFont(Font.TRUETYPE_FONT, is);
 private Font loadFont(String name){
	 try {
		 return Font.createFont(Font.TRUETYPE_FONT,Main.class.getResourceAsStream(name));
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
 }
 public static void main(String[] args) {
	 javax.swing.SwingUtilities.invokeLater(new Runnable(){
		public void run(){
		 try {
			new Main();
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	 });
 }

}

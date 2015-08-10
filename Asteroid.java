/*
Asteroid Clone Thing

Written by Bradley Black

Started: 16/6/2015
Finished:

Ship sprite is from: http://flylib.com/books/1/122/1/html/2/images/fig11-5.jpg

R:\DigiTech\JDK86\compall.bat "$(FULL_CURRENT_PATH)" "$(CURRENT_DIRECTORY)" "$(NAME_PART)"

Then Press Run
----------------------------------------------------------------------------------------------------------------------
Use this file as the starting point for all of your programs in the programming topic. 

*/
import java.awt.image.BufferStrategy;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.applet.Applet;
import java.awt.Graphics2D;
import java.util.ArrayList;	

import java.awt.geom.Point2D;	

public class Asteroid extends JFrame implements ActionListener{
	Canvas canvas;
	Button startButton, stopButton;
	boolean animationRunning = true;
	private BufferStrategy	strategy;
	
	Image shipImg = Toolkit.getDefaultToolkit().createImage("ship.png");//Sprite for the ship.
	//Image shipImg = Toolkit.getDefaultToolkit().createImage("husky.png");//Sprite for the ship.
	//Image asteroidImg = Toolkit.getDefaultToolkit().createImage("asteroid.png");//Sprite for the asteroid.
	//Image smllastImg = Toolkit.getDefaultToolkit().createImage("small.png");//Sprite for the ship.

	boolean shooting = false;

	final static int START_UP=0;
	final static int IN_GAME=1;
	final static int GAME_OVER=2;
	final static int PAUSE=3;
	int gameState=START_UP;
	
	int score = 0;
	int highScore = 0;
	String name = "ABCD";
	
	final static int MIN_SHOT_COOLDOWN=50;
	final static int MAX_SHOT_COOLDOWN=1000;
	int shotCoolDown;
	
	private final static int SIZE = 10;
	
	long timeForNextShot;
	
	TextField tfFPS = new TextField("10");
	TextField tfAsteroidSpawn = new TextField("01");
	TextField tfAsteroidSpeed = new TextField("1");
	TextField tfShootRate = new TextField("50");
		
	final static int FPS_MIN = 10;
	final static int FPS_MAX = 30;
	int fps = FPS_MIN;
	
	int canShoot = 0;
	
	int asteroidSpawn = 1000;
	int setAsteroidSpawn = 1;
	long timeForNextAsteroid;
	final static int ASTEROID_SPAWN_MIN = 1; 
	final static int ASTEROID_SPAWN_MAX = 10;
	
	int asteroidHorzSpd;
	final static int ASTEROID_HORIZONTAL_SPD_MIN = 1;
	final static int ASTEROID_HORIZONTAL_SPD_MAX = 5;
	
	int shootRate;
	final static int SHOOT_RATE_MIN = 10;
	final static int SHOOT_RATE_MAX = 500;

	final static int LARGE_ASTEROID_SIZE = 30;//100
	final static int MEDIUM_ASTEROID_SIZE = 10;//30
	final static int SMALL_ASTEROID_SIZE = 5;//10
	
	Ship ship; //Calling the ship class file
	ArrayList<Enemy> largeAsteroid = new ArrayList<Enemy>();//Large Asteroids
	ArrayList<Enemy> medAsteroid = new ArrayList<Enemy>();//Medium Asteroids
	ArrayList<Enemy> smlAsteroid = new ArrayList<Enemy>();//Small Asteroids
	ArrayList<Shot> shots = new ArrayList<Shot>();
	
	public static void main(String s[]) {
		new Asteroid();
	}
	static {
		//System.setProperty("sun.java2d.transaccel", "True");
		//System.setProperty("sun.java2d.trace", "timestamp,log,count");
		//System.setProperty("sun.java2d.opengl", "True");
		//System.setProperty("sun.java2d.d3d", "True");
		System.setProperty("sun.java2d.ddforcevram", "True");
	}
	
	public Asteroid(){
		
		canvas = new Canvas();
		canvas.setSize(1200, 500);
		
		Panel buttonPanel = new Panel();
		buttonPanel.setLayout(new FlowLayout());
		startButton = new Button("Start");
		stopButton = new Button("Stop");
		buttonPanel.add(startButton);
		buttonPanel.add(stopButton);
		buttonPanel.add(new Label("FPS"));
		buttonPanel.add(tfFPS);
		buttonPanel.add(new Label("Asteroid Spawn Time"));
		buttonPanel.add(tfAsteroidSpawn);
		buttonPanel.add(new Label("Asteroid Speed"));
		buttonPanel.add(tfAsteroidSpeed);
		buttonPanel.add(new Label("Shoot Rate"));
		buttonPanel.add(tfShootRate);
		/*
		buttonPanel.add(new Label("Asteroid Spawn Time"));
		buttonPanel.add(tfEnemySpeed);
		*/
		startButton.addActionListener( this);
		stopButton.addActionListener( this);
		// Add a window listner for close button
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		canvas.addMouseMotionListener(new MouseMotionListener(){
            public void mouseMoved(MouseEvent e) {
				//objectC.x=e.getX();
				//objectC.y=e.getY();
			}

			public void mouseDragged(MouseEvent e) {
			   //objectC.x=e.getX();
			   //objectC.y=e.getY();
			}     
        });
        canvas.addKeyListener(new KeyListener(){
            public void keyTyped(KeyEvent e) {}

			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				switch(key){
					case KeyEvent.VK_W: ship.setYdir(-ship.getSpeed()); break;
					case KeyEvent.VK_A: ship.setXdir(-ship.getSpeed()); break;
					case KeyEvent.VK_S: ship.setYdir(ship.getSpeed()); break;
					case KeyEvent.VK_D: ship.setXdir(ship.getSpeed()); break;
					case KeyEvent.VK_SPACE: doSpaceKey(); break;
					case KeyEvent.VK_R: resetGame(); break;
					//case KeyEvent.VK_P: handlePause(); break;
				}
			}
			
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				switch(key){
					case KeyEvent.VK_W: 
					case KeyEvent.VK_S: ship.setYdir(0); break;
					case KeyEvent.VK_A: 
					case KeyEvent.VK_D: ship.setXdir(0); break;
					case KeyEvent.VK_SPACE: shooting = false; 
					//case KeyEvent.VK_SPACE: shooting=false; break;
				}
			}     
        });
		this.getContentPane().add(buttonPanel, BorderLayout.NORTH);
		//this.getContentPane().add(buttonPanel, BorderLayout.WEST);
		this.getContentPane().add(canvas, BorderLayout.CENTER);
		this.setTitle("Asteroids");
		this.pack();
		this.setVisible(true);
		canvas.createBufferStrategy(2);
		strategy = canvas.getBufferStrategy();
		ship = new Ship(50, canvas.getHeight()/2);
		new AnimationThread(this);
	}
	
	public void doSpaceKey(){
		if (gameState == IN_GAME){
			shooting = true;
		}else if (gameState == GAME_OVER){
			resetGame();
		}
	}
	
	/*////////////////////////////////////////////////////////*/
	//This is the update game function.
	//It deals with almost everything in the game that need to 
	//be constatly updated such as moving, checking collision.
	//
	/*////////////////////////////////////////////////////////*/
	/*
 .-.---------------------------------.-.
((o))                                   )
 \U/_______          _____         ____/
   |                                  |
   |This is the update game function. | 
   |It deals with almost everything   |
   |in the game that needs to         |
   |be constatly updated such as      |
   |moving, checking collision.       |                       
   |__    _______    __  ____       __|
  /A\                                  \
 ((o))                                  )
  '-'----------------------------------'
	*/
	public void updateGame(){
		
		if (gameState != IN_GAME){
			ship.setXdir(0);
			ship.setYdir(0);
		}
	
		doesShipMoveOffScreen();
		ship.moveShip();
		
		if(System.currentTimeMillis() > timeForNextAsteroid){
			largeAsteroid.add(new Enemy(canvas.getWidth() + LARGE_ASTEROID_SIZE, randomInteger(0, canvas.getHeight()), asteroidHorzSpd * -1, randomInteger(-2, 2)));
			timeForNextAsteroid = System.currentTimeMillis() + asteroidSpawn;
			asteroidSpawn -= setAsteroidSpawn;
		}

		//Move the large asteroids. 
		for (int i = 0; i < largeAsteroid.size(); i++){
			largeAsteroid.get(i).moveAsteroid();
			if (largeAsteroid.get(i).getX() < 0 || largeAsteroid.get(i).getY() < 0 - LARGE_ASTEROID_SIZE || largeAsteroid.get(i).getY() > canvas.getHeight()){
				//If a large asteroid has moved off of the playing screen then remove it from the array list.
				largeAsteroid.remove(i);
			}
		}
		//Move medium asteroids.
		for (int i = 0; i < medAsteroid.size(); i++){
			medAsteroid.get(i).moveAsteroid();
			if (medAsteroid.get(i).getX() < 0 || medAsteroid.get(i).getY() < 0 - MEDIUM_ASTEROID_SIZE || medAsteroid.get(i).getY() > canvas.getHeight()){
				//If a medium asteroid has moved off of the playing screen then remove it from the array list.
				medAsteroid.remove(i);
			}
		}
		
		//Move small asteroids.
		for (int i = 0; i < smlAsteroid.size() - 1; i++){
			smlAsteroid.get(i).moveAsteroid();
			if (smlAsteroid.get(i).getX() < 0 || smlAsteroid.get(i).getY() < 0 - SMALL_ASTEROID_SIZE || smlAsteroid.get(i).getY() > canvas.getHeight()){
				//If a small asteroid has moved off of the playing screen then remove it from the array list.
				smlAsteroid.remove(i);
			}
		}
		
		if(shooting){
			canShoot++;
		}else{
			canShoot--;
		}
		
		//If there are active shots then move them.
		for (int i = 0; i < shots.size(); i++){
			shots.get(i).moveShot();
			if (shots.get(i).getX() > canvas.getWidth()){//If active shots move off screen then remove them from array list. 
				shots.remove(i);
			}
		}
		
		if(System.currentTimeMillis() > timeForNextShot && shooting && canShoot <=0){
			shots.add(new Shot(ship.getX() + ship.getSize(), ship.getY() + ship.getSize() / 2));
			/*
			if(timeShootBoosterExpires > System.currentTimeMillis()){
				timeForNextShot = System.currentTimeMillis() + shotCooldown/2;
			}
			else{
				timeForNextShot = System.currentTimeMillis() + shotCooldown;
			}
			*/	
		}
		
		//Functions checking for collisions.
		hasAsteroidCollided();
		hasShotCollided();
		hasShipCollided();
		
	}

	/*////////////////////////////////////////////////////////*/
	//This is the doesShipMoveOffScreen function
	//This function checks to see if the user's ship ever 
	//moves off the screen. 
	//
	/*////////////////////////////////////////////////////////*/
	
	public void doesShipMoveOffScreen(){
		if (ship.getY() <= 0){
			ship.setY(canvas.getHeight() - ship.getSize());
		}else if (ship.getY() >= canvas.getHeight()){
			ship.setY(0);
		}else if (ship.getXdir() < 0 && ship.getX() <= 3){//Does the ship's x reach less than 0 on the canvas?
			ship.setXdir(0);//Set Xdir to 0 to stop the ship from moving any further than 0.
		}
	}
		
	/*////////////////////////////////////////////////////////*/
	//This is the hasShipCollided function
	//This function checks to see if the user's ship ever 
	//collides with an asteroid. Since I have three asteroids
	//it checks each asteroid array to see if the ship and the
	//asteroid has collided. 
	/*////////////////////////////////////////////////////////*/	
	
	public void hasShipCollided(){
		for (int i = 0; i < largeAsteroid.size(); i++){
			if (Math.hypot(ship.getX() - largeAsteroid.get(i).getX(), ship.getY() - largeAsteroid.get(i).getY()) < LARGE_ASTEROID_SIZE){
				doGameOver();
				return;
			}
		}
		for (int i = 0; i < medAsteroid.size(); i++){
			if (Math.hypot(ship.getX() - medAsteroid.get(i).getX(), ship.getY() - medAsteroid.get(i).getY()) < MEDIUM_ASTEROID_SIZE){
				doGameOver();
				return;
			}
		}
		for (int i = 0; i < smlAsteroid.size(); i++){
			if (Math.hypot(ship.getX() - smlAsteroid.get(i).getX(), ship.getY() - smlAsteroid.get(i).getY()) < SMALL_ASTEROID_SIZE){
				doGameOver();
				return;
			}
		}
	}
	
	public void hasShotCollided(){
		for (int i = 0; i <= shots.size() - 1; i++){
			for (int j = 0; j < largeAsteroid.size() - 1; j++){
				if(Math.hypot(shots.get(i).getX() - largeAsteroid.get(j).getX(), shots.get(i).getY() - largeAsteroid.get(j).getY()) < LARGE_ASTEROID_SIZE){
					shots.remove(i);
					
					//Need to add three new medium sized asteroids before the large asteroid has been removed. 
					for (int k = 0; k < 3; k++){
						medAsteroid.add(new Enemy(largeAsteroid.get(j).getX(), largeAsteroid.get(j).getY(), asteroidHorzSpd * -1, randomInteger(-2, 2)));
					}
					
					largeAsteroid.remove(j);
					asteroidSpawn--;
					break;
				}
			}
		}
		for (int i = 0; i <= shots.size() - 1; i++){
			for (int j = 0; j < medAsteroid.size() - 1; j++){
				if(Math.hypot(shots.get(i).getX() - medAsteroid.get(j).getX(), shots.get(i).getY() - medAsteroid.get(j).getY()) < MEDIUM_ASTEROID_SIZE){
					shots.remove(i);
					
					//Need to add two new small sized asteroids before the medium asteroid has been removed. 
					for (int k = 0; k < 2; k++){
						smlAsteroid.add(new Enemy(medAsteroid.get(j).getX(), medAsteroid.get(j).getY(), asteroidHorzSpd * -1, randomInteger(-2, 2)));
					}
					medAsteroid.remove(j);
					setAsteroidSpawn--;
					break;
				}
			}
		}
		for (int i = 0; i < shots.size() - 1; i++){
			for (int j = 0; j < smlAsteroid.size() - 1; j++){
				if(Math.hypot(shots.get(i).getX() - smlAsteroid.get(j).getX(), shots.get(i).getY() - smlAsteroid.get(j).getY()) < SMALL_ASTEROID_SIZE){
					shots.remove(i);
					smlAsteroid.remove(j);
					//asteroidSpawn--;
					score++;
					break;
				}
			}
		}
	}
	
	public void hasAsteroidCollided(){
		
		for (int i = 0; i < largeAsteroid.size(); i++){
			int k = i + 1;
			if (k >= largeAsteroid.size()) k = 0;
			if(Math.hypot(largeAsteroid.get(i).getX() - largeAsteroid.get(k).getX(), largeAsteroid.get(i).getY() - largeAsteroid.get(k).getY()) < LARGE_ASTEROID_SIZE/2){
				largeAsteroid.get(i).setYdir(largeAsteroid.get(i).getYdir() * -1);
				largeAsteroid.get(k).setYdir(largeAsteroid.get(k).getYdir() * -1);
			}
		}
	}
	
	
	
	public void doGameOver(){
		if (score > highScore){
			highScore = score;
			name = JOptionPane.showInputDialog("Please input your name: ");//Makes a pop up window that allows the user to input their name.
		}
		score = 0;
		gameState = GAME_OVER;
	}
	
	public void resetGame(){
		fps = FPS_MIN;
		String errors="";
		try{
			fps = Integer.parseInt(tfFPS.getText());
			if(fps < FPS_MIN || fps > FPS_MAX){
				errors+="FPS is out of bounds! Enter a value less than 30 and more than 10.\n";
			}
		}catch(NumberFormatException nfe){
			errors+="FPS is not a number!\n";
		}
		try{
			setAsteroidSpawn = Integer.parseInt(tfAsteroidSpawn.getText());
			if(setAsteroidSpawn < ASTEROID_SPAWN_MIN || setAsteroidSpawn > ASTEROID_SPAWN_MAX){
				errors+="Asteroid Spawn is out of bounds! Enter a value less than 10 and more than 11.\n";
			}
		}catch(NumberFormatException nfe){
			errors+="Asteroid Spawn is not a number!\n";
		}
		try{
			asteroidHorzSpd = Integer.parseInt(tfAsteroidSpeed.getText());
			if(asteroidHorzSpd < ASTEROID_HORIZONTAL_SPD_MIN|| asteroidHorzSpd > ASTEROID_HORIZONTAL_SPD_MAX){
				errors+="Asteroid Speed is out of bounds! Enter a value less than 5 and more than 1.\n";
			}
		}catch(NumberFormatException nfe){
			errors+="Asteroid Speed is not a number!\n";
		}
		try{
			shotCoolDown = Integer.parseInt(tfShootRate.getText());
			if(shotCoolDown < MIN_SHOT_COOLDOWN|| shotCoolDown > MAX_SHOT_COOLDOWN){
				errors+="Shoot Rate is out of bounds! Enter a value less than 1000 and more than 50.\n";
			}
		}catch(NumberFormatException nfe){
			errors+="Asteroid Speed is not a number!\n";
		}

		if (errors.length() > 0){
			JOptionPane.showMessageDialog(this,errors);
		}else{
			
			//setAsteroidSpawn = 1000;
			largeAsteroid.clear();
			medAsteroid.clear();
			smlAsteroid.clear();

			shots.clear();

			ship = new Ship(50, canvas.getHeight()/2);
			animationRunning=true;
			gameState = IN_GAME;

		}
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == startButton){
			resetGame();
		}else if(e.getSource() == stopButton){
			animationRunning=false;
		}
	}
		
	public int randomInteger(int min, int max){
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
	public void gameOverCanvas(){
		Graphics2D g= (Graphics2D)strategy.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
		g.setColor(Color.green);		
		g.setFont(new Font("TimesRoman", Font.PLAIN, 32)); 
		g.drawString("GAMEOVER",  canvas.getWidth()/2 - 50, 30);
		g.drawString("Score: "+score, canvas.getWidth()/2 - 50, 130);
		g.drawString("High Score: "+highScore+"  "+name, canvas.getWidth()/2 - 50, 230);
		g.drawString("Press Space or 'R' to restart", canvas.getWidth()/2 - 50, 330);
		g.dispose();
		strategy.show();
		Toolkit.getDefaultToolkit().sync();
	}
	
	public void updateCanvas(){
		Graphics2D g= (Graphics2D)strategy.getDrawGraphics();  
		g.setColor(Color.black);
		g.fillRect(0,0,canvas.getWidth(), canvas.getHeight()); //clears screen
		g.setColor(Color.white);
		
		//Draw ship
		g.drawImage(shipImg, ship.getX(), ship.getY(), ship.getSize(), ship.getSize(), null);
		//g.fillRect(ship.getX(), ship.getY(), ship.getSize(), ship.getSize());

		//Draw shots
		g.setColor(Color.white);
		for (int i = 0; i < shots.size(); i++){
			g.drawLine(shots.get(i).getX(), shots.get(i).getY(), shots.get(i).getX() + 20, shots.get(i).getY());
		}
		
		//Draw Large Asteroids
		g.setColor(Color.white);
		for (int i = 0; i < largeAsteroid.size(); i++){
			g.drawOval(largeAsteroid.get(i).getX(), largeAsteroid.get(i).getY(), LARGE_ASTEROID_SIZE, LARGE_ASTEROID_SIZE);
			//g.drawImage(asteroidImg, largeAsteroid.get(i).getX(), largeAsteroid.get(i).getY(), LARGE_ASTEROID_SIZE, LARGE_ASTEROID_SIZE, null);
		}
		
		//Draw Medium Asteroids
		for (int i = 0; i < medAsteroid.size(); i++){
			g.drawOval(medAsteroid.get(i).getX(), medAsteroid.get(i).getY(), MEDIUM_ASTEROID_SIZE, MEDIUM_ASTEROID_SIZE);
			//smllastImg
			//g.drawImage(smllastImg, medAsteroid.get(i).getX(), medAsteroid.get(i).getY(), MEDIUM_ASTEROID_SIZE, MEDIUM_ASTEROID_SIZE, null);
		}
		
		//Draw Small Asteroids
		for (int i = 0; i < smlAsteroid.size(); i++){
			g.drawOval(smlAsteroid.get(i).getX(), smlAsteroid.get(i).getY(), SMALL_ASTEROID_SIZE, SMALL_ASTEROID_SIZE);
			//g.drawImage(smllastImg, smlAsteroid.get(i).getX(), smlAsteroid.get(i).getY(), SMALL_ASTEROID_SIZE, SMALL_ASTEROID_SIZE, null);
		}
		
		g.drawString("Score:"+score, canvas.getWidth()-100, canvas.getHeight()-10);
		g.drawString("Gun Charge:"+canShoot * -1, 10, canvas.getHeight()-10);
		
		g.dispose();
		strategy.show();
		Toolkit.getDefaultToolkit().sync();
	}
	
	class AnimationThread extends Thread{
		Asteroid at;
		
		public AnimationThread(Asteroid _at){
			at=_at;
			start();
		}
		
		public void run(){
			int FPS = 30;
			int SKIP_TICKS = 1000 / FPS;
			long next_game_tick = System.currentTimeMillis();
			long sleep_time = 0;
			while(true){
				if(gameState==IN_GAME){
					at.updateGame();
					at.updateCanvas();
				//}else if(gameState==PAUSE){
				//	at.pausedCanvas();
				}else if(gameState==GAME_OVER){
					//System.out.println("QWERTY");
					at.gameOverCanvas();
				}
				next_game_tick += SKIP_TICKS;
				sleep_time = next_game_tick - System.currentTimeMillis();
				if( sleep_time >= 0 ) {
					try{
						sleep(sleep_time); 
					}catch(InterruptedException ie){}
				}else{
					next_game_tick = System.currentTimeMillis();
				}					
			}
		}
	}
}

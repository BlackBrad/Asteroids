/*
Asteroid Clone

Started: 16/6/2015
Finished:

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

public class Asteroid extends JFrame implements ActionListener{
	Canvas canvas;
	Button startButton, stopButton;
	boolean animationRunning = true;
	private BufferStrategy	strategy;
	
	boolean shooting = false;

	final static int START_UP=0;
	final static int IN_GAME=1;
	final static int GAME_OVER=2;
	final static int PAUSE=3;
	int gameState=START_UP;
	
	private final static int SIZE = 10;
	
	TextField tfFPS = new TextField("10");
	TextField tfAsteroidSpawn = new TextField("01");
	TextField tfAsteroidSpeed = new TextField("1");
	TextField tfShootRate = new TextField("10");
		
	int fps;
	final static int FPS_MIN = 10;
	final static int FPS_MAX = 30;
	
	int asteroidSpawn = 1;
	long timeForNextAsteroid;
	final static int ASTEROID_SPAWN_MIN = 1;
	final static int ASTEROID_SPAWN_MAX = 10;
	
	int asteroidHorzSpd;
	final static int ASTEROID_HORIZONTAL_SPD_MIN = 1;
	final static int ASTEROID_HORIZONTAL_SPD_MAX = 5;
	
	int shootRate;
	final static int SHOOT_RATE_MIN = 10;
	final static int SHOOT_RATE_MAX = 500;


	
	final static int LARGE_ASTEROID_SIZE = 30;
	final static int MEDIUM_ASTEROID_SIZE = 10;
	final static int SMALL_ASTEROID_SIZE = 5;
	
	Ship ship; //Calling the ship class file
	ArrayList<Enemy> largeAsteroid = new ArrayList<Enemy>();//Large Asteroids
	ArrayList<Enemy> medAsteroid = new ArrayList<Enemy>();//Medium Asteroids
	ArrayList<Enemy> smlAsteroid = new ArrayList<Enemy>();//Small Asteroids
	ArrayList<Shot> shots = new ArrayList<Shot>();
	
	public static void main(String s[]) {
		new Asteroid();
	}
	static {
		System.setProperty("sun.java2d.transaccel", "True");
		// System.setProperty("sun.java2d.trace", "timestamp,log,count");
		System.setProperty("sun.java2d.opengl", "True");
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

	public void updateGame(){
		
		if (gameState != IN_GAME){
			ship.setXdir(0);
			ship.setYdir(0);
		}

		ship.moveShip();
		
		if (shooting == true){
			shots.add(new Shot(ship.getX(), ship.getY()));
			shooting = false;
		}

		if(System.currentTimeMillis() > timeForNextAsteroid){
			largeAsteroid.add(new Enemy(canvas.getWidth() + LARGE_ASTEROID_SIZE, randomInteger(0, canvas.getHeight()), asteroidHorzSpd * -1, randomInteger(-2, 2)));
			timeForNextAsteroid = System.currentTimeMillis() + asteroidSpawn * 1000;
			/*
			for (int i = 0; i < largeAsteroid.size(); i++){
				int k = i + 1;
				if (k >= largeAsteroid.size()) k = 0;
				if (largeAsteroid.get(i).getY() <= largeAsteroid.get(k).getY() + LARGE_ASTEROID_SIZE){
					largeAsteroid.remove(i);
					largeAsteroid.add(new Enemy(canvas.getWidth() + LARGE_ASTEROID_SIZE, randomInteger(0, canvas.getHeight()), asteroidHorzSpd * -1, randomInteger(-2, 2)));

				}
			}
			*/
			//timeForNextAsteroid = System.currentTimeMillis() + asteroidSpawn;
		}

		for (int i = 0; i < largeAsteroid.size(); i++){
			largeAsteroid.get(i).moveAsteroid();
			if (largeAsteroid.get(i).getX() < 0 || largeAsteroid.get(i).getY() < 0 - LARGE_ASTEROID_SIZE || largeAsteroid.get(i).getY() > canvas.getHeight()){
				largeAsteroid.remove(i);
			}
		}
	
		for (int i = 0; i < medAsteroid.size(); i++){
			medAsteroid.get(i).moveAsteroid();
		}
		
		for (int i = 0; i < shots.size(); i++){
			shots.get(i).moveShot();
		}
		hasAsteroidCollided();
		hasShotCollided();
	}

	public void splitAsteroid(){
		
	}
	
	public void hasShotCollided(){
		for (int i = 0; i <= shots.size() - 1; i++){
			for (int j = 0; j < largeAsteroid.size(); j++){
				if(Math.hypot(shots.get(i).getX() - largeAsteroid.get(j).getX(), shots.get(i).getY() - largeAsteroid.get(j).getY()) < LARGE_ASTEROID_SIZE){
					shots.remove(i);
					
					//Need to add three new medium sized asteroids before the large asteroid has been removed. 
					for (int k = 0; k < 3; k++){
						medAsteroid.add(new Enemy(largeAsteroid.get(j).getX(), largeAsteroid.get(j).getY(), asteroidHorzSpd * -1, randomInteger(-2, 2)));
					}
					largeAsteroid.remove(j);
					//splitAsteroid();
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

	public void resetGame(){
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
			asteroidSpawn = Integer.parseInt(tfAsteroidSpawn.getText());
			if(asteroidSpawn < ASTEROID_SPAWN_MIN || asteroidSpawn > ASTEROID_SPAWN_MAX){
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
		if (errors.length() > 0){
			JOptionPane.showMessageDialog(this,errors);
		}else{
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
	
	public void updateCanvas(){
		Graphics2D g= (Graphics2D)strategy.getDrawGraphics();  
		g.setColor(Color.black);
		g.fillRect(0,0,canvas.getWidth(), canvas.getHeight()); //clears screen
		g.setColor(Color.white);
		g.fillRect(ship.getX(), ship.getY(), ship.getSize(), ship.getSize());

		//Draw shots
		g.setColor(Color.red);
		for (int i = 0; i < shots.size(); i++){
			g.drawLine(shots.get(i).getX(), shots.get(i).getY(), shots.get(i).getX() + 10, shots.get(i).getY());
		}

		//Draw Large Asteroids
		g.setColor(Color.white);
		for (int i = 0; i < largeAsteroid.size(); i++){
			g.drawOval(largeAsteroid.get(i).getX(), largeAsteroid.get(i).getY(), LARGE_ASTEROID_SIZE, LARGE_ASTEROID_SIZE);
		}
		
		//Draw Medium Asteroids
		for (int i = 0; i < medAsteroid.size(); i++){
			g.drawOval(medAsteroid.get(i).getX(), medAsteroid.get(i).getY(), MEDIUM_ASTEROID_SIZE, MEDIUM_ASTEROID_SIZE);
		}

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
				if(animationRunning){
					updateGame();
					at.updateCanvas();
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
}

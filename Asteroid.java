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
	TextField tfAsteroidSpawn = new TextField("1");
	TextField tfAsteroidSpeed = new TextField("1");
	TextField tfShootRate = new TextField("10");
	
	
	int fps;
	final static int FPS_MIN = 10;
	final static int FPS_MAX = 30;
	
	int asteroidSpawn;
	final static int ASTEROID_SPAWN_MIN = 1;
	final static int ASTEROID_SPAWN_MAX = 10;
	
	int asteroidHorzSpd;
	final static int ASTEROID_HORIZONTAL_SPD_MIN = 1;
	final static int ASTEROID_HORIZONTAL_SPD_MAX = 5;
	
	int shootRate;
	final static int SHOOT_RATE_MIN = 10;
	final static int SHOOT_RATE_MAX = 500;
	
	Ship ship; //Calling the ship class file
	ArrayList<Enemy> asteroid = new ArrayList<Enemy>();
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
		}
	}

	public void updateGame(){
		ship.moveShip();
		if (shooting == true){
			shots.add(new Shot(ship.getX(), ship.getY()));
			shooting = false;
		}
		for (int i = 0; i < shots.size(); i++){
			shots.get(i).moveShot();
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
				errors+="Asteroid Spawn is out of bounds! Enter a value less than 30 and more than 10.\n";
			}
		}catch(NumberFormatException nfe){
			errors+="FPS is not a number!\n";
		}
		if (errors.length() > 0){
			JOptionPane.showMessageDialog(this,errors);
		}else{
			asteroid.clear();
			ship = new Ship(50, canvas.getHeight()/2);
			animationRunning=true;
		}
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == startButton){
			resetGame();
			gameState = IN_GAME;
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

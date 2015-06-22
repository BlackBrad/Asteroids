class Ship{
	private int x, y, xdir, ydir;
	private final static int SPEED = 1;
	private final static int SIZE = 10;
	
	public Ship(int x, int y){
		this.x = x;
		this.y = y;
		xdir = 0;
		ydir = 0;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getSpeed(){
		return SPEED;
	}
	
	public int getSize(){
		return SIZE;
	}	

	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}

	public void setXdir(int xdir){
		this.xdir = xdir;
	}

	public void setYdir(int ydir){
		this.ydir = ydir;
	}

	public void moveShip(){
		x += xdir;
		y += ydir;
	}
}
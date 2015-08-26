class Enemy{
	private int x, y, xdir, ydir, size;

	//Constructor
	public Enemy(int x, int y, int xdir, int ydir, int size){
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
		this.size = size;
	}

	//Getters	
	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int getYdir(){
		return ydir;
	}
	
	public int getSize(){
		return size;
	}

	//Setters
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}

	public void setYdir(int ydir){
		this.ydir = ydir;
	}

	public void moveAsteroid(){
		x += xdir;
		y += ydir;
	}
}
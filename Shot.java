class Shot{
	int x, y;
	final static int xdir = 5;

	public Shot(int x, int y){
		this.x = x;
		this.y = y;
	}

	//Getters
	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	//Setters
	public void setX(int x){
		this.x = x;
	}

	public void moveShot(){
		x += xdir;
	}
}
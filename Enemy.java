class Enemy{
	int x, y, xdir, ydir;
	
	public Enemy(int x, int y, int xdir, int ydir){
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int getYdir(){
		return ydir;
	}

	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	/*
	public void setXdir(int xdir){
		this.xdir = xdir;
	}
	*/
	public void setYdir(int ydir){
		this.ydir = ydir;
	}

	public void moveAsteroid(){
		x += xdir;
		y += ydir;
	}
}
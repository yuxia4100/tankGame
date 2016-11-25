package tankGame;

import java.util.Vector;

class Tank {
	int x = 0;
	int y = 0;
	//set tank speed
	int speed = 1;
	int color;
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}



	// 0 up, 1 right, 2 down, 3 left
	int direct = 0;
	
	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	
	
	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

// Player
class Hero extends Tank {
	boolean isLive = true;
	Vector<Shot> ss = new Vector<>();
	Shot s = null;
	int speed = 1;
	public Hero (int x, int y) {
		super(x, y);
	}
	
	public void moveUp() {
		y -= this.speed;
	}
	
	public void moveRight() {
		x += this.speed;
	}
	
	public void moveDown() {
		y += this.speed;
	}
	
	public void moveLeft() {
		x -= this.speed;
	}
	
	public void shotEnemy() {
		
		switch(this.direct){
		case 0:
			s = new Shot(x + 10, y, 0);
			ss.add(s);
			break;
		case 1:
			s = new Shot(x + 30, y + 10, 1);
			ss.add(s);
			break;	
		case 2:
			s = new Shot(x + 10, y + 30, 2);
			ss.add(s);
			break;
		case 3:
			s = new Shot(x, y + 10, 3);
			ss.add(s);
			break;	
		}
		
		Thread t = new Thread(s);
		t.start();
	}
}

// Enemy
class EnemyTank extends Tank implements Runnable{
	
	boolean isLive = true;
	int times = 0;
	Vector<Shot> ss = new Vector<>();
	public EnemyTank(int x, int y) {
		super(x, y);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while (true) {
			
			switch (this.direct) {
				case 0:
					for (int i = 0; i < 30; i++) {
						if (y > 0) {
							y -= speed;
						}
						try {
							Thread.sleep(50);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					break;
				case 1:
					for (int i = 0; i < 30; i++) {
						if (x < 400) {
							x += speed;
						}
						try {
							Thread.sleep(50);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					break;
				case 2:
					for (int i = 0; i < 30; i++) {
						if (y < 300) {
							y += speed;
						}
						try {
							Thread.sleep(50);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					break;
				case 3:
					for (int i = 0; i < 30; i++) {
						if (x > 0) {
							x -= speed;
						}	
						try {
							Thread.sleep(50);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					break;
					
			}
			
			times++;
			if (times % 2 == 0) {
				
					if (isLive) {
						if (ss.size() < 5) {
							
							Shot s = null;
							switch(this.direct) {
							case 0:
								s = new Shot(this.x + 10, this.y, 0);
								ss.add(s);
								break;
							case 1:
								s = new Shot(this.x + 30, this.y + 10, 1);
								ss.add(s);
								break;	
							case 2:
								s = new Shot(this.x + 10, this.y + 30, 2);
								ss.add(s);
								break;
							case 3:
								s = new Shot(this.x, this.y + 10, 3);
								ss.add(s);
								break;	
							
							}
							
							Thread t = new Thread(s);
							t.start();
						}
					
					
				} 
			}
			
			this.direct = (int) (Math.random() * 4);
			
			if (this.isLive == false) {
				
				break;
			}
			
			
			
			
		}
		
		
	}
}

//Shell
class Shot implements Runnable{
	int x;
	int y;
	int direct;
	int speed = 1;
	boolean isLive = true;
	
	public Shot(int x, int y, int direct) {
		this.x = x;
		this.y = y;
		this.direct = direct;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			switch(direct) {
				case 0:
					y -= speed;
					break;
				case 1:
					x += speed;
					break;
				case 2:
					y += speed;
					break;
				case 3:
					x -= speed;
					break;
			}
			
	
			if (x < 0 || x > 400 || y < 0 || y > 300) {
				this.isLive = false;
				break;
			}
		}
	}
	
}

class  Bomb {
	int x;
	int y;
	int life = 10;
	boolean isLive = true;
	public Bomb(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void lifeDecrease() {
		if (life > 0) {
			life--;
		} else {
			this.isLive = false;
		}
	}
}

package tankGame;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Vector;

class Recorder {
	private static int enemyNum = 20;
	private static int playerNum = 3;
	private static int totalScore = 0;
	private static FileWriter fw = null;
	private static BufferedWriter bw = null;
	public static int getEnemyNum() {
		return enemyNum;
	}
	public static void setEnemyNum(int enemyNum) {
		Recorder.enemyNum = enemyNum;
	}
	public static int getPlayerNum() {
		return playerNum;
	}
	public static void setPlayerNum(int playerNum) {
		Recorder.playerNum = playerNum;
	}
	public static int getTotalScore() {
		return totalScore;
	}
	public static void setTotalScore(int totalScore) {
		Recorder.totalScore = totalScore;
	}
	
	public static void enemyNumDecrease() {
		enemyNum--;
	}
	
	public static void playerNumDecrease() {
		playerNum--;
	}
	
	public static void totalScoreIncrease() {
		totalScore++;
	}
	
	public static void keepRecording() {
		try {
			fw = new FileWriter("c://Java//myRecording.txt");
			bw = new BufferedWriter(fw);
			bw.write("Your total score is " + totalScore);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				fw.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	} 
}



class Tank {
	int x = 0;
	int y = 0;
	//set tank speed
	int speed = 1;
	int color;
	boolean isLive = true;
	
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
	int times = 0;
	Vector<Shot> ss = new Vector<>();
	Vector<EnemyTank> ets = new Vector<EnemyTank>();
	public EnemyTank(int x, int y) {
		super(x, y);
	}
	public void setEts(Vector<EnemyTank> vv) {
		this.ets = vv;
	}
	
	public boolean isTouchOtherEnemy() {
		boolean b = false;
		switch(this.direct) {
		//Up
		case 0:
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et = ets.get(i);
				if (et != this) {
					if (et.direct == 0 || et.direct == 2) {
						if (this.x >= et.x && this.x <= et.x + 20 && this.y >= et.y && this.y <= et.y + 30) {
							return true;
						}
						
						if (this.x + 20 >= et.x && et.x + 20 <= et.x + 20 && this.y >= et.y && this.y <= et.y + 30) {
							return true;
						}
					}
					
					if (et.direct == 1 || et.direct == 3) {
						if (this.x >= et.x && this.x <= et.x + 30 && this.y >= et.y && this.y <= et.y + 20) {
							return true;
						}
						
						if (this.x + 20 >= et.x && this.x + 20 <= et.x + 30 && this.y >= et.y && this.y <= et.y + 20) {
							return true;
						}
					}
				}
			}
			break;
		//Right
		case 1:
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et = ets.get(i);
				if (et != this) {
					if (et.direct == 0 || et.direct == 2) {
						if (this.x + 30 >= et.x && this.x + 30 <= et.x + 20 && this.y >= et.y && this.y <= et.y + 30) {
							return true;
						}
						
						if (this.x + 30 >= et.x && this.x + 30 <= et.x + 20 && this.y + 20 >= et.y && this.y + 20 <= et.y + 30) {
							return true;
						}
					}
					
					if (et.direct == 1 || et.direct == 3) {
						if (this.x + 30 >= et.x && this.x  + 30<= et.x + 30 && this.y >= et.y && this.y <= et.y + 20) {
							return true;
						}
						
						if (this.x + 30 >= et.x && this.x + 30 <= et.x + 30 && this.y + 20 >= et.y && this.y + 20 <= et.y + 20) {
							return true;
						}
					}
				}
			}
			break;
		//Down	
		case 2:
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et = ets.get(i);
				if (et != this) {
					if (et.direct == 0 || et.direct == 2) {
						if (this.x >= et.x && this.x <= et.x + 20 && this.y + 30 >= et.y && this.y + 30 <= et.y + 30) {
							return true;
						}
						
						if (this.x + 20 >= et.x && this.x + 20 <= et.x + 20 && this.y + 30 >= et.y && this.y + 30 <= et.y + 30) {
							return true;
						}
					}
					
					if (et.direct == 1 || et.direct == 3) {
						if (this.x >= et.x && this.x <= et.x + 30 && this.y + 30 >= et.y && this.y + 30 <= et.y + 20) {
							return true;
						}
						
						if (this.x + 20 >= et.x && this.x + 20 <= et.x + 30 && this.y + 30 >= et.y && this.y + 30 <= et.y + 20) {
							return true;
						}
					}
				}
			}
			break;
		//Left	
		case 3:
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et = ets.get(i);
				if (et != this) {
					if (et.direct == 0 || et.direct == 2) {
						if (this.x >= et.x && this.x <= et.x + 20 && this.y >= et.y && this.y <= et.y + 30) {
							return true;
						}
						
						if (this.x >= et.x && this.x <= et.x + 20 && this.y + 20 >= et.y && this.y + 20 <= et.y + 30) {
							return true;
						}
					}
					
					if (et.direct == 1 || et.direct == 3) {
						if (this.x >= et.x && this.x <= et.x + 30 && this.y >= et.y && this.y <= et.y + 20) {
							return true;
						}
						
						if (this.x >= et.x && this.x <= et.x + 30 && this.y + 20 >= et.y && this.y + 20 <= et.y + 20) {
							return true;
						}
					}
				}
			}
			break;
		}
		
		return b;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while (true) {
			
			switch (this.direct) {
				case 0:
					for (int i = 0; i < 30; i++) {
						if (y > 0 && !isTouchOtherEnemy()) {
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
						if (x < 370 && !isTouchOtherEnemy()) {
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
						if (y < 270 && !isTouchOtherEnemy()) {
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
						if (x > 0 && !isTouchOtherEnemy()) {
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

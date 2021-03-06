package tankGame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;

import javax.sound.sampled.*;


class Node {
	int x;
	int y;
	int direct;
	
	public Node(int x, int y, int direct) {
		this.x = x;
		this.y = y;
		this.direct = direct;
	}
}

class AePlayWave extends Thread {
	private String fileName;
	boolean needStop = false;
	
	public AePlayWave(String wavfile) {
		fileName = wavfile;
	}
	
	public void run() {
		
			File soundFile = new File(fileName);
			AudioInputStream audioInputStream = null;
			
			try {
				audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return;
			}
			
			AudioFormat format = audioInputStream.getFormat();
			SourceDataLine sourceLine = null;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			
			try {
				sourceLine = (SourceDataLine) AudioSystem.getLine(info);
				sourceLine.open(format);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return;
			}
			
			sourceLine.start();
			int nBytesRead = 0;
			byte[] abData = new byte[1024];
			
			try {
				while (nBytesRead != -1 && !needStop) {
					nBytesRead = audioInputStream.read(abData, 0,  abData.length);
					if (nBytesRead >= 0) {
		                @SuppressWarnings("unused")
		                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
		            }
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return;
			} finally {
				sourceLine.drain();
				sourceLine.close();
			}
		}
		
		
	
}


 class Recorder {
	private static int enemyNum = 10;
	private static int playerNum = 3;
	private static int totalScore = 0;
	private static FileWriter fw = null;
	private static BufferedWriter bw = null;
	private static FileReader fr = null;
	private static BufferedReader br = null;
	private static Vector<EnemyTank> ets = new Vector<>();
	private static Vector<Node> nodes = new Vector<>();
	
	//load node
	public static Vector<Node> getNodes() {
		try {
			fr = new FileReader("c:\\Java\\myRecording.txt");
			br = new BufferedReader(fr);
			String n = "";
			n = br.readLine();
			totalScore = Integer.parseInt(n);
			while ((n = br.readLine()) != null) {
				String[] str = n.split(" ");
					Node node = new Node(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]));
					nodes.add(node);
			}
			
//			System.out.println(totalScore);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		
		return nodes;
	}
	
	public static Vector<EnemyTank> getEts() {
		return ets;
	}
	public static void setEts(Vector<EnemyTank> ets2) {
		ets = ets2;
	}
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
	
	//save destroy enemy tank number &  position & direction of enemy tank
	public static void keepRecAndEnemy () {
		try {
			fw = new FileWriter("c:\\Java\\myRecording.txt");
			bw = new BufferedWriter(fw);
			bw.write(totalScore + "\r\n");
			
			//save information of enemy tanks 
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et = ets.get(i);
				if (et.isLive) {
					String record = et.x + " " + et.y + " " + et.direct;
					bw.write(record + "\r\n");
				}
			}
			
			
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
	
	public static void keepRecording() {
		try {
			fw = new FileWriter("c:\\Java\\myRecording.txt");
			bw = new BufferedWriter(fw);
			bw.write(totalScore + "\r\n");
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
	
	public static void getRecording() {
		try {
			fr = new FileReader("c:\\Java\\myRecording.txt");
			br = new BufferedReader(fr);
			String num = br.readLine();
			totalScore = Integer.parseInt(num);
//			System.out.println(totalScore);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
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

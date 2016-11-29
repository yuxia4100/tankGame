package tankGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;



public class MyTankGame extends JFrame implements ActionListener{
	MyPanel mp = null;
	MyStartPanel msp = null;
	JMenuBar jmb = null;
	JMenu jm1 = null;
	JMenuItem jmi1 = null;
	JMenuItem jmi2 = null;
	JMenuItem jmi3 = null;
	JMenuItem jmi4 = null;
	AePlayWave apw = null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyTankGame mtg = new MyTankGame();
		
	}
	
	public MyTankGame() {
//		
		apw = new AePlayWave("src/java_tankgame_bgm.wav");
		apw.start();
		
		jmb = new JMenuBar();
		jm1 = new JMenu("Game(G)");
		jm1.setMnemonic(KeyEvent.VK_G);
		
		jmi1 = new JMenuItem("New game(N)");
		jmi1.setMnemonic(KeyEvent.VK_N);
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newgame");
		jm1.add(jmi1);
		jmb.add(jm1);
		
		jmi4 = new JMenuItem("Continue(C)");
		jmi4.setMnemonic(KeyEvent.VK_C);
		jmi4.addActionListener(this);
		jmi4.setActionCommand("continue");
		jm1.add(jmi4);
		
		jmi3 = new JMenuItem("Save & Exit(S)");
		jmi3.setMnemonic(KeyEvent.VK_S);
		jmi3.addActionListener(this);
		jmi3.setActionCommand("save");
		jm1.add(jmi3);
		
		jmi2 = new JMenuItem("Exit(E)");
		jmi2.setMnemonic(KeyEvent.VK_E);
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exit");
		jm1.add(jmi2);
		
		
		msp = new MyStartPanel();
		Thread t = new Thread(msp);
		t.start();
		
		
		this.setJMenuBar(jmb);
		this.add(msp);
		this.setSize(600, 500);
		this.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("newgame") ) {
			apw.needStop = true;
			AePlayWave apw2 = new AePlayWave("src/java_tankgame_start.wav");
			apw2.start();
			if (mp == null) {
				mp = new MyPanel("newGame");
				Thread t = new Thread(mp);
				t.start();
				this.remove(msp);
				
				this.add(mp);
				this.addKeyListener(mp);
				this.setVisible(true);
			
				
			} else {
				mp.needStop = true;
				this.remove(mp);
				mp = new MyPanel("newGame");
				Thread t = new Thread(mp);
				t.start();
				this.remove(msp);
				
				this.add(mp);
				this.addKeyListener(mp);
				this.setVisible(true);
			}
		} else if (e.getActionCommand().equals("exit")) {
			Recorder.setTotalScore(0);
			Recorder.keepRecording();
			System.exit(0);
		} else if (e.getActionCommand().equals("save")) {
			Recorder.setEts(mp.ets);
			Recorder.keepRecAndEnemy();
			System.exit(0);
		} else if (e.getActionCommand().equals("continue") && mp == null) {
			apw.stop();
			mp = new MyPanel("continue");
			Thread t = new Thread(mp);
			t.start();
			this.remove(msp);
			AePlayWave apw2 = new AePlayWave("src/java_tankgame_start.wav");
			apw2.start();
			this.add(mp);
			this.addKeyListener(mp);
			this.setVisible(true);
		}
	}
	
	
}

class MyEndPanel extends JPanel {
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		g.setColor(Color.yellow);
		Font system = new Font("System", Font.BOLD, 30);
		g.setFont(system);
		g.drawString("You win !!!", 150, 150);
	}
}

class MyStartPanel extends JPanel implements Runnable{
	int times = 0;
	
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		
		if (times % 2 == 0) {
			g.setColor(Color.yellow);
			Font system = new Font("System", Font.BOLD, 30);
			g.setFont(system);
			g.drawString("Stage 1", 150, 150);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			times++;
			this.repaint();
		}
	}
	
	
}

class MyPanel extends JPanel implements KeyListener, Runnable{
	int times = 0;
	Hero hero = null;
	boolean needStop = false;
	//new game or continue
	Vector<EnemyTank> ets = new Vector<>();
	Vector<Node> nodes =  new Vector<>();
	int vectorSize = 3;
	int currSize = vectorSize;
	Vector<Bomb> bombs = new Vector<>();
	Image image1 = null;
	
	public MyPanel(String flag) {
		
		Recorder.getRecording();
		hero = new Hero(100, 100);
		
		//boom image
		image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/Boom.gif"));
		
		if (flag.equals("newGame")) {
			for (int i = 0; i < vectorSize; i++) {
				EnemyTank et = new EnemyTank((i + 1) * 50, 0);
				et.setColor(0);
				et.setDirect(2);
				et.setEts(ets);
				Thread t = new Thread(et);
				t.start();
				Shot s = new Shot(et.x + 10, et.y + 30, 2);
				et.ss.add(s);
				Thread t2 = new Thread(s);
				t2.start();
				ets.add(et);
			}
		} else if (flag.equals("continue")){
			nodes = Recorder.getNodes();
			Recorder.setEnemyNum(nodes.size());
			for (int i = 0; i < nodes.size(); i++) {
				Node node = nodes.get(i);
				EnemyTank et = new EnemyTank(node.x, node.y);
				et.setColor(0);
				et.setDirect(node.direct);
				et.setEts(ets);
				Thread t = new Thread(et);
				t.start();
				Shot s = new Shot(et.x + 10, et.y + 30, 2);
				et.ss.add(s);
				Thread t2 = new Thread(s);
				t2.start();
				ets.add(et);
			}
		}
		
	}
	public void paint(Graphics g) {
		super.paint(g);
		
		if (Recorder.getEnemyNum() == 0) {
			g.fillRect(0, 0, 400, 300);
			if (times % 2 == 0) {
				g.setColor(Color.yellow);
				Font system = new Font("System", Font.BOLD, 30);
				g.setFont(system);
				g.drawString("You Win !!!", 120, 150);
			}
		} else if (Recorder.getPlayerNum() == 0) {
			g.fillRect(0, 0, 400, 300);
			if (times % 2 == 0) {
				g.setColor(Color.yellow);
				Font system = new Font("System", Font.BOLD, 30);
				g.setFont(system);
				g.drawString("Game Over", 120, 150);
			}
		}else {
		
			g.fillRect(0, 0, 400, 300);
			//show information
			this.showInfo(g);
			
			//draw user tank
			if (hero.isLive) {
				this.drawTank(hero.getX(), hero.getY(), g, this.hero.direct, 1);
				
				//draw hero shot
				for (int i = 0; i < hero.ss.size(); i++) {
					Shot myShot = hero.ss.get(i);
					if (myShot != null && myShot.isLive) {
						g.setColor(Color.yellow);
						g.draw3DRect(myShot.x, myShot.y, 1, 1, false);
					}
					
					if (myShot.isLive == false) {
						hero.ss.remove(myShot);
					}
				}	
			}
			
			//draw bomb
			for (int i = 0; i < bombs.size(); i++) {
				Bomb b = bombs.get(i);
				if (b.life > 0) {
					g.drawImage(image1, b.x, b.y, 30, 30, this);
				}
				
				b.lifeDecrease();
				if (b.life == 0) {
					bombs.remove(b);
				}
				
			}
			
			//spawn enemy tank
			if (currSize < vectorSize && Recorder.getEnemyNum() >= vectorSize) {
				EnemyTank et = new EnemyTank(50,50);
				et.setColor(0);
				et.setDirect(2);
				et.setEts(ets);
				Thread t = new Thread(et);
				t.start();
				Shot s = new Shot(et.x + 10, et.y + 30, 2);
				et.ss.add(s);
				Thread t2 = new Thread(s);
				t2.start();
				ets.add(et);
				currSize++;
			}
			
			//spawn player tank
			if (Recorder.getPlayerNum() >= 1 && !hero.isLive) {
				hero = new Hero(100, 100);
				this.drawTank(hero.getX(), hero.getY(), g, this.hero.direct, 1);
			}
			
			
			//draw enemy tank
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et = ets.get(i);
				if (et.isLive) {
					
					this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 0);
					
					
					for (int j = 0; j < et.ss.size(); j++) {
						Shot enemyShot = et.ss.get(j);
						if (enemyShot.isLive) {
							g.setColor(Color.cyan);
							g.draw3DRect(enemyShot.x, enemyShot.y, 1, 1, false);
						} else {
							et.ss.remove(enemyShot);
						}
						
						
					}
				}
			}
		}
		
		
	}
	
	
	public void showInfo(Graphics g) {
		
		this.drawTank(80, 330, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getEnemyNum() + "", 110, 350);
		this.drawTank(130, 330, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Recorder.getPlayerNum() + "", 160, 350);
		
		g.setColor(Color.black);
		Font t = new Font("System", Font.BOLD, 20);
		g.setFont(t);
		g.drawString("Total Score", 420, 30);
		
		this.drawTank(420, 60, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getTotalScore() + "", 450, 80);
		
		
	}
	
	public void hitTank(Shot s, Tank t) {
		switch(t.direct) {
		//Up
		case 0:
		//Down
		case 2:
			if (s.x > t.x && s.x < t.x + 20 && s.y > t.y && s.y < t.y + 30) {
				// is hit
				AePlayWave apw = new AePlayWave("src/java_tankgame_boom.wav");
				apw.start();
				//shot dead
				s.isLive = false;
				//tank dead
				t.isLive = false;
				// create bomb, add to vector
				if (t instanceof Hero) {
					Recorder.playerNumDecrease();
				}
				
				if (t instanceof EnemyTank) {
					Recorder.enemyNumDecrease();
					Recorder.totalScoreIncrease();
					currSize--;
				}
				Bomb b = new Bomb(t.x, t.y);
				bombs.add(b);	
			}
			break;
		//Left	
		case 1:
		//Right	
		case 3:
			if (s.x > t.x && s.x < t.x + 30 && s.y > t.y && s.y < t.y + 20) {
				//is hit
				AePlayWave apw = new AePlayWave("src/java_tankgame_boom.wav");
				apw.start();
				//shot dead
				s.isLive = false;
				//tank dead
				t.isLive = false;
				if (t instanceof Hero) {
					Recorder.playerNumDecrease();
				}
				
				if (t instanceof EnemyTank) {
					Recorder.enemyNumDecrease();
					Recorder.totalScoreIncrease();
					currSize--;
				}
				// create bomb, add to vector
				Bomb b = new Bomb(t.x, t.y);
				bombs.add(b);
			}
			break;
			
		}
	}
	
	public void hitEnemy() {
		for (int i = 0; i < hero.ss.size(); i++) {
			Shot myShot = hero.ss.get(i);
			if (myShot.isLive) {
				for (int j = 0; j < ets.size(); j++) {
					EnemyTank et = ets.get(j);
					
					if (et.isLive) {
						this.hitTank(myShot, et);
					}
				}
			}
		}
	}
	
	public void hitHero() {
		for (int i = 0; i < ets.size(); i++) {
			EnemyTank et = ets.get(i);
			
			for (int j = 0; j < et.ss.size(); j++) {
				Shot enemyShot = et.ss.get(j);
				
				if (hero.isLive) {
					this.hitTank(enemyShot, hero);
				}
			}
		}
	}
	
	
	
	public void drawTank (int x, int y, Graphics g, int direct, int type) {
		switch(type) {
		case 0:
			g.setColor(Color.cyan);
			break;
		case 1:
			g.setColor(Color.yellow);
			break;
		}
		
		switch(direct) {
		// Up
		case 0:
			g.fill3DRect(x, y, 5, 30, false);
			g.fill3DRect(x + 15, y, 5, 30, false);
			g.fill3DRect(x + 5, y + 5, 10, 20, false);
			g.fillOval(x + 5, y + 10, 10, 10);
			g.drawLine(x + 10, y + 15, x + 10, y);
			break;
			
		// Right	
		case 1:	
			g.fill3DRect(x, y, 30, 5, false);
			g.fill3DRect(x, y + 15, 30, 5, false);
			g.fill3DRect(x + 5, y + 5, 20, 10, false);
			g.fillOval(x + 10, y + 5, 10, 10);
			g.drawLine(x + 15, y + 10, x + 30, y + 10);
			break;
			
		// Down
		case 2:
			g.fill3DRect(x, y, 5, 30, false);
			g.fill3DRect(x + 15, y, 5, 30, false);
			g.fill3DRect(x + 5, y + 5, 10, 20, false);
			g.fillOval(x + 5, y + 10, 10, 10);
			g.drawLine(x + 10, y + 15, x + 10, y + 30);
			break;
			
		case 3:
			g.fill3DRect(x, y, 30, 5, false);
			g.fill3DRect(x, y + 15, 30, 5, false);
			g.fill3DRect(x + 5, y + 5, 20, 10, false);
			g.fillOval(x + 10, y + 5, 10, 10);
			g.drawLine(x + 15, y + 10, x, y + 10);
			break;
		}
		
		
		
		
			
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		// set hero tank direct
			if (hero.isLive) {
				if (e.getKeyCode() == KeyEvent.VK_W) {	
					this.hero.setDirect(0);
					this.hero.moveUp();
				} else if (e.getKeyCode() == KeyEvent.VK_D) {
					this.hero.setDirect(1);
					this.hero.moveRight();
				} else if (e.getKeyCode() == KeyEvent.VK_S) {
					this.hero.setDirect(2);
					this.hero.moveDown();
				} else if (e.getKeyCode() == KeyEvent.VK_A) {
					this.hero.setDirect(3);
					this.hero.moveLeft();
				}
				
				if (e.getKeyCode() == KeyEvent.VK_J) {
					if (this.hero.ss.size() < 5) {
						this.hero.shotEnemy();
						AePlayWave apw = new AePlayWave("src/java_tankgame_fire.wav");
						apw.start();
					}
				}
			}
				this.repaint();
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!needStop) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			times++;
			// Enemy tank is hit ?
			this.hitEnemy();
			
			// Hero is hit?
			this.hitHero();
			
			this.repaint();
			
		}
		
		
	}
	
	
	
	
}


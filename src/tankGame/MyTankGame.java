package tankGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyTankGame extends JFrame{
	MyPanel mp = null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyTankGame mtg = new MyTankGame();

	}
	
	public MyTankGame() {
		mp = new MyPanel();
		Thread t = new Thread(mp);
		t.start();
		this.add(mp);
		this.addKeyListener(mp);
		this.setSize(400, 300);
		this.setVisible(true);
		
	}
	
	
}

class MyPanel extends JPanel implements KeyListener, Runnable{
	Hero hero = null;
	
	Vector<EnemyTank> ets = new Vector<>();
	int vectorSize = 3;
	
	Vector<Bomb> bombs = new Vector<>();
	Image image1 = null;
	
	public MyPanel() {
		
		hero = new Hero(100, 100);
		
		//boom image
		image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/Boom.gif"));
		
		
		for (int i = 0; i < vectorSize; i++) {
			EnemyTank et = new EnemyTank((i + 1) * 50, 0);
			et.setColor(0);
			et.setDirect(2);
			Thread t = new Thread(et);
			t.start();
			Shot s = new Shot(et.x + 10, et.y + 30, 2);
			et.ss.add(s);
			Thread t2 = new Thread(s);
			t2.start();
			ets.add(et);
		}
		
	}
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		//draw user tank
		if (hero.isLive) {
			this.drawTank(hero.getX(), hero.getY(), g, this.hero.direct, 1);
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
		
		
		
		
		//draw enemy tank
		for (int i = 0; i < ets.size(); i++) {
			EnemyTank et = ets.get(i);
			if (et.isLive) {
				
				this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 0);
				
				
				for (int j = 0; j < et.ss.size(); j++) {
					Shot enemyShot = et.ss.get(j);
					if (enemyShot.isLive) {
						g.draw3DRect(enemyShot.x, enemyShot.y, 1, 1, false);
					} else {
						et.ss.remove(enemyShot);
					}
					
					
				}
			}
		}
		
		//draw hero shot
		for (int i = 0; i < hero.ss.size(); i++) {
			Shot myShot = hero.ss.get(i);
			if (myShot != null && myShot.isLive) {
				g.draw3DRect(myShot.x, myShot.y, 1, 1, false);
			}
			
			if (myShot.isLive == false) {
				hero.ss.remove(myShot);
			}
		}
		
	}
	
	public void hitHank(Shot s, EnemyTank et) {
		switch(et.direct) {
		//Up
		case 0:
		//Down
		case 2:
			if (s.x > et.x && s.x < et.x + 20 && s.y > et.y && s.y < et.y + 30) {
				// is hit
				//shot dead
				s.isLive = false;
				//tank dead
				et.isLive = false;
				// create bomb, add to vector
				Bomb b = new Bomb(et.x, et.y);
				bombs.add(b);
				
			}
		//Left	
		case 1:
		//Right	
		case 3:
			if (s.x > et.x && s.x < et.x + 30 && s.y > et.y && s.y < et.y + 20) {
				//is hit
				//shot dead
				s.isLive = false;
				//tank dead
				et.isLive = false;
				// create bomb, add to vector
				Bomb b = new Bomb(et.x, et.y);
				bombs.add(b);
				
			}
		}
	}
	
	public void hitHero(Shot s, Hero hero) {
		switch(hero.direct) {
		//Up
		case 0:
		//Down
		case 2:
			if (s.x > hero.x && s.x < hero.x + 20 && s.y > hero.y && s.y < hero.y + 30) {
				// is hit
				//shot dead
				s.isLive = false;
				//tank dead
				hero.isLive = false;
				// create bomb, add to vector
				Bomb b = new Bomb(hero.x, hero.y);
				bombs.add(b);
				
			}
		//Left	
		case 1:
		//Right	
		case 3:
			if (s.x > hero.x && s.x < hero.x + 30 && s.y > hero.y && s.y < hero.y + 20) {
				//is hit
				//shot dead
				s.isLive = false;
				//tank dead
				hero.isLive = false;
				// create bomb, add to vector
				Bomb b = new Bomb(hero.x, hero.y);
				bombs.add(b);
				
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
		while (true) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// Enemy tank is hit ?
			for (int i = 0; i < hero.ss.size(); i++) {
				Shot myShot = hero.ss.get(i);
				if (myShot.isLive) {
					for (int j = 0; j < ets.size(); j++) {
						EnemyTank et = ets.get(j);
						
						if (et.isLive) {
							this.hitHank(myShot, et);
						}
					}
				}
			}
			
			// Hero is hit?
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et = ets.get(i);
				
				for (int j = 0; j < et.ss.size(); j++) {
					Shot enemyShot = et.ss.get(j);
					
					if (enemyShot.isLive) {
						this.hitHero(enemyShot, hero);
					}
				}
			}
			
			
			this.repaint();
		}
		
	}
	
	
}


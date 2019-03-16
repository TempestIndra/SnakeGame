package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements KeyListener, Runnable {
 
	public static final int WIDTH = 500;
	public static final int HEIGHT = 500;
	//render
	private Graphics2D g2d;
	private BufferedImage image;
	
	//Game Loop
	private Thread thread;
	private boolean running;
	private long targetTime;
	
	//gameStuffs
	private final int SIZE = 10;
	private Entity head,object;
	private ArrayList<Entity> snake;
	private int score,level;
	private boolean gameover;
	//movement
	private int directionX,directionY;
	//input
	private boolean up,down,left,right,start;
	
	public GamePanel(){
		//https://docs.oracle.com/javase/7/docs/api/javax/swing/JComponent.html Class JComponent API
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}
	
	
	@Override
	public void addNotify() {
		super.addNotify();
		thread = new Thread(this);
		thread.start();
	}
	
	private void setFPS(int fps){
		targetTime= 1000/fps;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int k = e.getExtendedKeyCode();
		if(k == KeyEvent.VK_UP)
		{
			up = true;
		}
		if(k == KeyEvent.VK_DOWN)
		{
			down = true;
		}
		if(k == KeyEvent.VK_LEFT)
		{
			left = true;
		}
		if(k == KeyEvent.VK_RIGHT)
		{
			right = true;
		}
		if(k == KeyEvent.VK_ENTER)
		{
			start = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		int k = e.getExtendedKeyCode();
		if(k == KeyEvent.VK_UP)
		{
			up = false;
		}
		if(k == KeyEvent.VK_DOWN)
		{
			down = false;
		}
		if(k == KeyEvent.VK_LEFT)
		{
			left = false;
		}
		if(k == KeyEvent.VK_RIGHT)
		{
			right = false;
		}
		if(k == KeyEvent.VK_ENTER)
		{
			start = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	
	@Override
	public void run() {
		if(running == true)
		{
			return;
		}
		start();
		double startTime,elapsed,wait;
		while(running == true)
		{
			startTime = System.nanoTime();
			
			update();
			requestRender();
			
			elapsed = System.nanoTime() - startTime;
			wait = targetTime - elapsed / 1000000;
			if(wait > 0){
				try{
					Thread.sleep((long) wait);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private void start()
	{
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		running = true;
		setUplevel();
		gameover = false;
		level = 1;
		setFPS(level * 10);
	}
	
	public void setUplevel(){
		snake = new ArrayList<Entity>();
		head = new Entity(SIZE);
		head.setPosistion(WIDTH/2, HEIGHT/2);
		snake.add(head);
		for(int i = 1;i<3;i++){
			Entity s = new Entity(SIZE);
			s.setPosistion(head.getX() + (i * SIZE), head.getY());
			snake.add(s);
		}
		object = new Entity(SIZE);
		setObject();
		score = 0;
		gameover = false;
		level = 1;
		directionX = 0;
		directionY = 0;
		setFPS(level * 10);
	}
	
	
	public void setObject(){
		int x = (int) (Math.random()*(WIDTH - SIZE));
		int y = (int) (Math.random()*(HEIGHT - SIZE));
		x = x- (x % SIZE);
		y = y- (y % SIZE);
		object.setPosistion(x, y);
	}
	private void requestRender(){
		render(g2d);
		Graphics g = getGraphics();
		g.drawImage(image,0,0,null);
		g.dispose();
	}
	
	private void update(){
		
		if(gameover == true)
		{
			if(start == true)
			{
				setUplevel();
			}
			return;
		}
		if(up && directionY == 0)
		{
			directionY = -SIZE;
			directionX = 0;
		}
		if(down && directionY == 0)
		{
			directionY = SIZE;
			directionX = 0;
		}
		if(left && directionX == 0)
		{
			directionY = 0;
			directionX = -SIZE;
		}
		if(right && directionX == 0 && directionY != 0)
		{
			directionY = 0;
			directionX = SIZE;
		}
		if(directionX != 0 || directionY != 0)
		{
			for(int i = snake.size()-1 ;i > 0 ; i--)
			{
				snake.get(i).setPosistion(snake.get(i-1).getX(),snake.get(i-1).getY());
			}
			head.move(directionX, directionY);
		}
		
		for(Entity s :snake)
		{
			if(s.isHit(head))
			{
				gameover = true;
				break;
			}
		}
		if(object.isHit(head)){
			score++;
			setObject();
			
			Entity s = new Entity(SIZE);
			s.setPosistion(-100, -100);
			snake.add(s);
			if(score % 10 == 0)
			{
				level++;
				if(level > 10)
				{
					level = 10;
				}
				setFPS(level * 10);
			}
		}
		
		if(head.getX() < 0)
		{
			head.setX(WIDTH);
		}
		if(head.getY() < 0)
		{
			head.setY(HEIGHT);
		}
		if(head.getX() >WIDTH - 1)
		{
			head.setX(0);
		}
		if(head.getY() >HEIGHT - 1)
		{
			head.setY(0);
		}
	}

	public void render(Graphics2D g2d)
	{
		g2d.clearRect(0, 0, WIDTH, HEIGHT);
		g2d.setColor(Color.RED);
		for(Entity e : snake)
		{
			e.render(g2d);
		}
		
		int red = (int) (Math.random() * 256);
		int blue = (int) (Math.random() * 256);
		int green = (int) (Math.random() * 256);
		g2d.setColor(new Color(red, blue, green));
		object.render(g2d);
		if(gameover == true)
		{
			g2d.drawString("GameOver!", WIDTH/2, HEIGHT/2);
		}
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("Score : " + score + " Level : " + level, 10, 20);
		
		if(directionX == 0 && directionY == 0)
		{
			g2d.drawString("READY!",  WIDTH/2, HEIGHT/2);
		}
	}
	
}

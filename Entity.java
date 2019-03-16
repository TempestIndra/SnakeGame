package game;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Entity {
	
	private int x,y,size;
	public Entity(int size)
	{
		this.size = size;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public void setPosistion(int x,int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void move(int directionX, int directionY)
	{
		x += directionX;
		y += directionY;
	}
	
	public Rectangle getBound(){
		return new Rectangle(x,y,size,size);
	}
	
	public boolean isHit(Entity s)
	{
		if(s == this){
			return false;
		}
		return getBound().intersects(s.getBound());
	}
	
	public void render(Graphics g2d)
	{
		g2d.fillRect(x + 1, y + 1, size-2, size-2);
		
	}
	
}

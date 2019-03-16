package game;

import java.awt.Dimension;
import javax.swing.JFrame;

public class MainClass {

	public static void main(String[] args) {
		// https://docs.oracle.com/javase/7/docs/api/javax/swing/JFrame.html#setContentPane(java.awt.Container) JFrame  API
		JFrame frame = new JFrame("SnakeGame");
		frame.setContentPane(new GamePanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setPreferredSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}

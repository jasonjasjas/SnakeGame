package com.jason.snake.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jason.snake.SnakeGame;
import com.jason.snake.SnakeGameBackProp;
import com.jason.snake.SnakeGameQlearn;
import com.jason.snake.SnakeGameRecord;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Snake";
        config.height = 500;
        config.width = 750;
		new LwjglApplication(new SnakeGameQlearn(), config);
	}
}

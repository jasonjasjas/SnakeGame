package com.jason.snake;

public class GameController {
    int score;
    int highscore;
    int maxscore;
    Block food;
    public GameController() {
        score = 0;
        highscore = 0;
        maxscore = 0;
        food = new Block(10,10,Direction.NORTH);
        food.relocate();
    }
}

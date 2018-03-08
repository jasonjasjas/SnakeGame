package com.jason.snake;

public class GameController {
    int score;
    int highscore;
    int maxscore;
    int actualscore;
    int genactualscore;
    int generationscore;
    Block food;
    public GameController() {
        score = 0;
        highscore = 0;
        maxscore = 0;
        actualscore = 0;
        genactualscore = 0;
        generationscore = 0;
        food = new Block(10,10,Direction.NORTH);
        food.relocate();
    }
}

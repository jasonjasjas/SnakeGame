package com.jason.snake;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SnakeGameRecord extends ApplicationAdapter {
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer;
    BitmapFont font;
    CheckBox fastForward;

    NumberFormat formatter = new DecimalFormat("#0.00");
    Table table;
    Stage stage;

    SpriteBatch batch;
    Texture img;

    GameController controller;
    Snake snake;
    GeneticAlgo genetic;

    int time = 0;
    int speed = 1;

    FileWriter fileWriter;
    BufferedWriter bufferedWriter;

    @Override
    public void create () {
        controller = new GameController();
        snake = new Snake();
        genetic = new GeneticAlgo();


        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        TextureRegionDrawable checkOn = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("checkon.png"))));
        TextureRegionDrawable checkOff = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("checkoff.png"))));
        fastForward = new CheckBox("FastForward",new CheckBox.CheckBoxStyle(checkOff,checkOn,font,Color.BLACK));

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(750,500);
        table = new Table();
        stage =new Stage();
        Gdx.input.setInputProcessor(stage);
        table.setFillParent(true);
        stage.addActor(table);
        table.add(fastForward);

        Gdx.graphics.setContinuousRendering(false);
        Gdx.graphics.requestRendering();


    }

    @Override
    public void render () {
        for (int x = 0; x < speed; x++) {


            time = (time+1)%1;

            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(0, 0, 500, 500);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(controller.food.x * 25, controller.food.y * 25, 24, 24);
            shapeRenderer.setColor(Color.BLACK);
            for (int i = 0; i < snake.length; i++) {
                shapeRenderer.rect(snake.blocks[i].x * 25, snake.blocks[i].y * 25, 24, 24);
            }
            shapeRenderer.end();

            if (time == 0) {







                try{

                    fileWriter = new FileWriter("data.txt",true);
                    bufferedWriter = new BufferedWriter(fileWriter);

                    int maxIndex = -1;
                    if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
                        if(snake.blocks[0].direction == Direction.WEST){
                            //Ahead
                            snake.move();
                            maxIndex = 2;

                        }else if(snake.blocks[0].direction == Direction.SOUTH){
                            //Right
                            snake.turnRight();
                            snake.move();
                            maxIndex = 1;

                        }else if(snake.blocks[0].direction == Direction.NORTH){
                            //LEft
                            snake.turnLeft();
                            snake.move();
                            maxIndex = 0;

                        }
                        bufferedWriter.write(Double.toString(snake.getBodyAhead()) + " ");
                        bufferedWriter.write(Double.toString(snake.getBodyLeft())+ " ");
                        bufferedWriter.write(Double.toString(snake.getBodyRight())+ " ");
                        bufferedWriter.write(Double.toString(snake.getFoodDistanceAhead(controller.food))+ " ");
                        bufferedWriter.write(Double.toString(snake.getFoodDistanceLeft(controller.food))+ " ");
                        bufferedWriter.write(Double.toString(snake.getFoodDistanceRight(controller.food))+ " ");
                        if(maxIndex == 0){
                            bufferedWriter.write("1.0 0.0 0.0");
                        }else if(maxIndex == 1){
                            bufferedWriter.write("0.0 1.0 0.0");
                        }else if(maxIndex == 2){
                            bufferedWriter.write("0.0 0.0 1.0");
                        }
                        bufferedWriter.newLine();

                    }else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
                        if(snake.blocks[0].direction == Direction.EAST){
                            //Ahead
                            snake.move();
                            maxIndex = 2;
                        }else if(snake.blocks[0].direction == Direction.NORTH){
                            //Right
                            snake.turnRight();
                            snake.move();
                            maxIndex = 1;

                        }else if(snake.blocks[0].direction == Direction.SOUTH){
                            //Left
                            snake.turnLeft();
                            snake.move();
                            maxIndex = 0;

                        }
                        bufferedWriter.write(Double.toString(snake.getBodyAhead()) + " ");
                        bufferedWriter.write(Double.toString(snake.getBodyLeft())+ " ");
                        bufferedWriter.write(Double.toString(snake.getBodyRight())+ " ");
                        bufferedWriter.write(Double.toString(snake.getFoodDistanceAhead(controller.food))+ " ");
                        bufferedWriter.write(Double.toString(snake.getFoodDistanceLeft(controller.food))+ " ");
                        bufferedWriter.write(Double.toString(snake.getFoodDistanceRight(controller.food))+ " ");
                        if(maxIndex == 0){
                            bufferedWriter.write("1.0 0.0 0.0");
                        }else if(maxIndex == 1){
                            bufferedWriter.write("0.0 1.0 0.0");
                        }else if(maxIndex == 2){
                            bufferedWriter.write("0.0 0.0 1.0");
                        }
                        bufferedWriter.newLine();

                    }else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
                        if(snake.blocks[0].direction == Direction.NORTH){
                            //Ahead
                            snake.move();
                            maxIndex = 2;
                        }else if(snake.blocks[0].direction == Direction.WEST){
                            //Right
                            snake.turnRight();
                            snake.move();
                            maxIndex = 1;

                        }else if(snake.blocks[0].direction == Direction.EAST){
                            //Left
                            snake.turnLeft();
                            snake.move();
                            maxIndex = 0;

                        }
                        bufferedWriter.write(Double.toString(snake.getBodyAhead()) + " ");
                        bufferedWriter.write(Double.toString(snake.getBodyLeft())+ " ");
                        bufferedWriter.write(Double.toString(snake.getBodyRight())+ " ");
                        bufferedWriter.write(Double.toString(snake.getFoodDistanceAhead(controller.food))+ " ");
                        bufferedWriter.write(Double.toString(snake.getFoodDistanceLeft(controller.food))+ " ");
                        bufferedWriter.write(Double.toString(snake.getFoodDistanceRight(controller.food))+ " ");
                        if(maxIndex == 0){
                            bufferedWriter.write("1.0 0.0 0.0");
                        }else if(maxIndex == 1){
                            bufferedWriter.write("0.0 1.0 0.0");
                        }else if(maxIndex == 2){
                            bufferedWriter.write("0.0 0.0 1.0");
                        }
                        bufferedWriter.newLine();

                    }else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
                        if(snake.blocks[0].direction == Direction.SOUTH){
                            //Ahead
                            snake.move();
                            maxIndex = 2;
                        }else if(snake.blocks[0].direction == Direction.EAST){
                            //Right
                            snake.turnRight();
                            snake.move();
                            maxIndex = 1;

                        }else if(snake.blocks[0].direction == Direction.WEST){
                            //Left
                            snake.turnLeft();
                            snake.move();
                            maxIndex = 0;
                        }
                        bufferedWriter.write(Double.toString(snake.getBodyAhead()) + " ");
                        bufferedWriter.write(Double.toString(snake.getBodyLeft())+ " ");
                        bufferedWriter.write(Double.toString(snake.getBodyRight())+ " ");
                        bufferedWriter.write(Double.toString(snake.getFoodDistanceAhead(controller.food))+ " ");
                        bufferedWriter.write(Double.toString(snake.getFoodDistanceLeft(controller.food))+ " ");
                        bufferedWriter.write(Double.toString(snake.getFoodDistanceRight(controller.food))+ " ");
                        if(maxIndex == 0){
                            bufferedWriter.write("1.0 0.0 0.0");
                        }else if(maxIndex == 1){
                            bufferedWriter.write("0.0 1.0 0.0");
                        }else if(maxIndex == 2){
                            bufferedWriter.write("0.0 0.0 1.0");
                        }
                        bufferedWriter.newLine();

                    }





                    bufferedWriter.close();
                }catch (Exception e){

                }


                if ((snake.blocks[0].x == controller.food.x) && (snake.blocks[0].y == controller.food.y)) {
                    controller.food.relocate();
                    for (int i = 0; i < snake.length; i++) {
                        if ((snake.blocks[i].x == controller.food.x) && (snake.blocks[i].y == controller.food.y)) {
                            controller.food.relocate();
                            i = 0;
                        }
                    }

                    controller.score += 100;
                    snake.increaseLength();


                }


                for (int i = 0; i < snake.length; i++) {
                    for (int j = 0; j < snake.length; j++) {
                        if ((snake.blocks[i].x == snake.blocks[j].x) && (snake.blocks[i].y == snake.blocks[j].y) && (i != j)) {
                            snake.alive = false;
                        }
                    }
                }

                if (!snake.alive) {
                    restartGame();
                    Snake s = new Snake();
                    snake = s;
                    controller.score = 0;
                    controller.highscore = 0;
                    controller.food.relocate();
                }

            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                Snake s = new Snake();
                snake = s;
                controller.score = 0;
                controller.food.relocate();
            }

            batch.begin();
            batch.end();
            //stage.draw();
        }

    }

    @Override
    public void dispose () {
        batch.dispose();
        shapeRenderer.dispose();
        img.dispose();
        font.dispose();
    }

    public void keyboardcontrol(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            if(snake.blocks[0].direction != Direction.EAST){
                snake.blocks[0].direction = Direction.WEST;
                snake.move();
            }
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            if(snake.blocks[0].direction != Direction.WEST) {
                snake.blocks[0].direction = Direction.EAST;
                snake.move();
            }
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            if(snake.blocks[0].direction != Direction.SOUTH) {
                snake.blocks[0].direction = Direction.NORTH;
                snake.move();
            }
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            if(snake.blocks[0].direction != Direction.NORTH) {
                snake.blocks[0].direction = Direction.SOUTH;
                snake.move();
            }
        }

    }

    public void restartGame(){
        genetic.population.players[genetic.currentPlayer].fitness = controller.highscore;
        genetic.currentPlayer++;
        if (genetic.currentPlayer == genetic.popSize) {
            genetic.generation++;
            genetic.currentPlayer = 0;
            genetic.population = genetic.evolvePopulation(genetic.population);
        }
    }


}


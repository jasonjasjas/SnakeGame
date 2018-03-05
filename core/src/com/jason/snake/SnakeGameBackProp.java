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
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

public class SnakeGameBackProp extends ApplicationAdapter {
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

    double trainInputs[][] = new double [465][6];
    double trainOutputs[][] = new double [465][3];
    BackProp backprop = new BackProp();

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

        File data = new File("data2.txt");
        int counter = 1;
        int c2 = 0;
        try{
            Scanner scanner = new Scanner(data);
            while(scanner.hasNextDouble()){
                switch (counter){
                    case 1:
                        trainInputs[c2][0] = scanner.nextDouble();
                        break;
                    case 2:
                        trainInputs[c2][1] = scanner.nextDouble();
                        break;
                    case 3:
                        trainInputs[c2][2] = scanner.nextDouble();
                        break;
                    case 4:
                        trainInputs[c2][3] = scanner.nextDouble();
                        break;
                    case 5:
                        trainInputs[c2][4] = scanner.nextDouble();
                        break;
                    case 6:
                        trainInputs[c2][5] = scanner.nextDouble();
                        break;
                    case 7:
                        trainOutputs[c2][0] = scanner.nextDouble();
                        break;
                    case 8:
                        trainOutputs[c2][1] = scanner.nextDouble();
                        break;
                    case 9:
                        trainOutputs[c2][2] = scanner.nextDouble();
                        break;
                }
                counter++;
                if(counter == 10){
                    counter = 1;
                    c2++;
                }



            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("error");
        }

        int sample = 0;
        for (int i = 0; i < 10000; i++) {
            if(sample == 464){
                sample = 0;
            }
            for (int j = 0; j < backprop.inputSize; j++) {
                backprop.inputNeurons[j] = trainInputs[sample][j];
            }
            for (int j = 0; j < backprop.outputSize; j++) {
                backprop.dataNeurons[j] = trainOutputs[sample][j];
            }
            backprop.compute();
            backprop.backPropagate();
            sample++;
        }





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

                backprop.inputNeurons[0] = snake.getBodyAhead();
                backprop.inputNeurons[1] = snake.getBodyLeft();
                backprop.inputNeurons[2] = snake.getBodyRight();
                backprop.inputNeurons[3] = snake.getFoodDistanceAhead(controller.food);
                backprop.inputNeurons[4] = snake.getFoodDistanceLeft(controller.food);
                backprop.inputNeurons[5] = snake.getFoodDistanceRight(controller.food);
                backprop.compute();
                double output1 = backprop.outputNeurons[0];
                double output2 = backprop.outputNeurons[1];
                double output3 = backprop.outputNeurons[2];

                double maxoutput = Math.max(output1,Math.max(output2,output3));
                System.out.println(output1);
                System.out.println(output2);
                System.out.println(output3);

                if (maxoutput == output1) {
                    snake.turnLeft();
                }else if(maxoutput == output2) {
                    snake.turnRight();
                }else{
                }
                snake.move();

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


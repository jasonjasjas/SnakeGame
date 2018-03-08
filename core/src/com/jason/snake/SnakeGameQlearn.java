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

public class SnakeGameQlearn extends ApplicationAdapter {
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer;
    BitmapFont font;
    CheckBox fastForward;
    CheckBox exploitBox;
    boolean exploit;

    NumberFormat formatter = new DecimalFormat("#0.00");
    Table table;
    Stage stage;

    SpriteBatch batch;
    Texture img;

    GameController controller;
    Snake snake;

    int time = 0;
    int speed = 1;

    QLearn qLearn;
    boolean explore;
    int leftc;
    int rightc;
    FileWriter fileWriter;
    BufferedWriter bufferedWriter;
    boolean testing = false;
    int repeatedMoves = 0;
    int counter = 1;


    @Override
    public void create () {
        controller = new GameController();
        snake = new Snake();
        qLearn = new QLearn();
        explore = false;
        leftc = 0;
        rightc = 0;
        testing = false;
        repeatedMoves = 0;

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        TextureRegionDrawable checkOn = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("checkon.png"))));
        TextureRegionDrawable checkOff = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("checkoff.png"))));
        fastForward = new CheckBox("FastForward",new CheckBox.CheckBoxStyle(checkOff,checkOn,font,Color.BLACK));
        exploitBox = new CheckBox("Exploit",new CheckBox.CheckBoxStyle(checkOff,checkOn,font,Color.BLACK));

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(750,500);
        table = new Table();
        stage =new Stage();
        Gdx.input.setInputProcessor(stage);
        table.setFillParent(true);
        stage.addActor(table);
        table.add(fastForward);
        table.add(exploitBox);

        fastForward.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if(fastForward.isChecked()){
                    speed = 30;
                }else{
                    speed = 1;
                }
            }
        });

        exploitBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(exploitBox.isChecked()){
                    exploit = true;
                }else{
                    exploit = false;
                }
            }
        });


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
            if(counter == 1){
                
            }

            if(qLearn.epoch>10000){
                create();
                counter++;
            }

            if(qLearn.epoch%1000 == 0){
                testing = true;
                repeatedMoves = 0;
                restartGame();
                Snake s = new Snake();
                snake = s;
                controller.score = 0;
                controller.highscore = 0;
                controller.food.relocate();
                qLearn.epoch++;
                System.out.println("test");
            }

            if(testing){
                double state1 = qLearn.inputNeurons[0] = snake.getBodyAhead();
                double state2 = qLearn.inputNeurons[1] = snake.getBodyLeft();
                double state3 = qLearn.inputNeurons[2] = snake.getBodyRight();
                double state4 = qLearn.inputNeurons[3] = snake.getFoodDistanceAhead(controller.food);
                double state5 = qLearn.inputNeurons[4] = snake.getFoodDistanceLeft(controller.food);
                double state6 = qLearn.inputNeurons[5] = snake.getFoodDistanceRight(controller.food);

                qLearn.inputNeurons[6] = 1;
                qLearn.inputNeurons[7] = 0;
                qLearn.inputNeurons[8] = 0;

                qLearn.compute();
                double output1 = qLearn.outputNeurons[0];

                qLearn.inputNeurons[6] = 0;
                qLearn.inputNeurons[7] = 1;
                qLearn.inputNeurons[8] = 0;

                qLearn.compute();
                double output2 = qLearn.outputNeurons[0];

                qLearn.inputNeurons[6] = 0;
                qLearn.inputNeurons[7] = 0;
                qLearn.inputNeurons[8] = 1;

                qLearn.compute();
                double output3 = qLearn.outputNeurons[0];

                double maxOutput = Math.max(output1,Math.max(output2,output3));

                if(maxOutput == output1){
                    snake.turnLeft();
                }else if(maxOutput == output2){
                    snake.turnRight();
                }else{
                }

                snake.move();
                repeatedMoves++;

                if(repeatedMoves > 999){
                    snake.alive = false;
                }


                if ((snake.blocks[0].x == controller.food.x) && (snake.blocks[0].y == controller.food.y)) {
                    controller.food.relocate();
                    for (int i = 0; i < snake.length; i++) {
                        if ((snake.blocks[i].x == controller.food.x) && (snake.blocks[i].y == controller.food.y)) {
                            controller.food.relocate();
                            i = 0;
                        }
                    }
                    controller.score++;
                    snake.increaseLength();
                    repeatedMoves = 0;
                }

                for (int i = 0; i < snake.length; i++) {
                    for (int j = 0; j < snake.length; j++) {
                        if ((snake.blocks[i].x == snake.blocks[j].x) && (snake.blocks[i].y == snake.blocks[j].y) && (i != j)) {
                            snake.alive = false;
                            testing = false;
                        }
                    }
                }

                if (!snake.alive) {
                    if (controller.highscore > controller.maxscore) {
                        controller.maxscore = controller.highscore;
                    }

                    try {
                        fileWriter = new FileWriter("qlearn1.txt", true);
                        bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(qLearn.epoch-1 + " " + controller.score);
                        bufferedWriter.newLine();
                        bufferedWriter.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    restartGame();
                    Snake s = new Snake();
                    snake = s;
                    controller.score = 0;
                    controller.highscore = 0;
                    controller.food.relocate();
                    testing = false;
                }

            }else {

                if (time == 0) {


                    double reward = 0;

                    double state1 = qLearn.inputNeurons[0] = snake.getBodyAhead();
                    double state2 = qLearn.inputNeurons[1] = snake.getBodyLeft();
                    double state3 = qLearn.inputNeurons[2] = snake.getBodyRight();
                    double state4 = qLearn.inputNeurons[3] = snake.getFoodDistanceAhead(controller.food);
                    double state5 = qLearn.inputNeurons[4] = snake.getFoodDistanceLeft(controller.food);
                    double state6 = qLearn.inputNeurons[5] = snake.getFoodDistanceRight(controller.food);

//                double state1 = qLearn.inputNeurons[0] = snake.blocks[0].x/19;
//                double state2 = qLearn.inputNeurons[1] = snake.blocks[0].y/19;
//                double state3 = 0;
//                double state4 = 0;
//                switch(snake.blocks[0].direction){
//                    case NORTH:  state3 = qLearn.inputNeurons[2] = 0; state4 = qLearn.inputNeurons[3] = 0;break;
//                    case WEST: state3 =qLearn.inputNeurons[2] = 0; state4 =qLearn.inputNeurons[3] = 1;break;
//                    case SOUTH: state3 =qLearn.inputNeurons[2] = 1; state4 =qLearn.inputNeurons[3] = 0;break;
//                    case EAST: state3 =qLearn.inputNeurons[2] = 1; state4 =qLearn.inputNeurons[3] = 1;break;
//                }
//                double state5 = qLearn.inputNeurons[4] = controller.food.x/19;
//                double state6 = qLearn.inputNeurons[5] = controller.food.y/19;

                    qLearn.inputNeurons[6] = 1;
                    qLearn.inputNeurons[7] = 0;
                    qLearn.inputNeurons[8] = 0;

                    qLearn.compute();
                    double output1 = qLearn.outputNeurons[0];

                    qLearn.inputNeurons[6] = 0;
                    qLearn.inputNeurons[7] = 1;
                    qLearn.inputNeurons[8] = 0;

                    qLearn.compute();
                    double output2 = qLearn.outputNeurons[0];

                    qLearn.inputNeurons[6] = 0;
                    qLearn.inputNeurons[7] = 0;
                    qLearn.inputNeurons[8] = 1;

                    qLearn.compute();
                    double output3 = qLearn.outputNeurons[0];

                    System.out.println("1 :" + Double.toString(output1));
                    System.out.println("2 :" + Double.toString(output2));
                    System.out.println("3 :" + Double.toString(output3));

                    double maxOutput = Math.max(output1, Math.max(output2, output3));
                    if (rightc == 4) {
                        rightc = 0;
                        explore = true;
                    } else if (leftc == 4) {
                        leftc = 0;
                        explore = true;

                    } else if (exploit) {
                        if (maxOutput == output1) {
                            snake.turnLeft();
                            leftc++;
                            rightc = 0;
                        } else if (maxOutput == output2) {
                            snake.turnRight();
                            rightc++;
                            leftc = 0;
                        } else {
                            rightc = 0;
                            leftc = 0;

                        }
                    } else {
                        if (Math.random() > 0.3) {
                            if (maxOutput == output1) {
                                snake.turnLeft();
                                leftc++;
                            } else if (maxOutput == output2) {
                                snake.turnRight();
                                rightc++;
                            } else {
                                rightc = 0;
                                leftc = 0;
                            }
                        } else {
                            explore = true;
                            if (Math.random() > 0.33) {
                                snake.turnLeft();
                                leftc++;
                                maxOutput = output1;
                            } else if (Math.random() > 0.5) {
                                snake.turnRight();
                                rightc++;
                                maxOutput = output2;
                            } else {
                                maxOutput = output3;
                                rightc = 0;
                                leftc = 0;
                            }
                        }
                    }


                    double distance = snake.distanceToFood(controller.food);

                    snake.move();

                    if (distance > snake.distanceToFood(controller.food)) {
                        reward = 0.0005;
                    } else {
                        reward = -0.0010;
                    }


                    if ((snake.blocks[0].x == controller.food.x) && (snake.blocks[0].y == controller.food.y)) {
                        controller.food.relocate();
                        for (int i = 0; i < snake.length; i++) {
                            if ((snake.blocks[i].x == controller.food.x) && (snake.blocks[i].y == controller.food.y)) {
                                controller.food.relocate();
                                i = 0;
                            }
                        }
                        controller.score++;
                        reward = 0.9;
                        snake.increaseLength();
                    }

                    for (int i = 0; i < snake.length; i++) {
                        for (int j = 0; j < snake.length; j++) {
                            if ((snake.blocks[i].x == snake.blocks[j].x) && (snake.blocks[i].y == snake.blocks[j].y) && (i != j)) {
                                snake.alive = false;
                                reward = -0.5;
                            }
                        }
                    }


                    if (!explore) {
                        qLearn.inputNeurons[0] = snake.getBodyAhead();
                        qLearn.inputNeurons[1] = snake.getBodyLeft();
                        qLearn.inputNeurons[2] = snake.getBodyRight();
                        qLearn.inputNeurons[3] = snake.getFoodDistanceAhead(controller.food);
                        qLearn.inputNeurons[4] = snake.getFoodDistanceLeft(controller.food);
                        qLearn.inputNeurons[5] = snake.getFoodDistanceRight(controller.food);

//                    qLearn.inputNeurons[0] = snake.blocks[0].x / 19;
//                    qLearn.inputNeurons[1] = snake.blocks[0].y / 19;
//                    switch (snake.blocks[0].direction) {
//                        case NORTH:
//                            qLearn.inputNeurons[2] = 0;
//                            qLearn.inputNeurons[3] = 0;
//                            break;
//                        case WEST:
//                            qLearn.inputNeurons[2] = 0;
//                            qLearn.inputNeurons[3] = 1;
//                            break;
//                        case SOUTH:
//                            qLearn.inputNeurons[2] = 1;
//                            qLearn.inputNeurons[3] = 0;
//                            break;
//                        case EAST:
//                            qLearn.inputNeurons[2] = 1;
//                            qLearn.inputNeurons[3] = 1;
//                            break;
//                    }
//                    qLearn.inputNeurons[4] = controller.food.x / 19;
//                    qLearn.inputNeurons[5] = controller.food.y / 19;

                        qLearn.inputNeurons[6] = 1;
                        qLearn.inputNeurons[7] = 0;
                        qLearn.inputNeurons[8] = 0;

                        qLearn.compute();
                        double output1st2 = qLearn.outputNeurons[0];

                        qLearn.inputNeurons[6] = 0;
                        qLearn.inputNeurons[7] = 1;
                        qLearn.inputNeurons[8] = 0;

                        qLearn.compute();
                        double output2st2 = qLearn.outputNeurons[0];

                        qLearn.inputNeurons[6] = 0;
                        qLearn.inputNeurons[7] = 0;
                        qLearn.inputNeurons[8] = 1;

                        qLearn.compute();
                        double output3st2 = qLearn.outputNeurons[0];

                        double maxOutputst2 = Math.max(output3st2, Math.max(output1st2, output2st2));

                        qLearn.inputNeurons[0] = state1;
                        qLearn.inputNeurons[1] = state2;
                        qLearn.inputNeurons[2] = state3;
                        qLearn.inputNeurons[3] = state4;
                        qLearn.inputNeurons[4] = state5;
                        qLearn.inputNeurons[5] = state6;

                        if (maxOutput == output1) {
                            qLearn.inputNeurons[6] = 1;
                            qLearn.inputNeurons[7] = 0;
                            qLearn.inputNeurons[8] = 0;
                        } else if (maxOutput == output2) {
                            qLearn.inputNeurons[6] = 0;
                            qLearn.inputNeurons[7] = 1;
                            qLearn.inputNeurons[8] = 0;
                        } else {
                            qLearn.inputNeurons[6] = 0;
                            qLearn.inputNeurons[7] = 0;
                            qLearn.inputNeurons[8] = 1;
                        }

                        qLearn.compute();

                        qLearn.dataNeurons[0] = reward + qLearn.discountFactor * maxOutputst2;
                        qLearn.outputNeurons[0] = maxOutput;

                        qLearn.backPropagate();
                        qLearn.epoch++;
                    } else {
                        explore = false;
                    }

                    if (!snake.alive) {
                        if (controller.highscore > controller.maxscore) {
                            controller.maxscore = controller.highscore;
                        }

                        restartGame();
                        Snake s = new Snake();
                        snake = s;
                        controller.score = 0;
                        controller.highscore = 0;
                        controller.food.relocate();
                    }

                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                Snake s = new Snake();
                snake = s;
                controller.score = 0;
                controller.food.relocate();
            }

            batch.begin();
            font.draw(batch, "Score: " + Integer.toString(controller.score), 135, 235);
            font.draw(batch, "High Score: " + Integer.toString(controller.highscore), 135, 215);
            font.draw(batch, "Max Score: " + Integer.toString(controller.maxscore), 135, 195);
            font.draw(batch, "Epoch: " + Integer.toString(qLearn.epoch), 135, 175);




            fastForward.setPosition(135,35);

            //fastForward.draw(batch,1);


            batch.end();
            fastForward.setPosition(505,85);
            exploitBox.setPosition(505,115);
            stage.draw();
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
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            if(snake.blocks[0].direction != Direction.EAST){
                snake.blocks[0].direction = Direction.WEST;
            }
        }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            if(snake.blocks[0].direction != Direction.WEST) {
                snake.blocks[0].direction = Direction.EAST;
            }
        }else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            if(snake.blocks[0].direction != Direction.SOUTH) {
                snake.blocks[0].direction = Direction.NORTH;
            }
        }else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            if(snake.blocks[0].direction != Direction.NORTH) {
                snake.blocks[0].direction = Direction.SOUTH;
            }
        }
    }

    public void restartGame(){

    }


}


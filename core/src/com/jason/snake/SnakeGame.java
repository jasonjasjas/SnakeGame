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

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SnakeGame extends ApplicationAdapter {
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

        fastForward.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if(fastForward.isChecked()){
                    speed = 30;
                }else{
                    speed = 1;
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

            if (time == 0) {
                //keyboardcontrol();
//                genetic.population.players[genetic.currentPlayer].inputNeurons[0] = (double) snake.getBodyDistanceAhead() / 19;
//                genetic.population.players[genetic.currentPlayer].inputNeurons[1] = (double) snake.getBodyDistanceLeft() / 19;
//                genetic.population.players[genetic.currentPlayer].inputNeurons[2] = (double) snake.getBodyDistanceRight() / 19;
//                genetic.population.players[genetic.currentPlayer].inputNeurons[3] = (double) snake.getWallDistanceAhead() / 19;
//                genetic.population.players[genetic.currentPlayer].inputNeurons[4] = (double) snake.getWallDistanceLeft() / 19;
//                genetic.population.players[genetic.currentPlayer].inputNeurons[5] = (double) snake.getWallDistanceRight() / 19;
//                genetic.population.players[genetic.currentPlayer].inputNeurons[6] = (double) snake.getFoodDistanceAhead(controller.food) / 19;
//                genetic.population.players[genetic.currentPlayer].inputNeurons[7] = (double) snake.getFoodDistanceLeft(controller.food) / 19;
//                genetic.population.players[genetic.currentPlayer].inputNeurons[8] = (double) snake.getFoodDistanceRight(controller.food) / 19;

                genetic.population.players[genetic.currentPlayer].inputNeurons[0] = snake.getBodyAhead();
                genetic.population.players[genetic.currentPlayer].inputNeurons[1] = snake.getBodyLeft();
                genetic.population.players[genetic.currentPlayer].inputNeurons[2] = snake.getBodyRight();
                genetic.population.players[genetic.currentPlayer].inputNeurons[3] = snake.getFoodDistanceAhead(controller.food);
                genetic.population.players[genetic.currentPlayer].inputNeurons[4] = snake.getFoodDistanceLeft(controller.food);
                genetic.population.players[genetic.currentPlayer].inputNeurons[5] = snake.getFoodDistanceRight(controller.food);
                genetic.population.players[genetic.currentPlayer].compute();



                double output1 = genetic.population.players[genetic.currentPlayer].outputNeurons[0];
                double output2 = genetic.population.players[genetic.currentPlayer].outputNeurons[1];
                double output3 = genetic.population.players[genetic.currentPlayer].outputNeurons[2];

                double maxoutput = Math.max(output1,Math.max(output2,output3));

                if (maxoutput == output1) {
                    snake.turnLeft();
                }else if(maxoutput == output2) {
                    snake.turnRight();
                }else{
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

                double distance = snake.distanceToFood(controller.food);

                snake.move();

                if(distance > snake.distanceToFood(controller.food)){
                    controller.score += 2;
                }else{
                    controller.score -= 3;
                }
                if (controller.score > controller.highscore) {
                    controller.highscore = controller.score;
                }

                for (int i = 0; i < snake.length; i++) {
                    for (int j = 0; j < snake.length; j++) {
                        if ((snake.blocks[i].x == snake.blocks[j].x) && (snake.blocks[i].y == snake.blocks[j].y) && (i != j)) {
                            snake.alive = false;
                        }
                    }
                }

                if(controller.score < -20){
                    snake.alive = false;
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
            font.draw(batch, "Current Player: " + Integer.toString(genetic.currentPlayer), 135, 175);
            font.draw(batch, "Generation: " + Integer.toString(genetic.generation), 135, 155);

            font.draw(batch, formatter.format(genetic.population.players[genetic.currentPlayer].inputNeurons[0]), 135, 115);
            font.draw(batch, formatter.format(genetic.population.players[genetic.currentPlayer].inputNeurons[1]), 175, 115);
            font.draw(batch, formatter.format(genetic.population.players[genetic.currentPlayer].inputNeurons[2]), 215, 115);
            font.draw(batch, formatter.format(genetic.population.players[genetic.currentPlayer].inputNeurons[3]), 255, 115);
            font.draw(batch, formatter.format(genetic.population.players[genetic.currentPlayer].inputNeurons[4]), 295, 115);
            font.draw(batch, formatter.format(genetic.population.players[genetic.currentPlayer].inputNeurons[5]), 335, 115);

           // font.draw(batch, formatter.format(genetic.population.players[genetic.currentPlayer].inputNeurons[6]), 135, 135);
           // font.draw(batch, formatter.format(genetic.population.players[genetic.currentPlayer].inputNeurons[7]), 175, 135);
            //font.draw(batch, formatter.format(genetic.population.players[genetic.currentPlayer].inputNeurons[8]), 215, 135);

            //font.draw(batch, formatter.format(genetic.population.players[genetic.currentPlayer].inputNeurons[9]), 295, 135);
            //font.draw(batch, formatter.format(genetic.population.players[genetic.currentPlayer].inputNeurons[10]), 335, 135);

            font.draw(batch, formatter.format(genetic.population.players[genetic.currentPlayer].outputNeurons[0]), 135, 85);
            font.draw(batch, formatter.format(genetic.population.players[genetic.currentPlayer].outputNeurons[1]), 175, 85);
            font.draw(batch, formatter.format(genetic.population.players[genetic.currentPlayer].outputNeurons[2]), 215, 85);

            fastForward.setPosition(135,35);
            //fastForward.draw(batch,1);


            batch.end();
            fastForward.setPosition(505,85);
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
        genetic.population.players[genetic.currentPlayer].fitness = controller.highscore;
        genetic.currentPlayer++;
        if (genetic.currentPlayer == genetic.popSize) {
            genetic.generation++;
            genetic.currentPlayer = 0;
            genetic.population = genetic.evolvePopulation(genetic.population);
        }
    }


}


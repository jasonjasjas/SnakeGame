package com.jason.snake;

public class Snake {
    int length;
    int hunger;
    boolean alive;
    Block[] blocks;
    Snake(){
        length = 4;
        alive = true;
        hunger = 100;
        blocks = new Block[20*20];
        blocks[0] = new Block(10,10,Direction.NORTH);
        blocks[1] = new Block(10,9,Direction.NORTH);
        blocks[2] = new Block(10,8,Direction.NORTH);
        blocks[3] = new Block(10,7,Direction.NORTH);
    }

    void increaseLength(){
        int x = blocks[length-1].x;
        int y = blocks[length-1].y;
        Direction direction = blocks[length-1].direction;
        switch(direction){
            case NORTH:y--;break;
            case EAST:x--;break;
            case SOUTH:y++;break;
            case WEST:x++;break;
        }
        blocks[length] = new Block(x,y,direction);
        length++;
    }

    void move(){
        if(alive){
            for (int i = length-1; i > 0; i--) {
                blocks[i].x = blocks[i-1].x;
                blocks[i].y = blocks[i-1].y;
                blocks[i].direction = blocks[i-1].direction;
            }
            switch(blocks[0].direction){
                case NORTH:blocks[0].y++;break;
                case EAST:blocks[0].x++;break;
                case SOUTH:blocks[0].y--;break;
                case WEST:blocks[0].x--;break;
            }
            if((blocks[0].y > 19)||(blocks[0].y < 0)||(blocks[0].x > 19)||(blocks[0].x < 0)){
                alive = false;
                switch(blocks[0].direction){
                    case NORTH:blocks[0].y--;break;
                    case EAST:blocks[0].x--;break;
                    case SOUTH:blocks[0].y++;break;
                    case WEST:blocks[0].x++;break;
                }
            }
        }
    }
    void turnLeft(){
        switch(blocks[0].direction){
            case NORTH:
                blocks[0].direction = Direction.WEST;
                break;
            case EAST:
                blocks[0].direction = Direction.NORTH;
                break;
            case WEST:
                blocks[0].direction = Direction.SOUTH;
                break;
            case SOUTH:
                blocks[0].direction = Direction.EAST;
                break;
        }
    }
    void turnRight(){
        switch(blocks[0].direction){
            case NORTH:
                blocks[0].direction = Direction.EAST;
                break;
            case EAST:
                blocks[0].direction = Direction.SOUTH;
                break;
            case WEST:
                blocks[0].direction = Direction.WEST;
                break;
            case SOUTH:
                blocks[0].direction = Direction.SOUTH;
                break;
        }
    }

    int getBodyDistanceAhead(){
        switch( blocks[0].direction){
            case NORTH:
                for (int i = blocks[0].y; i < 20; i++) {
                    for (int j = 1; j < length; j++) {
                        if((blocks[0].x == blocks[j].x)&&(i == blocks[j].y)){
                            return blocks[j].y-blocks[0].y;
                        }
                    }
                }break;
            case EAST:
                for (int i = blocks[0].x; i < 20; i++) {
                    for (int j = 1; j < length; j++) {
                        if((i == blocks[j].x)&&(blocks[0].y == blocks[j].y)){
                            return blocks[j].x-blocks[0].x;
                        }
                    }
                }
                break;
            case WEST:
                for (int i = blocks[0].x; i >= 0; i--) {
                    for (int j = 1; j < length; j++) {
                        if((i == blocks[j].x)&&(blocks[0].y == blocks[j].y)){
                            return blocks[0].x-blocks[j].x;
                        }
                    }
                }
                break;
            case SOUTH:
                for (int i = blocks[0].y; i >=0 ; i--) {
                    for (int j = 1; j < length; j++) {
                        if((blocks[0].x == blocks[j].x)&&(i == blocks[j].y)){
                            return blocks[0].y-blocks[j].y;
                        }
                    }
                }break;
        }
        return -19;
    }
    int getBodyDistanceLeft(){
        switch( blocks[0].direction){
            case EAST:
                for (int i = blocks[0].y; i < 20; i++) {
                    for (int j = 1; j < length; j++) {
                        if((blocks[0].x == blocks[j].x)&&(i == blocks[j].y)){
                            return blocks[j].y-blocks[0].y;
                        }
                    }
                }break;
            case SOUTH:
                for (int i = blocks[0].x; i < 20; i++) {
                    for (int j = 1; j < length; j++) {
                        if((i == blocks[j].x)&&(blocks[0].y == blocks[j].y)){
                            return blocks[j].x-blocks[0].x;
                        }
                    }
                }
                break;
            case NORTH:
                for (int i = blocks[0].x; i >= 0; i--) {
                    for (int j = 1; j < length; j++) {
                        if((i == blocks[j].x)&&(blocks[0].y == blocks[j].y)){
                            return blocks[0].x-blocks[j].x;
                        }
                    }
                }
                break;
            case WEST:
                for (int i = blocks[0].y; i >=0 ; i--) {
                    for (int j = 1; j < length; j++) {
                        if((blocks[0].x == blocks[j].x)&&(i == blocks[j].y)){
                            return blocks[0].y-blocks[j].y;
                        }
                    }
                }break;
        }

        return -19;
    }
    int getBodyDistanceRight(){
        switch( blocks[0].direction){
            case WEST:
                for (int i = blocks[0].y; i < 20; i++) {
                    for (int j = 1; j < length; j++) {
                        if((blocks[0].x == blocks[j].x)&&(i == blocks[j].y)){
                            return blocks[j].y-blocks[0].y;
                        }
                    }
                }break;
            case NORTH:
                for (int i = blocks[0].x; i < 20; i++) {
                    for (int j = 1; j < length; j++) {
                        if((i == blocks[j].x)&&(blocks[0].y == blocks[j].y)){
                            return blocks[j].x-blocks[0].x;
                        }
                    }
                }
                break;
            case SOUTH:
                for (int i = blocks[0].x; i >= 0; i--) {
                    for (int j = 1; j < length; j++) {
                        if((i == blocks[j].x)&&(blocks[0].y == blocks[j].y)){
                            return blocks[0].x-blocks[j].x;
                        }
                    }
                }
                break;
            case EAST:
                for (int i = blocks[0].y; i >=0 ; i--) {
                    for (int j = 1; j < length; j++) {
                        if((blocks[0].x == blocks[j].x)&&(i == blocks[j].y)){
                            return blocks[0].y-blocks[j].y;
                        }
                    }
                }break;
        }

        return -19;
    }
    int getWallDistanceAhead(){
        switch( blocks[0].direction){
            case NORTH:
                return 19-blocks[0].y;
            case EAST:
                return 19-blocks[0].x;
            case WEST:
                return blocks[0].x;
            case SOUTH:
                return blocks[0].y;
        }
        return -19;
    }
    int getWallDistanceLeft(){
        switch( blocks[0].direction){
            case NORTH:
                return blocks[0].x;
            case EAST:
                return 19-blocks[0].y;
            case WEST:
                return blocks[0].y;
            case SOUTH:
                return 19 - blocks[0].x;
        }
        return -19;
    }
    int getWallDistanceRight(){
        switch( blocks[0].direction){
            case NORTH:
                return 19-blocks[0].x;
            case EAST:
                return blocks[0].y;
            case WEST:
                return 19-blocks[0].y;
            case SOUTH:
                return blocks[0].x;
        }
        return -19;
    }
    int getFoodDistanceAhead(Block food){
        switch(blocks[0].direction){
            case NORTH:
                if((blocks[0].x == food.x)&&(blocks[0].y < food.y)){
                    return 1;
                }break;
            case EAST:
                if((blocks[0].y == food.y)&&(blocks[0].x < food.x)){
                    return 1;
                }break;
            case WEST:
                if((blocks[0].y == food.y)&&(blocks[0].x > food.x)){
                    return 1;
                }break;
            case SOUTH:
                if((blocks[0].x == food.x)&&(blocks[0].y > food.y)){
                    return 1;
                }break;
        }
        return 0;
    }
    int getFoodDistanceLeft(Block food){
        switch(blocks[0].direction){
            case NORTH:
                if((blocks[0].y == food.y)&&(blocks[0].x > food.x)){
                    return 1;
                }break;
            case EAST:
                if((blocks[0].x == food.x)&&(blocks[0].y < food.y)){

                    return 1;
                }break;

            case WEST:
                if((blocks[0].x == food.x)&&(blocks[0].y > food.y)){
                    return 1;
                }break;
            case SOUTH:
                if((blocks[0].y == food.y)&&(blocks[0].x < food.x)){
                    return 1;
                }break;
        }
        return 0;
    }
    int getFoodDistanceRight(Block food){
        switch(blocks[0].direction){
            case NORTH:
                if((blocks[0].y == food.y)&&(blocks[0].x < food.x)){
                    return 1;
                }break;
            case EAST:
                if((blocks[0].x == food.x)&&(blocks[0].y > food.y)){
                    return 1;
                }break;
            case WEST:
                if((blocks[0].x == food.x)&&(blocks[0].y < food.y)){
                    return 1;
                }break;
            case SOUTH:
                if((blocks[0].y == food.y)&&(blocks[0].x > food.x)){
                    return 1;
                }break;
        }
        return 0;
    }

    int getFoodAhead(Block food) {
        switch(blocks[0].direction){
            case NORTH:
                if((blocks[0].x == food.x)&&(blocks[0].y + 1 == food.y)){
                    return 1;
                }break;
            case EAST:
                if((blocks[0].y == food.y)&&(blocks[0].x + 1 == food.x)){
                    return 1;
                }break;
            case WEST:
                if((blocks[0].y == food.y)&&(blocks[0].x - 1 == food.x)){
                    return 1;
                }break;
            case SOUTH:
                if((blocks[0].x == food.x)&&(blocks[0].y - 1 == food.y)){
                    return 1;
                }break;
        }
        return 0;
    }
    int getFoodLeft(Block food){
        switch(blocks[0].direction){
            case NORTH:
                if((blocks[0].y == food.y)&&(blocks[0].x -1 == food.x)){
                    return 1;
                }break;
            case EAST:
                if((blocks[0].x == food.x)&&(blocks[0].y + 1 == food.y)){

                    return 1;
                }break;

            case WEST:
                if((blocks[0].x == food.x)&&(blocks[0].y -1 == food.y)){
                    return 1;
                }break;
            case SOUTH:
                if((blocks[0].y == food.y)&&(blocks[0].x + 1 == food.x)){
                    return 1;
                }break;
        }
        return 0;
    }
    int getFoodRight(Block food){
        switch(blocks[0].direction){
            case NORTH:
                if((blocks[0].y == food.y)&&(blocks[0].x + 1 == food.x)){
                    return 1;
                }break;
            case EAST:
                if((blocks[0].x == food.x)&&(blocks[0].y - 1 == food.y)){
                    return 1;
                }break;
            case WEST:
                if((blocks[0].x == food.x)&&(blocks[0].y -1 ==  food.y)){
                    return 1;
                }break;
            case SOUTH:
                if((blocks[0].y == food.y)&&(blocks[0].x + 1 == food.x)){
                    return 1;
                }break;
        }
        return 0;
    }

    int getBodyAhead(){
        switch( blocks[0].direction){
            case NORTH:
                if(blocks[0].y == 19){
                    return 1;
                }
                for (int j = 1; j < length; j++) {
                    if((blocks[0].x == blocks[j].x)&&(blocks[0].y + 1 == blocks[j].y)){
                        return 1;
                    }
                }
                break;
            case EAST:
                if(blocks[0].x == 19){
                    return 1;
                }
                for (int j = 1; j < length; j++) {
                    if((blocks[0].x - 1 == blocks[j].x)&&(blocks[0].y == blocks[j].y)){
                        return 1;
                    }
                }
                break;
            case WEST:
                if(blocks[0].x == 0){
                    return 1;
                }
                for (int j = 1; j < length; j++) {
                    if((blocks[0].x + 1 == blocks[j].x)&&(blocks[0].y == blocks[j].y)){
                        return 1;
                    }
                }
                break;
            case SOUTH:
                if(blocks[0].y == 0){
                    return 1;
                }
                for (int j = 1; j < length; j++) {
                    if((blocks[0].x == blocks[j].x)&&(blocks[0].y -1 == blocks[j].y)){
                        return 1;
                    }
                }
                break;
        }
        return 0;
    }
    int getBodyLeft(){
        switch( blocks[0].direction){
            case NORTH:
                if(blocks[0].x == 0){
                    return 1;
                }
                for (int j = 1; j < length; j++) {
                    if((blocks[0].x - 1 == blocks[j].x)&&(blocks[0].y  == blocks[j].y)){
                        return 1;
                    }
                }
                break;
            case EAST:
                if(blocks[0].y == 19){
                    return 1;
                }
                for (int j = 1; j < length; j++) {
                    if((blocks[0].x  == blocks[j].x)&&(blocks[0].y + 1== blocks[j].y)){
                        return 1;
                    }
                }
                break;
            case WEST:
                if(blocks[0].y == 0){
                    return 1;
                }
                for (int j = 1; j < length; j++) {
                    if((blocks[0].x  == blocks[j].x)&&(blocks[0].y  -1 == blocks[j].y)){
                        return 1;
                    }
                }
                break;
            case SOUTH:
                if(blocks[0].x == 19){
                    return 1;
                }
                for (int j = 1; j < length; j++) {
                    if((blocks[0].x + 1 == blocks[j].x)&&(blocks[0].y  == blocks[j].y)){
                        return 1;
                    }
                }
                break;
        }
        return 0;
    }
    int getBodyRight(){
        switch( blocks[0].direction){
            case NORTH:
                if(blocks[0].x == 19){
                    return 1;
                }
                for (int j = 1; j < length; j++) {
                    if((blocks[0].x + 1 == blocks[j].x)&&(blocks[0].y  == blocks[j].y)){
                        return 1;
                    }
                }
                break;
            case EAST:
                if(blocks[0].y == 0){
                    return 1;
                }
                for (int j = 1; j < length; j++) {
                    if((blocks[0].x  == blocks[j].x)&&(blocks[0].y - 1== blocks[j].y)){
                        return 1;
                    }
                }
                break;
            case WEST:
                if(blocks[0].y == 19){
                    return 1;
                }
                for (int j = 1; j < length; j++) {
                    if((blocks[0].x  == blocks[j].x)&&(blocks[0].y  + 1 == blocks[j].y)){
                        return 1;
                    }
                }
                break;
            case SOUTH:
                if(blocks[0].x == 0){
                    return 1;
                }
                for (int j = 1; j < length; j++) {
                    if((blocks[0].x - 1 == blocks[j].x)&&(blocks[0].y  == blocks[j].y)){
                        return 1;
                    }
                }
                break;
        }
        return 0;
    }

    void changeDirection(Direction direction){
        if(direction == Direction.WEST){
            if(blocks[0].direction != Direction.EAST){
                blocks[0].direction = Direction.WEST;
            }
        }else if(direction == Direction.EAST){
            if(blocks[0].direction != Direction.WEST) {
                blocks[0].direction = Direction.EAST;
            }
        }else if(direction == Direction.NORTH){
            if(blocks[0].direction != Direction.SOUTH) {
                blocks[0].direction = Direction.NORTH;
            }
        }else if(direction == Direction.SOUTH){
            if(blocks[0].direction != Direction.NORTH) {
                blocks[0].direction = Direction.SOUTH;
            }
        }
    }

    double distanceToFood(Block food){
        return Math.sqrt(Math.pow((blocks[0].x - food.x),2) + Math.pow((blocks[0].y - food.y),2));
    }

}

class Block{
    int x;
    int y;
    Direction direction;
    Block(int x, int y,Direction direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    void relocate(){
        x = (int) Math.floor(Math.random()*20);
        y = (int) Math.floor(Math.random()*20);
    }
}



enum Direction{
    NORTH,WEST,SOUTH,EAST
}
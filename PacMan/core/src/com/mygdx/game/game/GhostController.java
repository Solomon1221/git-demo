package com.mygdx.game.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.config.GameConfig;

public class GhostController {
    public float direction = 1;

    public float lastDirction = 1;

    public boolean isUp;
    public boolean isDown;
    public boolean isRight;
    public boolean isLeft;

    public boolean canMoveUp;
    public boolean canMoveDown;
    public boolean canMoveRight;
    public boolean canMoveLeft;

    // got to figure out those last few bugs
    // maybe shorten side antenas and would fix diagnal move bug

    // possible isStuck where getX or getY doesn't move by anything.
    // special canmove in each direction for when isStuck
    // if isStuck and canMove in each direction



    public double ghostMoveConstant = 4.25;

    public boolean stayStraight;

    public boolean isStop;

    public boolean antenaFlag;

    public Ghost ghost;
    public GhostInner ghostInner;

    public GameRenderer gameRenderer;

    public boolean flag;

    public float counterDown;
    public float counterDown2;
    public float counterDown3;
    public float counterDown4;


    public float counterUp;
    public float counterUp2;
    public float counterUp3;
    public float counterUp4;

    public float counterRight;
    public float counterRight2;
    public float counterRight3;
    public float counterRight4;

    public float counterLeft;
    public float counterLeft2;
    public float counterLeft3;
    public float counterLeft4;

    PacManInner pac;

    public float timer;

    // that way you can just comment out method calls
    // and get back to start
    public AI ai;

    // direction 1 = right, 2 = down, 3 = left, 4 = up


    // maybe pass direction to constructor
    public GhostController(GameRenderer gameRenderer, PacManInner pac) {
        this.pac = pac;
        this.gameRenderer = gameRenderer;
        this.ai = new AI(gameRenderer);
    }

    // in order to get back to beginning comment out threes and fours and go with right and left only, or whatever.

    public void moveGhosts(float delta, ShapeRenderer renderer) {
        canMoveRight = false;
        canMoveLeft = false;
        canMoveDown = false;
        canMoveUp = false;
        stayStraight = false;

        timer += delta;
        if (timer >= 2.5) {
            System.out.println("Times up!");
            timer = 0;
        }
        float rand = MathUtils.random(1, 4);
        for (int i = 0; i < gameRenderer.getGhosts().size(); i++) {
            ghost = gameRenderer.getGhosts().get(i);
            ghostInner = gameRenderer.getGhostsInner().get(i);


            if (gameRenderer.ghosts.get(i).getDirection() == 1) {
                float x = ghost.getX();
                ghost.setPosition(x += delta * ghostMoveConstant, ghost.getY());
                ghostInner.setPosition(ghost.getX() + ghostInner.getWidth() / 2, ghost.getY() + ghostInner.getHeight() / 2);

                DownAntena downAntena = new DownAntena();
                downAntena.setPosition(ghostInner.getX(), ghostInner.getY() - ghostInner.getHeight());
                downAntena.setSize(1, 2);
                ghost.setDownAntena(downAntena);

                UpAntena upAntena = new UpAntena();
                upAntena.setPosition(ghostInner.getX(), ghostInner.getY());
                upAntena.setSize(1, 2);
                ghost.setUpAntena(upAntena);

                LeftAntena leftAntena = new LeftAntena();
                leftAntena.setPosition(ghostInner.getX() - ghost.getWidth(), ghostInner.getY());
                leftAntena.setSize(5, 1);
                ghost.setLeftAntena(leftAntena);

                RightAntena rightAntena = new RightAntena();
                rightAntena.setPosition(ghostInner.getX(), ghostInner.getY());
                rightAntena.setSize((float) 1.5, 1);
                ghost.setRightAntena(rightAntena);

                renderer.rect(downAntena.getX(), downAntena.getY(), downAntena.getWidth(), downAntena.getHeight());
//
                renderer.rect(upAntena.getX(), upAntena.getY(), upAntena.getWidth(), upAntena.getHeight());

                renderer.rect(rightAntena.getX(), rightAntena.getY(), rightAntena.getWidth(), rightAntena.getHeight());


//                LeftAntena leftAntena = ghost.getLeftAntena();

                // might be off center
//                leftAntena.setPosition(ghostInner.getX() - 2, ghostInner.getY());

                direction = 1;
                isRight = true;

                isLeft = false;
                isUp = false;
                isDown = false;
                // use gethorizontal walls and see if hitting wall, the have boolean is stopRight, etc.

//                boolean isOverlaps = gameRenderer.getHorizontalWalls().get(h).getBounds().overlaps(ghostInner.getBounds());
//                boolean isToLeft = ghostInner.getX() < gameRenderer.getHorizontalWalls().get(h).getX()


                // note that you might only need isoverlaps, not
                // is toleft
                for (int h = 0; h < gameRenderer.getHorizontalWalls().size - 1; h++) {
                    boolean isOverlaps = gameRenderer.getHorizontalWalls().get(h).getBounds().overlaps(ghostInner.getBounds());
                    boolean isToLeft = ghostInner.getX() < gameRenderer.getHorizontalWalls().get(h).getX();


                    if (ghostInner.getX() + ghostInner.getWidth() >= GameConfig.WORLD_WIDTH - 17) {

                        float random = MathUtils.random(1, 4);

                        gameRenderer.getGhosts().get(i).setDirection(random);
                        gameRenderer.getGhostsInner().get(i).setDirection(random);


                    }

                    if (gameRenderer.getGhostsInner().get(i).getX() < 7 || gameRenderer.getGhostsInner().get(i).getX() > GameConfig.WORLD_WIDTH - 20) {
                        counterRight = 1;
                        counterRight2 = 1;


                    }

                    // if greater than or less than set others equal to one, otherwise it will just keep going back and forth

                    // this was not commented out, will have to figure this out
//                    if (gameRenderer.getGhostsInner().get(i).getY() >= 24 || gameRenderer.getGhostsInner().get(i).getY() < 5) {
////                        counterRight3 = 1;
////                        counterRight4 = 1;
//                        stayStraight = true;
//                    }


                    // best way to do it is four different antenas for four different directions
                    // this is it
                    // at first create two, and then four
                    // with two you can test it
                    // also save before change
                    // start with this one and test it, then do the other directions
                    // don't change down antena, that way will leave working model


                    if (downAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getY() > 3) {
                        //System.out.println("Overlaps!");
                        // down

                        counterRight++;

                    }
//
                    if (upAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getY() < 27) {
                        //System.out.println("Overlaps!");
                        // up


                        counterRight2++;
                    }

                    if (rightAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getX() < GameConfig.WORLD_WIDTH - 17) {
                        //System.out.println("Overlaps!");
                        // right or stayStraight


                        counterRight3++;
                    }

                    // reset stay straight true at beginning of each method turn

                    // get right antena


                    // could easily comment out if statement, how to test as small part

                    // separate if statement for right and left

                    // remember to label right and left feelers


                }

                // could put four directions, and else if with greater than, less than


                // in else put boolean canmove, then have decision making process
                // add four directions and canmove boolean


// I don't know if this is supposed to be here from when I erased comments
//
//                if (counterRight3 >= 1) {
//                    canMoveLeft = true;
//                } else {
//
//                    stayStraight = true;
//                }
//

                if (counterRight >= 1) {
                    stayStraight = true;
                } else {

                    canMoveDown = true;
                }

                if (counterRight2 >= 1) {
                    stayStraight = true;
                } else {

                    canMoveUp = true;
                }


                if (counterRight3 >= 1) {
                    stayStraight = false;
                } else {

                    //  canMoveUp = true;
                }

                // should I comment this out?
                if (gameRenderer.getGhostsInner().get(i).getX() > GameConfig.WORLD_WIDTH - 19) {
                    stayStraight = false;
                }


                // next do with right
                // then with up and down
                // then figure out way to test the other ones


                // make it go straight back when hits wall
                // ps, doesn't matter if it goes behind it


                counterRight = 0;

                counterRight2 = 0;

                counterRight3 = 0;

                counterRight4 = 0;


                if (canMoveUp) {
                    gameRenderer.getGhosts().get(i).setDirection(4);
                    gameRenderer.getGhostsInner().get(i).setDirection(4);
                } else if (canMoveDown) {
                    gameRenderer.getGhosts().get(i).setDirection(2);
                    gameRenderer.getGhostsInner().get(i).setDirection(2);
                }


                if (canMoveUp && pac.getY() > gameRenderer.getGhostsInner().get(i).getY()) {
                    gameRenderer.getGhosts().get(i).setDirection(4);
                    gameRenderer.getGhostsInner().get(i).setDirection(4);
                } else if (canMoveDown && pac.getY() < gameRenderer.getGhostsInner().get(i).getY()) {
                    gameRenderer.getGhosts().get(i).setDirection(2);
                    gameRenderer.getGhostsInner().get(i).setDirection(2);
                } else if (stayStraight) {
                    gameRenderer.getGhosts().get(i).setDirection(1);
                    gameRenderer.getGhostsInner().get(i).setDirection(1);
                }

                if (!stayStraight && !canMoveUp && !canMoveDown && gameRenderer.getGhosts().get(i).getX() < GameConfig.WORLD_WIDTH - 20) {
                    gameRenderer.getGhosts().get(i).setDirection(3);
                    gameRenderer.getGhostsInner().get(i).setDirection(3);
                }

                if (gameRenderer.getGhostsInner().get(i).getX() > GameConfig.WORLD_WIDTH - 20 && pac.getY() > gameRenderer.getGhostsInner().get(i).getY()) {
                    gameRenderer.getGhosts().get(i).setDirection(4);
                    gameRenderer.getGhostsInner().get(i).setDirection(4);
                } else if (gameRenderer.getGhostsInner().get(i).getX() > GameConfig.WORLD_WIDTH - 20 && pac.getY() < gameRenderer.getGhostsInner().get(i).getY()) {
                    gameRenderer.getGhosts().get(i).setDirection(2);
                    gameRenderer.getGhostsInner().get(i).setDirection(2);
                }


                if (lastDirction != 1 && stayStraight == true) {
                    gameRenderer.getGhosts().get(i).setDirection(1);
                    gameRenderer.getGhostsInner().get(i).setDirection(1);
                }


                if ((gameRenderer.getGhostsInner().get(i).getY() >= 25 && pac.getY() >= 24) || (gameRenderer.getGhostsInner().get(i).getY() < 4 && pac.getY() < 5)) {
                    gameRenderer.getGhosts().get(i).setDirection(1);
                    gameRenderer.getGhostsInner().get(i).setDirection(1);
                }


                if(gameRenderer.getGhostsInner().get(i).getY() >= 25 && pac.getY() < 24){
                    if(canMoveDown){
                        gameRenderer.getGhosts().get(i).setDirection(2);
                        gameRenderer.getGhostsInner().get(i).setDirection(2);
                    } else{
                        gameRenderer.getGhosts().get(i).setDirection(1);
                        gameRenderer.getGhostsInner().get(i).setDirection(1);
                    }
                }


                if(gameRenderer.getGhostsInner().get(i).getX() > GameConfig.WORLD_WIDTH - 19 && pac.getY() > gameRenderer.getGhostsInner().get(i).getY()){
                    gameRenderer.getGhosts().get(i).setDirection(4);
                    gameRenderer.getGhostsInner().get(i).setDirection(4);
                } else if(gameRenderer.getGhostsInner().get(i).getX() > GameConfig.WORLD_WIDTH - 19 && pac.getY() < gameRenderer.getGhostsInner().get(i).getY()){
                    gameRenderer.getGhosts().get(i).setDirection(2);
                    gameRenderer.getGhostsInner().get(i).setDirection(2);
                }


                // I think keep this last part and then just fix the sides


//
//                if (canMoveLeft) {
//                    gameRenderer.getGhosts().get(i).setDirection(3);
//                    gameRenderer.getGhostsInner().get(i).setDirection(3);
//                } else if (stayStraight) {
//                    gameRenderer.getGhosts().get(i).setDirection(1);
//                    gameRenderer.getGhostsInner().get(i).setDirection(1);
//                }
//
//                if (flag == false) {
//                    gameRenderer.getGhosts().get(i).setDirection(4);
//                    gameRenderer.getGhostsInner().get(i).setDirection(4);
//                    flag = true;
//                }


                stayStraight = false;
                canMoveRight = false;
                canMoveDown = false;
                canMoveUp = false;
                canMoveLeft = false;

                lastDirction = 1;


            }

            if (gameRenderer.getGhosts().get(i).getDirection() == 2) {
                float x = ghost.getX();
                float y = ghost.getY();
                ghost.setPosition(x, y -= delta * ghostMoveConstant);
                ghostInner.setPosition(ghost.getX() + ghostInner.getWidth() / 2, ghost.getY() + ghostInner.getHeight() / 2);
                LeftAntena leftAntena = new LeftAntena();
                leftAntena.setPosition(ghostInner.getX() - ghost.getWidth(), ghostInner.getY());
                leftAntena.setSize(3, 1);
                ghost.setLeftAntena(leftAntena);

                RightAntena rightAntena = new RightAntena();
                rightAntena.setPosition(ghostInner.getX(), ghostInner.getY());
                rightAntena.setSize(3, 1);
                ghost.setRightAntena(rightAntena);

                DownAntena downAntena = new DownAntena();
                downAntena.setPosition(ghostInner.getX(), ghost.getY());
                downAntena.setSize(1, (float) 1.5);
                ghost.setDownAntena(downAntena);


                renderer.rect(leftAntena.getX(), leftAntena.getY(), leftAntena.getWidth(), leftAntena.getHeight());
                renderer.rect(rightAntena.getX(), rightAntena.getY(), rightAntena.getWidth(), rightAntena.getHeight());
                renderer.rect(downAntena.getX(), downAntena.getY(), downAntena.getWidth(), downAntena.getHeight());

                for (int h = 0; h < gameRenderer.getHorizontalWalls().size - 1; h++) {
                    boolean isOverlaps = gameRenderer.getHorizontalWalls().get(h).getBounds().overlaps(ghostInner.getBounds());
                    boolean isToLeft = ghostInner.getX() < gameRenderer.getHorizontalWalls().get(h).getX();

                    // what this is saying is if this doesn't overlap with all walls


                    if (ghostInner.getY() <= 4) {

//                        !leftAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(i).getBounds())

                        float random = MathUtils.random(1, 4);

                        gameRenderer.getGhosts().get(i).setDirection(random);
                        gameRenderer.getGhostsInner().get(i).setDirection(random);


                    }


                    // come back and study this code in depth before you move on
                    if (gameRenderer.getGhostsInner().get(i).getY() > 24) {
                        counterDown = 1;
                        counterDown2 = 1;
                    }


                    // I think keep this commented out, but not sure yet

                    // could have different rules for side lanes

//                    if (gameRenderer.getGhostsInner().get(i).getX() < 7 || gameRenderer.getGhostsInner().get(i).getX() > GameConfig.WORLD_WIDTH - 15) {
//                        counterDown3 = 1;
//                        counterDown4 = 1;
//
//                    }

                    // if greater than or less than set others equal to one, otherwise it will just keep going back and forth

                    if (gameRenderer.getGhostsInner().get(i).getY() >= 24 || gameRenderer.getGhostsInner().get(i).getY() < 5) {
                        counterDown = 1;
                        counterDown2 = 1;
                    }

                    // this will turn into counterDown left, counterDown2 right
                    // have to also test for greater than less than getx +- something


                    if (leftAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getX() > 5) {
                        //System.out.println("Overlaps!");
                        // left
                        counterDown++;
                    }
                    if (rightAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getX() < GameConfig.WORLD_WIDTH - 16) {
                        //System.out.println("Overlaps!");
                        // right
                        counterDown2++;
                    }

                    if (downAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getY() > 3) {
                        //System.out.println("Overlaps!");
                        // right
                        counterDown3++;
                    }


                    // commented this out because I think it's old
//                    if (leftAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getY() > 5 && ghostInner.getY() + .5 > gameRenderer.getHorizontalWalls().get(h).getY()) {
//                        //System.out.println("Overlaps!");
//                        // down, keep straight
//                        counterDown3++;
//                    } else if (leftAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getY() <= 24) {
//                        //System.out.println("Overlaps!");5
//                        // up, but won't do shit
//                        counterDown4++;
//                    }


                }

                // somehow this code is interfering

                // potentially put else if times up

                if (counterDown >= 1) {
                    //  canMoveDown = true;
                    stayStraight = true;
                } else {

                    canMoveLeft = true;

                }

                if (counterDown2 >= 1) {
                    // canMoveDown = true;
                    stayStraight = true;
                } else {

                    canMoveRight = true;

                }

                // possible flag for straightshot and else if statement
                // could just create else if statement here


                if (counterDown3 >= 1) {
//                    gameRenderer.getGhosts().get(i).setDirection(4);
//                    gameRenderer.getGhostsInner().get(i).setDirection(4);
                    stayStraight = false;
                }


                if (canMoveRight) {
                    gameRenderer.getGhosts().get(i).setDirection(1);
                    gameRenderer.getGhostsInner().get(i).setDirection(1);
                } else if (canMoveLeft) {
                    gameRenderer.getGhosts().get(i).setDirection(3);
                    gameRenderer.getGhostsInner().get(i).setDirection(3);
                }

                if (canMoveRight && pac.getX() > gameRenderer.getGhostsInner().get(i).getX()) {
                    gameRenderer.getGhosts().get(i).setDirection(1);
                    gameRenderer.getGhostsInner().get(i).setDirection(1);
                } else if (canMoveLeft && pac.getX() < gameRenderer.getGhostsInner().get(i).getX()) {
                    gameRenderer.getGhosts().get(i).setDirection(3);
                    gameRenderer.getGhostsInner().get(i).setDirection(3);
                } else if (stayStraight) {
                    gameRenderer.getGhosts().get(i).setDirection(2);
                    gameRenderer.getGhostsInner().get(i).setDirection(2);
                }

                if (!stayStraight && !canMoveLeft && !canMoveRight) {
                    gameRenderer.getGhosts().get(i).setDirection(4);
                    gameRenderer.getGhostsInner().get(i).setDirection(4);
                }

                if (gameRenderer.getGhostsInner().get(i).getY() < 5 && pac.getX() > gameRenderer.getGhostsInner().get(i).getX()) {
                    gameRenderer.getGhosts().get(i).setDirection(1);
                    gameRenderer.getGhostsInner().get(i).setDirection(1);
                } else if (gameRenderer.getGhostsInner().get(i).getY() < 5 && pac.getX() < gameRenderer.getGhostsInner().get(i).getX()) {
                    gameRenderer.getGhosts().get(i).setDirection(3);
                    gameRenderer.getGhostsInner().get(i).setDirection(3);
                }


                if (lastDirction != 2 && stayStraight == true) {
                    gameRenderer.getGhosts().get(i).setDirection(2);
                    gameRenderer.getGhostsInner().get(i).setDirection(2);
                }

                // possible flag until reaches condition

                if ((gameRenderer.getGhostsInner().get(i).getX() < 7 && pac.getX() < 7) || (gameRenderer.getGhostsInner().get(i).getX() > GameConfig.WORLD_WIDTH - 19 && pac.getX() > GameConfig.WORLD_WIDTH - 15)) {
                    gameRenderer.getGhosts().get(i).setDirection(2);
                    gameRenderer.getGhostsInner().get(i).setDirection(2);
                }

                if(gameRenderer.getGhostsInner().get(i).getX() >= GameConfig.WORLD_WIDTH - 19 && pac.getX() < GameConfig.WORLD_WIDTH - 19){
                    if(canMoveLeft){
                        gameRenderer.getGhosts().get(i).setDirection(3);
                        gameRenderer.getGhostsInner().get(i).setDirection(3);
                    } else{
                        gameRenderer.getGhosts().get(i).setDirection(2);
                        gameRenderer.getGhostsInner().get(i).setDirection(2);
                    }
                }


                if(gameRenderer.getGhostsInner().get(i).getY() < 7 && pac.getX() > gameRenderer.getGhostsInner().get(i).getX()){
                    gameRenderer.getGhosts().get(i).setDirection(1);
                    gameRenderer.getGhostsInner().get(i).setDirection(1);
                } else if(gameRenderer.getGhostsInner().get(i).getY() < 7 && pac.getX() < gameRenderer.getGhostsInner().get(i).getX()){
                    gameRenderer.getGhosts().get(i).setDirection(3);
                    gameRenderer.getGhostsInner().get(i).setDirection(3);
                }

                counterDown = 0;

                counterDown2 = 0;

                counterDown3 = 0;

                counterDown4 = 0;
                stayStraight = false;
                canMoveRight = false;
                canMoveDown = false;
                canMoveUp = false;
                canMoveLeft = false;

                lastDirction = 2;


            }

            if (gameRenderer.getGhosts().get(i).getDirection() == 3) {
                float x = ghost.getX();
                float y = ghost.getY();
                ghost.setPosition(x -= delta * ghostMoveConstant, y);
                ghostInner.setPosition(ghost.getX() + ghostInner.getWidth() / 2, ghost.getY() + ghostInner.getHeight() / 2);

                DownAntena downAntena = new DownAntena();
                downAntena.setPosition(ghostInner.getX(), ghostInner.getY() - ghostInner.getHeight());
                downAntena.setSize(1, 2);
                ghost.setDownAntena(downAntena);

                UpAntena upAntena = new UpAntena();
                upAntena.setPosition(ghostInner.getX(), ghostInner.getY());
                upAntena.setSize(1, 2);
                ghost.setUpAntena(upAntena);

                // first set width to 3 and render

                LeftAntena leftAntena = new LeftAntena();
                leftAntena.setPosition(ghost.getX(), ghostInner.getY());
                leftAntena.setSize((float) 1.5, 1);
                ghost.setLeftAntena(leftAntena);

                renderer.rect(upAntena.getX(), upAntena.getY(), upAntena.getWidth(), upAntena.getHeight());
                renderer.rect(downAntena.getX(), downAntena.getY(), downAntena.getWidth(), downAntena.getHeight());
                renderer.rect(leftAntena.getX(), leftAntena.getY(), leftAntena.getWidth(), leftAntena.getHeight());


                for (int h = 0; h < gameRenderer.getHorizontalWalls().size - 1; h++) {
                    boolean isOverlaps = gameRenderer.getHorizontalWalls().get(h).getBounds().overlaps(ghostInner.getBounds());
                    boolean isToLeft = ghostInner.getX() < gameRenderer.getHorizontalWalls().get(h).getX();

                    // I think what is happening is it keeps overlapping at each possibility
                    // until it has to move one way

                    // maybe put stop in there like pacman has so it won't walk through walls

                    if (ghostInner.getX() >= GameConfig.WORLD_WIDTH - 17 || ghostInner.getX() < 6) {

                        float random = MathUtils.random(1, 4);

                        gameRenderer.getGhosts().get(i).setDirection(random);
                        gameRenderer.getGhostsInner().get(i).setDirection(random);


                    }

                    // will start feeling with left antena after first row

                    if (gameRenderer.getGhostsInner().get(i).getX() < 7 || gameRenderer.getGhostsInner().get(i).getX() > GameConfig.WORLD_WIDTH - 20) {
                        counterLeft = 1;
                        counterLeft2 = 1;

                    }

                    // copy this and use for one and two in other directions

                    if (gameRenderer.getGhostsInner().get(i).getY() >= 24 || gameRenderer.getGhostsInner().get(i).getY() < 5) {
                        counterLeft3 = 1;
                        counterLeft4 = 1;
                    }

                    // this will turn into counterDown left, counterDown2 right
                    // have to also test for greater than less than getx +- something


                    if (downAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getY() > 3) {
                        //System.out.println("Overlaps!");
                        // down

                        counterLeft++;

                    }
//
                    if (upAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getY() < 27) {
                        //System.out.println("Overlaps!");
                        // up


                        counterLeft2++;
                    }

                    if (leftAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getX() > 5) {
                        //System.out.println("Overlaps!");
                        // left, or stay straight
                        // counterLeft3


                        counterLeft3++;
                    }


                }

                if (counterLeft >= 1) {
                    //  canMoveLeft = true;

                    stayStraight = true;
//                    gameRenderer.getGhosts().get(i).setDirection(3);
//                    gameRenderer.getGhostsInner().get(i).setDirection(3);
                } else {

                    canMoveDown = true;
//                    gameRenderer.getGhosts().get(i).setDirection(2);
//                    gameRenderer.getGhostsInner().get(i).setDirection(2);

                }

                if (counterLeft2 >= 1) {
                    //  canMoveLeft = true;

                    stayStraight = true;
//                    gameRenderer.getGhosts().get(i).setDirection(3);
//                    gameRenderer.getGhostsInner().get(i).setDirection(3);
                } else {

                    canMoveUp = true;
//                    gameRenderer.getGhosts().get(i).setDirection(4);
//                    gameRenderer.getGhostsInner().get(i).setDirection(4);
                }


                // how does counterLeft3 work, if canMoveRight = true, else stay straight, just for now

                // commented this out after


                if (counterLeft3 >= 1) {
                    stayStraight = false;
                } else {

                    //stayStraight = true;
                }


                counterLeft = 0;

                counterLeft2 = 0;

                counterLeft3 = 0;

                counterLeft4 = 0;

                // for some reason stayStraight is getting triggered, otherwise it works
                // is getting triggered by other intersections one and two
                // maybe could put stayStraight first, that way if others come
                // it will get overridden

                // now do mirror opposite on right
                // save class after you do right side,
                // that way you will only have two to go


                if (canMoveUp) {
                    gameRenderer.getGhosts().get(i).setDirection(4);
                    gameRenderer.getGhostsInner().get(i).setDirection(4);
                } else if (canMoveDown) {
                    gameRenderer.getGhosts().get(i).setDirection(2);
                    gameRenderer.getGhostsInner().get(i).setDirection(2);
                }

                // maybe save last direction and see if opposite of this direction, then change direction


                if (canMoveUp && pac.getY() > gameRenderer.getGhostsInner().get(i).getY()) {
                    gameRenderer.getGhosts().get(i).setDirection(4);
                    gameRenderer.getGhostsInner().get(i).setDirection(4);
                } else if (canMoveDown && pac.getY() < gameRenderer.getGhostsInner().get(i).getY()) {
                    gameRenderer.getGhosts().get(i).setDirection(2);
                    gameRenderer.getGhostsInner().get(i).setDirection(2);
                } else if (stayStraight) {
                    gameRenderer.getGhosts().get(i).setDirection(3);
                    gameRenderer.getGhostsInner().get(i).setDirection(3);
                }

                if (!stayStraight && !canMoveUp && !canMoveDown) {
                    gameRenderer.getGhosts().get(i).setDirection(1);
                    gameRenderer.getGhostsInner().get(i).setDirection(1);
                }

                if (gameRenderer.getGhostsInner().get(i).getX() < 6 && pac.getY() > gameRenderer.getGhostsInner().get(i).getY() && pac.getX() < 6) {
                    gameRenderer.getGhosts().get(i).setDirection(4);
                    gameRenderer.getGhostsInner().get(i).setDirection(4);
                } else if (gameRenderer.getGhostsInner().get(i).getX() < 6 && pac.getY() < gameRenderer.getGhostsInner().get(i).getY() && pac.getX() < 6) {
                    gameRenderer.getGhosts().get(i).setDirection(2);
                    gameRenderer.getGhostsInner().get(i).setDirection(2);
                }


                if (lastDirction != 3 && stayStraight == true) {
                    gameRenderer.getGhosts().get(i).setDirection(3);
                    gameRenderer.getGhostsInner().get(i).setDirection(3);
                }


                if ((gameRenderer.getGhostsInner().get(i).getY() >= 25 && pac.getY() >= 24) || (gameRenderer.getGhostsInner().get(i).getY() < 4 && pac.getY() < 5)) {
                    gameRenderer.getGhosts().get(i).setDirection(3);
                    gameRenderer.getGhostsInner().get(i).setDirection(3);
                }

                if(gameRenderer.getGhostsInner().get(i).getY() >= 25 && pac.getY() < 24){
                    if(canMoveDown){
                        gameRenderer.getGhosts().get(i).setDirection(2);
                        gameRenderer.getGhostsInner().get(i).setDirection(2);
                    } else{
                        gameRenderer.getGhosts().get(i).setDirection(3);
                        gameRenderer.getGhostsInner().get(i).setDirection(3);
                    }
                }

                if(gameRenderer.getGhostsInner().get(i).getX() < 6 && pac.getY() > gameRenderer.getGhostsInner().get(i).getY()){
                    gameRenderer.getGhosts().get(i).setDirection(4);
                    gameRenderer.getGhostsInner().get(i).setDirection(4);
                } else if(gameRenderer.getGhostsInner().get(i).getX() < 6 && pac.getY() < gameRenderer.getGhostsInner().get(i).getY()){
                    gameRenderer.getGhosts().get(i).setDirection(2);
                    gameRenderer.getGhostsInner().get(i).setDirection(2);
                }




//                if (canMoveRight) {
//                    gameRenderer.getGhosts().get(i).setDirection(1);
//                    gameRenderer.getGhostsInner().get(i).setDirection(1);
//                } else if (stayStraight) {
//                    gameRenderer.getGhosts().get(i).setDirection(3);
//                    gameRenderer.getGhostsInner().get(i).setDirection(3);
//                }

                stayStraight = false;
                canMoveRight = false;
                canMoveDown = false;
                canMoveUp = false;
                canMoveLeft = false;


                lastDirction = 3;


            }

            if (gameRenderer.getGhosts().get(i).getDirection() == 4) {
                float x = ghost.getX();
                float y = ghost.getY();
                ghost.setPosition(x, y += delta * ghostMoveConstant);
                ghostInner.setPosition(ghost.getX() + ghostInner.getWidth() / 2, ghost.getY() + ghostInner.getHeight() / 2);

                LeftAntena leftAntena = new LeftAntena();
                leftAntena.setPosition(ghostInner.getX() - ghost.getWidth(), ghostInner.getY());
                leftAntena.setSize(3, 1);
                ghost.setLeftAntena(leftAntena);

                RightAntena rightAntena = new RightAntena();
                rightAntena.setPosition(ghostInner.getX(), ghostInner.getY());
                rightAntena.setSize(3, 1);
                ghost.setRightAntena(rightAntena);


                UpAntena upAntena = new UpAntena();
                upAntena.setPosition(ghostInner.getX(), ghostInner.getY());
                upAntena.setSize(1, (float) 1.5);
                ghost.setUpAntena(upAntena);


                renderer.rect(leftAntena.getX(), leftAntena.getY(), leftAntena.getWidth(), leftAntena.getHeight());
                renderer.rect(rightAntena.getX(), rightAntena.getY(), rightAntena.getWidth(), rightAntena.getHeight());
                renderer.rect(upAntena.getX(), upAntena.getY(), upAntena.getWidth(), upAntena.getHeight());

                for (int h = 0; h < gameRenderer.getHorizontalWalls().size - 1; h++) {
                    boolean isOverlaps = gameRenderer.getHorizontalWalls().get(h).getBounds().overlaps(ghostInner.getBounds());
                    boolean isToLeft = ghostInner.getX() < gameRenderer.getHorizontalWalls().get(h).getX();

                    if (ghostInner.getY() > 25) {

                        float random = MathUtils.random(1, 4);
                        gameRenderer.getGhosts().get(i).setDirection(random);
                        gameRenderer.getGhostsInner().get(i).setDirection(random);


                    }


//                    if (gameRenderer.getGhostsInner().get(i).getX() < 7 || gameRenderer.getGhostsInner().get(i).getX() > GameConfig.WORLD_WIDTH - 15) {
//                        counterUp3 = 1;
//                        counterUp4 = 1;
//
//                    }

                    // if greater than or less than set others equal to one, otherwise it will just keep going back and forth

                    if (gameRenderer.getGhostsInner().get(i).getY() >= 24 || gameRenderer.getGhostsInner().get(i).getY() < 5) {
                        counterUp = 1;
                        counterUp2 = 1;
                    }

                    // this will turn into counterDown left, counterDown2 right
                    // have to also test for greater than less than getx +- something


                    if (leftAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getX() < GameConfig.WORLD_WIDTH - 16) {
                        //System.out.println("Overlaps!");
                        // left
                        counterUp++;
                    }
                    if (rightAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getX() > 5) {
                        //System.out.println("Overlaps!");
                        // right
                        counterUp2++;
                    }

                    if (upAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getY() < 27) {
                        //System.out.println("Overlaps!");
                        // right
                        counterUp3++;
                    }

//  commented out
//                    if (leftAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getY() <= 24 && ghostInner.getY() < gameRenderer.getHorizontalWalls().get(h).getX()) {
//                        //System.out.println("Overlaps!");
//                        // keep straight
//                        counterUp3++;
//                    } else if (leftAntena.getBounds().overlaps(gameRenderer.getHorizontalWalls().get(h).getBounds()) && gameRenderer.getHorizontalWalls().get(h).getY() > 5) {
//                        //System.out.println("Overlaps!");
//                        // down, but don't do shit
//                        counterUp4++;
//                    }


                }

                if (counterUp >= 1) {
                    stayStraight = true;
                } else {
                    // boolean canMove here then control flow at end of entire method that will change direction
                    canMoveLeft = true;


                }

                if (counterUp2 >= 1) {
                    stayStraight = true;
                } else {

                    canMoveRight = true;

                }


                if (counterUp3 >= 1) {
                    stayStraight = false;
//                    gameRenderer.getGhosts().get(i).setDirection(2);
//                    gameRenderer.getGhostsInner().get(i).setDirection(2);
                } else {


                }


                if (canMoveLeft) {
                    gameRenderer.getGhosts().get(i).setDirection(3);
                    gameRenderer.getGhostsInner().get(i).setDirection(3);
                } else if (canMoveRight) {
                    gameRenderer.getGhosts().get(i).setDirection(1);
                    gameRenderer.getGhostsInner().get(i).setDirection(1);
                }


                if (canMoveLeft && pac.getX() < gameRenderer.getGhostsInner().get(i).getX()) {
                    gameRenderer.getGhosts().get(i).setDirection(3);
                    gameRenderer.getGhostsInner().get(i).setDirection(3);
                } else if (canMoveRight && pac.getX() > gameRenderer.getGhostsInner().get(i).getX()) {
                    gameRenderer.getGhosts().get(i).setDirection(1);
                    gameRenderer.getGhostsInner().get(i).setDirection(1);
                } else if (stayStraight) {
                    gameRenderer.getGhosts().get(i).setDirection(4);
                    gameRenderer.getGhostsInner().get(i).setDirection(4);
                }

                if (!stayStraight && !canMoveLeft && !canMoveRight) {
                    gameRenderer.getGhosts().get(i).setDirection(2);
                    gameRenderer.getGhostsInner().get(i).setDirection(2);
                }

                if (gameRenderer.getGhostsInner().get(i).getY() >= 24 && pac.getX() > gameRenderer.getGhostsInner().get(i).getX()) {
                    gameRenderer.getGhosts().get(i).setDirection(1);
                    gameRenderer.getGhostsInner().get(i).setDirection(1);
                } else if (gameRenderer.getGhostsInner().get(i).getY() >= 24 && pac.getX() < gameRenderer.getGhostsInner().get(i).getX()) {
                    gameRenderer.getGhosts().get(i).setDirection(3);
                    gameRenderer.getGhostsInner().get(i).setDirection(3);
                }


                // I think this is it

                if (lastDirction != 4 && stayStraight == true) {
                    gameRenderer.getGhosts().get(i).setDirection(4);
                    gameRenderer.getGhostsInner().get(i).setDirection(4);
                }

                if ((gameRenderer.getGhostsInner().get(i).getX() < 7 && pac.getX() < 7) || (gameRenderer.getGhostsInner().get(i).getX() > GameConfig.WORLD_WIDTH - 19 && pac.getX() > GameConfig.WORLD_WIDTH - 20)) {
                    gameRenderer.getGhosts().get(i).setDirection(4);
                    gameRenderer.getGhostsInner().get(i).setDirection(4);
                }

                if(gameRenderer.getGhostsInner().get(i).getX() >= GameConfig.WORLD_WIDTH - 19 && pac.getX() < GameConfig.WORLD_WIDTH - 19){
                    if(canMoveLeft){
                        gameRenderer.getGhosts().get(i).setDirection(3);
                        gameRenderer.getGhostsInner().get(i).setDirection(3);
                    } else{
                        gameRenderer.getGhosts().get(i).setDirection(4);
                        gameRenderer.getGhostsInner().get(i).setDirection(4);
                    }
                }


                if(gameRenderer.getGhostsInner().get(i).getY() > 24 && pac.getX() > gameRenderer.getGhostsInner().get(i).getX()){
                    gameRenderer.getGhosts().get(i).setDirection(1);
                    gameRenderer.getGhostsInner().get(i).setDirection(1);
                } else if(gameRenderer.getGhostsInner().get(i).getY() > 24 && pac.getX() < gameRenderer.getGhostsInner().get(i).getX()){
                    gameRenderer.getGhosts().get(i).setDirection(3);
                    gameRenderer.getGhostsInner().get(i).setDirection(3);
                }


                counterUp = 0;

                counterUp2 = 0;

                counterUp3 = 0;

                counterUp4 = 0;

                stayStraight = false;
                canMoveRight = false;
                canMoveDown = false;
                canMoveUp = false;
                canMoveLeft = false;

                lastDirction = 4;


            }


            if (ghostInner.getY() >= 27) {
                gameRenderer.getGhosts().get(i).setDirection(2);
                gameRenderer.getGhostsInner().get(i).setDirection(2);
            } else if (ghostInner.getY() <= 3) {
                gameRenderer.getGhosts().get(i).setDirection(4);
                gameRenderer.getGhostsInner().get(i).setDirection(4);
            } else if (ghostInner.getX() >= GameConfig.WORLD_WIDTH - 17) {
                gameRenderer.getGhosts().get(i).setDirection(3);
                gameRenderer.getGhostsInner().get(i).setDirection(3);
            } else if (ghostInner.getX() <= 5) {
                gameRenderer.getGhosts().get(i).setDirection(1);
                gameRenderer.getGhostsInner().get(i).setDirection(1);
            }

        }

    }
}

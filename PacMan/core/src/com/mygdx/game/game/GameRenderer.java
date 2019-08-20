package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.util.ViewportUtils;
import com.mygdx.game.util.debug.DebugCameraController;

import java.util.ArrayList;

public class GameRenderer implements Disposable {

    private final GameController controller;

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private DebugCameraController debugCameraController;
    private boolean flag;
    private Array<Wall> horizontalWalls;
    private Array<AntiWall> antiWalls;
    private ArrayList<Integer> antiWallNumbers;
    private boolean fillSpots;
    private float counter;
    private float counter2;
    private float boxCounter;
    private Array<Integer> removeBox = new Array<Integer>();
    public PacMan pacMan;
    public PacManInner pacManInner;

    public ArrayList<Coin> coins = new ArrayList<Coin>();

    public SpriteBatch batch = new SpriteBatch();

    public float whyNot;
    public float whyNot2;
    int randCounter = 0;
    public boolean moveLeft = true;
    public boolean moveRight = true;
    public boolean moveUp = true;
    public boolean moveDown = true;
    public float direction;

    public Ghost ghost1;
    public Ghost ghost2;
    public Ghost ghost3;
    public Ghost ghost4;

    public GhostInner ghostInner1;
    public GhostInner ghostInner2;
    public GhostInner ghostInner3;
    public GhostInner ghostInner4;

    public boolean killPlayer;

    public GhostController ghostController;

    public ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
    public ArrayList<GhostInner> ghostsInner = new ArrayList<GhostInner>();

    Texture pac = new Texture("RightFacingPacManMouthAllOpen.png");

    public GameRenderer(GameController controller) {
        this.controller = controller;
        init();
    }

    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
        horizontalWalls = new Array<Wall>();
        antiWalls = new Array<AntiWall>();
        antiWallNumbers = new ArrayList();
        createRandomMaze();
        createCoins();
        // maybe createCoins method
        pacMan = new PacMan();
        pacMan.setPosition(17, 13);
        pacMan.setSize(2, 2);
        pacManInner = new PacManInner();
//        pacManInner.setPosition(18, 14);
        pacManInner.setSize(1, 1);
        pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
        createGhosts();
        ghostController = new GhostController(this, getPacManInner());
    }

    // == public methods ==


    public PacManInner getPacManInner() {
        return pacManInner;
    }

    public void render(float delta) {
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawWalls();
        drawCoins();

        determineWhereToMove();
        movePacMan(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(pac, GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT / 2, 3, 3);
        batch.end();

        if(killPlayer == false) {
            drawPlayerInner(renderer);
            drawPlayer(renderer);
        }


        determineWhereToMoveInner();
        movePacManInner(delta);
//        pacMan.update(delta, pacMan.getX(), pacMan.getY());

        drawGhosts(renderer);
        drawGhostsInner(renderer);

        moveGhosts(delta, renderer);

        intersectPlayerWithCoins();
        intersectPlayerWidthGhosts();

        renderer.end();

        renderDebug();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        ViewportUtils.debugPixelsPerUnit(viewport);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    // == private methods ==

    private void moveGhosts(float delta, ShapeRenderer renderer) {
        // ghostController method calls go here

        // moveGhosts is in renderer, so you can draw antena
        ghostController.moveGhosts(delta, renderer);
    }

    private void drawGhosts(ShapeRenderer renderer) {
        for (int i = 0; i < ghosts.size(); i++) {
            ghosts.get(i).drawGhost(renderer);
        }
    }

    private void intersectPlayerWidthGhosts(){
        for (int i = 0; i < ghostsInner.size(); i++) {

            if(pacManInner.getBounds().overlaps(ghostsInner.get(i).getBounds())){
                killPlayer = true;
            }


        }
    }

    private void intersectPlayerWithCoins(){
        for (int i = 0; i < coins.size(); i++) {

            if(pacManInner.getBounds().overlaps(coins.get(i).getBounds())){
                coins.remove(i);
            }


        }
    }

    private void drawGhostsInner(ShapeRenderer renderer) {
        for (int i = 0; i < ghostsInner.size(); i++) {
            ghostsInner.get(i).drawGhost(renderer);
        }
    }

    public ArrayList<Ghost> getGhosts() {
        return ghosts;
    }

    public Array<Wall> getHorizontalWalls() {
        return horizontalWalls;
    }

    public ArrayList<GhostInner> getGhostsInner() {
        return ghostsInner;
    }

    private void createGhosts() {

        ghost1 = new Ghost();
        ghost1.setPosition(GameConfig.WORLD_WIDTH - 19, 25);
        ghost1.setSize(2, 2);



        ghost2 = new Ghost();
        ghost2.setPosition(5, 25);
        ghost2.setSize(2, 2);
        ghosts.add(ghost2);

        ghost3 = new Ghost();
        ghost3.setPosition(5, 3);
        ghost3.setSize(2, 2);
        ghosts.add(ghost3);

        ghost4 = new Ghost();
        ghost4.setPosition(GameConfig.WORLD_WIDTH - 19, 3);
        ghost4.setSize(2, 2);
        ghosts.add(ghost4);


        ghostInner1 = new GhostInner();
        ghostInner1.setSize(1, 1);
        ghostInner1.setPosition(ghost1.getX() + ghostInner1.getWidth() / 2, ghost1.getY() + ghostInner1.getHeight() / 2);
        LeftAntena leftAntena = new LeftAntena();
        leftAntena.setPosition(ghostInner1.getX() - 1, ghost1.getY());
        leftAntena.setSize(4, 1);
        ghost1.setLeftAntena(leftAntena);
        ghosts.add(ghost1);
        ghostsInner.add(ghostInner1);

        ghostInner2 = new GhostInner();
        ghostInner2.setSize(1, 1);
        ghostInner2.setPosition(ghost2.getX() + ghostInner2.getWidth() / 2, ghost2.getY() + ghostInner2.getHeight() / 2);
        ghostsInner.add(ghostInner2);

        ghostInner3 = new GhostInner();
        ghostInner3.setSize(1, 1);
        ghostInner3.setPosition(ghost3.getX() + ghostInner3.getWidth() / 2, ghost3.getY() + ghostInner3.getHeight() / 2);
        ghostsInner.add(ghostInner3);

        ghostInner4 = new GhostInner();
        ghostInner4.setSize(1, 1);
        ghostInner4.setPosition(ghost4.getX() + ghostInner4.getWidth() / 2, ghost4.getY() + ghostInner4.getHeight() / 2);
        ghostsInner.add(ghostInner4);


//        pacMan = new PacMan();
//        pacMan.setPosition(17, 13);
//        pacMan.setSize(2, 2);
//        pacManInner = new PacManInner();
////        pacManInner.setPosition(18, 14);
//        pacManInner.setSize(1, 1);
//        pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
    }

    private void createCoins(){
        // j was 25
        for (int j = 26; j >= 3; j -= 2) {

            for (int i = 0; i < (GameConfig.WORLD_WIDTH / 3) - 3; i++) {


                // only creates one random number
                float rand = MathUtils.random(1, 100);

                    // was + 5
                    float setX = (i * 2) + 6;
                    //    System.out.println((i * 2) + 5 + "y =" + j);
                    Coin coin = new Coin();
                    coin.setPosition(setX, j);
                    coin.setSize((float) .25, (float) .25);

                    coins.add(coin);





            }
        }
    }

    private void createRandomMaze() {

        // all of this works. Go down to bottom and remove only certain walls


        // study it for awhile before you add coins
        // thinking copy for loops and start one over


        for (int j = 25; j >= 3; j -= 2) {

            for (int i = 0; i < (GameConfig.WORLD_WIDTH / 3) - 3; i++) {


                // only creates one random number
                float rand = MathUtils.random(1, 100);

                if (rand < 15) {
                    float setX = (i * 2) + 5;
                    //    System.out.println((i * 2) + 5 + "y =" + j);
                    Wall wall = new Wall(setX, j, 2, (float) .01);
                    wall.setPosition(setX, j);
                    wall.setSize(2, (float) .01);
                    if (i == 0) {
                        wall.setRemove(true);
                    }
                    horizontalWalls.add(wall);

                    counter = 0;
                } else if (counter == 1) {
                    float setX = (i * 2) + 5;
                    //  System.out.println((i * 2) + 5 + "y =" + j);
                    Wall wall = new Wall(setX, j, 2, (float) .01);
                    wall.setPosition(setX, j);
                    wall.setSize(2, (float) .01);
                    if (i == 0) {
                        wall.setRemove(true);
                    }
                    horizontalWalls.add(wall);
                    counter = 0;
                } else {
                    counter++;
                }


            }
        }
        int randCounter2 = 0;


        for (int i = 0; i < (GameConfig.WORLD_WIDTH / 3) - 3; i++) {
            for (int j = 25; j >= 5; j -= 2) {


                // only creates one random number
                float rand = MathUtils.random(1, 100);

                if (rand < 15) {
                    //  System.out.println(i);
                    Wall wall = new Wall((i * 2) + 5, j, (float) .01, 2);
                    wall.setPosition((i * 2) + 5, j);
                    wall.setSize((float) .01, 2);
                    horizontalWalls.add(wall);
                    counter2 = 0;

                } else if (counter2 == 1) {
                    //  System.out.println(i);
                    Wall wall = new Wall((i * 2) + 5, j, (float) .01, 2);
                    wall.setPosition((i * 2) + 5, j);
                    wall.setSize((float) .01, 2);
                    horizontalWalls.add(wall);
                    counter2 = 0;

                } else {
                    counter2++;
                }

            }


            randCounter2++;

        }


        for (int i = 0; i < horizontalWalls.size - 1; i++) {
            whyNot++;
            Integer rand = MathUtils.random(0, 100);
            if (horizontalWalls.get(i).getRemove()) {
                horizontalWalls.removeIndex(i);
                System.out.println("removed");

            }
            if (horizontalWalls.get(i).getX() == 31 && horizontalWalls.get(i).getWidth() == 2) {
                horizontalWalls.removeIndex(i);

            }

            if (horizontalWalls.get(i).getX() == 17 && horizontalWalls.get(i).getWidth() == 2) {
                horizontalWalls.removeIndex(i);
            }

            if (horizontalWalls.get(i).getY() == 25 && horizontalWalls.get(i).getHeight() == 2) {
                horizontalWalls.removeIndex(i);
            }

            if (horizontalWalls.get(i).getY() == 3 && horizontalWalls.get(i).getHeight() == 2) {
                horizontalWalls.removeIndex(i);
            }

            if (horizontalWalls.get(i).getY() == 13 && horizontalWalls.get(i).getHeight() == 2) {
                horizontalWalls.removeIndex(i);
            }

            if (horizontalWalls.get(i).getY() < 11 && horizontalWalls.get(i).getY() > 5 && horizontalWalls.get(i).getHeight() == 2 && rand < 30) {
                horizontalWalls.removeIndex(i);
            }

            if (horizontalWalls.get(i).getY() < 23 && horizontalWalls.get(i).getY() > 15 && horizontalWalls.get(i).getHeight() == 2 && rand < 30) {
                horizontalWalls.removeIndex(i);
            }

            if (horizontalWalls.get(i).getX() < 15 && horizontalWalls.get(i).getX() > 7 && horizontalWalls.get(i).getWidth() == 2 && rand < 30) {
                horizontalWalls.removeIndex(i);
            }

            if (horizontalWalls.get(i).getX() < 29 && horizontalWalls.get(i).getX() > 15 && horizontalWalls.get(i).getWidth() == 2 && rand < 30) {
                horizontalWalls.removeIndex(i);
            }


        }

        for (int i = 0; i < horizontalWalls.size - 1; i++) {
            if (horizontalWalls.get(i).getRemove()) {
                horizontalWalls.removeIndex(i);
                System.out.println("removed");

            }

            if (horizontalWalls.get(i).getY() == 13 && horizontalWalls.get(i).getHeight() == 2) {
                horizontalWalls.removeIndex(i);
            }

            if (horizontalWalls.get(i).getY() == 25 && horizontalWalls.get(i).getHeight() == 2) {
                horizontalWalls.removeIndex(i);
            }

            if (horizontalWalls.get(i).getY() == 3) {
                horizontalWalls.removeIndex(i);
            }

            if (horizontalWalls.get(i).getX() == 31 && horizontalWalls.get(i).getWidth() == 2) {
                horizontalWalls.removeIndex(i);

            }
        }


        // System.out.println("outside = " + whyNot + "inside = " + whyNot2);


        int randCounter3 = 0;

        for (int i = 0; i < (GameConfig.WORLD_WIDTH / 3) - 3; i++) {
            // only creates one random number
            float rand = MathUtils.random(1, 100);


            Wall wall = new Wall((i * 2) + 5, 3, 2, (float) .01);
            horizontalWalls.add(wall);


            randCounter3++;

        }

        int randCounter4 = 0;

        for (int i = 0; i < (GameConfig.WORLD_WIDTH / 3) - 3; i++) {


            // only creates one random number
            float rand = MathUtils.random(1, 100);


            Wall wall = new Wall((i * 2) + 5, 27, 2, (float) .01);
            horizontalWalls.add(wall);


            randCounter4++;

        }

        int randCounter5 = 0;

        for (int i = 0; i < (25 / 3) + 4; i++) {


            // only creates one random number
            float rand = MathUtils.random(1, 100);


            Wall wall = new Wall(5, (i * 2) + 3, (float) .01, 2);
            horizontalWalls.add(wall);


            randCounter5++;
        }


        int randCounter6 = 0;

        for (int i = 0; i < (25 / 3) + 4; i++) {


            // only creates one random number
            float rand = MathUtils.random(1, 100);


            Wall wall = new Wall(GameConfig.WORLD_WIDTH - 17, (i * 2) + 3, (float) .01, 2);
            horizontalWalls.add(wall);

            randCounter6++;

        }


        // here is where you should work


    }

    private void determineWhereToMove() {
        moveLeft = true;
        moveRight = true;
        moveDown = true;
        moveUp = true;


        // maybe check if already moveWhatever false, then don't
        // do the next one
        for (int i = 0; i < horizontalWalls.size - 1; i++) {

            if ((pacManInner.getX() + .5 >= horizontalWalls.get(i).getX()) && horizontalWalls.get(i).getHeight() == 2) {
                if (pacManInner.getBounds().overlaps(horizontalWalls.get(i).getBounds())) {
                    pacMan.setPosition((float) (horizontalWalls.get(i).getX() + .01), pacMan.getY());
                    pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
                    moveLeft = false;
                }
            }
            if ((pacManInner.getX() + .5 >= horizontalWalls.get(i).getX()) && horizontalWalls.get(i).getWidth() == 2 && Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if (pacManInner.getBounds().overlaps(horizontalWalls.get(i).getBounds())) {
                    pacMan.setPosition((float) (horizontalWalls.get(i).getX() + .01 + 2), pacMan.getY());
                    pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
                    moveLeft = false;
                }
            }

            if ((pacManInner.getX() + .5 <= horizontalWalls.get(i).getX()) && horizontalWalls.get(i).getHeight() == 2) {
                if (pacManInner.getBounds().overlaps(horizontalWalls.get(i).getBounds())) {
                    pacMan.setPosition((float) (horizontalWalls.get(i).getX() - .01 - pacMan.getWidth()), pacMan.getY());
                    pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
                    moveRight = false;
                }
            }
            if ((pacManInner.getX() + .5 <= horizontalWalls.get(i).getX()) && horizontalWalls.get(i).getWidth() == 2 && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if (pacManInner.getBounds().overlaps(horizontalWalls.get(i).getBounds())) {
                    pacMan.setPosition((float) (horizontalWalls.get(i).getX() - .01 - pacMan.getWidth()), pacMan.getY());
                    pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
                    moveRight = false;
                }
            }

            if ((pacManInner.getY() + .5 >= horizontalWalls.get(i).getY()) && horizontalWalls.get(i).getHeight() == 2) {

                // note, this is how you want to get all of them
                if (pacManInner.getBounds().overlaps(horizontalWalls.get(i).getBounds())) {
                    pacMan.setPosition((float) (horizontalWalls.get(i).getY() + .01 + pacMan.getWidth() + horizontalWalls.get(i).getHeight()), pacMan.getY());
                    pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
                    moveDown = false;
                }
            }
            if ((pacManInner.getY() + .5 >= horizontalWalls.get(i).getY()) && horizontalWalls.get(i).getWidth() == 2 && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                if (pacManInner.getBounds().overlaps(horizontalWalls.get(i).getBounds())) {
                    pacMan.setPosition((float) (pacMan.getX()), (float) (pacMan.getY() + .01 + pacManInner.getHeight()));
                    pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
                    moveDown = false;
                }
            }

            if ((pacManInner.getY() + .5 <= horizontalWalls.get(i).getY()) && horizontalWalls.get(i).getHeight() == 2) {

                // note, this is how you want to get all of them
                if (pacManInner.getBounds().overlaps(horizontalWalls.get(i).getBounds())) {
                    pacMan.setPosition((float) (horizontalWalls.get(i).getY() - .01 - pacMan.getWidth() + horizontalWalls.get(i).getHeight()), pacMan.getY());
                    pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
                    moveUp = false;
                }
            }
            if ((pacManInner.getY() + .5 <= horizontalWalls.get(i).getY()) && horizontalWalls.get(i).getWidth() == 2 && Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if (pacManInner.getBounds().overlaps(horizontalWalls.get(i).getBounds())) {
                    pacMan.setPosition((float) (pacMan.getX()), (float) (pacMan.getY() - .01 - pacManInner.getHeight()));
                    pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
                    moveUp = false;
                }
            }


            // keep on trying can always go back to the previous way if you fail.

//            if (pacMan.getY() <= horizontalWalls.get(i).getY() && pacMan.getBounds().overlaps(horizontalWalls.get(i).getBounds()) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//                pacMan.setPosition((float) pacMan.getX(), (float) ((float) horizontalWalls.get(i).getY() + .01));
//                pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
//            }
//            if (pacMan.getY() >= horizontalWalls.get(i).getY() - pacMan.getHeight() && pacMan.getBounds().overlaps(horizontalWalls.get(i).getBounds()) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
//                pacMan.setPosition((float) pacMan.getX(), (float) ((float) horizontalWalls.get(i).getY() - pacMan.getHeight() - .01));
//                pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
//            }
//            if (pacMan.getX() >= horizontalWalls.get(i).getX() - pacMan.getWidth() && pacMan.getBounds().overlaps(horizontalWalls.get(i).getBounds()) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//                pacMan.setPosition((float) (horizontalWalls.get(i).getX() - pacMan.getWidth() - .01), (float) pacMan.getY());
//                pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
//            }
//
//            if (pacMan.getX() <= horizontalWalls.get(i).getX() + pacMan.getX() && pacMan.getBounds().overlaps(horizontalWalls.get(i).getBounds()) && Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//                pacMan.setPosition((float) (horizontalWalls.get(i).getX() + .01), (float) pacMan.getY());
//                pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
//            }

            // come back and do this to the rest

            if (pacManInner.getX() - .5 <= 5) {
                pacMan.setPosition((float) (5 + .01), (float) pacMan.getY());
                pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
            }
            if (pacManInner.getX() + .5 >= GameConfig.WORLD_WIDTH - 17 - pacManInner.getWidth()) {
                pacMan.setPosition((float) (GameConfig.WORLD_WIDTH - 17 - pacMan.getWidth() - .01), (float) pacMan.getY());
                pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
            }
            if (pacMan.getY() >= 27 - pacMan.getHeight()) {
                pacMan.setPosition((float) pacMan.getX(), (float) ((float) 27 - pacMan.getHeight() - .01));
                pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
            }
            if (pacMan.getY() <= 3) {
                pacMan.setPosition((float) pacMan.getX(), (float) ((float) 3 + .01));
                pacManInner.setPosition(pacMan.getX() + pacManInner.getWidth() / 2, pacMan.getY() + pacManInner.getHeight() / 2);
            }


//            if (pacManInner.getBounds().overlaps(horizontalWalls.get(i).getBounds())) {
//                System.out.println("intersect");
//                if ((pacManInner.getX() + .5 >= horizontalWalls.get(i).getX())) {
//                    if (pacManInner.getBounds().overlaps(horizontalWalls.get(i).getBounds())) {
//                        moveLeft = false;
//                    }
//                }
//                if ((pacManInner.getX() + .5 <= horizontalWalls.get(i).getX())) {
//                    if (pacManInner.getBounds().overlaps(horizontalWalls.get(i).getBounds())) {
//                        moveRight = false;
//                    }
//                }
//                if ((pacManInner.getY() + .5 >= horizontalWalls.get(i).getY())) {
//                    if (pacManInner.getBounds().overlaps(horizontalWalls.get(i).getBounds())) {
//                        moveDown = false;
//                    }
//                }
//                if ((pacManInner.getY() + .5 <= horizontalWalls.get(i).getY())) {
//                    if (pacManInner.getBounds().overlaps(horizontalWalls.get(i).getBounds())) {
//                        moveUp = false;
//                    }
//                }
//            }
        }
    }

    private void determineWhereToMoveInner() {

    }

    private void drawWalls() {
        for (int i = 0; i < horizontalWalls.size; i++) {

            renderer.rect(horizontalWalls.get(i).getX(), horizontalWalls.get(i).getY(), horizontalWalls.get(i).getWidth(), horizontalWalls.get(i).getHeight());


        }
    }

    private void drawCoins(){
        for (int i = 0; i < coins.size(); i++) {

            renderer.rect(coins.get(i).getX(), coins.get(i).getY(), coins.get(i).getWidth(), coins.get(i).getHeight());


        }
    }


    private void drawPlayer(ShapeRenderer renderer) {
//        pacMan.setPosition(17, 13);
//        pacMan.setSize(2, 2);
        pacMan.drawPacMan(renderer);

    }

    private void drawPlayerInner(ShapeRenderer renderer) {
        pacManInner.drawPacMan(renderer);
    }

    private void movePacMan(float delta) {

        // under old approach had up and moveUp == true

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && moveUp == true) {
            direction = 1;
            float y = pacMan.getY();
            y += delta * 5;
            pacMan.setPosition(pacMan.getX(), y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && moveDown == true) {
            direction = 2;
            float y = pacMan.getY();
            y -= delta * 5;
            pacMan.setPosition(pacMan.getX(), y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && moveRight == true) {
            direction = 3;
            float x = pacMan.getX();
            x += delta * 5;
            pacMan.setPosition(x, pacMan.getY());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && moveLeft == true) {
            direction = 4;
            float x = pacMan.getX();
            x -= delta * 5;
            pacMan.setPosition(x, pacMan.getY());
        }
    }

    private void movePacManInner(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && moveUp == true) {
            float y = pacManInner.getY();
            y += delta * 5;
            pacManInner.setPosition(pacManInner.getX(), y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && moveDown == true) {
            float y = pacManInner.getY();
            y -= delta * 5;
            pacManInner.setPosition(pacManInner.getX(), y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && moveRight == true) {
            float x = pacManInner.getX();
            x += delta * 5;
            pacManInner.setPosition(x, pacManInner.getY());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && moveLeft == true) {
            float x = pacManInner.getX();
            x -= delta * 5;
            pacManInner.setPosition(x, pacManInner.getY());
        }
    }


    private void renderDebug() {
        if (flag == false) {
            ViewportUtils.drawGrid(viewport, renderer, GameConfig.CELL_SIZE);
        }
        if (true) {
            flag = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) {
            flag = false;
        }
    }
}

package com.mygdx.game.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.entity.Enemy;
import com.mygdx.game.entity.EnemyController;
import com.mygdx.game.entity.EnemyController2;
import com.mygdx.game.entity.EnemyController3;
import com.mygdx.game.entity.EnemyController4;
import com.mygdx.game.entity.Ground;
import com.mygdx.game.entity.Player;
import com.mygdx.game.util.ViewportUtils;
import com.mygdx.game.util.debug.DebugCameraController;

import java.util.ArrayList;

public class GameRenderer implements Disposable {

    private final GameController controller;

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;
    private SpriteBatch batch;

    private DebugCameraController debugCameraController;
    private boolean flag;
    private boolean groundflag;

    private float y = 3;
    private float x;
    Player player;
    public static boolean flagJump;
    public boolean flagPlayer;
    public boolean flagEnemy;
    public boolean flagEnemy2;

    public float xMinus;
    public float xBefore = 3;

    public float playerMinus;
    public float playerBefore = 3;
    public Enemy enemy;
    public Enemy enemy2;
    float z1;
    float z2;
    public Array<Enemy> enemies = new Array<Enemy>();

    public Array<Ground> groundArray = new Array<Ground>();
    float dir = 1;
    float dir2 = 2;

    float direction;
    public boolean stopRight;
    public boolean stopLeft;
    EnemyController ec;
    EnemyController2 ec2;
    EnemyController3 ec3;
    EnemyController4 ec4;
    private Texture sky;
    private Texture ground1;
    private Texture ground2;
    private Texture ground3;
    private Texture ground4;

    float skyX;
    float skyX2 = skyX + 1600;
    float skyX3 = skyX2 + 1600;
    float skyX4 = skyX3 + 1600;
    float skyX5 = skyX4 + 1600;

    float groundX;
    float groundX2 = groundX + 1600;
    float groundX3 = groundX + 1600;
    float groundX4 = groundX + 1760;

    GameScreen gameScreen;
    AssetManager assetManager;

    private Animation playerAnimation;
    private TextureRegion playerRegion;
    private Animation enemyAnimation;


    public GameRenderer(GameController controller, GameScreen gameScreen) {
        this.controller = controller;
        this.gameScreen = gameScreen;
        this.assetManager = gameScreen.getAssetManager();
        init();
    }

    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
        player = new Player(this);
        ec = new EnemyController();
        ec2 = new EnemyController2();
        ec3 = new EnemyController3();
        ec4 = new EnemyController4();
        batch = new SpriteBatch();
        sky = new Texture(Gdx.files.internal("sky.png"));
        ground1 = new Texture(Gdx.files.internal("ground1.png"));
        ground2 = new Texture(Gdx.files.internal("ground2.png"));
        ground3 = new Texture(Gdx.files.internal("ground3.png"));
        ground4 = new Texture(Gdx.files.internal("ground3.png"));

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);

        playerAnimation = new Animation(0.1f,
                gamePlayAtlas.findRegions(RegionNames.PLAYER),
                Animation.PlayMode.LOOP_PINGPONG);

        enemyAnimation = new Animation(0.1f,
                gamePlayAtlas.findRegions(RegionNames.ENEMY),
                Animation.PlayMode.LOOP_PINGPONG);

        playerRegion = gamePlayAtlas.findRegions(RegionNames.PLAYER).first();

        initGround();
        initEnemies();
    }

    // == public methods ==

    public void updateY() {
        float y = player.getY() + player.getSpeed();
        player.setY(y);
    }

    public void render(float delta) {
        if (groundflag == false) {
            debugCameraController.handleDebugInput(delta);
            debugCameraController.applyTo(camera);
        }


        viewport.apply();
        batch.begin();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !stopRight) {
            skyX -= (float) (7.5 * delta) * 32;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !stopLeft) {
            skyX += (float) (7.5 * delta) * 32;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !stopRight) {
            skyX2 -= (float) (7.5 * delta) * 32;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !stopLeft) {
            skyX2 += (float) (7.5 * delta) * 32;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !stopRight) {
            skyX3 -= (float) (7.5 * delta) * 32;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !stopLeft) {
            skyX3 += (float) (7.5 * delta) * 32;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !stopRight) {
            skyX4 -= (float) (7.5 * delta) * 32;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !stopLeft) {
            skyX4 += (float) (7.5 * delta) * 32;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !stopRight) {
            skyX5 -= (float) (7.5 * delta) * 32;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !stopLeft) {
            skyX5 += (float) (7.5 * delta) * 32;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !stopRight && !stopLeft) {
            groundX -= (float) (7.5 * delta) * 32;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !stopLeft && !stopRight) {
            groundX += (float) (7.5 * delta) * 32;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !stopRight && !stopLeft) {
            groundX2 -= (float) (7.5 * delta) * 32;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !stopLeft && !stopRight) {
            groundX2 += (float) (7.5 * delta) * 32;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !stopRight && !stopLeft) {
            groundX3 -= (float) (7.5 * delta) * 32;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !stopLeft && !stopRight) {
            groundX3 += (float) (7.5 * delta) * 32;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !stopRight && !stopLeft) {
            groundX4 -= (float) (7.5 * delta) * 32;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !stopLeft && !stopRight) {
            groundX4 += (float) (7.5 * delta) * 32;
        }
        System.out.println("skyx" + skyX);


        batch.draw(sky, skyX, 0, 1600, 960);
        batch.draw(sky, skyX2, 0, 1600, 960);
        batch.draw(sky, skyX3, 0, 1600, 960);
        batch.draw(sky, skyX4, 0, 1600, 960);
        batch.draw(sky, skyX5, 0, 1600, 960);

        batch.draw(ground1, groundX, 0, 1600, 96);
        batch.draw(ground1, groundX + 1600, 0);
        batch.draw(ground1, groundX + 3200, 0);
        batch.draw(ground1, groundX + 4800, 0);
        batch.draw(ground1, groundX + 6400, 0);
        batch.draw(ground2, groundX2, 96, (float) 480.5, 64);
        batch.draw(ground3, groundX3, 0, (float) 480.5, 96);
        batch.draw(ground4, groundX4, 160, 160, 64);

        batch.draw(ground2, groundX + 3072, 96, (float) 480.5, 64);
        batch.draw(ground4, groundX + 3232, 160, (160), 64);

        batch.draw(ground2, groundX + 4512, 96, (float) 4800.5, 64);
        batch.draw(ground4, groundX + 4672, 160, (float) 4800.5, 64);

        player.update(delta, xMinus);
        if (flagPlayer == false) {
            renderPlayer(delta);
        }

        PlayerCollisionWithEnemies(delta);

        batch.end();

        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);


        renderGround();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player.isWalking()) {
            player.jump();
        }

        renderer.rect(player.getX(), player.getY(), 1, 1);
//
//        player.update(delta, xMinus);
//        if (flagPlayer == false) {
//            renderPlayer(delta);
//        }
        if (flagEnemy == false) {
            renderEnemy(delta, renderer);

        }
        if (flagEnemy2 == false) {
            renderEnemy2(delta);
        }
//        PlayerCollisionWithEnemies(delta);
        renderClouds();

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
    private void renderGround() {
        for (int i = 0; i < groundArray.size; i++) {
            Ground ground = groundArray.get(i);
            renderer.rect(ground.getX(), ground.getY(), ground.getWidth(), ground.getHeight());
        }
    }

    private void renderPlayer(float delta) {
        // float y = player.getSpeed() + 3;

        for (int i = 0; i < groundArray.size; i++) {
            if (player.getX() >= groundArray.get(i).getX()) {

                xMinus = groundArray.get(i).getY();

            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                direction = 1;
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                direction = 2;
            }

            // was wrong greater than and less that getX coordinates, fixed

            if (player.isWalking() && player.getX() >= GameConfig.WORLD_WIDTH - 1 && player.getY() < 5 && Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getX() < GameConfig.WORLD_WIDTH) {
                groundflag = true;
                stopRight = true;
            } else if (player.isWalking() && player.getX() >= GameConfig.WORLD_WIDTH + 5 - 1 && player.getY() < 7 && Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getX() < GameConfig.WORLD_WIDTH + 5) {
                groundflag = true;
                stopRight = true;
            } else if (player.isWalking() && player.getX() <= GameConfig.WORLD_WIDTH + 10 && player.getY() < 7 && Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getX() > GameConfig.WORLD_WIDTH + 10 - 1) {
                groundflag = true;
                stopLeft = true;
            } else if (player.isWalking() && player.getX() <= GameConfig.WORLD_WIDTH + 15 && player.getY() < 5 && Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getX() > GameConfig.WORLD_WIDTH + 15 - 1) {
                groundflag = true;
                stopLeft = true;
            } else if (player.isWalking() && player.getX() >= GameConfig.WORLD_WIDTH + 46 - 1 && player.getY() < 5 && Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getX() < GameConfig.WORLD_WIDTH + 46) {
                groundflag = true;
                stopRight = true;
            } else if (player.isWalking() && player.getX() >= GameConfig.WORLD_WIDTH + 51 - 1 && player.getY() < 7 && Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getX() < GameConfig.WORLD_WIDTH + 51) {
                groundflag = true;
                stopRight = true;
            } else if (player.isWalking() && player.getX() <= GameConfig.WORLD_WIDTH + 56 && player.getY() < 7 && Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getX() > GameConfig.WORLD_WIDTH + 56 - 1) {
                groundflag = true;
                stopLeft = true;
            } else if (player.isWalking() && player.getX() <= GameConfig.WORLD_WIDTH + 61 && player.getY() < 5 && Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getX() > GameConfig.WORLD_WIDTH + 61 - 1) {
                groundflag = true;
                stopLeft = true;
            } else if (player.isWalking() && player.getX() >= GameConfig.WORLD_WIDTH + 91 - 1 && player.getY() < 5 && Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getX() < GameConfig.WORLD_WIDTH + 91) {
                groundflag = true;
                stopRight = true;
            } else if (player.isWalking() && player.getX() >= GameConfig.WORLD_WIDTH + 96 - 1 && player.getY() < 7 && Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getX() < GameConfig.WORLD_WIDTH + 96) {
                groundflag = true;
                stopRight = true;
            } else {
                if (stopRight == true && direction == 1) {
                    groundflag = true;
                } else if (stopLeft == true && direction == 2) {
                    groundflag = true;
                } else {
                    groundflag = false;
                    stopRight = false;
                    stopLeft = false;
                }
            }


        }


        x = player.getSpeed() + y;

        TextureRegion playerAnimationRegion = (TextureRegion) playerAnimation.getKeyFrame(controller.getAnimationTime());

        player.setPosition(camera.position.x, x);
        player.setSize(1, 1);
        //renderer.rect(player.getX(), player.getY(), 1, 1);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !stopRight && player.isWalking()) {
            batch.draw(playerAnimationRegion,
                    GameConfig.WORLD_CENTER_X * 32, x * 32,
                    32, 32
            );
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !stopLeft && player.isWalking()) {
            batch.draw(playerAnimationRegion,
                    GameConfig.WORLD_CENTER_X * 32, x * 32,
                    32, 32
            );
        } else {
            batch.draw(playerRegion,
                    GameConfig.WORLD_CENTER_X * 32, x * 32,
                    32, 32);
        }

    }

    public void renderEnemy(float delta, ShapeRenderer renderer) {
        ec.render(delta, renderer);
        ec2.render(delta, renderer);
        ec3.render(delta, renderer);
        ec4.render(delta, renderer);

    }

    public void renderEnemy2(float delta) {

    }

    public void PlayerCollisionWithEnemies(float delta) {

        ArrayList<Enemy> e = ec.getEnemies();
        for (int i = 0; i < e.size(); i++) {
            Rectangle enemyRect = e.get(i).getBounds();
            TextureRegion enemyAnimationRegion = (TextureRegion) enemyAnimation.getKeyFrame(controller.getAnimationTime());
            batch.draw(enemyAnimationRegion,
                    e.get(i).getX() * 32 - player.getX() * 32 +25*32, 96,
                    32, 32
            );
            System.out.println("Player x: " + player.getX());
            if (player.getBounds().overlaps(enemyRect) && player.isWalking()) {
                if (e.get(i).getCode() == 0 && flagEnemy == false) {
                    flagPlayer = true;
                }
                if (e.get(i).getCode() == 1 && flagEnemy2 == false) {
                    flagPlayer = true;
                }
            }
            System.out.println(e.get(i).getCode());
            if (player.getBounds().overlaps(enemyRect) && player.isFalling()) {

                ec.removeEnemy(e.get(i));

            }
        }

        ArrayList<Enemy> e2 = ec2.getEnemies();
        for (int i = 0; i < e2.size(); i++) {
            Rectangle enemyRect = e2.get(i).getBounds();

            TextureRegion enemyAnimationRegion = (TextureRegion) enemyAnimation.getKeyFrame(controller.getAnimationTime());
            batch.draw(enemyAnimationRegion,
                    e2.get(i).getX() * 32 - player.getX() * 32 +25*32, 96,
                    32, 32
            );

            if (player.getBounds().overlaps(enemyRect) && player.isWalking()) {
                if (e2.get(i).getCode() == 0 && flagEnemy == false) {
                    flagPlayer = true;
                }
                if (e2.get(i).getCode() == 1 && flagEnemy2 == false) {
                    flagPlayer = true;
                }
            }
            System.out.println(e2.get(i).getCode());
            if (player.getBounds().overlaps(enemyRect) && player.isFalling()) {

                ec2.removeEnemy(e2.get(i));

            }
        }

        ArrayList<Enemy> e3 = ec3.getEnemies();
        for (int i = 0; i < e3.size(); i++) {
            Rectangle enemyRect = e3.get(i).getBounds();
            TextureRegion enemyAnimationRegion = (TextureRegion) enemyAnimation.getKeyFrame(controller.getAnimationTime());
            batch.draw(enemyAnimationRegion,
                    e3.get(i).getX() * 32 - player.getX() * 32 +25*32, 96,
                    32, 32
            );
            if (player.getBounds().overlaps(enemyRect) && player.isWalking()) {
                if (e3.get(i).getCode() == 0 && flagEnemy == false) {
                    flagPlayer = true;
                }
                if (e3.get(i).getCode() == 1 && flagEnemy2 == false) {
                    flagPlayer = true;
                }
            }
            System.out.println(e3.get(i).getCode());
            if (player.getBounds().overlaps(enemyRect) && player.isFalling()) {

                ec3.removeEnemy(e3.get(i));

            }
        }

        ArrayList<Enemy> e4 = ec4.getEnemies();
        for (int i = 0; i < e4.size(); i++) {
            Rectangle enemyRect = e4.get(i).getBounds();
            TextureRegion enemyAnimationRegion = (TextureRegion) enemyAnimation.getKeyFrame(controller.getAnimationTime());
            batch.draw(enemyAnimationRegion,
                    e4.get(i).getX() * 32 - player.getX() * 32 +25*32, 224,
                    32, 32
            );
            if (player.getBounds().overlaps(enemyRect) && player.isWalking()) {
                if (e4.get(i).getCode() == 0 && flagEnemy == false) {
                    flagPlayer = true;
                }
                if (e4.get(i).getCode() == 1 && flagEnemy2 == false) {
                    flagPlayer = true;
                }
            }
            System.out.println(e4.get(i).getCode());
            if (player.getBounds().overlaps(enemyRect) && player.isFalling()) {

                ec4.removeEnemy(e4.get(i));

            }
        }

    }

    public void setY(float y) {
        this.y = y;
    }

    public Array<Ground> getGroundArray() {
        return groundArray;
    }


    private void renderClouds() {
        renderer.rect(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y, 8, 4);
        renderer.rect(GameConfig.WORLD_WIDTH, GameConfig.WORLD_CENTER_Y + 3, 8, 5);
        renderer.rect(GameConfig.WORLD_WIDTH + GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_CENTER_Y + 1, 6, 4);
        renderer.rect(GameConfig.WORLD_WIDTH * 2, GameConfig.WORLD_CENTER_Y - 1, 7, 4);
        renderer.rect((GameConfig.WORLD_WIDTH * 2) + 35, GameConfig.WORLD_CENTER_Y + 2, 6, 4);
        renderer.rect((GameConfig.WORLD_WIDTH * 3 + 20), GameConfig.WORLD_CENTER_Y + 2, 6, 4);
    }


    private void initGround() {
        Ground ground = new Ground();
        ground.setPosition(0, 3);
        ground.setSize(GameConfig.WORLD_WIDTH, (float) .01);
        groundArray.add(ground);
        Ground ground2 = new Ground();
        ground2.setPosition(GameConfig.WORLD_WIDTH, 5);
        ground2.setSize(5, (float) .01);
        groundArray.add(ground2);
        Ground ground3 = new Ground();
        ground3.setPosition(GameConfig.WORLD_WIDTH + 5, 7);
        ground3.setSize(5, (float) .01);
        groundArray.add(ground3);
        Ground ground4 = new Ground();
        ground4.setPosition(GameConfig.WORLD_WIDTH + 10, 5);
        ground4.setSize(5, (float) .01);
        groundArray.add(ground4);
        Ground ground5 = new Ground();
        ground5.setPosition(GameConfig.WORLD_WIDTH + 15, 3);
        ground5.setSize(31, (float) .01);
        groundArray.add(ground5);
        Ground ground6 = new Ground();
        ground6.setPosition(GameConfig.WORLD_WIDTH + 46, 5);
        ground6.setSize(5, (float) .01);
        groundArray.add(ground6);
        Ground ground7 = new Ground();
        ground7.setPosition(GameConfig.WORLD_WIDTH + 51, 7);
        ground7.setSize(5, (float) .01);
        groundArray.add(ground7);
        Ground ground8 = new Ground();
        ground8.setPosition(GameConfig.WORLD_WIDTH + 56, 5);
        ground8.setSize(5, (float) .01);
        groundArray.add(ground8);
        Ground ground9 = new Ground();
        ground9.setPosition(GameConfig.WORLD_WIDTH + 61, 3);
        ground9.setSize(30, (float) .01);
        groundArray.add(ground9);
        Ground ground10 = new Ground();
        ground10.setPosition(GameConfig.WORLD_WIDTH + 91, 5);
        ground10.setSize(5, (float) .01);
        groundArray.add(ground10);
        Ground ground11 = new Ground();
        ground11.setPosition(GameConfig.WORLD_WIDTH + 96, 7);
        ground11.setSize(5, (float) .01);
        groundArray.add(ground11);
        Ground ground12 = new Ground();
        ground12.setPosition(GameConfig.WORLD_WIDTH + 101, 7);
        ground12.setSize(50, (float) .01);
        groundArray.add(ground12);

    }

    private void initEnemies() {
        for (int i = 0; i < 2; i++) {
            enemy = new Enemy();
            float rand = GameConfig.WORLD_WIDTH - 5;
            if (i > 0) {
                rand -= 10;
            }
//                    MathUtils.random(0, GameConfig.WORLD_WIDTH);
            enemy.setPosition(rand, 3);
            enemy.setSize(1, 1);
            ec.addEnemy(enemy);
        }

        for (int i = 0; i < 2; i++) {
            enemy = new Enemy();
            float rand = GameConfig.WORLD_WIDTH - 5 + 46;
            if (i > 0) {
                rand -= 10;
            }
//                    MathUtils.random(0, GameConfig.WORLD_WIDTH);
            enemy.setPosition(rand, 3);
            enemy.setSize(1, 1);
            ec2.addEnemy(enemy);
        }

        for (int i = 0; i < 2; i++) {
            enemy = new Enemy();
            float rand = GameConfig.WORLD_WIDTH - 5 + 91;
            if (i > 0) {
                rand -= 10;
            }
//                    MathUtils.random(0, GameConfig.WORLD_WIDTH);
            enemy.setPosition(rand, 3);
            enemy.setSize(1, 1);
            ec3.addEnemy(enemy);
        }

        for (int i = 0; i < 2; i++) {
            enemy = new Enemy();
            float rand = GameConfig.WORLD_WIDTH - 5 + 91;
            if (i > 0) {
                rand -= 10;
            }
//                    MathUtils.random(0, GameConfig.WORLD_WIDTH);
            enemy.setPosition(rand, 7);
            enemy.setSize(1, 1);
            ec4.addEnemy(enemy);
        }


    }


    private void renderDebug() {
        if (flag == false) {
            ViewportUtils.drawGrid(viewport, renderer, GameConfig.CELL_SIZE);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            flag = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) {
            flag = false;
        }
    }
}

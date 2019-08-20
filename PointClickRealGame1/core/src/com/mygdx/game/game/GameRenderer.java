package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.entity.Bullet;
import com.mygdx.game.entity.Enemy;
import com.mygdx.game.entity.EnemyBullet;
import com.mygdx.game.entity.Player;
import com.mygdx.game.util.ViewportUtils;
import com.mygdx.game.util.debug.DebugCameraController;

import java.util.ArrayList;
import java.util.Iterator;

public class GameRenderer implements Disposable {

    private final GameController controller;

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private DebugCameraController debugCameraController;

    Array<Bullet> bullets;
    Array<EnemyBullet> enemyBullets;
    Array<Bullet> bullets2;
    public float angle;
    public float specificAngle;
    public float deltaX;
    public float deltaY;
    public boolean flag;
    public boolean flagEnemiesLoop;
    public boolean flagIntersect;
    public boolean flagEnemyBug;
    public boolean flagPlayerDead;
    public boolean flagEnemyDeadForBullet;


    public float cameraCenterX;
    public float cameraCenterY;
    public float cameraAdjustedX;
    public float cameraAdjustedY;
    public Player player;
    public Enemy enemy;
    public int[] record = new int[300];
    public int h;
    public ArrayList enemies = new ArrayList();

    float[] randomX = new float[50];
    float[] randomY = new float[50];
    public int enemyBulletCounter;

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
        bullets = new Array<Bullet>();
        enemyBullets = new Array<EnemyBullet>();
        player = new Player();

        for (int i = 0; i < 30; i++) {
            randomX[i] = MathUtils.random(-GameConfig.WORLD_WIDTH, GameConfig.WORLD_WIDTH);
            randomY[i] = MathUtils.random(-GameConfig.WORLD_HEIGHT, GameConfig.WORLD_HEIGHT);

        }
        for (int i = 0; i < 30; i++) {
            enemy = new Enemy();
            enemy.setPosition(randomX[i], randomY[i]);
            enemy.setSize(1, 1);

            enemies.add(enemy);
        }
    }

    // == public methods ==
    public void render(float delta) {
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderEnemies();
        renderPlayer();
        intersectEnemiesPlayer();
        shootingEnemies(delta);

        shoot();
        renderer.end();

        renderDebug();
    }

    public void shootingEnemies(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = (Enemy) enemies.get(i);
            if(enemy.getFlag() == false && enemy.getFlag2() == true) {
                enemyBulletCounter = MathUtils.random(0, 10000);
                if (enemyBulletCounter < 60) {
                    EnemyBullet b = new EnemyBullet();
                    angle = MathUtils.random(0, 360);
                    b.setAngle(angle);
                    b.position.set(enemy.getX(), enemy.getY());
                    b.setIdentity(i);

                    enemyBullets.add(b);


                }
            }

        }

        for (Iterator<EnemyBullet> itr = enemyBullets.iterator(); itr.hasNext(); ) {
            EnemyBullet b = itr.next();
            specificAngle = b.getAngle();

            b.updateAngle(specificAngle);

            renderer.rect(b.position.x, b.position.y, (float) .25, (float) .25);
            if (b.getBounds().overlaps(player.getBounds())) {
                flagPlayerDead = true;
            }

            if (b.position.y > Gdx.graphics.getHeight() || b.position.x > Gdx.graphics.getWidth()) {
                itr.remove();
            }
        }
    }


    public void intersectEnemiesPlayer() {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = (Enemy) enemies.get(i);
            if (enemy.getBounds().overlaps(player.getBounds()) && (enemy.getFlag() == false)) {
                flagPlayerDead = true;
            }


        }
    }

    private void renderEnemies() {

        // one enemy not being rendered, but firing

        for (int i = 0; i < 30; i++) {

            Color oldColor = renderer.getColor().cpy();
            renderer.setColor(Color.RED);
            bullets2 = getBullets();
            Rectangle EnemyRect = new Rectangle(randomX[i], randomY[i], 1, 1);

            enemy = (Enemy) enemies.get(i);


            for (int j = 0; j < bullets2.size; j++) {
                Bullet bullet = bullets2.get(j);
                if (bullet.getBounds().overlaps(enemy.getBounds()) && (enemy.getFlag() == false)) {
//                    flagIntersect = true;
                    enemy.setFlag(true);


                    record[h++] = i;

                } else {
                    flagIntersect = false;
                }

            }
            for (int g = 0; g < record.length; g++) {
                if (record[g] == i) {

                    flagIntersect = true;

                }
            }
            if (flagIntersect == false) {
                // need to remove actual enemy or else there is bug

                renderer.rect(randomX[i], randomY[i], 1, 1);
                for(int u = 0; u < enemies.size(); u++){
                    Enemy eenemy = (Enemy) enemies.get(i);
                    eenemy.setFlag2(true);
                }

            }
            flagIntersect = false;


            renderer.setColor(oldColor);


        }

    }

    private void renderPlayer() {
        cameraCenterX = (float) (camera.position.x - .5);
        cameraCenterY = (float) (camera.position.y - .5);
        player.setPosition(cameraCenterX, cameraCenterY);
        player.setSize(1, 1);

        if (!flagPlayerDead) {
            renderer.rect(cameraCenterX, cameraCenterY, 1, 1);
        }

    }

    private void shoot() {
        if (Gdx.input.justTouched()) {


            deltaX = (Gdx.input.getX());

            deltaY = (Gdx.graphics.getHeight() - Gdx.input.getY());
            System.out.println(" x: " + Gdx.input.getX() + "delta x: " + deltaX + "camera x: " + camera.position.x);
//            angle = MathUtils.atan2(deltaY-(32*GameConfig.WORLD_CENTER_Y), deltaX-(32*GameConfig.WORLD_CENTER_X));
            angle = MathUtils.atan2(deltaY - (32 * GameConfig.WORLD_CENTER_Y), deltaX - (32 * GameConfig.WORLD_CENTER_X));
            System.out.println("angle: " + angle + "camera.x: " + 32 * camera.position.x);
            Bullet b = new Bullet();
            b.setAngle(angle);
            b.position.set(camera.position.x, camera.position.y);

            bullets.add(b);
        }
        for (Iterator<Bullet> itr = bullets.iterator(); itr.hasNext(); ) {
            Bullet b = itr.next();
            specificAngle = b.getAngle();

            b.updateAngle(specificAngle);

            renderer.rect(b.position.x, b.position.y, (float) .25, (float) .25);
//            renderer.line(25, b.position.y, 25, b.position.y + 15);
            if (b.position.y > Gdx.graphics.getHeight() || b.position.x > Gdx.graphics.getWidth()) {
                itr.remove();
            }
        }
    }

    public Array<Bullet> getBullets() {
        return bullets;
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
    private void renderDebug() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            flag = true;
        }
        if (flag == false) {
            ViewportUtils.drawGrid(viewport, renderer, GameConfig.CELL_SIZE);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) {
            flag = false;
        }
    }
}

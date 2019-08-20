package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.entity.Bullet;
import com.mygdx.game.entity.Enemy;
import com.mygdx.game.entity.Player;
import com.mygdx.game.util.ViewportUtils;
import com.mygdx.game.util.debug.DebugCameraController;

import java.util.Iterator;

public class GameRenderer implements Disposable {

    private final GameController controller;

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private DebugCameraController debugCameraController;

    Array<Bullet> bullets;
    public float angle;
    public float specificAngle;
    public float deltaX;
    public float deltaY;
    public boolean flag;
    public float cameraCenterX;
    public float caeraCenterY;
    public float cameraAdjustedX;
    public float cameraAdjustedY;
    public Player player;
    private float enemyTimer;
    Array<Enemy> enemies;
    SpriteBatch batch;
    Texture background;
    Texture spaceShip;
    Texture enemyShip;
    public AssetManager assetManager;

    private boolean isPlayerDead;

    private ParticleEffectPool fireEffectPool;
    private Array<ParticleEffectPool.PooledEffect> effects = new Array<ParticleEffectPool.PooledEffect>();

    public GameRenderer(GameController controller, AssetManager assetManager) {
        this.assetManager = assetManager;

        ParticleEffect effect = this.assetManager.get("realFire.pfx");
        fireEffectPool = new ParticleEffectPool(effect, 5, 20);
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
        enemies = new Array<Enemy>();
        player = new Player(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y, 1, 1);
        batch = new SpriteBatch();
        background = new Texture("SpaceBackground.png");
        spaceShip = new Texture("SpaceShip.png");
        enemyShip = new Texture("EnemyShip.png");
        // enemies.add(new Enemy(5*32, 25*32, 1));
        player.setPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
    }

    // == public methods ==
    public void render(float delta) {
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        viewport.apply();
        batch.begin();
        drawBackground();
        drawPlayerBatch(batch);
        drawEnemyBatch(batch);


        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        cameraCenterX = (float) (camera.position.x - .5);
        caeraCenterY = (float) (camera.position.y - .5);

        movePlayer(delta);
        wrapPlayer();
        drawPlayer(cameraCenterX, caeraCenterY);
        spawnEnemy(delta);
        moveEnemy(delta);
        drawEnemy(batch);
        bulletEnemyCollision();
        for (int i = 0; i < effects.size; i++) {
            ParticleEffectPool.PooledEffect effect = effects.get(i);
            effect.update(delta);
            if(effect.isComplete()){
                effects.removeIndex(i);
                effect.free();
            }

        }

        playerEnemyCollision();

        Array<ParticleEffectPool.PooledEffect> effects = getEffects();
        for(int i = 0; i< effects.size; i++){
            ParticleEffectPool.PooledEffect effect = effects.get(i);
            effect.draw(batch);
        }

        batch.end();


        if (Gdx.input.justTouched()) {


            deltaX = (Gdx.input.getX());

            deltaY = (Gdx.graphics.getHeight() - Gdx.input.getY());
            System.out.println(" x: " + Gdx.input.getX() + "delta x: " + deltaX + "camera x: " + camera.position.x);
//            angle = MathUtils.atan2(deltaY-(32*GameConfig.WORLD_CENTER_Y), deltaX-(32*GameConfig.WORLD_CENTER_X));
            angle = MathUtils.atan2(deltaY - (32 * player.getY()), deltaX - (32 * player.getX()));
            System.out.println("angle: " + angle + "camera.x: " + 32 * camera.position.x);
            Bullet b = new Bullet();
            b.setAngle(angle);
            b.position.set(player.getX(), player.getY());
            b.setPosition(player.getX(), player.getY());
            b.setSize(.25f, .25f);
            bullets.add(b);
        }
        for (Iterator<Bullet> itr = bullets.iterator(); itr.hasNext(); ) {
            Bullet b = itr.next();
            specificAngle = b.getAngle();

            b.updateAngle(specificAngle);
            b.setPosition(b.position.x, b.position.y);
            drawBullet(b.position.x, b.position.y);
//            renderer.line(25, b.position.y, 25, b.position.y + 15);
            if (b.position.y > Gdx.graphics.getHeight() || b.position.x > Gdx.graphics.getWidth()) {
                itr.remove();
            }
        }
        renderer.end();

        renderDebug();

//        Array<ParticleEffectPool.PooledEffect> effects = getEffects();
//        for(int i = 0; i< effects.size; i++){
//            ParticleEffectPool.PooledEffect effect = effects.get(i);
//        }
    }

    private void drawBackground() {
        batch.draw(background, 0, 0, 1600, 960);
    }

    public Array<ParticleEffectPool.PooledEffect> getEffects() {
        return effects;
    }

    private void drawEnemyBatch(SpriteBatch batch) {

    }

    private void drawPlayerBatch(SpriteBatch batch) {
        if (isPlayerDead == false) {
            batch.draw(spaceShip, player.getX() * 32 - player.getWidth() / 2, player.getY() * 32 - player.getHeight() / 2, player.getWidth() * 32, player.getHeight() * 32);
        }
    }

    private void bulletEnemyCollision() {
        int h = enemies.size;
        for (int i = 0; i < enemies.size; i++) {
            for (int j = 0; j < bullets.size; j++) {
                if (i < enemies.size && j < bullets.size) {
                    if (bullets.get(j).getBounds().overlaps(enemies.get(i).getBounds())) {

                        // create effect
                        float effectX = enemies.get(i).getX() + enemies.get(i).getWidth() / 2f;
                        float effectY = enemies.get(i).getY() + enemies.get(i).getHeight() / 2f;

                        spawnFireEffect(effectX*32, effectY*32);

                        enemies.removeIndex(i);
                        System.out.println("hit");
                        System.out.println("hit");
                    }
                }

            }
        }

        // next step is intersect enemies with player
    }

    private void playerEnemyCollision() {
        for (int i = 0; i < enemies.size; i++) {
            if (enemies.get(i).getBounds().overlaps(player.getBounds())) {
                isPlayerDead = true;
                float effectX = player.getX() + player.getWidth() / 2f;
                float effectY = player.getY() + player.getHeight() / 2f;

                spawnFireEffect(effectX*32, effectY*32);

            }
        }
    }

    private void moveEnemy(float delta) {
        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i);
            if (enemies.get(i).getRandGen() == 1) {
                enemies.get(i).update1(delta);
            } else if (enemies.get(i).getRandGen() == 2) {
                enemies.get(i).update2(delta);
            } else if (enemies.get(i).getRandGen() == 3) {
                enemies.get(i).update3(delta);
            } else if (enemies.get(i).getRandGen() == 4) {
                enemies.get(i).update4(delta);
            }
        }
    }


    private void spawnEnemy(float delta) {
        enemyTimer += delta;
        // set position in enemy constructor with random digit off stage

        if (enemyTimer >= 1) {
            float randGen = MathUtils.random(1, 4);
            if (randGen == 1) {
                float randX = MathUtils.random(0 - 1, GameConfig.WORLD_WIDTH + 1);
                //float randY = MathUtils.random(0 - 1, GameConfig.WORLD_HEIGHT + 1);
                Enemy enemy = new Enemy(randX, GameConfig.WORLD_HEIGHT + 2, randGen);
                enemies.add(enemy);
                enemyTimer = 0;
            } else if (randGen == 2) {
                float randX = MathUtils.random(0 - 1, GameConfig.WORLD_WIDTH + 1);
                //float randY = MathUtils.random(0 - 1, GameConfig.WORLD_HEIGHT + 1);
                Enemy enemy = new Enemy(randX, -2, randGen);
                enemies.add(enemy);
                enemyTimer = 0;
            } else if (randGen == 3) {
//                float randX = MathUtils.random(0 - 1, GameConfig.WORLD_WIDTH + 1);
                float randY = MathUtils.random(0 - 1, GameConfig.WORLD_HEIGHT + 1);
                Enemy enemy = new Enemy(-1, randY, randGen);
                enemies.add(enemy);
                enemyTimer = 0;
            } else if (randGen == 4) {
                float randY = MathUtils.random(0 - 1, GameConfig.WORLD_HEIGHT + 1);
                Enemy enemy = new Enemy(GameConfig.WORLD_WIDTH + 1, randY, randGen);
                enemies.add(enemy);
                enemyTimer = 0;
            }

        }
    }

    private void drawEnemy(SpriteBatch batch) {
        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i);
            float x = enemies.get(i).getX();
            float y = enemies.get(i).getY();
            float width = enemies.get(i).getWidth();
            float height = enemies.get(i).getHeight();
            batch.draw(enemyShip, x * 32 - width / 2, y * 32 - height / 2, width * 39, height * 39);
            renderer.rect(enemies.get(i).getX(), enemies.get(i).getY(), enemies.get(i).getWidth(), enemies.get(i).getHeight());

        }
        //  renderer.rect(enemies.get(0).getX(), enemies.get(0).getY(), enemies.get(0).getWidth(), enemies.get(0).getHeight());
        //  batch.draw(enemyShip, enemies.get(0).getX() + enemies.get(0).getWidth()*32/2, enemies.get(0).getY() + enemies.get(0).getHeight()*32/2,enemies.get(0).getWidth()*32, enemies.get(0).getHeight()*32);
    }

    private void movePlayer(float delta) {
        // do diagnals before
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            float y2 = player.getY();
            float y = y2 += delta * 1.5;
            float x2 = player.getX();
            float x = x2 += delta * 2.5;
            player.setPosition(x, y);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            float y2 = player.getY();
            float y = y2 += delta * 1.5;
            float x2 = player.getX();
            float x = x2 -= delta * 2.5;
            player.setPosition(x, y);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            float y2 = player.getY();
            float y = y2 -= delta * 1.5;
            float x2 = player.getX();
            float x = x2 += delta * 2.5;
            player.setPosition(x, y);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            float y2 = player.getY();
            float y = y2 -= delta * 1.5;
            float x2 = player.getX();
            float x = x2 -= delta * 2.5;
            player.setPosition(x, y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            float y2 = player.getY();
            float y = y2 += delta * 4;
            player.setPosition(player.getX(), y);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            float y2 = player.getY();
            float y = y2 -= delta * 4;
            player.setPosition(player.getX(), y);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            float x2 = player.getX();
            float x = x2 += delta * 4;
            player.setPosition(x, player.getY());
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            float x2 = player.getX();
            float x = x2 -= delta * 4;
            player.setPosition(x, player.getY());
        }
    }

    private void wrapPlayer() {
        // see if width or world width
        System.out.println(player.getX());
        if (player.getX() >= GameConfig.WORLD_WIDTH) {
            player.setPosition(0, player.getY());
        } else if (player.getX() <= 0) {
            player.setPosition(GameConfig.WORLD_WIDTH, player.getY());
        } else if (player.getY() >= GameConfig.WORLD_HEIGHT) {
            player.setPosition(player.getX(), 0);
        } else if (player.getY() <= 0) {
            player.setPosition(player.getX(), GameConfig.WORLD_HEIGHT);
        }
    }

    private void drawPlayer(float cameraCenterX, float caeraCenterY) {
//        renderer.rect(cameraCenterX, caeraCenterY, (float).25, (float).25);
        if (isPlayerDead == false) {
            renderer.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        }
    }

    private void drawBullet(float cameraCenterX, float caeraCenterY) {
        renderer.rect(cameraCenterX, caeraCenterY, (float) .25, (float) .25);
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

    public ParticleEffectPool.PooledEffect createFire(float x, float y) {
        ParticleEffectPool.PooledEffect effect = fireEffectPool.obtain();
        effect.setPosition(x, y);
        effect.start();
        return effect;
    }

    public void spawnFireEffect(float x, float y) {
        ParticleEffectPool.PooledEffect effect = createFire(x, y);
        effects.add(effect);
    }

}

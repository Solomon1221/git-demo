package com.mygdx.game.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.entity.Bullet;
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
    }

    // == public methods ==
    public void render(float delta) {
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        cameraCenterX = (float) (camera.position.x - .5);
        caeraCenterY = (float) (camera.position.y - .5);
        renderer.rect(cameraCenterX, caeraCenterY, 1, 1);

        if (Gdx.input.justTouched()) {


            deltaX = (Gdx.input.getX());

            deltaY = (Gdx.graphics.getHeight() - Gdx.input.getY());
            System.out.println(" x: " + Gdx.input.getX() + "delta x: " + deltaX + "camera x: " + camera.position.x);
//            angle = MathUtils.atan2(deltaY-(32*GameConfig.WORLD_CENTER_Y), deltaX-(32*GameConfig.WORLD_CENTER_X));
            angle = MathUtils.atan2(deltaY-(32*GameConfig.WORLD_CENTER_Y), deltaX-(32*GameConfig.WORLD_CENTER_X));
            System.out.println("angle: " + angle + "camera.x: " + 32*camera.position.x);
            Bullet b = new Bullet();
            b.setAngle(angle);
            b.position.set(camera.position.x, camera.position.y);
            bullets.add(b);
        }
        for (Iterator<Bullet> itr = bullets.iterator(); itr.hasNext(); ) {
            Bullet b = itr.next();
            specificAngle = b.getAngle();

            b.updateAngle(specificAngle);

            renderer.rect(b.position.x, b.position.y, 1, 1);
//            renderer.line(25, b.position.y, 25, b.position.y + 15);
            if (b.position.y > Gdx.graphics.getHeight() || b.position.x > Gdx.graphics.getWidth()) {
                itr.remove();
            }
        }
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

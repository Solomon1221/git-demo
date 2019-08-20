package com.mygdx.game.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.util.GdxUtils;

public class GameScreen extends ScreenAdapter {

    private final MyGdxGame game;
    private final AssetManager assetManager;

    private GameController controller;
    private GameRenderer renderer;

    public GameScreen(MyGdxGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    // == public methods ==

    @Override
    public void show() {
        controller = new GameController();
        renderer = new GameRenderer(controller);
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();

        controller.update(delta);
        renderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}

package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.screen.game.GameScreen;

public class MyGdxGame extends Game {

    private AssetManager assetManger;
    private SpriteBatch batch;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        assetManger = new AssetManager();
        assetManger.getLogger().setLevel(Logger.DEBUG);

        batch = new SpriteBatch();
        setScreen(new GameScreen(this));
    }



    @Override
    public void dispose() {
        super.dispose();
        assetManger.dispose();
        batch.dispose();
    }

    public AssetManager getAssetManger() {
        return assetManger;
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}

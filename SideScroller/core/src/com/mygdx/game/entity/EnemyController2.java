package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.config.GameConfig;

import java.util.ArrayList;

public class EnemyController2 {

    public float dir = 1;
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    public EnemyController2() {
    }

    public void render(float delta, ShapeRenderer renderer) {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.render(renderer, enemy.getX(), enemy.getY(), enemy.width, enemy.height);
            update(enemy, delta);
        }
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    public void removeEnemy(Enemy e) {
        enemies.remove(e);
    }

    public void update(Enemy e, float delta) {
        float x = e.getX();
        if (x <= GameConfig.WORLD_WIDTH + 15 || dir == 1) {
            x += delta * 5;

        } else if (e.getX() >= GameConfig.WORLD_WIDTH + 46 || dir == 2) {
            x -= delta * 5;

        }

        if (x <= GameConfig.WORLD_WIDTH + 15) {
            dir = 1;
        } else if (x >= GameConfig.WORLD_WIDTH - 1 + 46) {
            dir = 2;
        }


        e.setPosition(x, e.getY());
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
}

package com.mygdx.game.entity;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.config.GameConfig;

public class Enemy extends EntityBase {
    public float randGen;
    public float y1;
    public float x1;
    public float superX;
    public float superY;
    public boolean flag;

    public Enemy(float x, float y, float randGen) {
        this.x = x;
        this.y = y;
        this.randGen = randGen;
    }

    private void stopHalf() {
        if (flag == false) {
            superX = getX();
            superY = getY();
            flag = true;
        }
    }


    public void update1(float delta) {
        stopHalf();
        float randX = MathUtils.random(1, 2);
        float randY = MathUtils.random(1, 5);
        float x = getX();
        if (superX < GameConfig.WORLD_CENTER_X) {
            x = x += delta*2;
        } else if (superX > GameConfig.WORLD_CENTER_X) {
            x = x -= delta*2;
        }
        y1 = getY();
        y1 = y1 -= delta * 5;
        setPosition(x, y1);
    }

    public void update2(float delta) {
        stopHalf();
        float randX = MathUtils.random(1, 2);
        float randY = MathUtils.random(1, 5);
        float x = getX();
        if (superX < GameConfig.WORLD_CENTER_X) {
            x = x += delta*2;
        } else if (superX > GameConfig.WORLD_CENTER_X) {
            x = x -= delta*2;
        }
        y1 = getY();
        y1 = y1 += delta * 5;
        setPosition(x, y1);
    }

    public void update3(float delta) {
        // three starts from left side
        stopHalf();
        float randX = MathUtils.random(1, 2);
        float randY = MathUtils.random(1, 5);
        float x = getX();
        if (superY < GameConfig.WORLD_CENTER_Y) {
            y = y += delta*2;
        } else if (superY > GameConfig.WORLD_CENTER_Y) {
            y = y -= delta*2;
        }
        x1 = getX();
        x1 = x1 += delta * 5;
        setPosition(x1, y);
    }

    public void update4(float delta) {
        stopHalf();
        float randX = MathUtils.random(1, 2);
        float randY = MathUtils.random(1, 5);
        float x = getX();
        if (superY < GameConfig.WORLD_CENTER_Y) {
            y = y += delta*2;
        } else if (superY > GameConfig.WORLD_CENTER_Y) {
            y = y -= delta*2;
        }
        x1 = getX();
        x1 = x1 -= delta * 5;
        setPosition(x1, y);
    }

    public float getRandGen() {
        return randGen;
    }
}

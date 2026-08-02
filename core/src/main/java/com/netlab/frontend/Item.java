package com.netlab.frontend;

import com.badlogic.gdx.graphics.Color;

public class Item extends GameObject {
    private String itemType;

    public Item(float x, float y, String itemType) {
        super(x, y, 16, 16, 100f, Color.WHITE);
        this.itemType = itemType;
    }

    public Item(float x, float y, float width, float height, float speed, String itemType) {
        super(x, y, width, height, speed, Color.WHITE);
        this.itemType = itemType;
    }

    @Override
    public void update(float delta) {
        // Linear movement downwards
        this.y -= speed * delta;
    }

    public String getItemType() { return itemType; }
}

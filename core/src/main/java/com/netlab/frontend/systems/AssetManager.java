package com.netlab.frontend.systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private static AssetManager instance;
    private Map<String, TextureRegion> textureRegionMap;
    private Map<String, Animation<TextureRegion>> animationMap;
    private Map<String, Texture> textureMap;

    private AssetManager() {
        textureRegionMap = new HashMap<>();
        animationMap = new HashMap<>();
        textureMap = new HashMap<>();
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    // 1. O(1) Dynamic Flyweight Lookups (No switch statements)
    public TextureRegion getTextureRegion(String key) {
        return textureRegionMap.get(key);
    }

    public Animation<TextureRegion> getAnimation(String key) {
        return animationMap.get(key);
    }

    // 2. Generic Registration Methods
    public Texture loadTexture(String filename) {
        if (!textureMap.containsKey(filename)) {
            textureMap.put(filename, new Texture(filename));
        }
        return textureMap.get(filename);
    }

    public void registerRegion(String key, TextureRegion region) {
        textureRegionMap.put(key, region);
    }

    public void registerRegionFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int col) {
        Texture tex = loadTexture(filename);
        TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);
        textureRegionMap.put(key, grid[row][col]);
    }

    public void registerAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int numFrames, float frameDuration) {
        Texture tex = loadTexture(filename);
        TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);

        TextureRegion[] frames = new TextureRegion[numFrames];
        for (int i = 0; i < numFrames; i++) {
            frames[i] = grid[row][i];
        }

        Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
        anim.setPlayMode(Animation.PlayMode.LOOP);

        animationMap.put(key, anim);
        textureRegionMap.put(key, frames[0]); // Default first frame
    }

    public void dispose() {
        for (Texture tex : textureMap.values()) {
            tex.dispose();
        }
        textureMap.clear();
        textureRegionMap.clear();
        animationMap.clear();
    }
}

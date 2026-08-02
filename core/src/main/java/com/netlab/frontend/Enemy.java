package com.netlab.frontend;

import com.badlogic.gdx.graphics.Color;

public class Enemy extends GameObject {
    protected String name;
    protected int hp;
    protected int maxHp;

    public Enemy(String name, int hp) {
        super(200, 380, 24, 24, 0, Color.PINK);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
    }

    public Enemy(float x, float y, float width, float height, Color color, String name, int hp) {
        super(x, y, width, height, 0, color);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(name + " took " + damage + " damage! HP: " + this.hp + "/" + this.maxHp);
        if (this.hp == 0) {
            System.out.println(name + " was defeated!");
        }
    }

    public void attack(Player player, int damage) {
        System.out.println(name + " unleashes bullet barrage on " + player.getName() + "!");
        player.takeDamage(damage);
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    // Encapsulation getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, hp); }

    public int getMaxHp() { return maxHp; }
}

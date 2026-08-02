package com.netlab.frontend;

import com.badlogic.gdx.graphics.Color;

public class Player extends GameObject {
    private String name;
    private int hp;
    private int power;
    private int spellCards;

    public Player(String name, int hp, int power, int spellCards) {
        super(280, 40, 32, 32, 0, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 32, 0, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
    }

    public void shoot(Enemy target) {
        int damage = 10 + power;
        System.out.println(name + " shoots " + target.getName() + " dealing " + damage + " DMG!");
        target.takeDamage(damage);
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(name + " took " + damage + " damage! Remaining HP: " + this.hp);
        if (this.hp == 0) {
            System.out.println(name + " was defeated (Pichuun~)! ");
        }
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    // Encapsulation getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, hp); }

    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }

    public int getSpellCards() { return spellCards; }
    public void setSpellCards(int spellCards) { this.spellCards = spellCards; }
}

package com.netlab.frontend;

class Player {
    String name;
    int hp;
    int powerLevel = 0;
    int score = 0;

    Player(String name, int hp) {
        this.name = name;
        this.hp = hp;
    }

    void shoot(Enemy enemy, int dmg) {
        enemy.takeDamage(dmg);
    }
}

class Enemy {
    String name;
    int hp;

    Enemy(String name, int hp) {
        this.name = name;
        this.hp = hp;
    }

    void takeDamage(int dmg) {
        System.out.println(name + " takes " + dmg + " DMG!");
        this.hp = -dmg;
        if (this.hp <= 0) {
            System.out.println(name + " dies!");
        }
    }
}

class Test {
    public static void  main(String[] args) {
        Player player = new Player("Akbar", 10);
        Enemy enemy = new Enemy("Perry", 10);

        player.shoot(enemy, 10);
    }
}


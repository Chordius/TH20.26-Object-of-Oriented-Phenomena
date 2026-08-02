package com.netlab.frontend;

public class Test {
    public static void main(String[] args) {
        System.out.println("=== TOUHOU OOP PRACTICUM - MODULE 2: ENCAPSULATION & INHERITANCE ===");

        // 1. Instantiating polymorphic objects
        Player reimu = new Player("Reimu Hakurei", 100, 15, 3);
        Fairy fairy = new Fairy("Stage 1 Fairy", 20);
        Boss cirno = new Boss("Cirno (Stage 2 Boss)", 150);
        Item item = new Item(200, 450, "Power Item");

        System.out.println("\n--- Testing Encapsulation (Getters & Inheritance) ---");
        System.out.println("Player: " + reimu.getName() + " | Position: (" + reimu.getX() + ", " + reimu.getY() + ")");
        System.out.println("Fairy:  " + fairy.getName() + " | Color: " + fairy.getColor());
        System.out.println("Boss:   " + cirno.getName() + " | Size: " + cirno.getWidth() + "x" + cirno.getHeight());
        System.out.println("Item:   " + item.getItemType() + " | Y-Pos: " + item.getY() + " | Speed: " + item.getSpeed());

        System.out.println("\n--- Testing Movement Logic (Item update) ---");
        System.out.println("Initial Item Y: " + item.getY());
        item.update(0.5f); // simulate 0.5s passing
        System.out.println("Item Y after 0.5s update: " + item.getY() + " (linear downward movement)");

        System.out.println("\n--- Testing Battle Mechanics & Method Overrides ---");
        reimu.shoot(fairy);
        cirno.attack(reimu, 30);

        System.out.println("\n=== Module 2 Test Completed Successfully ===");
    }
}

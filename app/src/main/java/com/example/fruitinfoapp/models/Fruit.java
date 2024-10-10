package com.example.fruitinfoapp.models;

public class Fruit {
    private final String name;
    private final Nutritions nutritions;

    // Constructor for a creating the fruit object
    public Fruit(String name, Nutritions nutritions) {
        this.name = name;
        this.nutritions = nutritions;
    }

    // Get String name
    public String getName() {
        return name;
    }

    // Gets the nutrition information of the specific fruit
    public Nutritions getNutritions() {
        return nutritions;
    }

    // Nested class to represent the nutrition information of the specific fruit
    public static class Nutritions {
        private final double calories;
        private final double protein;
        private final double sugar;

        // Constructor for creating the nutrition object
        public Nutritions(double calories, double sugar, double protein) {
            this.calories = calories;
            this.protein = protein;
            this.sugar = sugar;
        }

        // Getters to get the calories, protein and sugar of the fruit
        public double getCalories() {
            return calories;
        }

        public double getProtein() {
            return protein;
        }
        public double getSugar() {
            return sugar;
        }
    }
}

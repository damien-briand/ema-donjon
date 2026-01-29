package org.example.model;

import org.example.interfaces.Attackable;
import org.example.interfaces.Healable;

public abstract class Creature implements Attackable, Healable {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int attackPower;

    public Creature(String name, int maxHealth, int attackPower) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attackPower = attackPower;
    }

    @Override
    public void attack(Attackable target) {
        int damage = calculateDamage();
        target.takeDamage(damage);
        System.out.println(name + " attaque pour " + damage + " dégâts!"); //TODO : change to a Logger class after
    }

    @Override
    public void takeDamage(int damage) {
        health = Math.max(0, health - damage); // TODO : faire en sorte de mettre une chance de pas mettre de degats en fonctions de l'écart du niveaux (et aussi coup critique)
        System.out.println(name + " reçoit " + damage + " dégâts! HP: " + health);
    }

    @Override
    public void heal(int amount){
        health = Math.min(maxHealth, health + amount);
        System.out.println(name + " récupère " + amount + " HP! HP : " + health); //TODO : change to a Logger class after
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    protected abstract int calculateDamage(); // Polymorphisme !

    public String getName() { return name; }
    public int getHealth() { return health; }


}

package org.example.model;

import org.example.interfaces.Attackable;
import org.example.interfaces.Healable;

public abstract class Creature implements Attackable, Healable {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int attackPower;

    public Creature(String name, int maxHealth, int attackPower){
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attackPower = attackPower;
    }

    @Override
    public void attack(Attackable target) {
        int damage = calculateDamage();
        target.takeDamage(damage);
        System.out.println(name + " attaque pour " + damage + " dégâts!"); //todo : change to a Logger class after
    }

    @Override
    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
        System.out.println(name + " reçoit " + damage + " dégâts! HP: " + health);
    }

    protected abstract int calculateDamage();
}

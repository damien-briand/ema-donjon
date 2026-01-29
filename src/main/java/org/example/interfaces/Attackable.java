package org.example.interfaces;

public interface Attackable {
    void attack(Attackable target);
    void takeDamage(int damage);
    boolean isAlive();
}

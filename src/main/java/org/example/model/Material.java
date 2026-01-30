package org.example.model;

public class Material extends Item {
    private int quantity;

    public Material(String name, String description, int quantity) {
        super(name, description);
        this.quantity = quantity;
    }

    @Override
    public void use() {
        System.out.println(name + " est un matériau de craft. Quantité: " + quantity);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
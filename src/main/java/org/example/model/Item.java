package org.example.model;

public abstract class Item {
    protected String name;
    protected String description;

    public Item() {}

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void use();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

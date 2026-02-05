package org.example.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Potion.class, name = "potion"),
        @JsonSubTypes.Type(value = Weapon.class, name = "weapon"),
        @JsonSubTypes.Type(value = Material.class, name = "material"),
        @JsonSubTypes.Type(value = Armor.class, name = "armor")
})
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

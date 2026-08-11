package net.straws11.egyptianpast.item;

import net.minecraft.util.StringRepresentable;

public enum OrganType implements StringRepresentable {
    EMPTY("empty"),
    LIVER("liver"),
    LUNGS("lungs"),
    STOMACH("stomach"),
    INTESTINES("intestines");

    private final String name;

    OrganType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}

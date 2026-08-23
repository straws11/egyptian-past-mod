package net.straws11.egyptianpast.item;

import net.minecraft.util.StringRepresentable;

public enum ScrollType implements StringRepresentable  {
    BLANK("blank"),
    SUN_STRIKE("sun_strike");

    private final String name;

    ScrollType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}

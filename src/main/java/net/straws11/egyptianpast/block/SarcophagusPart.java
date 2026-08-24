package net.straws11.egyptianpast.block;

import net.minecraft.util.StringRepresentable;

public enum SarcophagusPart implements StringRepresentable {
    HEAD("head"),
    MIDDLE("middle"),
    FEET("feet");

    private final String name;

    SarcophagusPart(String part) {
        this.name = part;
    }
    @Override
    public String getSerializedName() {
        return this.name;
    }
}

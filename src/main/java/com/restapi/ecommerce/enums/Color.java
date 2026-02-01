package com.restapi.ecommerce.enums;

public enum Color {
    RED(1),
    ORANGE(2),
    YELLOW(3),
    BLUE(4),
    PINK(5),
    PURPLE(6),
    WHITE(7),
    MULTI_COLOR(8);

	private final int value;

	private Color(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}

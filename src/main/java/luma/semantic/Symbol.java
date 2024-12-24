package luma.semantic;

import luma.parser.Expr;

public class Symbol {
    private final String name;
    private final String type;
    private final boolean isFunction;
    private final Expr value;

    public Symbol(String name, String type, boolean isFunction, Expr value) {
        this.name = name;
        this.type = type;
        this.isFunction = isFunction;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isFunction() {
        return isFunction;
    }

    public Expr getValue() {
        return value;
    }
}

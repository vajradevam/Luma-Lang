package luma.parser;

import luma.lexer.Token;

public class IdentifierExpr extends Expr {
    public String name;

    public IdentifierExpr(Token token, String name) {
        super(token);
        this.name = name;
    }
}

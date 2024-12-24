package luma.parser;

import luma.lexer.Token;

public class LiteralExpr extends Expr {
    public String value;

    public LiteralExpr(Token token, String value) {
        super(token);
        this.value = value;
    }
}

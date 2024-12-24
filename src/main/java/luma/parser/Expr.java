package luma.parser;

import luma.lexer.Token;

public abstract class Expr {
    Token token;

    public Expr(Token token) {
        this.token = token;
    }
}

package luma.parser;

import luma.lexer.Token;

public class BinaryExpr extends Expr {
    public Expr left;
    public Expr right;
    public String operator;

    public BinaryExpr(Token token, Expr left, String operator, Expr right) {
        super(token);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}

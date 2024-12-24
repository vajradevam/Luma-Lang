package luma.parser;

public class ReturnStmt extends Stmt {
    public Expr value;

    public ReturnStmt(Expr value) {
        this.value = value;
    }
}

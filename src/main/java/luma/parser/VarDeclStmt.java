package luma.parser;

public class VarDeclStmt extends Stmt {
    public String name;
    public Expr initializer;

    public VarDeclStmt(String name, Expr initializer) {
        this.name = name;
        this.initializer = initializer;
    }
}

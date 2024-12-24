package luma.parser;

public class IfStmt extends Stmt {
    public Expr condition;
    public Stmt thenBranch;
    public Stmt elseBranch;

    public IfStmt(Expr condition, Stmt thenBranch, Stmt elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
}

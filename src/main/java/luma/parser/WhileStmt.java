package luma.parser;

public class WhileStmt extends Stmt {
    private final Expr condition;
    private final Stmt body;

    public WhileStmt(Expr condition, Stmt body) {
        this.condition = condition;
        this.body = body;
    }

    public Expr getCondition() {
        return condition;
    }

    public Stmt getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "WhileStmt{" +
                "condition=" + condition +
                ", body=" + body +
                '}';
    }
}

package luma.parser;

import java.util.List;

public class BlockStmt extends Stmt {
    public List<Stmt> statements;

    public BlockStmt(List<Stmt> statements) {
        this.statements = statements;
    }
}

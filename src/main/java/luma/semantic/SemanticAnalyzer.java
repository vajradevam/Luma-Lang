package luma.semantic;

import luma.parser.*;

import java.util.List;

public class SemanticAnalyzer {
    private final SymbolTable symbolTable;

    public SemanticAnalyzer() {
        this.symbolTable = new SymbolTable();
    }

    public void analyze(List<Stmt> statements) {
        for (Stmt statement : statements) {
            analyze(statement);
        }
    }

    private void analyze(Stmt stmt) {
        if (stmt instanceof VarDeclStmt) {
            analyzeVarDeclStmt((VarDeclStmt) stmt);
        } else if (stmt instanceof BlockStmt) {
            analyzeBlockStmt((BlockStmt) stmt);
        } else if (stmt instanceof IfStmt) {
            analyzeIfStmt((IfStmt) stmt);
        } else if (stmt instanceof ReturnStmt) {
            analyzeReturnStmt((ReturnStmt) stmt);
        }
        // Add more cases for other statement types
    }

    private void analyzeVarDeclStmt(VarDeclStmt stmt) {
        String name = stmt.name;
        Expr initializer = stmt.initializer;
        Symbol symbol = new Symbol(name, "var", false, initializer); // You can extend this to handle types
        symbolTable.declare(name, symbol);
    }

    private void analyzeBlockStmt(BlockStmt stmt) {
        symbolTable.pushScope();
        for (Stmt innerStmt : stmt.statements) {
            analyze(innerStmt);
        }
        symbolTable.popScope();
    }

    private void analyzeIfStmt(IfStmt stmt) {
        analyzeExpr(stmt.condition);
        analyze(stmt.thenBranch);
        if (stmt.elseBranch != null) {
            analyze(stmt.elseBranch);
        }
    }

    private void analyzeReturnStmt(ReturnStmt stmt) {
        analyzeExpr(stmt.value);
    }

    private void analyzeExpr(Expr expr) {
        if (expr instanceof IdentifierExpr) {
            IdentifierExpr identifier = (IdentifierExpr) expr;
            symbolTable.resolve(identifier.name);
        } else if (expr instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr) expr;
            analyzeExpr(binaryExpr.left);
            analyzeExpr(binaryExpr.right);
        } else if (expr instanceof LiteralExpr) {
            // Literal expressions don't require analysis
        }
        // Add more cases for other expression types
    }
}

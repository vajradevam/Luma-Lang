package luma.interpreter;

import luma.lexer.TokenType;
import luma.semantic.SemanticError;
import luma.parser.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {

    private final Map<String, Object> globals = new HashMap<>();
    private final Map<String, Object> locals = new HashMap<>();

    public void interpret(List<Stmt> statements) {
        try {
            for (Stmt stmt : statements) {
                execute(stmt);
            }
        } catch (RuntimeException e) {
            System.out.println("Runtime error: " + e.getMessage());
        }
    }

    private void execute(Stmt stmt) {
        if (stmt instanceof VarDeclStmt) {
            executeVarDecl((VarDeclStmt) stmt);
        } else if (stmt instanceof BlockStmt) {
            executeBlock((BlockStmt) stmt);
        } else if (stmt instanceof IfStmt) {
            executeIf((IfStmt) stmt);
        } else if (stmt instanceof ReturnStmt) {
            executeReturn((ReturnStmt) stmt);
        } else if (stmt instanceof WhileStmt) {
            visitWhileStmt((WhileStmt) stmt);
        } else {
            throw new RuntimeException("Unknown statement type: " + stmt);
        }
    }

    public Void visitWhileStmt(WhileStmt stmt) {
        while (isTruthy(evaluate(stmt.getCondition()))) {
            execute(stmt.getBody());
        }
        return null;
    }

    private void executeVarDecl(VarDeclStmt stmt) {
        Object value = evaluate(stmt.initializer);
        locals.put(stmt.name, value);
    }

    private void executeBlock(BlockStmt stmt) {
        Map<String, Object> previousScope = new HashMap<>(locals);
        try {
            for (Stmt innerStmt : stmt.statements) {
                execute(innerStmt);
            }
        } finally {
            locals.clear();
            locals.putAll(previousScope);
        }
    }

    private void executeIf(IfStmt stmt) {
        Object condition = evaluate(stmt.condition);
        if (isTruthy(condition)) {
            execute(stmt.thenBranch);
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }
    }

    private void executeReturn(ReturnStmt stmt) {
        Object value = evaluate(stmt.value);
        System.out.println("Returned: " + value);
        throw new ReturnException(value);
    }

    public Object evaluate(Expr expr) {
        if (expr instanceof LiteralExpr) {
            Object value = ((LiteralExpr) expr).value;
            if (value instanceof String) {
                // Attempt to parse numbers from string literals
                try {
                    return Double.parseDouble((String) value);
                } catch (NumberFormatException e) {
                    // If it's not a number, return as-is (e.g., a plain string)
                    return value;
                }
            }
            return value;
        } else if (expr instanceof IdentifierExpr) {
            String name = ((IdentifierExpr) expr).name;
            if (locals.containsKey(name)) {
                return locals.get(name);
            } else if (globals.containsKey(name)) {
                return globals.get(name);
            } else {
                throw new SemanticError("Variable '" + name + "' is not defined.");
            }
        } else if (expr instanceof BinaryExpr) {
            return evaluateBinary((BinaryExpr) expr);
        }
        throw new RuntimeException("Unknown expression type: " + expr);
    }

    private Object evaluateBinary(BinaryExpr expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);
        return switch (expr.operator) {
            case "+" -> (double) left + (double) right;
            case "-" -> (double) left - (double) right;
            case "*" -> (double) left * (double) right;
            case "/" -> {
                if ((double) right == 0) {
                    throw new RuntimeException("Division by zero.");
                }
                yield (double) left / (double) right;
            }
            case "==" -> left.equals(right);
            case "!=" -> !left.equals(right);
            case ">" -> (double) left > (double) right;
            case "<" -> (double) left < (double) right;
            case ">=" -> (double) left >= (double) right;
            case "<=" -> (double) left <= (double) right;
            default -> throw new RuntimeException("Unknown operator: " + expr.operator);
        };
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean) object;
        return true;
    }

    private static class ReturnException extends RuntimeException {
        private final Object value;

        public ReturnException(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }
}

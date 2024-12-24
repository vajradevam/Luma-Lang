package luma.semantic;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SymbolTable {
    private final Stack<Map<String, Symbol>> scopes;

    public SymbolTable() {
        scopes = new Stack<>();
        pushScope(); // Add the global scope
    }

    public void pushScope() {
        scopes.push(new HashMap<>());
    }

    public void popScope() {
        if (scopes.size() > 1) {
            scopes.pop();
        } else {
            throw new IllegalStateException("Cannot remove the global scope.");
        }
    }

    public void declare(String name, Symbol symbol) {
        if (scopes.peek().containsKey(name)) {
            throw new SemanticError("Variable or function '" + name + "' is already declared in this scope.");
        }
        scopes.peek().put(name, symbol);
    }

    public Symbol resolve(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name)) {
                return scopes.get(i).get(name);
            }
        }
        throw new SemanticError("Variable or function '" + name + "' is not declared.");
    }
}

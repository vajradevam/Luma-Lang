package luma;

import luma.interpreter.Interpreter;
import luma.lexer.Lexer;
import luma.lexer.Token;
import luma.parser.Parser;
import luma.parser.Stmt;
import luma.semantic.SemanticAnalyzer;

import java.util.List;

public class LumaInterpreter {

    public static void main(String[] args) {
        String sourceCode = """
                var x = 0;
                if (2 < 3) {
                    var x = x + 1;
                }
                return x;
                """;

        Lexer lexer = new Lexer(sourceCode);
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);

        List<Stmt> statements;
        try {
            statements = parser.parse();
            System.out.println("Parsed successfully: " + statements.size() + " statements.");
        } catch (RuntimeException e) {
            System.out.println("Error during parsing: " + e.getMessage());
            return;
        }

        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        try {
            semanticAnalyzer.analyze(statements);
            System.out.println("Semantic analysis passed successfully.");
        } catch (RuntimeException e) {
            System.out.println("Error during semantic analysis: " + e.getMessage());
            return;
        }

        Interpreter interpreter = new Interpreter();
        System.out.println("Executing program:");
        interpreter.interpret(statements);
    }
}

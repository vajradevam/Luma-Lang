package luma.parser;

import luma.lexer.Token;
import luma.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(statement());
        }
        return statements;
    }

    private Stmt statement() {
        if (match(TokenType.VAR)) {
            return varDeclaration();
        } else if (match(TokenType.FUNCTION)) {
            return functionDeclaration();
        } else if (match(TokenType.IF)) {
            return ifStatement();
        } else if (match(TokenType.RETURN)) {
            return returnStatement();
        } else if (match(TokenType.LBRACE)) {
            return block();
        } else if (match(TokenType.WHILE)) {
            return whileStatement();
        }
        throw new RuntimeException("Unexpected token: " + peek().getValue());
    }

    private Stmt block() {
        List<Stmt> statements = new ArrayList<>();

        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            statements.add(statement());
        }

        consume(TokenType.RBRACE, "Expect '}' after block.");
        return new BlockStmt(statements);
    }

    private Stmt varDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expect variable name.");
        consume(TokenType.ASSIGN, "Expect '=' after variable name.");
        Expr initializer = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after variable declaration.");
        return new VarDeclStmt(name.getValue(), initializer);
    }

    private Stmt functionDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expect function name.");
        consume(TokenType.LPAREN, "Expect '(' after function name.");
        // Function parameter parsing can be added here
        consume(TokenType.RPAREN, "Expect ')' after function parameters.");
        consume(TokenType.LBRACE, "Expect '{' before function body.");
        List<Stmt> body = new ArrayList<>();
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            body.add(statement());
        }
        consume(TokenType.RBRACE, "Expect '}' after function body.");
        return new BlockStmt(body);
    }

    private Stmt whileStatement() {
        consume(TokenType.LPAREN, "Expect '(' after 'while'.");
        Expr condition = expression();
        consume(TokenType.RPAREN, "Expect ')' after condition.");
        Stmt body = statement();
        return new WhileStmt(condition, body);
    }

    private Stmt ifStatement() {
        consume(TokenType.LPAREN, "Expect '(' after 'if'.");
        Expr condition = expression();
        consume(TokenType.RPAREN, "Expect ')' after condition.");
        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (match(TokenType.ELSE)) {
            elseBranch = statement();
        }
        return new IfStmt(condition, thenBranch, elseBranch);
    }

    private Stmt returnStatement() {
        Expr value = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after return statement.");
        return new ReturnStmt(value);
    }

    private Expr expression() {
        return equality();  // Start with equality operator (the lowest precedence)
    }

    private Expr equality() {
        Expr expr = comparison();
        while (match(TokenType.EQUALS, TokenType.NOT_EQUALS)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new BinaryExpr(operator, expr, operator.getValue(), right);
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = term();
        while (match(TokenType.GREATER_THAN, TokenType.GREATER_THAN_EQUALS, TokenType.LESS_THAN, TokenType.LESS_THAN_EQUALS)) {
            Token operator = previous();
            Expr right = term();
            expr = new BinaryExpr(operator, expr, operator.getValue(), right);
        }
        return expr;
    }

    private Expr term() {
        Expr expr = factor();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new BinaryExpr(operator, expr, operator.getValue(), right);
        }
        return expr;
    }

    private Expr factor() {
        Expr expr = unary();
        while (match(TokenType.MULTIPLY, TokenType.DIVIDE)) {
            Token operator = previous();
            Expr right = unary();
            expr = new BinaryExpr(operator, expr, operator.getValue(), right);
        }
        return expr;
    }

    private Expr unary() {
        if (match(TokenType.MINUS)) {
            Token operator = previous();
            Expr right = primary();
            return new BinaryExpr(operator, new LiteralExpr(operator, "0"), operator.getValue(), right);
        }
        return primary();
    }

    private Expr primary() {
        if (match(TokenType.NUMBER)) {
            return new LiteralExpr(previous(), previous().getValue());
        } else if (match(TokenType.IDENTIFIER)) {
            return new IdentifierExpr(previous(), previous().getValue());
        }
        throw new RuntimeException("Unexpected token: " + peek().getValue());
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw new RuntimeException(message);
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }
}

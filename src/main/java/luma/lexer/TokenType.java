package luma.lexer;

public enum TokenType {
    // Keywords
    FUNCTION("function"),
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    RETURN("return"),
    VAR("var"),

    // Operators
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    ASSIGN("="),
    EQUALS("=="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_EQUALS(">="),
    LESS_THAN_EQUALS("<="),

    // Delimiters
    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    SEMICOLON(";"),
    COMMA(","),

    // Literals
    IDENTIFIER,
    NUMBER,
    STRING,

    // Special tokens
    EOF,
    ERROR;

    private final String lexeme;

    TokenType() {
        this.lexeme = null;
    }

    TokenType(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }
}

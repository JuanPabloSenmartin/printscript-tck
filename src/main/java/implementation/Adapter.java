package implementation;


import ast.AST;
import interpreter.Interpreter;
import lexer.Lexer;
import parser.Parser;
import token.Token;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class Adapter {
    private final Interpreter interpreter;

    List<String> errors = new ArrayList<>();


    public Adapter(InputStream input, Double version) {
        this.interpreter = createInterpreter(input, version);
        try{
            interpreter.interpret();
        }catch (Exception e){
            errors.add(e.getMessage());
        }

    }

    private Interpreter createInterpreter(InputStream input, Double version) {
        List<Token> tokens = new ArrayList<>();
        try{
            tokens = Lexer.tokenize(input, version);
        }catch (Exception e){
            errors.add(e.getMessage());
        }
        AST ast = new AST(new ArrayList<>());
        try{
            ast = new Parser(tokens, version).parse();
        }catch (Exception e){
            errors.add(e.getMessage());
        }
        return new Interpreter(ast);
    }


    public List<String> getErrors() {
        return errors;
    }

    public List<String> getPrintedMessages() {
        return interpreter.getPrinted();
    }
}

package implementation;


import ast.AST;
import interpreter.InputProvider;
import interpreter.Interpreter;
import lexer.Lexer;
import parser.Parser;
import token.Token;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class Adapter {
    private final Interpreter interpreter;

    List<String> errors = new ArrayList<>();


    public Adapter(InputStream input, Double version, InputProvider provider) {
        this.interpreter = createInterpreter(input, version);
        //add input
        String i = provider.input("");
        if (i != null){
            String readInput = i;
            InputStream in = new ByteArrayInputStream(readInput.getBytes());
            System.setIn(in);
        }

        try{
            interpreter.interpret();
        }catch (Exception e){
            errors.add(e.getMessage());
        }

    }

    private Interpreter createInterpreter(InputStream input, Double version) {
        List<Token> tokens = new ArrayList<>();
        try{
            Lexer lexer = new Lexer(input, version);
            tokens = lexer.tokenize();
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

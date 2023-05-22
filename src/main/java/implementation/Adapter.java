package implementation;


import ast.AST;
import interpreter.Interpreter;
import lexer.Lexer;
import org.apache.commons.io.FileUtils;
import parser.Parser;
import token.Token;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class Adapter {
    private final Interpreter interpreter;

    List<String> errors = new ArrayList<>();


    public Adapter(File src, Double version) {
        this.interpreter = createInterpreter(src, version);
        try{
            interpreter.interpret();
        }catch (Exception e){
            errors.add(e.getMessage());
        }

    }

    private Interpreter createInterpreter(File src, Double version) {
        String input;
        try {
            input = FileUtils.readFileToString(src, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Token> tokens = new ArrayList<>();
        try{
            tokens = Lexer.tokenize(new ByteArrayInputStream(input.getBytes()), version);
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

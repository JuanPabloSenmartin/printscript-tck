package implementation;


import interpreter.Interpreter;
import lexer.Lexer;
import lexer.Token;
import org.apache.commons.io.FileUtils;
import parser.AST;
import parser.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Adapter {
    private Interpreter interpreter;

    List<String> errors = new ArrayList<>();

    List<String> print = new ArrayList<>();


    public Adapter(File src, String version) {
        this.interpreter = createInterpreter(src, version);
        try{
            interpreter.interpret();
        }catch (Exception e){
            errors.add(e.getMessage());
        }

    }

    private Interpreter createInterpreter(File src, String version) {
        String input;
        try {
            input = FileUtils.readFileToString(src, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Token> tokens = new ArrayList<>();
        try{
            tokens = Lexer.tokenize(input, Double.parseDouble(version));
        }catch (Exception e){
            errors.add(e.getMessage());
        }
        AST ast = new AST(new ArrayList<>());
        try{
            ast = new Parser(tokens).parse(Double.parseDouble(version));
        }catch (Exception e){
            errors.add(e.getMessage());
        }
        return new Interpreter(ast);
    }


    public List<String> getErrors() {
//        return Stream.concat(errors.stream(), interpreter.getErrors().stream())
//                .collect(Collectors.toList());
        return errors;
    }

    public List<String> getPrintedMessages() {
        return interpreter.getPrinted();
    }
}

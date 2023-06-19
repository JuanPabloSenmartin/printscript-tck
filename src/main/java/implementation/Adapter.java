package implementation;


import ast.AST;
import ast.node.Node;
import interpreter.InputProvider;
import interpreter.Interpreter;
import lexer.Lexer;
import parser.Parser;
import token.Token;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.PeekingIterator;


public class Adapter {
    private final Interpreter interpreter;

    List<String> errors = new ArrayList<>();


    public Adapter(InputStream input, Double version, InputProvider provider) {
        this.interpreter = createInterpreter(input, version);
        //add input
        String i = provider.input("");
        if (i != null && !i.equals("")){
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
        PeekingIterator<Token> tokenIterator = null;
        try{
            Lexer lexer = new Lexer(new PushbackInputStream(input), version);
            tokenIterator = lexer.getTokenIterator();
        }catch (Exception e){
            errors.add(e.getMessage());
        }
        Iterator<Node> nodeIterator = null;
        try{
            Parser parser = new Parser(tokenIterator, version);
            nodeIterator = parser.getNodeIterator();
        }catch (Exception e){
            errors.add(e.getMessage());
        }
        return new Interpreter(nodeIterator);
    }


    public List<String> getErrors() {
        return errors;
    }

    public List<String> getPrintedMessages() {
        return interpreter.getPrinted();
    }
}

package implementation;


import ast.node.Node;
import com.google.common.collect.PeekingIterator;
import interpreter.InputProvider;
import interpreter.Interpreter;
import interpreter.PrintEmitter;
import lexer.Lexer;
import parser.Parser;
import token.Token;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Adapter {
    private final Interpreter interpreter;
    private final PrintEmitter emitter;
    private final InputProvider provider;

    List<String> errors = new ArrayList<>();

    public Adapter(InputStream input, Double version, PrintEmitter emitter, InputProvider provider) {
        this.interpreter = createInterpreter(input, version);
        this.emitter = emitter;
        this.provider = provider;
    }

    public void interpret(){
        //add input
        String readInput = provider.input("");
        if (readInput != null && !readInput.equals("")){
            InputStream in = new ByteArrayInputStream(readInput.getBytes());
            System.setIn(in);
        }
        interpreter.interpret();

        emitPrintedMessages(interpreter.getPrinted());
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

    public void emitPrintedMessages(List<String> toPrint){
        for (String message : toPrint){
            emitter.print(message);
        }
    }
}

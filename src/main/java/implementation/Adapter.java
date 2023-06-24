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
import java.util.Iterator;


public class Adapter {
    private final Interpreter interpreter;
    private final InputProvider provider;

    public Adapter(InputStream input, Double version, PrintEmitter emitter, InputProvider provider) {
        this.interpreter = createInterpreter(input, version, emitter);
        this.provider = provider;
    }

    public void interpret(){
        addInput();
        interpreter.interpret();
    }
    public void addInput(){
        String readInput = provider.input("");
        if (readInput != null && !readInput.equals("")){
            InputStream in = new ByteArrayInputStream(readInput.getBytes());
            System.setIn(in);
        }
    }


    private Interpreter createInterpreter(InputStream input, Double version, PrintEmitter emitter) {
        PushbackInputStream inputStream = new PushbackInputStream(input);
        Lexer lexer = new Lexer(inputStream, version);
        PeekingIterator<Token> tokenIterator = lexer.getTokenIterator();
        Parser parser = new Parser(tokenIterator, version);
        Iterator<Node> nodeIterator = parser.getNodeIterator();
        return new Interpreter(nodeIterator, emitter::print);
    }
}

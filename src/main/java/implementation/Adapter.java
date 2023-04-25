package implementation;


import interpreter.Interpreter;
import lexer.Lexer;
import org.apache.commons.io.FileUtils;
import parser.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Adapter {
    private File src;
    private String version;
    private Interpreter interpreter;

    public Adapter(File src, String version) {
        this.src = src;
        this.version = version;
        this.interpreter = createInterpreter(src, version);
    }

    private Interpreter createInterpreter(File src, String version) {
        String input;
        try {
            input = FileUtils.readFileToString(src, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Parser parser = new Parser(Lexer.tokenize(input));
        return new Interpreter(parser.parse());
    }


    public List<String> getErrors() {
        return null;
    }

    public List<String> getPrintedMessages() {
        return null;
    }
}

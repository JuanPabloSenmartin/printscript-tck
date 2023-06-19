package implementation;

import interpreter.PrintScriptInterpreter;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
         return (src, version, emitter, handler, provider) -> {
             try{
                 Adapter adapter = new Adapter(src, Double.parseDouble(version),emitter, provider);
                 adapter.interpret();
             } catch (Error | Exception e){
                 handler.reportError(e.getMessage());
             }
         };
    }
}
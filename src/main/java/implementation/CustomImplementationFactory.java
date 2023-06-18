package implementation;

import interpreter.PrintScriptInterpreter;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        // your PrintScript implementation should be returned here.
        // make sure to ADAPT your implementation to PrintScriptInterpreter interface.
//        return interpreter;
//        throw new NotImplementedException("Needs implementation"); // TODO: implement

         return (src, version, emitter, handler, provider) -> {
             Adapter adapter = new Adapter(src, Double.parseDouble(version), provider);
             for (String error : adapter.getErrors()){
                handler.reportError(error);
             }
             for (String message : adapter.getPrintedMessages()){
                 emitter.print(message);
             }
         };
    }
}
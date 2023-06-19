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

             try{
                 int a = src.available();
                 Adapter adapter = new Adapter(src, Double.parseDouble(version),emitter, provider);
                 adapter.interpret();
             } catch (Error | Exception e){
                 handler.reportError(e.getMessage());
             }
         };
    }
}
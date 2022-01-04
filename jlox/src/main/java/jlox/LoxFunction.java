package jlox;

import java.util.List;

public class LoxFunction implements LoxCallable {
    private final Statement.Function declaration;
    private final Environment closure;

    LoxFunction(Statement.Function declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        // Global environment is inherited with function arguments
        Environment environment = new Environment(closure);

        for (int i = 0; i < declaration.params.size(); i++) {
            // Match each parameter with corresponding argument and define in environment
            environment.define(declaration.params.get(i).lexeme, arguments.get(i));
        }

        // Exit the execution upon a return statement
        try {
            interpreter.executeBlock(declaration.body, environment);
        } catch (Return returnValue) {
            return returnValue.value;
        }
        return null;
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public String toString() {
        return "<function " + declaration.name.lexeme + ">";
    }
}
package caju;

import caju.SymbolTableManager.Symbol;
import caju.analysis.DepthFirstAdapter;
import caju.node.*;

import java.util.Stack;

public class Semantico extends DepthFirstAdapter {

    private SymbolTableManager symbolTableManager;
    private Stack<String> typeStack;

    public Semantico() {
        symbolTableManager = new SymbolTableManager();
        typeStack = new Stack<>();
    }

    @Override
    public void inStart(Start node) {
        System.out.println("Iniciando Semantico");
    }

    @Override
    public void outStart(Start node) {
        System.out.println("Finalizando Semantico");
    }

    @Override
    public void inAArProgramaAPrograma(AArProgramaAPrograma node) {
        symbolTableManager.enterScope();
    }

    @Override
    public void outAArProgramaAPrograma(AArProgramaAPrograma node) {
        symbolTableManager.exitScope();
    }

    @Override
    public void inAArDecFuncaoADecFuncao(AArDecFuncaoADecFuncao node) {
        String functionName = node.getId().getText();
        String returnType = node.getATipoRetorno().toString();
        
        if (symbolTableManager.lookup(functionName) != null) {
            System.err.println("Erro: Função " + functionName + " já está declarada.");
        } else {
            if (returnType.contains("vazio")) {
                returnType = "void";  // Handle the "vazio" (void) return type correctly
            }
            symbolTableManager.addSymbol(functionName, new Symbol(returnType, null));
        }
        symbolTableManager.enterScope();
    }

    @Override
    public void outAArDecFuncaoADecFuncao(AArDecFuncaoADecFuncao node) {
        symbolTableManager.exitScope();
    }

    @Override
    public void inAArDecVariavelADecVariavel(AArDecVariavelADecVariavel node) {
        String varName = node.getAListaNomes().toString();
        String varType = node.getATipo().toString();
        
        if (varType.contains("numero")) {
            varType = "int";  // Map 'numero' to 'int'
        }

        if (symbolTableManager.lookup(varName) != null) {
            System.err.println("Erro: Variável " + varName + " já está declarada neste escopo.");
        } else {
            symbolTableManager.addSymbol(varName, new Symbol(varType, null));
        }
    }

    // Atribuição de variável (i := 1)
    @Override
    public void inAArAtribAAtrib(AArAtribAAtrib node) {
        String varName = node.getAVar().toString();
        Symbol varSymbol = symbolTableManager.lookup(varName);

        if (varSymbol == null) {
            System.err.println("Erro: Variável " + varName + " não foi declarada.");
        } else {
            // Process the expression part before checking the assignment
            if (node.getAExp() != null) {
                node.getAExp().apply(this);  // Process the expression and push its type onto the stack
            }

            String varType = varSymbol.getType();

            if (!typeStack.isEmpty()) {  // Check if there's a type to pop from the stack
                String expType = typeStack.pop();
                
                if (!varType.equals(expType)) {
                    System.err.println("Erro: incompatibilidade de tipo na atribuição. A variável " + varName + " é do tipo " + varType + ", mas a expressão é do tipo " + expType + ".");
                }
            } else {
                System.err.println("Erro: Nenhum tipo de expressão encontrado na pilha de tipos.");
            }
        }
    }

    @Override
    public void inAArBlocoABloco(AArBlocoABloco node) {
        symbolTableManager.enterScope();
    }

    @Override
    public void outAArBlocoABloco(AArBlocoABloco node) {
        symbolTableManager.exitScope();
    }

    @Override
    public void inAArChamadaAChamada(AArChamadaAChamada node) {
        String functionName = node.getId().getText();
        Symbol funcSymbol = symbolTableManager.lookup(functionName);
        
        if (funcSymbol == null) {
            System.err.println("Erro: Função " + functionName + " não foi declarada.");
        } else {
            typeStack.push(funcSymbol.getType());
        }
    }

    @Override
    public void inAArRetorneAComandoSemCasam(AArRetorneAComandoSemCasam node) {
        if (node.getAExp() != null) {
            if (!typeStack.isEmpty()) {
                String returnType = typeStack.pop();
                String functionReturnType = symbolTableManager.lookup("currentFunction").getType();
                
                if (!returnType.equals(functionReturnType)) {
                    System.err.println("Erro: incompatibilidade de tipo de retorno. Esperado " + functionReturnType + ", mas obteve " + returnType + ".");
                }
            } else {
                System.err.println("Erro: Nenhum tipo de retorno encontrado na pilha de tipos.");
            }
        }
    }

    @Override
    public void inAArSeAComandoCasam(AArSeAComandoCasam node) {
        if (!typeStack.isEmpty()) {
            String conditionType = typeStack.pop();
            if (!conditionType.equals("boolean")) {
                System.err.println("Erro: a condição em 'se' deve ser booleana.");
            }
        } else {
            System.err.println("Erro: Nenhum tipo de condição encontrado na pilha de tipos.");
        }
    }

    @Override
    public void inAArEnquantoAComandoCasam(AArEnquantoAComandoCasam node) {
        if (!typeStack.isEmpty()) {
            String conditionType = typeStack.pop();
            if (!conditionType.equals("boolean")) {
                System.err.println("Erro: A condição em 'enquanto' deve ser booleana.");
            }
        } else {
            System.err.println("Erro: Nenhum tipo de condição encontrado na pilha de tipos.");
        }
    }

    @Override
    public void inAArParaAComandoCasam(AArParaAComandoCasam node) {
        symbolTableManager.enterScope();
    }

    @Override
    public void outAArParaAComandoCasam(AArParaAComandoCasam node) {
        symbolTableManager.exitScope();
    }

    @Override
    public void inAArMaisAExp(AArMaisAExp node) {
        if (typeStack.size() >= 2) {
            String rightType = typeStack.pop();
            String leftType = typeStack.pop();
            
            if (!leftType.equals("int") || !rightType.equals("int")) {
                System.err.println("Erro: ambos os operandos de '+' devem ser inteiros.");
            }
            typeStack.push("int");  // Push the result type
        } else {
            System.err.println("Erro: Operandos insuficientes para a operação de '+'.");
        }
    }

    @Override
    public void inAArVarAExp(AArVarAExp node) {
        String varName = node.getAVar().toString();
        Symbol varSymbol = symbolTableManager.lookup(varName);
        
        if (varSymbol != null) {
            typeStack.push(varSymbol.getType());
        } else {
            System.err.println("Erro: Variável " + varName + " não foi declarada.");
        }
    }

    // Handle numeric literals, e.g., '1' in i := 1.
    @Override
    public void inAArNumeroAExp(AArNumeroAExp node) {
        typeStack.push("int");  // Ensure 'int' is pushed for numbers
    }

    @Override
    public void inAArBooleanoAExp(AArBooleanoAExp node) {
        typeStack.push("boolean");
    }
}

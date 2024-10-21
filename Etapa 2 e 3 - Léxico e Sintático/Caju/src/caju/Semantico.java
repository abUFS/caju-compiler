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
    public void inStart(Start node)
    {
        System.out.println("Iniciando Semantico");
    }
    
    @Override
    public void outStart(Start node)
    {
    	System.out.println("Finalizando Semantico");
    }
    // Inicia um novo escopo (global)
    @Override
    public void inAArProgramaAPrograma(AArProgramaAPrograma node) {
        symbolTableManager.enterScope();
    }
    
    // Finaliza o escopo do programa
    @Override
    public void outAArProgramaAPrograma(AArProgramaAPrograma node) {
        symbolTableManager.exitScope();
    }
    
    // Inicia um novo escopo para funcao caso nao exista funcao com mesmo nove ja declarada
    @Override
    public void inAArDecFuncaoADecFuncao(AArDecFuncaoADecFuncao node) {
        String functionName = node.getId().getText();
        String returnType = node.getATipoRetorno().toString();
        
        if (symbolTableManager.lookup(functionName) != null) {
            System.err.println("Erro: Função" + functionName + "já está declarado.");
        } else {
        	// Adiciona funcao e seu tipo de retorno ao escopo anterior
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
        
        if (symbolTableManager.lookup(varName) != null) {
            System.err.println("Erro: Variável " + varName + " já está declarada neste escopo.");
        } else {
            symbolTableManager.addSymbol(varName, new Symbol(varType, null));
        }
    }

    @Override
    public void inAArAtribAAtrib(AArAtribAAtrib node) {
        String varName = node.getAVar().toString();
        Symbol varSymbol = symbolTableManager.lookup(varName);
        
        if (varSymbol == null) {
            System.err.println("Erro: Variável " + varName + " não foi declarado.");
        } else {
            String varType = varSymbol.getType();
            String expType = typeStack.pop();
            if (!varType.equals(expType)) {
                System.err.println("Erro: incompatibilidade de tipo na atribuição. A variável " + varName + " é do tipo " + varType + ", mas a expressão é do tipo " + expType + ".");
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
            String returnType = typeStack.pop();
            String functionReturnType = symbolTableManager.lookup("currentFunction").getType();
            
            if (!returnType.equals(functionReturnType)) {
                System.err.println("Erro: incompatibilidade de tipo de retorno. Esperado " + functionReturnType + ", mas obteve " + returnType + ".");
            }
        }
    }

    @Override
    public void inAArSeAComandoCasam(AArSeAComandoCasam node) {
        String conditionType = typeStack.pop();
        if (!conditionType.equals("boolean")) {
            System.err.println("Erro: a condição em 'se' deve ser booleana.");
        }
    }

    @Override
    public void inAArEnquantoAComandoCasam(AArEnquantoAComandoCasam node) {
        String conditionType = typeStack.pop();
        if (!conditionType.equals("boolean")) {
            System.err.println("Erro: A condição em 'enquanto' deve ser booleana.");
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
        String rightType = typeStack.pop();
        String leftType = typeStack.pop();
        
        if (!leftType.equals("int") || !rightType.equals("int")) {
            System.err.println("Erro: ambos os operandos de '+' devem ser inteiros.");
        }
        typeStack.push("int");
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

    @Override
    public void inAArNumeroAExp(AArNumeroAExp node) {
        typeStack.push("int");
    }

    @Override
    public void inAArBooleanoAExp(AArBooleanoAExp node) {
        typeStack.push("boolean");
    }
}


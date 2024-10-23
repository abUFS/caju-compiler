package caju;

import caju.SymbolTableManager.Symbol;
import caju.analysis.DepthFirstAdapter;
import caju.node.*;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Semantico extends DepthFirstAdapter {

	private SymbolTableManager symbolTableManager;
	private Stack<String> typeStack;
	private Stack<String> currentFunctionReturnTypeStack = new Stack<>();

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
		declareBuiltinFunctions();
	}

	@Override
	public void outAArProgramaAPrograma(AArProgramaAPrograma node) {
		symbolTableManager.exitScope();
	}

	@Override
	public void inAArDecFuncaoADecFuncao(AArDecFuncaoADecFuncao node) {
		declareFunction(node);
	}

	@Override
	public void outAArDecFuncaoADecFuncao(AArDecFuncaoADecFuncao node) {
		symbolTableManager.exitScope();
		if (!currentFunctionReturnTypeStack.isEmpty()) {
			currentFunctionReturnTypeStack.pop();
		}
	}

	private void declareBuiltinFunctions() {
		declareFunctionInScope("exibir", "vazio");
		declareFunctionInScope("ler", "vazio");
	}

	private void declareFunction(AArDecFuncaoADecFuncao node) {
		String functionName = node.getId().getText().trim();
		String returnType = node.getATipoRetorno().toString().trim();
		if (symbolTableManager.lookup(functionName) != null) {
			System.err.println("Erro: Função " + functionName + " já está declarada.");
		} else {
			symbolTableManager.addSymbol(functionName, new Symbol(returnType, null));
			currentFunctionReturnTypeStack.push(returnType);
		}
		symbolTableManager.enterScope();
		declareFunctionParameters(node);
	}
	
    private void processParameter(PAParametro paramNode) {
        if (paramNode instanceof AArParametroAParametro) {
            AArParametroAParametro param = (AArParametroAParametro) paramNode;
            String paramName = param.getId().getText();
            String paramType = param.getATipo().toString().trim();

            if (symbolTableManager.lookup(paramName) != null) {
                System.err.println("Erro: Parâmetro " + paramName + " já está declarado.");
            } else {
                symbolTableManager.addSymbol(paramName, new Symbol(paramType, null));
            }
        }
    }

	private void declareFunctionParameters(AArDecFuncaoADecFuncao node) {
		if (node.getAParametros() != null) {
			PAParametros parametrosNode = node.getAParametros();
			if (parametrosNode instanceof AArParametrosAParametros) {
				AArParametrosAParametros parametros = (AArParametrosAParametros) parametrosNode;
				if (parametros.getEsq() != null) {
					processParameter(parametros.getEsq());
				}
				for (PAParametro param : parametros.getDir()) {
					processParameter(param);
				}
			}
		}
	}

	private void declareFunctionInScope(String functionName, String returnType) {
		if (symbolTableManager.lookup(functionName) == null) {
			symbolTableManager.addSymbol(functionName, new Symbol(returnType, null));
		}
	}

	@Override
	public void inAArDecVariavelADecVariavel(AArDecVariavelADecVariavel node) {
		String baseTypeWithDimensions = node.getATipo().toString().trim();
		List<String> varNames = Arrays.asList(node.getAListaNomes().toString().split(" "));
		String[] parts = baseTypeWithDimensions.split("\\s+");
		String baseType = parts[0];
		int[] dimensions = Arrays.stream(parts, 1, parts.length).mapToInt(Integer::parseInt).toArray();
		for (String varName : varNames) {
			if (parts.length == 1) {
				declareVariable(varName, baseType);
			} else {
				generateVectorAccess(varName, dimensions, baseType);
			}
		}
	}

	private void declareVariable(String varName, String varType) {
		if (symbolTableManager.lookup(varName) != null) {
			System.err.println("Erro: Variável " + varName + " já está declarada neste escopo.");
		} else {
			symbolTableManager.addSymbol(varName, new Symbol(varType, null));
		}
	}

	public void generateVectorAccess(String vectorName, int[] dimensions, String baseType) {
		generateRecursive(vectorName, dimensions, new int[dimensions.length], 0, baseType);
	}

	private void generateRecursive(String vectorName, int[] dimensions, int[] indices, int depth, String baseType) {
		if (depth == dimensions.length) {
			StringBuilder sb = new StringBuilder(vectorName);
			for (int index : indices) {
				sb.append("[").append(index).append("]");
			}
			symbolTableManager.addSymbol(sb.toString().trim(), new Symbol(baseType, null));
			return;
		}
		for (int i = 0; i < dimensions[depth]; i++) {
			indices[depth] = i;
			generateRecursive(vectorName, dimensions, indices, depth + 1, baseType);
		}
	}

	@Override
	public void inAArAtribAAtrib(AArAtribAAtrib node) {
		String varText = node.getAVar().toString().trim();
		String[] parts = varText.split("\\s+");
		String varName = parts[0];
		String[] indexes = Arrays.stream(parts, 1, parts.length).toArray(String[]::new);
		if (parts.length == 1) {
			handleSimpleVariableAssignment(varName, node);
		} else {
			handleIndexedVariableAssignment(varName, indexes);
		}
	}

	private void handleSimpleVariableAssignment(String varName, AArAtribAAtrib node) {
		Symbol varSymbol = symbolTableManager.lookup(varName);
		if (varSymbol == null) {
			System.err.println("Erro: Variável " + varName + " não foi declarada.");
		} else {
			processExpression(node.getAExp());
			validateAssignment(varName, varSymbol);
		}
	}

	private void handleIndexedVariableAssignment(String varName, String[] indexes) {
		Boolean[] canBeConverted = Arrays.stream(indexes).map(this::canBeInteger).toArray(Boolean[]::new);
		int position = 0;
		for (String index : indexes) {
			if (!canBeConverted[position]) {
				Symbol indexVar = symbolTableManager.lookup(index);
				if (!indexVar.getType().equals("numero")) {
					System.err.println("Erro: Variável índice" + index + " não é inteira.");
				}
			}
		}
	}

	private Boolean canBeInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private void validateAssignment(String varName, Symbol varSymbol) {
		if (!typeStack.isEmpty()) {
			String expType = typeStack.pop();
			String varType = varSymbol.getType().trim();
			if (!varType.equals(expType)) {
				System.err.println("Erro: incompatibilidade de tipo na atribuição. A variável " + varName + " é do tipo " + varType + ", mas a expressão é do tipo " + expType + ".");
			}
		} else {
			System.err.println("Erro: Nenhum tipo de expressão encontrado na pilha de tipos.");
		}
	}

	private void processExpression(PAExp exp) {
		if (exp != null) {
			exp.apply(this);
		}
	}

	@Override
	public void inAArParaCadaAComandoCasam(AArParaCadaAComandoCasam node) {
		symbolTableManager.enterScope();
		String baseTypeWithDimensions = node.getATipo().toString().trim();
		String varName = node.getEsq().toString().trim();
		String[] parts = baseTypeWithDimensions.split("\\s+");
		String baseType = parts[0];
		if (parts.length > 1) {
			int[] dimensions = Arrays.stream(parts, 1, parts.length).mapToInt(Integer::parseInt).toArray();
			generateVectorAccess(varName, dimensions, baseType);
		} else {
			declareVariable(varName, baseType);
		}
	}

	@Override
	public void outAArParaCadaAComandoCasam(AArParaCadaAComandoCasam node) {
		symbolTableManager.exitScope();
	}

	public void binaryOperation(String tipo, String operador, String tipo_resultado) {
		if (typeStack.size() >= 2) {
			String rightType = typeStack.pop();
			String leftType = typeStack.pop();
			if (!leftType.equals(tipo) || !rightType.equals(tipo)) {
				System.err.println("Erro: ambos os operandos de " + operador + " devem ser " + tipo + ".");
			}
			typeStack.push(tipo_resultado);
		} else {
			System.err.println("Erro: Operandos insuficientes para a operação de " + operador + ".");
		}
	}

	@Override
	public void outAArOuAExp(AArOuAExp node) {
		binaryOperation("booleano", "ou", "booleano");
	}

	@Override
	public void outAArEAExp(AArEAExp node) {
		binaryOperation("booleano", "e", "booleano");
	}

	@Override
	public void outAArIgualAExp(AArIgualAExp node) {
		validateBinaryOperationWithSameType("booleano");
	}

	@Override
	public void outAArMenorIgualAExp(AArMenorIgualAExp node) {
		binaryOperation("numero", "<=", "booleano");
	}

	@Override
	public void outAArMaiorIgualAExp(AArMaiorIgualAExp node) {
		binaryOperation("numero", ">=", "booleano");
	}

	@Override
	public void outAArMenorAExp(AArMenorAExp node) {
		binaryOperation("numero", "<", "booleano");
	}

	@Override
	public void outAArMaiorAExp(AArMaiorAExp node) {
		binaryOperation("numero", ">", "booleano");
	}

	@Override
	public void outAArMaisAExp(AArMaisAExp node) {
		binaryOperation("numero", "+", "numero");
	}

	@Override
	public void outAArMenosAExp(AArMenosAExp node) {
		binaryOperation("numero", "-", "numero");
	}

	@Override
	public void outAArMultAExp(AArMultAExp node) {
		binaryOperation("numero", "*", "numero");
	}

	@Override
	public void outAArDivAExp(AArDivAExp node) {
		binaryOperation("numero", "/", "numero");
		if (node.getDir().toString().trim().equals("0")) {
			System.err.println("Divisão por 0!");
		}
	}

	@Override
	public void outAArNaoAExp(AArNaoAExp node) {
		validateUnaryOperation("booleano", "nao");
	}

	private void validateBinaryOperationWithSameType(String resultType) {
		if (typeStack.size() >= 2) {
			String rightType = typeStack.pop();
			String leftType = typeStack.pop();
			if (!leftType.equals(rightType)) {
				System.err.println("Erro: operandos devem ser do mesmo tipo.");
			}
			typeStack.push(resultType);
		} else {
			System.err.println("Erro: Operandos insuficientes para a operação de '='.");
		}
	}

	private void validateUnaryOperation(String expectedType, String operator) {
		if (typeStack.size() >= 1) {
			String type = typeStack.pop();
			if (!type.equals(expectedType)) {
				System.err.println("Erro: operando deve ser " + expectedType + " para operação " + operator + ".");
			}
			typeStack.push(expectedType);
		} else {
			System.err.println("Erro: Operandos insuficientes para a operação de '" + operator + "'.");
		}
	}

	@Override
	public void inAArVarAExp(AArVarAExp node) {
		String varWithIndex = node.getAVar().toString().trim();
		String[] parts = varWithIndex.split("\\s+");
		String varName = parts[0];
		String[] indexes = Arrays.stream(parts, 1, parts.length).toArray(String[]::new);
		if (parts.length == 1) {
			handleVariableAccess(varName);
		} else {
			handleIndexedVariableAccess(varName, indexes);
		}
	}

	private void handleVariableAccess(String varName) {
		Symbol varSymbol = symbolTableManager.lookup(varName);
		if (varSymbol != null) {
			typeStack.push(varSymbol.getType());
		} else {
			System.err.println("Erro: Variável " + varName + " não foi declarada.");
		}
	}

	private void handleIndexedVariableAccess(String varName, String[] indexes) {
		Boolean[] canBeConverted = Arrays.stream(indexes).map(this::canBeInteger).toArray(Boolean[]::new);
		for (int i = 0; i < indexes.length; i++) {
			if (!canBeConverted[i]) {
				Symbol indexVar = symbolTableManager.lookup(indexes[i]);
				if (!indexVar.getType().equals("numero")) {
					System.err.println("Erro: Variável índice" + indexes[i] + " não é inteira.");
				}
			}
		}
	}

	@Override
	public void inAArNumeroAExp(AArNumeroAExp node) {
		typeStack.push("numero");
	}

	@Override
	public void inAArCaractereAExp(AArCaractereAExp node) {
		typeStack.push("caractere");
	}

	@Override
	public void inAArBooleanoAExp(AArBooleanoAExp node) {
		typeStack.push("booleano");
	}

	@Override
	public void inAArStringAExp(AArStringAExp node) {
		typeStack.push("char[]");
	}
}

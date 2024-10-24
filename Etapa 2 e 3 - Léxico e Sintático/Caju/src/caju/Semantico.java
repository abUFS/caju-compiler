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
	private Stack<String> currentFunctionReturnTypeStack;

	public Semantico() {
		symbolTableManager = new SymbolTableManager();
		typeStack = new Stack<>();
		currentFunctionReturnTypeStack = new Stack<>();
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
	
	private void processParameter(PAParametro paramNode) {
	    if (paramNode instanceof AArParametroAParametro) {
	        AArParametroAParametro param = (AArParametroAParametro) paramNode;
	        String baseTypeWithDimensions = param.getATipo().toString().trim();
	        String paramName = param.getId().getText().trim();

	        String[] parts = baseTypeWithDimensions.split("\\s+");
	        String baseType = parts[0];
	        int[] dimensions = Arrays.stream(parts, 1, parts.length).mapToInt(Integer::parseInt).toArray();

	        if (parts.length == 1) {
	            declareVariable(paramName, baseType);
	        } else {
	            declareVector(paramName, baseType, dimensions);
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
				declareVector(varName, baseType, dimensions);
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

	private void declareVector(String vectorName, String baseType, int[] dimensions) {
		if (symbolTableManager.lookup(vectorName) != null) {
			System.err.println("Erro: Vetor " + vectorName + " já está declarado neste escopo.");
		} else {
			symbolTableManager.addSymbol(vectorName, new Symbol(baseType, null, dimensions));
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
		Symbol symbol = symbolTableManager.lookup(varName);
		if (symbol == null || !symbol.isVector()) {
			System.err.println("Erro: Variável " + varName + " não é um vetor ou não foi declarada.");
			return;
		}
		validateIndexes(varName, indexes, symbol.getDimensions());
	}

	private void validateIndexes(String varName, String[] indexes, int[] dimensions) {

		for (int i = 0; i < indexes.length; i++) {
			String index = indexes[i];
			if (!canBeInteger(index)) {
				Symbol indexVar = symbolTableManager.lookup(index);
				if (indexVar == null || !"numero".equals(indexVar.getType())) {
					System.err.println("Erro: Índice " + index + " não é inteiro válido.");
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
			declareVector(varName, baseType, dimensions);
		} else {
			declareVariable(varName, baseType);
		}
	}

	@Override
	public void outAArParaCadaAComandoCasam(AArParaCadaAComandoCasam node) {
		symbolTableManager.exitScope();
	}

	public void binaryOperation(String[] tiposPermitidos, String operador, String tipoResultado) {
		if (typeStack.size() >= 2) {
			String rightType = typeStack.pop();
			String leftType = typeStack.pop();
			boolean leftValid = Arrays.asList(tiposPermitidos).contains(leftType);
			boolean rightValid = Arrays.asList(tiposPermitidos).contains(rightType);
			if (!leftValid || !rightValid) {
				System.err.println("Erro: ambos os operandos de " + operador + " devem ser de um dos tipos: " +
						Arrays.toString(tiposPermitidos) + ".");
			} else {
				typeStack.push(tipoResultado);
			}
		} else {
			System.err.println("Erro: Operandos insuficientes para a operação de " + operador + ".");
		}
	}

	@Override
	public void outAArOuAExp(AArOuAExp node) {
		String[] tiposPermitidosOu = {"numero", "caractere", "booleano"};
		binaryOperation(tiposPermitidosOu, "ou", "booleano");
	}

	@Override
	public void outAArEAExp(AArEAExp node) {
		String[] tiposPermitidosE = {"numero", "caractere", "booleano"};
		binaryOperation(tiposPermitidosE, "e", "booleano");
	}

	@Override
	public void outAArIgualAExp(AArIgualAExp node) {
		if (typeStack.size() >= 2) {
			String rightType = typeStack.pop();
			String leftType = typeStack.pop();
			if (!leftType.equals(rightType)) {
				System.err.println("Erro: operandos devem ser do mesmo tipo.");
			}
			typeStack.push("booleano");
		} else {
			System.err.println("Erro: Operandos insuficientes para a operação de '='.");
		}
	}

	@Override
	public void outAArMenorIgualAExp(AArMenorIgualAExp node) {
		String[] tiposPermitidosMenorIgual = {"numero", "caractere", "booleano"};
		binaryOperation(tiposPermitidosMenorIgual, "<=", "booleano");
	}

	@Override
	public void outAArMaiorIgualAExp(AArMaiorIgualAExp node) {
		String[] tiposPermitidosMaiorIgual = {"numero", "caractere", "booleano"};
		binaryOperation(tiposPermitidosMaiorIgual, ">=", "booleano");
	}

	@Override
	public void outAArMenorAExp(AArMenorAExp node) {
		String[] tiposPermitidosMenor = {"numero", "caractere", "booleano"};
		binaryOperation(tiposPermitidosMenor, "<", "booleano");
	}

	@Override
	public void outAArMaiorAExp(AArMaiorAExp node) {
		String[] tiposPermitidosMaior = {"numero", "caractere", "booleano"};
		binaryOperation(tiposPermitidosMaior, ">", "booleano");
	}

	@Override
	public void outAArMaisAExp(AArMaisAExp node) {
		String[] tiposPermitidosSoma = {"numero", "caractere", "booleano"};
		binaryOperation(tiposPermitidosSoma, "+", "numero");
	}

	@Override
	public void outAArMenosAExp(AArMenosAExp node) {
		String[] tiposPermitidosSubtracao = {"numero", "caractere", "booleano"};
		binaryOperation(tiposPermitidosSubtracao, "-", "numero");
	}

	@Override
	public void outAArMultAExp(AArMultAExp node) {
		String[] tiposPermitidosMultiplicacao = {"numero", "caractere", "booleano"};
		binaryOperation(tiposPermitidosMultiplicacao, "*", "numero");
	}

	@Override
	public void outAArDivAExp(AArDivAExp node) {
		String[] tiposPermitidosDivisao = {"numero", "caractere", "booleano"};
		binaryOperation(tiposPermitidosDivisao, "/", "numero");

		if (node.getDir().toString().trim().equals("0")) {
			System.err.println("Divisão por 0!");
		}
	}

	@Override
	public void outAArNaoAExp(AArNaoAExp node) {
		validateUnaryOperation("booleano", "nao");
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
		Symbol symbol = symbolTableManager.lookup(varName);
		if (symbol == null || !symbol.isVector()) {
			System.err.println("Erro: Variável " + varName + " não é um vetor ou não foi declarada.");
			return;
		}
		validateIndexes(varName, indexes, symbol.getDimensions());
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

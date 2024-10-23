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

		String exibirFunctionName = "exibir";
		String exibirReturnType = "vazio";
		if (symbolTableManager.lookup(exibirFunctionName) == null) {
			symbolTableManager.addSymbol(exibirFunctionName, new Symbol(exibirReturnType, null));
		}

		String lerFunctionName = "ler";
		String lerReturnType = "vazio";
		if (symbolTableManager.lookup(lerFunctionName) == null) {
			symbolTableManager.addSymbol(lerFunctionName, new Symbol(lerReturnType, null));
		}
	}

	@Override
	public void outAArProgramaAPrograma(AArProgramaAPrograma node) {
		symbolTableManager.exitScope();
	}

	@Override
	public void inAArDecFuncaoADecFuncao(AArDecFuncaoADecFuncao node) {
		String functionName = node.getId().getText().trim();
		String returnType = node.getATipoRetorno().toString().trim();

		if (symbolTableManager.lookup(functionName) != null) {
			System.err.println("Erro: Função " + functionName + " já está declarada.");
		} else {
			symbolTableManager.addSymbol(functionName, new Symbol(returnType, null));
			currentFunctionReturnTypeStack.push(returnType);
		}
		symbolTableManager.enterScope();

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

	@Override
	public void outAArDecFuncaoADecFuncao(AArDecFuncaoADecFuncao node) {
		symbolTableManager.exitScope();
		if (!currentFunctionReturnTypeStack.isEmpty()) {
			currentFunctionReturnTypeStack.pop();
		}
	}

	private void processParameter(PAParametro paramNode) {
		if (paramNode instanceof AArParametroAParametro) {
			AArParametroAParametro param = (AArParametroAParametro) paramNode;
			String paramName = param.getId().getText().trim();
			String paramType = param.getATipo().toString().trim();

			if (symbolTableManager.lookup(paramName) != null) {
				System.err.println("Erro: Parâmetro " + paramName + " já está declarado.");
			} else {
				symbolTableManager.addSymbol(paramName, new Symbol(paramType, null));
			}
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
				if (symbolTableManager.lookup(varName) != null) {
					System.err.println("Erro: Variável " + varName + " já está declarada neste escopo.");
				} else {

					symbolTableManager.addSymbol(varName, new Symbol(baseType, null));
				}
			} else {
				generateVectorAccess(varName, dimensions, baseType);

			}

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

			symbolTableManager.addSymbol(sb.toString(), new Symbol(baseType, null));
			return;
		}

		for (int i = 0; i < dimensions[depth]; i++) {
			indices[depth] = i;
			generateRecursive(vectorName, dimensions, indices, depth + 1, baseType);
		}
	}

	// Atribuição de variável (ex: i := 1)
	@Override
	public void inAArAtribAAtrib(AArAtribAAtrib node) {
		// tratar vetores
		String varText = node.getAVar().toString().trim();

		String[] parts = varText.split("\\s+");
		String varName = parts[0];
		String[] indexes = Arrays.stream(parts, 1, parts.length).toArray(String[]::new);

		if (parts.length == 1) {
			Symbol varSymbol = symbolTableManager.lookup(varName);

			if (varSymbol == null) {
				System.err.println("Erro: Variável " + varName + " não foi declarada.");
			} else {
				if (node.getAExp() != null) {
					node.getAExp().apply(this);

				}

				String varType = varSymbol.getType().trim();

				if (typeStack.isEmpty()) {
					System.err.println("Erro: Nenhum tipo de expressão encontrado na pilha de tipos.");
					
				} else {
					String expType = typeStack.pop();
					if (!varType.equals(expType)) {
						System.err.println("Erro: incompatibilidade de tipo na atribuição. A variável " + varName
								+ " é do tipo " + varType + ", mas a expressão é do tipo " + expType + ".");
					}
					else {
						/* set value da variável*/
					}
				}
			}
		} else {
			Boolean[] canBeConverted = Arrays.stream(parts, 1, parts.length).map(s -> {
				try {
					Integer.parseInt(s);
					return true;
				} catch (NumberFormatException e) {
					return false;
				}
			}).toArray(Boolean[]::new);

			boolean[] result = new boolean[canBeConverted.length];
			for (int i = 0; i < canBeConverted.length; i++) {
				result[i] = canBeConverted[i];
			}

			int position = 0;
			for (String index : indexes) {

				if (!result[position]) {
					Symbol indexVar = symbolTableManager.lookup(index);
					if (!indexVar.getType().equals("numero")) {
						System.err.println("Erro: Variável índice" + index + " não é inteira.");

					}
				} else {
					/* verificar se está no limite do tamanho do array */
				}

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
		String functionName = node.getId().getText().trim();
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
			validateReturnType();
		}
	}

	@Override
	public void inAArSeAComandoCasam(AArSeAComandoCasam node) {
		symbolTableManager.enterScope();
	}

	@Override
	public void outAArSeAComandoCasam(AArSeAComandoCasam node) {
		symbolTableManager.exitScope();
	}

	@Override
	public void inAArSenaoAComandoCasam(AArSenaoAComandoCasam node) {
		symbolTableManager.enterScope();
	}

	@Override
	public void outAArSenaoAComandoCasam(AArSenaoAComandoCasam node) {
		symbolTableManager.exitScope();
	}

	private void validateReturnType() {
		if (!typeStack.isEmpty()) {
			String returnType = typeStack.pop();
			if (!currentFunctionReturnTypeStack.isEmpty()) {
				String expectedReturnType = currentFunctionReturnTypeStack.peek();
				if (!returnType.equals(expectedReturnType)) {
					System.err.println("Erro: incompatibilidade de tipo de retorno. Esperado " + expectedReturnType
							+ ", mas obteve " + returnType + ".");
				}
			} else {
				System.err.println("Erro: Nenhum tipo de função ativa encontrada.");
			}
		} else {
			System.err.println("Erro: Nenhum tipo de retorno encontrado na pilha de tipos.");
		}
	}

	@Override
	public void inAArEnquantoAComandoCasam(AArEnquantoAComandoCasam node) {
		symbolTableManager.enterScope();
	}

	@Override
	public void outAArEnquantoAComandoCasam(AArEnquantoAComandoCasam node) {

		symbolTableManager.exitScope();
		/*
		 * if (!typeStack.isEmpty()) { String conditionType = typeStack.pop(); if
		 * (!conditionType.equals("booleano")) {
		 * System.err.println("Erro: A condição em 'enquanto' deve ser booleana."); } }
		 * else { System.err.
		 * println("Erro: Nenhum tipo de condição encontrado na pilha de tipos."); }
		 */
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
    public void inAArParaCadaAComandoCasam(AArParaCadaAComandoCasam node)
    {
		symbolTableManager.enterScope();
		
		node.getAComando();
    }
    
	@Override
    public void outAArParaCadaAComandoCasam(AArParaCadaAComandoCasam node)
    {
		symbolTableManager.exitScope();
    }

	public void operacaoBinaria(String tipo, String operador, String tipo_resultado) {
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
		operacaoBinaria("booleano", "ou", "booleano");
	}

	@Override
	public void outAArEAExp(AArEAExp node) {
		operacaoBinaria("booleano", "e", "booleano");
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
		operacaoBinaria("numero", "<=", "booleano");
	}

	@Override
	public void outAArMaiorIgualAExp(AArMaiorIgualAExp node) {
		operacaoBinaria("numero", ">=", "booleano");
	}

	@Override
	public void outAArMenorAExp(AArMenorAExp node) {
		operacaoBinaria("numero", "<", "booleano");
	}

	@Override
	public void outAArMaiorAExp(AArMaiorAExp node) {
		operacaoBinaria("numero", ">", "booleano");
	}

	@Override
	public void outAArMaisAExp(AArMaisAExp node) {
		operacaoBinaria("numero", "+", "numero");
	}

	@Override
	public void outAArMenosAExp(AArMenosAExp node) {
		operacaoBinaria("numero", "-", "numero");
	}

	@Override
	public void outAArMultAExp(AArMultAExp node) {
		operacaoBinaria("numero", "*", "numero");
	}

	@Override
	public void outAArDivAExp(AArDivAExp node) {
		operacaoBinaria("numero", "/", "numero");

		if (node.getDir().toString().trim().equals("0")) {
			System.err.println("Divisão por 0!");
		}
	}

	@Override
	public void outAArNaoAExp(AArNaoAExp node) {
		if (typeStack.size() >= 1) {
			String type = typeStack.pop();

			if (!type.equals("booleano")) {
				System.err.println("Erro: operando deve ser booleano .");
			}
			typeStack.push("booleano");
		} else {
			System.err.println("Erro: Operandos insuficientes para a operação de 'nao' .");
		}
	}

	@Override
	public void inAArVarAExp(AArVarAExp node) {
		String varWithIndex = node.getAVar().toString().trim();

		String[] parts = varWithIndex.split("\\s+");

		String varName = parts[0];
		String[] indexes = Arrays.stream(parts, 1, parts.length).toArray(String[]::new);

		if (parts.length == 1) {
			Symbol varSymbol = symbolTableManager.lookup(varName);

			if (varSymbol != null) {
				typeStack.push(varSymbol.getType());
			} else {
				System.err.println("Erro: Variável " + varName + " não foi declarada.");
			}
		} else {
			Boolean[] canBeConverted = Arrays.stream(parts, 1, parts.length).map(s -> {
				try {
					Integer.parseInt(s);
					return true;
				} catch (NumberFormatException e) {
					return false;
				}
			}).toArray(Boolean[]::new);

			boolean[] result = new boolean[canBeConverted.length];
			for (int i = 0; i < canBeConverted.length; i++) {
				result[i] = canBeConverted[i];
			}

			int position = 0;
			for (String index : indexes) {

				if (!result[position]) {
					Symbol indexVar = symbolTableManager.lookup(index);
					if (!indexVar.getType().equals("numero")) {
						System.err.println("Erro: Variável índice" + index + " não é inteira.");

					}
				} else {
					/* verificar se está no limite do tamanho do array */
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

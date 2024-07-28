{
    Autor: Rafael Takeguma
    Matrícula: 202100011833
    Disciplina: Compiladores
    
    Nome do arquivo: codigo03.cj
    Data de criação: 26/07/2024
}

# Função para calcular o n-ésimo termo da sequência de Fibonacci
-> numero fib(numero n) inicio
    se (n <= 1) inicio
        retorne [n].
    fim senao inicio
        retorne [fib(n-1) + fib(n-2)].
    fim
fim

# Função para realizar uma busca binária
-> numero buscaBinaria(vetor numero[] arr| numero esquerda| numero direita| numero alvo) inicio
    enquanto(esquerda <= direita) inicio
        numero meio := esquerda + (direita - esquerda) / 2.

        # se o elemento estiver no meio retorna ele
        se (arr[meio] = alvo)
            retorne [meio]. 

        # se o elemento for maior ta no lado direito, se for menor ta na esquerda
        se (arr[meio] < alvo)
            esquerda := meio + 1.
        senao
            direita := meio - 1.
    fim
fim

# Main
-> numero main() inicio
    numero n, resultado.
    vetor numero[30] sequencia.

    # solicita o n ao usuário
    exibir("Digite o valor de n: ").
    ler("%d", &n).

    # Exibe a sequência de Fibonacci até o n-ésimo termo
    para(numero i := 0; i <= 0; i = i + 1) inicio
        sequencia[i] := fib(i).
        exibir("%d", fib(i)).
    fim

    resultado := buscaBinaria(sequencia| 0| 7| 2).
    exibir("%d", resultado).

    retorne [0].
fim

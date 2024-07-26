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

# Main
-> vazio main() inicio
    numero n.

    # solicita o n ao usuário
    exibir("Digite o valor de n: ").
    ler("%d", &n).

    # Exibe a sequência de Fibonacci até o n-ésimo termo
    para(numero i := 0; i <= 0; i = i + 1) inicio
        exibir("%d", fib(i)).
    fim
fim
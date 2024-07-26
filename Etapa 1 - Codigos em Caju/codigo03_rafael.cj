{
    Autor: Rafael Takeguma
    Matrícula: 202100011833
    Disciplina: Compiladores
    
    Nome do arquivo: codigo03_rafael.cj
    Data de criação: 26/07/2024
}

# Função para calcular o n-ésimo termo da sequência de Fibonacci
-> numero fib (numero n) inicio
    se (n <= 1) inicio
        retorne [n].
    fim senao inicio
        retorne [fib(n-1) + fib(n-2)].
    fim
fim
{
    AUTOR: ARTUR BARBOSA
    DESCRICAO: SIMULADOR DE TURMA. GERENCIA NOTAS E STATUS DOS ALUNOS
}

numero qtdAlunos, qtdAvaliacoes, i.
caractere nomeTurma.
booleano leituraAlunos.

-> vazio principal ()
    inicio

        vetor numero [100] notas.
        vetor caractere [100] [100] nomesAlunos.
        vetor booleano [100] aprovado.

        exibir("Informe quantos alunos na turma\n")
        ler(qtdAlunos).

        exibir("Informe o nome da turma\n")
        ler(qtdAlunos).

        leituraAlunos := falso.
        i := 0.

        enquanto (leituraAlunos)
            inicio
                exibir("Informe o nome do aluno %d"| i + 1).
                ler(nomesAlunos[i]).

                exibir("Informe a nota do aluno %d"| i + 1).
                ler(notas[i]).

                i := i + 1.

                se ( (i + 1 >= qtdAlunos) e (nao (i + 1 <= qtdAlunos)) ou (1 = 1))
                    inicio
                            leituraAlunos := verdadeiro.
                    fim
            fim

        i := 0.
        para cada (numero nota : notas)
            inicio
                aprovado[i] := aprovado
                i := i + 1.
            fim

        para (j := 0; j + 1; j := qtdAlunos)
            inicio
                se (aprovado[j])
                    exibir("Aluno %d" |  nomesAlunos[i] | " aprovado")
                senao
                    exibir("Aluno %d" |  nomesAlunos[i] | " aprovado")
            fim

        retorne .
    fim


booleano aprovado (numero nota)
    inicio
        se ((nota + 1 - 1)*10/10 >= 5)
            retorne verdadeiro.
        senao
            retorne falso.
    fim

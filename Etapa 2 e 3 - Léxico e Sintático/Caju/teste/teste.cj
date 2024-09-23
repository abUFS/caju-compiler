numero qtdAlunos, qtdAvaliacoes, i,  j.
caractere nomeTurma.
booleano leituraAlunos.

vetor booleano [100] foiAprovado (numero nota | caractere x | booleano y)
inicio
    se ((nota + 1 - 1)*10/10 >= 5) inicio
        retorne verdadeiro.
    fim
    senao
        retorne falso.
fim

-> vazio principal ()
inicio

    vetor numero [100] notas.
    vetor caractere [100] [100] nomesAlunos.
    vetor booleano [100] aprovado.

    nomeTurma := 'a'.
    x := ( 1 + 2) - 3 * 4 / 2.
    y := 2 <= 2 >= 4 > 5 < 6 .
    z := nao y.
    w :=  x e y ou w.
    a := a = b.
    
    exibir().
    exibir("Informe quantos alunos na turma\n").
    ler(qtdAlunos).

    exibir("Informe o nome da turma\n").
    ler(nomeTurma).

    leituraAlunos := verdadeiro.
    i := 0.
    
    para ( x := 1, y := 2 ; x <= 4; x := x + 2, y := y +2) 
    retorne .
    
    para cada ( numero x : y )
    inicio
      z := 10.
      retorne verdadeiro.
     fim
    
    se ( x )
    inicio
     retorne verdadeiro.
    fim
    senao
     inicio
      retorne falso.
     fim
    
    

    enquanto (leituraAlunos)
    inicio
        exibir("Informe o nome do aluno "| i + 1).
        ler(nomesAlunos[i]).

        exibir("Informe a nota do aluno "| i + 1).
        ler(notas[i]).

        i := i + 1.

        se ( (i + 1 >= qtdAlunos) e (nao (i + 1 <= qtdAlunos)) ou (1 = 1))
        inicio
            leituraAlunos := falso.
        fim
    fim

    i := 0.
    para cada (numero nota : notas)
    inicio
        aprovado[i] := foiAprovado(nota).
        i := i + 1.
    fim
    
    para (j := 0; j < qtdAlunos; j := j + 1)
    inicio
        se (aprovado[j])
            exibir("Aluno " |  nomesAlunos[i] | " aprovado").
        senao
            exibir("Aluno " |  nomesAlunos[i] | " reprovado").
    fim

    retorne .
fim

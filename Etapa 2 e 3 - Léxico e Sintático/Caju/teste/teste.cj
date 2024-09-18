vazio ordena(vetor numero[1] A | numero n) inicio

    numero i, j, v.

    para (i := 1 ; i <= n - 1 ; i := i + 1) inicio

        v := A[i].
        j := i - 1.

        enquanto (j >= 0 e A[j] > v) inicio
            A[j+1] := A[j].
            j := j - 1.            
        fim

        A[j+1] := v.
    fim
fim

-> vazio principal() inicio

    numero caixas, busca, cont, i, j, x, y.
    vetor numero[10000] prateleira.

    enquanto (caixas = verdadeiro) inicio

        para (i := 0 ; i < caixas ; i := i + 1) inicio
            para (j := x ; j <= y ; j := j + 1) inicio
                prateleira[cont] := j.
                cont := cont + 1.
            fim
        fim

        ordena(prateleira | cont).

        para (i := 0 ; i < cont ; i := i + 1) inicio
            se (prateleira[i] = busca e x = -1)  x := i.
            se (prateleira[i] = busca e nao(x = -1,2)) y := i.
        fim

        se (nao(x = 1)) inicio 
            se (y = 1) y := x.      
        fim
    
    fim    

fim

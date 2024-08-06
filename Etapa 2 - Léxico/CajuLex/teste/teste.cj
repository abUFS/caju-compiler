# comentário de linha $%&*¨¢£³¹¬

vazio ordena(vetor numero[] A | numero n) inicio

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

    enquanto (ler(caixas) = verdadeiro) inicio

        cont := 0.

        para (i := 0 ; i < caixas ; i := i + 1) inicio
            ler(x | y).
            para (j = x ; j <= y ; j := j + 1) inicio
                prateleira[cont] := j.
                cont := cont + 1.
            fim
        fim

        ordena(prateleira | cont).

        ler(busca).

        x := -1.
        y := -1.

        para (i := 0 ; i < cont ; i := i + 1) inicio
            se (prateleira[i] = busca e x = -1)  x := i.
            se (prateleira[i] = busca e nao(x = -1)) y := i.
        fim

        se (nao(x = -1)) inicio 
            se (y = -1) y := x.      
            exibir(busca | " encontrado de " | x | " a " | y | "\n").
        fim
        senao exibir(busca | " nao encontrado\n").
    
    fim    

fim
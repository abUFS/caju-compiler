{
    - Descrição
    Pedro está trabalhando em um novo emprego em uma loja de parafusos e porcas.
    Toda semana ele irá receber novas encomendas de parafusos mistos em grandes caixas
    e precisa saber onde ele deve armazenar esses produtos.Mango, que é supervisor de Pedro,
    pediu-lhe para organizar todos os parafusos e porcas em um rack enorme, de forma que seja
    fácil de responder onde é possı́vel encontrar cada predeterminado lote de parafusos e quanto
    de cada um destes lotes a loja tem.Pedro recebe todos os parafusos e porcas em caixas etiquetadas
    com o intervalo de tamanho dos produtos.Por exemplo, duas caixas com os respectivos intervalos
    (1, 2) e (4, 8) representam lotes de produtos com tamanho {1, 2, 4, 5, 6, 7, 8}. Um número de
    parafuso e porca pode estar presente em mais de uma caixa, e ele aparece na lista uma vez para
    cada intervalo. Por exemplo, três caixas com os respectivos intervalos (1, 3), (2, 4) e (3, 5)
    representam lotes de parafusos e porcas com tamanho {1, 2, 2, 3, 3, 3, 4, 4, 5}.

    - Formato de entrada
    A entrada contém vários casos de teste e termina com EOF. Cada caso de teste consiste de várias linhas.
    A primeira linha contém um inteiro positivo N (N < 100) indicando o número de caixas de parafusos.
    Cada uma das N linhas seguintes contém um par de números, X e Y (1 ≤ X < Y ≤ 100) representando os
    tamanhos dos lotes de porcas e parafusos na caixa. A linha seguinte da entrada contém um inteiro positivo
    Num (1 ≤ Num ≤ 100), que indica o número que Mango quer procurar depois que todos os produtos estiverem
    organizados em uma prateleira do rack.
    Note que o rack possui prateleiras numeradas de 0 a P e este P não deve exceder 10000.

    - Formato de saída
    Cada caso de teste deve produzir uma única linha de saída, indicando todas as posições da
    prateleira onde o tamanho de parafuso procurado pode ser encontrado ou, informando que ele não
    pode ser encontrado como no exemplo a seguir.
}

# função de ordenação
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

# função principal
-> vazio principal() inicio

    numero caixas, busca, cont, i, j, x, y.
    vetor numero[10000] prateleira.

    enquanto (ler("%d"| &caixas) = verdadeiro) inicio

        cont := 0.

        para (i := 0 ; i < caixas ; i := i + 1) inicio
            ler("%d %d"| &x| &y).
            para (j = x ; j <= y ; j := j + 1) inicio
                prateleira[cont] := j.
                cont := cont + 1.
            fim
        fim

        ordena(prateleira | cont).

        ler("%d"| &busca).

        x := -1.
        y := -1.

        para (i := 0 ; i < cont ; i := i + 1) inicio
            se (prateleira[i] = busca e x = -1)  x := i.
            se (prateleira[i] = busca e nao(x = -1)) y := i.
        fim

        se (nao(x = -1)) inicio 
            se (y = -1) y := x.      
            exibir("%d encontrado de %d a %d\n" | busca | x | y).
        fim
        senao exibir("%d nao encontrado\n" | busca).
    
    fim    

fim

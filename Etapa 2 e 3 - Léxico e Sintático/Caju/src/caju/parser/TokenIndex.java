/* This file was generated by SableCC (http://www.sablecc.org/). */

package caju.parser;

import caju.node.*;
import caju.analysis.*;

class TokenIndex extends AnalysisAdapter
{
    int index;

    @Override
    public void caseTBaseCaractere(@SuppressWarnings("unused") TBaseCaractere node)
    {
        this.index = 0;
    }

    @Override
    public void caseTBaseNumero(@SuppressWarnings("unused") TBaseNumero node)
    {
        this.index = 1;
    }

    @Override
    public void caseTBaseBooleano(@SuppressWarnings("unused") TBaseBooleano node)
    {
        this.index = 2;
    }

    @Override
    public void caseTVetor(@SuppressWarnings("unused") TVetor node)
    {
        this.index = 3;
    }

    @Override
    public void caseTVazio(@SuppressWarnings("unused") TVazio node)
    {
        this.index = 4;
    }

    @Override
    public void caseTCaractere(@SuppressWarnings("unused") TCaractere node)
    {
        this.index = 5;
    }

    @Override
    public void caseTBooleano(@SuppressWarnings("unused") TBooleano node)
    {
        this.index = 6;
    }

    @Override
    public void caseTNumero(@SuppressWarnings("unused") TNumero node)
    {
        this.index = 7;
    }

    @Override
    public void caseTVetorUnidimensional(@SuppressWarnings("unused") TVetorUnidimensional node)
    {
        this.index = 8;
    }

    @Override
    public void caseTInicio(@SuppressWarnings("unused") TInicio node)
    {
        this.index = 9;
    }

    @Override
    public void caseTFim(@SuppressWarnings("unused") TFim node)
    {
        this.index = 10;
    }

    @Override
    public void caseTComentLinha(@SuppressWarnings("unused") TComentLinha node)
    {
        this.index = 11;
    }

    @Override
    public void caseTComentBloco(@SuppressWarnings("unused") TComentBloco node)
    {
        this.index = 12;
    }

    @Override
    public void caseTEnquanto(@SuppressWarnings("unused") TEnquanto node)
    {
        this.index = 13;
    }

    @Override
    public void caseTPara(@SuppressWarnings("unused") TPara node)
    {
        this.index = 14;
    }

    @Override
    public void caseTParaCada(@SuppressWarnings("unused") TParaCada node)
    {
        this.index = 15;
    }

    @Override
    public void caseTSe(@SuppressWarnings("unused") TSe node)
    {
        this.index = 16;
    }

    @Override
    public void caseTSenao(@SuppressWarnings("unused") TSenao node)
    {
        this.index = 17;
    }

    @Override
    public void caseTRetorne(@SuppressWarnings("unused") TRetorne node)
    {
        this.index = 18;
    }

    @Override
    public void caseTMais(@SuppressWarnings("unused") TMais node)
    {
        this.index = 19;
    }

    @Override
    public void caseTMenos(@SuppressWarnings("unused") TMenos node)
    {
        this.index = 20;
    }

    @Override
    public void caseTMult(@SuppressWarnings("unused") TMult node)
    {
        this.index = 21;
    }

    @Override
    public void caseTDiv(@SuppressWarnings("unused") TDiv node)
    {
        this.index = 22;
    }

    @Override
    public void caseTMaior(@SuppressWarnings("unused") TMaior node)
    {
        this.index = 23;
    }

    @Override
    public void caseTMenor(@SuppressWarnings("unused") TMenor node)
    {
        this.index = 24;
    }

    @Override
    public void caseTMaiorIgual(@SuppressWarnings("unused") TMaiorIgual node)
    {
        this.index = 25;
    }

    @Override
    public void caseTMenorIgual(@SuppressWarnings("unused") TMenorIgual node)
    {
        this.index = 26;
    }

    @Override
    public void caseTIgual(@SuppressWarnings("unused") TIgual node)
    {
        this.index = 27;
    }

    @Override
    public void caseTNao(@SuppressWarnings("unused") TNao node)
    {
        this.index = 28;
    }

    @Override
    public void caseTE(@SuppressWarnings("unused") TE node)
    {
        this.index = 29;
    }

    @Override
    public void caseTOu(@SuppressWarnings("unused") TOu node)
    {
        this.index = 30;
    }

    @Override
    public void caseTAtribuir(@SuppressWarnings("unused") TAtribuir node)
    {
        this.index = 31;
    }

    @Override
    public void caseTPontoFinal(@SuppressWarnings("unused") TPontoFinal node)
    {
        this.index = 32;
    }

    @Override
    public void caseTExibir(@SuppressWarnings("unused") TExibir node)
    {
        this.index = 33;
    }

    @Override
    public void caseTLer(@SuppressWarnings("unused") TLer node)
    {
        this.index = 34;
    }

    @Override
    public void caseTVirgula(@SuppressWarnings("unused") TVirgula node)
    {
        this.index = 35;
    }

    @Override
    public void caseTAbreColchete(@SuppressWarnings("unused") TAbreColchete node)
    {
        this.index = 36;
    }

    @Override
    public void caseTFechaColchete(@SuppressWarnings("unused") TFechaColchete node)
    {
        this.index = 37;
    }

    @Override
    public void caseTSeta(@SuppressWarnings("unused") TSeta node)
    {
        this.index = 38;
    }

    @Override
    public void caseTAbreParenteses(@SuppressWarnings("unused") TAbreParenteses node)
    {
        this.index = 39;
    }

    @Override
    public void caseTFechaParenteses(@SuppressWarnings("unused") TFechaParenteses node)
    {
        this.index = 40;
    }

    @Override
    public void caseTPipe(@SuppressWarnings("unused") TPipe node)
    {
        this.index = 41;
    }

    @Override
    public void caseTPontoVirgula(@SuppressWarnings("unused") TPontoVirgula node)
    {
        this.index = 42;
    }

    @Override
    public void caseTDoisPontos(@SuppressWarnings("unused") TDoisPontos node)
    {
        this.index = 43;
    }

    @Override
    public void caseTId(@SuppressWarnings("unused") TId node)
    {
        this.index = 44;
    }

    @Override
    public void caseEOF(@SuppressWarnings("unused") EOF node)
    {
        this.index = 45;
    }
}

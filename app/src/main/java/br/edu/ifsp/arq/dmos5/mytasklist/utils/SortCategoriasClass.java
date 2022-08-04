package br.edu.ifsp.arq.dmos5.mytasklist.utils;

import java.util.Comparator;

import br.edu.ifsp.arq.dmos5.mytasklist.model.Categoria;

public class SortCategoriasClass implements Comparator<Categoria> {

    @Override
    public int compare(Categoria c1, Categoria c2) {
        if (c1.equals(c2)){
            return 100;
        }
        return c1.getNome().compareTo(c2.getNome());
    }

}

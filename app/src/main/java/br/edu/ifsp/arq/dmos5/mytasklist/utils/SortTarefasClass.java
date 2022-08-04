package br.edu.ifsp.arq.dmos5.mytasklist.utils;

import java.util.Comparator;

import br.edu.ifsp.arq.dmos5.mytasklist.model.Tarefa;

public class SortTarefasClass implements Comparator<Tarefa> {

        // Method of this class
        // @Override
        public int compare(Tarefa a, Tarefa b){
                // Returning the value after comparing the objects
                // this will sort the data in Ascending order
                if (!a.getData().equals(b.getData())){
                        return a.getData().compareTo(b.getData());
                } else {
                        return a.getHora().compareTo(b.getHora());
                }
        }
}
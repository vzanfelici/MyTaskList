package br.edu.ifsp.arq.dmos5.mytasklist.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifsp.arq.dmos5.mytasklist.R;
import br.edu.ifsp.arq.dmos5.mytasklist.model.Categoria;

public class ListarCategoriasAdapter extends BaseAdapter {

    private final List<Categoria> categorias;
    private final Activity activity;

    public ListarCategoriasAdapter(List<Categoria> categorias, Activity activity) {
        this.categorias = categorias;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return categorias.size();
    }

    @Override
    public Object getItem(int i) {
        return categorias.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.adapter_listar_categorias, parent, false);

        Categoria categoria = categorias.get(position);

        TextView txtCategoriaNome = view.findViewById(R.id.txt_item_categoria_nome);
        txtCategoriaNome.setText(categoria.getNome());

        return view;
    }
}

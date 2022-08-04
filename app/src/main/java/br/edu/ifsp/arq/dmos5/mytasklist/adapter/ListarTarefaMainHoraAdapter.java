package br.edu.ifsp.arq.dmos5.mytasklist.adapter;

import static br.edu.ifsp.arq.dmos5.mytasklist.R.drawable.star_fill;
import static br.edu.ifsp.arq.dmos5.mytasklist.R.drawable.star_line;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.List;

import br.edu.ifsp.arq.dmos5.mytasklist.R;
import br.edu.ifsp.arq.dmos5.mytasklist.model.Tarefa;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.TarefaViewModel;

public class ListarTarefaMainHoraAdapter extends BaseAdapter {

    private final List<Tarefa> tarefas;
    private final Activity activity;

    private TarefaViewModel tarefaViewModel;

    public ListarTarefaMainHoraAdapter(List<Tarefa> tarefas, Activity activity) {
        this.tarefas = tarefas;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return tarefas.size();
    }

    @Override
    public Object getItem(int i) {
        return tarefas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.adapter_listar_tarefas_main_hora, parent, false);

        tarefaViewModel = new ViewModelProvider((ViewModelStoreOwner) view.getContext())
                .get(TarefaViewModel.class);

        Tarefa tarefa = tarefas.get(position);

        TextView txtItemDescricao = view.findViewById(R.id.txt_item_main_hora_descricao);
        TextView txtItemHora = view.findViewById(R.id.txt_item_main_hora_hora);
        txtItemDescricao.setText(tarefa.getDescricao());
        txtItemHora.setText(tarefa.getHora());

        ImageButton imgStarDataHora = view.findViewById(R.id.img_main_hora_flag);

        if (tarefa.isFavoritado()){
            imgStarDataHora.setImageDrawable(ContextCompat.getDrawable(view.getContext(), star_fill));
        } else {
            imgStarDataHora.setImageDrawable(ContextCompat.getDrawable(view.getContext(), star_line));
        }

        imgStarDataHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tarefa.isFavoritado()){
                    imgStarDataHora.setImageDrawable(ContextCompat.getDrawable(view.getContext(), star_line));
                    tarefa.setFavoritado(false);
                } else {
                    imgStarDataHora.setImageDrawable(ContextCompat.getDrawable(view.getContext(), star_fill));
                    tarefa.setFavoritado(true);
                }
                tarefaViewModel.updateFavoritado(tarefa);
            }
        });



        return view;
    }
}

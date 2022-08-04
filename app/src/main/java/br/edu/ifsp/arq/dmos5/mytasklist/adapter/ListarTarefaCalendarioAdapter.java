package br.edu.ifsp.arq.dmos5.mytasklist.adapter;

import static br.edu.ifsp.arq.dmos5.mytasklist.R.drawable.star_fill;
import static br.edu.ifsp.arq.dmos5.mytasklist.R.drawable.star_line;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
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

public class ListarTarefaCalendarioAdapter extends BaseAdapter {

    private final List<Tarefa> tarefas;
    private final Activity activity;

    private TarefaViewModel tarefaViewModel;

    public ListarTarefaCalendarioAdapter(List<Tarefa> tarefas, Activity activity) {
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
        View view = activity.getLayoutInflater().inflate(R.layout.adapter_listar_tarefas_calendario, parent, false);

        tarefaViewModel = new ViewModelProvider((ViewModelStoreOwner) view.getContext())
                .get(TarefaViewModel.class);

        Tarefa tarefa = tarefas.get(position);

        TextView txtItemDescricao = view.findViewById(R.id.txt_item_calendario_descricao);
        TextView txtItemHora = view.findViewById(R.id.txt_item_calendario_hora);
        txtItemDescricao.setText(tarefa.getDescricao());
        txtItemHora.setText(tarefa.getHora());

        ImageButton imgStarDataHora = view.findViewById(R.id.img_calendario_flag);

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

        if (tarefa.isAtrasado()){
            txtItemDescricao.setTextColor(Color.RED);
            txtItemHora.setTextColor(Color.RED);
            txtItemDescricao.setPaintFlags(txtItemDescricao.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            txtItemHora.setPaintFlags(txtItemDescricao.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (tarefa.isRealizado()){
            txtItemDescricao.setTextColor(Color.BLUE);
            txtItemHora.setTextColor(Color.BLUE);
            txtItemDescricao.setPaintFlags(txtItemDescricao.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            txtItemHora.setPaintFlags(txtItemDescricao.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return view;
    }
}

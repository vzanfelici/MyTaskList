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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.edu.ifsp.arq.dmos5.mytasklist.R;
import br.edu.ifsp.arq.dmos5.mytasklist.model.Tarefa;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.TarefaViewModel;

public class ListarTarefaMainDataHoraAdapter extends BaseAdapter {

    private final List<Tarefa> tarefas;
    private final Activity activity;

    private Calendar cal = Calendar.getInstance();
    private TarefaViewModel tarefaViewModel;

    public ListarTarefaMainDataHoraAdapter(List<Tarefa> tarefas, Activity activity) {
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
        View view = activity.getLayoutInflater().inflate(R.layout.adapter_listar_tarefas_main_data_hora, parent, false);

        tarefaViewModel = new ViewModelProvider((ViewModelStoreOwner) view.getContext())
                .get(TarefaViewModel.class);

        Tarefa tarefa = tarefas.get(position);
        TextView txtItemDescricao = view.findViewById(R.id.txt_item_main_data_hora_descricao);
        TextView txtItemDataHora = view.findViewById(R.id.txt_item_main_hora_data);

        txtItemDescricao.setText(tarefa.getDescricao());

        ImageButton imgStarDataHora = view.findViewById(R.id.img_main_hora_data_flag);

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

        Date hoje = new Date();
        cal = Calendar.getInstance();
        cal.setTime(hoje);

        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date amanha = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date depoisDeAmanha = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date doisDiasDepoisDeAmanha = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date tresDiasDepoisDeAmanha = cal.getTime();

        cal.setTime(hoje);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date ontem = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date antesDeOntem = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date doisDiasAntesDeOntem = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date tresDiasAntesDeOntem = cal.getTime();

        if (tarefa.getData().equals(formataData.format(hoje))) {
            cal.setTime(hoje);
            txtItemDataHora.setText("HOJE " + tarefa.getHora());
        } else if (tarefa.getData().equals(formataData.format(amanha))){
            cal.setTime(amanha);
            txtItemDataHora.setText("AMANHÃ " + tarefa.getHora());
        } else if (tarefa.getData().equals(formataData.format(depoisDeAmanha))){
            cal.setTime(depoisDeAmanha);
            txtItemDataHora.setText(getWeek(formataData.format(depoisDeAmanha))+" "+ tarefa.getHora());
        } else if (tarefa.getData().equals(formataData.format(doisDiasDepoisDeAmanha))){
            cal.setTime(doisDiasDepoisDeAmanha);
            txtItemDataHora.setText(getWeek(formataData.format(doisDiasDepoisDeAmanha))+" "+ tarefa.getHora());
        } else if (tarefa.getData().equals(formataData.format(tresDiasDepoisDeAmanha))){
            cal.setTime(tresDiasDepoisDeAmanha);
            txtItemDataHora.setText(getWeek(formataData.format(tresDiasDepoisDeAmanha))+" "+ tarefa.getHora());
        } else if (tarefa.getData().equals(formataData.format(ontem))){
            cal.setTime(ontem);
            txtItemDataHora.setText("ONTEM " + tarefa.getHora());
        } else if (tarefa.getData().equals(formataData.format(antesDeOntem))){
            cal.setTime(antesDeOntem);
            txtItemDataHora.setText(getPastWeek(formataData.format(antesDeOntem))+" "+ tarefa.getHora());
        } else if (tarefa.getData().equals(formataData.format(doisDiasAntesDeOntem))){
            cal.setTime(doisDiasAntesDeOntem);
            txtItemDataHora.setText(getPastWeek(formataData.format(doisDiasAntesDeOntem))+" "+ tarefa.getHora());
        } else if (tarefa.getData().equals(formataData.format(tresDiasAntesDeOntem))){
            cal.setTime(tresDiasAntesDeOntem);
            txtItemDataHora.setText(getPastWeek(formataData.format(tresDiasAntesDeOntem))+" "+ tarefa.getHora());
        } else {
            txtItemDataHora.setText(tarefa.getData()+" "+ tarefa.getHora());
        }

        return view;
    }

    public static String getWeek(String date){
        String dayWeek = "---";
        GregorianCalendar gc = new GregorianCalendar();
        try {
            gc.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(date));
            switch (gc.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SUNDAY:
                    dayWeek = "DOMINGO";
                    break;
                case Calendar.MONDAY:
                    dayWeek = "SEGUNDA-FEIRA";
                    break;
                case Calendar.TUESDAY:
                    dayWeek = "TERÇA-FEIRA";
                    break;
                case Calendar.WEDNESDAY:
                    dayWeek = "QUARTA-FEIRA";
                    break;
                case Calendar.THURSDAY:
                    dayWeek = "QUINTA-FEIRA";
                    break;
                case Calendar.FRIDAY:
                    dayWeek = "SEXTA-FEIRA";
                    break;
                case Calendar.SATURDAY:
                    dayWeek = "SÁBADO";
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayWeek;
    }

    public static String getPastWeek(String date){
        String dayWeek = "---";
        GregorianCalendar gc = new GregorianCalendar();
        try {
            gc.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(date));
            switch (gc.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SUNDAY:
                    dayWeek = "DOMINGO PASSADO";
                    break;
                case Calendar.MONDAY:
                    dayWeek = "SEGUNDA PASSADA";
                    break;
                case Calendar.TUESDAY:
                    dayWeek = "TERÇA PASSADA";
                    break;
                case Calendar.WEDNESDAY:
                    dayWeek = "QUARTA PASSADA";
                    break;
                case Calendar.THURSDAY:
                    dayWeek = "QUINTA PASSADA";
                    break;
                case Calendar.FRIDAY:
                    dayWeek = "SEXTA PASSADA";
                    break;
                case Calendar.SATURDAY:
                    dayWeek = "SÁBADO PASSADO";
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayWeek;
    }
}
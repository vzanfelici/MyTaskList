package br.edu.ifsp.arq.dmos5.mytasklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.edu.ifsp.arq.dmos5.mytasklist.adapter.ListarTarefaCalendarioAdapter;
import br.edu.ifsp.arq.dmos5.mytasklist.model.Tarefa;
import br.edu.ifsp.arq.dmos5.mytasklist.utils.SortTarefasClass;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.TarefaViewModel;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.UsuarioViewModel;

public class CalendarioActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtTitulo;

    private TextView txtData;
    private CalendarView calendar;

    private ListView lsvListarTarefas;

    private UsuarioViewModel usuarioViewModel;
    private TarefaViewModel tarefaViewModel;

    private Tarefa tarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usuarioViewModel = new ViewModelProvider(this)
                .get(UsuarioViewModel.class);
        tarefaViewModel = new ViewModelProvider(this)
                .get(TarefaViewModel.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        swipeRefreshLayout = findViewById(R.id.refresh_layout_calendar);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(CalendarioActivity.this, CalendarioActivity.class));
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        txtTitulo = findViewById(R.id.toolbar_title);
        txtTitulo.setText(R.string.calendario_titulo);

        lsvListarTarefas = findViewById(R.id.lsv_listar_tarefa_calendario);

        txtData = findViewById(R.id.myDate);
        calendar = findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String dia ="";
                String mes ="";
                if (i2<10){
                    dia = "0"+i2;
                } else {
                    dia = String.valueOf(i2);
                }
                if (i1+1<10){
                    mes = "0"+(i1+1);
                } else {
                    mes = String.valueOf((i1+1));
                }

                String date = dia + "/" + mes + "/" + i;

                configurarListView(date);
            }
        });

        lsvListarTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CalendarioActivity.this, TarefaAtualizarActivity.class);
                final Tarefa tarefaSelecionada = (Tarefa) lsvListarTarefas.getAdapter().getItem(position);
                intent.putExtra("tarefaSelecionada", tarefaSelecionada);
                intent.putExtra("option", 1);
                startActivity(intent);
            }
        });
    }

    private void configurarListView(String date) {
        lsvListarTarefas.setVisibility(View.INVISIBLE);

        tarefaViewModel.getTarefasByIdAndData(date).observe(CalendarioActivity.this, new Observer<List<Tarefa>>() {
            int i = 0;

            @Override
            public void onChanged(List<Tarefa> tarefas) {
                if (tarefas != null) {
                    List<Tarefa> listTarefas = new ArrayList<>();
                    for (Tarefa t : tarefas) {
                        if (t != null) {
                            listTarefas.add(t);

                            if (i == listTarefas.size() - 1) {
                                Collections.sort(listTarefas, new SortTarefasClass());

                                ListarTarefaCalendarioAdapter adapter = new ListarTarefaCalendarioAdapter(listTarefas, CalendarioActivity.this);
                                lsvListarTarefas.setAdapter(adapter);
                                lsvListarTarefas.setVisibility(View.VISIBLE);
                                txtData.setText("Suas tarefas para o dia: "+date);
                            }
                            i++;
                        }
                    }
                    if (i==0){
                        txtData.setText("Você não tem tarefas para o dia: "+date);
                    }
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(CalendarioActivity.this, MainActivity.class));
        return true;
    }
}
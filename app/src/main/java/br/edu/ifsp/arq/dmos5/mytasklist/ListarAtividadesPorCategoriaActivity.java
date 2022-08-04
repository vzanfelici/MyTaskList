package br.edu.ifsp.arq.dmos5.mytasklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.edu.ifsp.arq.dmos5.mytasklist.adapter.ListarTarefaMainDataHoraAdapter;
import br.edu.ifsp.arq.dmos5.mytasklist.model.Categoria;
import br.edu.ifsp.arq.dmos5.mytasklist.model.Tarefa;
import br.edu.ifsp.arq.dmos5.mytasklist.utils.SortTarefasClass;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.TarefaViewModel;

public class ListarAtividadesPorCategoriaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtTitulo;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView txtCategoriaNome;
    private TextView txtCategoriaSemAtividade;

    private ListView lsvTarefas;

    private TarefaViewModel tarefaViewModel;

    private Categoria categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_atividades_por_categoria);

        tarefaViewModel = new ViewModelProvider(this)
                .get(TarefaViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        swipeRefreshLayout = findViewById(R.id.refresh_layout_atividades_categoria);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(ListarAtividadesPorCategoriaActivity.this, ListarAtividadesPorCategoriaActivity.class);
                intent.putExtra("categoriaSelecionada", categoria);
                startActivity(intent);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        txtTitulo = findViewById(R.id.toolbar_title);
        txtTitulo.setText(R.string.atividades_categorias_titulo);

        txtCategoriaNome = findViewById(R.id.txt_tarefa_categoria_nome);
        txtCategoriaSemAtividade = findViewById(R.id.txt_sem_tarefa_categoria);
        txtCategoriaSemAtividade.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        categoria = (Categoria) intent.getSerializableExtra("categoriaSelecionada");
        txtCategoriaNome.setText(categoria.getNome());

        lsvTarefas = findViewById(R.id.lsv_listar_tarefas_categoria);
        tarefaViewModel.getTarefasByIdAndCaregoria(categoria.getNome()).observe(this, new Observer<List<Tarefa>>() {
            int i = 0;
            List<Tarefa> listTarefasCategoria = new ArrayList<>();

            @Override
            public void onChanged(List<Tarefa> tarefas) {
                if (tarefas != null) {
                    for (Tarefa t : tarefas) {
                        if (t != null) {
                            listTarefasCategoria.add(t);

                            if (i == listTarefasCategoria.size() - 1) {

                                Collections.sort(listTarefasCategoria, new SortTarefasClass());

                                lsvTarefas.setVisibility(View.VISIBLE);
                                txtCategoriaSemAtividade.setVisibility(View.GONE);
                                ListarTarefaMainDataHoraAdapter adapter = new ListarTarefaMainDataHoraAdapter(listTarefasCategoria, ListarAtividadesPorCategoriaActivity.this);
                                lsvTarefas.setAdapter(adapter);

                            }
                            i++;
                        }
                    }
                    if (i==0){
                        txtCategoriaSemAtividade.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
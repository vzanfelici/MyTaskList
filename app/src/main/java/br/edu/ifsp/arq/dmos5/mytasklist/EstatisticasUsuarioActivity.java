package br.edu.ifsp.arq.dmos5.mytasklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import br.edu.ifsp.arq.dmos5.mytasklist.model.Tarefa;
import br.edu.ifsp.arq.dmos5.mytasklist.model.Usuario;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.TarefaViewModel;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.UsuarioViewModel;

public class EstatisticasUsuarioActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtTitulo;

    private TextView txtAtrasadas;
    private TextView txtAndamento;
    private TextView txtConcluidas;

    private TarefaViewModel tarefaViewModel;
    private UsuarioViewModel usuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas_usuario);

        tarefaViewModel = new ViewModelProvider(this)
                .get(TarefaViewModel.class);
        usuarioViewModel = new ViewModelProvider(this)
                .get(UsuarioViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtTitulo = findViewById(R.id.toolbar_title);
        txtTitulo.setText(R.string.usuario_estatistica_titulo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtAtrasadas = findViewById(R.id.txt_estatisticas_atrasadas);
        txtAndamento = findViewById(R.id.txt_estatisticas_andamento);
        txtConcluidas = findViewById(R.id.txt_estatisticas_concluidas);

        txtAtrasadas.setVisibility(View.INVISIBLE);
        txtAndamento.setVisibility(View.INVISIBLE);
        txtConcluidas.setVisibility(View.INVISIBLE);

        usuarioViewModel.isLogged().observe(this, new Observer<Usuario>() {
                    @Override
                    public void onChanged(Usuario usuario) {
                        if (usuario != null) {

                            tarefaViewModel.getTarefasAtrasadasCont().observe(EstatisticasUsuarioActivity.this, new Observer<List<Tarefa>>() {
                                int i=0;

                                @Override
                                public void onChanged(List<Tarefa> tarefas) {
                                    if (tarefas != null) {
                                        for (Tarefa t : tarefas) {
                                            if (t != null) {
                                                i++;
                                            }
                                        }
                                        if (i<=0){
                                            txtAtrasadas.setText("Você possui o total de 0 tarefas atrasadas.");
                                        } else if (i==1){
                                            txtAtrasadas.setText("Você possui o total de "+i+" tarefa atrasada.");
                                        } else if (i>1){
                                            txtAtrasadas.setText("Você possui o total de "+i+" tarefas atrasadas.");
                                        }
                                        txtAtrasadas.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                            tarefaViewModel.getTarefasAndamentoCont().observe(EstatisticasUsuarioActivity.this, new Observer<List<Tarefa>>() {
                                int i=0;

                                @Override
                                public void onChanged(List<Tarefa> tarefas) {
                                    if (tarefas != null) {
                                        for (Tarefa t : tarefas) {
                                            if (t != null) {
                                                i++;
                                            }
                                        }
                                        if (i<=0){
                                            txtAndamento.setText("Você possui o total de 0 tarefas em andamento.");
                                        } else if (i==1){
                                            txtAndamento.setText("Você possui o total de "+i+" tarefa em andamento.");
                                        } else if (i>1){
                                            txtAndamento.setText("Você possui o total de "+i+" tarefas em andamento.");
                                        }
                                        txtAndamento.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                            tarefaViewModel.getTarefasConcluidasCont().observe(EstatisticasUsuarioActivity.this, new Observer<List<Tarefa>>() {
                                int i=0;

                                @Override
                                public void onChanged(List<Tarefa> tarefas) {
                                    if (tarefas != null) {
                                        for (Tarefa t : tarefas) {
                                            if (t != null) {
                                                i++;
                                            }
                                        }
                                        if (i<=0){
                                            txtConcluidas.setText("Você possui o total de 0 tarefas concluídas.");
                                        } else if (i==1){
                                            txtConcluidas.setText("Você possui o total de "+i+" tarefa concluídas.");
                                        } else if (i>1){
                                            txtConcluidas.setText("Você possui o total de "+i+" tarefas concluídas.");
                                        }
                                        txtConcluidas.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                        } else{
                            startActivity(new Intent(EstatisticasUsuarioActivity.this,
                                    UsuarioLoginActivity.class));
                            finish();
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(EstatisticasUsuarioActivity.this, MainActivity.class));
        return true;
    }
}
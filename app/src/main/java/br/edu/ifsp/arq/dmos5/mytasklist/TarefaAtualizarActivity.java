package br.edu.ifsp.arq.dmos5.mytasklist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import br.edu.ifsp.arq.dmos5.mytasklist.model.Categoria;
import br.edu.ifsp.arq.dmos5.mytasklist.model.Tarefa;
import br.edu.ifsp.arq.dmos5.mytasklist.model.Usuario;
import br.edu.ifsp.arq.dmos5.mytasklist.utils.SortCategoriasClass;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.CategoriaViewModel;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.TarefaViewModel;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.UsuarioViewModel;

public class TarefaAtualizarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtTitulo;

    private TextView txtNome;
    private TextView txtData;
    private TextView txtHora;
    private Spinner spCategoria;
    private CheckBox cbxRealizado;

    private Button btnAtualizar;
    private ImageButton btnExcluir;
    private ImageButton btnShare;

    private Tarefa tarefa;
    private TarefaViewModel tarefaViewModel;
    private UsuarioViewModel usuarioViewModel;
    private CategoriaViewModel categoriaViewModel;

    private int option;

    private List<String> listCategoriasNome = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa_atualizar);

        tarefaViewModel = new ViewModelProvider(this)
                .get(TarefaViewModel.class);
        usuarioViewModel = new ViewModelProvider(this)
                .get(UsuarioViewModel.class);
        categoriaViewModel = new ViewModelProvider(this)
                .get(CategoriaViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtTitulo = findViewById(R.id.toolbar_title);
        txtTitulo.setText(R.string.tarefa_atualizar_titulo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtNome = findViewById(R.id.txt_edit_tarefa_atualizar_nome);
        txtData = findViewById(R.id.txt_edit_tarefa_atualizar_data);
        txtHora = findViewById(R.id.txt_edit_tarefa_atualizar_hora);
        spCategoria = findViewById(R.id.sp_atualizar_categorias);
        cbxRealizado = findViewById(R.id.cbx_atualizar_realizado);

        categoriaViewModel.getCategoriasById().observe(this, new Observer<List<Categoria>>() {
            int i=0;

            @Override
            public void onChanged(List<Categoria> categorias) {
                if (categorias != null) {
                    List<String> listCategoriasNome = new ArrayList<>();

                    Collections.sort(categorias, new SortCategoriasClass());

                    listCategoriasNome.add("-selecione-");
                    for (Categoria c: categorias){
                        listCategoriasNome.add(c.getNome());
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(TarefaAtualizarActivity.this, android.R.layout.simple_spinner_item, listCategoriasNome);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCategoria.setAdapter(arrayAdapter);
                }
            }
        });

        Intent intent = getIntent();
        tarefa = (Tarefa) intent.getSerializableExtra("tarefaSelecionada");
        option = intent.getIntExtra("option", -1);
        if (tarefa != null) {
            txtNome.setText(tarefa.getDescricao());
            txtData.setText(tarefa.getData());
            txtHora.setText(tarefa.getHora());

            String categoriaEsc = tarefa.getCategoria();

            categoriaViewModel.getCategoriasById().observe(this, new Observer<List<Categoria>>() {
                @Override
                public void onChanged(List<Categoria> categorias) {
                    if (categorias != null) {
                        Collections.sort(categorias, new SortCategoriasClass());

                        for (int i=0; i<categorias.size(); i++){
                            if (categorias.get(i).getNome().equals(categoriaEsc)){
                                spCategoria.setSelection(i+1);
                            }
                        }
                    }
                }
            });

            cbxRealizado.setChecked(tarefa.isRealizado());
        }

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                updateCalendar();
            }

            private void updateCalendar(){
                String Format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.getDefault());
                txtData.setText(sdf.format(calendar.getTime()));
            }
        };
        txtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TarefaAtualizarActivity.this, date,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, i1);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                txtHora.setText(sdf.format(calendar.getTime()));
            }
        };
        txtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(TarefaAtualizarActivity.this, time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        btnAtualizar = findViewById(R.id.btn_tarefa_atualizar);
        btnExcluir = findViewById(R.id.btn_tarefa_excluir);
        btnShare = findViewById(R.id.btn_tarefa_compartilhar);

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    tarefa.setDescricao(txtNome.getText().toString());
                    tarefa.setData(txtData.getText().toString());
                    tarefa.setHora(txtHora.getText().toString());
                    tarefa.setDataHora(tarefa.converterDataHora(txtData.getText().toString(), txtHora.getText().toString()));
                    tarefa.setCategoria(spCategoria.getSelectedItem().toString());
                    tarefa.setRealizado(cbxRealizado.isChecked());

                    tarefaViewModel.update(tarefa);

                    Toast.makeText(TarefaAtualizarActivity.this, "Tarefa atualizada com Sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog a = new AlertDialog.Builder(TarefaAtualizarActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja realmente excluir essa tarefa?")
                        .setPositiveButton("Sim",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String usuarioId = tarefa.getUsuarioId();
                                        tarefaViewModel.getUsuario(usuarioId).observe(TarefaAtualizarActivity.this, new Observer<Usuario>() {
                                            @Override
                                            public void onChanged(Usuario usuario) {
                                                if (usuario!=null){
                                                    usuario.setQtdAtividades(usuario.getQtdAtividades()-1);
                                                    usuarioViewModel.update(usuario);
                                                }
                                            }
                                        });

                                        tarefaViewModel.delete(tarefa);

                                        Toast.makeText(TarefaAtualizarActivity.this, "Tarefa removida com Sucesso", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(TarefaAtualizarActivity.this, MainActivity.class));

                                        finish();
                                    }
                                })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String texto = "Olá, eu acabei de criar uma nova tarefa chamada: '"+tarefa.getDescricao()+"' !" +
                        "\n - Gostaria de tentar criar a sua? Acesse 'My Task List'";
                sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;
        if(txtNome.getText().toString().trim().isEmpty()){
            txtNome.setError("Preencha o campo nome");
            isValid = false;
        }else{
            txtNome.setError(null);
        }

        if(txtData.getText().toString().trim().isEmpty()){
            txtData.setError("Preencha o campo data");
            isValid = false;
        }else{
            txtData.setError(null);
        }

        if(txtHora.getText().toString().trim().isEmpty()){
            txtHora.setError("Preencha o campo horário");
            isValid = false;
        }else{
            txtHora.setError(null);
        }

        if (spCategoria.getSelectedItemPosition()==0){
            TextView errorText = (TextView)spCategoria.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            isValid = false;
        }

        return isValid;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        if (option==-1){
            System.out.println("CAIU AQUIIIIIIIII OPTION -1");
            startActivity(new Intent(TarefaAtualizarActivity.this, MainActivity.class));
        }
        return true;
    }
}
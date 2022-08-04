package br.edu.ifsp.arq.dmos5.mytasklist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

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

public class TarefaCadastroActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtTitulo;

    private TextInputEditText txtNome;
    private TextInputEditText txtData;
    private TextInputEditText txtHorario;
    private Spinner spCategoria;
    private Calendar calendar;

    private Button btnCadastro;

    UsuarioViewModel usuarioViewModel;
    TarefaViewModel tarefaViewModel;
    CategoriaViewModel categoriaViewModel;

    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa_cadastro);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtTitulo = findViewById(R.id.toolbar_title);
        txtTitulo.setText(R.string.tarefa_cadastro_titulo);

        txtNome = findViewById(R.id.txt_edit_tarefa_nome);

        txtData = findViewById(R.id.txt_edit_tarefa_data);
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
                new DatePickerDialog(TarefaCadastroActivity.this, date,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtHorario = findViewById(R.id.txt_edit_tarefa_hora);
        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, i1);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                txtHorario.setText(sdf.format(calendar.getTime()));
            }
        };
        txtHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(TarefaCadastroActivity.this, time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        categoriaViewModel = new ViewModelProvider(this)
                .get(CategoriaViewModel.class);

        spCategoria = findViewById(R.id.sp_categorias);
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

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(TarefaCadastroActivity.this, android.R.layout.simple_spinner_item, listCategoriasNome);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCategoria.setAdapter(arrayAdapter);
                }
            }
        });

        usuarioViewModel = new ViewModelProvider(this)
                .get(UsuarioViewModel.class);
        tarefaViewModel = new ViewModelProvider(this)
                .get(TarefaViewModel.class);

        btnCadastro = findViewById(R.id.btn_tarefa_cadastrar);
        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuarioViewModel.isLogged().observe(TarefaCadastroActivity.this, new Observer<Usuario>() {
                    @Override
                    public void onChanged(Usuario usuario) {
                        if(usuario != null){
                            TarefaCadastroActivity.this.usuario = usuario;

                            if (validate()){

                                Tarefa tarefa = new Tarefa(
                                        usuario.getId(),
                                        txtNome.getText().toString(),
                                        txtData.getText().toString(),
                                        txtHorario.getText().toString(),
                                        false,
                                        false,
                                        false,
                                        spCategoria.getSelectedItem().toString());

                                tarefaViewModel.createTarefa(tarefa);

                                Toast.makeText(TarefaCadastroActivity.this, String.format("Tarefa registrada!\nDia: %s Horário: %s", txtData.getText(), txtHorario.getText()), Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            startActivity(new Intent(TarefaCadastroActivity.this,
                                    UsuarioLoginActivity.class));
                            finish();
                        }
                    }
                });

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

        if(txtHorario.getText().toString().trim().isEmpty()){
            txtHorario.setError("Preencha o campo horário");
            isValid = false;
        }else{
            txtHorario.setError(null);
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
        startActivity(new Intent(TarefaCadastroActivity.this, MainActivity.class));
        return true;
    }
}
package br.edu.ifsp.arq.dmos5.mytasklist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.edu.ifsp.arq.dmos5.mytasklist.model.Usuario;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.UsuarioViewModel;


public class UsuarioPerfilActivity extends AppCompatActivity {

    private final int REQUEST_TAKE_PHOTO = 1;

    private Toolbar toolbar;
    private TextView txtTitulo;
    private TextInputEditText txtNome;
    private TextInputEditText txtSobrenome;
    private TextInputEditText txtEmail;
    private Button btnAtualizar;

    private TextInputEditText txtData;
    private Calendar calendar;

    private UsuarioViewModel usuarioViewModel;
    private Usuario usuario;

    private LinearLayout llAlterarTema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_perfil);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtTitulo = findViewById(R.id.toolbar_title);
        txtTitulo.setText(R.string.usuario_perfil_titulo);
        txtNome = findViewById(R.id.txt_edit_perfil_nome);
        txtSobrenome = findViewById(R.id.txt_edit_perfil_sobrenome);
        txtEmail = findViewById(R.id.txt_edit_perfil_email);

        txtData = findViewById(R.id.txt_edit_perfil_data_nascimento);
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
                new DatePickerDialog(UsuarioPerfilActivity.this, date,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        usuarioViewModel = new ViewModelProvider(this)
                .get(UsuarioViewModel.class);

        usuarioViewModel.isLogged().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if(usuario != null){
                    UsuarioPerfilActivity.this.usuario = usuario;
                    txtNome.setText(usuario.getNome());
                    txtEmail.setText(usuario.getEmail());

                    String sobrenome = usuario.getSobrenome();
                    String data = usuario.getDataNascimento();

                    if (sobrenome!=null){
                        txtSobrenome.setText(sobrenome);
                    }
                    if (data!=null){
                        txtData.setText(data);
                    }

                }else{
                    startActivity(new Intent(UsuarioPerfilActivity.this,
                            UsuarioLoginActivity.class));
                    finish();
                }
            }
        });

        btnAtualizar = findViewById(R.id.btn_usuario_atualizar);
        btnAtualizar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }

    public void update(){
        if(!validate()){
            return;
        }

        usuario.setNome(txtNome.getText().toString());
        usuario.setSobrenome(txtSobrenome.getText().toString());
        usuario.setEmail(txtEmail.getText().toString());
        usuario.setDataNascimento(txtData.getText().toString());

        usuarioViewModel.update(usuario);

        Toast.makeText(this, getString(R.string.msg_perfil_sucesso), Toast.LENGTH_SHORT).show();
    }

    private boolean validate(){
        boolean isValid = true;
        if(txtNome.getText().toString().trim().isEmpty()){
            txtNome.setError("Preencha o campo nome");
            isValid = false;
        }else{
            txtNome.setError(null);
        }

        if (txtSobrenome.getText().toString().trim().isEmpty()){
            txtSobrenome.setError("Preencha o campo sobrenome");
            isValid = false;
        } else {
            txtSobrenome.setError(null);
        }

        if(txtEmail.getText().toString().trim().isEmpty()){
            txtEmail.setError("Preencha o campo email");
            isValid = false;
        }else{
            txtEmail.setError(null);
        }

        if(txtData.getText().toString().trim().isEmpty()){
            txtData.setError("Preencha o campo data");
            isValid = false;
        }else{
            txtData.setError(null);
        }

        return isValid;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(UsuarioPerfilActivity.this, MainActivity.class));
        return true;
    }


}
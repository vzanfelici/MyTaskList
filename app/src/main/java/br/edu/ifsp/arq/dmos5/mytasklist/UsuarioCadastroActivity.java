package br.edu.ifsp.arq.dmos5.mytasklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import br.edu.ifsp.arq.dmos5.mytasklist.model.Usuario;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.UsuarioViewModel;

public class UsuarioCadastroActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtTitulo;
    private TextInputEditText txtNome;
    private TextInputEditText txtData;
    private Spinner spnGenero;
    private TextInputEditText txtTelefone;
    private TextInputEditText txtEmail;
    private TextInputEditText txtSenha;
    private Button btnCadastrar;

    private UsuarioViewModel usuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_cadastro);

        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtTitulo = findViewById(R.id.toolbar_title);
        txtTitulo.setText(R.string.usuario_cadastro_titulo);
        txtNome = findViewById(R.id.txt_edit_nome);
        txtEmail = findViewById(R.id.txt_edit_email);
        txtData = findViewById(R.id.txt_edit_perfil_data_nascimento);
        txtSenha = findViewById(R.id.txt_edit_senha);

        btnCadastrar = findViewById(R.id.btn_usuario_cadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Usuario usuario = new Usuario(
                        txtNome.getText().toString(),
                        "",
                        "",
                        txtEmail.getText().toString(),
                        txtSenha.getText().toString(),
                        0);

                if(validate()){
                    usuarioViewModel.createUsuario(usuario);
                    usuarioViewModel.login(txtEmail.getText().toString(), txtSenha.getText().toString())
                            .observe(UsuarioCadastroActivity.this, new Observer<Usuario>() {
                                        @Override
                                        public void onChanged(Usuario usuario) {
                                            Intent intent = new Intent(UsuarioCadastroActivity.this,
                                                    UsuarioLoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                            );
                }
            }
        });
    }
    private boolean validate() {
        boolean isValid = true;
        if (txtNome.getText().toString().trim().isEmpty()) {
            txtNome.setError("Preencha o campo nome");
            isValid = false;
        } else {
            txtNome.setError(null);
        }

        if (txtSenha.getText().toString().trim().isEmpty()) {
            txtSenha.setError("Preencha o campo senha");
            isValid = false;
        } else if (txtSenha.getText().toString().length() < 6){
            txtSenha.setError("A senha deve ter 6 ou mais caracteres.");
            isValid = false;
        } else {
            txtSenha.setError(null);
        }

        if (txtEmail.getText().toString().trim().isEmpty()) {
            txtEmail.setError("Preencha o campo email");
            isValid = false;
        } else {
            txtEmail.setError(null);
        }
        return isValid;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
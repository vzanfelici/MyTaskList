package br.edu.ifsp.arq.dmos5.mytasklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import br.edu.ifsp.arq.dmos5.mytasklist.model.Usuario;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.UsuarioViewModel;

public class UsuarioLoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtTitulo;
    private Button btnCadastrar;
    private Button btnEntrar;
    private TextInputEditText txtEmail;
    private TextInputEditText txtSenha;

    private UsuarioViewModel usuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_login);

        usuarioViewModel =
                new ViewModelProvider(this).get(UsuarioViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtTitulo = findViewById(R.id.toolbar_title);
        txtTitulo.setText(R.string.usuario_login_titulo);
        txtEmail = findViewById(R.id.txt_edit_login_email);
        txtSenha = findViewById(R.id.txt_edit_login_senha);

        btnCadastrar = findViewById(R.id.btn_login_cadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(UsuarioLoginActivity.this,
                        UsuarioCadastroActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnEntrar = findViewById(R.id.btn_usuario_entrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuarioViewModel.login(txtEmail.getText().toString(),
                        txtSenha.getText().toString())
                        .observe(UsuarioLoginActivity.this, new Observer<Usuario>() {
                            @Override
                            public void onChanged(Usuario usuario) {
                                if(usuario == null){
                                    Toast.makeText(getApplicationContext(), R.string.msg_login,
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    startActivity(new Intent(UsuarioLoginActivity.this,
                                            MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(UsuarioLoginActivity.this, MainActivity.class));
        return true;
    }
}
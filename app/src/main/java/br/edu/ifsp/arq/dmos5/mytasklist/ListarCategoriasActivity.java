package br.edu.ifsp.arq.dmos5.mytasklist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.edu.ifsp.arq.dmos5.mytasklist.adapter.ListarCategoriasAdapter;
import br.edu.ifsp.arq.dmos5.mytasklist.model.Categoria;
import br.edu.ifsp.arq.dmos5.mytasklist.model.Usuario;
import br.edu.ifsp.arq.dmos5.mytasklist.utils.SortCategoriasClass;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.CategoriaViewModel;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.UsuarioViewModel;

public class ListarCategoriasActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private TextView txtTitulo;
    private SwipeRefreshLayout swipeRefreshLayout;


    private TextView txtSemCategoria;
    private TextView txtCadastrarCategoria;

    private FloatingActionButton fabAdicionar;

    private ListView lsvListarCategorias;

    private UsuarioViewModel usuarioViewModel;
    private CategoriaViewModel categoriaViewModel;

    private Usuario usuario;
    private Categoria categoria;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private TextInputEditText txtNome;

    private Button btnSave;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_categorias);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usuarioViewModel = new ViewModelProvider(this)
                .get(UsuarioViewModel.class);
        categoriaViewModel = new ViewModelProvider(this)
                .get(CategoriaViewModel.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        swipeRefreshLayout = findViewById(R.id.refresh_layout_categorias);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(ListarCategoriasActivity.this, ListarCategoriasActivity.class);
                startActivity(intent);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        txtTitulo = findViewById(R.id.toolbar_title);
        txtTitulo.setText(R.string.categorias_titulo);

        txtSemCategoria = findViewById(R.id.txt_sem_categoria);
        txtCadastrarCategoria = findViewById(R.id.txt_cadastro_categoria);

        txtSemCategoria.setVisibility(View.INVISIBLE);
        txtCadastrarCategoria.setVisibility(View.INVISIBLE);

        lsvListarCategorias = findViewById(R.id.lsv_listar_categorias);
        lsvListarCategorias.setVisibility(View.INVISIBLE);

        usuarioViewModel.isLogged().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if (usuario != null) {

                    categoriaViewModel.getCategoriasById().observe(ListarCategoriasActivity.this, new Observer<List<Categoria>>() {
                        int i = 0;

                        @Override
                        public void onChanged(List<Categoria> categorias) {
                            if (categorias != null) {
                                List<Categoria> listCategorias = new ArrayList<>();
                                for (Categoria c : categorias) {
                                    if (c != null) {
                                        listCategorias.add(c);

                                        if (i == listCategorias.size() - 1) {
                                            Collections.sort(listCategorias, new SortCategoriasClass());

                                            ListarCategoriasAdapter adapter = new ListarCategoriasAdapter(listCategorias, ListarCategoriasActivity.this);
                                            lsvListarCategorias.setAdapter(adapter);
                                            lsvListarCategorias.setVisibility(View.VISIBLE);
                                            txtSemCategoria.setVisibility(View.GONE);
                                            txtCadastrarCategoria.setVisibility(View.GONE);
                                        }
                                        i++;
                                    }
                                }
                                if (i==0){
                                    txtSemCategoria.setVisibility(View.VISIBLE);
                                    txtCadastrarCategoria.setVisibility(View.VISIBLE);
                                    lsvListarCategorias.setVisibility(View.GONE);
                                }
                            }
                        }
                    });

                } else {
                    startActivity(new Intent(ListarCategoriasActivity.this,
                            UsuarioLoginActivity.class));
                    finish();
                }
            }
        });

        lsvListarCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListarCategoriasActivity.this, ListarAtividadesPorCategoriaActivity.class);
                final Categoria categoriaSelecionada = (Categoria) lsvListarCategorias.getAdapter().getItem(position);
                intent.putExtra("categoriaSelecionada", categoriaSelecionada);
                startActivity(intent);
            }
        });


        lsvListarCategorias.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                final Categoria categoriaSelecionada = (Categoria) lsvListarCategorias.getAdapter().getItem(info.position);
                MenuItem compartilhar = menu.add("Compartilhar");
                MenuItem deletar = menu.add("Deletar");

                deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        AlertDialog a = new AlertDialog.Builder(ListarCategoriasActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Deletar")
                                .setMessage("Deseja realmente excluir essa categoria?")
                                .setPositiveButton("Sim",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                categoriaViewModel.delete(categoriaSelecionada);
                                                Toast.makeText(ListarCategoriasActivity.this, "Categoria deletada com sucesso!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), ListarCategoriasActivity.class));
                                            }
                                        })
                                .setNegativeButton("Não", null)
                                .show();

                        return false;
                    }
                });

                compartilhar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        String texto = "Olá, eu acabei de criar uma nova categoria chamada: "+categoriaSelecionada.getNome()+"!" +
                                "\n - Gostaria de tentar criar a sua? Acesse 'My Task List'";
                        sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        return false;
                    }
                });
            }});

        fabAdicionar = findViewById(R.id.fab_cadastrar_categoria);
        fabAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuarioViewModel.isLogged().observe(ListarCategoriasActivity.this, new Observer<Usuario>() {
                    @Override
                    public void onChanged(Usuario usuario) {
                        if(usuario != null){
                            ListarCategoriasActivity.this.usuario = usuario;

                            criarPopup();

                        }else{
                            startActivity(new Intent(ListarCategoriasActivity.this,
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

        return isValid;
    }

    public void criarPopup(){
        builder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_adicionar_categoria,null);
        txtNome = contactPopupView.findViewById(R.id.txt_edit_categoria_nome);
        btnSave = contactPopupView.findViewById(R.id.btn_popup_adicionar);
        btnCancel = contactPopupView.findViewById(R.id.btn_popup_cancelar);

        builder.setView(contactPopupView);
        dialog = builder.create();
        dialog.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){

                    Categoria categoria = new Categoria(
                            usuario.getId(),
                            txtNome.getText().toString());

                    categoriaViewModel.createCategoria(categoria);

                    Toast.makeText(ListarCategoriasActivity.this, String.format("Categoria registrada!"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ListarCategoriasActivity.class));
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(), ListarCategoriasActivity.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(ListarCategoriasActivity.this, MainActivity.class));
        return true;
    }
}
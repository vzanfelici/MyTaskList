package br.edu.ifsp.arq.dmos5.mytasklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.edu.ifsp.arq.dmos5.mytasklist.adapter.ListarTarefaMainDataHoraAdapter;
import br.edu.ifsp.arq.dmos5.mytasklist.adapter.ListarTarefaMainHoraAdapter;
import br.edu.ifsp.arq.dmos5.mytasklist.model.Tarefa;
import br.edu.ifsp.arq.dmos5.mytasklist.model.Usuario;
import br.edu.ifsp.arq.dmos5.mytasklist.utils.SortTarefasClass;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.TarefaViewModel;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.UsuarioViewModel;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtTitulo;
    private TextView txtLogin;

    private TextView txtNome;
    private TextView txtSemCadastro;
    private TextView txtSemTarefa;

    private TextView txtTarefaAtrasadas;
    private TextView txtTarefaHoje;
    private TextView txtTarefaProximosDias;
    private TextView txtTarefaConcluidas;

    private ListView lsvListarTarefasAtrasadas;
    private ListView lsvListarTarefasHoje;
    private ListView lsvListarTarefasProximosDias;
    private ListView lsvListarTarefasConcluidas;

    private List<Tarefa> listTarefasAtrasadas;
    private List<Tarefa> listTarefasHoje;
    private List<Tarefa> listTarefasProximosDias;
    private List<Tarefa> listTarefasConcluidas;

    private ListarTarefaMainDataHoraAdapter adapterAtrasadas;
    private ListarTarefaMainHoraAdapter adapterHoje;
    private ListarTarefaMainDataHoraAdapter adapterProximosDias;
    private ListarTarefaMainDataHoraAdapter adapterConcluidas;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private Button btnEntrar;
    private FloatingActionButton fabAddTarefa;

    private Tarefa tarefa;
    private UsuarioViewModel usuarioViewModel;
    private TarefaViewModel tarefaViewModel;

    private List<String> totalTarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioViewModel = new ViewModelProvider(this)
                .get(UsuarioViewModel.class);
        tarefaViewModel = new ViewModelProvider(this)
                .get(TarefaViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        txtTitulo = findViewById(R.id.toolbar_title);
        txtTitulo.setText(getString(R.string.app_name));

        txtNome = findViewById(R.id.txt_listar_tarefas_nome);
        txtNome.setVisibility(View.INVISIBLE);

        txtSemCadastro = findViewById(R.id.txt_sem_cadastro_main);
        txtSemTarefa = findViewById(R.id.txt_sem_tarefa_main);

        txtSemCadastro.setVisibility(View.INVISIBLE);
        txtSemTarefa.setVisibility(View.INVISIBLE);

        drawerLayout = findViewById(R.id.nav_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.toogle_open,
                R.string.toogle_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_account:
                        intent = new Intent(MainActivity.this,
                                UsuarioPerfilActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_calendar:
                        intent = new Intent(MainActivity.this,
                                CalendarioActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_category:
                        intent = new Intent(MainActivity.this,
                                ListarCategoriasActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_stats:
                        intent = new Intent(MainActivity.this,
                                EstatisticasUsuarioActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_settings:
                        intent = new Intent(MainActivity.this,
                                SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_logout:
                        usuarioViewModel.logout();
                        finish();
                        startActivity(getIntent());
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        txtLogin = navigationView.getHeaderView(0)
                .findViewById(R.id.header_profile_name);
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        UsuarioLoginActivity.class);
                startActivity(intent);
            }
        });

        fabAddTarefa = findViewById(R.id.fab_cadastrar_tarefa);
        fabAddTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        TarefaCadastroActivity.class);
                startActivity(intent);
            }
        });

        txtTarefaAtrasadas = findViewById(R.id.txt_listar_tarefas_atrasadas);
        txtTarefaHoje = findViewById(R.id.txt_listar_tarefas_hoje);
        txtTarefaProximosDias = findViewById(R.id.txt_listar_tarefas_proximos_dias);
        txtTarefaConcluidas = findViewById(R.id.txt_listar_tarefas_concluidas);

        txtTarefaAtrasadas.setVisibility(View.INVISIBLE);
        txtTarefaHoje.setVisibility(View.INVISIBLE);
        txtTarefaProximosDias.setVisibility(View.INVISIBLE);
        txtTarefaConcluidas.setVisibility(View.INVISIBLE);

        lsvListarTarefasAtrasadas = findViewById(R.id.lsv_listar_tarefas_atrasadas);
        lsvListarTarefasHoje = findViewById(R.id.lsv_listar_tarefas_hoje);
        lsvListarTarefasProximosDias = findViewById(R.id.lsv_listar_tarefas_proximos_dias);
        lsvListarTarefasConcluidas = findViewById(R.id.lsv_listar_tarefas_concluidas);

        totalTarefas = new ArrayList<>();
        listTarefasAtrasadas = new ArrayList<>();
        listTarefasHoje = new ArrayList<>();
        listTarefasProximosDias = new ArrayList<>();
        listTarefasConcluidas = new ArrayList<>();

        clickListsView();
    }

    private void clickListsView() {
        lsvListarTarefasAtrasadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TarefaAtualizarActivity.class);
                final Tarefa tarefaSelecionada = (Tarefa) lsvListarTarefasAtrasadas.getAdapter().getItem(position);
                intent.putExtra("tarefaSelecionada", tarefaSelecionada);
                startActivity(intent);
            }
        });

        lsvListarTarefasHoje.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TarefaAtualizarActivity.class);
                final Tarefa tarefaSelecionada = (Tarefa) lsvListarTarefasHoje.getAdapter().getItem(position);
                intent.putExtra("tarefaSelecionada", tarefaSelecionada);
                startActivity(intent);
            }
        });

        lsvListarTarefasProximosDias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TarefaAtualizarActivity.class);
                final Tarefa tarefaSelecionada = (Tarefa) lsvListarTarefasProximosDias.getAdapter().getItem(position);
                intent.putExtra("tarefaSelecionada", tarefaSelecionada);
                startActivity(intent);
            }
        });

        lsvListarTarefasConcluidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TarefaAtualizarActivity.class);
                final Tarefa tarefaSelecionada = (Tarefa) lsvListarTarefasConcluidas.getAdapter().getItem(position);
                intent.putExtra("tarefaSelecionada", tarefaSelecionada);
                startActivity(intent);
            }
        });
    }

    private void atualizarLista() {
        tarefaViewModel.getTarefasAtrasadasById().observe(this, new Observer<List<Tarefa>>() {
            int i = 0;

            @Override
            public void onChanged(List<Tarefa> tarefas) {
                if (tarefas != null) {
                    for (Tarefa t : tarefas) {
                        if (t != null) {
                            MainActivity.this.tarefa = t;
                            listTarefasAtrasadas.add(t);

                            if (i == listTarefasAtrasadas.size() - 1) {

                                Collections.sort(listTarefasAtrasadas, new SortTarefasClass());

                                lsvListarTarefasAtrasadas.setVisibility(View.VISIBLE);
                                txtTarefaAtrasadas.setVisibility(View.VISIBLE);
                                adapterAtrasadas = new ListarTarefaMainDataHoraAdapter(listTarefasAtrasadas, MainActivity.this);

                                lsvListarTarefasAtrasadas.setAdapter(adapterAtrasadas);

                                View item = adapterAtrasadas.getView(0, null, lsvListarTarefasAtrasadas);
                                item.measure(0, 0);
                                LayoutParams params = lsvListarTarefasAtrasadas.getLayoutParams();
                                if(adapterAtrasadas.getCount() == 1){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (1.1607143 * item.getMeasuredHeight());
                                } else if(adapterAtrasadas.getCount() == 2){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (2.18750001 * item.getMeasuredHeight());
                                } else if(adapterAtrasadas.getCount() == 3){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (3.21428572 * item.getMeasuredHeight());
                                } else if(adapterAtrasadas.getCount() == 4){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (4.24107143 * item.getMeasuredHeight());
                                } else if(adapterAtrasadas.getCount() >= 5){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (5.26785714 * item.getMeasuredHeight());
                                }
                                lsvListarTarefasAtrasadas.setLayoutParams(params);
                            }
                            i++;
                        }
                    }
                    if (i == 0) {
                        //System.out.println("CAIU AQUI ATRA -I- VAZIO");
                        txtTarefaAtrasadas.setVisibility(View.GONE);
                        lsvListarTarefasAtrasadas.setVisibility(View.GONE);
                        totalTarefas.add("1");
                    }
                } else {
                    //System.out.println("CAIU AQUI ATRA VAZIO");
                }
            }
        });

        tarefaViewModel.getTarefasHojeById().observe(this, new Observer<List<Tarefa>>() {
            int i = 0;

            @Override
            public void onChanged(List<Tarefa> tarefas) {
                if (tarefas != null) {
                    for (Tarefa t : tarefas) {
                        if (t != null) {
                            MainActivity.this.tarefa = t;
                            listTarefasHoje.add(t);

                            if (i == listTarefasHoje.size() - 1) {

                                Collections.sort(listTarefasHoje, new SortTarefasClass());

                                lsvListarTarefasHoje.setVisibility(View.VISIBLE);
                                txtTarefaHoje.setVisibility(View.VISIBLE);
                                adapterHoje = new ListarTarefaMainHoraAdapter(listTarefasHoje, MainActivity.this);

                                lsvListarTarefasHoje.setAdapter(adapterHoje);

                                View item = adapterHoje.getView(0, null, lsvListarTarefasHoje);
                                item.measure(0, 0);
                                LayoutParams params = lsvListarTarefasHoje.getLayoutParams();
                                if(adapterHoje.getCount() == 1){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (1.1607143 * item.getMeasuredHeight());
                                } else if(adapterHoje.getCount() == 2){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (2.18750001 * item.getMeasuredHeight());
                                } else if(adapterHoje.getCount() == 3){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (3.21428572 * item.getMeasuredHeight());
                                } else if(adapterHoje.getCount() == 4){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (4.24107143 * item.getMeasuredHeight());
                                } else if(adapterHoje.getCount() >= 5){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (5.26785714 * item.getMeasuredHeight());
                                }
                                lsvListarTarefasHoje.setLayoutParams(params);
                            }
                            i++;
                        }
                    }
                    if (i == 0) {
                        //System.out.println("CAIU AQUI HJ -I- VAZIO");
                        txtTarefaHoje.setVisibility(View.GONE);
                        lsvListarTarefasHoje.setVisibility(View.GONE);
                        totalTarefas.add("2");
                    }
                } else {
                    //System.out.println("CAIU AQUI HJ VAZIO");
                }
            }
        });

        tarefaViewModel.getTarefasProximosDiasById().observe(this, new Observer<List<Tarefa>>() {
            int i = 0;

            @Override
            public void onChanged(List<Tarefa> tarefas) {
                if (tarefas != null) {
                    for (Tarefa t : tarefas) {
                        if (t != null) {
                            MainActivity.this.tarefa = t;
                            listTarefasProximosDias.add(t);

                            if (i == listTarefasProximosDias.size() - 1) {
                                Collections.sort(listTarefasProximosDias, new SortTarefasClass());

                                lsvListarTarefasProximosDias.setVisibility(View.VISIBLE);
                                txtTarefaProximosDias.setVisibility(View.VISIBLE);
                                adapterProximosDias = new ListarTarefaMainDataHoraAdapter(listTarefasProximosDias, MainActivity.this);

                                lsvListarTarefasProximosDias.setAdapter(adapterProximosDias);

                                View item = adapterProximosDias.getView(0, null, lsvListarTarefasProximosDias);
                                item.measure(0, 0);
                                LayoutParams params = lsvListarTarefasProximosDias.getLayoutParams();
                                if(adapterProximosDias.getCount() == 1){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (1.1607143 * item.getMeasuredHeight());
                                } else if(adapterProximosDias.getCount() == 2){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (2.18750001 * item.getMeasuredHeight());
                                } else if(adapterProximosDias.getCount() == 3){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (3.21428572 * item.getMeasuredHeight());
                                } else if(adapterProximosDias.getCount() == 4){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (4.24107143 * item.getMeasuredHeight());
                                } else if(adapterProximosDias.getCount() >= 5){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (5.26785714 * item.getMeasuredHeight());
                                }
                                lsvListarTarefasProximosDias.setLayoutParams(params);
                                // 1.02678571
                            }
                            i++;
                        }
                    }
                    if (i == 0) {
                        //System.out.println("CAIU AQUI PROX -I- VAZIO");
                        txtTarefaProximosDias.setVisibility(View.GONE);
                        lsvListarTarefasProximosDias.setVisibility(View.GONE);
                        totalTarefas.add("3");
                    }
                } else {
                    //System.out.println("CAIU AQUI PROX VAZIO");
                }
            }
        });

        tarefaViewModel.getTarefasConcluidasById().observe(this, new Observer<List<Tarefa>>() {
            int i = 0;

            @Override
            public void onChanged(List<Tarefa> tarefas) {
                if (tarefas != null) {
                    for (Tarefa t : tarefas) {
                        if (t != null) {
                            MainActivity.this.tarefa = t;
                            listTarefasConcluidas.add(t);

                            if (i == listTarefasConcluidas.size() - 1) {
                                Collections.sort(listTarefasConcluidas, new SortTarefasClass());

                                lsvListarTarefasConcluidas.setVisibility(View.VISIBLE);
                                txtTarefaConcluidas.setVisibility(View.VISIBLE);
                                adapterConcluidas = new ListarTarefaMainDataHoraAdapter(listTarefasConcluidas, MainActivity.this);

                                lsvListarTarefasConcluidas.setAdapter(adapterConcluidas);

                                View item = adapterConcluidas.getView(0, null, lsvListarTarefasConcluidas);
                                item.measure(0, 0);
                                LayoutParams params = lsvListarTarefasConcluidas.getLayoutParams();
                                if(adapterConcluidas.getCount() == 1){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (1.1607143 * item.getMeasuredHeight());
                                } else if(adapterConcluidas.getCount() == 2){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (2.18750001 * item.getMeasuredHeight());
                                } else if(adapterConcluidas.getCount() == 3){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (3.21428572 * item.getMeasuredHeight());
                                } else if(adapterConcluidas.getCount() == 4){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (4.24107143 * item.getMeasuredHeight());
                                } else if(adapterConcluidas.getCount() >= 5){
                                    params.width = LayoutParams.MATCH_PARENT;
                                    params.height = (int) (5.26785714 * item.getMeasuredHeight());
                                }
                                lsvListarTarefasConcluidas.setLayoutParams(params);
                                // 1.02678571
                            }
                            i++;
                        }
                    }
                    if (i == 0) {
                        //System.out.println("CAIU AQUI CONCLU -I- VAZIO");
                        txtTarefaConcluidas.setVisibility(View.GONE);
                        lsvListarTarefasConcluidas.setVisibility(View.GONE);
                        totalTarefas.add("4");
                    }
                } else {
                    //System.out.println("CAIU AQUI CONCLU VAZIO");
                }
            }
        });
    }

    private void atualizarTarefasAtrasadas() {
        tarefaViewModel.atualizarTarefasAtrasadas().observe(this, new Observer<List<Tarefa>>() {
            @Override
            public void onChanged(List<Tarefa> tarefas) {
                if (tarefas != null) {
                    for (Tarefa t : tarefas) {
                        if (t != null) {
                            MainActivity.this.tarefa = t;

                            Calendar calendarHoje = Calendar.getInstance();
                            Calendar calendarVencimento = Calendar.getInstance();

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            Date dataTarefa = null;
                            try {
                                dataTarefa = simpleDateFormat.parse(tarefa.getData()+" "+tarefa.getHora());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Date hoje = new Date();
                            calendarHoje.setTime(hoje);
                            calendarVencimento.setTime(dataTarefa);
                            if (calendarHoje.after(calendarVencimento)){
                                tarefa.setAtrasado(true);
                            } else {
                                tarefa.setAtrasado(false);
                            }

                            tarefaViewModel.updateAtrasado(tarefa);
                        }
                    }
                }
            }
        });
    }

    public void criarPopup() {
        builder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_inicio, null);
        btnEntrar = contactPopupView.findViewById(R.id.btn_entrar_main);

        builder.setView(contactPopupView);
        dialog = builder.create();
        dialog.show();

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UsuarioLoginActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        atualizarTarefasAtrasadas();

        atualizarLista();

        usuarioViewModel.isLogged().observe(MainActivity.this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if (usuario != null) {
                    MenuItem navSair = navigationView.getMenu().getItem(2).getSubMenu().getItem().getSubMenu().getItem(1);
                    navSair.setVisible(true);
                    txtLogin.setText(usuario.getNome());
                    txtNome.setText("Olá, " + usuario.getNome() + " 😃");
                    txtNome.setVisibility(View.VISIBLE);
                    txtSemCadastro.setVisibility(View.GONE);

                    System.out.println("TAMANHO:::: "+totalTarefas.size());
                    for (String a:totalTarefas){
                        System.out.println("A:: "+a);
                    }
                    if(totalTarefas.size()==4){
                        txtSemTarefa.setVisibility(View.VISIBLE);
                    } else {
                        txtSemTarefa.setVisibility(View.GONE);
                    }

                } else {
                    MenuItem navSair = navigationView.getMenu().getItem(2).getSubMenu().getItem().getSubMenu().getItem(1);
                    navSair.setVisible(false);
                    txtNome.setText("Olá 😃");
                    txtNome.setVisibility(View.VISIBLE);

                    txtSemCadastro.setVisibility(View.VISIBLE);

                    criarPopup();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
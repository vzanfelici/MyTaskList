package br.edu.ifsp.arq.dmos5.mytasklist.repository;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifsp.arq.dmos5.mytasklist.model.Tarefa;

public class TarefasRepository {

    private FirebaseFirestore firestore;

    private RequestQueue queue;

    private SharedPreferences preference;

    public TarefasRepository(Application application) {
        firestore = FirebaseFirestore.getInstance();
        queue = Volley.newRequestQueue(application);
        preference = PreferenceManager.getDefaultSharedPreferences(application);
    }

    public void createTarefa(Tarefa tarefa){

        firestore.collection("tarefa").document(tarefa.getId()).set(tarefa).addOnSuccessListener(unused -> {
            Log.d(this.toString(), "Tarefa " + "registrada com sucesso.");
        });
    }

    public MutableLiveData<List<Tarefa>> getTarefasAtrasadasById(String usuarioId) {
        MutableLiveData<List<Tarefa>> liveData = new MutableLiveData<>();
        Query tarefaRef = firestore.collection("tarefa").whereEqualTo("usuarioId", usuarioId).whereEqualTo("realizado", false).whereEqualTo("atrasado",true);


        tarefaRef.get().addOnSuccessListener(snapshot -> {
            List<Tarefa> tarefas = snapshot.toObjects(Tarefa.class);

            liveData.setValue(tarefas);
        });

        return liveData;
    }

    public MutableLiveData<List<Tarefa>> getTarefasHojeById(String usuarioId) {
        MutableLiveData<List<Tarefa>> liveData = new MutableLiveData<>();

        Date hoje = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(hoje);

        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        String dataHoje = formataData.format(data);

        Query tarefaRef = firestore.collection("tarefa").whereEqualTo("usuarioId", usuarioId).whereEqualTo("data", dataHoje).whereEqualTo("realizado", false).whereEqualTo("atrasado",false);

        tarefaRef.get().addOnSuccessListener(snapshot -> {
            List<Tarefa> tarefas = snapshot.toObjects(Tarefa.class);

            liveData.setValue(tarefas);
        });

        return liveData;
    }

    public MutableLiveData<List<Tarefa>> getTarefasProximosDiasById(String usuarioId) {
        MutableLiveData<List<Tarefa>> liveData = new MutableLiveData<>();

        Date hoje = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(hoje);

        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        String dataHoje = formataData.format(data);

        Query tarefaRef = firestore.collection("tarefa").whereEqualTo("usuarioId", usuarioId).whereNotEqualTo("data", dataHoje).whereEqualTo("realizado", false).whereEqualTo("atrasado",false);
        tarefaRef.get().addOnSuccessListener(snapshot -> {
            List<Tarefa> tarefas = snapshot.toObjects(Tarefa.class);

            liveData.setValue(tarefas);
        });

        return liveData;
    }

    public MutableLiveData<List<Tarefa>> getTarefasConcluidasById(String usuarioId) {
        MutableLiveData<List<Tarefa>> liveData = new MutableLiveData<>();

        Query tarefaRef = firestore.collection("tarefa").whereEqualTo("usuarioId", usuarioId).whereEqualTo("realizado", true);

        tarefaRef.get().addOnSuccessListener(snapshot -> {
            List<Tarefa> tarefas = snapshot.toObjects(Tarefa.class);

            liveData.setValue(tarefas);
        });

        return liveData;
    }


    public LiveData<List<Tarefa>> getTarefasByIdAndData(String usuarioId, String dataEscolhida) {
        MutableLiveData<List<Tarefa>> liveData = new MutableLiveData<>();

        Query tarefaRef = firestore.collection("tarefa").whereEqualTo("usuarioId", usuarioId).whereEqualTo("data", dataEscolhida);

        tarefaRef.get().addOnSuccessListener(snapshot -> {
            List<Tarefa> tarefas = snapshot.toObjects(Tarefa.class);

            liveData.setValue(tarefas);
        });

        return liveData;
    }

    public boolean delete(Tarefa tarefa) {
        final Boolean[] deletado = {false};

        Task<Void> tarefaRef = firestore.collection("tarefa")
                .document(tarefa.getId()).delete().
                        addOnSuccessListener(unused -> {
                            deletado[0] = true;
                        });

        return deletado[0];
    }

    public boolean update(Tarefa tarefa) {
        final Boolean[] atualizado = {false};

        Map<String, Object> updateMap = new HashMap();
        updateMap.put("descricao", tarefa.getDescricao());
        updateMap.put("data", tarefa.getData());
        updateMap.put("hora", tarefa.getHora());
        updateMap.put("dataHora", tarefa.getDataHora());
        updateMap.put("categoria", tarefa.getCategoria());
        updateMap.put("realizado", tarefa.isRealizado());

        Task<Void> tarefaRef = firestore.collection("tarefa")
                .document(tarefa.getId()).update(updateMap).
                        addOnSuccessListener(unused -> {
                            atualizado[0] = true;
                        });

        return atualizado[0];
    }

    public LiveData<List<Tarefa>> atualizarTarefasAtrasadas() {
        MutableLiveData<List<Tarefa>> liveData = new MutableLiveData<>();

        Query tarefaRef = firestore.collection("tarefa").whereEqualTo("realizado", false);

        tarefaRef.get().addOnSuccessListener(snapshot -> {
            List<Tarefa> tarefas = snapshot.toObjects(Tarefa.class);

            liveData.setValue(tarefas);
        });

        return liveData;
    }

    public boolean updateAtrasado(Tarefa tarefa) {
        final Boolean[] atualizado = {false};

        Map<String, Object> updateMap = new HashMap();
        updateMap.put("atrasado", tarefa.isAtrasado());

        Task<Void> tarefaRef = firestore.collection("tarefa")
                .document(tarefa.getId()).update(updateMap).
                        addOnSuccessListener(unused -> {
                            atualizado[0] = true;
                        });

        return atualizado[0];
    }

    public boolean updateFavoritado(Tarefa tarefa) {
        final Boolean[] favoritado = {false};

        Map<String, Object> updateMap = new HashMap();
        updateMap.put("favoritado", tarefa.isFavoritado());

        Task<Void> tarefaRef = firestore.collection("tarefa")
                .document(tarefa.getId()).update(updateMap).
                        addOnSuccessListener(unused -> {
                            favoritado[0] = true;
                        });

        return favoritado[0];
    }

    public MutableLiveData<List<Tarefa>> getTarefasAtrasadasCont(String usuarioId) {
        MutableLiveData<List<Tarefa>> liveData = new MutableLiveData<>();

        Query tarefaRef = firestore.collection("tarefa").whereEqualTo("usuarioId", usuarioId).whereEqualTo("atrasado", true);

        tarefaRef.get().addOnSuccessListener(snapshot -> {
            List<Tarefa> tarefas = snapshot.toObjects(Tarefa.class);

            liveData.setValue(tarefas);
        });

        return liveData;
    }

    public MutableLiveData<List<Tarefa>> getTarefasAndamentoCont(String usuarioId) {
        MutableLiveData<List<Tarefa>> liveData = new MutableLiveData<>();

        Query tarefaRef = firestore.collection("tarefa").whereEqualTo("usuarioId", usuarioId).whereEqualTo("atrasado", false).whereEqualTo("realizado", false);

        tarefaRef.get().addOnSuccessListener(snapshot -> {
            List<Tarefa> tarefas = snapshot.toObjects(Tarefa.class);

            liveData.setValue(tarefas);
        });

        return liveData;
    }

    public MutableLiveData<List<Tarefa>> getTarefasConcluidasCont(String usuarioId) {
        MutableLiveData<List<Tarefa>> liveData = new MutableLiveData<>();

        Query tarefaRef = firestore.collection("tarefa").whereEqualTo("usuarioId", usuarioId).whereEqualTo("realizado", true);

        tarefaRef.get().addOnSuccessListener(snapshot -> {
            List<Tarefa> tarefas = snapshot.toObjects(Tarefa.class);

            liveData.setValue(tarefas);
        });

        return liveData;
    }

    public MutableLiveData<List<Tarefa>> getTarefasByIdAndCaregoria(String usuarioId, String categoriaNome) {
        MutableLiveData<List<Tarefa>> liveData = new MutableLiveData<>();

        Query tarefaRef = firestore.collection("tarefa").whereEqualTo("usuarioId", usuarioId).whereEqualTo("categoria", categoriaNome);

        tarefaRef.get().addOnSuccessListener(snapshot -> {
            List<Tarefa> tarefas = snapshot.toObjects(Tarefa.class);

            liveData.setValue(tarefas);
        });

        return liveData;
    }
}
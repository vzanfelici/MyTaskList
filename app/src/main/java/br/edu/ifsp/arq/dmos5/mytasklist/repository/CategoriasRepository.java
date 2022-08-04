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

import java.util.List;

import br.edu.ifsp.arq.dmos5.mytasklist.model.Categoria;

public class CategoriasRepository {

    private FirebaseFirestore firestore;

    private RequestQueue queue;

    private SharedPreferences preference;

    public CategoriasRepository(Application application) {
        firestore = FirebaseFirestore.getInstance();
        queue = Volley.newRequestQueue(application);
        preference = PreferenceManager.getDefaultSharedPreferences(application);
    }

    public LiveData<List<Categoria>> getCategoriasById(String usuarioId) {
        MutableLiveData<List<Categoria>> liveData = new MutableLiveData<>();

        Query categoriaRef = firestore.collection("categoria").whereEqualTo("usuarioId", usuarioId);

        categoriaRef.get().addOnSuccessListener(snapshot -> {
            List<Categoria> categorias = snapshot.toObjects(Categoria.class);

            liveData.setValue(categorias);
        });

        return liveData;
    }

    public void createCategoria(Categoria categoria){

        firestore.collection("categoria").document(categoria.getId()).set(categoria).addOnSuccessListener(unused -> {
            Log.d(this.toString(), "Categoria " + "registrada com sucesso.");
        });
    }

    public boolean delete(Categoria categoria) {
        final Boolean[] deletado = {false};

        Task<Void> categoriaRef = firestore.collection("categoria")
                .document(categoria.getId()).delete().
                        addOnSuccessListener(unused -> {
                            deletado[0] = true;
                        });

        return deletado[0];
    }

}

package br.edu.ifsp.arq.dmos5.mytasklist.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import java.util.List;
import java.util.Optional;

import br.edu.ifsp.arq.dmos5.mytasklist.model.Categoria;
import br.edu.ifsp.arq.dmos5.mytasklist.repository.CategoriasRepository;

public class CategoriaViewModel extends AndroidViewModel {

    public static final String USUARIO_ID = "USUARIO_ID";

    public CategoriasRepository categoriasRepository;

    public CategoriaViewModel(@NonNull Application application) {
        super(application);
        categoriasRepository = new CategoriasRepository(application);
    }

    public void createCategoria(Categoria categoria){
        categoriasRepository.createCategoria(categoria);
    }

    public LiveData<List<Categoria>> getCategoriasById() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        Optional<String> id = Optional.ofNullable(sharedPreferences.getString(USUARIO_ID, null));
        if(!id.isPresent()){
            return new MutableLiveData<>(null);
        }
        return categoriasRepository.getCategoriasById(id.get());
    }

    public void delete(Categoria categoria) {
        categoriasRepository.delete(categoria);
    }

}

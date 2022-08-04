package br.edu.ifsp.arq.dmos5.mytasklist.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import java.util.Optional;

import br.edu.ifsp.arq.dmos5.mytasklist.model.Usuario;
import br.edu.ifsp.arq.dmos5.mytasklist.repository.UsuariosRepository;

public class UsuarioViewModel extends AndroidViewModel {
    public static final String USUARIO_ID = "USUARIO_ID";

    private UsuariosRepository usuariosRepository;

    public UsuarioViewModel(@NonNull Application application) {
        super(application);
        usuariosRepository = new UsuariosRepository(application);
    }

    public void createUsuario(Usuario usuario){
        usuariosRepository.createUsuario(usuario);
    }

    public void update(Usuario usuario){
        usuariosRepository.update(usuario);
    }

    public LiveData<Usuario> login(String email, String senha) {
        return usuariosRepository.login(email, senha);
    }

    public void logout(){
        PreferenceManager.getDefaultSharedPreferences(getApplication())
                .edit().remove(USUARIO_ID)
                .apply();
    }

    public LiveData<Usuario> isLogged(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        Optional<String> id = Optional.ofNullable(sharedPreferences.getString(USUARIO_ID, null));
        if(!id.isPresent()){
            return new MutableLiveData<>(null);
        }
        return usuariosRepository.load(id.get());
    }

}

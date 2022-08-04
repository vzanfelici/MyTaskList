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

import br.edu.ifsp.arq.dmos5.mytasklist.model.Tarefa;
import br.edu.ifsp.arq.dmos5.mytasklist.repository.TarefasRepository;

public class TarefaViewModel extends AndroidViewModel {

    public static final String USUARIO_ID = "USUARIO_ID";

    public TarefasRepository tarefasRepository;

    public TarefaViewModel(@NonNull Application application) {
        super(application);
        tarefasRepository = new TarefasRepository(application);
    }

    public void createTarefa(Tarefa tarefa){
        tarefasRepository.createTarefa(tarefa);
    }

    public MutableLiveData<List<Tarefa>> getTarefasAtrasadasById() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        Optional<String> id = Optional.ofNullable(sharedPreferences.getString(USUARIO_ID, null));
        if(!id.isPresent()){
            return new MutableLiveData<>(null);
        }
        return tarefasRepository.getTarefasAtrasadasById(id.get());
    }

    public MutableLiveData<List<Tarefa>> getTarefasHojeById() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        Optional<String> id = Optional.ofNullable(sharedPreferences.getString(USUARIO_ID, null));
        if(!id.isPresent()){
            return new MutableLiveData<>(null);
        }
        return tarefasRepository.getTarefasHojeById(id.get());
    }

    public MutableLiveData<List<Tarefa>> getTarefasProximosDiasById() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        Optional<String> id = Optional.ofNullable(sharedPreferences.getString(USUARIO_ID, null));
        if(!id.isPresent()){
            return new MutableLiveData<>(null);
        }
        return tarefasRepository.getTarefasProximosDiasById(id.get());
    }

    public MutableLiveData<List<Tarefa>> getTarefasConcluidasById() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        Optional<String> id = Optional.ofNullable(sharedPreferences.getString(USUARIO_ID, null));
        if(!id.isPresent()){
            return new MutableLiveData<>(null);
        }
        return tarefasRepository.getTarefasConcluidasById(id.get());
    }

    public LiveData<List<Tarefa>> getTarefasByIdAndData(String dataEscolhida) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        Optional<String> id = Optional.ofNullable(sharedPreferences.getString(USUARIO_ID, null));
        if(!id.isPresent()){
            return new MutableLiveData<>(null);
        }
        return tarefasRepository.getTarefasByIdAndData(id.get(), dataEscolhida);
    }

    public void delete(Tarefa tarefa) {
        tarefasRepository.delete(tarefa);
    }

    public void update(Tarefa tarefa) {
        tarefasRepository.update(tarefa);
    }

    public LiveData<List<Tarefa>> atualizarTarefasAtrasadas() {
        return tarefasRepository.atualizarTarefasAtrasadas();
    }

    public void updateAtrasado(Tarefa tarefa) {
        tarefasRepository.updateAtrasado(tarefa);
    }

    public LiveData<List<Tarefa>> getTarefasAtrasadasCont() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        Optional<String> id = Optional.ofNullable(sharedPreferences.getString(USUARIO_ID, null));
        if(!id.isPresent()){
            return new MutableLiveData<>(null);
        }
        return tarefasRepository.getTarefasAtrasadasCont(id.get());
    }

    public LiveData<List<Tarefa>> getTarefasAndamentoCont() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        Optional<String> id = Optional.ofNullable(sharedPreferences.getString(USUARIO_ID, null));
        if(!id.isPresent()){
            return new MutableLiveData<>(null);
        }
        return tarefasRepository.getTarefasAndamentoCont(id.get());
    }

    public LiveData<List<Tarefa>> getTarefasConcluidasCont() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        Optional<String> id = Optional.ofNullable(sharedPreferences.getString(USUARIO_ID, null));
        if(!id.isPresent()){
            return new MutableLiveData<>(null);
        }
        return tarefasRepository.getTarefasConcluidasCont(id.get());
    }

    public LiveData<List<Tarefa>> getTarefasByIdAndCaregoria(String categoriaNome) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        Optional<String> id = Optional.ofNullable(sharedPreferences.getString(USUARIO_ID, null));
        if(!id.isPresent()){
            return new MutableLiveData<>(null);
        }
        return tarefasRepository.getTarefasByIdAndCaregoria(id.get(), categoriaNome);
    }

    public void updateFavoritado(Tarefa tarefa) {
        tarefasRepository.updateFavoritado(tarefa);
    }
}

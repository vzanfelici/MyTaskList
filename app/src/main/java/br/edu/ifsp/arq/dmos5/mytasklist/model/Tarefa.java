package br.edu.ifsp.arq.dmos5.mytasklist.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "tarefa")
public class Tarefa implements Serializable {

    @NonNull
    @PrimaryKey
    private String id;

    private String usuarioId;
    private String descricao;
    private String data;
    private String hora;
    private boolean realizado = false;
    private boolean atrasado = false;
    private boolean favoritado = false;
    private String categoria;
    private static transient Timestamp dataHora;

    public Tarefa(String usuarioId, String descricao, String data, String hora, boolean realizado, boolean atrasado, boolean favoritado, String categoria) {
        this.id = UUID.randomUUID().toString();
        this.usuarioId = usuarioId;
        this.descricao = descricao;
        this.data = data;
        this.hora = hora;
        this.realizado = realizado;
        this.atrasado = atrasado;
        this.favoritado = favoritado;
        this.categoria = categoria;
        this.dataHora = converterDataHora(data, hora);
    }

    @Ignore
    public Tarefa(){
        this("","","", "",false, false, false, "");
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }

    public boolean isAtrasado() {
        return atrasado;
    }

    public void setAtrasado(boolean atrasado) {
        this.atrasado = atrasado;
    }

    public boolean isFavoritado() {
        return favoritado;
    }

    public void setFavoritado(boolean favoritado) {
        this.favoritado = favoritado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Timestamp getDataHora() {
        return dataHora;
    }

    public void setDataHora(Timestamp dataHora) {
        this.dataHora = converterDataHora(data, hora);
    }

    public Timestamp converterDataHora(String dataAux, String horaAux) {
        String dataVenc = dataAux;
        String horaVenc = horaAux;
        String dataHoraVenc = dataVenc+" "+horaVenc;

        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date;
        Timestamp timestamp = null;

        try {
            date = fmt.parse(dataHoraVenc);
            timestamp = new Timestamp(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarefa tarefa = (Tarefa) o;
        return id.equals(tarefa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

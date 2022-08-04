package br.edu.ifsp.arq.dmos5.mytasklist.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.UUID;

@Entity(tableName = "categoria")
public class Categoria implements Serializable {

    @NonNull
    @PrimaryKey
    private String id;

    private String usuarioId;
    private String nome;

    public Categoria(String usuarioId, String nome) {
        this.id = UUID.randomUUID().toString();
        this.usuarioId = usuarioId;
        this.nome = nome;
    }

    @Ignore
    public Categoria(){
        this("","");
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

package com.example.thomas.denuncie;

import java.io.Serializable;
import java.util.Date;

public class Denuncia implements Serializable {

    private Integer id;
    private String descricao;
    private Date data;
    private Double longitude;
    private Double latitude;
    private String uriMidia;
    private String usuario;
    private String categoria;

    public Denuncia() {
    }

    public Denuncia(Integer id, String descricao, Date data, Double longitude, Double latitude, String uriMidia, String usuario, String categoria) {
        this.id = id;
        this.descricao = descricao;
        this.data = data;
        this.longitude = longitude;
        this.latitude = latitude;
        this.uriMidia = uriMidia;
        this.usuario = usuario;
        this.categoria = categoria;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getUriMidia() {
        return uriMidia;
    }

    public void setUriMidia(String uriMidia) {
        this.uriMidia = uriMidia;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
package net.latinus.pushapp.Entidades;

import java.io.Serializable;

public class Guardados implements Serializable {

    private Integer idGuardado;

    private String tituloGuardado;
    private String contenidoGuardado;

    private String fechaGuardado;

    private String dataGuardado;
    private String imagenGuardado;
    private String urlGuardado;


    public Guardados() {
    }

    public Guardados(Integer idGuardado, String tituloGuardado, String contenidoGuardado, String fechaGuardado, String dataGuardado, String imagenGuardado, String urlGuardado) {
        this.idGuardado = idGuardado;
        this.tituloGuardado = tituloGuardado;
        this.contenidoGuardado = contenidoGuardado;
        this.fechaGuardado = fechaGuardado;
        this.dataGuardado = dataGuardado;
        this.imagenGuardado = imagenGuardado;
        this.urlGuardado = urlGuardado;
    }

    public Integer getIdGuardado() {
        return idGuardado;
    }

    public void setIdGuardado(Integer idGuardado) {
        this.idGuardado = idGuardado;
    }

    public String getTituloGuardado() {
        return tituloGuardado;
    }

    public void setTituloGuardado(String tituloGuardado) {
        this.tituloGuardado = tituloGuardado;
    }

    public String getContenidoGuardado() {
        return contenidoGuardado;
    }

    public void setContenidoGuardado(String contenidoGuardado) {
        this.contenidoGuardado = contenidoGuardado;
    }

    public String getFechaGuardado() {
        return fechaGuardado;
    }

    public void setFechaGuardado(String fechaGuardado) {
        this.fechaGuardado = fechaGuardado;
    }

    public String getDataGuardado() {
        return dataGuardado;
    }

    public void setDataGuardado(String dataGuardado) {
        this.dataGuardado = dataGuardado;
    }

    public String getImagenGuardado() {
        return imagenGuardado;
    }

    public void setImagenGuardado(String imagenGuardado) {
        this.imagenGuardado = imagenGuardado;
    }

    public String getUrlGuardado() {
        return urlGuardado;
    }

    public void setUrlGuardado(String urlGuardado) {
        this.urlGuardado = urlGuardado;
    }
}

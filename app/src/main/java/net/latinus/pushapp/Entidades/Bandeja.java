package net.latinus.pushapp.Entidades;

import java.io.Serializable;

public class Bandeja implements Serializable {

    private Integer idBandeja;

    private String tituloBandeja;
    private String contenidoBandeja;

    private String fechaBandeja;



    private String dataBandeja;
    private String imagenBandeja;
    private String urlBandeja;


    public Bandeja() {
    }

    public Bandeja(Integer idBandeja, String tituloBandeja, String contenidoBandeja, String fechaBandeja,  String dataBandeja, String imagenBandeja, String urlBandeja) {
        this.idBandeja = idBandeja;
        this.tituloBandeja = tituloBandeja;
        this.contenidoBandeja = contenidoBandeja;
        this.fechaBandeja = fechaBandeja;
        this.dataBandeja = dataBandeja;
        this.imagenBandeja = imagenBandeja;
        this.urlBandeja = urlBandeja;
    }

    public Integer getIdBandeja() {
        return idBandeja;
    }

    public void setIdBandeja(Integer idBandeja) {
        this.idBandeja = idBandeja;
    }

    public String getTituloBandeja() {
        return tituloBandeja;
    }

    public void setTituloBandeja(String tituloBandeja) {
        this.tituloBandeja = tituloBandeja;
    }

    public String getContenidoBandeja() {
        return contenidoBandeja;
    }

    public void setContenidoBandeja(String contenidoBandeja) {
        this.contenidoBandeja = contenidoBandeja;
    }

    public String getFechaBandeja() {
        return fechaBandeja;
    }

    public void setFechaBandeja(String fechaBandeja) {
        this.fechaBandeja = fechaBandeja;
    }

    public String getDataBandeja() {
        return dataBandeja;
    }

    public void setDataBandeja(String dataBandeja) {
        this.dataBandeja = dataBandeja;
    }

    public String getImagenBandeja() {
        return imagenBandeja;
    }

    public void setImagenBandeja(String imagenBandeja) {
        this.imagenBandeja = imagenBandeja;
    }

    public String getUrlBandeja() {
        return urlBandeja;
    }

    public void setUrlBandeja(String urlBandeja) {
        this.urlBandeja = urlBandeja;
    }
}

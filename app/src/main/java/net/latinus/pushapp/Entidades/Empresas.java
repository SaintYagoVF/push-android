package net.latinus.pushapp.Entidades;

public class Empresas {

    private Integer idEmpresa;

    private String nombreEmpresa;

    public Integer getId_tabEmpresa() {
        return id_tabEmpresa;
    }

    public void setId_tabEmpresa(Integer id_tabEmpresa) {
        this.id_tabEmpresa = id_tabEmpresa;
    }

    private Integer id_tabEmpresa;

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }


    public String getLogoEmpresa() {
        return logoEmpresa;
    }

    public void setLogoEmpresa(String logoEmpresa) {
        this.logoEmpresa = logoEmpresa;
    }

    private String logoEmpresa;

    public Empresas(){

    }

    public Empresas(Integer idEmpresa, Integer id_tabEmpresa, String nombreEmpresa, String logoEmpresa) {

        this.idEmpresa = idEmpresa;
        this.id_tabEmpresa=id_tabEmpresa;
        this.nombreEmpresa = nombreEmpresa;
        this.logoEmpresa = logoEmpresa;
    }
}

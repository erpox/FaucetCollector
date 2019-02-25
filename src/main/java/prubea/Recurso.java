/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.prubea;

/**
 *
 * @author edupe
 */
public class Recurso {

    private String libro;
    private String estadoRecurso;
    private String fecha;
    private String estadoProcesal;
    private String recurso;
    private String corte;

    public Recurso(String libro, String estadoRecurso, String fecha, String estadoProcesal, String recurso, String corte) {
        this.libro = libro;
        this.estadoRecurso = estadoRecurso;
        this.fecha = fecha;
        this.estadoProcesal = estadoProcesal;
        this.recurso = recurso;
        this.corte = corte;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getEstadoRecurso() {
        return estadoRecurso;
    }

    public void setEstadoRecurso(String estadoRecurso) {
        this.estadoRecurso = estadoRecurso;
    }

    public String getEstadoProcesal() {
        return estadoProcesal;
    }

    public void setEstadoProcesal(String estadoProcesal) {
        this.estadoProcesal = estadoProcesal;
    }

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }

    public String getCorte() {
        return corte;
    }

    public void setCorte(String corte) {
        this.corte = corte;
    }

}

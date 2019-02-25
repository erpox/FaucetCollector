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
public class Roles {
    private int primeKey;
    private String nroIngreso;

    public Roles(int primeKey, String nroIngreso) {
        this.primeKey = primeKey;
        this.nroIngreso = nroIngreso;
    }

    public int getPrimeKey() {
        return primeKey;
    }

    public void setPrimeKey(int primeKey) {
        this.primeKey = primeKey;
    }

    public String getNroIngreso() {
        return nroIngreso;
    }

    public void setNroIngreso(String nroIngreso) {
        this.nroIngreso = nroIngreso;
    }

    @Override
    public String toString() {
        return "Roles{" + "primeKey=" + primeKey + ", nroIngreso=" + nroIngreso + '}';
    }
    
    
}

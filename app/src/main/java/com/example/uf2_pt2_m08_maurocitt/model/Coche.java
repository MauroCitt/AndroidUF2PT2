package com.example.uf2_pt2_m08_maurocitt.model;

public class Coche {
    private String nombre;
    private String apellido;
    private String telefono;
    private String marca;
    private String modelo;
    private String matricula;

    public Coche() {
    }

    public Coche(String nombre, String apellido, String telefono, String marca, String modelo, String matricula) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}

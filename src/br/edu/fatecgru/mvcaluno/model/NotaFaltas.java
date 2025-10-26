package br.edu.fatecgru.mvcaluno.model;

public class NotaFaltas {
    private double nota;
    private int faltas;
    private String status;

    // Construtor padrão
    public NotaFaltas() {}

    // Construtor completo (usado no DAO)
    public NotaFaltas(double nota, int faltas, String status) {
        this.nota = nota;
        this.faltas = faltas;
        this.status = status;
    }

    // Getters e Setters
    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
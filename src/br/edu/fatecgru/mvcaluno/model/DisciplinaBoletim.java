package br.edu.fatecgru.mvcaluno.model;

public class DisciplinaBoletim {
    private String nomeDisciplina;
    private double nota;
    private int faltas;
    private String status;
    private String semestreAtual;

    // Construtor padrão
    public DisciplinaBoletim() {}

    // Construtor completo (usado no DAO)
    public DisciplinaBoletim(String nomeDisciplina, double nota, int faltas, String status, String semestreAtual) {
        this.nomeDisciplina = nomeDisciplina;
        this.nota = nota;
        this.faltas = faltas;
        this.status = status;
        this.semestreAtual = semestreAtual;
    }

    // Getters e Setters
    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

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

    public String getSemestreAtual() {
        return semestreAtual;
    }

    public void setSemestreAtual(String semestreAtual) {
        this.semestreAtual = semestreAtual;
    }
}
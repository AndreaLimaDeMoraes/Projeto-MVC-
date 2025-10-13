package br.edu.fatecgru.mvcaluno.model;

public class DisciplinaBoletim {
    private String nomeDisciplina;
    private double nota;
    private int faltas;
    private String semestreAtual;

    public DisciplinaBoletim(String nomeDisciplina, double nota, int faltas, String semestreAtual) {
        this.nomeDisciplina = nomeDisciplina;
        this.nota = nota;
        this.faltas = faltas;
        this.semestreAtual = semestreAtual;
    }

    // Getters
    public String getNomeDisciplina() { return nomeDisciplina; }
    public double getNota() { return nota; }
    public int getFaltas() { return faltas; }
    public String getSemestreAtual() { return semestreAtual; }
}
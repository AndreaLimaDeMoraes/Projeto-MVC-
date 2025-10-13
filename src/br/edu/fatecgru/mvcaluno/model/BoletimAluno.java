package br.edu.fatecgru.mvcaluno.model;

public class BoletimAluno {
    private int idAluno;
    private String ra;
    private String nome;
    private String nomeCurso;

    public BoletimAluno(int idAluno, String ra, String nome, String nomeCurso) {
        this.idAluno = idAluno;
        this.ra = ra;
        this.nome = nome;
        this.nomeCurso = nomeCurso;
    }

    // Getters
    public int getIdAluno() { return idAluno; }
    public String getRa() { return ra; }
    public String getNome() { return nome; }
    public String getNomeCurso() { return nomeCurso; }
}
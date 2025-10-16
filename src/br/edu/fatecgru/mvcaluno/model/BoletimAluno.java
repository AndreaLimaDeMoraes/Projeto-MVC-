package br.edu.fatecgru.mvcaluno.model;

public class BoletimAluno {
    private int idAluno;
    private String ra;
    private String nome;
    private String nomeCurso;
    private String campus; 

    public BoletimAluno() {}
    
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
    public String getCampus() { return campus; }

    //Setters 
    public void setIdAluno(int idAluno) { this.idAluno = idAluno; }
    public void setRa(String ra) { this.ra = ra; }
    public void setNome(String nome) { this.nome = nome; }
    public void setNomeCurso(String nomeCurso) { this.nomeCurso = nomeCurso; }
    public void setCampus(String campus) { this.campus = campus; }
}
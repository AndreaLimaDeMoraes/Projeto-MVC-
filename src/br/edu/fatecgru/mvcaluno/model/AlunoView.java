package br.edu.fatecgru.mvcaluno.model;

public class AlunoView extends Aluno { 
    
    private String nomeCurso;
    private String campus;
    private String semestreAtual;
    private int idCurso;

    public AlunoView(int idAluno, String ra, String nome, String dataNascimento, String cpf, String email, 
            String endereco, String municipio, String uf, String celular, boolean ativo, 
            String nomeCurso, String campus, String semestreAtual, int idCurso) { 
        
        super(idAluno, ra, nome, dataNascimento, cpf, email, endereco, municipio, uf, celular, ativo);
        this.nomeCurso = nomeCurso;
        this.campus = campus;
        this.semestreAtual = semestreAtual;
        this.idCurso = idCurso; 
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }
    
    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }
    
    public String getSemestreAtual() {
        return semestreAtual;
    }

    public void setSemestreAtual(String semestreAtual) {
        this.semestreAtual = semestreAtual;
    }
    public int getIdCurso() { 
        return idCurso;
    }

    public void setIdCurso(int idCurso) { 
        this.idCurso = idCurso;
    }
}
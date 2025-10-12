package br.edu.fatecgru.mvcaluno.model;

// Esta classe herda todos os campos de Aluno e adiciona o nome do curso
public class AlunoView extends Aluno { 
    
    private String nomeCurso;
    private String campus;

    public AlunoView(int idAluno, String ra, String nome, String dataNascimento, String cpf, String email, 
            String endereco, String municipio, String uf, String celular, boolean ativo, String nomeCurso, String campus) {
        
        super(idAluno, ra, nome, dataNascimento, cpf, email, endereco, municipio, uf, celular, ativo);
        this.nomeCurso = nomeCurso;
        this.campus = campus;
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
}
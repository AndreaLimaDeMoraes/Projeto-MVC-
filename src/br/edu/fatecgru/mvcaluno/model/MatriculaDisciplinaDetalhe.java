package br.edu.fatecgru.mvcaluno.model;

public class MatriculaDisciplinaDetalhe extends MatriculaDisciplina {
    private String nomeDisciplina;

    // Construtor padrão e super()
    public MatriculaDisciplinaDetalhe() {
        super();
    }
    
    // Construtor completo
    public MatriculaDisciplinaDetalhe(int idMatriculaDisciplina, int idMatricula, int idDisciplina,
                               String semestreCursado, int faltas, double nota,
                               String status, boolean ativo, String nomeDisciplina) {
        super(idMatriculaDisciplina, idMatricula, idDisciplina, semestreCursado, faltas, nota, status, ativo);
        this.nomeDisciplina = nomeDisciplina;
    }


    // Getters e Setters
    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }
}
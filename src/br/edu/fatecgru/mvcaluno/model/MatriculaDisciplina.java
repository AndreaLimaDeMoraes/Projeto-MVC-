package br.edu.fatecgru.mvcaluno.model;

public class MatriculaDisciplina {
    private int idMatriculaDisciplina;
    private int idMatricula;
    private int idDisciplina;
    private String semestreCursado;
    private int faltas;
    private double nota;
    private String status; //"Cursando", "Aprovado", "Reprovado"
    private boolean ativo;

    // ===============================================
    // Construtores
    // ===============================================

    /**
     * Construtor padrão (sem parâmetros).
     */
    public MatriculaDisciplina() {}

    public MatriculaDisciplina(int idMatriculaDisciplina, int idMatricula, int idDisciplina, String semestreCursado, int faltas, double nota, String status, boolean ativo) {
		this.idMatriculaDisciplina = idMatriculaDisciplina;
		this.idMatricula = idMatricula;
		this.idDisciplina = idDisciplina;
		this.semestreCursado = semestreCursado; 
		setFaltas(faltas); // Usa setter para validação
		setNota(nota); // Usa setter para validação
		this.status = status;
		this.ativo = ativo;
		}

    // ===============================================
    // Getters e Setters
    // ===============================================

    public int getIdMatriculaDisciplina() {
        return idMatriculaDisciplina;
    }

    public void setIdMatriculaDisciplina(int idMatriculaDisciplina) {
        this.idMatriculaDisciplina = idMatriculaDisciplina;
    }

    public int getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(int idMatricula) {
        this.idMatricula = idMatricula;
    }

    public int getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(int idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getSemestreCursado() {
        return semestreCursado;
    }

    public void setSemestreCursado(String semestreCursado) {
        this.semestreCursado = semestreCursado;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        if (faltas < 0 || faltas > 60) {
            throw new IllegalArgumentException("Faltas deve estar entre 0 e 80.");
        }
        this.faltas = faltas;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        if (nota < 0 || nota > 10) {
            throw new IllegalArgumentException("Nota deve estar entre 0 e 10.");
        }
        this.nota = nota;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
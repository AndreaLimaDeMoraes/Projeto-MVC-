package br.edu.fatecgru.mvcaluno.model;

public class MatriculaDisciplina {
	private int idMatriculaDisciplina;
	private int idMatricula;
	private int faltas;
	private String semestreAtual;
	private double nota;
	private boolean ativo;

	public MatriculaDisciplina() {}

	public MatriculaDisciplina(int idMatriculaDisciplina, int idMatricula, int faltas, 
			String semestreAtual, double nota, boolean ativo) {
		this.idMatriculaDisciplina = idMatriculaDisciplina;
		this.idMatricula = idMatricula;
		this.faltas = faltas;
		this.semestreAtual = semestreAtual;
		this.nota = nota;
		this.ativo = ativo;
	}

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

	public int getFaltas() {
		return faltas;
	}

	public void setFaltas(int faltas) {
		this.faltas = faltas;
	}

	public String getSemestreAtual() {
		return semestreAtual;
	}

	public void setSemestreAtual(String semestreAtual) {
		this.semestreAtual = semestreAtual;
	}

	public double getNota() {
		return nota;
	}

	public void setNota(double nota) {
		this.nota = nota;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}

package br.edu.fatecgru.mvcaluno.model;

public class Matricula {
	private int idMatricula;
	private int idAluno;
	private int idCurso;
	private boolean ativo;

	public Matricula() {}

	public Matricula(int idMatricula, int idAluno, int idCurso, boolean ativo) {
		this.idMatricula = idMatricula;
		this.idAluno = idAluno;
		this.idCurso = idCurso;
		this.ativo = ativo;
	}

	public int getIdMatricula() {
		return idMatricula;
	}

	public void setIdMatricula(int idMatricula) {
		this.idMatricula = idMatricula;
	}

	public int getIdAluno() {
		return idAluno;
	}

	public void setIdAluno(int idAluno) {
		this.idAluno = idAluno;
	}

	public int getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}

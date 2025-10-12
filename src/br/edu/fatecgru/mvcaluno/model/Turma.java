package br.edu.fatecgru.mvcaluno.model;

public class Turma {
	private int idTurma;
	private int idCurso;
	private String semestreInicio;
	private boolean ativo;

	public Turma() {}

	public Turma(int idTurma, int idCurso, String semestreInicio, boolean ativo) {
		this.idTurma = idTurma;
		this.idCurso = idCurso;
		this.semestreInicio = semestreInicio;
		this.ativo = ativo;
	}

	public int getIdTurma() {
		return idTurma;
	}

	public void setIdTurma(int idTurma) {
		this.idTurma = idTurma;
	}

	public int getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}

	public String getSemestreInicio() {
		return semestreInicio;
	}

	public void setSemestreInicio(String semestreInicio) {
		this.semestreInicio = semestreInicio;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}

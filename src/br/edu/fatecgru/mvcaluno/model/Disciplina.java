package br.edu.fatecgru.mvcaluno.model;

public class Disciplina {
	private int idDisciplina;
	private int idCurso;
	private String nome;
	private String semestre;
	private boolean ativo;

	public Disciplina() {}

	public Disciplina(int idDisciplina, int idCurso, String nome, String semestre, boolean ativo) {
		this.idDisciplina = idDisciplina;
		this.idCurso = idCurso;
		this.nome = nome;
		this.semestre = semestre;
		this.ativo = ativo;
	}

	public int getIdDisciplina() {
		return idDisciplina;
	}

	public void setIdDisciplina(int idDisciplina) {
		this.idDisciplina = idDisciplina;
	}

	public int getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}

package br.edu.fatecgru.mvcaluno.model;

public class Curso {
	private int idCurso;
	private String nome;
	private String campus;
	private String periodo;
	private int duracao;
	private boolean ativo;

	public Curso() {}

	public Curso(int idCurso, String nome, String campus, String periodo, int duracao, boolean ativo) {
		this.idCurso = idCurso;
		this.nome = nome;
		this.campus = campus;
		this.periodo = periodo;
		this.duracao = duracao;
		this.ativo = ativo;
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

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public int getDuracao() {
		return duracao;
	}

	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}

package br.edu.fatecgru.mvcaluno.model;

/**
 * Classe de visualização (ViewModel) que estende o modelo base 'Aluno'
 * para incluir informações adicionais vindas de outras tabelas, como:
 * curso, campus e semestre.
 * 
 * Essa classe é ideal para consultas SQL com JOINs e para exibição em telas.
 */
public class AlunoView extends Aluno { 
    
    // Campos adicionais que não estão na tabela 'aluno' diretamente
    private String nomeCurso;
    private String campus;
    private String semestreAtual;
    private int idCurso;
    
    /**
     * Construtor padrão — chama o construtor de Aluno (classe pai).
     */
    public AlunoView() {
        super();
    }

    /**
     * Construtor completo — inicializa tanto os atributos herdados de Aluno
     * quanto os novos campos de curso/campus/semestre.
     */
    public AlunoView(int idAluno, String ra, String nome, String dataNascimento, String cpf, String email, 
            String endereco, String municipio, String uf, String celular, boolean ativo, 
            String nomeCurso, String campus, String semestreAtual, int idCurso) { 
        
        // Chama o construtor da superclasse (Aluno)
        super(idAluno, ra, nome, dataNascimento, cpf, email, endereco, municipio, uf, celular, ativo);
        
        // Inicializa os novos atributos
        this.nomeCurso = nomeCurso;
        this.campus = campus;
        this.semestreAtual = semestreAtual;
        this.idCurso = idCurso; 
    }

    // Getters e setters
    
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

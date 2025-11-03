package br.edu.fatecgru.mvcaluno.model;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabela simplificado para exibição de alunos em uma JTable.
 * 
 * Essa classe serve como "ponte" entre os dados (List<AlunoView>)
 * e a interface gráfica (JTable), informando quantas colunas e linhas
 * existem e o que deve aparecer em cada célula.
 */
public class AlunoTableModelSimplificado extends AbstractTableModel {

    // Número de versão da classe para evitar erro de serialização em runtime
    private static final long serialVersionUID = 1L;
    
    // Lista de objetos AlunoView que contém os dados da tabela
    private final List<AlunoView> dados; 
    
    // Cabeçalhos das colunas exibidas na JTable
    private final String[] colunas = {"RA", "Nome do Aluno", "Curso", "Campus", "Semestre"}; 

    /**
     * Construtor que recebe a lista de alunos a ser exibida.
     */
    public AlunoTableModelSimplificado(List<AlunoView> dados) {
        this.dados = dados;
    }

    /**
     * Retorna o nome da coluna conforme o índice (usado pela JTable).
     */
    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    /**
     * Retorna o número de linhas da tabela (quantos alunos há na lista).
     */
    @Override
    public int getRowCount() {
        return dados.size();
    }

    /**
     * Retorna o número de colunas (fixo conforme o array 'colunas').
     */
    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    /**
     * Retorna o valor a ser exibido em cada célula da tabela.
     * Cada coluna corresponde a um campo de AlunoView.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AlunoView aluno = dados.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return aluno.getRa(); 
            case 1: return aluno.getNome(); 
            case 2: return aluno.getNomeCurso(); 
            case 3: return aluno.getCampus(); 
            case 4: return aluno.getSemestreAtual();
            default: return null;
        }
    }
    
    /**
     * Retorna o objeto completo de um aluno, dado o índice da linha.
     * Útil quando o usuário clica em uma linha da JTable.
     */
    public AlunoView getAlunoAt(int rowIndex) {
        return dados.get(rowIndex);
    }
    
    /**
     * Define o tipo de dado que cada coluna contém (aqui, todas são String).
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
}

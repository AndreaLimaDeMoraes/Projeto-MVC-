package br.edu.fatecgru.mvcaluno.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class AlunoTableModelSimplificado extends AbstractTableModel {

    private static final long serialVersionUID = 1L; // Adicionado para resolver erro de serialização
    
    private final List<AlunoView> dados; 
    
    private final String[] colunas = {"RA", "Nome do Aluno", "Curso", "Campus", "Semestre"}; 

    public AlunoTableModelSimplificado(List<AlunoView> dados) {
        this.dados = dados;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public int getRowCount() {
        return dados.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

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
    
    public AlunoView getAlunoAt(int rowIndex) {
        return dados.get(rowIndex);
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
}
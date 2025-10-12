package br.edu.fatecgru.mvcaluno.model;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import br.edu.fatecgru.mvcaluno.model.Aluno;

public class AlunoTableModelSimplificado extends AbstractTableModel {

    // Mude a lista para AlunoView
    private final List<AlunoView> dados; 
    private final String[] colunas = {"ID", "RA", "Nome", "Curso", "Campus"}; 

    // Mude o construtor para receber List<AlunoView>
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
        
        if (columnIndex == 0) return aluno.getIdAluno();
        
        switch (columnIndex) {
            case 1: return aluno.getRa();
            case 2: return aluno.getNome();
            case 3: return aluno.getNomeCurso();
            case 4: return aluno.getCampus();
            default: return null;
        }
    }
    // Garante que o ID (coluna 0) seja tratado como Integer para a próxima tela
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) return Integer.class;
        return super.getColumnClass(columnIndex);
    }
}
package br.edu.fatecgru.mvcaluno.view;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import br.edu.fatecgru.mvcaluno.dao.CursoDAO;
import br.edu.fatecgru.mvcaluno.model.Curso;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

public class TelaDisciplina extends JPanel {

    private static final long serialVersionUID = 1L;
    private JComboBox<String> cmbCurso;
    private JTextField txtDisciplina;
    private JComboBox<String> cmbSemestre;

    public TelaDisciplina() {
        setLayout(null);
        setBackground(UIManager.getColor("Button.background"));

        // Painel principal — ocupa mais espaço, alinhado igual à tela de Cursos
        JPanel panel = new JPanel();
        panel.setBounds(20, 20, 893, 90);
        panel.setBorder(new TitledBorder(null, "Cadastrar Nova Disciplina", 
                                         TitledBorder.LEFT, TitledBorder.TOP, 
                                         new Font("Tahoma", Font.BOLD, 12), 
                                         new Color(54, 70, 78)));
        panel.setLayout(null);
        add(panel);

        // Label Curso
        JLabel lblCurso = new JLabel("Curso:");
        lblCurso.setBounds(10, 39, 50, 14);
        panel.add(lblCurso);

        // ComboBox Curso
        cmbCurso = new JComboBox<>();
        cmbCurso.setEditable(true);
        cmbCurso.setBounds(49, 34, 258, 24);
        panel.add(cmbCurso);

        // Label Nome
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(317, 38, 45, 14);
        panel.add(lblNome);

        // Campo Nome da disciplina
        txtDisciplina = new JTextField();
        txtDisciplina.setBounds(360, 34, 200, 24);
        panel.add(txtDisciplina);
        txtDisciplina.setColumns(10);

        // Label Semestre
        JLabel lblSemestre = new JLabel("Semestre:");
        lblSemestre.setBounds(580, 38, 70, 14);
        panel.add(lblSemestre);

        // ComboBox Semestre
        cmbSemestre = new JComboBox<>();
        cmbSemestre.setEditable(true);
        cmbSemestre.setBounds(650, 34, 80, 24);
        panel.add(cmbSemestre);

        // Botão Nova Disciplina
        JButton btnNovaDisciplina = new JButton("Nova Disciplina");
        btnNovaDisciplina.setBounds(750, 33, 133, 26);
        btnNovaDisciplina.setPreferredSize(new Dimension(120, 30));
        btnNovaDisciplina.setForeground(Color.WHITE);
        btnNovaDisciplina.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnNovaDisciplina.setFocusPainted(false);
        btnNovaDisciplina.setBackground(new Color(54, 70, 78));
        panel.add(btnNovaDisciplina);
        
        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBorder(new TitledBorder(null, "Disciplinas Cadastradas", 

                                                 TitledBorder.LEFT, TitledBorder.TOP, 

                                                 new Font("Tahoma", Font.BOLD, 12), 

                                                 new Color(54, 70, 78)));
        panel_1.setBounds(20, 121, 893, 209);
        add(panel_1);
        
        DefaultListModel<String> modelo = new DefaultListModel<>();
        JList<String> listDisciplina = new JList<>(modelo);
        listDisciplina.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listDisciplina.setFont(new Font("Tahoma", Font.PLAIN, 12));
        listDisciplina.setBackground(Color.WHITE);
        listDisciplina.setBounds(10, 24, 873, 174);
        panel_1.add(listDisciplina);

        // Carrega os cursos na ComboBox
        carregarCursosNome();

        // (Opcional) Preenche o ComboBox de semestre
        for (int i = 1; i <= 10; i++) {
            cmbSemestre.addItem(i + "");
        }
    }

    private void carregarCursosNome() {
        try {
            CursoDAO dao = new CursoDAO();
            List<Curso> listaCursos = dao.listarTodos();

            cmbCurso.removeAllItems();
            for (Curso curso : listaCursos) {
                cmbCurso.addItem(curso.getNome());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cursos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    

    
    
    
    
}

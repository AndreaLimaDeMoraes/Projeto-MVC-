package br.edu.fatecgru.mvcaluno.view;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import java.util.ArrayList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import br.edu.fatecgru.mvcaluno.dao.AlunoDAO;
import br.edu.fatecgru.mvcaluno.model.AlunoView;
import javax.swing.JSeparator;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.ImageIcon;

public class NotasFaltas extends JPanel {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel panelPesquisarAluno;
    private JLabel lblPesquisarAluno;
    private JTextField txtPesquisarAluno;
    private JPanel panelDados;
    private JLabel lblCurso;
    private JTextField txtNota;
    private JTextField txtFalta;
    private JLabel lblNome;
    private JLabel lblRA;
    private JLabel lblInformeADisciplina;
    private JComboBox<String> cmbDisciplina;
    private JPanel panelNotasFaltas;
    private JLabel lblNota;
    private JLabel lblFaltas;
    private JButton btnAtribuir;
    private JLabel lblSemestre;
    private JComboBox<String> cmbSemestre;

    private TelaPrincipal telaPrincipal;
    private int idAlunoSelecionado = -1;
    private int idMatriculaSelecionada = -1;
    private String semestreAtual = "2025/2"; // Padronizado com barra

    private final String HINT_TEXT = "Digite nome ou RA do aluno";
    private final Color HINT_COLOR = Color.LIGHT_GRAY;
    private final Color TEXT_COLOR = Color.BLACK;

    // Componentes para autocomplete
    private JPopupMenu popupSugestoes;
    private JList<AlunoView> listaSugestoes;
    private DefaultListModel<AlunoView> listModelSugestoes;
    private AlunoDAO alunoDAO;
    private JLabel lblNewLabel;
    private JLabel lblRa;
    private JLabel lblCurso_1;

    public NotasFaltas(TelaPrincipal p, int mode) {
        this.telaPrincipal = p;
        try {
            this.alunoDAO = new AlunoDAO();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        inicializarAutocomplete();
        setLayout(null);
        setBorder(new EmptyBorder(5, 5, 5, 5));

        panelPesquisarAluno = new JPanel();
        panelPesquisarAluno.setBounds(10, 11, 920, 73);
        add(panelPesquisarAluno);
        panelPesquisarAluno.setLayout(null);

        lblPesquisarAluno = new JLabel("Pesquisar aluno:");
        lblPesquisarAluno.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblPesquisarAluno.setBounds(18, 21, 138, 24);
        panelPesquisarAluno.add(lblPesquisarAluno);

        txtPesquisarAluno = new JTextField();
        txtPesquisarAluno.setFont(new Font("Tahoma", Font.PLAIN, 17));
        txtPesquisarAluno.setBounds(148, 20, 414, 27);
        panelPesquisarAluno.add(txtPesquisarAluno);
        txtPesquisarAluno.setColumns(10);

        // Configurar hint
        txtPesquisarAluno.setText(HINT_TEXT);
        txtPesquisarAluno.setForeground(HINT_COLOR);

        txtPesquisarAluno.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtPesquisarAluno.getText().equals(HINT_TEXT)) {
                    txtPesquisarAluno.setText("");
                    txtPesquisarAluno.setForeground(TEXT_COLOR);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent() != null && e.getOppositeComponent().getParent() == popupSugestoes) {
                    return;
                }
                if (txtPesquisarAluno.getText().isEmpty()) {
                    txtPesquisarAluno.setText(HINT_TEXT);
                    txtPesquisarAluno.setForeground(HINT_COLOR);
                }
            }
        });

        // Adicionar DocumentListener para busca em tempo real
        txtPesquisarAluno.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarSugestoes();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarSugestoes();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarSugestoes();
            }
        });

        panelDados = new JPanel();
        panelDados.setBorder(new TitledBorder(null, "Dados do Aluno", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelDados.setLayout(null);
        panelDados.setBounds(10, 86, 920, 159);
        add(panelDados);
        panelDados.setVisible(false); // Oculto inicialmente

        lblCurso = new JLabel("Carregando...");
        lblCurso.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblCurso.setBounds(565, 33, 345, 24);
        panelDados.add(lblCurso);

        lblNome = new JLabel("Carregando...");
        lblNome.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNome.setBounds(63, 33, 194, 24);
        panelDados.add(lblNome);

        lblRA = new JLabel("Carregando...");
        lblRA.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblRA.setBounds(296, 33, 211, 24);
        panelDados.add(lblRA);

        lblInformeADisciplina = new JLabel("Informe a disciplina:");
        lblInformeADisciplina.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblInformeADisciplina.setBounds(10, 118, 169, 24);
        panelDados.add(lblInformeADisciplina);

        cmbDisciplina = new JComboBox<>();
        cmbDisciplina.setFont(new Font("Tahoma", Font.PLAIN, 15));
        cmbDisciplina.setBounds(177, 119, 430, 28);
        panelDados.add(cmbDisciplina);

        lblSemestre = new JLabel("Semestre:");
        lblSemestre.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblSemestre.setBounds(10, 74, 85, 24);
        panelDados.add(lblSemestre);

        cmbSemestre = new JComboBox<>();
        cmbSemestre.setFont(new Font("Tahoma", Font.PLAIN, 15));
        cmbSemestre.setBounds(84, 77, 95, 22);
        panelDados.add(cmbSemestre);
        
        lblNewLabel = new JLabel("Nome:");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblNewLabel.setBounds(10, 36, 56, 19);
        panelDados.add(lblNewLabel);
        
        lblRa = new JLabel("RA:");
        lblRa.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblRa.setBounds(263, 36, 32, 19);
        panelDados.add(lblRa);
        
        lblCurso_1 = new JLabel("Curso:");
        lblCurso_1.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblCurso_1.setBounds(512, 35, 48, 19);
        panelDados.add(lblCurso_1);

        // Listener para cmbSemestre
        cmbSemestre.addActionListener(e -> {
            String semestreSelecionado = (String) cmbSemestre.getSelectedItem();
            popularDisciplinas(semestreSelecionado);
        });

        // Listener para cmbDisciplina
        cmbDisciplina.addActionListener(e -> {
            String disciplinaSelecionada = (String) cmbDisciplina.getSelectedItem();
            if (disciplinaSelecionada != null && !disciplinaSelecionada.equals("Selecione uma disciplina")) {
                int idDisciplina = extrairIdDisciplina(disciplinaSelecionada);
                carregarNotaFaltas(idDisciplina, (String) cmbSemestre.getSelectedItem());
                panelNotasFaltas.setVisible(true);
            } else {
                panelNotasFaltas.setVisible(false);
            }
        });

        panelNotasFaltas = new JPanel();
        panelNotasFaltas.setBorder(new TitledBorder(null, "Notas e Faltas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelNotasFaltas.setBounds(10, 247, 920, 85);
        add(panelNotasFaltas);
        panelNotasFaltas.setVisible(false); // Oculto inicialmente
        panelNotasFaltas.setLayout(null);

        lblNota = new JLabel("Nota:");
        lblNota.setBounds(33, 33, 47, 24);
        lblNota.setFont(new Font("Tahoma", Font.PLAIN, 17));
        panelNotasFaltas.add(lblNota);

        txtNota = new JTextField();
        txtNota.setBounds(79, 33, 84, 25);
        panelNotasFaltas.add(txtNota);
        txtNota.setFont(new Font("Tahoma", Font.PLAIN, 17));
        txtNota.setColumns(10);

        lblFaltas = new JLabel("Faltas:");
        lblFaltas.setBounds(203, 33, 52, 24);
        lblFaltas.setFont(new Font("Tahoma", Font.PLAIN, 17));
        panelNotasFaltas.add(lblFaltas);

        txtFalta = new JTextField();
        txtFalta.setBounds(258, 33, 84, 25);
        panelNotasFaltas.add(txtFalta);
        txtFalta.setFont(new Font("Tahoma", Font.PLAIN, 17));
        txtFalta.setColumns(10);

        btnAtribuir = new JButton("    Atribuir");
        btnAtribuir.setBounds(403, 22, 148, 45);
        btnAtribuir.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnAtribuir.setForeground(Color.black);
        btnAtribuir.setContentAreaFilled(false); 
        btnAtribuir.setFocusPainted(false);
        btnAtribuir.setIcon(new ImageIcon(NotasFaltas.class.getResource("/Resources/imagens/salve-.png")));
        btnAtribuir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarNotaFaltas();
            }
        });
        panelNotasFaltas.add(btnAtribuir);
    }

    private void inicializarAutocomplete() {
        popupSugestoes = new JPopupMenu();
        popupSugestoes.setFocusable(false);

        listModelSugestoes = new DefaultListModel<>();
        listaSugestoes = new JList<>(listModelSugestoes);

        listaSugestoes.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof AlunoView) {
                    AlunoView aluno = (AlunoView) value;
                    setText(aluno.getNome() + " (RA: " + aluno.getRa() + ")");
                }
                return this;
            }
        });

        JScrollPane scrollPaneSugestoes = new JScrollPane(listaSugestoes);
        popupSugestoes.add(scrollPaneSugestoes);

        listaSugestoes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selecionarAlunoSugerido();
                }
            }
        });
    }

    private void atualizarSugestoes() {
        String textoDigitado = txtPesquisarAluno.getText().trim();

        if (textoDigitado.isEmpty() || textoDigitado.equals(HINT_TEXT)) {
            popupSugestoes.setVisible(false);
            return;
        }

        try {
            List<AlunoView> alunosEncontrados = alunoDAO.listarPorFiltro(textoDigitado);
            listModelSugestoes.clear();
            if (!alunosEncontrados.isEmpty()) {
                for (AlunoView aluno : alunosEncontrados) {
                    listModelSugestoes.addElement(aluno);
                }
                popupSugestoes.show(txtPesquisarAluno, 0, txtPesquisarAluno.getHeight());
                popupSugestoes.setPopupSize(txtPesquisarAluno.getWidth(), 150);
            } else {
                popupSugestoes.setVisible(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            popupSugestoes.setVisible(false);
        }
    }

    private void selecionarAlunoSugerido() {
        AlunoView alunoSelecionado = listaSugestoes.getSelectedValue();
        if (alunoSelecionado != null) {
            idAlunoSelecionado = alunoSelecionado.getIdAluno();
            txtPesquisarAluno.setText(alunoSelecionado.getNome());
            lblNome.setText(alunoSelecionado.getNome());
            lblRA.setText(alunoSelecionado.getRa());
            lblCurso.setText(alunoSelecionado.getNomeCurso());
            try {
                idMatriculaSelecionada = alunoDAO.buscarIdMatricula(idAlunoSelecionado);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao buscar matrícula: " + e.getMessage());
                return;
            }
            panelDados.setVisible(true);
            popularSemestres();
            cmbSemestre.setSelectedItem(semestreAtual);
            popularDisciplinas(semestreAtual);
            popupSugestoes.setVisible(false);
        }
    }

    private void popularSemestres() {
        cmbSemestre.removeAllItems();
        cmbSemestre.addItem("2023/1");
        cmbSemestre.addItem("2023/2");
        cmbSemestre.addItem("2024/1");
        cmbSemestre.addItem("2024/2");
        cmbSemestre.addItem("2025/1");
        cmbSemestre.addItem("2025/2");
    }

    private void popularDisciplinas(String semestre) {
        cmbDisciplina.removeAllItems();
        cmbDisciplina.addItem("Selecione uma disciplina"); // Opção padrão
        if (semestre == null || semestre.isEmpty()) {
            return; // Não faz nada se semestre for null ou vazio
        }
        try {
            int idCurso = obterIdCursoDaMatricula(idMatriculaSelecionada);
            if (idCurso == -1) return;
            int semestreInt = Integer.parseInt(semestre.split("/")[1]);
            List<String> disciplinas = alunoDAO.listarDisciplinasPorCursoESemestre(idCurso, semestreInt);
            for (String disc : disciplinas) {
                cmbDisciplina.addItem(disc);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar disciplinas: " + e.getMessage());
        }
    }

    private int obterIdCursoDaMatricula(int idMatricula) {
        try {
            java.sql.Connection conn = br.edu.fatecgru.mvcaluno.util.ConnectionFactory.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement("SELECT idCurso FROM matricula WHERE idMatricula = ?");
            ps.setInt(1, idMatricula);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("idCurso");
                br.edu.fatecgru.mvcaluno.util.ConnectionFactory.closeConnection(conn, ps, rs);
                return id;
            }
            br.edu.fatecgru.mvcaluno.util.ConnectionFactory.closeConnection(conn, ps, rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int extrairIdDisciplina(String item) {
        return Integer.parseInt(item.split(" - ")[0]);
    }

    private void carregarNotaFaltas(int idDisciplina, String semestre) {
        try {
            double[] resultado = alunoDAO.buscarNotaFaltas(idMatriculaSelecionada, idDisciplina, semestre);
            if (resultado != null) {
                txtNota.setText(String.valueOf(resultado[0]));
                txtFalta.setText(String.valueOf((int) resultado[1]));
            } else {
                txtNota.setText("");
                txtFalta.setText("");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar nota e faltas: " + e.getMessage());
        }
    }

    private void salvarNotaFaltas() {
        String notaStr = txtNota.getText();
        String faltasStr = txtFalta.getText();
        if (notaStr.isEmpty() || faltasStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha nota e faltas.");
            return;
        }
        try {
            double nota = Double.parseDouble(notaStr);
            int faltas = Integer.parseInt(faltasStr);
            
            if (nota < 0 || nota > 10) {
                JOptionPane.showMessageDialog(this, "Nota deve estar entre 0 e 10.");
                return;
            }
            if (faltas < 0 || faltas > 80) {
                JOptionPane.showMessageDialog(this, "Faltas devem estar entre 0 e 80.");
                return;
            }
            
            String semestre = (String) cmbSemestre.getSelectedItem();
            int idDisciplina = extrairIdDisciplina((String) cmbDisciplina.getSelectedItem());
            alunoDAO.salvarNotaFaltas(idMatriculaSelecionada, idDisciplina, semestre, nota, faltas);
            JOptionPane.showMessageDialog(this, "Salvo com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nota ou faltas inválidas (devem ser números).");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }

}

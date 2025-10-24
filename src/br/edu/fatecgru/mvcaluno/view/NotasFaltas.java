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

/**
 * View para atribuição e consulta de Notas e Faltas dos alunos.
 * Implementa um recurso de AutoComplete na pesquisa de alunos.
 */
public class NotasFaltas extends JPanel {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel panelPesquisarAluno;
    private JLabel lblPesquisarAluno;
    private JTextField txtPesquisarAluno; // Campo de pesquisa com AutoComplete
    private JPanel panelDados; // Painel que exibe Nome, RA e Curso do aluno selecionado
    private JLabel lblCurso;
    private JTextField txtNota;
    private JTextField txtFalta;
    private JLabel lblNome;
    private JLabel lblRA;
    private JLabel lblInformeADisciplina;
    private JComboBox<String> cmbDisciplina; // Dropdown para selecionar a disciplina
    private JPanel panelNotasFaltas; // Painel para inserir Nota e Falta
    private JLabel lblNota;
    private JLabel lblFaltas;
    private JButton btnAtribuir; // Botão para salvar/atualizar Notas e Faltas
    private JLabel lblSemestre;
    private JComboBox<String> cmbSemestre; // Dropdown para selecionar o semestre

    private TelaPrincipal telaPrincipal;
    private int idAlunoSelecionado = -1; // ID do aluno encontrado pelo AutoComplete
    private int idMatriculaSelecionada = -1; // ID da matrícula (usada para vincular a nota/falta)
    private String semestreAtual = "2025/2"; // Semestre padrão para preenchimento inicial

    private final String HINT_TEXT = "Digite nome ou RA do aluno";
    private final Color HINT_COLOR = Color.LIGHT_GRAY;
    private final Color TEXT_COLOR = Color.BLACK;

    // Componentes para autocomplete (JPopupMenu exibe a lista, JList contém os itens)
    private JPopupMenu popupSugestoes;
    private JList<AlunoView> listaSugestoes;
    private DefaultListModel<AlunoView> listModelSugestoes;
    private AlunoDAO alunoDAO; // Objeto DAO para acesso ao banco de dados (busca de aluno e notas)
    private JLabel lblNewLabel;
    private JLabel lblRa;
    private JLabel lblCurso_1;

    /**
     * Construtor da View NotasFaltas.
     * @param p A referência da TelaPrincipal (Frame pai).
     * @param mode Modo de operação (não usado atualmente, mas mantido para padrão).
     */
    public NotasFaltas(TelaPrincipal p, int mode) {
        this.telaPrincipal = p;
        try {
            // Instancia a DAO para comunicação com o banco de dados.
            this.alunoDAO = new AlunoDAO();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        inicializarAutocomplete(); // Configura todos os componentes do AutoComplete
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

        // Adicionar FocusListener para gerenciar o texto de Hint (dica)
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
                // Lógica para evitar que o hint apareça se o foco for para a lista de sugestões
                if (e.getOppositeComponent() != null && e.getOppositeComponent().getParent() == popupSugestoes) {
                    return;
                }
                if (txtPesquisarAluno.getText().isEmpty()) {
                    txtPesquisarAluno.setText(HINT_TEXT);
                    txtPesquisarAluno.setForeground(HINT_COLOR);
                }
            }
        });

        // Adicionar DocumentListener para busca em tempo real (AutoUpdate)
        txtPesquisarAluno.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarSugestoes(); // Chama a função que busca e exibe o popup
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
        panelDados.setVisible(false); // Painel de dados do aluno oculto até que um aluno seja selecionado

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
            // Ao mudar o semestre, recarrega as disciplinas disponíveis
            String semestreSelecionado = (String) cmbSemestre.getSelectedItem();
            popularDisciplinas(semestreSelecionado);
        });

        // Listener para cmbDisciplina
        cmbDisciplina.addActionListener(e -> {
            // Ao selecionar uma disciplina, tenta carregar as notas/faltas existentes
            String disciplinaSelecionada = (String) cmbDisciplina.getSelectedItem();
            if (disciplinaSelecionada != null && !disciplinaSelecionada.equals("Selecione uma disciplina")) {
                int idDisciplina = extrairIdDisciplina(disciplinaSelecionada);
                carregarNotaFaltas(idDisciplina, (String) cmbSemestre.getSelectedItem());
                panelNotasFaltas.setVisible(true); // Exibe o painel de edição
            } else {
                panelNotasFaltas.setVisible(false); // Oculta se não houver disciplina selecionada
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
        // Ação de salvar nota/falta
        btnAtribuir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarNotaFaltas(); // Chama a função de persistência dos dados
            }
        });
        panelNotasFaltas.add(btnAtribuir);
    }

    /**
     * Configura os componentes necessários para o AutoComplete:
     * JPopupMenu (a janela flutuante), JList (a lista de sugestões) e o CellRenderer (formatação da lista).
     */
    private void inicializarAutocomplete() {
        popupSugestoes = new JPopupMenu();
        popupSugestoes.setFocusable(false); // Importante para não roubar o foco da caixa de texto

        listModelSugestoes = new DefaultListModel<>();
        listaSugestoes = new JList<>(listModelSugestoes);

        // Define como cada item da lista de sugestões será exibido (Nome + RA)
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

        // Adiciona listener para capturar o clique do mouse na lista de sugestões
        listaSugestoes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selecionarAlunoSugerido(); // Chama a função para preencher a tela com o aluno
                }
            }
        });
    }

    /**
     * Busca alunos no banco de dados com base no texto digitado (filtro)
     * e atualiza o JPopupMenu de sugestões em tempo real.
     */
    private void atualizarSugestoes() {
        String textoDigitado = txtPesquisarAluno.getText().trim();

        if (textoDigitado.isEmpty() || textoDigitado.equals(HINT_TEXT)) {
            popupSugestoes.setVisible(false);
            return;
        }

        try {
            // Chama a DAO para buscar alunos por nome ou RA
            List<AlunoView> alunosEncontrados = alunoDAO.listarPorFiltro(textoDigitado);
            listModelSugestoes.clear();
            if (!alunosEncontrados.isEmpty()) {
                for (AlunoView aluno : alunosEncontrados) {
                    listModelSugestoes.addElement(aluno);
                }
                // Exibe o popup e define seu tamanho
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

    /**
     * Processa o aluno selecionado na lista de sugestões:
     * 1. Preenche os dados do aluno (Nome, RA, Curso).
     * 2. Busca o ID da Matrícula.
     * 3. Exibe o painel de dados.
     * 4. Popula os ComboBoxes de Semestre e Disciplina.
     */
    private void selecionarAlunoSugerido() {
        AlunoView alunoSelecionado = listaSugestoes.getSelectedValue();
        if (alunoSelecionado != null) {
            idAlunoSelecionado = alunoSelecionado.getIdAluno();
            txtPesquisarAluno.setText(alunoSelecionado.getNome());
            lblNome.setText(alunoSelecionado.getNome());
            lblRA.setText(alunoSelecionado.getRa());
            lblCurso.setText(alunoSelecionado.getNomeCurso());
            try {
                // Busca o ID da matrícula do aluno (necessário para persistir a nota/falta)
                idMatriculaSelecionada = alunoDAO.buscarIdMatricula(idAlunoSelecionado);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao buscar matrícula: " + e.getMessage());
                return;
            }
            panelDados.setVisible(true); // Exibe o painel com Nome/RA/Curso
            popularSemestres(); // Popula o JComboBox de semestres
            cmbSemestre.setSelectedItem(semestreAtual); // Seta o semestre atual como padrão
            popularDisciplinas(semestreAtual); // Popula disciplinas do semestre atual
            popupSugestoes.setVisible(false); // Oculta a lista de sugestões
        }
    }

    /**
     * Popula o ComboBox de semestres com valores fixos.
     */
    private void popularSemestres() {
        cmbSemestre.removeAllItems();
        cmbSemestre.addItem("2023/1");
        cmbSemestre.addItem("2023/2");
        cmbSemestre.addItem("2024/1");
        cmbSemestre.addItem("2024/2");
        cmbSemestre.addItem("2025/1");
        cmbSemestre.addItem("2025/2");
    }

    /**
     * Popula o ComboBox de disciplinas com base no curso do aluno e no semestre selecionado.
     * @param semestre O semestre no formato "AAAA/N".
     */
    private void popularDisciplinas(String semestre) {
        cmbDisciplina.removeAllItems();
        cmbDisciplina.addItem("Selecione uma disciplina"); // Opção padrão
        if (semestre == null || semestre.isEmpty()) {
            return;
        }
        try {
            // Primeiro, obtém o ID do curso a partir da matrícula
            int idCurso = obterIdCursoDaMatricula(idMatriculaSelecionada);
            if (idCurso == -1) return;
            
            // Extrai o número do semestre (ex: 2025/2 -> 2)
            int semestreInt = Integer.parseInt(semestre.split("/")[1]);
            
            // Chama a DAO para listar as disciplinas do curso e semestre
            List<String> disciplinas = alunoDAO.listarDisciplinasPorCursoESemestre(idCurso, semestreInt);
            for (String disc : disciplinas) {
                cmbDisciplina.addItem(disc);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar disciplinas: " + e.getMessage());
        }
    }

    /**
     * Busca o ID do Curso associado a uma Matrícula.
     * Necessário para filtrar as disciplinas corretas.
     * (Método com conexão JDBC direta, simplificado para a View).
     * @param idMatricula O ID da matrícula do aluno.
     * @return O ID do curso, ou -1 em caso de erro.
     */
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

    /**
     * Extrai o ID da disciplina de uma string formatada no ComboBox.
     * O formato esperado é "ID - Nome da Disciplina".
     * @param item A string selecionada no JComboBox.
     * @return O ID numérico da disciplina.
     */
    private int extrairIdDisciplina(String item) {
        return Integer.parseInt(item.split(" - ")[0]);
    }

    /**
     * Carrega a nota e a falta existentes para a disciplina e semestre selecionados.
     * Se não existir, limpa os campos.
     */
    private void carregarNotaFaltas(int idDisciplina, String semestre) {
        try {
            // Chama a DAO para buscar no banco (retorna um array [nota, falta])
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

    /**
     * Salva ou atualiza a nota e falta para a disciplina e semestre selecionados.
     * Implementa validação básica (intervalo da nota e faltas).
     */
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
            
            // Validações de domínio
            if (nota < 0 || nota > 10) {
                JOptionPane.showMessageDialog(this, "Nota deve estar entre 0 e 10.");
                return;
            }
            if (faltas < 0 || faltas > 80) { // Exemplo de limite de faltas
                JOptionPane.showMessageDialog(this, "Faltas devem estar entre 0 e 80.");
                return;
            }
            
            String semestre = (String) cmbSemestre.getSelectedItem();
            int idDisciplina = extrairIdDisciplina((String) cmbDisciplina.getSelectedItem());
            
            // Chama a DAO para persistir os dados no banco
            alunoDAO.salvarNotaFaltas(idMatriculaSelecionada, idDisciplina, semestre, nota, faltas);
            JOptionPane.showMessageDialog(this, "Salvo com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nota ou faltas inválidas (devem ser números).");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }

}
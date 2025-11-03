package br.edu.fatecgru.mvcaluno.view;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;

import br.edu.fatecgru.mvcaluno.dao.AlunoDAO;
import br.edu.fatecgru.mvcaluno.dao.CursoDAO;
import br.edu.fatecgru.mvcaluno.model.AlunoTableModelSimplificado;
import br.edu.fatecgru.mvcaluno.model.AlunoView;

/**
 * Classe responsável por exibir a tela de listagem de alunos.
 * 
 * Possui filtros por nome/RA e por curso, além de permitir abrir o cadastro
 * (novo ou existente) ao clicar em um aluno.
 */
public class ListarAlunos extends JPanel {

    private static final long serialVersionUID = 1L;

    // --- Componentes de UI ---
    private JLabel lblBuscar;
    private JTextField txtBuscar;
    private JLabel lblCurso;
    private JComboBox<String> cmbCurso;
    private JTable tblListaAlunos;
    private JPanel panelFiltros;
    private JButton btnNovoAluno;

    // --- Referências e controle ---
    private JFrame framePai; // Tela principal (para trocar painéis)
    private int idAluno;     // Usado ao abrir tela de edição

    // --- Construtores ---
    public ListarAlunos(JFrame framePai) {
        this.framePai = framePai;
        setupLayout();
    }

    public ListarAlunos() {
        setupLayout();
    }

    /**
     * Método principal de configuração da tela.
     * Cria todos os componentes, define eventos e carrega dados iniciais.
     */
    private void setupLayout() {

        // --- Constantes para o campo de busca ---
        final String HINT_TEXT = "Informe nome ou RA do aluno";
        final Color HINT_COLOR = Color.LIGHT_GRAY;
        final Color TEXT_COLOR = Color.BLACK;

        setLayout(new BorderLayout(5, 5));

        // Painel de filtros superior
        panelFiltros = new JPanel(null);
        panelFiltros.setPreferredSize(new Dimension(950, 75));

        // Label "Buscar"
        lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblBuscar.setBounds(10, 33, 68, 35);
        panelFiltros.add(lblBuscar);

        // Campo de busca com hint
        txtBuscar = new JTextField(HINT_TEXT);
        txtBuscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txtBuscar.setBounds(66, 38, 325, 27);
        txtBuscar.setForeground(HINT_COLOR);
        panelFiltros.add(txtBuscar);

        // --- Placeholder (hint) control ---
        txtBuscar.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBuscar.getText().equals(HINT_TEXT)) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(TEXT_COLOR);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtBuscar.getText().isEmpty()) {
                    txtBuscar.setText(HINT_TEXT);
                    txtBuscar.setForeground(HINT_COLOR);
                }
            }
        });

        // --- Atualiza busca dinamicamente ---
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { realizarBusca(); }
            @Override
            public void removeUpdate(DocumentEvent e) { realizarBusca(); }
            @Override
            public void changedUpdate(DocumentEvent e) { realizarBusca(); }

            private void realizarBusca() {
                String textoBusca = txtBuscar.getText();

                if (textoBusca.equals(HINT_TEXT) || textoBusca.trim().isEmpty()) {
                    textoBusca = null;
                }

                String cursoSelecionado = (String) cmbCurso.getSelectedItem();

                if (cursoSelecionado == null || cursoSelecionado.equals("Todos os Cursos")) {
                    carregarTabelaAlunos(textoBusca);
                } else {
                    String[] dadosCurso = extrairNomeCursoECampus(cursoSelecionado);
                    carregarTabelaAlunosPorCursoECampusEFiltro(dadosCurso[0], dadosCurso[1], textoBusca);
                }
            }
        });

        // Label "Curso"
        lblCurso = new JLabel("Curso:");
        lblCurso.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblCurso.setBounds(415, 31, 54, 35);
        panelFiltros.add(lblCurso);

        // ComboBox de cursos
        cmbCurso = new JComboBox<>();
        cmbCurso.setBounds(466, 36, 274, 27);
        popularComboCursos();
        panelFiltros.add(cmbCurso);

        // Botão "Novo Aluno"
        btnNovoAluno = new JButton(" Novo aluno");
        btnNovoAluno.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/adicionar-usuario.png")));
        btnNovoAluno.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnNovoAluno.setForeground(Color.BLACK);
        btnNovoAluno.setHorizontalAlignment(SwingConstants.LEFT);
        btnNovoAluno.setBounds(762, 25, 167, 40);
        btnNovoAluno.setContentAreaFilled(false);
        btnNovoAluno.setFocusPainted(false);
        panelFiltros.add(btnNovoAluno);

        // --- Eventos de ação ---
        cmbCurso.addActionListener(e -> aplicarFiltroCurso());
        btnNovoAluno.addActionListener(e -> abrirTelaNovoAluno());

        // Adiciona painel superior
        add(panelFiltros, BorderLayout.NORTH);

        // Tabela de alunos
        tblListaAlunos = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblListaAlunos);
        add(scrollPane, BorderLayout.CENTER);

        // Carrega dados iniciais
        carregarTabelaAlunos(null);
        adicionarEventoCliqueTabela();
    }

    // ============================================================
    // MÉTODOS DE APOIO À BUSCA E FILTROS
    // ============================================================

    private void aplicarFiltroCurso() {
        String itemSelecionado = (String) cmbCurso.getSelectedItem();

        String textoBusca = txtBuscar.getText();
        final String HINT_TEXT = "Informe nome ou RA do aluno";

        if (textoBusca.equals(HINT_TEXT) || textoBusca.trim().isEmpty()) {
            textoBusca = null;
        }

        if (itemSelecionado == null || itemSelecionado.equals("Todos os Cursos")) {
            carregarTabelaAlunos(textoBusca);
        } else {
            String[] dadosCurso = extrairNomeCursoECampus(itemSelecionado);
            carregarTabelaAlunosPorCursoECampusEFiltro(dadosCurso[0], dadosCurso[1], textoBusca);
        }
    }

    private void popularComboCursos() {
        try {
            CursoDAO dao = new CursoDAO();
            List<String> listaCursosFormatada = dao.listarCursosParaCombo();
            cmbCurso.removeAllItems();

            for (String cursoFormatado : listaCursosFormatada) {
                cmbCurso.addItem(cursoFormatado);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar cursos: " + e.getMessage(),
                "Erro de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Divide a string do ComboBox em nome e campus.
     * Ex: "ADS (Guarulhos)" → ["ADS", "Guarulhos"]
     */
    private String[] extrairNomeCursoECampus(String itemSelecionado) {
        int indexParenteses = itemSelecionado.lastIndexOf(" (");
        int indexFechamento = itemSelecionado.lastIndexOf(")");

        if (indexParenteses != -1 && indexFechamento > indexParenteses) {
            String nomeCurso = itemSelecionado.substring(0, indexParenteses).trim();
            String campus = itemSelecionado.substring(indexParenteses + 2, indexFechamento).trim();
            return new String[]{nomeCurso, campus};
        }
        return new String[]{itemSelecionado.trim(), null};
    }

    // ============================================================
    // MÉTODOS DE CARREGAMENTO DE TABELA
    // ============================================================

    private void carregarTabelaAlunos(String filtro) {
        try {
            AlunoDAO dao = new AlunoDAO();
            List<AlunoView> listaAlunos = 
                (filtro == null || filtro.trim().isEmpty())
                ? dao.listarTodos()
                : dao.listarPorFiltro(filtro);

            tblListaAlunos.setModel(new AlunoTableModelSimplificado(listaAlunos));
            configurarVisualTabela();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar alunos: " + e.getMessage(),
                "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabelaAlunosPorCursoECampusEFiltro(String nomeCurso, String campus, String filtroTexto) {
        if (campus == null) {
            JOptionPane.showMessageDialog(this,
                "Erro: Não foi possível identificar o Campus selecionado.",
                "Erro de Filtro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            AlunoDAO dao = new AlunoDAO();
            List<AlunoView> listaAlunos = dao.listarPorCursoECampusEFiltro(nomeCurso, campus, filtroTexto);
            tblListaAlunos.setModel(new AlunoTableModelSimplificado(listaAlunos));
            configurarVisualTabela();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao filtrar alunos: " + e.getMessage(),
                "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Define tamanho, fontes e alinhamento das colunas.
     */
    private void configurarVisualTabela() {
        tblListaAlunos.setFont(new Font("Tahoma", Font.PLAIN, 15));
        tblListaAlunos.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 15));
        tblListaAlunos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(SwingConstants.LEFT);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        tblListaAlunos.getColumnModel().getColumn(0).setCellRenderer(left);
        tblListaAlunos.getColumnModel().getColumn(1).setCellRenderer(left);
        tblListaAlunos.getColumnModel().getColumn(2).setCellRenderer(left);
        tblListaAlunos.getColumnModel().getColumn(3).setCellRenderer(center);
        tblListaAlunos.getColumnModel().getColumn(4).setCellRenderer(center);

        tblListaAlunos.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblListaAlunos.getColumnModel().getColumn(1).setPreferredWidth(255);
        tblListaAlunos.getColumnModel().getColumn(2).setPreferredWidth(300);
        tblListaAlunos.getColumnModel().getColumn(3).setPreferredWidth(180);
        tblListaAlunos.getColumnModel().getColumn(4).setPreferredWidth(117);
    }

    // ============================================================
    // EVENTOS
    // ============================================================

    private void adicionarEventoCliqueTabela() {
        tblListaAlunos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Clique duplo abre a tela de edição do aluno
                if (e.getClickCount() == 2) {
                    int linhaSelecionada = tblListaAlunos.getSelectedRow();
                    if (linhaSelecionada != -1) {
                        AlunoTableModelSimplificado model = (AlunoTableModelSimplificado) tblListaAlunos.getModel();
                        AlunoView aluno = model.getAlunoAt(linhaSelecionada);
                        abrirTelaDadosPessoais(aluno.getIdAluno());
                    }
                }
            }
        });
    }

    private void abrirTelaNovoAluno() {
    	if (framePai instanceof TelaPrincipal) {
    	    TelaPrincipal telaPrincipal = (TelaPrincipal) framePai;

    	    DadosPessoais telaCadastro = new DadosPessoais(telaPrincipal, 0);
    	    telaPrincipal.trocarPainelConteudo(telaPrincipal.getPnlConteudoAluno(), telaCadastro);
    	    telaPrincipal.ativarBotaoMenuDadosPessoais();
    	}else {
            JOptionPane.showMessageDialog(this,
                "Clique em 'Novo Aluno'. O formulário de cadastro seria aberto aqui.",
                "Teste de Cadastro", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void abrirTelaDadosPessoais(int idAluno) {
    	if (framePai instanceof TelaPrincipal) {
    	    TelaPrincipal telaPrincipal = (TelaPrincipal) framePai;

    	    DadosPessoais telaEdicao = new DadosPessoais(telaPrincipal, idAluno);
    	    telaPrincipal.trocarPainelConteudo(telaPrincipal.getPnlConteudoAluno(), telaEdicao);
    	    telaPrincipal.ativarBotaoMenuDadosPessoais();
    	} else {
            JOptionPane.showMessageDialog(this,
                "Aluno ID " + idAluno + " selecionado. A tela de edição seria aberta aqui.",
                "Teste de Clique", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ============================================================
    // MÉTODO MAIN PARA TESTE INDEPENDENTE
    // ============================================================

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frameTeste = new JFrame("Teste Listar Alunos");
            frameTeste.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameTeste.setBounds(100, 100, 900, 600);
            frameTeste.setContentPane(new ListarAlunos());
            frameTeste.setVisible(true);
        });
    }

    /**
     * Método público para excluir aluno selecionado - chamado pelo menu
     */
    public void excluirAlunoSelecionado() {
        int linhaSelecionada = tblListaAlunos.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um aluno na lista para excluir!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Obtém o aluno selecionado da tabela
            AlunoTableModelSimplificado model = (AlunoTableModelSimplificado) tblListaAlunos.getModel();
            AlunoView aluno = model.getAlunoAt(linhaSelecionada);
            
            int idAluno = aluno.getIdAluno();
            String nomeAluno = aluno.getNome();
            String raAluno = aluno.getRa();
            
            System.out.println("Tentando excluir aluno - ID: " + idAluno + ", Nome: " + nomeAluno);
            
            // Confirmação antes de excluir
            int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o aluno?\n\n" +
                "Nome: " + nomeAluno + "\n" +
                "RA: " + raAluno + "\n" +
                "ID: " + idAluno,
                "Confirmar Exclusão", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("Usuário confirmou exclusão do aluno ID: " + idAluno);
                
                // Executa a exclusão no banco de dados
                AlunoDAO alunoDAO = new AlunoDAO();
                alunoDAO.excluir(idAluno);
                
                JOptionPane.showMessageDialog(this, 
                    "Aluno excluído com sucesso!\n" +
                    "Nome: " + nomeAluno + "\n" +
                    "RA: " + raAluno, 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                // Recarrega a tabela para refletir a exclusão
                recarregarTabela();
                
            } else {
                System.out.println("Usuário cancelou a exclusão");
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao excluir aluno: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Erro ao excluir aluno: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método auxiliar para recarregar a tabela após exclusão
     */
    private void recarregarTabela() {
        String textoBusca = txtBuscar.getText();
        final String HINT_TEXT = "Informe nome ou RA do aluno";
        
        // Verifica se o texto é apenas o placeholder
        if (textoBusca.equals(HINT_TEXT) || textoBusca.trim().isEmpty()) {
            textoBusca = null;
        }
        
        String cursoSelecionado = (String) cmbCurso.getSelectedItem();
        
        try {
            if (cursoSelecionado == null || cursoSelecionado.equals("Todos os Cursos")) {
                carregarTabelaAlunos(textoBusca);
            } else {
                String[] dadosCurso = extrairNomeCursoECampus(cursoSelecionado);
                carregarTabelaAlunosPorCursoECampusEFiltro(dadosCurso[0], dadosCurso[1], textoBusca);
            }
            System.out.println("Tabela recarregada após exclusão");
        } catch (Exception e) {
            System.out.println("Erro ao recarregar tabela: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Erro ao recarregar lista de alunos: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

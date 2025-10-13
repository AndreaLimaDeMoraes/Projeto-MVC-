package br.edu.fatecgru.mvcaluno.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import br.edu.fatecgru.mvcaluno.dao.AlunoDAO;
import br.edu.fatecgru.mvcaluno.model.AlunoView;
import br.edu.fatecgru.mvcaluno.model.BoletimAluno;
import br.edu.fatecgru.mvcaluno.model.DisciplinaBoletim;

public class Documentos extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel lblNewLabel;
    private JTextField txtBuscar;
    private JComboBox<String> cmbDocumento;  // Restaurado
    private JPanel panelFiltros;
    private JPanel panelWrapper;  // Novo: Wrapper para centralizar o conteúdo
    private JPanel panelDocumento;  // Painel interno para o documento
    
    // Variáveis para a funcionalidade
    private JFrame framePai;
    
    // Componentes para autocomplete
    private JPopupMenu popupSugestoes;
    private JList<AlunoView> listaSugestoes;
    private DefaultListModel<AlunoView> listModelSugestoes;
    private AlunoDAO alunoDAO;
    private AlunoView alunoSelecionado;
    
    // Novos componentes
    private JButton btnGerarDocumento;
    private JTable tableDisciplinas;
    private DefaultTableModel modelDisciplinas;

    // Construtores
    public Documentos(JFrame framePai) {
        this.framePai = framePai;
        try {
            this.alunoDAO = new AlunoDAO();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        setupLayout();
        inicializarComponentes();
        adicionarListeners();
    }

    public Documentos() {
        this(null);
    }
    
    private void setupLayout() {
        final String HINT_TEXT = "Informe ID, Nome ou RA do Aluno";
        final Color HINT_COLOR = Color.LIGHT_GRAY;
        final Color TEXT_COLOR = Color.BLACK;

        setLayout(new BorderLayout(5, 5));

        // ----- Painel de Filtros (Topo) -----
        panelFiltros = new JPanel();
        panelFiltros.setLayout(null);
        panelFiltros.setPreferredSize(new Dimension(950, 75));
        
        lblNewLabel = new JLabel("Aluno:");
        lblNewLabel.setBounds(29, 28, 68, 35);
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panelFiltros.add(lblNewLabel);
        
        txtBuscar = new JTextField(); 
        txtBuscar.setBounds(78, 33, 325, 27);		
        txtBuscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txtBuscar.setColumns(10);
        txtBuscar.setText(HINT_TEXT);
        txtBuscar.setForeground(HINT_COLOR);
        panelFiltros.add(txtBuscar);
        
        JLabel lblDocumento = new JLabel("Tipo de Documento:");
        lblDocumento.setBounds(429, 28, 150, 35);
        lblDocumento.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panelFiltros.add(lblDocumento);
        
        cmbDocumento = new JComboBox<>(new String[]{"Boletim", "Histórico Escolar"});
        cmbDocumento.setBounds(571, 33, 150, 27);
        panelFiltros.add(cmbDocumento);
        
        btnGerarDocumento = new JButton("Gerar Documento");
        btnGerarDocumento.setIcon(new ImageIcon(Documentos.class.getResource("/Resources/imagens/documento-de-texto.png")));
        btnGerarDocumento.setBounds(755, 22, 179, 50);
        btnGerarDocumento.setEnabled(false);  // Desabilitado até selecionar aluno
        panelFiltros.add(btnGerarDocumento);
        
        add(panelFiltros, BorderLayout.NORTH);

        // Novo: Painel wrapper para centralizar o conteúdo com margens
        panelWrapper = new JPanel(new BorderLayout());
        panelWrapper.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 50, 10, 50));  // Margens: topo, esquerda, baixo, direita
        panelDocumento = new JPanel(new BorderLayout());
        panelWrapper.add(panelDocumento, BorderLayout.CENTER);  // Adiciona o panelDocumento ao centro do wrapper
        add(panelWrapper, BorderLayout.CENTER);

        // ----- Painel de Botões (Inferior) -----
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVoltar = new JButton("Voltar para Listagem");
        pnlBotoes.add(btnVoltar);
        add(pnlBotoes, BorderLayout.SOUTH);
        
        btnVoltar.addActionListener(e -> voltarParaListagem());
    }
    
    private void inicializarComponentes() {
        inicializarAutocomplete();
        
        String[] colunas = {"Disciplina", "Nota", "Faltas", "Semestre"};
        modelDisciplinas = new DefaultTableModel(colunas, 0);
        tableDisciplinas = new JTable(modelDisciplinas);
        tableDisciplinas.setPreferredScrollableViewportSize(new Dimension(600, 200));
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
                    setText(aluno.getIdAluno() + " - " + aluno.getNome() + " (RA: " + aluno.getRa() + ")");
                }
                return this;
            }
        });

        JScrollPane scrollPaneSugestoes = new JScrollPane(listaSugestoes);
        popupSugestoes.add(scrollPaneSugestoes);
    }

    private void adicionarListeners() {
        final String HINT_TEXT = "Informe ID, Nome ou RA do Aluno";
        final Color HINT_COLOR = Color.LIGHT_GRAY;
        final Color TEXT_COLOR = Color.BLACK;

        txtBuscar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBuscar.getText().equals(HINT_TEXT)) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(TEXT_COLOR);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent() != null && e.getOppositeComponent().getParent() == popupSugestoes) {
                    return;
                }
                if (txtBuscar.getText().isEmpty()) {
                    txtBuscar.setText(HINT_TEXT);
                    txtBuscar.setForeground(HINT_COLOR);
                }
            }
        });

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { atualizarSugestoes(); }
            @Override public void removeUpdate(DocumentEvent e) { atualizarSugestoes(); }
            @Override public void changedUpdate(DocumentEvent e) { }
        });
        
        listaSugestoes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selecionarAlunoSugerido();
                }
            }
        });

        btnGerarDocumento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (alunoSelecionado == null) {
                    JOptionPane.showMessageDialog(Documentos.this, "Selecione um aluno primeiro!");
                    return;
                }
                String tipoDocumento = (String) cmbDocumento.getSelectedItem();
                if ("Boletim".equals(tipoDocumento)) {
                    gerarBoletim(alunoSelecionado.getIdAluno());
                } else if ("Histórico Escolar".equals(tipoDocumento)) {
                    gerarHistoricoEscolar(alunoSelecionado.getIdAluno());
                }
            }
        });
    }

    private void atualizarSugestoes() {
        String textoDigitado = txtBuscar.getText().trim();

        if (textoDigitado.isEmpty() || textoDigitado.equals("Informe ID, Nome ou RA do Aluno")) {
            popupSugestoes.setVisible(false);
            btnGerarDocumento.setEnabled(false);
            return;
        }

        try {
            List<AlunoView> alunosEncontrados = alunoDAO.listarPorFiltro(textoDigitado);
            listModelSugestoes.clear();
            if (!alunosEncontrados.isEmpty()) {
                for (AlunoView aluno : alunosEncontrados) {
                    listModelSugestoes.addElement(aluno);
                }
                popupSugestoes.show(txtBuscar, 0, txtBuscar.getHeight());
                popupSugestoes.setPopupSize(txtBuscar.getWidth(), 150);
            } else {
                popupSugestoes.setVisible(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            popupSugestoes.setVisible(false);
        }
    }

    private void selecionarAlunoSugerido() {
        alunoSelecionado = listaSugestoes.getSelectedValue();
        if (alunoSelecionado != null) {
            txtBuscar.setText(alunoSelecionado.getNome() + " (" + alunoSelecionado.getNomeCurso() + ")");
            popupSugestoes.setVisible(false);
            btnGerarDocumento.setEnabled(true);
        }
    }

    private void gerarBoletim(int idAluno) {
        try {
            BoletimAluno dadosAluno = alunoDAO.buscarDadosBoletimAluno(idAluno);
            if (dadosAluno == null) {
                JOptionPane.showMessageDialog(this, "Dados do aluno não encontrados.");
                return;
            }

            List<DisciplinaBoletim> disciplinas = alunoDAO.buscarDisciplinasBoletim(idAluno);
            exibirDocumento(dadosAluno, disciplinas, "Boletim");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao gerar boletim: " + e.getMessage());
        }
    }

    private void gerarHistoricoEscolar(int idAluno) {
        try {
            BoletimAluno dadosAluno = alunoDAO.buscarDadosBoletimAluno(idAluno);
            if (dadosAluno == null) {
                JOptionPane.showMessageDialog(this, "Dados do aluno não encontrados.");
                return;
            }

            List<DisciplinaBoletim> disciplinas = alunoDAO.buscarHistoricoEscolar(idAluno);
            exibirDocumento(dadosAluno, disciplinas, "Histórico Escolar");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao gerar histórico: " + e.getMessage());
        }
    }

    private void exibirDocumento(BoletimAluno dadosAluno, List<DisciplinaBoletim> disciplinas, String tipo) {
        JPanel panelInfo = new JPanel(new GridLayout(4, 2));
        
        panelInfo.add(new JLabel("Nome:"));
        panelInfo.add(new JLabel(dadosAluno.getNome()));
        panelInfo.add(new JLabel("Curso:"));
        panelInfo.add(new JLabel(dadosAluno.getNomeCurso()));
        panelInfo.add(new JLabel("ID Aluno:"));
        panelInfo.add(new JLabel(String.valueOf(dadosAluno.getIdAluno())));
        panelInfo.add(new JLabel("RA:"));
        panelInfo.add(new JLabel(dadosAluno.getRa()));
        

        modelDisciplinas.setRowCount(0);
        for (DisciplinaBoletim disc : disciplinas) {
            modelDisciplinas.addRow(new Object[]{
                disc.getNomeDisciplina(),
                String.format("%.2f", disc.getNota()),
                disc.getFaltas(),
                disc.getSemestreAtual()
            });
        }

        panelDocumento.removeAll();
        panelDocumento.add(panelInfo, BorderLayout.NORTH);
        panelDocumento.add(new JScrollPane(tableDisciplinas), BorderLayout.CENTER);
        panelDocumento.revalidate();
        panelDocumento.repaint();

        if (!disciplinas.isEmpty()) {
            double media = disciplinas.stream().mapToDouble(DisciplinaBoletim::getNota).average().orElse(0.0);
            JOptionPane.showMessageDialog(this, tipo + " gerado. Média Geral: " + String.format("%.2f", media));
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum registro encontrado para " + tipo + ".");
        }
    }

    private void voltarParaListagem() {
        if (framePai instanceof TelaPrincipal) {
            TelaPrincipal telaPrincipal = (TelaPrincipal) framePai;
            telaPrincipal.trocarPainelConteudo(new ListarAlunos(telaPrincipal));
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Tela de Documentos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            Documentos painel = new Documentos();
            frame.setContentPane(painel);
            frame.setVisible(true);
        });
    }
}
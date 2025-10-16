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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import br.edu.fatecgru.mvcaluno.dao.AlunoDAO;
import br.edu.fatecgru.mvcaluno.model.AlunoView;
import br.edu.fatecgru.mvcaluno.model.BoletimAluno;
import br.edu.fatecgru.mvcaluno.model.DisciplinaBoletim;

public class Boletim extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel lblNewLabel;
    private JTextField txtBuscar;
    private JPanel panelFiltros;
    private JPanel panelBoletim; 
    
    private JFrame framePai;
    
    private JPopupMenu popupSugestoes;
    private JList<AlunoView> listaSugestoes;
    private DefaultListModel<AlunoView> listModelSugestoes;
    private AlunoDAO alunoDAO;
    private AlunoView alunoSelecionado;
    
    private JButton btnGerarBoletim;
    private JTable tableDisciplinas;
    private DefaultTableModel modelDisciplinas;

    public Boletim(JFrame framePai) {
        this.framePai = framePai;
        try {
            this.alunoDAO = new AlunoDAO();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        setupLayout();
        inicializarComponentesBoletim();
        adicionarListeners();
    }

    public Boletim() {
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
        
        btnGerarBoletim = new JButton("Gerar Boletim");
        btnGerarBoletim.setBounds(420, 33, 150, 27);
        btnGerarBoletim.setEnabled(false);
        panelFiltros.add(btnGerarBoletim);
        
        add(panelFiltros, BorderLayout.NORTH);

        panelBoletim = new JPanel(new BorderLayout());
        add(panelBoletim, BorderLayout.CENTER);

        // ----- Painel de Botões (Inferior) -----
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVoltar = new JButton("Voltar para Listagem");
        pnlBotoes.add(btnVoltar);
        add(pnlBotoes, BorderLayout.SOUTH);
        
        btnVoltar.addActionListener(e -> voltarParaListagem());
    }
    
    private void inicializarComponentesBoletim() {
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

        btnGerarBoletim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Tentando gerar boletim para aluno: " + (alunoSelecionado != null ? alunoSelecionado.getNome() : "null"));
                if (alunoSelecionado == null) {
                    JOptionPane.showMessageDialog(Boletim.this, "Selecione um aluno primeiro!");
                    return;
                }
                gerarBoletim(alunoSelecionado.getIdAluno());
            }
        });
    }

    private void atualizarSugestoes() {
        String textoDigitado = txtBuscar.getText().trim();

        if (textoDigitado.isEmpty() || textoDigitado.equals("Informe ID, Nome ou RA do Aluno")) {
            popupSugestoes.setVisible(false);
            btnGerarBoletim.setEnabled(false);
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
            System.out.println("Aluno selecionado: " + alunoSelecionado.getNome());
            txtBuscar.setText(alunoSelecionado.getNome());
            popupSugestoes.setVisible(false);
            btnGerarBoletim.setEnabled(true);
        }
    }

    private void gerarBoletim(int idAluno) {
    	try {
            System.out.println("Gerando boletim para ID: " + idAluno);
            BoletimAluno dadosAluno = alunoDAO.buscarDadosBoletimAluno(idAluno);
            if (dadosAluno == null) {
                JOptionPane.showMessageDialog(this, "Dados do aluno não encontrados.");
                return;
            }

            // Exibe dados do aluno
            JPanel panelInfo = new JPanel(new GridLayout(4, 2));
            
            // 🚨 GARANTIA DE QUE ESTAMOS USANDO O CAMPO CORRETO DO MODELO
            panelInfo.add(new JLabel("Campus:"));
            panelInfo.add(new JLabel(dadosAluno.getCampus()));         
            
            panelInfo.add(new JLabel("RA:"));
            panelInfo.add(new JLabel(dadosAluno.getRa()));
            panelInfo.add(new JLabel("Nome:"));
            panelInfo.add(new JLabel(dadosAluno.getNome()));
            panelInfo.add(new JLabel("Curso:"));
            panelInfo.add(new JLabel(dadosAluno.getNomeCurso()));
            
            List<DisciplinaBoletim> disciplinas = alunoDAO.buscarDisciplinasBoletim(idAluno);
            modelDisciplinas.setRowCount(0);
            for (DisciplinaBoletim disc : disciplinas) {
                modelDisciplinas.addRow(new Object[]{
                    disc.getNomeDisciplina(),
                    String.format("%.2f", disc.getNota()),
                    disc.getFaltas(),
                    disc.getSemestreAtual()
                });
            }

            panelBoletim.removeAll();
            panelBoletim.add(panelInfo, BorderLayout.NORTH);
            panelBoletim.add(new JScrollPane(tableDisciplinas), BorderLayout.CENTER);
            panelBoletim.revalidate();
            panelBoletim.repaint();

            // Opcional: Cálculo de média 
            if (!disciplinas.isEmpty()) {
                double media = disciplinas.stream().mapToDouble(DisciplinaBoletim::getNota).average().orElse(0.0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao gerar boletim: " + e.getMessage());
        }
    }

    private void voltarParaListagem() {
        if (framePai instanceof TelaPrincipal) {
            TelaPrincipal telaPrincipal = (TelaPrincipal) framePai;
            // Ajuste a chamada conforme a sua classe TelaPrincipal
            // telaPrincipal.trocarPainelConteudo(new ListarAlunos(telaPrincipal));
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Tela de Boletim");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            Boletim painel = new Boletim();
            frame.setContentPane(painel);
            frame.setVisible(true);
        });
    }
}
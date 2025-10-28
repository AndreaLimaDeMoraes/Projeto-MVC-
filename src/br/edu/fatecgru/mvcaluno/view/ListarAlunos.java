package br.edu.fatecgru.mvcaluno.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;

import br.edu.fatecgru.mvcaluno.dao.AlunoDAO;
import br.edu.fatecgru.mvcaluno.dao.CursoDAO;
import br.edu.fatecgru.mvcaluno.model.AlunoTableModelSimplificado;
import br.edu.fatecgru.mvcaluno.model.AlunoView; 

public class ListarAlunos extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel lblNewLabel;
    private JTextField txtBuscar;
    private JLabel lblCurso;
    private JComboBox<String> cmbCurso;
    private JTable tblListaAlunos;
    private JPanel panelFiltros;
    private JButton btnNovoAluno;
    
    private JFrame framePai; 
    private int idAluno; 

    public ListarAlunos(JFrame framePai) {
        this.framePai = framePai;
        setupLayout();
    }

    public ListarAlunos() {
        setupLayout();
    }
    
    private void setupLayout() {
        
        final String HINT_TEXT = "Informe nome ou RA do aluno";
        final Color HINT_COLOR = Color.LIGHT_GRAY;
        final Color TEXT_COLOR = Color.BLACK;

        setLayout(new BorderLayout(5, 5)); 

        panelFiltros = new JPanel();
        panelFiltros.setLayout(null);
        panelFiltros.setPreferredSize(new Dimension(950, 75));
        
        lblNewLabel = new JLabel("Buscar:");
        lblNewLabel.setBounds(10, 33, 68, 35);
        panelFiltros.add(lblNewLabel);
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        
        txtBuscar = new JTextField(); 
        txtBuscar.setBounds(66, 38, 325, 27);		
        txtBuscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txtBuscar.setColumns(10);

        txtBuscar.setText(HINT_TEXT);
        txtBuscar.setForeground(HINT_COLOR);
        
        panelFiltros.add(txtBuscar);

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
        
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                realizarBusca();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                realizarBusca();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                realizarBusca();
            }
            
            private void realizarBusca() {
                String textoBusca = txtBuscar.getText();
                final String HINT_TEXT = "Informe nome ou RA do aluno";
                
                if (textoBusca.equals(HINT_TEXT) || textoBusca.trim().isEmpty()) {
                    carregarTabelaAlunos(null); 
                } else {
                    carregarTabelaAlunos(textoBusca);
                }
            }
        });
                
        lblCurso = new JLabel("Curso:");
        lblCurso.setBounds(415, 31, 54, 35);
        panelFiltros.add(lblCurso);
        lblCurso.setFont(new Font("Tahoma", Font.PLAIN, 15));
        
        cmbCurso = new JComboBox<>();
        cmbCurso.setBounds(466, 36, 274, 27); 
        popularComboCursos();
        panelFiltros.add(cmbCurso);
        
        btnNovoAluno = new JButton(" Novo aluno");
        btnNovoAluno.setHorizontalAlignment(SwingConstants.LEFT);
        btnNovoAluno.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/adicionar-usuario.png")));
        btnNovoAluno.setForeground(Color.black);
        btnNovoAluno.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnNovoAluno.setBounds(762, 25, 167, 40);
        btnNovoAluno.setContentAreaFilled(false); 
        btnNovoAluno.setFocusPainted(false);
        panelFiltros.add(btnNovoAluno);
        
        cmbCurso.addActionListener(e -> {
            aplicarFiltroCurso();
        });
        
        btnNovoAluno.addActionListener(e -> {
            
            if (framePai instanceof TelaPrincipal) {
                
                TelaPrincipal telaPrincipal = (TelaPrincipal) framePai;
                
                DadosPessoais telaCadastro = new DadosPessoais(telaPrincipal, 0); 
                telaPrincipal.trocarPainelConteudo(
                	    telaPrincipal.getPnlConteudoAluno(),
                	    (JPanel) telaCadastro
                	);
                telaPrincipal.ativarBotaoMenuDadosPessoais();
                
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Clique em 'Novo Aluno'. O formulário de cadastro seria aberto aqui.", 
                    "Teste de Cadastro", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        add(panelFiltros, BorderLayout.NORTH);
        
        tblListaAlunos = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblListaAlunos);
        add(scrollPane, BorderLayout.CENTER);
        carregarTabelaAlunos(null); 
        adicionarEventoCliqueTabela();
    }
    
    private void aplicarFiltroCurso() {
        String itemSelecionado = (String) cmbCurso.getSelectedItem();
        
        if (itemSelecionado == null || itemSelecionado.equals("Todos os Cursos")) {
            carregarTabelaAlunos(null); 
        } else {
            String nomeCurso = itemSelecionado;
            int indexParenteses = nomeCurso.lastIndexOf(" (");
            
            if (indexParenteses != -1) {
                nomeCurso = nomeCurso.substring(0, indexParenteses);
            }
            
            carregarTabelaAlunosPorCurso(nomeCurso); 
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
                "Erro ao carregar cursos para filtro: " + e.getMessage(), 
                "Erro de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }    
    
    private void carregarTabelaAlunosPorCurso(String nomeCurso) {
        try {
            AlunoDAO dao = new AlunoDAO();
            List<AlunoView> listaAlunos = dao.listarPorCurso(nomeCurso);
            
            tblListaAlunos.setModel(new AlunoTableModelSimplificado(listaAlunos));
            configurarVisualTabela();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao filtrar alunos por curso: " + e.getMessage(), 
                "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void configurarVisualTabela() {
        tblListaAlunos.setFont(new Font("Tahoma", Font.PLAIN, 15));
        tblListaAlunos.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 15));
        tblListaAlunos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        tblListaAlunos.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        tblListaAlunos.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        tblListaAlunos.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
        tblListaAlunos.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblListaAlunos.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        tblListaAlunos.getColumnModel().getColumn(0).setPreferredWidth(100); 
        tblListaAlunos.getColumnModel().getColumn(1).setPreferredWidth(255); 
        tblListaAlunos.getColumnModel().getColumn(2).setPreferredWidth(300); 
        tblListaAlunos.getColumnModel().getColumn(3).setPreferredWidth(180); 
        tblListaAlunos.getColumnModel().getColumn(4).setPreferredWidth(117); 
        
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame frameTeste = new JFrame("Teste Listar Alunos");
                    frameTeste.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameTeste.setBounds(100, 100, 800, 600); 
                    ListarAlunos painelAlunos = new ListarAlunos();
                    frameTeste.setContentPane(painelAlunos);
                    frameTeste.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private void carregarTabelaAlunos(String filtro) {
        try {
            AlunoDAO dao = new AlunoDAO();
            List<AlunoView> listaAlunos; 
            
            if (filtro == null || filtro.trim().isEmpty()) {
                listaAlunos = dao.listarTodos(); 
            } else {
                listaAlunos = dao.listarPorFiltro(filtro);
            }
            
            tblListaAlunos.setModel(new AlunoTableModelSimplificado(listaAlunos));
            configurarVisualTabela();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar lista de alunos: " + e.getMessage(), 
                "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void adicionarEventoCliqueTabela() {
        tblListaAlunos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int linhaSelecionada = tblListaAlunos.getSelectedRow();
                    if (linhaSelecionada != -1) {
                        AlunoTableModelSimplificado model = (AlunoTableModelSimplificado) tblListaAlunos.getModel();
                        AlunoView aluno = model.getAlunoAt(linhaSelecionada);
                        int idAluno = aluno.getIdAluno();

                        abrirTelaDadosPessoais(idAluno);
                    }
                }
            }
        });
    }

    private void abrirTelaDadosPessoais(int idAluno) {
        
        if (framePai instanceof TelaPrincipal) {
            
            TelaPrincipal telaPrincipal = (TelaPrincipal) framePai;
            
            DadosPessoais telaEdicao = new DadosPessoais(telaPrincipal, idAluno); 
            
            telaPrincipal.trocarPainelConteudo(
            	    telaPrincipal.getPnlConteudoAluno(),
            	    (JPanel) telaEdicao
            	);

            	telaPrincipal.ativarBotaoMenuDadosPessoais();
            
        } else {
            JOptionPane.showMessageDialog(this, 
                "Aluno ID " + idAluno + " selecionado. A tela de Edição/Exclusão seria aberta aqui.", 
                "Teste de Clique", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

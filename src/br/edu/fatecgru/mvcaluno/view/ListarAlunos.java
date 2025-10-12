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
import br.edu.fatecgru.mvcaluno.view.TelaPrincipal;
import br.edu.fatecgru.mvcaluno.dao.AlunoDAO;
import br.edu.fatecgru.mvcaluno.model.AlunoView;
import br.edu.fatecgru.mvcaluno.model.AlunoTableModelSimplificado; 

public class ListarAlunos extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblNewLabel;
	private JTextField txtBuscar;
	private JLabel lblCurso;
	private JComboBox<String> cmbCurso;
	private JTable tblListaAlunos;
	private JPanel panelFiltros;
	private JButton btnNovoAluno;
	
    // Variável para armazenar a referência ao JFrame principal (TelaPrincipal)
    private JFrame framePai; 
    private int idAluno; 

    // ===================================
    // CONSTRUTORES
    // ===================================

    /**
     * Construtor principal chamado pela TelaPrincipal.
     * Recebe a referência do JFrame pai para troca de telas.
     */
    public ListarAlunos(JFrame framePai) {
        this.framePai = framePai;
        setupLayout(); // Chama o método que monta a interface e carrega os dados
    }

    /**
     * Construtor auxiliar (apenas para o main de teste).
     */
    public ListarAlunos() {
        setupLayout();
    }
    
    // ===================================
    // MÉTODO DE LAYOUT
    // ===================================
    
    private void setupLayout() {
        
        // Define as constantes da Hint
        final String HINT_TEXT = "Informe nome ou RA do aluno";
        final Color HINT_COLOR = Color.LIGHT_GRAY;
        final Color TEXT_COLOR = Color.BLACK;

        // Define o layout principal do JPanel ListarAlunos
        setLayout(new BorderLayout(5, 5)); 

        // ----- Painel de Filtros (Topo) -----
        panelFiltros = new JPanel();
        panelFiltros.setLayout(null);
        panelFiltros.setPreferredSize(new Dimension(950, 75));
        
        // Campos de Filtro
        lblNewLabel = new JLabel("Buscar:");
        lblNewLabel.setBounds(10, 33, 68, 35);
        panelFiltros.add(lblNewLabel);
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        
        // INSTANCIA o JTextField
        txtBuscar = new JTextField(); 
        txtBuscar.setBounds(66, 38, 325, 27);		
        txtBuscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txtBuscar.setColumns(10);

        // CONFIGURA O HINT INICIAL
        txtBuscar.setText(HINT_TEXT);
        txtBuscar.setForeground(HINT_COLOR);
        
        // ADICIONA O PAINEL (Chamada ÚNICA)
        panelFiltros.add(txtBuscar);

        // ADICIONA O FocusListener (Lógica de Hint)
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
            
            // Método chamado quando o texto é inserido
            @Override
            public void insertUpdate(DocumentEvent e) {
                realizarBusca();
            }
            
            // Método chamado quando o texto é removido
            @Override
            public void removeUpdate(DocumentEvent e) {
                realizarBusca();
            }
            
            // Método chamado quando o texto é alterado
            @Override
            public void changedUpdate(DocumentEvent e) {
                realizarBusca();
            }
            
            // Método auxiliar para obter o texto de busca e chamar a atualização da tabela
            private void realizarBusca() {
                String textoBusca = txtBuscar.getText();
                final String HINT_TEXT = "Informe nome ou RA do aluno";
                
                // Ignora a busca se for o texto de hint
                if (textoBusca.equals(HINT_TEXT) || textoBusca.trim().isEmpty()) {
                    // Se o usuário apagar tudo, recarrega a lista completa
                    carregarTabelaAlunos(null); 
                } else {
                    // Chama a função de carregamento com o que foi digitado
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
        
        btnNovoAluno.addActionListener(e -> {
            
            // VERIFICA e faz o CASTING (Conversão de Tipo)
            if (framePai instanceof TelaPrincipal) {
                
                // Faz o casting do JFrame genérico para o TelaPrincipal específico
                TelaPrincipal telaPrincipal = (TelaPrincipal) framePai;
                
                //Instancia a nova tela de cadastro (DadosPessoais)
                // O valor '0' ou '-1' é uma convenção para indicar que é um NOVO cadastro
                DadosPessoais telaCadastro = new DadosPessoais(telaPrincipal, 0); 
                
                //Chama o método do TelaPrincipal para trocar o painel central
                telaPrincipal.trocarPainelConteudo(telaCadastro);
                
                //Muda o foco do menu lateral
                telaPrincipal.ativarBotaoMenuDadosPessoais();
                
            } else {
                // Isso deve ser usado apenas para o seu main de teste, onde framePai é um JFrame simples
                JOptionPane.showMessageDialog(this, 
                    "Clique em 'Novo Aluno'. O formulário de cadastro seria aberto aqui.", 
                    "Teste de Cadastro", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Adiciona o painel de filtros na região NORTE (Topo)
        add(panelFiltros, BorderLayout.NORTH);
        
        // ----- Tabela de Alunos (Centro) -----
        tblListaAlunos = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblListaAlunos);
        add(scrollPane, BorderLayout.CENTER);
        carregarTabelaAlunos(null); 
        add(scrollPane, BorderLayout.CENTER);
        adicionarEventoCliqueTabela();
    }
    
    

    private void configurarVisualTabela() {
        tblListaAlunos.setFont(new Font("Tahoma", Font.PLAIN, 15));
        tblListaAlunos.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 15));
        tblListaAlunos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        tblListaAlunos.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);

        tblListaAlunos.getColumnModel().getColumn(0).setPreferredWidth(55);
        tblListaAlunos.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblListaAlunos.getColumnModel().getColumn(2).setPreferredWidth(300);
        tblListaAlunos.getColumnModel().getColumn(3).setPreferredWidth(300);
        tblListaAlunos.getColumnModel().getColumn(4).setPreferredWidth(150);
    }
    
	/**
	 * Launch the application. (Mantido para testes)
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
                    // Para testar esta classe, ela deve ser exibida em um JFrame
                    JFrame frameTeste = new JFrame("Teste Listar Alunos");
                    frameTeste.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameTeste.setBounds(100, 100, 800, 600); 
					ListarAlunos painelAlunos = new ListarAlunos(); // Usa o construtor sem argumentos para o teste
                    frameTeste.setContentPane(painelAlunos);
					frameTeste.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	 // ======================================
    // MÉTODOS DE CONTROLE DA TABELA
    // ======================================

	private void carregarTabelaAlunos(String filtro) {
		try {
	        AlunoDAO dao = new AlunoDAO();
	        List<AlunoView> listaAlunos; 
	        
	        if (filtro == null || filtro.trim().isEmpty()) {
	            // Se o filtro for vazio, lista todos
	            listaAlunos = dao.listarTodos(); 
	        } else {
	            // Se houver filtro, chama o novo método de busca
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
                        // O ID do Aluno é a primeira coluna (índice 0)
                        Object idValue = tblListaAlunos.getValueAt(linhaSelecionada, 0);
                        int idAluno = (int) idValue; 
                        
                        abrirTelaDadosPessoais(idAluno);
                    }
                }
            }
        });
    }

    private void abrirTelaDadosPessoais(int idAluno) {
        
        //Verifica se o framePai é a TelaPrincipal
        if (framePai instanceof TelaPrincipal) {
            
            TelaPrincipal telaPrincipal = (TelaPrincipal) framePai;
            
            //Instancia a nova tela de DadosPessoais, passando o ID do aluno.
            // O valor idAluno (> 0) indica que a tela deve carregar os dados para EDIÇÃO/EXCLUSÃO.
            DadosPessoais telaEdicao = new DadosPessoais(telaPrincipal, idAluno); 
            
            //Troca o painel central
            telaPrincipal.trocarPainelConteudo(telaEdicao);
            
            //Muda o foco do menu lateral para "Dados Pessoais"
            telaPrincipal.ativarBotaoMenuDadosPessoais();
            
        } else {
            // Lógica para main de teste, se necessário
            JOptionPane.showMessageDialog(this, 
                "Aluno ID " + idAluno + " selecionado. A tela de Edição/Exclusão seria aberta aqui.", 
                "Teste de Clique", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
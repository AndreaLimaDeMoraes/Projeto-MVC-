package br.edu.fatecgru.mvcaluno.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.Toolkit;

public class TelaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JPanel panelAluno;
	private JPanel panelFaculdade;
	private JPanel panelMenuAluno;
	private JButton btnListar;
	private JButton btnDadosPessoais;
	private JButton btnDocumentos;
	private JPanel panelMenuAluno_1;
	private JButton btnCursos;
	private JButton btnDisciplinas;
	private JPanel pnlConteudoAluno;
	
	
	//CORES PARA MUDAR O FOCO NOS BOTÕES
	// Cor Padrão
	private final Color COR_INATIVA = new Color(54, 70, 78);
	// Cor de Destaque (Um tom mais escuro para simular o clique/foco)
	private final Color COR_ATIVA = new Color(40, 50, 58);
	private List<JButton> botoesMenuAluno;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenu mnNewMenu_1;
	private JMenu mnNewMenu_2;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmNewMenuItem_1;
	private JMenuItem mntmNewMenuItem_2;
	private JMenuItem mntmNewMenuItem_3;
	private JMenuItem mntmNewMenuItem_4;
	private JSeparator separator;
	private JMenuItem mntmNewMenuItem_5;
	private JMenuItem mntmNewMenuItem_6;
	private JMenuItem mntmNewMenuItem_7;
	private JMenuItem mntmNewMenuItem_8;
	private JMenuItem mntmNewMenuItem_9;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal frame = new TelaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 */
	public TelaPrincipal() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/imagens/emprego.png")));
		setTitle("MVC ALUNO");		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 960, 557);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("Aluno");
		menuBar.add(mnNewMenu);
		
		mntmNewMenuItem = new JMenuItem("Salvar");
		mntmNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		mnNewMenu.add(mntmNewMenuItem);
		
		mntmNewMenuItem_1 = new JMenuItem("Alterar");
		mnNewMenu.add(mntmNewMenuItem_1);
		
		mntmNewMenuItem_2 = new JMenuItem("Consultar");
		mnNewMenu.add(mntmNewMenuItem_2);
		
		mntmNewMenuItem_3 = new JMenuItem("Excluir");
		mnNewMenu.add(mntmNewMenuItem_3);
		
		separator = new JSeparator();
		mnNewMenu.add(separator);
		
		mntmNewMenuItem_4 = new JMenuItem("Sair");
		mntmNewMenuItem_4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_DOWN_MASK));
		mnNewMenu.add(mntmNewMenuItem_4);
		
		mnNewMenu_1 = new JMenu("Notas e Faltas");
		menuBar.add(mnNewMenu_1);
		
		mntmNewMenuItem_5 = new JMenuItem("Salvar");
		mnNewMenu_1.add(mntmNewMenuItem_5);
		
		mntmNewMenuItem_7 = new JMenuItem("Alterar");
		mntmNewMenuItem_7.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		mnNewMenu_1.add(mntmNewMenuItem_7);
		
		mntmNewMenuItem_6 = new JMenuItem("Excluir");
		mnNewMenu_1.add(mntmNewMenuItem_6);
		
		mntmNewMenuItem_8 = new JMenuItem("Consultar");
		mnNewMenu_1.add(mntmNewMenuItem_8);
		
		mnNewMenu_2 = new JMenu("Ajuda");
		menuBar.add(mnNewMenu_2);
		
		mntmNewMenuItem_9 = new JMenuItem("Sobre");
		mnNewMenu_2.add(mntmNewMenuItem_9);
		contentPane = new JPanel();
		contentPane.setToolTipText("");
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tabbedPane.setBounds(10, 26, 924, 459);
		contentPane.add(tabbedPane);
		
		panelAluno = new JPanel();
		tabbedPane.addTab("   Aluno   ", null, panelAluno, null);
		panelAluno.setLayout(null);
		
		panelMenuAluno = new JPanel();
		panelMenuAluno.setBackground(new Color(54, 70, 78));
		panelMenuAluno.setBounds(0, 0, 165, 426);
		panelAluno.add(panelMenuAluno);
		panelMenuAluno.setLayout(null);
		
		btnListar = new JButton("   Listar alunos");
		btnListar.setForeground(new Color(255, 255, 255));
		btnListar.setBackground(new Color(54, 70, 78));
		btnListar.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnListar.setHorizontalAlignment(SwingConstants.LEFT);
		btnListar.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/lista-de-controle (3).png")));
		btnListar.setBounds(0, 5, 166, 46);
		btnListar.setBorderPainted(false);   // Remove a borda estática (se houver)
		btnListar.setContentAreaFilled(false); // Remove o preenchimento da área de conteúdo (ajuda na transparência)
		//Remove o contorno que aparece ao clicar ou quando o botão está selecionado (Focus Border)
		btnListar.setFocusPainted(false);
		panelMenuAluno.add(btnListar);
		
		btnDadosPessoais = new JButton("   Dados pessoais");
		btnDadosPessoais.setForeground(new Color(255, 255, 255));
		btnDadosPessoais.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/perfil-de-usuario (1).png")));
		btnDadosPessoais.setHorizontalAlignment(SwingConstants.LEFT);
		btnDadosPessoais.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnDadosPessoais.setBounds(-6, 53, 172, 53);
		btnDadosPessoais.setBorderPainted(false);   // Remove a borda estática (se houver)
		btnDadosPessoais.setContentAreaFilled(false); // Remove o preenchimento da área de conteúdo (ajuda na transparência)
		//Remove o contorno que aparece ao clicar ou quando o botão está selecionado (Focus Border)
		btnDadosPessoais.setFocusPainted(false);
		panelMenuAluno.add(btnDadosPessoais);
		
		btnDocumentos = new JButton("  Documentos");
		btnDocumentos.setOpaque(false);
		btnDocumentos.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/documentos.png")));
		btnDocumentos.setHorizontalAlignment(SwingConstants.LEFT);
		btnDocumentos.setForeground(Color.WHITE);
		btnDocumentos.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnDocumentos.setContentAreaFilled(false);
		btnDocumentos.setBorderPainted(false); // Remove a borda estática (se houver)
		btnDocumentos.setBounds(0, 106, 178, 46);
		btnDocumentos.setContentAreaFilled(false); // Remove o preenchimento da área de conteúdo (ajuda na transparência)
		//Remove o contorno que aparece ao clicar ou quando o botão está selecionado (Focus Border)
		btnDocumentos.setFocusPainted(false);
		panelMenuAluno.add(btnDocumentos);
		
		pnlConteudoAluno = new JPanel();
		pnlConteudoAluno.setBounds(166, 0, 753, 426);
		panelAluno.add(pnlConteudoAluno);
		
		panelFaculdade = new JPanel();
		tabbedPane.addTab("  Faculdade  ", null, panelFaculdade, null);
		panelFaculdade.setLayout(null);
		
		panelMenuAluno_1 = new JPanel();
		panelMenuAluno_1.setLayout(null);
		panelMenuAluno_1.setBackground(new Color(54, 70, 78));
		panelMenuAluno_1.setBounds(0, 0, 165, 426);
		panelFaculdade.add(panelMenuAluno_1);
		
		btnCursos = new JButton("   Cursos");
		btnCursos.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/chapeu-de-graduacao.png")));
		btnCursos.setHorizontalAlignment(SwingConstants.LEFT);
		btnCursos.setForeground(Color.WHITE);
		btnCursos.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnCursos.setBackground(new Color(54, 70, 78));
		btnCursos.setBounds(0, 0, 165, 55);
		btnCursos.setBorderPainted(false);  // Remove a borda estática (se houver)
		btnCursos.setContentAreaFilled(false); // Remove o preenchimento da área de conteúdo (ajuda na transparência)
		//Remove o contorno que aparece ao clicar ou quando o botão está selecionado (Focus Border)
		btnCursos.setFocusPainted(false);
		panelMenuAluno_1.add(btnCursos);
		
		btnDisciplinas = new JButton("   Disciplinas");
		btnDisciplinas.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/caderno.png")));
		btnDisciplinas.setHorizontalAlignment(SwingConstants.LEFT);
		btnDisciplinas.setForeground(Color.WHITE);
		btnDisciplinas.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnDisciplinas.setBounds(-1, 55, 167, 55);
		btnDisciplinas.setBorderPainted(false); // Remove a borda estática (se houver)
		btnDisciplinas.setContentAreaFilled(false); // Remove o preenchimento da área de conteúdo (ajuda na transparência)
		//Remove o contorno que aparece ao clicar ou quando o botão está selecionado (Focus Border)
		btnDisciplinas.setFocusPainted(false);
		panelMenuAluno_1.add(btnDisciplinas);
		
		
		//evento para mudar de cor os botoes
		botoesMenuAluno = new ArrayList<>();
		botoesMenuAluno.add(btnListar);
		botoesMenuAluno.add(btnDadosPessoais);
		botoesMenuAluno.add(btnDocumentos);
		botoesMenuAluno.add(btnCursos);
		botoesMenuAluno.add(btnDisciplinas);
		
		

		btnListar.addActionListener(e -> {
		    // Chama a função que gerencia a cor
		    ativarBotaoMenu(btnListar);

		    //Lógica de Conteúdo (Troca de Painel)
		    pnlConteudoAluno.removeAll();
		    pnlConteudoAluno.setLayout(null);
		    
		    //Instancia o novo JPanel de conteúdo
		    ListarAlunos listarAlunos = new ListarAlunos();
		    listarAlunos.setBounds(0, 0, 753, 426);
		    pnlConteudoAluno.add(listarAlunos);
		    listarAlunos.setLayout(null);
		    
		    pnlConteudoAluno.revalidate(); // Recalcula o layout
		    pnlConteudoAluno.repaint(); // Redesenha a tela
		});
		
		btnDadosPessoais.addActionListener(e -> {
		    // Chama a função que gerencia a cor
		    ativarBotaoMenu(btnDadosPessoais);

		    // ** Lógica do MVC para trocar de tela vem AQUI **
		    // ...
		});
		
		btnDocumentos.addActionListener(e -> {
		    // Chama a função que gerencia a cor
		    ativarBotaoMenu(btnDocumentos);

		    // ** Lógica do MVC para trocar de tela vem AQUI **
		    // Por exemplo, painel.setLayout(new BorderLayout()); painel.add(new ListaAlunosView(), BorderLayout.CENTER);
		});
		
		btnCursos.addActionListener(e -> {
		    // Chama a função que gerencia a cor
		    ativarBotaoMenu(btnCursos);

		    // ** Lógica do MVC para trocar de tela vem AQUI **
		    // ...
		});

		btnDisciplinas.addActionListener(e -> {
		    // Chama a função que gerencia a cor
		    ativarBotaoMenu(btnDisciplinas);

		    // ** Lógica do MVC para trocar de tela vem AQUI **
		    // ...
		});
	}
	/**
	 * Altera a cor e a opacidade dos botões do menu para indicar qual está ativo.
	 * @param botaoClicado O botão que acabou de ser clicado.
	 */
	private void ativarBotaoMenu(JButton botaoClicado) {
	    // 1. LIMPAR O FOCO: Define todos os botões como INATIVOS/Transparentes
	    for (JButton botao : botoesMenuAluno) {
	        // Volta a exibir a cor do Painel de Fundo (COR_PADRAO_MENU)
	        botao.setOpaque(false);
	        // O setBackground(COR_PADRAO_MENU) é opcional, mas garante o estado
	        botao.setBackground(COR_INATIVA); 
	    }

	    // 2. ATIVAR FOCO: Define o botão clicado como ATIVO/Opaco com a cor de destaque
	    botaoClicado.setOpaque(true);
	    botaoClicado.setBackground(COR_ATIVA);
	}
}

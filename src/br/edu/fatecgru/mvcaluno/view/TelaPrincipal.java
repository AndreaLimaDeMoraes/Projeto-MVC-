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
import java.awt.Component;
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
import br.edu.fatecgru.mvcaluno.view.TelaCurso;

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
    private JButton btnNotasFaltas; // Movido para campo da classe
    private JPanel panelMenuAluno_1;
    private JButton btnCursos;
    private JButton btnDisciplinas;
    private JPanel pnlConteudoAluno;
    private JPanel pnlConteudoFaculdade;
    private JPanel telaAtual;
    
    // CORES PARA MUDAR O FOCO NOS BOTÕES
    // Cor Padrão
    private final Color COR_INATIVA = new Color(54, 70, 78);
    // Cor de Destaque (Um tom mais escuro para simular o clique/foco)
    private final Color COR_ATIVA = new Color(40, 50, 58);
    private List<JButton> botoesMenuAluno;
    private List<JButton> botoesMenuFaculdade; // Adicionada lista separada para Faculdade
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
        setBounds(100, 100, 1200, 557);
        
        menuBar = new JMenuBar();
        menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 15));
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
        tabbedPane.setBounds(10, 26, 1166, 459);
        contentPane.add(tabbedPane);
        
        panelAluno = new JPanel();
        tabbedPane.addTab("   Aluno   ", null, panelAluno, null);
        panelAluno.setLayout(null);
        
        panelMenuAluno = new JPanel();
        panelMenuAluno.setBackground(new Color(54, 70, 78));
        panelMenuAluno.setBounds(0, 0, 190, 426);
        panelAluno.add(panelMenuAluno);
        panelMenuAluno.setLayout(null);
        
        // ===== BOTÕES CRIADOS DIRETAMENTE PARA SEREM VISÍVEIS NO DESIGN =====
        btnListar = new JButton("   Listar alunos");
        btnListar.setForeground(new Color(255, 255, 255));
        btnListar.setBackground(new Color(54, 70, 78));
        btnListar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnListar.setHorizontalAlignment(SwingConstants.LEFT);
        btnListar.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/lista-de-controle (3).png")));
        btnListar.setBounds(0, 1, 188, 50);
        btnListar.setBorderPainted(false);
        btnListar.setContentAreaFilled(false);
        btnListar.setFocusPainted(false);
        panelMenuAluno.add(btnListar);
        
        btnDadosPessoais = new JButton("   Dados pessoais");
        btnDadosPessoais.setForeground(new Color(255, 255, 255));
        btnDadosPessoais.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/perfil-de-usuario (1).png")));
        btnDadosPessoais.setHorizontalAlignment(SwingConstants.LEFT);
        btnDadosPessoais.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnDadosPessoais.setBounds(-6, 53, 194, 53);
        btnDadosPessoais.setBorderPainted(false);
        btnDadosPessoais.setContentAreaFilled(false);
        btnDadosPessoais.setFocusPainted(false);
        panelMenuAluno.add(btnDadosPessoais);
        
        btnDocumentos = new JButton("  Documentos");
        btnDocumentos.setOpaque(false);
        btnDocumentos.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/documentos.png")));
        btnDocumentos.setHorizontalAlignment(SwingConstants.LEFT);
        btnDocumentos.setForeground(Color.WHITE);
        btnDocumentos.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnDocumentos.setContentAreaFilled(false);
        btnDocumentos.setBorderPainted(false);
        btnDocumentos.setBounds(0, 167, 188, 46);
        btnDocumentos.setContentAreaFilled(false);
        btnDocumentos.setFocusPainted(false);
        panelMenuAluno.add(btnDocumentos);
        
        // ===== BOTÃO NOTAS E FALTAS CRIADO DIRETAMENTE NO CONSTRUTOR =====
        btnNotasFaltas = new JButton("   Notas e Faltas");
        btnNotasFaltas.setOpaque(false);
        btnNotasFaltas.setIcon(new ImageIcon(TelaPrincipal.class.getResource("/Resources/imagens/atribuicao.png")));
        btnNotasFaltas.setHorizontalAlignment(SwingConstants.LEFT);
        btnNotasFaltas.setForeground(Color.WHITE);
        btnNotasFaltas.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnNotasFaltas.setFocusPainted(false);
        btnNotasFaltas.setContentAreaFilled(false);
        btnNotasFaltas.setBorderPainted(false);
        btnNotasFaltas.setBounds(-6, 113, 196, 46);
        panelMenuAluno.add(btnNotasFaltas);
        
        pnlConteudoAluno = new JPanel();
        pnlConteudoAluno.setBounds(197, 0, 955, 426);
        panelAluno.add(pnlConteudoAluno);
        
        panelFaculdade = new JPanel();
        tabbedPane.addTab("  Faculdade  ", null, panelFaculdade, null);
        panelFaculdade.setLayout(null);
        
        panelMenuAluno_1 = new JPanel();
        panelMenuAluno_1.setLayout(null);
        panelMenuAluno_1.setBackground(new Color(54, 70, 78));
        panelMenuAluno_1.setBounds(0, 0, 190, 426);
        panelFaculdade.add(panelMenuAluno_1);
        
        btnCursos = new JButton("   Cursos");
        btnCursos.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/chapeu-de-graduacao.png")));
        btnCursos.setHorizontalAlignment(SwingConstants.LEFT);
        btnCursos.setForeground(Color.WHITE);
        btnCursos.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnCursos.setBackground(new Color(54, 70, 78));
        btnCursos.setBounds(0, 0, 190, 55);
        btnCursos.setBorderPainted(false);
        btnCursos.setContentAreaFilled(false);
        btnCursos.setFocusPainted(false);
        panelMenuAluno_1.add(btnCursos);
        
        btnDisciplinas = new JButton("   Disciplinas");
        btnDisciplinas.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/caderno.png")));
        btnDisciplinas.setHorizontalAlignment(SwingConstants.LEFT);
        btnDisciplinas.setForeground(Color.WHITE);
        btnDisciplinas.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnDisciplinas.setBounds(-1, 55, 191, 55);
        btnDisciplinas.setBorderPainted(false);
        btnDisciplinas.setContentAreaFilled(false);
        btnDisciplinas.setFocusPainted(false);
        panelMenuAluno_1.add(btnDisciplinas);
        
        pnlConteudoFaculdade = new JPanel();
        pnlConteudoFaculdade.setBounds(197, 0, 955, 426);
        panelFaculdade.add(pnlConteudoFaculdade);
        
        // ===== INICIALIZAÇÃO DAS LISTAS =====
        botoesMenuAluno = new ArrayList<>();
        botoesMenuAluno.add(btnListar);
        botoesMenuAluno.add(btnDadosPessoais);
        botoesMenuAluno.add(btnDocumentos);
        botoesMenuAluno.add(btnNotasFaltas); // Adicionado à lista
        
        botoesMenuFaculdade = new ArrayList<>();
        botoesMenuFaculdade.add(btnCursos);
        botoesMenuFaculdade.add(btnDisciplinas);
        
        // ===== LISTENERS DOS BOTÕES =====
        btnListar.addActionListener(e -> {
            ativarBotaoMenu(btnListar);
            pnlConteudoAluno.removeAll();
            pnlConteudoAluno.setLayout(new BorderLayout());
            ListarAlunos listarAlunos = new ListarAlunos(TelaPrincipal.this);
            pnlConteudoAluno.add(listarAlunos, BorderLayout.CENTER);
            pnlConteudoAluno.revalidate();
            pnlConteudoAluno.repaint();
        });
        
        btnDadosPessoais.addActionListener(e -> {
            ativarBotaoMenu(btnDadosPessoais);
            DadosPessoais telaInicialDadosPessoais = new DadosPessoais(TelaPrincipal.this, 0);
            trocarPainelConteudo(telaInicialDadosPessoais);
        });
        
        btnDocumentos.addActionListener(e -> {
            ativarBotaoMenu(btnDocumentos);
            Documentos documentos = new Documentos(TelaPrincipal.this);
            trocarPainelConteudo(documentos);
        });
        
        // ===== LISTENER PARA NOTAS E FALTAS =====
        btnNotasFaltas.addActionListener(e -> {
            ativarBotaoMenu(btnNotasFaltas);
            NotasFaltas telaInicialNotasFaltas = new NotasFaltas(TelaPrincipal.this, 0);
            trocarPainelConteudo(telaInicialNotasFaltas);
        });
        
        btnCursos.addActionListener(e -> {
            ativarBotaoMenu(btnCursos);
            tabbedPane.setSelectedComponent(panelFaculdade);
            pnlConteudoFaculdade.removeAll();
            pnlConteudoFaculdade.setLayout(new BorderLayout());
            TelaCurso telaCurso = new TelaCurso(TelaPrincipal.this, 0);
            telaAtual = telaCurso;
            pnlConteudoFaculdade.add(telaCurso, BorderLayout.CENTER);
            pnlConteudoFaculdade.revalidate();
            pnlConteudoFaculdade.repaint();
        });

        btnDisciplinas.addActionListener(e -> {
            ativarBotaoMenu(btnDisciplinas);
            tabbedPane.setSelectedComponent(panelFaculdade);
            pnlConteudoFaculdade.removeAll();
            pnlConteudoFaculdade.setLayout(new BorderLayout());
            TelaDisciplina disciplina = new TelaDisciplina();
            telaAtual = disciplina;
            pnlConteudoFaculdade.add(disciplina, BorderLayout.CENTER);
            pnlConteudoFaculdade.revalidate();
            pnlConteudoFaculdade.repaint();
        });
        
        conectarMenus();
    }
    
    private void conectarMenus() {
        mntmNewMenuItem.addActionListener(e -> {
            if (telaAtual instanceof TelaCurso) {
                ((TelaCurso) telaAtual).salvarCurso();
            }
        });
        mntmNewMenuItem_1.addActionListener(e -> {
            if (telaAtual instanceof TelaCurso) {
                ((TelaCurso) telaAtual).alterarCurso();
            }
        });
        mntmNewMenuItem_3.addActionListener(e -> {
            if (telaAtual instanceof TelaCurso) {
                ((TelaCurso) telaAtual).excluirCurso();
            }
        });
    }
    
    protected void ativarBotaoMenu(JButton botaoClicado) {
        List<JButton> lista = (botaoClicado == btnCursos || botaoClicado == btnDisciplinas) ? botoesMenuFaculdade : botoesMenuAluno;
        for (JButton botao : lista) {
            botao.setOpaque(false);
            botao.setBackground(COR_INATIVA);
        }
        botaoClicado.setOpaque(true);
        botaoClicado.setBackground(COR_ATIVA);
    }
    
    public void trocarPainelConteudo(JPanel novoPainel) {
        pnlConteudoAluno.removeAll();
        pnlConteudoAluno.setLayout(new BorderLayout());
        pnlConteudoAluno.add(novoPainel, BorderLayout.CENTER);
        pnlConteudoAluno.revalidate();
        pnlConteudoAluno.repaint();
    }
    
    public void trocarPainelConteudo(JPanel container, JPanel novoPainel) {
        container.removeAll();
        container.setLayout(new BorderLayout());
        container.add(novoPainel, BorderLayout.CENTER);
        container.revalidate();
        container.repaint();
    }
    
    public JPanel getPnlConteudoAluno() {
        return pnlConteudoAluno;
    }
    
    public void ativarBotaoMenuDadosPessoais() {
        ativarBotaoMenu(btnDadosPessoais);
    }
    
    public void ativarBotaoNotasFaltas() {
        if (btnNotasFaltas != null) {
            ativarBotaoMenu(btnNotasFaltas);
        }
    }
    
    public void ativarBotaoMenuListarAlunos() {
        ativarBotaoMenu(btnListar);
    }
}

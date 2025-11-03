package br.edu.fatecgru.mvcaluno.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

/**
 * Classe principal do sistema MVC Aluno.
 * Esta classe cria a janela principal com abas para "Aluno" e "Faculdade",
 * incluindo menus, botões de navegação e áreas de conteúdo dinâmicas.
 */
public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    // Painéis principais da janela
    private JPanel contentPane; // painel raiz do JFrame
    private JTabbedPane tabbedPane; // abas para alternar entre "Aluno" e "Faculdade"
    private JPanel panelAluno; // painel da aba "Aluno"
    private JPanel panelFaculdade; // painel da aba "Faculdade"
    
    // Menus laterais e botões da aba "Aluno"
    private JPanel panelMenuAluno;
    private JButton btnListar;
    private JButton btnDadosPessoais;
    private JButton btnDocumentos;
    private JButton btnNotasFaltas; // botão de notas e faltas
    
    // Menus laterais e botões da aba "Faculdade"
    private JPanel panelMenuAluno_1;
    private JButton btnCursos;
    private JButton btnDisciplinas;

    // Painéis de conteúdo dinâmico (onde serão carregadas telas internas)
    private JPanel pnlConteudoAluno;
    private JPanel pnlConteudoFaculdade;
    private JPanel telaAtual; // referência à tela atual aberta (ex: TelaCurso, TelaDisciplina)

    // CORES PARA MUDAR O FOCO NOS BOTÕES
    private final Color COR_INATIVA = new Color(54, 70, 78); // cor padrão dos botões
    private final Color COR_ATIVA = new Color(40, 50, 58); // cor de destaque quando o botão está selecionado

    // Listas de botões para facilitar alternância de cores e foco
    private List<JButton> botoesMenuAluno;
    private List<JButton> botoesMenuFaculdade; 

    // Menu superior
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
     * Método principal para iniciar a aplicação.
     * Utiliza EventQueue para garantir que a interface seja criada na thread correta.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaPrincipal frame = new TelaPrincipal();
                    frame.setVisible(true); // torna a janela visível
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Construtor da TelaPrincipal.
     * Inicializa todos os componentes, menus, botões e painéis de conteúdo.
     */
    public TelaPrincipal() {
        // Configurações da janela
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/imagens/emprego.png")));
        setTitle("MVC ALUNO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 557);

        // Inicializa menu superior
        menuBar = new JMenuBar();
        menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        setJMenuBar(menuBar);

        // Menu "Aluno" com itens Salvar, Alterar, Consultar, Excluir e Sair
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
        mntmNewMenuItem_4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK));
        mnNewMenu.add(mntmNewMenuItem_4);

        // Menu "Notas e Faltas" com itens Salvar, Alterar, Excluir e Consultar
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
        
        // Menu "Ajuda" com item Sobre
        mnNewMenu_2 = new JMenu("Ajuda");
        menuBar.add(mnNewMenu_2);
        mntmNewMenuItem_9 = new JMenuItem("Sobre");
        mnNewMenu_2.add(mntmNewMenuItem_9);

        // =============================================
        // CONEXÃO DOS MENUS (ADICIONE ESTE BLOCO)
        // =============================================

        // Listeners para o menu "Aluno"
        mntmNewMenuItem.addActionListener(e -> salvarAluno());
        mntmNewMenuItem_1.addActionListener(e -> alterarAluno());
        mntmNewMenuItem_2.addActionListener(e -> consultarAluno());
        mntmNewMenuItem_3.addActionListener(e -> excluirAluno());
        mntmNewMenuItem_4.addActionListener(e -> System.exit(0));

        // Listeners para o menu "Notas e Faltas"
        mntmNewMenuItem_5.addActionListener(e -> salvarNotasFaltas());
        mntmNewMenuItem_7.addActionListener(e -> alterarNotasFaltas());
        mntmNewMenuItem_6.addActionListener(e -> excluirNotasFaltas());
        mntmNewMenuItem_8.addActionListener(e -> consultarNotasFaltas());

        // Listener para o menu "Ajuda"
        mntmNewMenuItem_9.addActionListener(e -> mostrarSobre());

        // Painel principal que contém o tabbedPane
        contentPane = new JPanel();
        contentPane.setToolTipText("");
        contentPane.setBorder(null);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Criação das abas "Aluno" e "Faculdade"
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 15));
        tabbedPane.setBounds(10, 26, 1166, 459);
        contentPane.add(tabbedPane);

        // Painel da aba "Aluno"
        panelAluno = new JPanel();
        tabbedPane.addTab(" Aluno ", null, panelAluno, null);
        panelAluno.setLayout(null);

        // Menu lateral da aba "Aluno"
        panelMenuAluno = new JPanel();
        panelMenuAluno.setBackground(new Color(54, 70, 78));
        panelMenuAluno.setBounds(0, 0, 190, 426);
        panelAluno.add(panelMenuAluno);
        panelMenuAluno.setLayout(null);

        // Botões da aba "Aluno" (Listar, Dados Pessoais, Documentos, Notas e Faltas)
        // Cada botão possui ícone, cor, alinhamento e estilo de foco

        btnListar = new JButton(" Listar alunos");
        btnListar.setForeground(Color.WHITE);
        btnListar.setBackground(new Color(54, 70, 78));
        btnListar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnListar.setHorizontalAlignment(SwingConstants.LEFT);
        btnListar.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/lista-de-controle (3).png")));
        btnListar.setBounds(0, 1, 188, 58);
        btnListar.setBorderPainted(false);
        btnListar.setContentAreaFilled(false);
        btnListar.setFocusPainted(false);
        panelMenuAluno.add(btnListar);

        btnDadosPessoais = new JButton(" Dados pessoais");
        btnDadosPessoais.setForeground(Color.WHITE);
        btnDadosPessoais.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/perfil-de-usuario (1).png")));
        btnDadosPessoais.setHorizontalAlignment(SwingConstants.LEFT);
        btnDadosPessoais.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnDadosPessoais.setBounds(-6, 62, 194, 53);
        btnDadosPessoais.setBorderPainted(false);
        btnDadosPessoais.setContentAreaFilled(false);
        btnDadosPessoais.setFocusPainted(false);
        panelMenuAluno.add(btnDadosPessoais);

        btnDocumentos = new JButton("  Documentos");
        btnDocumentos.setOpaque(false);
        btnDocumentos.setIcon(new ImageIcon(TelaPrincipal.class.getResource("/Resources/imagens/pasta (1).png")));
        btnDocumentos.setHorizontalAlignment(SwingConstants.LEFT);
        btnDocumentos.setForeground(Color.WHITE);
        btnDocumentos.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnDocumentos.setContentAreaFilled(false);
        btnDocumentos.setBorderPainted(false);
        btnDocumentos.setBounds(-6, 176, 194, 53);
        btnDocumentos.setFocusPainted(false);
        panelMenuAluno.add(btnDocumentos);

        btnNotasFaltas = new JButton("  Notas e Faltas");
        btnNotasFaltas.setOpaque(false);
        btnNotasFaltas.setIcon(new ImageIcon(TelaPrincipal.class.getResource("/Resources/imagens/atribuicao.png")));
        btnNotasFaltas.setHorizontalAlignment(SwingConstants.LEFT);
        btnNotasFaltas.setForeground(Color.WHITE);
        btnNotasFaltas.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnNotasFaltas.setFocusPainted(false);
        btnNotasFaltas.setContentAreaFilled(false);
        btnNotasFaltas.setBorderPainted(false);
        btnNotasFaltas.setBounds(-10, 122, 202, 53);
        panelMenuAluno.add(btnNotasFaltas);

        // Painel de conteúdo da aba "Aluno", onde serão carregadas as telas internas
        pnlConteudoAluno = new JPanel();
        pnlConteudoAluno.setBounds(197, 0, 955, 426);
        panelAluno.add(pnlConteudoAluno);

        // Painel da aba "Faculdade"
        panelFaculdade = new JPanel();
        tabbedPane.addTab(" Faculdade ", null, panelFaculdade, null);
        panelFaculdade.setLayout(null);

        // Menu lateral da aba "Faculdade"
        panelMenuAluno_1 = new JPanel();
        panelMenuAluno_1.setLayout(null);
        panelMenuAluno_1.setBackground(new Color(54, 70, 78));
        panelMenuAluno_1.setBounds(0, 0, 190, 426);
        panelFaculdade.add(panelMenuAluno_1);

        // Botões da aba "Faculdade" (Cursos e Disciplinas)
        btnCursos = new JButton(" Cursos");
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

        btnDisciplinas = new JButton(" Disciplinas");
        btnDisciplinas.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/caderno.png")));
        btnDisciplinas.setHorizontalAlignment(SwingConstants.LEFT);
        btnDisciplinas.setForeground(Color.WHITE);
        btnDisciplinas.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnDisciplinas.setBounds(-1, 55, 191, 55);
        btnDisciplinas.setBorderPainted(false);
        btnDisciplinas.setContentAreaFilled(false);
        btnDisciplinas.setFocusPainted(false);
        panelMenuAluno_1.add(btnDisciplinas);

        // Painel de conteúdo da aba "Faculdade"
        pnlConteudoFaculdade = new JPanel();
        pnlConteudoFaculdade.setBounds(197, 0, 955, 426);
        panelFaculdade.add(pnlConteudoFaculdade);

        // Inicializa listas de botões para controle de foco
        botoesMenuAluno = new ArrayList<>();
        botoesMenuAluno.add(btnListar);
        botoesMenuAluno.add(btnDadosPessoais);
        botoesMenuAluno.add(btnDocumentos);
        botoesMenuAluno.add(btnNotasFaltas);

        botoesMenuFaculdade = new ArrayList<>();
        botoesMenuFaculdade.add(btnCursos);
        botoesMenuFaculdade.add(btnDisciplinas);

        // LISTENERS DOS BOTÕES
        // Cada botão ao ser clicado ativa o foco visual e troca o painel de conteúdo
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
    }

    // =============================================
    // MÉTODOS PÚBLICOS PARA O MENU
    // =============================================

    /**
     * Método público para salvar aluno - chamado pelo menu
     */
    public void salvarAluno() {
        System.out.println("Menu Salvar acionado");
        JOptionPane.showMessageDialog(this, "Funcionalidade Salvar Aluno");
    }

    /**
     * Método público para alterar aluno - chamado pelo menu  
     */
    public void alterarAluno() {
        System.out.println("Menu Alterar acionado");
        JOptionPane.showMessageDialog(this, "Funcionalidade Alterar Aluno");
    }

   
    
    /**
     * Método público para excluir aluno - chamado pelo menu
     */
    public void excluirAluno() {
        System.out.println("Menu Excluir acionado");
        
        // Verifica se há uma lista de alunos aberta
        if (pnlConteudoAluno.getComponentCount() > 0) {
            Component primeiroComponente = pnlConteudoAluno.getComponent(0);
            if (primeiroComponente instanceof ListarAlunos) {
                ListarAlunos lista = (ListarAlunos) primeiroComponente;
                lista.excluirAlunoSelecionado();
                return;
            }
        }
        
        JOptionPane.showMessageDialog(this, 
            "Abra a lista de alunos primeiro para excluir um aluno", 
            "Aviso", JOptionPane.WARNING_MESSAGE);
    }
    

    /**
     * Método público para consultar aluno - chamado pelo menu
     */
    public void consultarAluno() {
        System.out.println("Menu Consultar acionado");
        JOptionPane.showMessageDialog(this, "Funcionalidade Consultar Aluno");
    }

    /**
     * Método público para salvar notas e faltas - chamado pelo menu
     */
    public void salvarNotasFaltas() {
        System.out.println("Menu Salvar Notas e Faltas acionado");
        JOptionPane.showMessageDialog(this, "Funcionalidade Salvar Notas e Faltas");
    }

    /**
     * Método público para alterar notas e faltas - chamado pelo menu
     */
    public void alterarNotasFaltas() {
        System.out.println("Menu Alterar Notas e Faltas acionado");
        JOptionPane.showMessageDialog(this, "Funcionalidade Alterar Notas e Faltas");
    }

    /**
     * Método público para excluir notas e faltas - chamado pelo menu
     */
    public void excluirNotasFaltas() {
        System.out.println("Menu Excluir Notas e Faltas acionado");
        JOptionPane.showMessageDialog(this, "Funcionalidade Excluir Notas e Faltas");
    }

    /**
     * Método público para consultar notas e faltas - chamado pelo menu
     */
    public void consultarNotasFaltas() {
        System.out.println("Menu Consultar Notas e Faltas acionado");
        JOptionPane.showMessageDialog(this, "Funcionalidade Consultar Notas e Faltas");
    }

    private void mostrarSobre() {
        JOptionPane.showMessageDialog(this, 
            "Sistema MVC Aluno\nVersão 1.0", 
            "Sobre", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Ativa o botão clicado e desativa os outros, mudando a cor de fundo
     * para simular foco/seleção.
     */
    protected void ativarBotaoMenu(JButton botaoClicado) {
        List<JButton> lista = (botaoClicado == btnCursos || botaoClicado == btnDisciplinas)
                ? botoesMenuFaculdade : botoesMenuAluno;
        for (JButton botao : lista) {
            botao.setOpaque(false);
            botao.setBackground(COR_INATIVA);
        }
        botaoClicado.setOpaque(true);
        botaoClicado.setBackground(COR_ATIVA);
    }

    /**
     * Troca o conteúdo da aba "Aluno" para o novo painel passado como parâmetro.
     */
    public void trocarPainelConteudo(JPanel novoPainel) {
        pnlConteudoAluno.removeAll();
        pnlConteudoAluno.setLayout(new BorderLayout());
        pnlConteudoAluno.add(novoPainel, BorderLayout.CENTER);
        pnlConteudoAluno.revalidate();
        pnlConteudoAluno.repaint();
    }

    /**
     * Troca o conteúdo de um painel específico (passado como container) para outro painel.
     */
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

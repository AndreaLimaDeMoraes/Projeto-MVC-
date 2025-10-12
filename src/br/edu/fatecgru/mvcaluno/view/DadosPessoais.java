package br.edu.fatecgru.mvcaluno.view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;

public class DadosPessoais extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private JFrame framePai;
    private int idAluno;

    // ===================================
    // CONSTRUTOR CORRETO
    // ===================================
    /**
     * Construtor para Ação: Usado tanto para NOVO CADASTRO (idAluno=0)
     * quanto para EDIÇÃO (idAluno > 0).
     */
    public DadosPessoais(JFrame framePai, int idAluno) {
        this.framePai = framePai;
        this.idAluno = idAluno;
        
        setupLayout(); // Chama o método que monta a interface
    }
    
    // ===================================
    // LAYOUT E COMPONENTES
    // ===================================
    private void setupLayout() {
        // 1. Configuração do Painel Principal
        setLayout(new BorderLayout(10, 10));
        // Adiciona um espaçamento interno
        setBorder(new EmptyBorder(15, 15, 15, 15)); 
        
        // 2. Título (Apenas para indicar o modo)
        String titulo = (idAluno > 0) ? "EM EDIÇÃO: Dados Pessoais do Aluno ID: " + idAluno : "CADASTRAR NOVO ALUNO (Área de Trabalho da Equipe)";
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);
        
        // 3. Painel de Conteúdo (Local onde sua equipe adicionará os campos)
        JPanel pnlConteudo = new JPanel();
        pnlConteudo.setLayout(new BorderLayout());
        pnlConteudo.add(new JLabel("Aguardando formulário completo...", SwingConstants.CENTER));
        add(pnlConteudo, BorderLayout.CENTER);

        // 4. Painel de Botões (SUL)
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVoltar = new JButton("Voltar para Listagem");
        
        pnlBotoes.add(btnVoltar);
        
        add(pnlBotoes, BorderLayout.SOUTH);
        
        // 5. Adiciona a ação de Voltar
        btnVoltar.addActionListener(e -> voltarParaListagem());
    }
    
    // ===================================
    // LÓGICA DE NAVEGAÇÃO
    // ===================================
    /**
     * Lógica para voltar para o painel de Listagem de Alunos e reativar o foco no menu.
     */
    private void voltarParaListagem() {
        if (framePai instanceof TelaPrincipal) {
            TelaPrincipal telaPrincipal = (TelaPrincipal) framePai;
            
            // Instancia a ListarAlunos (que automaticamente recarrega a tabela)
            ListarAlunos painelListagem = new ListarAlunos(telaPrincipal);
            
            // Troca o painel central
            telaPrincipal.trocarPainelConteudo(painelListagem);
            
            // Muda o foco do menu lateral
            telaPrincipal.ativarBotaoMenuListarAlunos(); 
            
        } 
        // Se não for uma TelaPrincipal, ignoramos (pois é o main de teste)
    }
    
    // Removido o método main de teste (que estava incorreto por ser um JPanel)
}
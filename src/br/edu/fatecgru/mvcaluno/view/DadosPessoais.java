package br.edu.fatecgru.mvcaluno.view;

// Imports do seu esqueleto
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import br.edu.fatecgru.mvcaluno.dao.AlunoDAO;
import br.edu.fatecgru.mvcaluno.model.Aluno;
// --- IMPORTS ADICIONADOS DO NOVO FORMULÁRIO ---
import br.edu.fatecgru.mvcaluno.model.Curso;
import br.edu.fatecgru.mvcaluno.model.Matricula;
import br.edu.fatecgru.mvcaluno.util.ConnectionFactory; // Sua classe de conexão

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;

// --- IMPORTS ADICIONADOS PARA CORREÇÃO DA DATA ---
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DadosPessoais extends JPanel {

    private static final long serialVersionUID = 1L;
    
    // --- Variáveis de Navegação (do seu esqueleto) ---
    private JFrame framePai;
    private int idAluno;

    // --- COMPONENTES DO FORMULÁRIO (Corrigidos com RA) ---
    private JLabel lblRa, lblNome, lblDataNasc, lblCpf, lblEmail, lblEndereco, lblMunicipio, lblUf, lblCelular, lblCurso;
    private JTextField txtRa, txtNome, txtEmail, txtEndereco, txtMunicipio;
    private JFormattedTextField txtDataNasc, txtCpf, txtCelular;
    private JComboBox<String> jcbUf;
    private JComboBox<Curso> jcbCursos;
    private JButton btnRegistrar; // Botão de Registrar (Novo)
    private JButton btnSalvar;    // Botão de Salvar (Edição)

    // ===================================
      // Construtor ---
    // ===================================
    public DadosPessoais(JFrame framePai, int idAluno) {
        this.framePai = framePai;
        this.idAluno = idAluno;
        
        setupLayout();    // Chama o método que monta a interface
        carregarCursos(); // Popula o ComboBox de cursos
        
        // Se for modo de EDIÇÃO, carrega os dados do aluno
        if (this.idAluno > 0) {
            carregarDadosParaEdicao();
        }
    }
    
    // ===================================
    // LAYOUT E COMPONENTES
    // ===================================
    private void setupLayout() {
        // 1. Configuração do Painel Principal
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15)); 
        
        // 2. Título
        String titulo = (idAluno > 0) ? "EM EDIÇÃO: Dados Pessoais do Aluno ID: " + idAluno : "CADASTRAR NOVO ALUNO";
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);
        
        // 3. Painel de Conteúdo
        JPanel pnlConteudo = new JPanel();
        pnlConteudo.setBackground(new Color(230, 230, 230));
        
        // Inicialização dos componentes
        lblRa = new JLabel("RA"); 
        lblNome = new JLabel("Nome");
        lblDataNasc = new JLabel("Data de Nascimento");
        lblCpf = new JLabel("CPF");
        lblEmail = new JLabel("Email");
        lblEndereco = new JLabel("Endereço");
        lblMunicipio = new JLabel("Município");
        lblUf = new JLabel("UF");
        lblCelular = new JLabel("Celular");
        lblCurso = new JLabel("Curso");

        txtRa = new JTextField(10); 
        txtRa.setEditable(false); // o usuário não pode editar, RA é gerado automaticamente
        	
        // --- Gerar RA automático se for novo aluno ---
        if (idAluno == 0) { // novo aluno
            try {
                AlunoDAO alunoDAO = new AlunoDAO();
                txtRa.setText(alunoDAO.gerarProximoRA());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao gerar RA automático: " + ex.getMessage(),
                                              "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        txtNome = new JTextField(20);
        txtEmail = new JTextField(30);
        txtEndereco = new JTextField(30);
        txtMunicipio = new JTextField(15);

        try {
            txtDataNasc = new JFormattedTextField(new MaskFormatter("##/##/####"));
            txtCpf = new JFormattedTextField(new MaskFormatter("###.###.###-##"));
            txtCelular = new JFormattedTextField(new MaskFormatter("(##) #####-####"));
        } catch (ParseException e) {
            e.printStackTrace();
            txtDataNasc = new JFormattedTextField();
            txtCpf = new JFormattedTextField();
            txtCelular = new JFormattedTextField();
        }

        String[] ufs = {"SP", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SE", "TO"};
        jcbUf = new JComboBox<>(ufs);
        jcbCursos = new JComboBox<>();

        // Layout do painel de conteúdo
        GroupLayout layout = new GroupLayout(pnlConteudo);
        pnlConteudo.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblRa)
                    .addComponent(txtRa, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
                .addGap(20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblNome)
                    .addComponent(txtNome)))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblDataNasc)
                    .addComponent(txtDataNasc, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
                .addGap(20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblCpf)
                    .addComponent(txtCpf, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(lblEmail)
                .addComponent(txtEmail))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(lblEndereco)
                .addComponent(txtEndereco))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblMunicipio)
                    .addComponent(txtMunicipio, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
                .addGap(20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblUf)
                    .addComponent(jcbUf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblCelular)
                    .addComponent(txtCelular, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(lblCurso)
                .addComponent(jcbCursos))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblRa)
                .addComponent(lblNome))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(txtRa)
                .addComponent(txtNome))
            .addGap(15)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblDataNasc)
                .addComponent(lblCpf))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(txtDataNasc)
                .addComponent(txtCpf))
            .addGap(15)
            .addComponent(lblEmail)
            .addComponent(txtEmail)
            .addGap(15)
            .addComponent(lblEndereco)
            .addComponent(txtEndereco)
            .addGap(15)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblMunicipio)
                .addComponent(lblUf)
                .addComponent(lblCelular))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(txtMunicipio)
                .addComponent(jcbUf)
                .addComponent(txtCelular))
            .addGap(15)
            .addComponent(lblCurso)
            .addComponent(jcbCursos)
            .addContainerGap(20, Short.MAX_VALUE)
        );

        JScrollPane scrollPane = new JScrollPane(pnlConteudo);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        add(scrollPane, BorderLayout.CENTER);

        // 4. Painel de Botões com Excluir à esquerda
        JPanel pnlBotoes = new JPanel(new BorderLayout());

        // Painel Esquerdo -> Excluir
        JPanel pnlEsquerda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnExcluir = new JButton("Excluir Aluno");
        btnExcluir.setVisible(idAluno > 0);
        btnExcluir.addActionListener(ev -> {
            int opcao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir este aluno?",
                "Confirmação de Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (opcao == JOptionPane.YES_OPTION) {
                try {
                    AlunoDAO alunoDAO = new AlunoDAO();
                    alunoDAO.excluir(idAluno);
                    JOptionPane.showMessageDialog(this, 
                        "Aluno excluído com sucesso!",
                        "Exclusão Concluída",
                        JOptionPane.INFORMATION_MESSAGE);
                    voltarParaListagem();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                        "Erro ao excluir aluno: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        pnlEsquerda.add(btnExcluir);

        // Painel Direito -> Voltar, Salvar, Registrar
        JPanel pnlDireita = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVoltar = new JButton("Voltar para Listagem");
        btnRegistrar = new JButton("Registrar Aluno");
        btnRegistrar.setVisible(idAluno == 0);
        btnRegistrar.addActionListener(this::registrarAluno);
        btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.setVisible(idAluno > 0);
        btnSalvar.addActionListener(this::salvarAlteracoes);

        pnlDireita.add(btnVoltar);
        pnlDireita.add(btnSalvar);
        pnlDireita.add(btnRegistrar);

        pnlBotoes.add(pnlEsquerda, BorderLayout.WEST);
        pnlBotoes.add(pnlDireita, BorderLayout.EAST);

        add(pnlBotoes, BorderLayout.SOUTH);

        btnVoltar.addActionListener(e -> voltarParaListagem());
    }


    // ===============================================
    // LÓGICA DE BANCO DE DADOS (Métodos Adicionados)
    // ===============================================

    /**
     * Popula o JComboBox 'jcbCursos' com os dados da tabela 'curso'.
     */
    private void carregarCursos() {
        
        String sql = "SELECT idCurso, nome FROM curso ORDER BY nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            jcbCursos.removeAllItems();
            while (rs.next()) {
                Curso curso = new Curso(); 
                curso.setIdCurso(rs.getInt("idCurso"));
                curso.setNome(rs.getString("nome"));
                jcbCursos.addItem(curso);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar os cursos do banco de dados:\n" + e.getMessage(), 
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Carrega os dados do aluno (idAluno) do banco e preenche os campos
     * para permitir a edição.
     */
    private void carregarDadosParaEdicao() {
    	if (this.idAluno > 0) {
    	    carregarDadosAluno(this.idAluno); 
    	}

    }
    
    /**
     * Método de Ação do Botão "Registrar Aluno" (idAluno == 0).
     */
    private void registrarAluno(ActionEvent e) {
        // --- 1. Obter dados da interface ---
        String ra = txtRa.getText(); 
        String nome = txtNome.getText();
        String dataNascString = txtDataNasc.getText(); 
        String cpf = txtCpf.getText();
        String email = txtEmail.getText();
        String endereco = txtEndereco.getText();
        String municipio = txtMunicipio.getText();
        String uf = (String) jcbUf.getSelectedItem();
        String celular = txtCelular.getText();
        Curso cursoSelecionado = (Curso) jcbCursos.getSelectedItem();
        
        // --- Validação simples ---
        if (nome.isEmpty() || ra.isEmpty() || cpf.equals("   .   .   -  ") || cursoSelecionado == null) { 
            JOptionPane.showMessageDialog(this, 
                "Preencha pelo menos RA, Nome, CPF e selecione um Curso.", 
                "Campos Obrigatórios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idCurso = cursoSelecionado.getIdCurso();
        int semestre = 1; // Assumindo que todo novo aluno entra no 1º semestre

        // --- 2. Lógica de Transação no Banco de Dados ---
        Connection conn = null;
        PreparedStatement stmtAluno = null, stmtMatricula = null, stmtDisciplinas = null, stmtMatriculaDisc = null;
        ResultSet rsAluno = null, rsMatricula = null, rsDisciplinas = null;

        String sqlInsertAluno = "INSERT INTO aluno (ra, nome, dataNascimento, cpf, email, endereco, municipio, uf, celular, ativo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlInsertMatricula = "INSERT INTO matricula (idAluno, idCurso, ativo) VALUES (?, ?, 1)";
        String sqlSelectDisciplinas = "SELECT idDisciplina FROM disciplina WHERE idCurso = ? AND semestre = ?"; 
        String sqlInsertMatriculaDisc = "INSERT INTO matriculaDisciplina (idMatricula, idDisciplina, semestreCursado, faltas, nota) VALUES (?, ?, ?, ?, ?)";
        


        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            // --- Conversão da Data ---
            java.sql.Date dataNascimentoSQL = null;
            if (!dataNascString.equals("  /  /    ") && !dataNascString.trim().isEmpty()) {
                try {
                    DateTimeFormatter formatoBrasileiro = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dataNascimentoObj = LocalDate.parse(dataNascString, formatoBrasileiro);
                    dataNascimentoSQL = java.sql.Date.valueOf(dataNascimentoObj);
                } catch (Exception ex) {
                    throw new Exception("Formato de Data de Nascimento inválido. Use dd/MM/yyyy.");
                }
            }
            
            // --- ETAPA A: Inserir na tabela 'aluno' ---
            stmtAluno = conn.prepareStatement(sqlInsertAluno, Statement.RETURN_GENERATED_KEYS);
            stmtAluno.setString(1, ra); 
            stmtAluno.setString(2, nome); 
            stmtAluno.setDate(3, dataNascimentoSQL); 
            stmtAluno.setString(4, cpf); 
            stmtAluno.setString(5, email); 
            stmtAluno.setString(6, endereco); 
            stmtAluno.setString(7, municipio); 
            stmtAluno.setString(8, uf); 
            stmtAluno.setString(9, celular); 
            stmtAluno.setBoolean(10, true); 
            stmtAluno.executeUpdate();
            
            rsAluno = stmtAluno.getGeneratedKeys();
            if (!rsAluno.next()) throw new Exception("Falha ao obter o ID do Aluno gerado (idAluno).");
            int idAlunoGerado = rsAluno.getInt(1);

            // --- ETAPA B: Inserir na tabela 'matricula' ---
            
            // LÓGICA PARA PEGAR O SEMESTRE CORRETO (ex: "2025/1")
            String anoAtual = java.time.Year.now().toString();
            String semestreInicioTexto = null;
			try {
				semestreInicioTexto = calcularSemestreAtual();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
            
            stmtMatricula = conn.prepareStatement(sqlInsertMatricula, Statement.RETURN_GENERATED_KEYS);
            stmtMatricula.setInt(1, idAlunoGerado);
            stmtMatricula.setInt(2, idCurso);
            
            stmtMatricula.executeUpdate();
            
            rsMatricula = stmtMatricula.getGeneratedKeys();
            if (!rsMatricula.next()) throw new Exception("Falha ao obter o ID da Matrícula gerada.");
            int idMatriculaGerada = rsMatricula.getInt(1);

            // --- ETAPA C: Buscar disciplinas do 1º semestre do curso ---
            stmtDisciplinas = conn.prepareStatement(sqlSelectDisciplinas);
            stmtDisciplinas.setInt(1, idCurso);
            stmtDisciplinas.setInt(2, semestre); // Aqui está correto usar o INT 1
            
            rsDisciplinas = stmtDisciplinas.executeQuery();
            
            ArrayList<Integer> idsDisciplinas = new ArrayList<>();
            while (rsDisciplinas.next()) {
                idsDisciplinas.add(rsDisciplinas.getInt("idDisciplina"));
            }
            if (idsDisciplinas.isEmpty()) {
                throw new Exception("Nenhuma disciplina encontrada para o 1º semestre ("+semestre+") deste curso. Matrícula não finalizada.");
            }

            // --- ETAPA D: Inserir na tabela 'matriculaDisciplina' (em lote) ---
            stmtMatriculaDisc = conn.prepareStatement(sqlInsertMatriculaDisc);
            for (int idDisciplina : idsDisciplinas) {
                stmtMatriculaDisc.setInt(1, idMatriculaGerada);
                stmtMatriculaDisc.setInt(2, idDisciplina);
                stmtMatriculaDisc.setString(3, semestreInicioTexto); // <-- semestreCursado
                stmtMatriculaDisc.setInt(4, 0); // <-- faltas (inicia com 0)
                stmtMatriculaDisc.setFloat(5, 0.0f); // <-- nota (inicia com 0.0)
                stmtMatriculaDisc.addBatch();              
            }
            stmtMatriculaDisc.executeBatch(); 

            // --- SUCESSO: Efetiva a transação ---
            conn.commit(); 
            JOptionPane.showMessageDialog(this,
                    "Aluno registrado com sucesso!\n" +
                    "Matrícula realizada no semestre " + semestreInicioTexto + ".\n" +
                    idsDisciplinas.size() + " disciplinas associadas.",
                    "Cadastro Concluído",
                    JOptionPane.INFORMATION_MESSAGE);
            limparCampos();

        } catch (Exception ex) {
            // --- FALHA: Desfaz a transação ---
            try { if (conn != null) conn.rollback(); } catch (Exception exRollback) {}
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao registrar aluno: \n" + ex.getMessage(),
                    "Erro na Transação",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            // Fecha todos os recursos
            try { if (rsAluno != null) rsAluno.close(); } catch (Exception ex) {}
            try { if (rsMatricula != null) rsMatricula.close(); } catch (Exception ex) {}
            try { if (rsDisciplinas != null) rsDisciplinas.close(); } catch (Exception ex) {}
            try { if (stmtAluno != null) stmtAluno.close(); } catch (Exception ex) {}
            try { if (stmtMatricula != null) stmtMatricula.close(); } catch (Exception ex) {}
            try { if (stmtDisciplinas != null) stmtDisciplinas.close(); } catch (Exception ex) {}
            try { if (stmtMatriculaDisc != null) stmtMatriculaDisc.close(); } catch (Exception ex) {}
            try { 
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception ex) {}
        }
    }
    
    /**
     * Método de Ação do Botão "Salvar Alterações" (idAluno > 0).
     */
    private void salvarAlteracoes(ActionEvent e) {
        String ra = txtRa.getText(); 
        String nome = txtNome.getText();
        String dataNascString = txtDataNasc.getText(); 
        String cpf = txtCpf.getText();
        String email = txtEmail.getText();
        String endereco = txtEndereco.getText();
        String municipio = txtMunicipio.getText();
        String uf = (String) jcbUf.getSelectedItem();
        String celular = txtCelular.getText();
        Curso cursoSelecionado = (Curso) jcbCursos.getSelectedItem();

        if (nome.isEmpty() || ra.isEmpty() || cpf.equals("   .   .   -  ") || cursoSelecionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Preencha pelo menos RA, Nome, CPF e selecione um Curso.", 
                "Campos Obrigatórios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.sql.Date dataNascimentoSQL = null;
        if (!dataNascString.equals("  /  /    ") && !dataNascString.trim().isEmpty()) {
            try {
                DateTimeFormatter formatoBrasileiro = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataNascimentoObj = LocalDate.parse(dataNascString, formatoBrasileiro);
                dataNascimentoSQL = java.sql.Date.valueOf(dataNascimentoObj);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Formato de Data de Nascimento inválido. Use dd/MM/yyyy.",
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Connection conn = null;
        PreparedStatement psUpdateAluno = null;
        PreparedStatement psUpdateMatricula = null;
        PreparedStatement psSelectDisciplinas = null;
        PreparedStatement psInsertMatDisc = null;
        ResultSet rsDisciplinas = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); // iniciar transação

            // --- 1. Atualizar tabela aluno ---
            String sqlUpdateAluno = "UPDATE aluno SET ra=?, nome=?, dataNascimento=?, cpf=?, email=?, endereco=?, municipio=?, uf=?, celular=?, ativo=? WHERE idAluno=?";
            psUpdateAluno = conn.prepareStatement(sqlUpdateAluno);
            psUpdateAluno.setString(1, ra);
            psUpdateAluno.setString(2, nome);
            psUpdateAluno.setString(3, dataNascimentoSQL != null ? dataNascimentoSQL.toString() : null);
            psUpdateAluno.setString(4, cpf);
            psUpdateAluno.setString(5, email);
            psUpdateAluno.setString(6, endereco);
            psUpdateAluno.setString(7, municipio);
            psUpdateAluno.setString(8, uf);
            psUpdateAluno.setString(9, celular);
            psUpdateAluno.setBoolean(10, true);
            psUpdateAluno.setInt(11, idAluno);
            psUpdateAluno.executeUpdate();

            // --- 2. Atualizar matrícula (troca de curso) ---
            int idCurso = cursoSelecionado.getIdCurso();
            int idMatricula = new AlunoDAO().buscarIdMatricula(idAluno); 
            if (idMatricula > 0) {
                String sqlUpdateMatricula = "UPDATE matricula SET idCurso=? WHERE idMatricula=?";
                psUpdateMatricula = conn.prepareStatement(sqlUpdateMatricula);
                psUpdateMatricula.setInt(1, idCurso);
                psUpdateMatricula.setInt(2, idMatricula);
                psUpdateMatricula.executeUpdate();
            }

            // --- 3. Atualizar disciplinas da matrícula ---
            String semestreAtual = calcularSemestreAtual(); // seu método existente
            String sqlSelectDisciplinas = "SELECT idDisciplina FROM disciplina WHERE idCurso=? AND semestre=1"; // assume semestre 1
            psSelectDisciplinas = conn.prepareStatement(sqlSelectDisciplinas);
            psSelectDisciplinas.setInt(1, idCurso);
            rsDisciplinas = psSelectDisciplinas.executeQuery();

            ArrayList<Integer> idsDisciplinas = new ArrayList<>();
            while (rsDisciplinas.next()) {
                idsDisciplinas.add(rsDisciplinas.getInt("idDisciplina"));
            }

            if (!idsDisciplinas.isEmpty()) {
                String sqlInsertMatDisc = "INSERT IGNORE INTO matriculaDisciplina (idMatricula, idDisciplina, semestreCursado, faltas, nota) VALUES (?, ?, ?, 0, 0.0)";
                psInsertMatDisc = conn.prepareStatement(sqlInsertMatDisc);
                for (int idDisc : idsDisciplinas) {
                    psInsertMatDisc.setInt(1, idMatricula);
                    psInsertMatDisc.setInt(2, idDisc);
                    psInsertMatDisc.setString(3, semestreAtual);
                    psInsertMatDisc.addBatch();
                }
                psInsertMatDisc.executeBatch();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Aluno e matrícula atualizados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            try { if (conn != null) conn.rollback(); } catch (Exception exRollback) {}
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao atualizar aluno: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rsDisciplinas != null) rsDisciplinas.close(); } catch (Exception ex) {}
            try { if (psUpdateAluno != null) psUpdateAluno.close(); } catch (Exception ex) {}
            try { if (psUpdateMatricula != null) psUpdateMatricula.close(); } catch (Exception ex) {}
            try { if (psSelectDisciplinas != null) psSelectDisciplinas.close(); } catch (Exception ex) {}
            try { if (psInsertMatDisc != null) psInsertMatDisc.close(); } catch (Exception ex) {}
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (Exception ex) {}
        }
    }


    
    //carrega os dados do aluno selecionado
    private void carregarDadosAluno(int idAluno) {
        try {
            AlunoDAO alunoDAO = new AlunoDAO();
            Aluno aluno = alunoDAO.buscarPorId(idAluno);

            if (aluno != null) {
                // Preencher dados básicos
                txtRa.setText(aluno.getRa());
                txtNome.setText(aluno.getNome());
                
                String dataDoBanco = aluno.getDataNascimento(); // exemplo: "2000-05-21"
                if (dataDoBanco != null && !dataDoBanco.isEmpty()) {
                    // Converte de "yyyy-MM-dd" para LocalDate
                    LocalDate data = LocalDate.parse(dataDoBanco); // por padrão usa ISO "yyyy-MM-dd"
                    
                    // Formata para "dd/MM/yyyy" para o campo JFormattedTextField
                    DateTimeFormatter formatoBrasileiro = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    txtDataNasc.setText(data.format(formatoBrasileiro));
                }


                txtCpf.setText(aluno.getCpf());
                txtEmail.setText(aluno.getEmail());
                txtEndereco.setText(aluno.getEndereco());
                txtMunicipio.setText(aluno.getMunicipio());
                txtCelular.setText(aluno.getCelular());
                jcbUf.setSelectedItem(aluno.getUf());

                // --- Buscar matrícula ativa e curso ---
                int idMatricula = alunoDAO.buscarIdMatricula(idAluno);
                if (idMatricula > 0) {
                    Matricula matricula = alunoDAO.buscarMatriculaPorId(idMatricula); // implementar método para retornar Matricula completa
                    if (matricula != null) {
                        int idCurso = matricula.getIdCurso();

                        // Percorrer JComboBox e selecionar o curso correspondente
                        for (int i = 0; i < jcbCursos.getItemCount(); i++) {
                            Curso c = (Curso) jcbCursos.getItemAt(i);
                            if (c.getIdCurso() == idCurso) {
                                jcbCursos.setSelectedIndex(i);
                                break;
                            }
                        }
                    }
                }

                // Ajustar botões
                btnRegistrar.setVisible(false);
                btnSalvar.setVisible(true);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar dados do aluno: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    
    /**
     * Método auxiliar para limpar todos os campos do formulário
     * após um cadastro bem-sucedido.
     */
    private void limparCampos() {
        txtRa.setText(""); 
        txtNome.setText("");
        txtDataNasc.setValue(null); // Limpa o campo formatado
        txtCpf.setValue(null);
        txtEmail.setText("");
        txtEndereco.setText("");
        txtMunicipio.setText("");
        txtCelular.setValue(null);
        jcbUf.setSelectedIndex(0);
        jcbCursos.setSelectedIndex(0);
    }


    /**
     * Lógica para voltar para o painel de Listagem de Alunos e reativar o foco no menu.
     */
    private void voltarParaListagem() {
        if (framePai instanceof TelaPrincipal) {
            TelaPrincipal telaPrincipal = (TelaPrincipal) framePai;
            ListarAlunos painelListagem = new ListarAlunos(telaPrincipal);
            telaPrincipal.trocarPainelConteudo(painelListagem);
            telaPrincipal.ativarBotaoMenuListarAlunos(); 
        } 
    }
    
    // Método para calcular o semestre atual no formato "AAAA/S"
    private String calcularSemestreAtual() {
        java.time.LocalDate hoje = java.time.LocalDate.now();
        int ano = hoje.getYear();
        int semestre = (hoje.getMonthValue() <= 6) ? 1 : 2;
        return ano + "/" + semestre;
    }
    
    /**
     * Método público para salvar aluno - chamado pelo menu
     */
    public void salvarAlunoSelecionado() {
        if (idAluno > 0) {
            // Modo edição
            System.out.println("Salvando alterações do aluno ID: " + idAluno);
            java.awt.event.ActionEvent evento = 
                new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_PERFORMED, "");
            salvarAlteracoes(evento);
        } else {
            // Modo cadastro  
            System.out.println("Registrando novo aluno");
            java.awt.event.ActionEvent evento = 
                new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_PERFORMED, "");
            registrarAluno(evento);
        }
    }

    
    
    
}
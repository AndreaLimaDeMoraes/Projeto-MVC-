package br.edu.fatecgru.mvcaluno.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import br.edu.fatecgru.mvcaluno.dao.CursoDAO;
import br.edu.fatecgru.mvcaluno.model.Curso;

public class TelaCurso extends JPanel {
    private CursoDAO cursoDAO;
    
    // Componentes do formulário superior
    private JTextField txtNome;
    private JTextField txtDuracao;
    private JComboBox<String> cbCampus;
    private JRadioButton rdMatutino, rdVespertino, rdNoturno;
    private JButton btnNovoCurso; // BOTÃO MANTIDO
    
    // Componentes da lista
    private JList<String> listCursos;
    private DefaultListModel<String> listModel;
    
    private List<Curso> cursosLista; // Para guardar os cursos reais
    
    // Botões de ação (como na tela de disciplinas)
    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnCancelar;
    private JButton btnExcluir;
    private JPanel pnlBotoesAcao;
    
    private Curso cursoSelecionado;
    private boolean modoEdicao = false;
    
    // Construtores
    public TelaCurso(JFrame framePai, int idCurso) {
        this(); // Chama o construtor sem parâmetros
    }
    
    public TelaCurso() {
        try {
            cursoDAO = new CursoDAO();
            initialize();
            carregarCursos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar com o banco: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initialize() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ===== PAINEL SUPERIOR - FORMULÁRIO HORIZONTAL =====
        JPanel pnlFormulario = criarPainelFormulario();
        add(pnlFormulario, BorderLayout.NORTH);
        
        // ===== PAINEL CENTRAL - LISTA DE CURSOS =====
        JPanel pnlLista = criarPainelLista();
        add(pnlLista, BorderLayout.CENTER);
        
        // ===== PAINEL INFERIOR - BOTÕES DE AÇÃO (INICIALMENTE OCULTOS) =====
        pnlBotoesAcao = criarPainelBotoesAcao();
        add(pnlBotoesAcao, BorderLayout.SOUTH);
        pnlBotoesAcao.setVisible(false);
    }
    
    private JPanel criarPainelFormulario() {
        JPanel pnlFormulario = new JPanel();
        pnlFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Cadastrar Novo Curso", 
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Tahoma", Font.BOLD, 12)
        ));
        
        // Nome do Curso
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(10, 31, 43, 20);
        lblNome.setFont(new Font("Dialog", Font.PLAIN, 12));
        txtNome = new JTextField(15);
        txtNome.setBounds(49, 29, 201, 25);
        txtNome.setFont(new Font("Dialog", Font.PLAIN, 12));
        txtNome.setPreferredSize(new Dimension(150, 25));
        pnlFormulario.setLayout(null);
        pnlFormulario.add(lblNome);
        pnlFormulario.add(txtNome);
        pnlFormulario.setPreferredSize(new Dimension(950, 75));

        
        // Campus
        JLabel lblCampus = new JLabel("Campus:");
        lblCampus.setBounds(256, 31, 60, 20);
        lblCampus.setFont(new Font("Dialog", Font.PLAIN, 12));
        cbCampus = new JComboBox<>(new String[]{"São Paulo", "Guarulhos", "Diadema","Tatuapé", "Ipiranga","Jundiaí","São Caetano", "Paulista", "Vila Mariana", "Santo Amaro"});
        cbCampus.setBounds(310, 28, 120, 25);
        cbCampus.setFont(new Font("Dialog", Font.PLAIN, 12));
        cbCampus.setPreferredSize(new Dimension(120, 25));
        pnlFormulario.add(lblCampus);
        pnlFormulario.add(cbCampus);
        
        // Período
        JLabel lblPeriodo = new JLabel("Período:");
        lblPeriodo.setBounds(440, 29, 54, 20);
        lblPeriodo.setFont(new Font("Dialog", Font.PLAIN, 12));
        JPanel pnlPeriodo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlPeriodo.setBounds(486, 27, 159, 29);
        ButtonGroup grupoPeriodo = new ButtonGroup();
        
        rdMatutino = new JRadioButton("Mat");
        rdMatutino.setFont(new Font("Dialog", Font.PLAIN, 12));
        rdVespertino = new JRadioButton("Ves");
        rdVespertino.setFont(new Font("Dialog", Font.PLAIN, 12));
        rdNoturno = new JRadioButton("Not");
        rdNoturno.setFont(new Font("Dialog", Font.PLAIN, 12));
        
        grupoPeriodo.add(rdMatutino);
        grupoPeriodo.add(rdVespertino);
        grupoPeriodo.add(rdNoturno);
        rdNoturno.setSelected(true);
        
        pnlPeriodo.add(rdMatutino);
        pnlPeriodo.add(rdVespertino);
        pnlPeriodo.add(rdNoturno);
        
        pnlFormulario.add(lblPeriodo);
        pnlFormulario.add(pnlPeriodo);
        
        // Duração
        JLabel lblDuracao = new JLabel("Duração (Semestres):");
        lblDuracao.setBounds(650, 29, 132, 20);
        lblDuracao.setFont(new Font("Dialog", Font.PLAIN, 12));
        txtDuracao = new JTextField(3);
        txtDuracao.setBounds(775, 27, 45, 25);
        txtDuracao.setFont(new Font("Dialog", Font.PLAIN, 12));
        txtDuracao.setPreferredSize(new Dimension(40, 25));
        pnlFormulario.add(lblDuracao);
        pnlFormulario.add(txtDuracao);
        
        // ===== BOTÃO NOVO CURSO  =====
        btnNovoCurso = new JButton(" Novo");
        btnNovoCurso.setBounds(826, 20, 102, 40);
        btnNovoCurso.setIcon(new ImageIcon(TelaCurso.class.getResource("/Resources/imagens/novo-documento.png")));
        btnNovoCurso.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnNovoCurso.setBackground(new Color(255, 255, 255));
        btnNovoCurso.setForeground(new Color(0, 0, 0));
        btnNovoCurso.setFocusPainted(false);
        btnNovoCurso.setPreferredSize(new Dimension(120, 30));
        btnNovoCurso.addActionListener(e -> salvarCurso()); // Chama o mesmo método do menu
        
        pnlFormulario.add(btnNovoCurso);
        
        return pnlFormulario;
    }
    
    private JPanel criarPainelLista() {
        JPanel pnlLista = new JPanel(new BorderLayout());
        pnlLista.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Cursos Cadastrados", 
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Tahoma", Font.BOLD, 12)
        ));
        
        // Model e Lista
        listModel = new DefaultListModel<>();
        listCursos = new JList<>(listModel);
        listCursos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listCursos.setFont(new Font("Tahoma", Font.PLAIN, 12));
        listCursos.setBackground(Color.WHITE);
        
        // Adiciona o listener para duplo clique
        listCursos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Duplo clique
                    int index = listCursos.locationToIndex(e.getPoint());
                    if (index >= 0 && index < cursosLista.size()) {
                        cursoSelecionado = cursosLista.get(index);
                        preencherFormularioComCurso(cursoSelecionado);
                        modoEdicao = true;
                        pnlBotoesAcao.setVisible(true);
                        
                        // Desabilita o formulário superior para edição direta
                        txtNome.setEnabled(false);
                        txtDuracao.setEnabled(false);
                        cbCampus.setEnabled(false);
                        rdMatutino.setEnabled(false);
                        rdVespertino.setEnabled(false);
                        rdNoturno.setEnabled(false);
                        btnNovoCurso.setEnabled(false);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(listCursos);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        pnlLista.add(scrollPane, BorderLayout.CENTER);
        
        return pnlLista;
    }
    
    private JPanel criarPainelBotoesAcao() {
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlBotoes.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Editor Curso", 
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Tahoma", Font.BOLD, 12)
        ));
        
        // Botão Salvar
        btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnSalvar.setIcon(new ImageIcon(TelaCurso.class.getResource("/Resources/imagens/salve-.png")));
        btnSalvar.addActionListener(e -> salvarAlteracoes());
        
        // Botão Alterar
        btnAlterar = new JButton("Alterar");
        btnAlterar.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnAlterar.setIcon(new ImageIcon(TelaCurso.class.getResource("/Resources/imagens/editarCurso.png")));

        btnAlterar.addActionListener(e -> habilitarEdicao());
        
        // Botão Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnCancelar.setIcon(new ImageIcon(TelaCurso.class.getResource("/Resources/imagens/cancelarAction.png")));
        btnCancelar.addActionListener(e -> cancelarEdicao());
        
        // Botão Excluir
        btnExcluir = new JButton("Excluir");
        btnExcluir.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnExcluir.setIcon(new ImageIcon(TelaCurso.class.getResource("/Resources/imagens/excluirCurso.png")));
        btnExcluir.addActionListener(e -> excluirCursoSelecionado());
        
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnAlterar);
        pnlBotoes.add(btnCancelar);
        pnlBotoes.add(btnExcluir);
        
        return pnlBotoes;
    }
    
    private void preencherFormularioComCurso(Curso curso) {
        txtNome.setText(curso.getNome());
        txtDuracao.setText(String.valueOf(curso.getDuracao()));
        cbCampus.setSelectedItem(curso.getCampus());
        
        // Define o período selecionado
        rdMatutino.setSelected(false);
        rdVespertino.setSelected(false);
        rdNoturno.setSelected(false);
        
        switch(curso.getPeriodo()) {
            case "Matutino": rdMatutino.setSelected(true); break;
            case "Vespertino": rdVespertino.setSelected(true); break;
            case "Noturno": rdNoturno.setSelected(true); break;
        }
    }
    
    private void salvarAlteracoes() {
        if (cursoSelecionado == null) return;
        
        try {
            if (validarCampos()) {
                cursoSelecionado.setNome(txtNome.getText().trim());
                cursoSelecionado.setCampus(cbCampus.getSelectedItem().toString());
                cursoSelecionado.setPeriodo(getPeriodoSelecionado());
                cursoSelecionado.setDuracao(Integer.parseInt(txtDuracao.getText().trim()));
                
                cursoDAO.atualizar(cursoSelecionado);
                
                JOptionPane.showMessageDialog(this, "Curso alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                cancelarEdicao();
                carregarCursos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar alterações: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void habilitarEdicao() {
        txtNome.setEnabled(true);
        txtDuracao.setEnabled(true);
        cbCampus.setEnabled(true);
        rdMatutino.setEnabled(true);
        rdVespertino.setEnabled(true);
        rdNoturno.setEnabled(true);
        
        btnSalvar.setEnabled(true);
        btnAlterar.setEnabled(false);
    }
    
    private void cancelarEdicao() {
        modoEdicao = false;
        cursoSelecionado = null;
        pnlBotoesAcao.setVisible(false);
        
        // Reabilita o formulário superior
        txtNome.setEnabled(true);
        txtDuracao.setEnabled(true);
        cbCampus.setEnabled(true);
        rdMatutino.setEnabled(true);
        rdVespertino.setEnabled(true);
        rdNoturno.setEnabled(true);
        btnNovoCurso.setEnabled(true);
        
        // Reseta os botões
        btnSalvar.setEnabled(true);
        btnAlterar.setEnabled(true);
        
        limparFormulario();
    }
    
    private void excluirCursoSelecionado() {
        if (cursoSelecionado == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja excluir o curso:\n" + 
            cursoSelecionado.getNome() + " - " + cursoSelecionado.getCampus() + "?", 
            "Confirmação", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                cursoDAO.excluir(cursoSelecionado.getIdCurso());
                JOptionPane.showMessageDialog(this, "Curso excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                cancelarEdicao();
                carregarCursos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir curso: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void excluirCursosSelecionados() {
        int[] selectedIndices = listCursos.getSelectedIndices();
        if (selectedIndices.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um ou mais cursos para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Verificar se há mais de um curso selecionado
        if (selectedIndices.length == 1) {
            excluirCursoSelecionado();
            return;
        }
        
        // Para múltiplos cursos
        StringBuilder cursosParaExcluir = new StringBuilder();
        cursosParaExcluir.append("Tem certeza que deseja excluir os seguintes cursos?\n\n");
        
        for (int index : selectedIndices) {
            if (index >= 0 && index < cursosLista.size()) {
                Curso curso = cursosLista.get(index);
                cursosParaExcluir.append("• ").append(curso.getNome())
                               .append(" - ").append(curso.getCampus())
                               .append("\n");
            }
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            cursosParaExcluir.toString(), 
            "Confirmação de Exclusão Múltipla", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int excluidos = 0;
                for (int index : selectedIndices) {
                    if (index >= 0 && index < cursosLista.size()) {
                        Curso curso = cursosLista.get(index);
                        cursoDAO.excluir(curso.getIdCurso());
                        excluidos++;
                    }
                }
                
                JOptionPane.showMessageDialog(this, 
                    excluidos + " curso(s) excluído(s) com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                if (modoEdicao) {
                    cancelarEdicao();
                }
                carregarCursos();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao excluir cursos: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void excluirTodosCursos() {
        if (cursosLista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há cursos para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja excluir TODOS os " + cursosLista.size() + " cursos?\n\n" +
            "Esta ação não pode ser desfeita!", 
            "Confirmação de Exclusão Total", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int totalCursos = cursosLista.size();
                int excluidos = 0;
                
                for (Curso curso : cursosLista) {
                    cursoDAO.excluir(curso.getIdCurso());
                    excluidos++;
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Todos os " + excluidos + " cursos foram excluídos com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                if (modoEdicao) {
                    cancelarEdicao();
                }
                carregarCursos();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao excluir todos os cursos: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
// ===== MÉTODOS PÚBLICOS PARA O MENU DE CURSOS =====
    
    public void salvarCurso() {
        try {
            if (validarCampos()) {
                Curso curso = new Curso();
                curso.setNome(txtNome.getText().trim());
                curso.setCampus(cbCampus.getSelectedItem().toString());
                curso.setPeriodo(getPeriodoSelecionado());
                curso.setDuracao(Integer.parseInt(txtDuracao.getText().trim()));
                curso.setAtivo(true);
                
                cursoDAO.salvar(curso);
                
                JOptionPane.showMessageDialog(this, "Curso salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                carregarCursos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar curso: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public void consultarCurso() {
        try {
            String filtro = txtNome.getText().trim();
 
            if (filtro.isEmpty()) {
                cursosLista = cursoDAO.listarTodos();
            } else {
                cursosLista = cursoDAO.listarPorFiltro(filtro);
            }
 
            atualizarListaCursos();
 
            if (cursosLista.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Nenhum curso encontrado com o filtro informado.",
                    "Consulta", JOptionPane.INFORMATION_MESSAGE);
            }
 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao consultar cursos: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
 
    private void atualizarListaCursos() {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Curso c : cursosLista) {
            model.addElement(c.getNome() + " - " + c.getCampus() + " (" + c.getPeriodo() + ")");
        }
        listCursos.setModel(model);
    }
 
    
    
    public void alterarCurso() {
        int selectedIndex = listCursos.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < cursosLista.size()) {
            Curso cursoSelecionado = cursosLista.get(selectedIndex);
            abrirFormularioAlteracao(cursoSelecionado);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um curso para alterar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void excluirCurso() {
        int selectedIndex = listCursos.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < cursosLista.size()) {
            Curso cursoSelecionado = cursosLista.get(selectedIndex);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o curso:\n" +
                cursoSelecionado.getNome() + " - " + cursoSelecionado.getCampus() + "?",
                "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    cursoDAO.excluir(cursoSelecionado.getIdCurso());
                    JOptionPane.showMessageDialog(this, "Curso excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarCursos();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir curso: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um curso para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void limparCampos() {
        limparFormulario();
    }
    
    // ===== MÉTODOS PRIVADOS =====
    
    private void abrirFormularioAlteracao(Curso curso) {
        // Cria um painel para o formulário de edição
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        
        // Campo Nome
        panel.add(new JLabel("Nome do Curso:"));
        JTextField txtNome = new JTextField(curso.getNome());
        panel.add(txtNome);
        
        // Campo Campus  
        panel.add(new JLabel("Campus:"));
        JComboBox<String> cbCampus = new JComboBox<>(new String[]{"São Paulo","Guarulhos","Diadema","Tatuapé", "Ipiranga","Jundiaí","São Caetano", "Paulista", "Vila Mariana", "Santo Amaro"});
        cbCampus.setSelectedItem(curso.getCampus());
        panel.add(cbCampus);
        
        // Campo Período (COM OS TRÊS RADIO BUTTONS)
        panel.add(new JLabel("Período:"));
        JPanel panelPeriodo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        ButtonGroup grupoPeriodo = new ButtonGroup();
        
        JRadioButton rdMatutino = new JRadioButton("Matutino");
        JRadioButton rdVespertino = new JRadioButton("Vespertino"); 
        JRadioButton rdNoturno = new JRadioButton("Noturno");
        
        grupoPeriodo.add(rdMatutino);
        grupoPeriodo.add(rdVespertino);
        grupoPeriodo.add(rdNoturno);
        
        // Seleciona o período atual do curso
        switch(curso.getPeriodo()) {
            case "Matutino": rdMatutino.setSelected(true); break;
            case "Vespertino": rdVespertino.setSelected(true); break;
            case "Noturno": rdNoturno.setSelected(true); break;
        }
        
        panelPeriodo.add(rdMatutino);
        panelPeriodo.add(rdVespertino);
        panelPeriodo.add(rdNoturno);
        panel.add(panelPeriodo);
        
        // Campo Duração
        panel.add(new JLabel("Duração (semestres):"));
        JTextField txtDuracao = new JTextField(String.valueOf(curso.getDuracao()));
        panel.add(txtDuracao);
        
        // Mostra o diálogo de confirmação
        int result = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Editar Curso", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );
        
        // Se clicou em OK, salva as alterações
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Validações
                if (txtNome.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nome do curso é obrigatório!");
                    return;
                }
                
                int duracao;
                try {
                    duracao = Integer.parseInt(txtDuracao.getText().trim());
                    if (duracao < 2) {
                        JOptionPane.showMessageDialog(this, "Duração deve ser maior que um semestre!");
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Duração deve ser um número válido!");
                    return;
                }
                
                // Obtém o período selecionado
                String periodoSelecionado = "Noturno"; // padrão
                if (rdMatutino.isSelected()) periodoSelecionado = "Matutino";
                else if (rdVespertino.isSelected()) periodoSelecionado = "Vespertino";
                else if (rdNoturno.isSelected()) periodoSelecionado = "Noturno";
                
                // Atualiza o curso
                curso.setNome(txtNome.getText().trim());
                curso.setCampus(cbCampus.getSelectedItem().toString());
                curso.setPeriodo(periodoSelecionado);
                curso.setDuracao(duracao);
                
                // Salva no banco
                cursoDAO.atualizar(curso);
                
                JOptionPane.showMessageDialog(this, "Curso alterado com sucesso!");
                carregarCursos();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao alterar curso: " + e.getMessage());
            }
        }
    }
    
    void carregarCursos() {
        try {
            listModel.clear();
            cursosLista = cursoDAO.listarTodos();
            
            if (cursosLista.isEmpty()) {
                listModel.addElement("Nenhum curso cadastrado");
            } else {
                for (Curso curso : cursosLista) {
                    String cursoFormatado = String.format("%s - %s (%s) - %d semestres", 
                        curso.getNome(), curso.getCampus(), curso.getPeriodo(), curso.getDuracao());
                    listModel.addElement(cursoFormatado);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cursos: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do curso!", "Validação", JOptionPane.WARNING_MESSAGE);
            txtNome.requestFocus();
            return false;
        }
        
        if (txtDuracao.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a duração!", "Validação", JOptionPane.WARNING_MESSAGE);
            txtDuracao.requestFocus();
            return false;
        }
        
        try {
            int duracao = Integer.parseInt(txtDuracao.getText().trim());
            if (duracao < 2) {
                JOptionPane.showMessageDialog(this, "Duração deve ser maior que um!", "Validação", JOptionPane.WARNING_MESSAGE);
                txtDuracao.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Duração deve ser um número válido!", "Validação", JOptionPane.WARNING_MESSAGE);
            txtDuracao.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private String getPeriodoSelecionado() {
        if (rdMatutino.isSelected()) return "Matutino";
        if (rdVespertino.isSelected()) return "Vespertino";
        return "Noturno";
    }
    
    private void limparFormulario() {
        txtNome.setText("");
        txtDuracao.setText("");
        cbCampus.setSelectedIndex(0);
        rdNoturno.setSelected(true);
        txtNome.requestFocus();
    }
    
    public void atualizarLista() {
        carregarCursos();
    }
}
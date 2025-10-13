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
    }
    
    private JPanel criarPainelFormulario() {
        JPanel pnlFormulario = new JPanel();
        pnlFormulario.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Cadastrar Novo Curso", 
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Tahoma", Font.BOLD, 12)
        ));
        
        // Nome do Curso
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtNome = new JTextField(15);
        txtNome.setPreferredSize(new Dimension(150, 25));
        pnlFormulario.add(lblNome);
        pnlFormulario.add(txtNome);
        
        // Campus
        JLabel lblCampus = new JLabel("Campus:");
        lblCampus.setFont(new Font("Tahoma", Font.PLAIN, 12));
        cbCampus = new JComboBox<>(new String[]{"Tatuapé", "Paulista", "Vila Mariana", "Santo Amaro"});
        cbCampus.setPreferredSize(new Dimension(120, 25));
        pnlFormulario.add(lblCampus);
        pnlFormulario.add(cbCampus);
        
        // Período
        JLabel lblPeriodo = new JLabel("Período:");
        lblPeriodo.setFont(new Font("Tahoma", Font.PLAIN, 12));
        JPanel pnlPeriodo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        ButtonGroup grupoPeriodo = new ButtonGroup();
        
        rdMatutino = new JRadioButton("Mat");
        rdMatutino.setFont(new Font("Tahoma", Font.PLAIN, 11));
        rdVespertino = new JRadioButton("Ves");
        rdVespertino.setFont(new Font("Tahoma", Font.PLAIN, 11));
        rdNoturno = new JRadioButton("Not");
        rdNoturno.setFont(new Font("Tahoma", Font.PLAIN, 11));
        
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
        JLabel lblDuracao = new JLabel("Duração:");
        lblDuracao.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtDuracao = new JTextField(3);
        txtDuracao.setPreferredSize(new Dimension(40, 25));
        JLabel lblSemestres = new JLabel("semestres");
        lblSemestres.setFont(new Font("Tahoma", Font.PLAIN, 11));
        pnlFormulario.add(lblDuracao);
        pnlFormulario.add(txtDuracao);
        pnlFormulario.add(lblSemestres);
        
        // ===== BOTÃO NOVO CURSO MANTIDO =====
        btnNovoCurso = new JButton("Novo Curso");
        btnNovoCurso.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnNovoCurso.setBackground(new Color(54, 70, 78));
        btnNovoCurso.setForeground(Color.WHITE);
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
        listCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listCursos.setFont(new Font("Tahoma", Font.PLAIN, 12));
        listCursos.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(listCursos);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        pnlLista.add(scrollPane, BorderLayout.CENTER);
        
        return pnlLista;
    }
    
    // ===== MÉTODOS PÚBLICOS PARA O MENU DA TELAPRINCIPAL =====
    
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
        JComboBox<String> cbCampus = new JComboBox<>(new String[]{"Tatuapé", "Paulista", "Vila Mariana", "Santo Amaro"});
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
                    if (duracao <= 0) {
                        JOptionPane.showMessageDialog(this, "Duração deve ser maior que zero!");
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
            if (duracao <= 0) {
                JOptionPane.showMessageDialog(this, "Duração deve ser maior que zero!", "Validação", JOptionPane.WARNING_MESSAGE);
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
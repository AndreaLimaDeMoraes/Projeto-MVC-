package br.edu.fatecgru.mvcaluno.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import br.edu.fatecgru.mvcaluno.dao.CursoDAO;
import br.edu.fatecgru.mvcaluno.dao.DisciplinaDAO;
import br.edu.fatecgru.mvcaluno.model.Curso;
import br.edu.fatecgru.mvcaluno.model.Disciplina;

public class TelaDisciplina extends JPanel {

    private static final long serialVersionUID = 1L;
    private JComboBox<String> cmbCurso;
    private JTextField txtDisciplina;
    private JComboBox<String> cmbSemestre;
    private JList<String> listDisciplina;
    private JScrollPane scrollPane;

    // Painel de edição
    private JPanel panelEdicao;
    private JTextField txtEditarDisciplina;
    private JComboBox<String> cmbEditarSemestre;
    private JButton btnSalvarEdicao, btnCancelarEdicao, btnExcluirEdicao;
    private Disciplina disciplinaEmEdicao;

    public TelaDisciplina() {
        setLayout(null);
        setBackground(UIManager.getColor("Button.background"));

        // Painel principal
        JPanel panel = new JPanel();
        panel.setBounds(20, 20, 893, 90);
        panel.setBorder(new TitledBorder(null, "Cadastrar Nova Disciplina",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Tahoma", Font.BOLD, 12),
                new Color(54, 70, 78)));
        panel.setLayout(null);
        add(panel);

        // Label Curso
        JLabel lblCurso = new JLabel("Curso:");
        lblCurso.setBounds(10, 39, 50, 14);
        panel.add(lblCurso);

        // ComboBox Curso
        cmbCurso = new JComboBox<>();
        cmbCurso.setEditable(false);
        cmbCurso.setBounds(49, 34, 258, 24);
        panel.add(cmbCurso);

        // Label Nome
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(317, 38, 45, 14);
        panel.add(lblNome);

        // Campo Nome da disciplina
        txtDisciplina = new JTextField();
        txtDisciplina.setBounds(360, 34, 200, 24);
        panel.add(txtDisciplina);
        txtDisciplina.setColumns(10);

        // Label Semestre
        JLabel lblSemestre = new JLabel("Semestre:");
        lblSemestre.setBounds(580, 38, 70, 14);
        panel.add(lblSemestre);

        // ComboBox Semestre
        cmbSemestre = new JComboBox<>();
        cmbSemestre.setEditable(false);
        cmbSemestre.setBounds(650, 34, 80, 24);
        panel.add(cmbSemestre);

        // Botão Nova Disciplina
        JButton btnNovaDisciplina = new JButton("Nova Disciplina");
        btnNovaDisciplina.setBounds(750, 33, 133, 26);
        btnNovaDisciplina.setPreferredSize(new Dimension(120, 30));
        btnNovaDisciplina.setForeground(Color.WHITE);
        btnNovaDisciplina.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnNovaDisciplina.setFocusPainted(false);
        btnNovaDisciplina.setBackground(new Color(54, 70, 78));
        panel.add(btnNovaDisciplina);

        // Painel de lista
        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBorder(new TitledBorder(null, "Disciplinas Cadastradas",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Tahoma", Font.BOLD, 12),
                new Color(54, 70, 78)));
        panel_1.setBounds(20, 121, 893, 209);
        add(panel_1);

        DefaultListModel<String> modelo = new DefaultListModel<>();
        listDisciplina = new JList<>(modelo);
        listDisciplina.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listDisciplina.setFont(new Font("Tahoma", Font.PLAIN, 12));
        listDisciplina.setBackground(Color.WHITE);

        // ScrollPane da lista
        scrollPane = new JScrollPane(listDisciplina);
        scrollPane.setBounds(10, 24, 873, 174);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel_1.add(scrollPane);

        // Inicializar painel de edição
        inicializarPainelEdicao();

        // ---------- Inicialização ----------
        carregarCursosNome();

        // Quando mudar o curso, atualiza lista e semestres
        cmbCurso.addActionListener(e -> {
            try {
                filtrarDisciplinasPorCurso();
                atualizarSemestresDoCurso();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao filtrar disciplinas: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Ao clicar em "Nova Disciplina"
        btnNovaDisciplina.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    salvarDisciplina();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao salvar disciplina: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        // Duplo clique na disciplina para editar
        listDisciplina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = listDisciplina.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        String itemSelecionado = listDisciplina.getModel().getElementAt(index);
                        abrirEdicaoNaTela(itemSelecionado);
                    }
                }
            }
        });

        // Já começa filtrando automaticamente pelo primeiro curso
        if (cmbCurso.getItemCount() > 0) {
            cmbCurso.setSelectedIndex(0);
            try {
                filtrarDisciplinasPorCurso();
                atualizarSemestresDoCurso();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // -------- Métodos auxiliares --------

    private void carregarCursosNome() {
        try {
            CursoDAO dao = new CursoDAO();
            List<Curso> listaCursos = dao.listarTodos();

            cmbCurso.removeAllItems();
            cmbCurso.addItem("Todos os Cursos"); // Opção especial

            for (Curso curso : listaCursos) {
                cmbCurso.addItem(curso.getNome());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cursos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filtrarDisciplinasPorCurso() throws Exception {
        String cursoSelecionado = (String) cmbCurso.getSelectedItem();
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        CursoDAO cursoDAO = new CursoDAO();

        List<Disciplina> listaDisciplina = disciplinaDAO.listarTodos();
        DefaultListModel<String> modelo = new DefaultListModel<>();

        if (cursoSelecionado == null || cursoSelecionado.equals("Todos os Cursos")) {
            for (Disciplina d : listaDisciplina) {
                Curso curso = cursoDAO.buscarPorId(d.getIdCurso());
                String nomeCurso = (curso != null) ? curso.getNome() : "Curso desconhecido";
                modelo.addElement(d.getNome() + " – " + nomeCurso + " – " + d.getSemestre() + "º Semestre");
            }
            listDisciplina.setModel(modelo);
            return;
        }

        Curso cursoSelecionadoObj = null;
        for (Curso c : cursoDAO.listarTodos()) {
            if (c.getNome().equals(cursoSelecionado)) {
                cursoSelecionadoObj = c;
                break;
            }
        }

        if (cursoSelecionadoObj == null) return;

        for (Disciplina d : listaDisciplina) {
            if (d.getIdCurso() == cursoSelecionadoObj.getIdCurso()) {
                modelo.addElement(d.getNome() + " – " + d.getSemestre() + "º Semestre");
            }
        }
        listDisciplina.setModel(modelo);
    }

    private void atualizarSemestresDoCurso() throws Exception {
        String cursoSelecionado = (String) cmbCurso.getSelectedItem();
        cmbSemestre.removeAllItems();

        if (cursoSelecionado == null || cursoSelecionado.equals("Todos os Cursos")) {
            for (int i = 1; i <= 10; i++) {
                cmbSemestre.addItem(i + "");
            }
            return;
        }

        CursoDAO cursoDAO = new CursoDAO();
        List<Curso> listaCursos = cursoDAO.listarTodos();

        for (Curso c : listaCursos) {
            if (c.getNome().equals(cursoSelecionado)) {
                for (int i = 1; i <= c.getDuracao(); i++) {
                    cmbSemestre.addItem(i + "");
                }
                break;
            }
        }
    }

    // Salvar nova disciplina
    private void salvarDisciplina() throws Exception {
        String nomeDisciplina = txtDisciplina.getText().trim();
        String cursoSelecionado = (String) cmbCurso.getSelectedItem();
        String semestreStr = (String) cmbSemestre.getSelectedItem();

        if (nomeDisciplina.isEmpty() || cursoSelecionado == null || semestreStr == null) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        if (cursoSelecionado.equals("Todos os Cursos")) {
            JOptionPane.showMessageDialog(this, "Selecione um curso específico para cadastrar a disciplina!");
            return;
        }

        CursoDAO cursoDAO = new CursoDAO();
        Curso curso = null;
        for (Curso c : cursoDAO.listarTodos()) {
            if (c.getNome().equals(cursoSelecionado)) {
                curso = c;
                break;
            }
        }

        if (curso == null) {
            JOptionPane.showMessageDialog(this, "Curso não encontrado!");
            return;
        }

        Disciplina disciplina = new Disciplina();
        disciplina.setIdCurso(curso.getIdCurso());
        disciplina.setNome(nomeDisciplina);
        disciplina.setSemestre(Integer.parseInt(semestreStr));
        disciplina.setAtivo(true);

        DisciplinaDAO dao = new DisciplinaDAO();
        dao.salvar(disciplina);

        JOptionPane.showMessageDialog(this, "Disciplina cadastrada com sucesso!");

        txtDisciplina.setText("");
        filtrarDisciplinasPorCurso();
    }

    // -------- Painel de edição na própria tela --------
    private void inicializarPainelEdicao() {
        panelEdicao = new JPanel();
        panelEdicao.setLayout(null);
        panelEdicao.setBorder(new TitledBorder(null, "Editar Disciplina",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Tahoma", Font.BOLD, 12),
                new Color(54, 70, 78)));
        panelEdicao.setBounds(20, 340, 893, 120);
        panelEdicao.setVisible(false);
        add(panelEdicao);

        JLabel lblEditarNome = new JLabel("Nome:");
        lblEditarNome.setBounds(10, 30, 50, 14);
        panelEdicao.add(lblEditarNome);

        txtEditarDisciplina = new JTextField();
        txtEditarDisciplina.setBounds(60, 27, 200, 24);
        panelEdicao.add(txtEditarDisciplina);

        JLabel lblEditarSemestre = new JLabel("Semestre:");
        lblEditarSemestre.setBounds(280, 30, 70, 14);
        panelEdicao.add(lblEditarSemestre);

        cmbEditarSemestre = new JComboBox<>();
        cmbEditarSemestre.setBounds(350, 27, 80, 24);
        panelEdicao.add(cmbEditarSemestre);

        btnSalvarEdicao = new JButton("Salvar");
        btnSalvarEdicao.setBounds(450, 26, 80, 26);
        panelEdicao.add(btnSalvarEdicao);

        btnCancelarEdicao = new JButton("Cancelar");
        btnCancelarEdicao.setBounds(540, 26, 80, 26);
        panelEdicao.add(btnCancelarEdicao);

        btnExcluirEdicao = new JButton("Excluir");
        btnExcluirEdicao.setBounds(630, 26, 80, 26);
        panelEdicao.add(btnExcluirEdicao);

        btnCancelarEdicao.addActionListener(e -> panelEdicao.setVisible(false));
    }

    private void abrirEdicaoNaTela(String itemSelecionado) {
        try {
            String nomeDisciplina = itemSelecionado.split(" – ")[0];

            DisciplinaDAO dao = new DisciplinaDAO();
            List<Disciplina> lista = dao.listarTodos();
            disciplinaEmEdicao = null;

            for (Disciplina d : lista) {
                if (d.getNome().equals(nomeDisciplina)) {
                    disciplinaEmEdicao = d;
                    break;
                }
            }

            if (disciplinaEmEdicao == null) {
                JOptionPane.showMessageDialog(this, "Disciplina não encontrada!");
                return;
            }

            // Preencher campos
            txtEditarDisciplina.setText(disciplinaEmEdicao.getNome());
            cmbEditarSemestre.removeAllItems();
            for (int i = 1; i <= 10; i++) {
                cmbEditarSemestre.addItem(i + "");
            }
            cmbEditarSemestre.setSelectedItem(disciplinaEmEdicao.getSemestre() + "");
            panelEdicao.setVisible(true);

            // Botão salvar
            for (ActionListener al : btnSalvarEdicao.getActionListeners()) {
                btnSalvarEdicao.removeActionListener(al);
            }
            btnSalvarEdicao.addActionListener(e -> {
                try {
                    disciplinaEmEdicao.setNome(txtEditarDisciplina.getText().trim());
                    disciplinaEmEdicao.setSemestre(Integer.parseInt((String) cmbEditarSemestre.getSelectedItem()));
                    dao.atualizar(disciplinaEmEdicao);
                    filtrarDisciplinasPorCurso();
                    panelEdicao.setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar disciplina: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });

            // Botão excluir
            for (ActionListener al : btnExcluirEdicao.getActionListeners()) {
                btnExcluirEdicao.removeActionListener(al);
            }
            btnExcluirEdicao.addActionListener(e -> {
                try {
                    int confirmar = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir esta disciplina?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (confirmar == JOptionPane.YES_OPTION) {
                        dao.excluir(disciplinaEmEdicao.getIdDisciplina());
                        filtrarDisciplinasPorCurso();
                        panelEdicao.setVisible(false);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir disciplina: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

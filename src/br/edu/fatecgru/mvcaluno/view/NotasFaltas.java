package br.edu.fatecgru.mvcaluno.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import br.edu.fatecgru.mvcaluno.dao.AlunoDAO;
import br.edu.fatecgru.mvcaluno.dao.MatriculaDisciplinaDAO;
import br.edu.fatecgru.mvcaluno.model.AlunoView;
import br.edu.fatecgru.mvcaluno.model.MatriculaDisciplina;
import br.edu.fatecgru.mvcaluno.model.MatriculaDisciplinaDetalhe;
import javax.swing.ImageIcon;

/**
 * View para atribuição e consulta de Notas e Faltas dos alunos.
 * Implementa recurso de AutoComplete na pesquisa de alunos.
 */
public class NotasFaltas extends JPanel {

    private static class DisciplinaComboItem {
        private int idDisciplina;
        private String displayText;

        public DisciplinaComboItem(int idDisciplina, String displayText) {
            this.idDisciplina = idDisciplina;
            this.displayText = displayText;
        }

        public int getIdDisciplina() {
            return idDisciplina;
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    private static final long serialVersionUID = 1L;

    private final String HINT_TEXT = "Digite nome ou RA do aluno";
    private final Color HINT_COLOR = Color.LIGHT_GRAY;
    private final Color TEXT_COLOR = Color.BLACK;

    private TelaPrincipal telaPrincipal;
    private int idAlunoSelecionado = -1;
    private int idMatriculaSelecionada = -1;
    private AlunoView alunoSelecionado = null;

    private JPanel panelPesquisarAluno;
    private JLabel lblPesquisarAluno;
    private JTextField txtPesquisarAluno;

    private JPanel panelDados;
    private JLabel lblDadosAluno;
    private JComboBox<String> cmbSemestre;
    private JComboBox<Object> cmbDisciplina;

    private JPanel panelNotasFaltas;
    private JTextField txtNota, txtFalta, txtStatus;
    private JButton btnAtribuir, btnProcessarFimSemestre;

    private JPopupMenu popupSugestoes;
    private JList<AlunoView> listaSugestoes;
    private DefaultListModel<AlunoView> listModelSugestoes;

    private AlunoDAO alunoDAO;
    private MatriculaDisciplinaDAO matriculaDisciplinaDAO;
    private boolean bloqueioSemestreListener = false;

    public NotasFaltas(TelaPrincipal p, int mode) {
        this.telaPrincipal = p;
        try {
            this.alunoDAO = new AlunoDAO();
            this.matriculaDisciplinaDAO = new MatriculaDisciplinaDAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        inicializarAutocomplete();
        inicializarComponentes();
        adicionarListeners();
    }

    private void inicializarComponentes() {
        setLayout(null);
        setBorder(new EmptyBorder(5, 5, 5, 5));

        // --- Painel Pesquisa Aluno ---
        panelPesquisarAluno = new JPanel();
        panelPesquisarAluno.setBounds(10, 11, 940, 73);
        panelPesquisarAluno.setLayout(null);
        add(panelPesquisarAluno);

        lblPesquisarAluno = new JLabel("Pesquisar aluno:");
        lblPesquisarAluno.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblPesquisarAluno.setBounds(18, 21, 138, 24);
        panelPesquisarAluno.add(lblPesquisarAluno);

        txtPesquisarAluno = new JTextField(HINT_TEXT);
        txtPesquisarAluno.setFont(new Font("Tahoma", Font.PLAIN, 17));
        txtPesquisarAluno.setBounds(148, 20, 414, 27);
        txtPesquisarAluno.setForeground(HINT_COLOR);
        panelPesquisarAluno.add(txtPesquisarAluno);

       
     // --- Painel Dados ---
        panelDados = new JPanel();
        panelDados.setBorder(new TitledBorder(null, "Dados do Aluno", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelDados.setLayout(null);
        panelDados.setBounds(10, 86, 940, 159);
        add(panelDados);
        panelDados.setVisible(false);

        // Label único para mostrar Nome, RA e Curso na mesma linha
        lblDadosAluno = new JLabel("Carregando dados do aluno...");
        lblDadosAluno.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblDadosAluno.setBounds(20, 33, 880, 24);
        panelDados.add(lblDadosAluno);

        // ComboBox Semestre
        JLabel lblSemestre = new JLabel("Semestre:");
        lblSemestre.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblSemestre.setBounds(20, 77, 90, 22);
        panelDados.add(lblSemestre);

        cmbSemestre = new JComboBox<>();
        cmbSemestre.setFont(new Font("Tahoma", Font.PLAIN, 15));
        cmbSemestre.setBounds(111, 77, 95, 22);
        panelDados.add(cmbSemestre);

        // ComboBox Disciplina
        JLabel lblDisciplina = new JLabel("Informe a disciplina:");
        lblDisciplina.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblDisciplina.setBounds(20, 110, 160, 22);
        panelDados.add(lblDisciplina);

        cmbDisciplina = new JComboBox<>();
        cmbDisciplina.setFont(new Font("Tahoma", Font.PLAIN, 15));
        cmbDisciplina.setBounds(190, 110, 430, 28);
        panelDados.add(cmbDisciplina);


     // --- Painel Notas/Faltas ---
        panelNotasFaltas = new JPanel();
        panelNotasFaltas.setBorder(new TitledBorder(null, "Notas e Faltas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelNotasFaltas.setBounds(10, 247, 940, 142);
        panelNotasFaltas.setLayout(null);
        panelNotasFaltas.setVisible(false);
        add(panelNotasFaltas);

        // Label e campo de Nota
        JLabel lblNota = new JLabel("Nota:");
        lblNota.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblNota.setBounds(30, 35, 60, 24);
        panelNotasFaltas.add(lblNota);

        txtNota = criarTextField(85, 35, 80, 25);
        panelNotasFaltas.add(txtNota);

        // Label e campo de Faltas
        JLabel lblFaltas = new JLabel("Faltas:");
        lblFaltas.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblFaltas.setBounds(190, 35, 70, 24);
        panelNotasFaltas.add(lblFaltas);

        txtFalta = criarTextField(255, 35, 80, 25);
        panelNotasFaltas.add(txtFalta);

        // Botão Atribuir
        btnAtribuir = criarBotao("Atribuir", "/Resources/imagens/upload-de-arquivo.png", 360, 25, 140, 45);
        panelNotasFaltas.add(btnAtribuir);

        // Botão Processar Fim do Semestre
        btnProcessarFimSemestre = criarBotao("Processar Fim do Semestre", "/Resources/imagens/concluido.png", 520, 25, 270, 45);
        panelNotasFaltas.add(btnProcessarFimSemestre);
        
        // Botão Limpar
        JButton btnLimpar = criarBotao("Limpar", "/Resources/imagens/escovar.png", 800, 25, 120, 45);
        panelNotasFaltas.add(btnLimpar);

        // Situação atual
        JLabel lblStatus = new JLabel("Situação atual:");
        lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblStatus.setBounds(30, 85, 130, 24);
        panelNotasFaltas.add(lblStatus);

        txtStatus = criarTextField(160, 85, 300, 25);
        panelNotasFaltas.add(txtStatus);
        
        //metodo para limpar a tela
        btnLimpar.addActionListener(e -> {
            // Limpar campos de texto
            txtNota.setText("");
            txtFalta.setText("");
            txtStatus.setText("");

            // Resetar o JTextField de pesquisa
            txtPesquisarAluno.setText(HINT_TEXT);
            txtPesquisarAluno.setForeground(HINT_COLOR);

            // Resetar combos
            if (cmbDisciplina.getItemCount() > 0) cmbDisciplina.setSelectedIndex(0);
            if (cmbSemestre.getItemCount() > 0) cmbSemestre.setSelectedIndex(0);

            // Ocultar os painéis
            panelDados.setVisible(false);
            panelNotasFaltas.setVisible(false);

            // Resetar seleção do aluno
            alunoSelecionado = null;
            idAlunoSelecionado = -1;
            idMatriculaSelecionada = -1;
        });

    }

    private JLabel criarLabelBold(String texto, int x, int y, int w, int h) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 15));
        lbl.setBounds(x, y, w, h);
        return lbl;
    }

    private JTextField criarTextField(int x, int y, int w, int h) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, h);
        tf.setFont(new Font("Tahoma", Font.PLAIN, 17));
        tf.setColumns(10);
        return tf;
    }
    
    private JButton criarBotao(String texto, String icone, int x, int y, int w, int h) {
        JButton btn = new JButton(" " + texto);
        btn.setIcon(new ImageIcon(NotasFaltas.class.getResource(icone)));
        btn.setBounds(x, y, w, h);
        btn.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setHorizontalAlignment(JButton.LEFT);
        return btn;
    }



    private void adicionarListeners() {
        // AutoComplete
        txtPesquisarAluno.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (txtPesquisarAluno.getText().equals(HINT_TEXT)) {
                    txtPesquisarAluno.setText("");
                    txtPesquisarAluno.setForeground(TEXT_COLOR);
                }
            }

            public void focusLost(FocusEvent e) {
                if (txtPesquisarAluno.getText().isEmpty()) {
                    txtPesquisarAluno.setText(HINT_TEXT);
                    txtPesquisarAluno.setForeground(HINT_COLOR);
                }
            }
        });

        txtPesquisarAluno.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { atualizarSugestoes(); }
            public void removeUpdate(DocumentEvent e) { atualizarSugestoes(); }
            public void changedUpdate(DocumentEvent e) { atualizarSugestoes(); }
        });

        btnAtribuir.addActionListener(e -> atribuirNotaFalta());
        btnProcessarFimSemestre.addActionListener(e -> processarFimSemestre());

        // COMBO DE SEMESTRES
        cmbSemestre.addActionListener(e -> {
            if (!bloqueioSemestreListener && idAlunoSelecionado != -1 && cmbSemestre.getSelectedItem() != null) {
                popularDisciplinas((String) cmbSemestre.getSelectedItem());
            }
        });

        cmbDisciplina.addActionListener(e -> carregarNotaFalta());
    }

    private void inicializarAutocomplete() {
        popupSugestoes = new JPopupMenu();
        popupSugestoes.setFocusable(false);

        listModelSugestoes = new DefaultListModel<>();
        listaSugestoes = new JList<>(listModelSugestoes);

        listaSugestoes.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof AlunoView) {
                    AlunoView aluno = (AlunoView) value;
                    setText(aluno.getNome() + " (RA: " + aluno.getRa() + ")");
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaSugestoes);
        popupSugestoes.add(scrollPane);

        listaSugestoes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) selecionarAlunoSugerido();
            }
        });
    }

    private void atualizarSugestoes() {
        String texto = txtPesquisarAluno.getText().trim();
        if (texto.isEmpty() || texto.equals(HINT_TEXT)) {
            popupSugestoes.setVisible(false);
            return;
        }

        try {
            List<AlunoView> alunos = alunoDAO.listarPorFiltro(texto);
            listModelSugestoes.clear();
            if (!alunos.isEmpty()) {
                alunos.forEach(listModelSugestoes::addElement);
                popupSugestoes.show(txtPesquisarAluno, 0, txtPesquisarAluno.getHeight());
                popupSugestoes.setPopupSize(txtPesquisarAluno.getWidth(), 150);
            } else {
                popupSugestoes.setVisible(false);
            }
        } catch (Exception e) {
            popupSugestoes.setVisible(false);
        }
    }

    private void selecionarAlunoSugerido() {
        AlunoView aluno = listaSugestoes.getSelectedValue();
        if (aluno == null) return;

        this.alunoSelecionado = aluno;
        idAlunoSelecionado = aluno.getIdAluno();
        txtPesquisarAluno.setText(aluno.getNome());
        lblDadosAluno.setText(
        	    "Nome completo: " + aluno.getNome() + "                     " +
        	    "    RA: " + aluno.getRa() + "                     " +
        	    "    Curso: " + aluno.getNomeCurso()
        	);

        try {
            idMatriculaSelecionada = alunoDAO.buscarIdMatricula(idAlunoSelecionado);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar matrícula: " + e.getMessage());
            return;
        }

        panelDados.setVisible(true);

        // --- POPULAR SEMESTRES ---
        popularSemestres();

        // Seleciona automaticamente o semestre com disciplina em status "Cursando"
        String semestreAtual = "";
		try {
			semestreAtual = matriculaDisciplinaDAO.obterSemestreAtualAluno(idAlunoSelecionado);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Erro ao buscar carregar o semestre: " + e.getMessage());
		}
        cmbSemestre.setSelectedItem(semestreAtual);
        bloqueioSemestreListener = false;

        // --- CARREGAR DISCIPLINAS DO SEMESTRE SELECIONADO ---
        popularDisciplinas(semestreAtual);

        popupSugestoes.setVisible(false);
    }
    
    private void popularSemestres() {
        cmbSemestre.removeAllItems();

        if (idAlunoSelecionado == -1) return;

        try {
            // 1. Listar todos os semestres cursados pelo aluno
            List<String> semestres = matriculaDisciplinaDAO.listarSemestresCursados(idAlunoSelecionado);

            if (semestres.isEmpty()) {
                // Isso não deve mais acontecer se o aluno tiver matrícula
                cmbSemestre.addItem("N/A");
                cmbSemestre.setEnabled(false);
                return;
            }

            // 2. Adicionar todos os semestres no combo
            // IMPORTANTE: Limpar e adicionar para garantir a ordem correta
            cmbSemestre.removeAllItems(); // Já feito no início, mas reforçando
            semestres.forEach(cmbSemestre::addItem);

            // 3. Tentar selecionar o semestre com disciplina em status "Cursando" (Lógica original)
            String semestreCursando = null;
            for (String s : semestres) {
                // Aqui usamos o ID do aluno, não da matrícula
                if (matriculaDisciplinaDAO.temDisciplinaCursando(idAlunoSelecionado, s)) {
                    semestreCursando = s;
                    break;
                }
            }

            // 4. Se não houver nada cursando, seleciona o último semestre (o mais recente, que deve ser o próximo letivo)
            if (semestreCursando == null) {
                semestreCursando = semestres.get(semestres.size() - 1);
            }

            cmbSemestre.setSelectedItem(semestreCursando);
            cmbSemestre.setEnabled(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar semestres: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void popularDisciplinas(String semestre) {
        cmbDisciplina.removeAllItems();
        txtNota.setText("");
        txtFalta.setText("");
        txtStatus.setText("");
        panelNotasFaltas.setVisible(false);

        if (idAlunoSelecionado == -1 || semestre == null) return;

        try {
            List<MatriculaDisciplinaDetalhe> detalhes = matriculaDisciplinaDAO.listarDisciplinasParaAlunoNoSemestre(idAlunoSelecionado, semestre);
            cmbDisciplina.addItem(new DisciplinaComboItem(-1, "Selecione uma disciplina"));

            if (detalhes.isEmpty()) {
                cmbDisciplina.addItem("Nenhuma disciplina encontrada");
                cmbDisciplina.setEnabled(false);
                return;
            }

            detalhes.forEach(d -> cmbDisciplina.addItem(new DisciplinaComboItem(d.getIdDisciplina(), d.getNomeDisciplina())));
            cmbDisciplina.setEnabled(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar disciplinas: " + e.getMessage());
        }
    }

    private void carregarNotaFalta() {
        Object item = cmbDisciplina.getSelectedItem();
        String semestre = (String) cmbSemestre.getSelectedItem();

        txtNota.setText("");
        txtFalta.setText("");
        txtStatus.setText("");
        panelNotasFaltas.setVisible(false);

        if (!(item instanceof DisciplinaComboItem) || idMatriculaSelecionada == -1 || semestre == null) return;
        DisciplinaComboItem disciplinaItem = (DisciplinaComboItem) item;
        if (disciplinaItem.getIdDisciplina() == -1) return;

        try {
            MatriculaDisciplina md = matriculaDisciplinaDAO.buscarNotaFaltas(idMatriculaSelecionada, disciplinaItem.getIdDisciplina(), semestre);
            if (md != null) {
                // Se nota e faltas são 0 e status é "Cursando", deixar campos vazios (ainda não inserido)
                if (md.getNota() == 0.0 && md.getFaltas() == 0 && "Cursando".equals(md.getStatus())) {
                    txtNota.setText("");
                    txtFalta.setText("");
                } else {
                    txtNota.setText(String.format("%.2f", md.getNota()).replace(',', '.'));
                    txtFalta.setText(String.valueOf(md.getFaltas()));
                }
                txtStatus.setText(md.getStatus());
                btnAtribuir.putClientProperty("idMatriculaDisciplina", md.getIdMatriculaDisciplina());

                txtNota.setEnabled(true);
                txtFalta.setEnabled(true);
                btnAtribuir.setEnabled(true);
            } else {
                btnAtribuir.putClientProperty("idMatriculaDisciplina", -1);
                txtStatus.setText("Registro Inexistente (ERRO)");
                txtNota.setEnabled(false);
                txtFalta.setEnabled(false);
                btnAtribuir.setEnabled(false);
            }
            panelNotasFaltas.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar nota/falta: " + e.getMessage());
        }
    }
    
    private void atribuirNotaFalta() {
        Object disciplinaObj = cmbDisciplina.getSelectedItem();
        String semestre = (String) cmbSemestre.getSelectedItem();

        if (!(disciplinaObj instanceof DisciplinaComboItem) || idMatriculaSelecionada == -1 || semestre == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno, semestre e disciplina válidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DisciplinaComboItem disciplinaItem = (DisciplinaComboItem) disciplinaObj;

        double nota;
        int faltas;
        try {
            nota = Double.parseDouble(txtNota.getText().replace(',', '.'));
            faltas = Integer.parseInt(txtFalta.getText());
            if (nota < 0 || nota > 10 || faltas < 0) {
                JOptionPane.showMessageDialog(this, "Nota deve ser 0-10 e faltas >=0.", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Nota e Faltas devem ser números válidos.", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            MatriculaDisciplina md = new MatriculaDisciplina();
            md.setNota(nota);
            md.setFaltas(faltas);
            int idMdExistente = (int) btnAtribuir.getClientProperty("idMatriculaDisciplina");

            if (idMdExistente > 0) {
                md.setIdMatriculaDisciplina(idMdExistente);
                matriculaDisciplinaDAO.atribuirTemporariamenteNotaFaltas(md);
                JOptionPane.showMessageDialog(this, "Notas e Faltas lançadas.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarNotaFalta();
            } else {
                JOptionPane.showMessageDialog(this, "Registro da matrícula-disciplina não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Falha ao salvar/atualizar: " + e.getMessage(), "Erro de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processarFimSemestre() {
        String semestreAtual = (String) cmbSemestre.getSelectedItem();

        if (idMatriculaSelecionada == -1 || alunoSelecionado == null || semestreAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno e um semestre válidos.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Delegar totalmente para o método correto da DAO
            List<String> novasDisciplinas = matriculaDisciplinaDAO.processarFimSemestre(
                idMatriculaSelecionada,
                alunoSelecionado.getIdCurso(),
                semestreAtual
            );

            if (novasDisciplinas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Sem novas disciplinas para matrícula neste semestre.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Fim do semestre processado com sucesso!\n\nNovas matrículas realizadas em:\n - " +
                    String.join("\n - ", novasDisciplinas),
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
            popularSemestres(); // AGORA inclui o próximo semestre letivo
            
            // O próximo semestre letivo DEVE ser selecionado agora que está na lista:
            String proximoSemestreLetivo = matriculaDisciplinaDAO.calcularProximoSemestreLetivo(semestreAtual);
            
            // Bloqueamos o listener para evitar uma chamada dupla a popularDisciplinas
            bloqueioSemestreListener = true; 
            cmbSemestre.setSelectedItem(proximoSemestreLetivo);
            bloqueioSemestreListener = false;

            // Carrega as disciplinas do novo semestre selecionado
            popularDisciplinas(proximoSemestreLetivo);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao processar fim do semestre: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

}

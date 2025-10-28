package br.edu.fatecgru.mvcaluno.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer; // Importação adicionada
import javax.swing.table.DefaultTableModel;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import br.edu.fatecgru.mvcaluno.dao.AlunoDAO;
import br.edu.fatecgru.mvcaluno.model.AlunoView;
import br.edu.fatecgru.mvcaluno.model.BoletimAluno;
import br.edu.fatecgru.mvcaluno.model.DisciplinaBoletim;

public class Documentos extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel lblNewLabel;
    private JTextField txtBuscar;
    private JComboBox<String> cmbDocumento;
    private JPanel panelFiltros;
    private JPanel panelWrapper;
    private JPanel panelDocumento;
    
    // Variáveis para a funcionalidade
    private JFrame framePai;
    
    // Componentes para autocomplete
    private JPopupMenu popupSugestoes;
    private JList<AlunoView> listaSugestoes;
    private DefaultListModel<AlunoView> listModelSugestoes;
    private AlunoDAO alunoDAO;
    private AlunoView alunoSelecionado;
    private BoletimAluno dadosAluno;  // Para o PDF
    private List<DisciplinaBoletim> disciplinas;  // Para o PDF
    
    // Novos componentes
    private JButton btnGerarDocumento;
    private JButton btnExportarPDF;
    private JTable tableDisciplinas;
    private DefaultTableModel modelDisciplinas;
    private JLabel lblDocumento_1;
    

    // Construtores
    public Documentos(JFrame framePai) {
        this.framePai = framePai;
        try {
            // Inicializa o DAO
            this.alunoDAO = new AlunoDAO();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        setupLayout();
        inicializarComponentes();
        adicionarListeners();
    }

    public Documentos() {
        this(null);
    }
    
    private void setupLayout() {
        final String HINT_TEXT = "Informe ID, Nome ou RA do Aluno";
        final Color HINT_COLOR = Color.LIGHT_GRAY;
        final Color TEXT_COLOR = Color.BLACK;

        setLayout(new BorderLayout(5, 5));

        // ----- Painel de Filtros (Topo) -----
        panelFiltros = new JPanel();
        panelFiltros.setLayout(null);
        panelFiltros.setPreferredSize(new Dimension(950, 75));
        
        lblNewLabel = new JLabel("Aluno:");
        lblNewLabel.setBounds(29, 28, 68, 35);
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panelFiltros.add(lblNewLabel);
        
        txtBuscar = new JTextField(); 
        txtBuscar.setBounds(78, 33, 325, 27);		
        txtBuscar.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtBuscar.setColumns(10);
        txtBuscar.setText(HINT_TEXT);
        txtBuscar.setForeground(HINT_COLOR);
        panelFiltros.add(txtBuscar);
        
        JLabel lblDocumento = new JLabel("Tipo de Documento:");
        lblDocumento.setBounds(429, 28, 150, 35);
        lblDocumento.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panelFiltros.add(lblDocumento);
        
        cmbDocumento = new JComboBox<>(new String[]{"Boletim", "Histórico Escolar"});
        cmbDocumento.setBounds(571, 33, 150, 27);
        panelFiltros.add(cmbDocumento);
        
        btnGerarDocumento = new JButton("Gerar Documento");
        btnGerarDocumento.setIcon(new ImageIcon(Documentos.class.getResource("/Resources/imagens/documento-de-texto.png")));
        btnGerarDocumento.setBounds(755, 22, 179, 50);
        btnGerarDocumento.setEnabled(false);
        btnGerarDocumento.setContentAreaFilled(false);
        btnGerarDocumento.setFocusPainted(false);

        panelFiltros.add(btnGerarDocumento);
        
        add(panelFiltros, BorderLayout.NORTH);

        panelWrapper = new JPanel(new BorderLayout());
        panelWrapper.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 50, 10, 50));
        panelDocumento = new JPanel(new BorderLayout());
        panelWrapper.add(panelDocumento, BorderLayout.CENTER);
        add(panelWrapper, BorderLayout.CENTER);

        // ----- Painel de Botões (Inferior) -----
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnExportarPDF = new JButton(" Exportar");
        btnExportarPDF.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnExportarPDF.setContentAreaFilled(false);
        btnExportarPDF.setFocusPainted(false);
        btnExportarPDF.setIcon(new ImageIcon(Documentos.class.getResource("/Resources/imagens/baixar-pdf.png")));
        
        pnlBotoes.add(btnExportarPDF);
        
        // Esconde o botão de exportar por padrão
        pnlBotoes.setVisible(false); 
        
        add(pnlBotoes, BorderLayout.SOUTH);
        
        lblDocumento_1 = new JLabel("  ");
        lblDocumento_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        pnlBotoes.add(lblDocumento_1);
    }
    
    private void inicializarComponentes() {
        inicializarAutocomplete();
        
        Font tableFont = new Font("Tahoma", Font.PLAIN, 15);
        Font headerFont = new Font("Tahoma", Font.BOLD, 15); 

        String[] colunas = {"Disciplina", "Nota", "Faltas", "Semestre"};
        modelDisciplinas = new DefaultTableModel(colunas, 0);
        tableDisciplinas = new JTable(modelDisciplinas);
        
        tableDisciplinas.setFont(tableFont);
        
        tableDisciplinas.getTableHeader().setFont(headerFont);
        
        tableDisciplinas.setRowHeight(25);
        
        tableDisciplinas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                setFont(tableFont); 
                
                // Centraliza o texto nas colunas de Nota, Faltas e Semestre
                if (column > 0) {
                    setHorizontalAlignment(CENTER);
                } else {
                    setHorizontalAlignment(LEFT);
                }
                return this;
            }
        });
        
        tableDisciplinas.setPreferredScrollableViewportSize(new Dimension(600, 200));
    }
    
    private void inicializarAutocomplete() {
        popupSugestoes = new JPopupMenu();
        popupSugestoes.setFocusable(false);
        
        listModelSugestoes = new DefaultListModel<>();
        listaSugestoes = new JList<>(listModelSugestoes);

        listaSugestoes.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof AlunoView) {
                    AlunoView aluno = (AlunoView) value;
                    setText(aluno.getNome() + " (RA: " + aluno.getRa() + ")");
                }
                return this;
            }
        });

        JScrollPane scrollPaneSugestoes = new JScrollPane(listaSugestoes);
        popupSugestoes.add(scrollPaneSugestoes);
    }

    private void adicionarListeners() {
        final String HINT_TEXT = "Informe ID, Nome ou RA do Aluno";
        final Color HINT_COLOR = Color.LIGHT_GRAY;
        final Color TEXT_COLOR = Color.BLACK;

        txtBuscar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBuscar.getText().equals(HINT_TEXT)) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(TEXT_COLOR);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent() != null && e.getOppositeComponent().getParent() == popupSugestoes) {
                    return;
                }
                if (txtBuscar.getText().isEmpty()) {
                    txtBuscar.setText(HINT_TEXT);
                    txtBuscar.setForeground(HINT_COLOR);
                }
            }
        });

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { atualizarSugestoes(); }
            @Override public void removeUpdate(DocumentEvent e) { atualizarSugestoes(); }
            @Override public void changedUpdate(DocumentEvent e) { }
        });
        
        listaSugestoes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selecionarAlunoSugerido();
                }
            }
        });

        btnGerarDocumento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (alunoSelecionado == null) {
                    JOptionPane.showMessageDialog(Documentos.this, "Selecione um aluno primeiro!");
                    return;
                }
                String tipoDocumento = (String) cmbDocumento.getSelectedItem();
                if ("Boletim".equals(tipoDocumento)) {
                    gerarBoletim(alunoSelecionado.getIdAluno());
                } else if ("Histórico Escolar".equals(tipoDocumento)) {
                    gerarHistoricoEscolar(alunoSelecionado.getIdAluno());
                }
            }
        });

        btnExportarPDF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dadosAluno == null || disciplinas == null) {
                    JOptionPane.showMessageDialog(Documentos.this, "Gere um documento primeiro!");
                    return;
                }
                String tipo = (String) cmbDocumento.getSelectedItem();
                gerarPDF(dadosAluno, disciplinas, tipo);
            }
        });
    }

    private void atualizarSugestoes() {
        String textoDigitado = txtBuscar.getText().trim();

        if (textoDigitado.isEmpty() || textoDigitado.equals("Informe ID, Nome ou RA do Aluno")) {
            popupSugestoes.setVisible(false);
            btnGerarDocumento.setEnabled(false);
            return;
        }

        try {
            List<AlunoView> alunosEncontrados = alunoDAO.listarPorFiltro(textoDigitado);
            listModelSugestoes.clear();
            if (!alunosEncontrados.isEmpty()) {
                for (AlunoView aluno : alunosEncontrados) {
                    listModelSugestoes.addElement(aluno);
                }
                popupSugestoes.show(txtBuscar, 0, txtBuscar.getHeight());
                popupSugestoes.setPopupSize(txtBuscar.getWidth(), 150);
            } else {
                popupSugestoes.setVisible(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            popupSugestoes.setVisible(false);
        }
    }

    private void selecionarAlunoSugerido() {
        alunoSelecionado = listaSugestoes.getSelectedValue();
        if (alunoSelecionado != null) {
            txtBuscar.setText(alunoSelecionado.getNome() + " (" + alunoSelecionado.getNomeCurso() + ")");
            popupSugestoes.setVisible(false);
            btnGerarDocumento.setEnabled(true);
        }
    }

    private void gerarBoletim(int idAluno) {
        try {
            BoletimAluno dados = alunoDAO.buscarDadosBoletimAluno(idAluno);
            if (dados == null) {
                JOptionPane.showMessageDialog(this, "Dados do aluno não encontrados.");
                return;
            }
            disciplinas = alunoDAO.buscarDisciplinasBoletim(idAluno);
            dadosAluno = dados;
            exibirDocumento(dados, disciplinas, "Boletim");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao gerar boletim: " + e.getMessage());
        }
    }

    private void gerarHistoricoEscolar(int idAluno) {
        try {
            BoletimAluno dados = alunoDAO.buscarDadosBoletimAluno(idAluno);
            if (dados == null) {
                JOptionPane.showMessageDialog(this, "Dados do aluno não encontrados.");
                return;
            }
            disciplinas = alunoDAO.buscarHistoricoEscolar(idAluno);
            dadosAluno = dados;
            exibirDocumento(dados, disciplinas, "Histórico Escolar");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao gerar histórico: " + e.getMessage());
        }
    }
 
    private void exibirDocumento(BoletimAluno dadosAlunoParam, List<DisciplinaBoletim> disciplinasParam, String tipo) {
        BoletimAluno dadosAlunoLocal = dadosAlunoParam;
        List<DisciplinaBoletim> disciplinasLocal = disciplinasParam;
        
        // Painel que conterá os dados do aluno (usa BorderLayout para organizar)
        JPanel panelInfo = new JPanel(new BorderLayout());
        
        // ====================================================================
        // NOVO: Usa BoxLayout no eixo X (Horizontal) para controlar o espaçamento
        // entre os grupos de informações (Nome, RA, Curso, Campus).
        // ====================================================================
        JPanel panelDadosCompactos = new JPanel(); 
        panelDadosCompactos.setLayout(new javax.swing.BoxLayout(panelDadosCompactos, javax.swing.BoxLayout.X_AXIS));

        // Define o espaçamento vertical e o alinhamento
        int vgap = 10;
        int hgapEntreGrupos = 40; // Espaçamento MAIOR entre Rótulos (Ex: entre RA e Curso)
        int hgapDentroGrupo = 5;  // Espaçamento MENOR entre Rótulo e Valor (Ex: Nome: [Valor])

        // Definição da nova fonte: Tamanho 15
        Font fontBold = new Font("Tahoma", Font.BOLD, 15); // Rótulos em negrito
        Font fontPlain = new Font("Tahoma", Font.PLAIN, 15); // Valores normais
        
        
        // Função auxiliar para criar um bloco de Rótulo-Valor
        // Usa FlowLayout interno com hgap pequeno
        
        // ----------------- NOME -----------------
        JPanel pnlNome = new JPanel(new FlowLayout(FlowLayout.LEFT, hgapDentroGrupo, vgap));
        
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(fontBold);
        pnlNome.add(lblNome);
        
        JLabel valNome = new JLabel(dadosAlunoLocal.getNome());
        valNome.setFont(fontPlain);
        pnlNome.add(valNome);
        
        panelDadosCompactos.add(pnlNome);
        panelDadosCompactos.add(javax.swing.Box.createHorizontalStrut(hgapEntreGrupos)); // Espaçamento

        // ----------------- CURSO -----------------
        JPanel pnlCurso = new JPanel(new FlowLayout(FlowLayout.LEFT, hgapDentroGrupo, vgap));

        JLabel lblCurso = new JLabel("Curso:");
        lblCurso.setFont(fontBold);
        pnlCurso.add(lblCurso);
        
        JLabel valCurso = new JLabel(dadosAlunoLocal.getNomeCurso());
        valCurso.setFont(fontPlain);
        pnlCurso.add(valCurso);
        
        panelDadosCompactos.add(pnlCurso);
        panelDadosCompactos.add(javax.swing.Box.createHorizontalStrut(hgapEntreGrupos)); // Espaçamento

        // ----------------- RA -----------------
        JPanel pnlRA = new JPanel(new FlowLayout(FlowLayout.LEFT, hgapDentroGrupo, vgap));

        JLabel lblRA = new JLabel("RA:");
        lblRA.setFont(fontBold);
        pnlRA.add(lblRA);
        
        JLabel valRA = new JLabel(dadosAlunoLocal.getRa());
        valRA.setFont(fontPlain);
        pnlRA.add(valRA);
        
        panelDadosCompactos.add(pnlRA);
        panelDadosCompactos.add(javax.swing.Box.createHorizontalStrut(hgapEntreGrupos)); // Espaçamento

        // ----------------- CAMPUS -----------------
        JPanel pnlCampus = new JPanel(new FlowLayout(FlowLayout.LEFT, hgapDentroGrupo, vgap));

        JLabel lblCampus = new JLabel("Campus:");
        lblCampus.setFont(fontBold);
        pnlCampus.add(lblCampus);
        
        JLabel valCampus = new JLabel(dadosAlunoLocal.getCampus());
        valCampus.setFont(fontPlain);
        pnlCampus.add(valCampus);
        
        panelDadosCompactos.add(pnlCampus);
        // Sem espaçamento após o último grupo
        
        
        // Adiciona o painel compacto ao panel de informações principal
        // Usamos BorderLayout.WEST para evitar que ele seja esticado e ocupe todo o espaço
        panelInfo.add(panelDadosCompactos, BorderLayout.WEST);

        // Cria um painel vazio para servir como separador
        JPanel separador = new JPanel();
        separador.setPreferredSize(new Dimension(1, 20)); 
        separador.setMinimumSize(new Dimension(1, 20));
        separador.setBackground(new Color(0, 0, 0, 0)); // Torna transparente
        
        // ------------------------------------------------------------------
        
        modelDisciplinas.setRowCount(0);
        for (DisciplinaBoletim disc : disciplinasParam) { 
            modelDisciplinas.addRow(new Object[]{
                disc.getNomeDisciplina(),
                String.format("%.2f", disc.getNota()),
                disc.getFaltas(),
                disc.getSemestreAtual()
            });
        }

        panelDocumento.removeAll();
        
        JLabel lblTituloDados = new JLabel("Informações do Aluno");
        lblTituloDados.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblTituloDados.setHorizontalAlignment(JLabel.CENTER);
        
        // Painel para organizar o topo (Título + Informações + Separador)
        JPanel pnlTopo = new JPanel(new BorderLayout());
        pnlTopo.add(lblTituloDados, BorderLayout.NORTH);
        pnlTopo.add(panelInfo, BorderLayout.CENTER);
        pnlTopo.add(separador, BorderLayout.SOUTH);  

        
        // Adiciona os componentes ao painel principal do documento
        panelDocumento.add(pnlTopo, BorderLayout.NORTH);
        panelDocumento.add(new JScrollPane(tableDisciplinas), BorderLayout.CENTER); 

        
        panelDocumento.revalidate();
        panelDocumento.repaint();
        
        ((JPanel) btnExportarPDF.getParent()).setVisible(true);

    }
    
    private void gerarPDF(BoletimAluno dados, List<DisciplinaBoletim> disciplinasList, String tipo) {
        try {
            // Pasta Documentos do usuário
            String userDocuments = System.getProperty("user.home") + "/Documents/";
            new java.io.File(userDocuments).mkdirs();

            String fileName = tipo.replaceAll("\\s+", "_") + "_" + dados.getNome().replaceAll("\\s+", "_") + ".pdf";
            String dest = userDocuments + fileName;

            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Margens
            document.setMargins(50, 40, 50, 40);

            // --- Cabeçalho ---
            Paragraph cabecalho = new Paragraph(tipo)
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(cabecalho);
            document.add(new Paragraph("\n"));

            // --- Informações do aluno ---
            // Aumentamos o array para 3 linhas de informação
            Table tabelaInfo = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                    .useAllAvailableWidth();

            tabelaInfo.addCell(new Cell().add(new Paragraph("Nome:").setBold()));
            tabelaInfo.addCell(new Cell().add(new Paragraph(dados.getNome())));
            
            tabelaInfo.addCell(new Cell().add(new Paragraph("RA:").setBold()));
            tabelaInfo.addCell(new Cell().add(new Paragraph(dados.getRa())));
            
            tabelaInfo.addCell(new Cell().add(new Paragraph("Curso:").setBold()));
            tabelaInfo.addCell(new Cell().add(new Paragraph(dados.getNomeCurso())));
            
            // CORREÇÃO: Adiciona o Campus no PDF
            tabelaInfo.addCell(new Cell().add(new Paragraph("Campus:").setBold()));
            tabelaInfo.addCell(new Cell().add(new Paragraph(dados.getCampus()))); // <--- CORREÇÃO AQUI

            document.add(tabelaInfo);
            document.add(new Paragraph("\n"));

            // --- Título da tabela ---
            Paragraph tituloTabela = new Paragraph("Disciplinas e Desempenho")
                    .setBold()
                    .setFontSize(13)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(tituloTabela);
            document.add(new Paragraph("\n"));

            // --- Tabela das disciplinas ---
            Table tabelaDisciplinas = new Table(UnitValue.createPercentArray(new float[]{3, 1, 1, 1}))
                    .useAllAvailableWidth();

            // Cabeçalho
            String[] headers = {"Disciplina", "Nota", "Faltas", "Semestre"};
            for (String h : headers) {
                tabelaDisciplinas.addHeaderCell(
                    new Cell().add(new Paragraph(h).setBold().setTextAlignment(TextAlignment.CENTER))
                );
            }

            com.itextpdf.kernel.colors.Color cinzaClaro = new com.itextpdf.kernel.colors.DeviceRgb(235, 235, 235);
            com.itextpdf.kernel.colors.Color branco = com.itextpdf.kernel.colors.ColorConstants.WHITE;

            boolean corAlternada = false;
            for (DisciplinaBoletim disc : disciplinasList) {
                com.itextpdf.kernel.colors.Color bgColor = corAlternada ? cinzaClaro : branco;
                corAlternada = !corAlternada;

                tabelaDisciplinas.addCell(new Cell().add(new Paragraph(disc.getNomeDisciplina()))
                        .setBackgroundColor(bgColor));
                tabelaDisciplinas.addCell(new Cell().add(new Paragraph(String.format("%.2f", disc.getNota())))
                        .setBackgroundColor(bgColor).setTextAlignment(TextAlignment.CENTER));
                tabelaDisciplinas.addCell(new Cell().add(new Paragraph(String.valueOf(disc.getFaltas())))
                        .setBackgroundColor(bgColor).setTextAlignment(TextAlignment.CENTER));
                tabelaDisciplinas.addCell(new Cell().add(new Paragraph(disc.getSemestreAtual()))
                        .setBackgroundColor(bgColor).setTextAlignment(TextAlignment.CENTER));
            }

            document.add(tabelaDisciplinas);
            document.add(new Paragraph("\n\n"));

            // --- Rodapé ---
            String dataAtual = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            document.add(new Paragraph("Emitido em: " + dataAtual)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(new Paragraph("\n\n__________________________________________")
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Assinatura da Coordenação")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10));

            document.close();

            JOptionPane.showMessageDialog(this, "📄 Documento gerado com sucesso em:\n" + dest);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao gerar PDF: " + e.getMessage());
        }
    }


    private void voltarParaListagem() {
        if (framePai instanceof TelaPrincipal) {
            TelaPrincipal telaPrincipal = (TelaPrincipal) framePai;
            telaPrincipal.trocarPainelConteudo(new ListarAlunos(telaPrincipal));
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Tela de Documentos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            Documentos painel = new Documentos();
            frame.setContentPane(painel);
            frame.setVisible(true);
        });
    }
}
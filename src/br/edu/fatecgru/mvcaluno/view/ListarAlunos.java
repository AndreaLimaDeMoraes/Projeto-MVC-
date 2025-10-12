package br.edu.fatecgru.mvcaluno.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane; // Importar JScrollPane
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Dimension;

public class ListarAlunos extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblNewLabel;
	private JTextField txtBuscar;
	private JLabel lblCurso;
	private JComboBox<String> cmbCurso; // Definindo o tipo genérico
	private JTable tblListaAlunos;
	private JPanel panelFiltros;
	private JButton btnNovoAluno;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
                    // Para testar esta classe, ela deve ser exibida em um JFrame
                    JFrame frameTeste = new JFrame("Teste Listar Alunos");
                    frameTeste.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameTeste.setBounds(100, 100, 800, 600); // Defina um tamanho adequado
					ListarAlunos painelAlunos = new ListarAlunos();
                    frameTeste.setContentPane(painelAlunos);
					frameTeste.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the panel.
	 */
	public ListarAlunos() {
		
		 // Define as constantes da Hint
	    final String HINT_TEXT = "Informe nome ou RA do aluno";
	    final Color HINT_COLOR = Color.LIGHT_GRAY;
	    final Color TEXT_COLOR = Color.BLACK;
		
		// Define o layout principal do JPanel ListarAlunos
		// Usa BorderLayout para dividir o painel em Filtros (NORTH) e Tabela (CENTER)
		setLayout(new BorderLayout(5, 5)); 
		
		// ----- Painel de Filtros (Topo) -----
		panelFiltros = new JPanel();
		// Mantém o layout nulo dentro do painel de filtros para a posição dos componentes
		panelFiltros.setLayout(null);
		// Define o tamanho preferido para o painel de filtros (ajuste se precisar de mais espaço vertical)
		panelFiltros.setPreferredSize(new Dimension(950, 75));
		
		// Campos de Filtro
		lblNewLabel = new JLabel("Buscar:");
		lblNewLabel.setBounds(10, 33, 68, 35);
		panelFiltros.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		 //INSTANCIA o JTextField
	    txtBuscar = new JTextField(); 
	    txtBuscar.setBounds(66, 38, 325, 27);		
	    txtBuscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
	    txtBuscar.setColumns(10);
	    
	    //CONFIGURA O HINT INICIAL (DEPOIS da instanciação)
	    txtBuscar.setText(HINT_TEXT);
	    txtBuscar.setForeground(HINT_COLOR);
	    
	    //ADICIONAR O PAINEL
	    panelFiltros.add(txtBuscar);

	    // 4. ADICIONAR O FocusListener
	    txtBuscar.addFocusListener(new FocusListener() {
	        @Override
	        public void focusGained(FocusEvent e) {
	            if (txtBuscar.getText().equals(HINT_TEXT)) {
	                txtBuscar.setText("");
	                txtBuscar.setForeground(TEXT_COLOR);
	            }
	        }
	        @Override
	        public void focusLost(FocusEvent e) {
	            if (txtBuscar.getText().isEmpty()) {
	                txtBuscar.setText(HINT_TEXT);
	                txtBuscar.setForeground(HINT_COLOR);
	            }
	        }
	    });
	    
		txtBuscar.setBounds(66, 38, 325, 27);		
		
		panelFiltros.add(txtBuscar);
		txtBuscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtBuscar.setColumns(10);
		
		lblCurso = new JLabel("Curso:");
		lblCurso.setBounds(415, 31, 54, 35);
		panelFiltros.add(lblCurso);
		lblCurso.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		cmbCurso = new JComboBox<>();
		cmbCurso.setBounds(466, 36, 274, 27); 
		panelFiltros.add(cmbCurso);
		
		btnNovoAluno = new JButton(" Novo aluno");
		btnNovoAluno.setHorizontalAlignment(SwingConstants.LEFT);
		btnNovoAluno.setIcon(new ImageIcon(getClass().getResource("/Resources/imagens/adicionar-usuario.png")));
		btnNovoAluno.setForeground(Color.black);
		btnNovoAluno.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNovoAluno.setBounds(762, 25, 167, 40);
		btnNovoAluno.setContentAreaFilled(false); // Remove o preenchimento da área de conteúdo (ajuda na transparência)
		//Remove o contorno que aparece ao clicar ou quando o botão está selecionado (Focus Border)
		btnNovoAluno.setFocusPainted(false);
		panelFiltros.add(btnNovoAluno);
		
		// Adiciona o painel de filtros na região NORTE (Topo)
		add(panelFiltros, BorderLayout.NORTH);
		
		// ----- Tabela de Alunos (Centro) -----
		tblListaAlunos = new JTable();
		
		// IMPORTANTE: Envolve a tabela em um JScrollPane
		JScrollPane scrollPane = new JScrollPane(tblListaAlunos);
		
		// Adiciona o ScrollPane (e, portanto, a tabela) na região CENTRAL
		// O CENTER ocupará todo o espaço restante
		add(scrollPane, BorderLayout.CENTER);
		
		
	}
	
	
}
package br.edu.fatecgru.mvcaluno.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.SpringLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class ListarAlunos extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JTextField txtBuscar;
	private JLabel lblCurso;
	private JComboBox cmbCurso;
	private JTable tblListaAlunos;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListarAlunos frame = new ListarAlunos();
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
	public ListarAlunos() {
        setLayout(new BorderLayout(10, 10)); 
		
		panel = new JPanel();
		panel.setBounds(0, 0, 727, 59);
		add(panel);
		
		lblNewLabel = new JLabel("Buscar:");
		lblNewLabel.setBounds(14, 11, 68, 35);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		txtBuscar = new JTextField();
		txtBuscar.setBounds(70, 19, 325, 19);
		txtBuscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtBuscar.setColumns(10);
		panel.setLayout(null);
		panel.add(lblNewLabel);
		panel.add(txtBuscar);
		
		lblCurso = new JLabel("Curso:");
		lblCurso.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCurso.setBounds(409, 11, 54, 35);
		panel.add(lblCurso);
		
		cmbCurso = new JComboBox();
		cmbCurso.setBounds(460, 19, 248, 22);
		panel.add(cmbCurso);
		
		tblListaAlunos = new JTable();
		tblListaAlunos.setBounds(14, 84, 694, 309);
		panel.add(tblListaAlunos);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

	}
}

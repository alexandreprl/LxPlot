package fr.irit.smac.lxplot.server;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.irit.smac.lxplot.Constants;
import fr.irit.smac.lxplot.commons.XJFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.Box;
import javax.swing.SpringLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.border.BevelBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class MainWindow {

	private XJFrame frame;
	private JLabel status;
	private JPanel panel_1;
	private JCheckBox chckbxSleep;
	private JSpinner columnsSpinner;
	private JPanel panel_5;
	private JButton btnNewButton;
	private Component horizontalGlue;
	private JLabel lblNewLabel_1;
	private Component verticalGlue;
	private JButton btnNewButton_1;
	private JPanel panel_2;
	private JSpinner spinner;
	private JButton btnNewButton_2;
	private JButton btnNewButton_3;
	private JPanel panel_3;
	private JLabel lblNewLabel;
	private JPanel panel_4;
	private JButton btnNewButton_4;
	private JButton btnForceQuit;
	private Component horizontalStrut;
	private JPanel panel_6;
	private JLabel lblNewLabel_2;
	private JSpinner spinner_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow("");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @param title
	 */
	public MainWindow(String title) {
		initialize(title);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String title) {
		frame = new XJFrame(title);
		frame.setBounds(100, 100, 608, 562);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		status = new JLabel("Status");
		panel.add(status);

		horizontalGlue = Box.createHorizontalGlue();
		panel.add(horizontalGlue);

		lblNewLabel_1 = new JLabel("lx-plot v" + Constants.VERSION);
		panel.add(lblNewLabel_1);
		
		horizontalStrut = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut);
		
		btnForceQuit = new JButton("Force quit");
		btnForceQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		panel.add(btnForceQuit);
		
		panel_4 = new JPanel();
		frame.getContentPane().add(panel_4, BorderLayout.WEST);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[] {0, 1};
		gbl_panel_4.rowHeights = new int[]{0, 0, 0};
		gbl_panel_4.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		
		btnNewButton_4 = new JButton("Minimize all");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LxPlotChart.minimizeAll();
			}
		});
		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_4.gridx = 0;
		gbc_btnNewButton_4.gridy = 0;
		panel_4.add(btnNewButton_4, gbc_btnNewButton_4);
		
		panel_6 = new JPanel();
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 0;
		gbc_panel_6.gridy = 1;
		panel_4.add(panel_6, gbc_panel_6);
		panel_6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblNewLabel_2 = new JLabel("Columns");
		panel_6.add(lblNewLabel_2);
		
		spinner_1 = new JSpinner();
		spinner_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int value = (int) spinner_1.getValue();
				if (value < 1)
					value = 1;
				LxPlotChart.cols = value;
				LxPlotChart.refreshLayout();
			}
		});
		panel_6.add(spinner_1);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setStatus(String string) {
		status.setText(string);
	}

	public void setFrameName(String _name) {
		frame.setName(_name);
	}
}

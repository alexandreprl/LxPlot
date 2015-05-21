package com.lxprl.plot.server;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConfigWindow extends JFrame {
	private JTextField columnCount;

	public ConfigWindow() {

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblNewLabel = new JLabel("Number of columns");
		panel_1.add(lblNewLabel);

		columnCount = new JTextField(LxPlotChart.cols+"");
		columnCount.setSelectionStart(0);
		columnCount.setSelectionEnd(columnCount.getText().length());
		panel_1.add(columnCount);
		columnCount.setColumns(10);

		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Ok");
		panel_2.add(btnNewButton);
		btnNewButton.addActionListener(l->{
			LxPlotChart.cols = Integer.parseInt(columnCount.getText());
			LxPlotChart.refreshLayout();
			ConfigWindow.this.setVisible(false);
		});
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}

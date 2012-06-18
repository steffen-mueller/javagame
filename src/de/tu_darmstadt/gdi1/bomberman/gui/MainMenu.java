/*
 * Copyright (c) 2008-$today.year by teambits GmbH, Darmstadt, Germany (http://www.teambits.de). All rights reserved.
 * This is CONFIDENTIAL code. Use is subject to license terms.
 *
 * * $Id$ *
 */
package de.tu_darmstadt.gdi1.bomberman.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * javadoc: Not yet commented
 */
public class MainMenu extends JFrame
{
	// WICHTIG!!!
	// Diese Datei bitte nur in Absprache mit mir (Fabian) ändern, da ich einen Designer verwendet habe
	// Sonst muss ich die Datei neu machen... Wenn ihr irgendwas haben wollt, ruft mich kurz an!
	private Gui gui;
	
	public MainMenu (Gui gui)
	{
		this.gui = gui;
		initComponents();
	}

	private void QuitButtonActionPerformed(ActionEvent e) {
		gui.quitGameGUI();
	}

	private void PlayButtonActionPerformed(ActionEvent e) {
		// TODO add your code here
	}

	private void initComponents ()
	{
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		panel1 = new JPanel();
		label1 = new JLabel();
		panel2 = new JPanel();
		button1 = new JButton();
		button3 = new JButton();
		button2 = new JButton();

		setName("this");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		panel1.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel1.setPreferredSize(new Dimension(500, 200));
		panel1.setName("panel1");
		panel1.setLayout(new GridBagLayout());
		((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {75, 0, 0};
		((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0E-4};

		label1.setText("The Amazing TU-Darmstadt Bomberman");
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		label1.setFont(label1.getFont().deriveFont(label1.getFont().getStyle() | Font.BOLD, label1.getFont().getSize() + 4f));
		label1.setName("label1");
		panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		panel2.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel2.setName("panel2");
		panel2.setLayout(new GridBagLayout());
		((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 0, 0};
		((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {45, 0, 0};
		((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
		((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

		button1.setText(">> Play >>");
		button1.setFont(button1.getFont().deriveFont(button1.getFont().getStyle() | Font.BOLD));
		button1.setName("button1");
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PlayButtonActionPerformed(e);
			}
		});
		panel2.add(button1, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		button3.setText("Options");
		button3.setName("button3");
		panel2.add(button3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 5), 0, 0));

		button2.setText("Quit");
		button2.setName("button2");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				QuitButtonActionPerformed(e);
			}
		});
		panel2.add(button2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		panel1.add(panel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.SOUTH, GridBagConstraints.NONE,
			new Insets(0, 0, 0, 0), 0, 0));
		contentPane.add(panel1, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel panel1;
	private JLabel label1;
	private JPanel panel2;
	private JButton button1;
	private JButton button3;
	private JButton button2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}

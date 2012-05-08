package de.tu_darmstadt.gdi1.framework.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * An AboutWindow is an JFrame representing an simple information 
 * window, that tells the user the essentials about your game.<br><br>
 * 
 * The content of the AboutWindow is set with the properties file
 * delivered to the constructor, see the example game.properties for details.<br><br>
 * 
 * The AboutWindow comes fully featured, just .setVisible(true). 
 * 
 * @author f_m
 */
public class AboutWindow extends JFrame{
	private Properties prop;
    private JLabel author;
    private JLabel authorLabel;
    private JButton closeButton;
    private JLabel gameName;
    private JPanel imagePanel;
    private JLabel descriptionLabel;
    private JPanel jPanel2;
    private JLabel version;
    private JLabel versionLabel;
	
    /**
     * Constructor for an new AboutWindow.
     * 
     * @param propertiesFile The properties file containing the data to display.
     * @throws java.io.IOException if the propertiesFile couldn't be found or read.
     */
    public AboutWindow(final File propertiesFile) throws  IOException {
    	prop = new Properties();
    	InputStream is = new FileInputStream(propertiesFile);
    	prop.load(is);
    	is.close();
        
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        setTitle("About: " + prop.getProperty("name"));
        setName("About");
    	
        createComponents();
        createWindow();
        
        pack();
    }

    /**
     * places the components on the window.
     */
	private void createWindow() {
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(gameName)
                .addContainerGap(140, Short.MAX_VALUE))
            .add(descriptionLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(author)
                    .add(version))
                .add(18, 18, 18)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(versionLabel)
                    .add(authorLabel))
                .add(162, 162, 162))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(gameName)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(descriptionLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(author)
                    .add(authorLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(version)
                    .add(versionLabel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(imagePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(256, Short.MAX_VALUE)
                .add(closeButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(imagePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(closeButton)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        
        setLocationRelativeTo(null);
	}

	/**
	 * creates the components for the window.
	 */
	private void createComponents() {
        imagePanel = new JPanel();
        jPanel2 = new JPanel();
        gameName = new JLabel();
        descriptionLabel = new JLabel();
        author = new JLabel();
        version = new JLabel();
        authorLabel = new JLabel();
        versionLabel = new JLabel();
        closeButton = new JButton();
        
        jPanel2.setName("jPanel2"); // NOI18N

        gameName.setFont(new Font("DejaVu Sans", 1, 24)); // NOI18N
        gameName.setForeground(new java.awt.Color(3, 2, 151)); // NOI18N
        gameName.setText(prop.getProperty("name")); // NOI18N
        gameName.setName("gameName"); // NOI18N

        //enclosing text in <html> tags lets a JLabel float over multiple lines.
        descriptionLabel.setText("<html>" + prop.getProperty("description") + "</html>"); // NOI18N
        descriptionLabel.setName("descriptionLabel"); // NOI18N

        author.setFont(new Font("DejaVu Sans", Font.BOLD, 13)); // NOI18N
        author.setText("Author:"); // NOI18N
        author.setName("author"); // NOI18N

        version.setFont(new Font("DejaVu Sans", Font.BOLD, 13)); // NOI18N
        version.setText("Version:"); // NOI18N
        version.setName("version"); // NOI18N

        authorLabel.setText(prop.getProperty("author")); // NOI18N
        authorLabel.setName("authorLabel"); // NOI18N

        versionLabel.setText(prop.getProperty("version")); // NOI18N
        versionLabel.setName("versionLabel"); // NOI18N
        
        closeButton.setText("Close"); 
        closeButton.setName("closeButton");
        closeButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});

        imagePanel.setName("imagePanel");
        if(prop.getProperty("image") != null && !prop.getProperty("image").equals("")) {
        	ImageIcon image = new ImageIcon(prop.getProperty("image"));
        	JLabel label = new JLabel();
        	label.setIcon(image);
        	label.setBorder(null);
        	label.setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
        	imagePanel.add(label);
        }
	}

}

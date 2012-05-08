package de.tu_darmstadt.gdi1.framework.view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;


/**
 * A HelpWindow for your game. The HelpWindow is basically a 
 * JFrame with an very poor embedded web browser, thats able
 * to render HTML (with some limitations, e.g. no frames) and
 * load an new page (or jump around in the currently loaded page)
 * after clicking an link. <br><br>
 * 
 * The starting Page for your help can either be a file in the 
 * local file system or an website in the www. 
 * 
 * @author f_m
 */
public class HelpWindow extends JFrame{
	private JButton closeButton;
    private JScrollPane scrollPane;
    private JTextPane htmlPane;
    
    /**
     * Constructor for an new HelpWindow from a file.
     * @param startPage the html file.
     * @throws java.io.IOException if the file couldn't be read.
     */
	public HelpWindow(File startPage) throws IOException {
		init(startPage.toURI().toURL());
	}
	
	/**
	 * Constructor for an new HelpWindow from a URL.
	 * @param url the URL to the file.
	 * @throws java.io.IOException if the file couldn't be read or
	 * 						the URL was formated wrongly.
	 */
	public HelpWindow(URL url) throws IOException {
		init(url);
	}
	
	/**
	 * Build the window.
	 * @param url the url.
	 * @throws java.io.IOException
	 */
	private void init(URL url) throws IOException {
		createComponents();
		createWindow();	
		htmlPane.setPage(url);
		//we add an listener to react on clicked links
		htmlPane.addHyperlinkListener(new HyperlinkListener() {
			
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						htmlPane.setPage(e.getURL());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		pack();
        setLocationRelativeTo(null);
	}

	/**
	 * Create the needed components.
	 */
	private void createComponents() {
		scrollPane = new JScrollPane();
		
        htmlPane = new JTextPane();
        htmlPane.setEditable(false);
        scrollPane.setViewportView(htmlPane);
        
        closeButton = new JButton();
        closeButton.setText("Close");
        closeButton.addActionListener(new ActionListener() {
        	
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});


	}

	/**
	 * Do the layout and set components.
	 */
	private void createWindow() {
		
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Help");
        setName("Form");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.LEADING)
            .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(GroupLayout.TRAILING)
                    .add(GroupLayout.LEADING, scrollPane, GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
                    .add(closeButton))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.LEADING)
            .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(scrollPane, GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(closeButton)
                .addContainerGap())
        );
        

	}

}

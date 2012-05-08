/**
 * 
 */
package de.tu_darmstadt.gdi1.framework.view;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import de.tu_darmstadt.gdi1.framework.interfaces.IHighscoreEntry;
import de.tu_darmstadt.gdi1.framework.interfaces.IHighscoreStore;

/**
 * This class contains the default highscore window of the view.
 * The highscores will be displayed inside a HTML pane as a HTML table. 
 * 
 * Based on {@link HelpWindow}
 * 
 * @author Jan
 *
 */
public class HighscoreWindow extends JFrame{
	private JButton closeButton;
    private JScrollPane scrollPane;
    private JTextPane htmlPane;
    
    /**
     * Constructor initializing the window.
     * @param highscores a highscore store that will provide the scores
     */
	public HighscoreWindow(IHighscoreStore highscores) {
		createComponents();
		createWindow();	
		
		String html = "<html><body><table>";
		html += "<tr>";
		html += "<th>#</th>";
		for (String title : highscores.getTitles()) {
			html += "<th>" + escape(title) + "</th>";
		}
		html += "</tr>";
		int position = 1;
		for (IHighscoreEntry entry : highscores.getList() ) {
			html += "<tr>";
			html += "<td>"+ position++ +".</td>";
			for (String field : entry.getValues() ) {
				html += "<td>" + escape(field) + "</td>";
			}
			html += "</tr>";
		}
		html += "</table></body></html>";
		
		htmlPane.setContentType("text/html");
		htmlPane.setText(html);
		pack();
        setLocationRelativeTo(null);
	}
	
	/**
	 * Escapes a string for use in HTML, similar to PHP htmlspecialchars().
	 * @param data the string to escape
	 * @return the escaped string
	 */
	private String escape(String data) {
		return data.replaceAll("&", "&amp;")
			.replaceAll("\"", "&quot;")
			.replaceAll("'","&#039;")
			.replaceAll("<","&lt;")
			.replaceAll(">", "&gt;");
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
        setTitle("Highscores");
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

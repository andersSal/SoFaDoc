package frames;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import main.Application;
import main.Console;
import utilities.Gender;
import utilities.Phrase;

import java.awt.Cursor;

public class ChangeTextFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnOK; 
	private JButton btnLukk; 
	private static String changeTo = ""; 
	public static String textToChange = "";
	private ImageIcon changeIcon = new ImageIcon(getClass().getResource("/change32.png"));
	private Image changeImage = new ImageIcon(getClass().getResource("/change32.png")).getImage();



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangeTextFrame frame = new ChangeTextFrame(textToChange);
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
	public ChangeTextFrame(String text) {
		setResizable(false);
		setLocationRelativeTo(Application.getSplitPane());
		textToChange = text;
		setTitle("Forandre frase til: ");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(changeImage);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 414, 213);
		contentPane.add(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sl_panel.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane, 203, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, scrollPane, 404, SpringLayout.WEST, panel);
		panel.add(scrollPane);
		
		btnLukk = new JButton("Lukk");
		btnLukk.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLukk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnLukk.setBounds(324, 227, 89, 23);
		contentPane.add(btnLukk);
		JTextArea changeArea = new JTextArea();
		changeArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnLukk.doClick();
				} else if (e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnOK.doClick();
				}
				
			}
		});
		changeArea.setWrapStyleWord(true);
		scrollPane.setViewportView(changeArea);
		changeArea.setLineWrap(true);
		changeArea.setText(textToChange);
		changeArea.setCaretPosition(textToChange.length());
		
		btnOK = new JButton("OK");
		btnOK.setToolTipText("Insert");
		btnOK.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTo = changeArea.getText();
				while (changeTo.contains("  ")) {
					changeTo = changeTo.replace("  ", " ");
				}
				if (Gender.addHeOrSheToPhrase(changeTo).equals(Gender.addHeOrSheToPhrase(textToChange)) || changeTo.equals("")) {
					dispose(); 
					JOptionPane.showConfirmDialog(Application.getSplitPane(), "Frasen ble ikke endret.", "Obs", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

					Application.textField.requestFocus();
				} else {
					int chosenCount = Console.search.getPhraseCount(textToChange);
					Console.search.removePhrase(textToChange);
					Console.search.addPhrase(new Phrase(Gender.addHeOrSheToPhrase(changeTo), chosenCount));
					dispose();
					JOptionPane.showConfirmDialog(Application.getSplitPane(), "Frasen ble endret.",  "Suksess", JOptionPane.DEFAULT_OPTION, 0, changeIcon);
					Application.textField.requestFocus();
					Application.btnSearch.doClick();
				}
		
			}
		});
		btnOK.setBounds(225, 227, 89, 23);
		contentPane.add(btnOK);
	
	}
}

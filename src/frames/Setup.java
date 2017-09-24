package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.Console;
import utilities.CustomFileChooser;

public class Setup extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageIcon imgSetup = new ImageIcon(getClass().getResource("/setupIcon.png"));
	private Image loadImg = new ImageIcon(getClass().getResource("/load.png")).getImage();

	private final JPanel contentPanel = new JPanel();
	private JLabel label_1;
	private JLabel label_2;
	private JButton btnNy, btnOK, btnFinn; 
	private JTextField textField;
	private static final String appdataFolderPath = System.getenv("LOCALAPPDATA") + File.separator + "SoFa";
	
	public Setup(String title, String info1, String info2) {
		setMinimumSize(new Dimension(500, 175));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setModal(true);
		setResizable(false);
		setTitle(title);
		setIconImage(loadImg);
		setBounds(100, 100, 500, 175);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		JLabel lblImglbl = new JLabel("",imgSetup, JLabel.CENTER);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblImglbl, 10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblImglbl, 10, SpringLayout.WEST, contentPanel);
		contentPanel.add(lblImglbl);
		{
			label_1 = new JLabel(info1);
			sl_contentPanel.putConstraint(SpringLayout.NORTH, label_1, 10, SpringLayout.NORTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.WEST, label_1, 6, SpringLayout.EAST, lblImglbl);
			label_1.setForeground(Color.DARK_GRAY);
			label_1.setFont(new Font("SansSerif", Font.PLAIN, 12));
			contentPanel.add(label_1);
		}
		{
			label_2 = new JLabel(info2);
			sl_contentPanel.putConstraint(SpringLayout.NORTH, label_2, 6, SpringLayout.SOUTH, label_1);
			sl_contentPanel.putConstraint(SpringLayout.WEST, label_2, 6, SpringLayout.EAST, lblImglbl);
			label_2.setForeground(Color.DARK_GRAY);
			label_2.setFont(new Font("SansSerif", Font.PLAIN, 12));
			contentPanel.add(label_2);
		}
		{
			textField = new JTextField("");
			sl_contentPanel.putConstraint(SpringLayout.WEST, textField, 6, SpringLayout.EAST, lblImglbl);
			sl_contentPanel.putConstraint(SpringLayout.SOUTH, textField, 0, SpringLayout.SOUTH, lblImglbl);
			sl_contentPanel.putConstraint(SpringLayout.EAST, textField, 0, SpringLayout.EAST, label_1);
			textField.setFocusable(false);
			textField.setEditable(false);
			textField.setColumns(10);
			contentPanel.add(textField);
		}
		{
			JPanel panel = new JPanel();
			sl_contentPanel.putConstraint(SpringLayout.NORTH, panel, 6, SpringLayout.SOUTH, textField);
			sl_contentPanel.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, label_1);
			sl_contentPanel.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.EAST, label_1);
			contentPanel.add(panel);
			panel.setLayout(new GridLayout(1, 0, 0, 0));
			{
				btnNy = new JButton("Nytt bibliotek");
				btnNy.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						File file = null; 
						JFileChooser fc = new CustomFileChooser("user.home");
						fc.setDialogTitle("Ny biblioteksfil");
						FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt", "text");
						fc.setFileFilter(filter);
						int result = fc.showSaveDialog(btnNy);
						if (result == JFileChooser.APPROVE_OPTION) {
							String pathName = fc.getSelectedFile().getAbsolutePath();
							if (pathName.endsWith(".txt")) {
								file = fc.getSelectedFile();
								pathName = file.getAbsolutePath();	
							} else {
								file = fc.getSelectedFile();
								pathName = file.getAbsolutePath() + ".txt";
							}
							File usrLibraryFile = new File(pathName);
							try {
								usrLibraryFile.createNewFile();
							} catch (IOException e) {}
							textField.setText(pathName);
							addPathToAppData(pathName);
						} else {
							return ; 
						}
						
					}
				});
				panel.add(btnNy);
			}
			{
				btnFinn = new JButton("Finn bibliotek");
				btnFinn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						JFileChooser fc = new CustomFileChooser("user.home");
						fc.setDialogTitle("Finn biblioteksfil");
						FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt", "text");
						fc.setFileFilter(filter);
						fc.setCurrentDirectory(new java.io.File("user.home"));
						fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
						int option = fc.showOpenDialog(btnFinn);
						if (option == JFileChooser.APPROVE_OPTION) {
							textField.setText(fc.getSelectedFile().getAbsolutePath());
						}
					}
				});
				panel.add(btnFinn);
			}
			{
				btnOK = new JButton("OK");
				btnOK.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(textField.getText().equals("")) {
							dispose();
						} else {
							String path = textField.getText(); 
							addPathToAppData(path);
							dispose();							
						}
					}
				});
				panel.add(btnOK);
				getRootPane().setDefaultButton(btnOK);
			}
		}
	}
	
	private void addPathToAppData(String usrDefinedPath) {
		File libraryLocationFile = new File(appdataFolderPath + File.separator + "Library" + File.separator + "userDefinedLibraryPath.txt" );
		PrintWriter writer = null; 
		try {
			writer = new PrintWriter(libraryLocationFile);
			writer.write(usrDefinedPath);
			writer.flush();
			writer.close();
			Console.setLibraryPath(Paths.get(usrDefinedPath));
		} catch (FileNotFoundException nf) {
			JOptionPane.showConfirmDialog(null, "Gå til c:\\Brukere\\Din Bruker\nKlikk på menyen 'Visning' og klikk på boksen 'Vis skulte elementer'"
					+ "Naviger til 'AppData/Local, slett mappen 'SoFa' og kjør programmet på nytt", "%AppData% Library-error",JOptionPane.DEFAULT_OPTION, 
					JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		} 
	}
}

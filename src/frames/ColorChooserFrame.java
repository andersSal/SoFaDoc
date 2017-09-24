package frames;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import main.Application;
import main.Console;
import modules.ColorLinks;
import net.miginfocom.swing.MigLayout;
import utilities.Format;
import utilities.MapSorter;

public class ColorChooserFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton btnLagre;
	private JPanel colorPanel;
	private ColorLinks colorLinks; 
	private Image imgColor = new ImageIcon(getClass().getResource("/color.png")).getImage();
	private ImageIcon changeIcon = new ImageIcon(getClass().getResource("/change32.png"));
	private ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/trashIcon.png"));
	private JColorChooser jcc; 
	private DefaultTableModel model; 
	private JCheckBox colorCheckbox;
	private JButton btnEndre;
	private JButton btnSlett;
	private JTextPane textPane;
	private JButton btnLukk;
	private JTable table;
	private JPanel buttonPanel;
	private JTextField textField;
	private JSeparator sep1;
	private JSeparator sep2;
	private JScrollPane visualScrollPane;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ColorChooserFrame frame = new ColorChooserFrame(); 
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
	public ColorChooserFrame() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				btnLukk.doClick();
				
			}
			public void windowOpened(WindowEvent arg0) {
				colorCheckbox.setSelected(Console.colorLinksON);
				textField.requestFocus();
			}
			
		});
		colorLinks = new ColorLinks(); 
		setIconImage(imgColor);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Administrer ColorLinks");
		setBounds(100, 100, 678, 680);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		setLocationRelativeTo(Application.getSplitPane());
		
		colorPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, colorPanel, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, colorPanel, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, colorPanel, -372, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, colorPanel, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(colorPanel);
		colorPanel.setLayout(new MigLayout("", "[][][][grow]", "[][grow]"));
		
		JButton btnBruksanvisning = new JButton("Bruksanvisning");
		btnBruksanvisning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Application.openInNotePad(Console.getUsermanualPath());

			}
		});
		colorPanel.add(btnBruksanvisning, "cell 0 0");
		jcc = new JColorChooser(); 
		ColorSelectionModel jccModel = jcc.getSelectionModel();
		jcc.setColor(Color.RED);
		ChangeListener changeListener = new ChangeListener() {
		      public void stateChanged(ChangeEvent changeEvent) {
		          displayText(false, null);
		        }
		      };
		jccModel.addChangeListener(changeListener);
		colorPanel.add(jcc, "cell 0 1");
		model = new DefaultTableModel() {

		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		Object[] columns = {"Ord","Farge"};
		model.setColumnIdentifiers(columns);
		populateTable();
		
		colorCheckbox = new JCheckBox("Slå p\u00E5 ColorLinks ved oppstart");
		springLayout.putConstraint(SpringLayout.NORTH, colorCheckbox, 618, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, colorCheckbox, 0, SpringLayout.WEST, colorPanel);
		getContentPane().add(colorCheckbox);
		colorCheckbox.setToolTipText("");
		
		JScrollPane tableScrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, tableScrollPane, 6, SpringLayout.SOUTH, colorPanel);
		springLayout.putConstraint(SpringLayout.WEST, tableScrollPane, 0, SpringLayout.WEST, colorPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, tableScrollPane, -177, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, tableScrollPane, 2, SpringLayout.EAST, colorPanel);
		getContentPane().add(tableScrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableScrollPane.setViewportView(table);
		
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
			{
				Color myGrey = new Color(240, 240, 240);
				Color platinumGrey = new Color(62, 169, 159);
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				c.setBackground(row % 2 == 0 ? myGrey : Color.WHITE );
				if (isSelected) {
					c.setBackground(platinumGrey);
				}
				return c;
			}
		});
		
		
		table.setModel(model);
		
				table.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (table.getSelectedRow() != -1 && e.getKeyCode() == KeyEvent.VK_DELETE) {
							btnSlett.doClick();
						} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
							if (table.getSelectedRow() != -1 && (table.getSelectedRow() + 1 < table.getRowCount())) {
								Object o = table.getModel().getValueAt(table.getSelectedRow() + 1, 1);
								if (! o.toString().isEmpty()) {
									String[] rgba = o.toString().split(":");
									int red = Integer.parseInt(rgba[0]);
									int green = Integer.parseInt(rgba[1]);
									int blue = Integer.parseInt(rgba[2]);
									int alpha = Integer.parseInt(rgba[3]);
									Color color = new Color(red, green, blue, alpha);
									textPane.setForeground(color);
									String text = table.getModel().getValueAt(table.getSelectedRow() + 1, 0).toString();
									text = text.replaceAll(".{140}", "$0\n");
									textPane.setText(text);
								} else {
									textPane.setText("");
								}
							} 
						} else if (e.getKeyCode() == KeyEvent.VK_UP) {
							if (table.getSelectedRow() > 0) {
								Object o = table.getModel().getValueAt(table.getSelectedRow() -1, 1);
								if (! o.toString().isEmpty()) {
									String[] rgba = o.toString().split(":");
									int red = Integer.parseInt(rgba[0]);
									int green = Integer.parseInt(rgba[1]);
									int blue = Integer.parseInt(rgba[2]);
									int alpha = Integer.parseInt(rgba[3]);
									Color color = new Color(red, green, blue, alpha);
									textPane.setForeground(color);
									String text = table.getModel().getValueAt(table.getSelectedRow() - 1, 0).toString();
									text = text.replaceAll(".{140}", "$0\n");
									textPane.setText(text);
								} else {
									textPane.setText("");
								}
							}
						} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_INSERT) {
							btnEndre.doClick();
						} else if (e.getKeyCode() == KeyEvent.VK_TAB) {
							displayText(true, null); 
						}
					}
				});
				
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				displayText(true, null); 
			}
		});
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		buttonPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.WEST, buttonPanel, 8, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, buttonPanel, 0, SpringLayout.EAST, colorPanel);
		getContentPane().add(buttonPanel);
		
		textField = new JTextField();
		textField.setToolTipText("Skriv ord du vil linke her. Skilles med komma.");
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_INSERT:
					if (! textField.getText().equals("")) btnLagre.doClick();
					break;
				case KeyEvent.VK_ESCAPE:
					textField.setText("");
					break; 
				case KeyEvent.VK_ENTER:
					if (e.isControlDown()) btnLukk.doClick();
					else if (! textField.getText().equals("")) btnLagre.doClick();
				default:
					displayText(false, e);
				}
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, buttonPanel, 6, SpringLayout.SOUTH, textField);
		springLayout.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, colorPanel);
		springLayout.putConstraint(SpringLayout.EAST, textField, 2, SpringLayout.EAST, colorPanel);
		getContentPane().add(textField);
		textField.setColumns(10);
		buttonPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		btnEndre = new JButton("Endre");
		buttonPanel.add(btnEndre);
		btnEndre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() != -1) {
					Object o = table.getModel().getValueAt(table.getSelectedRow(), 1);
					if (! o.toString().isEmpty()) {
						String[] rgba = o.toString().split(":");
						int red = Integer.parseInt(rgba[0]);
						int green = Integer.parseInt(rgba[1]);
						int blue = Integer.parseInt(rgba[2]);
						int alpha = Integer.parseInt(rgba[3]);
						Color color = new Color(red, green, blue, alpha);
						String text = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
						text = text.replaceAll(".{75}", "$0\n");
						int n = JOptionPane.showOptionDialog(table, "Vil du endre fargekoden for:\n" + text,
								"Endre", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, changeIcon, new String[]{"Ja", "Nei"}, "Ja");
						if (n == JOptionPane.YES_OPTION) {
							textPane.setText("");
							text = text.toLowerCase();
							String[] array = text.split(",");
							String newString = "";
							Set<String> uniqueWords = new HashSet<>(); 
							for (String string : array) {
								string = string.trim(); 
								if (uniqueWords.contains(string)) newString += string + ", ";
								uniqueWords.add(string);
							}
							newString = newString.substring(0, newString.length() -2); 
							textField.setText(newString);
							Console.colorLinks.remove(color);
							populateTable();
							Format.printColorLinks(Console.colorLinks);
							textField.requestFocus();
						}
						
					} else {
						textPane.setText("");
					}
				}
				
			}
		});
		btnEndre.setToolTipText("Ctrl + Insert");
		
		btnSlett = new JButton("Slett");
		buttonPanel.add(btnSlett);
		btnSlett.setToolTipText("Delete");
		springLayout.putConstraint(SpringLayout.EAST, btnSlett, -23, SpringLayout.WEST, btnLukk);
		
		btnLukk = new JButton("Lukk");
		btnLukk.setToolTipText("Ctrl + Enter");
		buttonPanel.add(btnLukk);
		
		btnLagre = new JButton("Lagre ");
		buttonPanel.add(btnLagre);
		springLayout.putConstraint(SpringLayout.NORTH, btnLagre, 20, SpringLayout.SOUTH, buttonPanel);
		springLayout.putConstraint(SpringLayout.NORTH, btnSlett, 0, SpringLayout.NORTH, btnLagre);
		springLayout.putConstraint(SpringLayout.NORTH, btnLukk, 0, SpringLayout.NORTH, btnLagre);
		springLayout.putConstraint(SpringLayout.EAST, btnLukk, -14, SpringLayout.WEST, btnLagre);
		springLayout.putConstraint(SpringLayout.WEST, btnLagre, 182, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnLagre, -222, SpringLayout.EAST, getContentPane());
		btnLagre.setToolTipText("Insert");
		
		sep1 = new JSeparator();
		springLayout.putConstraint(SpringLayout.SOUTH, sep1, -118, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, textField, 6, SpringLayout.SOUTH, sep1);
		springLayout.putConstraint(SpringLayout.WEST, sep1, 0, SpringLayout.WEST, colorPanel);
		springLayout.putConstraint(SpringLayout.EAST, sep1, 0, SpringLayout.EAST, colorPanel);
		getContentPane().add(sep1);
		
		sep2 = new JSeparator();
		springLayout.putConstraint(SpringLayout.SOUTH, buttonPanel, -7, SpringLayout.NORTH, sep2);
		springLayout.putConstraint(SpringLayout.NORTH, sep2, 610, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, sep2, 0, SpringLayout.WEST, colorPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, sep2, -6, SpringLayout.NORTH, colorCheckbox);
		springLayout.putConstraint(SpringLayout.EAST, sep2, 0, SpringLayout.EAST, colorPanel);
		getContentPane().add(sep2);
		
		visualScrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, sep1, 6, SpringLayout.SOUTH, visualScrollPane);
		springLayout.putConstraint(SpringLayout.NORTH, visualScrollPane, 6, SpringLayout.SOUTH, tableScrollPane);
		springLayout.putConstraint(SpringLayout.WEST, visualScrollPane, 0, SpringLayout.WEST, colorPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, visualScrollPane, -126, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, visualScrollPane, 0, SpringLayout.EAST, colorPanel);
		getContentPane().add(visualScrollPane);
		
		textPane = new JTextPane();
		visualScrollPane.setViewportView(textPane);
		springLayout.putConstraint(SpringLayout.NORTH, textPane, 23, SpringLayout.SOUTH, buttonPanel);
		springLayout.putConstraint(SpringLayout.WEST, textPane, 317, SpringLayout.WEST, getContentPane());
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textPane.setEditable(false);
		btnLagre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (! textField.getText().isEmpty() && textField.getText() != null) {
					Color c = jcc.getColor();
					String string = textField.getText();
					String notAllowed = "\\.[]{}()*+?^$|!";
					String notAllowedDisplay = " \\  . [ ] { } ( ) * + ? ^ $ | ! ";

					boolean isLegalString = true; 
					for (int i = 0; i < string.length(); i++) {
						if (notAllowed.contains(string.charAt(i)+"")) {
							isLegalString = false; 
						}
					}
					if (! isLegalString) {
						JOptionPane.showConfirmDialog(table, "ColorLinks kan ikke bestå av tegnene:\n" + notAllowedDisplay, 
								"Fargekode ble ikke opprettet",JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					} else {
						string = string.replaceAll(",,", ",");
						String[] wordsToAdd = string.split(",");
						boolean isInLinks = false;
						for (String s : wordsToAdd) {
							s = s.trim();
							for (String code : Console.colorLinks.values()) {
								if (code.contains("\\b" + s + "\\b")) {
									JOptionPane.showConfirmDialog(table, "Det finnes allerede en fargekode for:\n'" + s + "'", 
											"Fargekode ble ikke opprettet",JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
									isInLinks = true; 
									break; 
								}
							}
						}
						if (! isInLinks) {
							colorLinks.addLink(c, string);
							populateTable();
						}
						if (! isInLinks && isLegalString) {
							textField.setText("");
							textPane.setText("");
						}
					}		
				}
			}
		});
		btnLukk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Format.printColorLinks(Console.colorLinks);
				dispose();
			}
		});
		btnSlett.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (table.getSelectedRow() != -1) {
					Object o = table.getModel().getValueAt(table.getSelectedRow(), 1);
					String s = table.getModel().getValueAt(table.getSelectedRow(), 0).toString(); 
					if (! s.isEmpty()) {
						s = s.replaceAll(".{75}", "$0\n");
						int n = JOptionPane.showOptionDialog(table, "Vil du slette denne linken?\n" + s, "Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
								deleteIcon, new String[]{"Ja", "Nei"}, "Ja");
						if (n == JOptionPane.YES_OPTION) {
							String[] rgba = o.toString().split(":");
							int red = Integer.parseInt(rgba[0]);
							int green = Integer.parseInt(rgba[1]);
							int blue = Integer.parseInt(rgba[2]);
							int alpha = Integer.parseInt(rgba[3]);
							Color color = new Color(red, green, blue, alpha);
							Console.colorLinks.remove(color);
							populateTable();
							textPane.setText("");
							Format.printColorLinks(Console.colorLinks);
						} 
					}
				}
			}
		});

		colorCheckbox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Console.colorLinksON = true; 
				} 
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					Console.colorLinksON = false; 
				}
			}
		});
		
	}
	
	public void setText(String text) {
		String alphabet = " abcdefghijklmnopqrstuvwxyzæøå";
		String clean = "";
		for (int i = 0; i < text.length(); i++) {
			if (alphabet.contains((text.charAt(i)+"").toLowerCase())) {
				clean += text.charAt(i);
			}
		}
		clean = clean.trim();
		clean = clean.replaceAll(" ", ", ");
		textField.setText(clean);
	}
	
	public void setTextFromEditor(String text) {
		textField.setText(text);
	}
	
	private void populateTable() {
		model.setRowCount(0);
		Map<Color, String> map = getWords(); 
		int counter = 0;
		Map<Color, String> sortedHashMap = MapSorter.sortByValue(map);
		for (Color c : sortedHashMap.keySet()) {
			counter++;
			String words = map.get(c);
			model.addRow(new Object[] {words.substring(0, words.length() - 2), c.getRed() + ":" + c.getGreen() + ":" + c.getBlue() + ":" + c.getAlpha()});
		}
		while (counter < 10) {
			model.addRow(new Object[] {"", ""});
			counter++; 
		}
	}

	private Map<Color, String> getWords() {
		Map<Color, String> map = new HashMap<>();
		for (Color color : Console.colorLinks.keySet()) {
			String pattern = Console.colorLinks.get(color);
			pattern = pattern.substring(1, pattern.length() - 1);
			pattern = pattern.replace("\\b|\\b", "\\b");
			pattern = pattern.substring(2);
			pattern = pattern.replace("\\b", ", ");
			map.put(color, pattern);
		}
		return map; 
	}
	
	private void displayText(boolean tableFocus, KeyEvent e) {
		if (tableFocus && table.getSelectedRow() != -1) {
			Object o = table.getModel().getValueAt(table.getSelectedRow(), 1);
			if (! o.toString().isEmpty()) {
				String[] rgba = o.toString().split(":");
				int red = Integer.parseInt(rgba[0]);
				int green = Integer.parseInt(rgba[1]);
				int blue = Integer.parseInt(rgba[2]);
				int alpha = Integer.parseInt(rgba[3]);
				Color color = new Color(red, green, blue, alpha);
				textPane.setForeground(color);
				String text = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
				text = text.replaceAll(".{140}", "$0\n");
				textPane.setText(text);
			} else {
				textPane.setText("");
			}
		} else if (! tableFocus && textField.getText() != null) {
			String alphabet = "abcdefghijklmnopqrstuvwxyzæøå";
			Color color = jcc.getColor(); 
			textPane.setForeground(color);
			String text = textField.getText(); 
			text = text.replaceAll(".{140}", "$0\n");
			if (e != null && alphabet.contains((e.getKeyChar()+"").toLowerCase())) {
				textPane.setText(text + e.getKeyChar());				
			} else {
				textPane.setText(text);
			}
		}

	}
}

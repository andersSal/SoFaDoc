package frames;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import main.Application;
import main.Console;
import modules.WordLinks;
import utilities.Format;
import utilities.LinkedString;

public class ClickCorrectFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtOrdSomSkal;
	private JTable table;
	private JButton btnLagre; 
	private JButton btnLukk; 
	private JButton btnSlett; 
	private JCheckBox checkbox; 
	private DefaultTableModel model; 
	private WordLinks wordLinks; 
	private Image imgPanel = new ImageIcon(getClass().getResource("/correctionIcon.png")).getImage();
	private ImageIcon changeIcon = new ImageIcon(getClass().getResource("/change32.png"));
	private ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/trashIcon.png"));
	private JPanel panel;
	private JButton btnEndre;
	private JSeparator separator_1;
	private JSeparator separator_2;




	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			 for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			        if ("Nimbus".equals(info.getName())) {
			            UIManager.setLookAndFeel(info.getClassName());
			            break;
			        }
			 }
		 } catch (Exception e) {
			 System.out.println("fail");
		 }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClickCorrectFrame frame = new ClickCorrectFrame();
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
	public ClickCorrectFrame() {
		setIconImage(imgPanel);
		wordLinks = new WordLinks(); 
		setTitle("Administrer QuickFix");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 570, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		setLocationRelativeTo(Application.getSplitPane());
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				btnLukk.doClick();
				
			}
			public void windowOpened(WindowEvent arg0) {
				populateTable();
				checkbox.setSelected(Console.wordLinksON);
				txtOrdSomSkal.requestFocus();
				
			}
		
		});
		
		
		JButton btnBruksanvisning = new JButton("Bruksanvisning");
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnBruksanvisning, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnBruksanvisning, 10, SpringLayout.WEST, contentPane);
		btnBruksanvisning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Application.openInNotePad(Console.getUsermanualPath());


			}
		});
		contentPane.add(btnBruksanvisning);
		
		JPanel panel_1 = new JPanel();
		sl_contentPane.putConstraint(SpringLayout.WEST, panel_1, 0, SpringLayout.WEST, btnBruksanvisning);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, panel_1, -61, SpringLayout.SOUTH, contentPane);
		contentPane.add(panel_1);
		SpringLayout sl_panel_1 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);
		
		txtOrdSomSkal = new JTextField();
		sl_panel_1.putConstraint(SpringLayout.NORTH, txtOrdSomSkal, 0, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, txtOrdSomSkal, 0, SpringLayout.WEST, panel_1);
		txtOrdSomSkal.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnLukk.doClick();
				} else if (e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (! txtOrdSomSkal.getText().equals("")) btnLagre.doClick();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					txtOrdSomSkal.setText("");
				} 
			}
		});
		txtOrdSomSkal.setToolTipText("Ord som skal linkes skilles med komma");
		panel_1.add(txtOrdSomSkal);
		txtOrdSomSkal.setColumns(10);
		
		btnLagre = new JButton(" Lagre ");
		sl_panel_1.putConstraint(SpringLayout.EAST, btnLagre, -10, SpringLayout.EAST, panel_1);
		btnLagre.setToolTipText("Insert");
		btnLagre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = txtOrdSomSkal.getText(); 
				if (text != null) {	
					String notAllowed = "\\.[]{}()*+?^$|!";
					String notAllowedDisplay = " \\  . [ ] { } ( ) * + ? ^ $ | ! ";
					boolean isLegalString = true; 
					for (int i = 0; i < text.length(); i++) {
						if (notAllowed.contains(text.charAt(i)+"")) {
							isLegalString = false;
						}
					}
					if (! isLegalString) {
						JOptionPane.showConfirmDialog(table, "En link kan ikke bestå av tegnene:\n" + notAllowedDisplay, 
								"Link ble ikke opprettet",JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					} else {
						if (text.split(",").length == 1) {
							JOptionPane.showConfirmDialog(table, "En link må bestå av minst to ord.",
									"Link ble ikke opprettet", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
						} else {
							boolean success = wordLinks.addWordLink(text, table);
							if (success) txtOrdSomSkal.setText("");
						}
					}
						populateTable();
					}
				}
		});
		sl_panel_1.putConstraint(SpringLayout.NORTH, btnLagre, 0, SpringLayout.NORTH, panel_1);
		panel_1.add(btnLagre);
		
		btnLukk = new JButton(" Lukk  ");
		btnLukk.setToolTipText("Ctrl + Enter");
		sl_panel_1.putConstraint(SpringLayout.EAST, txtOrdSomSkal, -6, SpringLayout.WEST, btnLukk);
		sl_panel_1.putConstraint(SpringLayout.EAST, btnLukk, -6, SpringLayout.WEST, btnLagre);
		btnLukk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Format.printWordLinks(Console.wordLinks);
				dispose(); 
			}
		});

		sl_panel_1.putConstraint(SpringLayout.NORTH, btnLukk, 0, SpringLayout.NORTH, panel_1);
		panel_1.add(btnLukk);
		
		JScrollPane scrollPane = new JScrollPane();
		sl_contentPane.putConstraint(SpringLayout.EAST, panel_1, 0, SpringLayout.EAST, scrollPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, scrollPane, -141, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, scrollPane, 6, SpringLayout.SOUTH, btnBruksanvisning);
		sl_contentPane.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, scrollPane, -15, SpringLayout.EAST, contentPane);
		contentPane.add(scrollPane);
		
		String[] columns = {"Lenker"};
		model = new DefaultTableModel(columns, 0) {

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
		
		table = new JTable(model) {
			
			private static final long serialVersionUID = 1L;

			public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    if(rowIndex >= 0){
                      tip = getValueAt(rowIndex, colIndex).toString();
                    }
                } catch (RuntimeException e1){}
                return tip;
			}
	    };	
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
		
		
		
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					btnSlett.doClick(); 
				}
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_INSERT) {
					btnEndre.doClick();
				}
			}
		});
		scrollPane.setViewportView(table);
		
		checkbox = new JCheckBox("Sl\u00E5 p\u00E5 QuickFix ved oppstart");
		sl_contentPane.putConstraint(SpringLayout.WEST, checkbox, 0, SpringLayout.WEST, btnBruksanvisning);
		checkbox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Console.wordLinksON = true; 
				} else {
					Console.wordLinksON = false; 
				}
			}
		});
		contentPane.add(checkbox);
		
		panel = new JPanel();
		sl_contentPane.putConstraint(SpringLayout.NORTH, panel, 6, SpringLayout.SOUTH, scrollPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, btnBruksanvisning);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, panel, -107, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, panel, -20, SpringLayout.EAST, contentPane);
		contentPane.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		btnSlett = new JButton("  Slett  ");
		GridBagConstraints gbc_btnSlett = new GridBagConstraints();
		gbc_btnSlett.insets = new Insets(0, 0, 0, 5);
		gbc_btnSlett.gridx = 0;
		gbc_btnSlett.gridy = 0;
		panel.add(btnSlett, gbc_btnSlett);
		sl_panel_1.putConstraint(SpringLayout.WEST, btnSlett, 71, SpringLayout.EAST, txtOrdSomSkal);
		btnSlett.setToolTipText("Delete");
		
		btnEndre = new JButton("  Endre  ");
		btnEndre.setToolTipText("Ctrl + Insert");
		btnEndre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() != -1) {
					Object o = table.getModel().getValueAt(table.getSelectedRow(), 0);
					if (o != null) {
						String s = o.toString();
						s = s.replaceAll(".{75}", "$0\n");
						int n = JOptionPane.showOptionDialog(table, "Vil du endre denne linken?\n" + s, "Ctrl + Insert",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, changeIcon,
								new String[] { "Ja", "Nei" }, ("Ja"));
						if (n == JOptionPane.YES_OPTION) {
							Console.wordLinks.remove(table.getSelectedRow());
							s = s.replace(" ", "");
							String[] array = s.split(",");
							Console.allWordsInLinks.removeAll(Arrays.asList(array));
							txtOrdSomSkal.setText(s.replaceAll(",", ", "));
							txtOrdSomSkal.requestFocus();
						}
						populateTable();
					}
				}
			}
		});
		GridBagConstraints gbc_btnEndre = new GridBagConstraints();
		gbc_btnEndre.insets = new Insets(0, 0, 0, 5);
		gbc_btnEndre.gridx = 1;
		gbc_btnEndre.gridy = 0;
		panel.add(btnEndre, gbc_btnEndre);
		
		JButton btnColor = new JButton("ColorLink");
		btnColor.setToolTipText("Legg til ColorLink p\u00E5 rad i tabellen");
		GridBagConstraints gbc_btnColor = new GridBagConstraints();
		gbc_btnColor.insets = new Insets(0, 0, 0, 5);
		gbc_btnColor.gridx = 2;
		gbc_btnColor.gridy = 0;
		panel.add(btnColor, gbc_btnColor);
		sl_panel_1.putConstraint(SpringLayout.EAST, btnColor, -6, SpringLayout.WEST, btnLukk);
		btnColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String txtTable = ""; 
				if (table.getSelectedRow() != -1) {
					Object o = table.getModel().getValueAt(table.getSelectedRow(), 0);
					if (o != null) txtTable = o.toString(); 
				}
				ColorChooserFrame colorFrame = new ColorChooserFrame();
				colorFrame.setText(txtTable);
				colorFrame.setVisible(true);
			}
		});
		btnSlett.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() != -1) {
					Object o = table.getModel().getValueAt(table.getSelectedRow(), 0);
					if (o != null) {
						String s = o.toString();
						s = s.replaceAll(".{75}", "$0\n");
						int n = JOptionPane.showOptionDialog(table, "Vil du slette denne linken?\n" + s, "Delete",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, deleteIcon,
								new String[] { "Ja", "Nei" }, ("Ja"));
						if (n == JOptionPane.YES_OPTION) {
							Console.wordLinks.remove(table.getSelectedRow());
							s = s.replace(" ", "");
							String[] array = s.split(",");
							Console.allWordsInLinks.removeAll(Arrays.asList(array));
							populateTable();
						} 
					} 
				}
			}
		});
		
		separator_1 = new JSeparator();
		sl_contentPane.putConstraint(SpringLayout.NORTH, checkbox, 6, SpringLayout.SOUTH, separator_1);
		sl_contentPane.putConstraint(SpringLayout.NORTH, separator_1, 6, SpringLayout.SOUTH, panel_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, separator_1, 0, SpringLayout.WEST, btnBruksanvisning);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, separator_1, -53, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, separator_1, -15, SpringLayout.EAST, contentPane);
		contentPane.add(separator_1);
		
		separator_2 = new JSeparator();
		sl_contentPane.putConstraint(SpringLayout.NORTH, panel_1, 2, SpringLayout.SOUTH, separator_2);
		sl_contentPane.putConstraint(SpringLayout.NORTH, separator_2, 6, SpringLayout.SOUTH, panel);
		sl_contentPane.putConstraint(SpringLayout.WEST, separator_2, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, separator_2, 16, SpringLayout.SOUTH, panel);
		sl_contentPane.putConstraint(SpringLayout.EAST, separator_2, -15, SpringLayout.EAST, contentPane);
		contentPane.add(separator_2);

	}
	
	public void setTextFromEditor(String text) {
		txtOrdSomSkal.setText(text);
	}
	
	private void populateTable() {
		model.setRowCount(0);
		int counter = 0; 
		List<List<LinkedString>> linkedLists = Console.wordLinks; 
		linkedLists.sort(new Comparator<List<LinkedString>>() {

			@Override
			public int compare(List<LinkedString> l1, List<LinkedString> l2) {
				return l1.toString().compareTo(l2.toString());
			}
			
		});
		for (List<LinkedString> list : linkedLists) {
			counter++; 
			String s = list.toString();
			model.addRow(new String[] {s.replace("[", "").replace("]", "")});
		}
		while (counter < 8) {
			model.addRow(new Object[] {});
			counter++; 
		}
	}
}

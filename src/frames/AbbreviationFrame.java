package frames;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import main.Application;
import main.Console;
import modules.Abbreviation;
import net.miginfocom.swing.MigLayout;
import utilities.Format;

public class AbbreviationFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel tableModel;
	private JScrollPane scroller = new JScrollPane(); 
	private JTextField txtShort;
	private JTextField txtLong;
	private JButton btnLeggTil;
	private JButton btnFjerne;
	private JButton btnLukk;
	private Image imgEdit = new ImageIcon(getClass().getResource("/edit.png")).getImage();
	private ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/trashIcon.png"));
	private ImageIcon changeIcon = new ImageIcon(getClass().getResource("/change32.png"));

	private JCheckBox chckbxSkruPAutofullfr;
	private JButton btnEndre;
	
	private Abbreviation a; 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AbbreviationFrame frame = new AbbreviationFrame();
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
	public AbbreviationFrame() {
		a = new Abbreviation(); 
		tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		setIconImage(imgEdit);
		addWindowListener(new WindowAdapter() {			
			@Override
			public void windowOpened(WindowEvent arg0) {
				chckbxSkruPAutofullfr.setSelected(Console.autoCorrectON);
			}
			
			public void windowClosing(WindowEvent e) {
				btnLukk.doClick(); 
			}
		});
		setResizable(false);
		setTitle("Administrer AutoCorrect");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 478, 561);
		setLocationRelativeTo(Application.getSplitPane());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[452px,grow]", "[][402px][][][]"));

		
		btnFjerne = new JButton("Slett  ");
		btnFjerne.setToolTipText("Delete");
		btnFjerne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				if (index != -1) {
					String row = ((String) table.getModel().getValueAt(index, 0));
					if (row != null) {
						int option = JOptionPane.showOptionDialog(table, "Vil du fjerne forkortelsen:\n" + row.replaceAll(".{75}", "$0\n"), "Slett", JOptionPane.YES_NO_OPTION, 
								JOptionPane.QUESTION_MESSAGE, deleteIcon, new String[]{"Ja", "Nei"}, "Ja");
						if (option == JOptionPane.YES_OPTION) {
							String[] arr = row.split(", ");
							Console.autoCorrectMap.remove(arr[0].trim());
							Console.autoCorrectMap.remove(arr[1].trim());
							populateTable();
							txtShort.requestFocus();
						} 
						
					}
				}
				
			}
		});

		btnEndre = new JButton("Endre");
		btnEndre.setToolTipText("Ctrl + Insert");
		btnEndre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				if (index != -1) {
					String row = ((String) table.getModel().getValueAt(index, 0));
					if (row != null) {
						int option = JOptionPane.showOptionDialog(table, "Vil du endre denne forkortelsen?\n" + row.replaceAll(".{75}", "$0\n"), 
								"Endre", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, changeIcon, new String[]{"Ja", "Nei"}, "Ja");
						if (option == JOptionPane.YES_OPTION) {
							String[] string = ((String) table.getModel().getValueAt(index, 0)).split(", ");
							String s = Console.autoCorrectMap.get(string[0]);
							Console.autoCorrectMap.remove(string[0].trim());
							Console.autoCorrectMap.remove(string[1].trim());
							populateTable();
							txtShort.setText(string[0]);
							txtLong.setText(s);
							txtShort.requestFocus();
						}	
					}
				}
				
			}
		});

		btnLukk = new JButton("Lukk  ");
		btnLukk.setToolTipText("Ctrl + Enter");
		btnLukk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Format.printAbbreviations(Console.autoCorrectMap);
				dispose(); 
			}
		});

		txtShort = new JTextField();
		txtShort.setToolTipText("Skriv forkortelse her...");
		txtShort.setColumns(10);
		txtLong = new JTextField();
		txtLong.setToolTipText("Skriv uttrykk her...");
		txtLong.setColumns(10);		
		
		table = new JTable(){
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
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Object[] columns = {"Forkortelse", "Uttrykk"}; 
		tableModel.setColumnIdentifiers(columns);
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
		
		table.setModel(tableModel);
		scroller = new JScrollPane(table);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		chckbxSkruPAutofullfr = new JCheckBox("Slå p\u00E5 AutoCorrect ved oppstart");
		chckbxSkruPAutofullfr.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Console.autoCorrectON = true; 
				} 
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					Console.autoCorrectON = false; 
				}
			}
		});
		
		contentPane.add(scroller, "cell 0 1,alignx left,aligny top");
		contentPane.add(txtShort, "cell 0 2,growx");
		contentPane.add(txtLong, "cell 0 2,growx");
		contentPane.add(btnEndre, "flowx,cell 0 3,growx");
		contentPane.add(btnFjerne, "cell 0 3,growx");
		
		btnLeggTil = new JButton("Lagre");
		btnLeggTil.setToolTipText("Insert");
		btnLeggTil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!txtShort.getText().equals("") && !txtLong.getText().equals("")) {
					boolean added = a.addAbbreviation(txtShort.getText().toLowerCase().trim(), txtLong.getText().trim());
					String k1 = txtShort.getText().trim();
					String k2 = "";
					if (Character.isLowerCase(k1.charAt(0))) {
						k2 = (k1.charAt(0)+"").toUpperCase() + k1.substring(1);
					} else {
						k2 = (k1.charAt(0)+"").toLowerCase() + k1.substring(1);
					}
					String s = (k1 + ", " + k2).replaceAll(".{75}", "$0\n");
					if (added == false) {
						JOptionPane.showConfirmDialog(table, "Det finnes allerede en forkortelse:\n" + s,
								"Forkortelse ble ikke opprettet.", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					} else {
						populateTable();
						txtLong.setText("");
						txtShort.setText("");
						txtShort.requestFocus();
					} 
				}		
			}
		});
		contentPane.add(btnLeggTil, "cell 0 3,growx");
		contentPane.add(btnLukk, "cell 0 3,growx");
		contentPane.add(chckbxSkruPAutofullfr, "cell 0 4");

		addKeyBindings(); 
		populateTable(); 
	}
	


	
	private void addKeyBindings() {
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					btnFjerne.doClick();
				} else if (e.getKeyCode() == KeyEvent.VK_INSERT) {
					if (e.isControlDown()) {
						btnEndre.doClick();
					}
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (e.isControlDown()) btnLukk.doClick();
				}
				
			}
		});
		txtLong.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (! txtShort.getText().isEmpty() && ! txtLong.getText().isEmpty()) btnLeggTil.doClick();
					if (e.isControlDown()) btnLukk.doClick();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					txtLong.setText("");
				} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					if (e.isControlDown()) btnLukk.doClick();
				}
				
			}
		});
		
		txtShort.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					txtShort.setText("");
					txtLong.setText("");
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (e.isControlDown()) btnLukk.doClick();
				}
				
			}
		});
	}

	private void populateTable() {
		tableModel.setRowCount(0);
		List<String> shortStrings = new ArrayList<>(); 
		for (String s : Console.autoCorrectMap.keySet()) {
			if (! shortStrings.contains(s.toLowerCase())) {
				shortStrings.add(s.toLowerCase()); 
			}
		}
		shortStrings.sort((a, b) -> a.toLowerCase().compareTo(b.toLowerCase()));

		int size = shortStrings.size(); 
		if (size < 23) {
			tableModel.setRowCount(23);
		} else {
			tableModel.setRowCount(size);
		}
		tableModel.setColumnCount(2);
		int counter = 0; 
		for (String key: shortStrings) {
			String keyCap = (key.charAt(0)+"").toUpperCase() + key.substring(1);
			String value = Console.autoCorrectMap.get(key);
			String valueCap = (value.charAt(0)+"").toUpperCase() + value.substring(1);
			tableModel.setValueAt(key + ", " + keyCap, counter, 0);
			tableModel.setValueAt(value + ", " + valueCap, counter, 1);
			counter += 1; 
		}
	
	}
	
	


}

package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import frames.AbbreviationFrame;
import frames.ChangeTextFrame;
import frames.ClickCorrectFrame;
import frames.ColorChooserFrame;
import frames.Setup;
import modules.WordLinks;
import net.miginfocom.swing.MigLayout;
import utilities.Format;
import utilities.Gender;
import utilities.LinkedString;
import utilities.Phrase;
import utilities.Search;

public class Application extends JFrame implements DragGestureListener  {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame; 
	private JTable table;
	public static JTextField textField;
	private DefaultTableModel tableModel; 
	private JScrollPane tableScroller;
	private static JSplitPane splitPane;
	private UndoManager undoManager;

	//JBUTTONS
	private JButton btnClipboard;
	private JButton btnClear;
	private JButton btnAdd;
	private JButton btnDelete;
	private JButton btnChange;
	public static JButton btnSearch;
	private JButton	btnSelect;
	private static JToggleButton tglAutoCorrect;
	private static JToggleButton tglGender;
	private JToggleButton tglColor; 
	private JToggleButton tglCorrection; 

	private List<String> strings = new ArrayList<>(); 
	private static char gender = 'F';
	private boolean isDividerDown = false; 
	private int numHits;
	
	private Phrase lastPhraseDeleted; 
	private int deletedChosenCount; 
	private String lastEditorText = ""; 
	private boolean isInFocusTable = false; 
	private boolean isInFocusBtnClipBoard = false; 
	private JMenuItem mntmAvslutt; 

	private String[] options = {"Ja", "Nei", "OK", "Lukk"};
	
	//ICONS
	private Image imgProgram = new ImageIcon(getClass().getResource("/mainIcon.png")).getImage();
	
	private Image imgMale = new ImageIcon(getClass().getResource("/maleIcon32.png")).getImage();
	private Image imgFemale = new ImageIcon(getClass().getResource("/female32.png")).getImage(); 
	private ImageIcon maleIcon = new ImageIcon(imgMale);
	private ImageIcon femaleIcon = new ImageIcon(imgFemale); 
	private ImageIcon clipIcon = new ImageIcon(getClass().getResource("/sciccors322.png"));
	private ImageIcon addIcon = new ImageIcon(getClass().getResource("/add32.png"));
	private ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/trashIcon.png"));
	private ImageIcon changeIcon = new ImageIcon(getClass().getResource("/change32.png"));
	private ImageIcon clearIcon = new ImageIcon(getClass().getResource("/transferIcon.png"));
	private ImageIcon autoIcon = new ImageIcon(getClass().getResource("/autoIcon.png"));
	private ImageIcon folderIcon = new ImageIcon(getClass().getResource("/folder.png"));
	private ImageIcon searchIcon = new ImageIcon(getClass().getResource("/searchIcon.png"));
	private ImageIcon copyrightIcon = new ImageIcon(getClass().getResource("/copyright-icon.png"));
	private ImageIcon colorIcon32 = new ImageIcon(getClass().getResource("/colorIcon32.png"));
	private ImageIcon correctionIcon = new ImageIcon(getClass().getResource("/correctionIcon.png")); 
	private static JTextPane textPane;
	private StyledDocument textEditorDoc;
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application frame = new Application();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Application() {
		setMinimumSize(new Dimension(640, 670));
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				frame.setSize(arg0.getComponent().getWidth(), arg0.getComponent().getHeight());
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				int option = JOptionPane.showOptionDialog(splitPane, "Sikker på at du vil avslutte?", "Bekreft valg", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Ja", "Nei"}, options[0]);
				if (option == JOptionPane.YES_OPTION) {
					Format.printLibrary(Console.search.getPhrases());	
					System.exit(0);
				}
			}
			public void windowOpened(WindowEvent arg0) {
				tglAutoCorrect.setSelected(Console.autoCorrectON);
				tglColor.setSelected(Console.colorLinksON);
				tglCorrection.setSelected(Console.wordLinksON);
				textField.requestFocus();
				if (tglColor.isSelected()) {
					tglColor.setToolTipText("Skru av ColorLinks (F9)");
				} else {
					tglColor.setToolTipText("Skru på ColorLinks (F9)");
				}
				if (tglAutoCorrect.isSelected()) {
					tglAutoCorrect.setToolTipText("Skru av AutoCorrect (F11)");
				} else {
					tglAutoCorrect.setToolTipText("Skru p\u00E5 AutoCorrect (F11)");
				}
				if (tglCorrection.isSelected()) {
					tglCorrection.setToolTipText("Skru av QuickFix (F7)");
				} else {
					tglCorrection.setToolTipText("Skru på QuickFix (F7)");
				}
				
			}
		});
		
		
		frame = new JFrame();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("SoFa 2.5");
		getContentPane().setLayout(new MigLayout("", "[grow]", "[grow]"));
		setIconImage(imgProgram);
		JDesktopPane desktopPane = new JDesktopPane();
		getContentPane().add(desktopPane, "cell 0 0,grow");
		SpringLayout sl_desktopPane = new SpringLayout();
		desktopPane.setLayout(sl_desktopPane);
		
		textField = new JTextField();
		sl_desktopPane.putConstraint(SpringLayout.WEST, textField, 10, SpringLayout.WEST, desktopPane);
		sl_desktopPane.putConstraint(SpringLayout.SOUTH, textField, 33, SpringLayout.NORTH, desktopPane);
		sl_desktopPane.putConstraint(SpringLayout.EAST, textField, -10, SpringLayout.EAST, desktopPane);
		textField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		desktopPane.add(textField);
		textField.setColumns(10);
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1 && !strings.isEmpty()) {
					setDivider(strings.size());
				}
				if (e.getClickCount() == 2) {
					textField.setText("");
				}
			}
		});
		
		splitPane = new JSplitPane();
		sl_desktopPane.putConstraint(SpringLayout.NORTH, splitPane, 36, SpringLayout.NORTH, desktopPane);
		sl_desktopPane.putConstraint(SpringLayout.SOUTH, splitPane, -10, SpringLayout.SOUTH, desktopPane);
		sl_desktopPane.putConstraint(SpringLayout.WEST, splitPane, 10, SpringLayout.WEST, desktopPane);
		sl_desktopPane.putConstraint(SpringLayout.EAST, splitPane, -81, SpringLayout.EAST, desktopPane);
		splitPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				 splitPane.setDividerLocation(0);
			}
		});
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		setDivider(0);
		desktopPane.add(splitPane);
		
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
		
		table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				isInFocusTable = true; 
			}
			public void focusLost(FocusEvent e) {
				isInFocusTable = false; 
			}
			
		});
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setFillsViewportHeight(true);
		table.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	    DragSource ds = new DragSource(); 
	    @SuppressWarnings("unused")
		DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(table, DnDConstants.ACTION_COPY, (DragGestureListener) this);
		table.setDragEnabled(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("SansSerif", Font.PLAIN, 14));
		Object[] columns = {""}; 
		table.setRowHeight(18);
		tableModel = new DefaultTableModel(); 	
		tableModel.setColumnIdentifiers(columns);
		table.setModel(tableModel);
		tableScroller = new JScrollPane(table);
		tableScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitPane.setLeftComponent(tableScroller);
		
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * 
			 */
			
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
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitPane.setRightComponent(scrollPane);
		
		textPane = new JTextPane();
		textPane.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
					
				if ((e.getModifiers() & ActionEvent.CTRL_MASK) ==ActionEvent.CTRL_MASK) {
					if (e.getWheelRotation() > 0) {
						clickAndCorrect(false); 
					} else {
						clickAndCorrect(true);
					}
				}

			}

		});

		textPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!e.isConsumed() && e.getClickCount() == 2 && textPane.getText() != null) {
					clickAndCorrect(true);
				} 
			}
		});

		textPane.setFont(new Font("Arial", Font.PLAIN, 14));
		scrollPane.setViewportView(textPane);
		
		btnSelect = new JButton("");
		sl_desktopPane.putConstraint(SpringLayout.WEST, btnSelect, 6, SpringLayout.EAST, splitPane);
		btnSelect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSelect.setToolTipText("Endre kildebibliotek");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showOptionDialog(splitPane, "Vil du endre til et annet bibliotek?", "Kilde", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Ja", "Nei"}, "Ja");
				if (option == JOptionPane.YES_OPTION) {
					String message = ""; String title = ""; 
					setDivider(0);
					String before = Console.getLibraryPath().toString();
					Format.printLibrary(Console.search.getPhrases());	
					Setup setup = new Setup("Endre kildebibliotek", "Vennligst angi ønsket filplassering for biblioteket. Programmet vil lese", "og skrive til denne filen inntil ny endring foretas."); 
					setup.setLocationByPlatform(true);
					Console.application.setVisible(false);
					Console.application.setLocationByPlatform(true); 
					setup.setVisible(true);
					String after = Console.getLibraryPath().toString();
					if (after == null || before.equals(after)|| ! after.endsWith(".txt")) {
						message =  "Kildebibliotek ble ikke endret";
						title = "Obs";
					} else {
						message = "Kildebiblioteket ble endret";
						title = "Suksess";
						Console.search = new Search();
						executeSearch();
						setDivider(0);	
					}
					Console.application.setLocationByPlatform(true);
					Console.application.setVisible(true);
					JOptionPane.showOptionDialog(splitPane, message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
							null, new String[]{"OK"}, "OK");
					textField.requestFocus();
				}
				
			}
		});
		
		btnClear = new JButton("");
		sl_desktopPane.putConstraint(SpringLayout.WEST, btnClear, 6, SpringLayout.EAST, splitPane);
		btnClear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!textPane.getText().equals("")) {
					int n = JOptionPane.showOptionDialog(splitPane, "Vil du slette editoren?", "Ny", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, clearIcon, new String[]{"Ja", "Nei"}, options[0]);
					if (n == JOptionPane.YES_OPTION) {
						lastEditorText = textPane.getText(); 
						textPane.setText("");
						textField.setText("");
						textField.requestFocus();
					}
				} 
				table.requestFocus();
			}
		});
		btnClear.setToolTipText("Visk ut teksteditor (F3)");
		btnClear.setIcon(clearIcon);
		desktopPane.add(btnClear);
		
		btnDelete = new JButton("");
		sl_desktopPane.putConstraint(SpringLayout.NORTH, btnSelect, 6, SpringLayout.SOUTH, btnDelete);
		sl_desktopPane.putConstraint(SpringLayout.WEST, btnDelete, 6, SpringLayout.EAST, splitPane);
		btnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() != -1) {
					String selected = (String) table.getValueAt(table.getSelectedRow(), 0);
					int n = JOptionPane.showOptionDialog(splitPane, "Vil du slette denne frasen fra biblioteket?", "Delete"
							,JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Ja", "Nei"}, "Ja");
					if (selected != null && n == 0) {
						try {
							deletedChosenCount = Console.search.getPhraseCount(selected);
							Console.search.removePhrase(selected);
							strings.clear();
							strings.addAll(Console.search.getPhrases().stream().map(Phrase::toString).collect(Collectors.toList()));
							executeSearch();
							String s = selected.replaceAll(".{150}", "$0\n");
							JOptionPane.showOptionDialog(splitPane, "Følgende frase ble fjernet fra biblioteket:\n' " + s  + " '",  "Slettet", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, 
									deleteIcon, new String[]{"Takk"}, "Takk");
							lastPhraseDeleted = new Phrase(Gender.addHeOrSheToPhrase(selected), deletedChosenCount); 
						} catch (IllegalArgumentException e1) {
							JOptionPane.showConfirmDialog(splitPane, "Kunne ikke finne frasen i biblioteket", "Obs", JOptionPane.DEFAULT_OPTION); 

						} 
					}
				}
			}
		});
		
		btnAdd = new JButton("");
		sl_desktopPane.putConstraint(SpringLayout.WEST, btnAdd, 6, SpringLayout.EAST, splitPane);
		btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selected = textPane.getSelectedText();
				if (selected != null) {
					selected = selected.trim(); 
					int option = JOptionPane.showOptionDialog(splitPane, "Vil du lagre denne frasen i biblioteket?", "Insert", JOptionPane.YES_NO_OPTION, 
							JOptionPane.QUESTION_MESSAGE, null, new String[]{"Ja", "Nei"}, "Ja");
					if (option == 0) {	
						try {
							//fjerner overflødige mellomrom.
							while (selected.contains("  ")) {
								selected = selected.replaceAll("  ", " ");
							}
							Console.search.addPhrase(selected);
							strings.clear();
							strings.addAll(Console.search.getPhrases().stream().map(Phrase::toString).collect(Collectors.toList()));
							executeSearch();
							setDivider(0);
							selected = selected.replaceAll(".{150}", "$0\n");
							JOptionPane.showOptionDialog(splitPane, "Følgende frase ble lagt til i biblioteket:\n' " + selected + " '",  "Suksess", 
									JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, addIcon, new String[]{"OK"}, "OK");
							
						} catch (IllegalArgumentException e1) {
					        JOptionPane.showConfirmDialog(splitPane, "Frasen finnes allerede i biblioteket", "Frase ble ikke lagt til",JOptionPane.DEFAULT_OPTION, 
					        		JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
		});
		btnAdd.setToolTipText("Legg til frase (Insert)");
		btnAdd.setIcon(addIcon);
		desktopPane.add(btnAdd);
		btnDelete.setToolTipText("Slett frase (Delete)");
		btnDelete.setIcon(deleteIcon);
		desktopPane.add(btnDelete);
		
		btnChange = new JButton("");
		sl_desktopPane.putConstraint(SpringLayout.NORTH, btnDelete, 6, SpringLayout.SOUTH, btnChange);
		sl_desktopPane.putConstraint(SpringLayout.NORTH, btnChange, 6, SpringLayout.SOUTH, btnAdd);
		sl_desktopPane.putConstraint(SpringLayout.WEST, btnChange, 6, SpringLayout.EAST, splitPane);
		btnChange.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				if (index != -1) {
					String selectedText = (String) table.getModel().getValueAt(index, 0);
					ChangeTextFrame frame = new ChangeTextFrame(selectedText);
					frame.setLocationRelativeTo(splitPane);
					frame.setVisible(true);
				}
			}
		});
		btnChange.setToolTipText("Forandre frase (Ctrl + Insert).");
		btnChange.setIcon(changeIcon);
		desktopPane.add(btnChange);
		
		tglGender = new JToggleButton("");
		sl_desktopPane.putConstraint(SpringLayout.WEST, tglGender, 6, SpringLayout.EAST, splitPane);
		tglGender.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tglGender.setIcon(femaleIcon);
		tglGender.setToolTipText("Forandre til " + Character.toString((char) 0x2642) + " (F12)");
		tglGender.addActionListener(new ActionListener() {
			int counter = 0; 
				public void actionPerformed(ActionEvent e) {
						tglGender.setSelected(false);
						if (counter % 2 == 0) {
							gender = 'M';
							tglGender.setToolTipText("Forandre til " + Character.toString((char) 0x2640) + " (F12)");
							tglGender.setIcon(maleIcon);
						} else {
							gender = 'F';
							tglGender.setToolTipText("Forandre til " + Character.toString((char) 0x2642) + " (F12)");
							tglGender.setIcon(femaleIcon);
						}
						counter += 1;
						if (numHits > 0) {
							executeSearch();
							table.requestFocus();
						}
				}
			});
		desktopPane.add(tglGender);
		tglAutoCorrect = new JToggleButton("");
		sl_desktopPane.putConstraint(SpringLayout.NORTH, tglAutoCorrect, 6, SpringLayout.SOUTH, tglGender);
		sl_desktopPane.putConstraint(SpringLayout.WEST, tglAutoCorrect, 6, SpringLayout.EAST, splitPane);
		tglAutoCorrect.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) {
					AbbreviationFrame abbreviationFrame = new AbbreviationFrame();
					abbreviationFrame.setVisible(true);
				}
				textPane.requestFocus();
				textPane.setCaretPosition(textPane.getText().length());
			}
		});
		tglAutoCorrect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tglAutoCorrect.setToolTipText("Skru p\u00E5 AutoCorrect (F11)");

		tglAutoCorrect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tglAutoCorrect.isSelected()) {
					tglAutoCorrect.setToolTipText("Skru av AutoCorrect (F11)");
				} else {
					tglAutoCorrect.setToolTipText("Skru p\u00E5 AutoCorrect (F11)");
				}
			}
		});
		tglAutoCorrect.setIcon(autoIcon);
		desktopPane.add(tglAutoCorrect);
		
		btnClipboard = new JButton("");
		sl_desktopPane.putConstraint(SpringLayout.NORTH, btnAdd, 6, SpringLayout.SOUTH, btnClipboard);
		sl_desktopPane.putConstraint(SpringLayout.NORTH, btnClipboard, 6, SpringLayout.SOUTH, btnClear);
		sl_desktopPane.putConstraint(SpringLayout.WEST, btnClipboard, 6, SpringLayout.EAST, splitPane);
		btnClipboard.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				isInFocusBtnClipBoard = true; 
			}
			public void focusLost(FocusEvent e) {
				isInFocusBtnClipBoard = false; 
			}
		});
		btnClipboard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClipboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((isInFocusTable || isInFocusBtnClipBoard) && table.getSelectedRow() != -1) {
					String textToCopy = (String) table.getValueAt(table.getSelectedRow(), 0);
					//Fjerne _ mellom wordlinks.
					textToCopy = textToCopy.replaceAll("_", " ");
					StringSelection stringSelection = new StringSelection(textToCopy);
					Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipBoard.setContents(stringSelection, null);
					setDivider(0);
					textToCopy = textToCopy.replaceAll(".{150}", "$0\n");
					JOptionPane.showConfirmDialog(splitPane, "Følgende frase ble kopiert til utklippstavle:\n' " + textToCopy + " '", "Ctrl + v for å lime inn...", JOptionPane.DEFAULT_OPTION, 0, clipIcon);
					Console.search.addChosencount(textToCopy);
					executeSearch();
					setDivider(0);
				} else if (! textPane.getText().equals("")) {
					String textToCopy = textPane.getText();
					//Fjerne _ mellom wordlinks.
					textToCopy = textToCopy.replaceAll("_", " ");
					StringSelection stringSelection = new StringSelection(textToCopy);
					Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipBoard.setContents(stringSelection, null);
					lastEditorText = textPane.getText();
					textPane.setText("");
					JOptionPane.showConfirmDialog(splitPane, "Editoren ble kopiert til utklippstavlen.", "CTRL V for å lime inn...", JOptionPane.DEFAULT_OPTION, 0, clipIcon);
				}
				textField.setText("");
				textField.requestFocus();
			}
		});
		btnClipboard.setToolTipText("Kopier editor til utklippstavle (F2)");
		btnClipboard.setIcon(clipIcon);
		desktopPane.add(btnClipboard);
		
		
		btnSelect.setIcon(folderIcon);
		desktopPane.add(btnSelect);
	
		btnSearch = new JButton("");
		sl_desktopPane.putConstraint(SpringLayout.NORTH, tglGender, 6, SpringLayout.SOUTH, btnSearch);
		sl_desktopPane.putConstraint(SpringLayout.NORTH, btnSearch, 0, SpringLayout.NORTH, splitPane);
		sl_desktopPane.putConstraint(SpringLayout.WEST, btnSearch, 6, SpringLayout.EAST, splitPane);
		btnSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {			
				executeSearch();
				table.requestFocus();
			}
		});
		btnSearch.setToolTipText("S\u00F8k ");
		btnSearch.setIcon(searchIcon);
		desktopPane.add(btnSearch);
		
		tglColor = new JToggleButton("");
		tglColor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tglColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					ColorChooserFrame colorFrame = new ColorChooserFrame(); 
					String selected = textPane.getSelectedText();
					if (selected != null) {
						colorFrame.setTextFromEditor(selected);
					}
					colorFrame.setVisible(true);
				}
			}
		});
		tglColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tglColor.isSelected()) {
					textPane.requestFocus();
					colorWords();
					tglColor.setToolTipText("Skru av ColorLinks (F9)");
				} else {
					clearTextColors();
					textPane.requestFocus();
					tglColor.setToolTipText("Skru på ColorLinks (F9)");
				}
			}
		});
		tglColor.setIcon(colorIcon32);
		sl_desktopPane.putConstraint(SpringLayout.NORTH, tglColor, 6, SpringLayout.SOUTH, tglAutoCorrect);
		sl_desktopPane.putConstraint(SpringLayout.WEST, tglColor, 6, SpringLayout.EAST, splitPane);
		desktopPane.add(tglColor);
		
		
		tglCorrection = new JToggleButton("");
		sl_desktopPane.putConstraint(SpringLayout.NORTH, btnClear, 6, SpringLayout.SOUTH, tglCorrection);
		tglCorrection.setIcon(correctionIcon);
		tglCorrection.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tglCorrection.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					String selected = textPane.getSelectedText(); 
					ClickCorrectFrame frame = new ClickCorrectFrame(); 
					if (selected != null) {
						frame.setTextFromEditor(selected);
					}
					frame.setVisible(true); 
				}
			}
		});
		tglCorrection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tglCorrection.isSelected()) {
					tglCorrection.setToolTipText("Skru av QuickFix (F7)");
				} else {
					tglCorrection.setToolTipText("Skru på QuickFix (F7)");
				}
			}
		});
		sl_desktopPane.putConstraint(SpringLayout.NORTH, tglCorrection, 6, SpringLayout.SOUTH, tglColor);
		sl_desktopPane.putConstraint(SpringLayout.WEST, tglCorrection, 6, SpringLayout.EAST, splitPane);
		desktopPane.add(tglCorrection);

		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFil = new JMenu("Fil");
		menuBar.add(mnFil);
		
		JMenuItem mntmEndreKildebibliotek = new JMenuItem("Endre kildebibliotek");
		mntmEndreKildebibliotek.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelect.doClick();
			}
		});
		mnFil.add(mntmEndreKildebibliotek);
		
		JSeparator separator = new JSeparator();
		mnFil.add(separator);
		
		mntmAvslutt = new JMenuItem("Avslutt");
		mntmAvslutt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showOptionDialog(splitPane, "Sikker på at du vil avslutte?", "Bekreft valg", JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE, null, new String[]{"Ja", "Nei"}, options[0]);
				if (option == JOptionPane.YES_OPTION) {
					Format.printLibrary(Console.search.getPhrases());	
					System.exit(0);
				}
			}
		});
		mnFil.add(mntmAvslutt);
		
		JSeparator separator_1 = new JSeparator();
		mnFil.add(separator_1);
		
		JMenu mnAutofullfr = new JMenu("AutoCorrect");
		menuBar.add(mnAutofullfr);
		
		JMenuItem mntmRedigerForkortelser = new JMenuItem("Administrer AutoCorrect");
		mnAutofullfr.add(mntmRedigerForkortelser);
		mntmRedigerForkortelser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AbbreviationFrame frame = new AbbreviationFrame(); 
				frame.setVisible(true);
			}
		});
		
		JMenu mnColor = new JMenu("ColorLinks");
		menuBar.add(mnColor);
		
		JMenuItem mntmNyLink = new JMenuItem("Administrer ColorLinks");
		mntmNyLink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ColorChooserFrame colorFrame = new ColorChooserFrame(); 
				colorFrame.setVisible(true);
			}
		});
		mnColor.add(mntmNyLink);
		
		JMenu mnKlikkrett = new JMenu("QuickFix");
		menuBar.add(mnKlikkrett);
		
		JMenuItem mntmRedigerKlikkrett = new JMenuItem("Administrer QuickFix");
		mntmRedigerKlikkrett.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClickCorrectFrame frame = new ClickCorrectFrame();
				frame.setVisible(true);
			}
		});
		mnKlikkrett.add(mntmRedigerKlikkrett);
				
		JMenu mnAngre = new JMenu("Angre");
		menuBar.add(mnAngre);
		
		JMenuItem mntmAngreSisteSletting = new JMenuItem("Siste frase slettet");
		mntmAngreSisteSletting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (lastPhraseDeleted != null) {
						Console.search.addPhrase(lastPhraseDeleted);
						List<String> tempWord = new ArrayList<>(); 
						tempWord.add(lastPhraseDeleted.toString());
						String s = Gender.changeDisplay(tempWord, gender).get(0); 
						s = s.replaceAll(".{150}", "$0\n");
						JOptionPane.showOptionDialog(splitPane, "Frasen: \n' " + s +
								" '\ner tilbake i biblioteket.", "Du ombestemte deg", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, addIcon, new String[]{"Takk"}, "Takk");
						executeSearch();
					}
				} catch (IllegalArgumentException e1) {}
			}
		});
		mnAngre.add(mntmAngreSisteSletting);
		
		JMenuItem mntmGjennopprettSisteTeksteditor = new JMenuItem("Gjennopprett siste teksteditor");
		mntmGjennopprettSisteTeksteditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textPane.getText().equals("")) {
					textPane.setText(lastEditorText);
				} else {
					String currentEditorText = textPane.getText(); 
					textPane.setText(lastEditorText);
					lastEditorText = currentEditorText; 
				}
				colorWords();

			}
		});
		mnAngre.add(mntmGjennopprettSisteTeksteditor);
		
		JMenu mnHjelp = new JMenu("Hjelp");
		menuBar.add(mnHjelp);
		
		JMenuItem mntmHurtigTaster = new JMenuItem("Hurtigtaster");
		mnHjelp.add(mntmHurtigTaster);
		
		JMenuItem mntmBruksanvisning = new JMenuItem("Bruksanvisning");
		mntmBruksanvisning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openInNotePad(Console.getUsermanualPath());
			}
		});
		
		mnHjelp.add(mntmBruksanvisning);
		mntmHurtigTaster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openInNotePad(Console.getUserManualKeysPath());
			}
		});
		
		JMenu mnOm = new JMenu("Om");
		menuBar.add(mnOm);
		
		JMenuItem mntmSofa = new JMenuItem("SoFa");
		mntmSofa.addActionListener(new ActionListener() {
			int c = 0x2122;
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showConfirmDialog(splitPane, "SoFa" + Character.toString((char) c) + "\nVersjon 2.5" + "\nUtviklet av Anders Salvesen\n" + 
					"Copyright Anders Salvesen (2016)", "Om SoFa", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, copyrightIcon);
			}
		});
		mnOm.add(mntmSofa);
	
		
		textEditorDoc = textPane.getStyledDocument();
		textPane.getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
		
		
		addUndoRedoFunctionality();
		addKeyBindings();
		frame.pack();
	}
	

	private void addUndoRedoFunctionality() {
		undoManager = new UndoManager();
		Document doc = textPane.getDocument();
		doc.addUndoableEditListener(new UndoableEditListener() {
		    @Override
		    public void undoableEditHappened(UndoableEditEvent e) {
		        undoManager.addEdit(e.getEdit());
		    }
		});

		InputMap im = textPane.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap am = textPane.getActionMap();

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Undo");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Redo");

		am.put("Undo", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
		    public void actionPerformed(ActionEvent e) {
		        try {
		            if (undoManager.canUndo()) {
		                undoManager.undo();
		            }
		        } catch (CannotUndoException exp) {
		            exp.printStackTrace();
		        }
		    }
		});
		am.put("Redo", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			
			@Override
		    public void actionPerformed(ActionEvent e) {
		        try {
		            if (undoManager.canRedo()) {
		                undoManager.redo();
		            }
		        } catch (CannotUndoException exp) {
		            exp.printStackTrace();
		        }
		    }
		});
		
	}
	/*
	 * next == true  --> vil ha next
	 * next == false --> vil ha previous
	 */
	private int clickAndCorrect(boolean next) {
		if (! tglCorrection.isSelected()) return -1; 
		while (textPane.getText().contains("  ")) {
			textPane.setText(textPane.getText().replaceAll("  ", " "));
		}
		String[] array = textPane.getText().split(" ");
		int counter = 0; 
		int pos = textPane.getCaretPosition(); 
		String wordAtCaret = "";
		for (String s : array) {
			counter += s.length() + 1; 
			if (pos < counter) {
				wordAtCaret = s;
				break; 
			}
		}
		if (! wordAtCaret.equals("")) {
			//Husker siste tegn (?.!, osv")
			String alphabet = "abcdefghijklmnopqrstuvwxyzæøå1234567890";
			String lastChar = wordAtCaret.charAt(wordAtCaret.length() - 1) + "";
			if (alphabet.contains(lastChar)) {
				lastChar = "";
			} else {
				wordAtCaret = wordAtCaret.substring(0, wordAtCaret.length() - 1);
			}
			int start = 0;
			int end = counter - 1;
			int addCaret = 0;
			String s = textPane.getText().substring(0, end);
			start = s.lastIndexOf(wordAtCaret);
			boolean firstLetterUpper = (Character.isUpperCase(wordAtCaret.charAt(0)));
			LinkedString linkedString = WordLinks.isInList(wordAtCaret.toLowerCase());
			if (linkedString != null) {
				String replacement = null; 
				if (next) {
					replacement = linkedString.getNext().toString();
				} else {
					LinkedString prev = linkedString.getPrev(); 
					if (prev != null) {
						replacement = linkedString.getPrev().toString();
					} 
					//midlertidig workaround. Bør søke annerledes i linkedstrings og heller kunne returnere prev.
					else {
						for (List<LinkedString> list : Console.wordLinks) {
							if (list.contains(linkedString)) {
								replacement = WordLinks.isInList(list.get(list.size() - 1).toString()).toString();									
								break; 
							}
						}
					}
				}
				if (replacement == null) return 0;
				addCaret += replacement.length();
				if (firstLetterUpper)
					replacement = (replacement.substring(0, 1).toUpperCase() + replacement.substring(1));
				textPane.setText(textPane.getText().substring(0, start) + replacement + lastChar
						+ textPane.getText().substring(end, textPane.getText().length()));
			}
			colorWords();
			textPane.setCaretPosition(start + addCaret);
		}
		return 1; 
	}
	
	private int colorWords() {
		if (! tglColor.isSelected()) return -1;
		clearTextColors();
		for (Color color : Console.colorLinks.keySet()) {
			String s = Console.colorLinks.get(color);
			Pattern pattern = Pattern.compile(s);
			Matcher match = pattern.matcher(textPane.getText());
			while (match.find()) {
				updateTextColor(match.start(), match.end() - match.start(), color);
			}
		}
		setJTextPaneFont(textPane, Color.BLACK);
		return 0;

	}

	private void clearTextColors() {
		updateTextColor(0, textPane.getText().length(), Color.BLACK);
	}

	private void updateTextColor(int offset, int length, Color c) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
				StyleConstants.Foreground, c);
		textEditorDoc.setCharacterAttributes(offset, length, aset, true);
	}
	
	private void executeSearch() {
		String searchString = textField.getText(); 
		strings.clear(); 
		if (searchString.equals("")) {
			strings.addAll(Console.search.getPhrases().stream().map(Phrase::toString).collect(Collectors.toList()));
			populateTable(); 				
		} else {
			String[] keyWords = searchString.split(" ");
			strings.addAll(Console.search.searchKeyWords(keyWords));			
			populateTable(); 
		}
	}
	
	private void populateTable() {
		strings = Gender.changeDisplay(strings, gender);
		Iterator<String>iterator = strings.iterator(); 
		tableModel.setRowCount(0);
		numHits = strings.size();
		String[] rows = new String[numHits];		
		setDivider(strings.size());
		while (iterator.hasNext()) {
			String line = iterator.next(); 
			rows[0] = line; 
			tableModel.addRow(rows);
		}
		
	}

	private void confirmExit(KeyEvent e) {
		if (e.isControlDown() && e.isAltDown() && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			int option = JOptionPane.showConfirmDialog(splitPane, "Sikker på at du vil avslutte?", "Bekreft valg", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				Format.printLibrary(Console.search.getPhrases());	
				System.exit(0);
			}
		}
	}

	@Override
	public void dragGestureRecognized(DragGestureEvent arg0) {
		int index = table.getSelectedRow();
		if (index != -1) {
			String selectedText = (String) table.getModel().getValueAt(index, 0);
			Console.search.addChosencount(selectedText);				
			strings.clear();
			strings.addAll(Console.search.getPhrases().stream().map(Phrase::toString).collect(Collectors.toList()));
			executeSearch();
			splitPane.setDividerLocation(.01);
			lastEditorText = textPane.getText();
			colorWords();
		}

	}
	
	private void setDivider(int numResults) {
		if (numResults == 0) {
			isDividerDown = false; 
		} else {
			isDividerDown = true; 
		}
		switch (numResults) {
		case 0:
			splitPane.setDividerLocation(0);
			break;
		case 1:
			splitPane.setDividerLocation(32);
			break;
		case 2:
			splitPane.setDividerLocation(50);
			break;
		case 3:
			splitPane.setDividerLocation(68);
			break;
		case 4:
			splitPane.setDividerLocation(86);
			break;
		case 5:
			splitPane.setDividerLocation(104);
			break;
		case 6:
			splitPane.setDividerLocation(122);
			break;
		case 7:
			splitPane.setDividerLocation(140);
			break;
		case 8:
			splitPane.setDividerLocation(158);
			break; 
		default:
			splitPane.setDividerLocation(176);
			break;
		}
	}
	
	private void addKeyBindings() {
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ESCAPE:
					setDivider(0);
					textField.setText("");
					break;
				case KeyEvent.VK_ENTER:
					if (e.isControlDown()) {
						textPane.requestFocus();
					} else {
						btnSearch.doClick();						
						table.requestFocus(); 										
					}
					break;
				case KeyEvent.VK_INSERT:
					if (e.isControlDown()) {
						btnChange.doClick();
					} else {
						btnAdd.doClick();
						textField.requestFocus();						
					}
					break;
				case KeyEvent.VK_DELETE:
					btnDelete.doClick();
					textField.requestFocus();
					break;
				case KeyEvent.VK_SPACE: 
					if (e.isControlDown()) {
						if (isDividerDown) {
							setDivider(0);
						} else {							
							setDivider(numHits);
						}
					}
					break;
				case KeyEvent.VK_F2:
					btnClipboard.doClick();
					setDivider(0);
					break;
				case KeyEvent.VK_F3:
					btnClear.doClick();
					break;
				case KeyEvent.VK_F7:
					tglCorrection.doClick();
					textField.requestFocus();
					break; 
				case KeyEvent.VK_F9:
					tglColor.doClick();
					textField.requestFocus();
					break;
				case KeyEvent.VK_F11:
					tglAutoCorrect.doClick();
					textField.requestFocus();
					break;
				case KeyEvent.VK_F12:
					tglGender.doClick();
					textField.requestFocus();
					break;
				case (KeyEvent.VK_BACK_SPACE):
					if (e.isControlDown() && e.isAltDown()) {
						confirmExit(e);						
					}
				default:
					break;
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ENTER:
					if (e.isControlDown()) {
						textPane.requestFocus();
					} else {
						int index = table.getSelectedRow();
						if (index != -1) {
							String selectedText = (String) table.getModel().getValueAt(index, 0);						
							Console.search.addChosencount(selectedText);								
							strings.clear();
							strings.addAll(Console.search.getPhrases().stream().map(Phrase::toString).collect(Collectors.toList()));
							executeSearch();
							textPane.setText(textPane.getText() + selectedText);
							textPane.requestFocus();
							splitPane.setDividerLocation(.01);
							colorWords();
							lastEditorText = textPane.getText();
						}						
					}
					break;
				case KeyEvent.VK_ESCAPE:
					textField.requestFocus();
					setDivider(0);
					break; 
				case KeyEvent.VK_INSERT:
					if (e.isControlDown()) {
						btnChange.doClick();
					} else {
						btnAdd.doClick();
					}
					break;
				case KeyEvent.VK_SPACE: 
					if (e.isControlDown()) {
						if (isDividerDown) {
							setDivider(0);
						} else {							
							setDivider(numHits);
						}
						textPane.requestFocus();
					} 
					break;
				case KeyEvent.VK_F2:
					btnClipboard.doClick();
					break;
				case KeyEvent.VK_F3:
					btnClear.doClick();
					break;
				case KeyEvent.VK_F7:
					tglCorrection.doClick();
					break; 
				case KeyEvent.VK_F9:
					tglColor.doClick();
					break;
				case KeyEvent.VK_F11:
					tglAutoCorrect.doClick();
					break;
				case KeyEvent.VK_F12:
					tglGender.doClick();
					break;
				case KeyEvent.VK_DELETE:
					btnDelete.doClick();
					break;
				case KeyEvent.VK_BACK_SPACE:
					if (e.isControlDown() && e.isAltDown()) {
						confirmExit(e);
					}
					break;
				default:
					break;
				}
			}
		});
		
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ENTER:
					if (e.isControlDown()) {
						textPane.requestFocus();
					} else {
						replaceAbbreviation();
					}
					break;
				case KeyEvent.VK_ESCAPE:
					textField.requestFocus();
					setDivider(0);
					break;
				case KeyEvent.VK_F2:
					btnClipboard.doClick();
					setDivider(0);
					break;
				case KeyEvent.VK_F3:
					btnClear.doClick();
					break;
				case KeyEvent.VK_INSERT:
					if (e.isControlDown()) {
						btnChange.doClick();
					} else {
						btnAdd.doClick();
					}
					break;
				case KeyEvent.VK_F7:
					tglCorrection.doClick(); 
					textPane.requestFocus();
					break; 
				case KeyEvent.VK_F9:
					tglColor.doClick();
					textPane.requestFocus();
					break;
				case KeyEvent.VK_F11:
					tglAutoCorrect.doClick();
					textPane.requestFocus();
					break;
				case KeyEvent.VK_F12:
					tglGender.doClick();
					break;
				case KeyEvent.VK_SPACE: 
					if (e.isControlDown()) {
						if (e.isControlDown() && e.isAltDown()) {
							confirmExit(e);
						} else if (isDividerDown) {
							setDivider(0);
						} else {							
							setDivider(numHits);
						}
						table.requestFocus();
						break; 
					} else {
						replaceAbbreviation();
						colorWords();
					}
					break;
				case KeyEvent.VK_COMMA: case KeyEvent.VK_PERIOD: case KeyEvent.VK_COLON:
					replaceAbbreviation();
					lastEditorText = textPane.getText();
					if (tglColor.isSelected()) {
						colorWords();
					}
					setJTextPaneFont(textPane, Color.BLACK);
					break;
				case KeyEvent.VK_BACK_SPACE:
					if (e.isControlDown() && e.isAltDown()) confirmExit(e);
					break;
				case KeyEvent.VK_DOWN:
					if (e.isControlDown()) clickAndCorrect(false);
					break;
				case KeyEvent.VK_UP:
					if (e.isControlDown()) clickAndCorrect(true);
					break;
				default:
					setDivider(0);
					break;
				}
			}
		});
	}
			
	private void replaceAbbreviation() {
		if (tglAutoCorrect.isSelected()) {
			int index = textPane.getCaretPosition();
			String possibleAbbreviation = "";
			//Redigering midt i teksten
			if (textPane.getCaretPosition() != textPane.getText().length()) {
				String afterCaret = textPane.getText().substring(textPane.getCaretPosition());
				String toCheck = textPane.getText().substring(0, textPane.getCaretPosition());
				possibleAbbreviation = toCheck.substring(toCheck.lastIndexOf(' ') + 1, index);
				if (Console.autoCorrectMap.containsKey(possibleAbbreviation)) {
					String replaceWith = Console.autoCorrectMap.get(possibleAbbreviation);
					textPane.setText(textPane.getText().substring(0, textPane.getText().lastIndexOf(possibleAbbreviation)));
					textPane.setText(textPane.getText() + replaceWith + afterCaret);
					textPane.setCaretPosition(textPane.getText().length() - afterCaret.length());
				}
				
			//På slutten av teksten
			} else if (textPane.getText().substring(0, index).contains(" ")) {
				possibleAbbreviation = textPane.getText().substring(textPane.getText().lastIndexOf(' ') + 1,
						index);
				if (Console.autoCorrectMap.containsKey(possibleAbbreviation)) {
					textPane.setText(textPane.getText().substring(0,
							textPane.getText().lastIndexOf(possibleAbbreviation)));
					textPane.setText(textPane.getText() + Console.autoCorrectMap.get(possibleAbbreviation));
				}
			//Ved ord nr.1
			} else {
				possibleAbbreviation = textPane.getText().substring(0, index);
				if (Console.autoCorrectMap.containsKey(possibleAbbreviation)) {
					textPane.setText(Console.autoCorrectMap.get(possibleAbbreviation));
				}
			} 
		}
	}

	private void setJTextPaneFont(JTextPane jtp, Color c) {
	        MutableAttributeSet attrs = jtp.getInputAttributes();
	        StyleConstants.setForeground(attrs, c);
	        StyledDocument doc = jtp.getStyledDocument();
	        doc.setCharacterAttributes(doc.getLength(), doc.getLength() + 1, attrs, false);
	}
	
	public static JSplitPane getSplitPane() {
		return splitPane;
	}
	
	public static JEditorPane getTextPane() {
		return textPane; 
	}
	
	public static char getGender() {
		return gender;
	}
	
	public static JToggleButton getTglGender() {
		return tglGender;
	}
	
	public static void openInNotePad(Path path) {
		File file = new File(path.toString());
		try {
			java.awt.Desktop.getDesktop().edit(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


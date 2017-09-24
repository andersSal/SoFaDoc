package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import frames.Setup;
import modules.Abbreviation;
import modules.ColorLinks;
import modules.WordLinks;
import utilities.LinkedString;
import utilities.Search;

public class Console {

	private static Path abbreviationPath = null; 
	private static Path colorLinksPath = null; 
	private static Path wordLinksPath = null; 
	private static Path userManualPath = null; 
	private static Path userManualKeysPath = null; 
	public static JFrame application; 
	public static Search search; 
	private static Path libraryPath = null; 
	public static boolean autoCorrectON = true; 
	public static boolean colorLinksON = true; 
	public static boolean wordLinksON = true; 
		
	public static Map<Color, String> colorLinks = new HashMap<>();
	public static List<List<LinkedString>> wordLinks = new ArrayList<>(); 
	public static List<String> allWordsInLinks = new ArrayList<>(); 
	public static Map<String, String> autoCorrectMap = new HashMap<>(); 
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
					@SuppressWarnings("unused")
					Console window = new Console();
					findOrCreatLibrary();
					findOrCreateAbbreviations();
					findOrCreateColorLinks();
					findOrCreateWordLinks(); 
					createUserManuals();
					new Abbreviation();
					new WordLinks(); 
					new ColorLinks(); 
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		});
	} 
	
	public static void findOrCreatLibrary() {
		String appdataLibraryLocation = System.getenv("LOCALAPPDATA") + File.separator + "SoFa" + File.separator + "Library";
		/*
		 * Sjekker om programmet finner adressen til biblioteksfilen i %appdata%/SoFa/Library
		 */
		File appdataLibraryLocationFile = new File(appdataLibraryLocation + File.separator + "userDefinedLibraryPath.txt");
		if (appdataLibraryLocationFile.exists() && ! appdataLibraryLocationFile.isDirectory()) {
			try {
				Scanner scanner = new Scanner(appdataLibraryLocationFile);
				if (scanner.hasNextLine()) {
					String line = scanner.nextLine(); 
					if (line == null || ! line.contains(".txt")) {
						System.exit(0);
					}
					libraryPath = Paths.get(line); 
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			new File(appdataLibraryLocation).mkdirs();
			Setup setup = new Setup("Setup", "SoFa benytter en tekstfil som bibliotek for dine egendefinerte fraser.", "Vennligst angi ønsket filplassering.");
			setup.setVisible(true);
		}
		/*
		 * Sjekker om adressen funnet eksisterer
		 */
		if (libraryPath == null) System.exit(0);
		File userLibraryFile = new File(libraryPath.toString());
		if (! userLibraryFile.exists() || userLibraryFile.isDirectory()) {
			Setup setup = new Setup("Biblioteksfil ikke funnet", "Biblioteksfilen ser ut til å være flyttet eller slettet. Angi eksisterende", "biblioteksfil eller lag nytt bibliotek."); 
			setup.setVisible(true);
			if (libraryPath == null || ! libraryPath.toString().contains(".txt")) {
				System.exit(0);
			}
		}
		Console.search = new Search(); 
		application = new Application();
		application.setVisible(true);
	}
		
	private static void findOrCreateAbbreviations() {
		String appdataAbbreviationsFilePath = System.getenv("LOCALAPPDATA") + File.separator + "SoFa" + File.separator + "Modules" + File.separator + "Abbreviations.txt";
		File abbreviationFile = new File(appdataAbbreviationsFilePath);
		try {
			new Scanner(new File(appdataAbbreviationsFilePath)).close();
		} catch(FileNotFoundException fnf) {
			try {
				abbreviationFile.getParentFile().mkdirs();
				new FileWriter(appdataAbbreviationsFilePath).close();
			} catch (IOException e1) {
				JOptionPane.showConfirmDialog(null, "Gå til c:\\Brukere\\Din Bruker\nKlikk på menyen 'Visning' og klikk på boksen 'Vis skulte elementer'"
						+ "Naviger til 'AppData/Local/SoFa/Modules, slett tekstfilen 'Abbreviations' og kjør programmet på nytt", "%AppData% AutoCorrect error",JOptionPane.DEFAULT_OPTION, 
						JOptionPane.WARNING_MESSAGE);
			}						
		}
		abbreviationPath = Paths.get(appdataAbbreviationsFilePath);
	}
	
	private static void findOrCreateColorLinks() {
		String appdataColorLinksFilePath = System.getenv("LOCALAPPDATA") + File.separator + "SoFa" + File.separator + "Modules" + File.separator + "ColorLinks.txt";
		File colorLinksFile = new File(appdataColorLinksFilePath);
		try {
			new Scanner(colorLinksFile).close();
		} catch (FileNotFoundException nf) {
			try {
				colorLinksFile.getParentFile().mkdirs();
				new FileWriter(colorLinksFile).close();
			} catch (IOException e) {
				JOptionPane.showConfirmDialog(null, "Gå til c:\\Brukere\\Din Bruker\nKlikk på menyen 'Visning' og klikk på boksen 'Vis skulte elementer'"
						+ "Naviger til 'AppData/Local/SoFa/Modules, slett tekstfilen 'ColorLinks' og kjør programmet på nytt", "%AppData% ColorLinks-error",JOptionPane.DEFAULT_OPTION, 
						JOptionPane.WARNING_MESSAGE);
			} 	
		}
		colorLinksPath = Paths.get(appdataColorLinksFilePath); 
	}
	
	private static void findOrCreateWordLinks() {
		String appdataQuickFixFilePath = System.getenv("LOCALAPPDATA") + File.separator + "SoFa" + File.separator + "Modules" + File.separator + "QuickFix.txt";
		File quickFixFile = new File(appdataQuickFixFilePath);
		try {
			new Scanner(quickFixFile).close();;
		} catch (FileNotFoundException nf) {
			try {
				quickFixFile.getParentFile().mkdirs();
				new FileWriter(quickFixFile).close();
			} catch (IOException e) {
				JOptionPane.showConfirmDialog(null, "Gå til c:\\Brukere\\Din Bruker\nKlikk på menyen 'Visning' og klikk på boksen 'Vis skulte elementer'"
						+ "Naviger til 'AppData/Local/SoFa/Modules, slett tekstfilen 'QuickFix' og kjør programmet på nytt", "%AppData% QuickFix-error",JOptionPane.DEFAULT_OPTION, 
						JOptionPane.WARNING_MESSAGE);
			} 	
		}
		wordLinksPath = Paths.get(appdataQuickFixFilePath);
	}
	
	private static void createUserManuals() {
		String appdataUserManualPath = System.getenv("LOCALAPPDATA") + File.separator + "SoFa" + File.separator + "Manuals";
		String appdataShortKeysPath = System.getenv("LOCALAPPDATA") + File.separator + "SoFa" + File.separator + "Manuals";
		File userManualFolder = new File(appdataUserManualPath);
		File shortKeysFolder = new File(appdataShortKeysPath);
		if (! userManualFolder.exists()) {
			userManualFolder.mkdirs();
		}
		if (! shortKeysFolder.exists()) {
			shortKeysFolder.mkdirs();
		}
		String strUserManual = appdataUserManualPath + File.separator +  "Bruksanvisning.txt";
		String strKeys = appdataShortKeysPath + File.separator + "Hurtigtaster.txt";
		Console.userManualPath = Paths.get(strUserManual);
		Console.userManualKeysPath = Paths.get(strKeys);
		printManual(new File(strUserManual),  "Bruksanvisning.txt");
		printManual(new File(strKeys), "Hurtigtaster.txt");

	}
	
	private static void printManual(File toFile, String resourceName) {
		String line = null;
			try {
				if (! toFile.exists())toFile.createNewFile();
				InputStream in =  ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				PrintWriter writer = new PrintWriter(new BufferedWriter((new FileWriter(toFile))));
				while( (line = reader.readLine()) != null){
					writer.println(line);
				}
				reader.close();
				in.close();
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
	}

	public static Path getLibraryPath() {
		return libraryPath;
	}
	
	public static Path getUsermanualPath() {
		return userManualPath; 
	}
	
	public static Path getUserManualKeysPath() {
		return userManualKeysPath; 
	}

	public static Path getAbbreviationPath() {
		return abbreviationPath;
	}
	
	public static Path getColorLinksPath() {
		return colorLinksPath;
	}
	
	public static Path getWordLinksPath() {
		return wordLinksPath; 
	}
	
	public static void setLibraryPath(Path path) {
		Console.libraryPath = path; 
	}

}

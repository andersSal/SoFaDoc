package utilities;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CustomFileChooser extends JFileChooser {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	  public CustomFileChooser(String extension) {
	    super();
	    UIManager.put("FileChooser.saveButtonText", "Lagre");
	    UIManager.put("FileChooser.saveButtonToolTipText", "Lagre fil");
        UIManager.put("FileChooser.lookInLabelText", "Se i");
        UIManager.put("FileChooser.openButtonText", "Velg");
        UIManager.put("FileChooser.cancelButtonText", "Lukk");
        UIManager.put("FileChooser.fileNameLabelText", "Filnavn");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Filtyper");
        UIManager.put("FileChooser.openButtonToolTipText", "Velg fil");
        UIManager.put("FileChooser.cancelButtonToolTipText","Lukk");
        UIManager.put("FileChooser.fileNameHeaderText","Filnavn");
        UIManager.put("FileChooser.upFolderToolTipText", "Opp et nivå");
        UIManager.put("FileChooser.homeFolderToolTipText","Skrivebord");
        UIManager.put("FileChooser.newFolderToolTipText","Ny mappe");
        UIManager.put("FileChooser.listViewButtonToolTipText","Liste");
        UIManager.put("FileChooser.newFolderButtonText","Ny mappe");
        UIManager.put("FileChooser.renameFileButtonText", "Gi nytt navn");
        UIManager.put("FileChooser.deleteFileButtonText", "Slett fil");
        UIManager.put("FileChooser.filterLabelText", "filtyper");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Se detaljer");
        UIManager.put("FileChooser.fileSizeHeaderText","Størrelse");
        UIManager.put("FileChooser.fileDateHeaderText", "Sist modifisert");
        SwingUtilities.updateComponentTreeUI(this);
        setMultiSelectionEnabled(false);
	    addChoosableFileFilter(new FileNameExtensionFilter(
	      String.format("%1$s Images (*.%1$s)", extension), extension));
	  }

	  @Override public File getSelectedFile() {
	    File selectedFile = super.getSelectedFile();
	    if (selectedFile != null) {
	      String name = selectedFile.getName();
	      if (!name.contains("."))
	        selectedFile = new File(selectedFile.getParentFile(), 
	          name);
	    }

	    return selectedFile;
	  }

	  @Override public void approveSelection() {
	    if (getDialogType() == SAVE_DIALOG) {
	      File selectedFile = getSelectedFile();
	      if ((selectedFile != null) && selectedFile.exists()) {
	        JOptionPane.showMessageDialog(this, "Ønsker du å laste eksisterende bibliotek trykk 'Lukk' og 'Finn bibliotek'", "Filnavn opptatt", JOptionPane.OK_OPTION);
	        return;
	      }
	    }

	    super.approveSelection();
	  }
	}
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DataExtractorForm {
    private JButton openButton;
    private JButton saveButton;
    private JTextField titleTextField;
    private JTextField authorTextField;
    private JTextField dateTextField;
    private JTextArea textArea;
    private DataExtractor extractor;

    public static void main(String[] args) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        JFrame frame = new JFrame("Data Extraction");
        frame.setPreferredSize(new Dimension(450, 600));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new DataExtractorForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JButton aboutButton;

    public DataExtractorForm() {
        final JFileChooser openChooser = new JFileChooser();
        FileNameExtensionFilter openFilter = new FileNameExtensionFilter(
                "HTML document (*.htm,*.html)", "htm", "html");
        openChooser.setFileFilter(openFilter);

        final JFileChooser saveChooser = new JFileChooser();
        FileNameExtensionFilter saveFilter = new FileNameExtensionFilter(
                "Comma-Separated Values (CSV) file (*.csv)", "csv");
        saveChooser.setFileFilter(saveFilter);

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = openChooser.showOpenDialog(mainPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    extractor = new DataExtractor(openChooser.getSelectedFile());

                    //update text fields
                    authorTextField.setText(extractor.getAuthor());
                    titleTextField.setText(extractor.getTitle());
                    dateTextField.setText(extractor.getDate());
                    textArea.setText(extractor.getContent());
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int returnVal = saveChooser.showSaveDialog(mainPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    if (extractor == null) {
                        JOptionPane.showMessageDialog(mainPanel,
                                "Open an HTML file first.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        int wc = extractor.saveAsCSV(saveChooser.getSelectedFile());
                        JOptionPane.showMessageDialog(mainPanel,
                                wc + " unique words were saved to the CSV file.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);

                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(mainPanel,
                                "Cannot save file.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog aboutDialog = new AboutDialog();
                aboutDialog.setPreferredSize(new Dimension(250,190));
                aboutDialog.pack();
                aboutDialog.setLocationRelativeTo(null);
                aboutDialog.setResizable(false);
                aboutDialog.setVisible(true);
            }
        });
    }
}

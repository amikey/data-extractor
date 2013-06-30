import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AboutDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextArea aboutTextPanel;
    private JPanel picPanel;
    private JButton buttonCancel;

    public AboutDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        aboutTextPanel.setText("    This program extracts data from an article's html file and stores the unique words found in the content to a CSV file.");
        this.setTitle("About");

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

// call onOK() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onOK();
            }
        });

// call onOK() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
// add your code here
        dispose();
    }

}

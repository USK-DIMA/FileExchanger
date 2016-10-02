package file.exchange.form;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Dmitry on 02.10.2016.
 */
public class MainForm {

    public MainForm() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame.setDefaultLookAndFeelDecorated(true);
            startGui();
        });
    }

    private void startGui(){
        JFrame frame = new JFrame("File Exchanger");
        JPanel panel = new JPanel();
        JPanel downladPanel = new JPanel();
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] columnNames = { "File Name", "Size", "Download Size", "Status" };

        String[][] emptyData = initEmptyData(columnNames);

        JTable table = new JTable(emptyData, columnNames);

        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(downladPanel);
        panel.add(scrollPane);
        frame.setPreferredSize(new Dimension(450, 200));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private String[][] initEmptyData(String[] columnNames) {
        return new String[0][columnNames.length];
    }
}

package App;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class markdown {
	private JFrame frame;
    private JTextArea textArea;
    private JPanel panel;
    private JButton exportTxtButton;
    private JButton exportPdfButton;
    private JEditorPane editorPane;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new markdown().createUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void createUI() {
    	
        frame = new JFrame("Markdown Note");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(253, 253, 220)); // Very light pale yellow
        
        ImageIcon icon = new ImageIcon("C:/Users/Admin/git/repository4/Test/note.png"); // Load your custom icon
    	frame.setIconImage(icon.getImage()); // Set it to the frame

        // Create a split layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5); // Adjust split ratio

        // Markdown Input Panel
        textArea = new JTextArea();
        textArea.setText("Write your markdown here...");
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setBackground(new Color(255, 255, 234)); // Subtle soft yellow
        textArea.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(240, 230, 140), 1), // Border with soft yellow tone
            "Markdown Input", TitledBorder.LEFT, TitledBorder.TOP
        ));

        textArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                renderMarkdown();
            }
        });

        // Output Panel
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(255, 255, 240)); // Matches input panel for consistency
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(240, 230, 140), 1), // Border with soft yellow tone
            "Rendered Output", TitledBorder.LEFT, TitledBorder.TOP
        ));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(253, 253, 220)); // Matches frame background

        exportTxtButton = new JButton("Export as .txt");
        exportPdfButton = new JButton("Export as .pdf");

        // Modern flat-style buttons
        Color pastelYellow = new Color(255, 239, 151); // Light pastel yellow
        Color darkGray = new Color(60, 60, 60); // Neutral text color

        exportTxtButton.setBackground(pastelYellow);
        exportTxtButton.setForeground(darkGray);
        exportPdfButton.setBackground(pastelYellow);
        exportPdfButton.setForeground(darkGray);

        // Enhance button appearance
        exportTxtButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        exportTxtButton.setFocusPainted(false);
        exportPdfButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        exportPdfButton.setFocusPainted(false);

        buttonPanel.add(exportTxtButton);
        buttonPanel.add(exportPdfButton);

        // Button Actions
        exportTxtButton.addActionListener(e -> exportToFile("txt"));
        exportPdfButton.addActionListener(e -> exportToFile("pdf"));

        splitPane.setLeftComponent(new JScrollPane(textArea));
        splitPane.setRightComponent(new JScrollPane(panel));

        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        frame.getContentPane().add(splitPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void renderMarkdown() {
        String markdownText = textArea.getText();

        // Parse markdown using CommonMark
        Parser parser = Parser.builder().build();
        org.commonmark.node.Node document = parser.parse(markdownText);

        // Convert markdown to HTML
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String htmlContent = renderer.render(document);

        // Display HTML in the panel using JEditorPane
        panel.removeAll();
        editorPane = new JEditorPane("text/html", htmlContent);
        editorPane.setEditable(false);
        panel.add(new JScrollPane(editorPane), BorderLayout.CENTER);

        panel.revalidate();
        panel.repaint();
    }

    private void exportToFile(String format) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as " + format);
        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (format.equals("txt")) {
                try (BufferedWriter bf = new BufferedWriter(new FileWriter(fileToSave + ".txt"))) {
                    bf.write(textArea.getText());
                    JOptionPane.showMessageDialog(frame, "File saved successfully!");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "Error saving file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (format.equals("pdf")) {
				/*
				 * // Parse markdown using CommonMark Parser parser = Parser.builder().build();
				 * org.commonmark.node.Node document = parser.parse(textArea.getText());
				 * 
				 * // Convert markdown to HTML HtmlRenderer renderer =
				 * HtmlRenderer.builder().build(); String htmlContent =
				 * renderer.render(document);
				 */
            	
            	String htmlContent = editorPane.getText();

                // Convert HTML to PDF using XMLWorkerHelper
                Document pdfDocument = new Document();
                try {
                    PdfWriter writer = PdfWriter.getInstance(pdfDocument, new FileOutputStream(fileToSave + ".pdf"));
                    pdfDocument.open();
                    XMLWorkerHelper.getInstance().parseXHtml(writer, pdfDocument, new StringReader(htmlContent));
                    pdfDocument.close();
                    JOptionPane.showMessageDialog(frame, "PDF saved successfully!");
                } catch (DocumentException | IOException e) {
                    JOptionPane.showMessageDialog(frame, "Error saving PDF.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}

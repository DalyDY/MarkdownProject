package App;

import static org.junit.Assert.*;

import java.io.File;
import javax.swing.SwingUtilities;
import org.junit.Before;
import org.junit.Test;

public class markdownTest {
    private markdown app;
    
    @Before
    public void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> app = new markdown());
    }

    @Test
    public void testCreateUI() {
        SwingUtilities.invokeLater(() -> {
            app.createUI();
            assertNotNull("Frame should be initialized", app.frame);
            assertNotNull("Text area should be initialized", app.textArea);
            assertNotNull("Panel should be initialized", app.panel);
            assertNotNull("Export TXT button should be initialized", app.exportTxtButton);
            assertNotNull("Export PDF button should be initialized", app.exportPdfButton);
        });
    }
    
    @Test
    public void testRenderMarkdown() {
        SwingUtilities.invokeLater(() -> {
            app.createUI();
            app.textArea.setText("# Heading\nThis is **bold** text.");
            app.renderMarkdown();
            assertNotNull("Editor pane should be initialized", app.editorPane);
            assertTrue("Rendered content should contain HTML", app.editorPane.getText().contains("<h1>Heading</h1>"));
        });
    }
    
    @Test
    public void testExportToTxt() {
        SwingUtilities.invokeLater(() -> {
            app.createUI();
            app.textArea.setText("Sample text");
            File testFile = new File("test_output.txt");
            app.exportToFile("txt", testFile);
            assertTrue("TXT file should be created", testFile.exists());
            testFile.delete();
        });
    }
    
    @Test
    public void testExportToPdf() {
        SwingUtilities.invokeLater(() -> {
            app.createUI();
            app.textArea.setText("# Heading\nThis is **bold** text.");
            app.renderMarkdown();
            File testFile = new File("test_output.pdf");
            app.exportToFile("pdf", testFile);
            assertTrue("PDF file should be created", testFile.exists());
            testFile.delete();
        });
    }
}
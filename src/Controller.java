import java.awt.print.PrinterJob;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.printing.PDFPageable;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

public class Controller implements Initializable
{
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
    private final String absolutePath = "D:\\work\\eurovia\\dlAutomation\\";
    @FXML
    HBox header;
    @FXML
    TextField input, inputCapacity, inputConstruction;
    @FXML
    Text dlNumberElement, capacityElement, textNmb, textCapacity, textConstruction;
    @FXML
    ImageView saveBtn;
    private int dlNumber, capacity;
    private String construction;

    public void printDLEurovia()
    {
        LocalDateTime now = LocalDateTime.now();
        try
        {
            //Loading an existing document
            File file = new File(absolutePath+"DL_Eurovia.pdf");
            PDDocument document = PDDocument.load(file);

            rewriteDetails(now,document);
            print(document);
            //Saving the document
            //document.save(new File(absolutePath+"DL_EuroviaE.pdf"));

            //Closing the document
            document.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void printDLStrabag()
    {
        LocalDateTime now = LocalDateTime.now();

        try
        {
            //Loading an existing document
            File file = new File(absolutePath+"DL_STRABAG.pdf");
            PDDocument document = PDDocument.load(file);

            rewriteDetails(now,document);
            print(document);
            //Saving the document
            //document.save(new File(absolutePath+"DL_STRABAGE.pdf"));

            //Closing the document
            document.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void rewriteDetails(LocalDateTime now, PDDocument document) throws IOException
    {
        PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(0), PDPageContentStream.AppendMode.APPEND,true);

        //Begin the Content stream
        contentStream.beginText();

        //Setting the font to the Content stream
        PDFont font = PDType0Font.load( document, new File( "C:\\Windows\\Fonts\\arial.ttf" ) );
        contentStream.setFont(font, 8);

        //Set dlnumber and date
        contentStream.newLineAtOffset(355, 807);
        String text = dlNumber +"                                                     " +df.format(now);
        contentStream.showText(text);
        contentStream.endText();

        //set time
        contentStream.beginText();
        contentStream.newLineAtOffset(470, 697);
        text = tf.format(now);
        contentStream.showText(text);
        contentStream.endText();

        //set capacity
        contentStream.beginText();
        contentStream.newLineAtOffset(280, 658);
        text = ""+capacity;
        contentStream.showText(text);
        contentStream.endText();

        //set construction
        contentStream.beginText();
        contentStream.newLineAtOffset(365, 719);
        text = construction;
        contentStream.showText(text);
        contentStream.endText();

        //Closing the content stream
        contentStream.close();

        //rewrite number in file
        dlNumber+=1;
        saveUpdate();
    }
    public void changeSettings()
    {
        input.setText(String.valueOf(dlNumber));
        input.setVisible(!input.isVisible());
        inputCapacity.setText(String.valueOf(capacity));
        inputCapacity.setVisible(!inputCapacity.isVisible());
        inputConstruction.setText(construction);
        inputConstruction.setVisible(!inputConstruction.isVisible());

        textCapacity.setVisible(!textCapacity.isVisible());
        textConstruction.setVisible(!textConstruction.isVisible());
        textNmb.setVisible(!textNmb.isVisible());

        saveBtn.setVisible(false);
    }
    private void saveUpdate() throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter(absolutePath+"settings.txt"));
        bw.write(String.valueOf(dlNumber));
        bw.newLine();
        bw.write(String.valueOf(capacity));
        bw.newLine();
        bw.write(construction);
        bw.close();
        dlNumberElement.setText(String.valueOf(dlNumber));
        capacityElement.setText(String.valueOf(capacity +" m3"));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(absolutePath+"settings.txt"));
            dlNumber = Integer.parseInt(br.readLine());
            capacity = Integer.parseInt(br.readLine());
            construction = br.readLine().trim();
            br.close();
            dlNumberElement.setText(String.valueOf(dlNumber));
            capacityElement.setText(String.valueOf(capacity +" m3"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void checkInputChange()
    {
        saveBtn.setVisible(true);
    }

    public void saveFromInput(MouseEvent mouseEvent) throws IOException
    {
        try
        {
            dlNumber = Integer.parseInt(input.getText());
            capacity = Integer.parseInt(inputCapacity.getText());
            construction = inputConstruction.getText().trim();
            saveUpdate();
            saveBtn.setVisible(false);
            input.setVisible(!input.isVisible());
            inputCapacity.setVisible(!inputCapacity.isVisible());
            inputConstruction.setVisible(!inputConstruction.isVisible());

            textNmb.setVisible(!textNmb.isVisible());
            textConstruction.setVisible(!textConstruction.isVisible());
            textCapacity.setVisible(!textCapacity.isVisible());
        }
        catch (NumberFormatException e)
        {
            //show err
        }

    }
    public void print(PDDocument document) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(new PDFPageable(document));
            PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
            attr.add(new Copies(3));
            job.print(attr);
        } catch (Exception e) {
           //show err
        }

    }
}

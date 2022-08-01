package project.report_gen.services;

import jakarta.xml.bind.JAXBElement;
import org.docx4j.XmlUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.model.table.TblFactory;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.DocumentSettingsPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.utils.XPathFactoryUtil;
import org.docx4j.wml.*;
import org.springframework.stereotype.Service;
import project.report_gen.models.Report;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    //TODO
    // add content controls and bind xml

    public void getMainDocumentPart(String docx) throws Exception {
        WordprocessingMLPackage template;
        try {
            template = WordprocessingMLPackage.load(Files.newInputStream(Paths.get(docx)));
        } catch (Docx4JException | IOException e) {
            throw new RuntimeException(e);
        }

        MainDocumentPart documentPart = template.getMainDocumentPart();

        // Pretty print the main document part
        VariablePrepare.prepare(template);
        System.out.println(
                XmlUtils.marshaltoString(documentPart.getJaxbElement(), true, true));
    }

    static ObjectFactory factory = Context.getWmlObjectFactory();

    public void createReport(Report report, String fileName) throws Exception {
        Report.builder().documentType(report.getDocumentType()).product(report.getProduct()).productionCell(report.getProductionCell()).build();
        WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.createPackage();

        // set title as document property
        wordprocessingMLPackage.setTitle("\n" + "Title for " + report.getDocumentType() + " for product " + report.getProduct() + " in production Cell " + report.getProductionCell());

        // create new paragraph with text from report and add to main document part
        MainDocumentPart mainDocumentPart = wordprocessingMLPackage.getMainDocumentPart();
        mainDocumentPart.addStyledParagraphOfText("Title",wordprocessingMLPackage.getTitle());
        mainDocumentPart.addParagraphOfText("This is an autogenerated " + report.getDocumentType() + " completed by Report Application.");
        mainDocumentPart.addParagraphOfText(" This report is for " + report.getProduct() + " in production cell " + report.getProductionCell() +".");

        // add image and table
        mainDocumentPart.addParagraphOfText("Image added" + "\r \r");
        addImage(wordprocessingMLPackage);
        mainDocumentPart.addParagraphOfText("Table added");
        addTable(wordprocessingMLPackage,mainDocumentPart);

        // set compatibility and save file
        setCompat(mainDocumentPart);
        saveFile(wordprocessingMLPackage, fileName);
    }

    public void addImage(WordprocessingMLPackage wordprocessingMLPackage) throws Exception {

        String imageFilePath = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/resources/image5.png";
        File file = new File(imageFilePath);

        InputStream is = new FileInputStream(file);
        long length = file.length();
        // create an array using int type
        if (length > Integer.MAX_VALUE) {
            System.out.println("File too large!!");
        }

        byte[] bytes = new byte[(int)length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            System.out.println("Could not completely read file "+imageFilePath);
        }
        is.close();

        String filenameHint = null;
        String altText = null;
        int id2 = 1;

        // Image 2: width 3000pixels
        org.docx4j.wml.P p2 = newImage(wordprocessingMLPackage, bytes,
                filenameHint, altText,
                id2, 9000 );
        wordprocessingMLPackage.getMainDocumentPart().addObject(p2);

    }

    public static org.docx4j.wml.P newImage( WordprocessingMLPackage wordMLPackage,
                                             byte[] bytes,
                                             String filenameHint, String altText,
                                             int id2, long cx) throws Exception {

        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

        Inline inline = imagePart.createImageInline( filenameHint, altText,
                wordMLPackage.getDrawingPropsIdTracker().generateId(), id2, cx, false);

        // Now add the inline in w:p/w:r/w:drawing
        org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
        org.docx4j.wml.P  p = factory.createP();
        org.docx4j.wml.R  run = factory.createR();
        p.getContent().add(run);
        org.docx4j.wml.Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);

        return p;
    }

    public void addTable(WordprocessingMLPackage wordprocessingMLPackage, MainDocumentPart mainDocumentPart) {
        //create table 1 X 3
        int writableWidthTwips = wordprocessingMLPackage.getDocumentModel().getSections().get(0).getPageDimensions().getWritableWidthTwips();
        int cols = 3;
        int cellWidthTwips = new Double(Math.floor((writableWidthTwips / cols))).intValue();
        Tbl tbl = TblFactory.createTable(5, 3, cellWidthTwips);
        mainDocumentPart.addObject(tbl);

    }

    private static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<Object>();
        if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();

        if (obj.getClass().equals(toSearch))
            result.add(obj);
        else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }

        }
        return result;
    }

    // save file
    public void saveFile(WordprocessingMLPackage wordprocessingMLPackage, String fileName) throws FileNotFoundException, Docx4JException {
        wordprocessingMLPackage.save(new FileOutputStream(new File("C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/" + fileName + ".docx")));
    }

    // set compatibility settings for MS office 2016
    public void setCompat(MainDocumentPart mainDocumentPart) throws Docx4JException {
        DocumentSettingsPart dsp = mainDocumentPart.getDocumentSettingsPart(true);
        CTCompat compat = Context.getWmlObjectFactory().createCTCompat();
        dsp.getContents().setCompat(compat);
        compat.setCompatSetting("compatibilityMode", "http://schemas.microsoft.com/office/word", "15");
    }
}
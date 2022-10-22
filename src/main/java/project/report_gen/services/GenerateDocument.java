package project.report_gen.services;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.table.TblFactory;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.springframework.stereotype.Service;
import project.report_gen.models.Image;
import project.report_gen.models.Report;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class GenerateDocument {

    // creating a table here, another option could be to find the first table in document (Table table =
    //                doc.MainDocumentPart.Document.Body.Elements<Table>().First())
    public void addCustomTable(WordprocessingMLPackage wordprocessingMLPackage, MainDocumentPart mainDocumentPart, Report report) {
        int writableWidthTwips = wordprocessingMLPackage.getDocumentModel().getSections().get(0).getPageDimensions().getWritableWidthTwips();
        int qtyCols = 5;
        int qtyRows = report.getProductSKU().getDefectList().size()+1;
        int cellWidthTwips = new Double(Math.floor((writableWidthTwips / qtyCols))).intValue();

        ObjectFactory factory = Context.getWmlObjectFactory();
        Tbl tbl = TblFactory.createTable(qtyRows, qtyCols, cellWidthTwips);
        List<Object> tableRows = tbl.getContent();
        int defectId=0;

        // set table header row
        Tr tr = (Tr) tableRows.get(0);
        List<Object>  cells = tr.getContent();
        ((Tc)cells.get(0)).getContent().add(createCell(factory,"DEFECT #"));
        ((Tc)cells.get(1)).getContent().add(createCell(factory,"DESCRIPTION"));
        ((Tc)cells.get(2)).getContent().add(createCell(factory,"AQL %"));
        ((Tc)cells.get(3)).getContent().add(createCell(factory,"SAMPLE SIZE"));
        ((Tc)cells.get(4)).getContent().add(createCell(factory,"ACC / REJ"));

        for(int j=1; j<tableRows.size(); j++){
            tr = (Tr) tableRows.get(j);
            cells = tr.getContent();

            ((Tc)cells.get(0)).getContent().add(createCell(factory,String.valueOf(defectId +1)));
            ((Tc)cells.get(1)).getContent().add(createCell(factory,String.valueOf(report.getProductSKU().getDefectList().get(defectId).getDescription())));
            ((Tc)cells.get(2)).getContent().add(createCell(factory,String.valueOf(report.getProductSKU().getDefectList().get(defectId).getAql())));
            ((Tc)cells.get(3)).getContent().add(createCell(factory,String.valueOf(report.getValSampleSize())));
            ((Tc)cells.get(4)).getContent().add(createCell(factory,String.valueOf(report.getProductSKU().getDefectList().get(defectId).getAcceptReject().toString())));

            defectId++;
        }
        mainDocumentPart.addObject(tbl);
    }

    private static P createCell(ObjectFactory factory, String value){

        P p = factory.createP();
        R r = factory.createR();
        Text t = factory.createText();

        t.setValue(value);

        r.getContent().add(t);
        p.getContent().add(r);

        return p;
    }

    public void addImage(WordprocessingMLPackage wordprocessingMLPackage, Image imageToInsert) throws Exception {

        //String imageFilePath = "C:\\Users\\User\\OneDrive\\Documents\\CodingNomads\\projects\\Capstone_Project\\src\\main\\resources\\image5.png";
        String imageFilePath = imageToInsert.getFilePath();
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
}

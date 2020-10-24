package micasa;

import java.io.File;
import java.io.FileOutputStream;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

/*
    Converts a rendered image file into a PDF document using the iText API.
    Author: Adam Cook
*/

public class ConvertPDF {

    //Takes in the rendered image's file location and converts to a PDF
    public static void convert(File file){
        
        //String FILE_NAME = "C:\\Users\\s1000564\\Desktop\\" + file.getName();
        Document document = new Document();
        int pos = file.getName().indexOf(".");
        String f = file.getName().substring(0, pos);
        String FILE = "C:/Users/s1000564/Desktop/" + f + ".pdf"; //File path for document
        
        try{
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            Image i = Image.getInstance(file.getPath());
            document.add(i);
            document.close();
            
        }catch (Exception e){
            e.printStackTrace();
        }  
    }
}

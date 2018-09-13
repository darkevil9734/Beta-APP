package com.example.legia.mobileweb.CreatePDF;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.legia.mobileweb.DAO.hoaDonDAO;
import com.example.legia.mobileweb.DTO.hoaDon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.regex.Pattern;

import javax.xml.marshal.XMLWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class createPDF_Bill {
    static String title;
    private static FirebaseAuth mAuth;


    public static String getPDF_Link(hoaDon hd, String chiTiet){
        final String[] url = {""};

        int idHD = hoaDonDAO.lastID()+1;
        title = idHD+"_"+removeAccent(hd.getTen_user())+".pdf";
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(new File(title)));
            document.open();
            Font f = new Font();
            f.setStyle(Font.BOLD);
            f.setSize(14);

            document.add(new Paragraph("Detail about your bill", f));

            Paragraph p = new Paragraph();
            p.add("Here is your detail: " +
                    "\n" + chiTiet);
            p.setAlignment(Element.ALIGN_CENTER);
            document.close();

            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                Source xmlSource = new DOMSource((Node) document);
                Result outputTarget = new StreamResult(outputStream);
                TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
                InputStream is = new ByteArrayInputStream(outputStream.toByteArray());

                int id = hoaDonDAO.lastID()+1;

                mAuth = FirebaseAuth.getInstance();

                FirebaseStorage storagePDF = FirebaseStorage.getInstance("gs://mobile-shop-1535131843934.appspot.com");
                StorageReference storageRefPDF = storagePDF.getReferenceFromUrl("gs://mobile-shop-1535131843934.appspot.com/");
                StorageReference pdf_upload = storageRefPDF.child("pdf_bill/"+hd.getTen_user()+"/"+id+"_"+removeAccent(hd.getTen_user())+".pdf");

                UploadTask uploadTask = pdf_upload.putStream(is);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        Uri downloadUrl = urlTask.getResult();
                        url[0] = String.valueOf(downloadUrl);
                    }
                });


            } catch (TransformerException e) {
                e.printStackTrace();
            }


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        return url[0];
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}

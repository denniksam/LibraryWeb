package step.library.servlets;

import org.json.JSONObject;
import step.library.models.Book;
import step.library.utils.Db;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@WebServlet("/books")
@MultipartConfig  // !!1 Без этого multipart/form-data не работает
public class BookServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // author, title, cover(file)
        String author = req.getParameter( "author" ) ;
        String title  = req.getParameter( "title" ) ;
        Part   cover  = req.getPart( "cover" ) ;
        int    resultStatus ;
        String resultMessage ;
        // TODO data validation
        if( author == null || author.length() < 2 ) {
            resultStatus  = -1 ;
            resultMessage = "Author empty or too short" ;
        } else if( title == null || title.length() < 2 ) {
            resultStatus = -2 ;
            resultMessage = "Title empty or too short" ;
        } else if( cover.getSize() == 0 ) {
            resultStatus = -3 ;
            resultMessage = "Cover file required" ;
        } else {
            String savedName = moveUploadedFile( cover, true ) ;
            if( savedName == null ) {
                resultStatus = -4 ;
                resultMessage = "Cover save error" ;
            } else {
                if( Db.getBookOrm().add( new Book(
                        author,
                        title,
                        savedName
                ) ) ) {
                    resultStatus = 1 ;
                    resultMessage = author + " " + title + " " + savedName ;
                } else {
                    resultStatus = -5 ;
                    resultMessage = "Book store error" ;
                }
            }
        }

        JSONObject result = new JSONObject();
        result.put( "status",  resultStatus  ) ;
        result.put( "message", resultMessage ) ;

        resp.setContentType( "application/json" ) ;
        resp.getWriter().print( result.toString() ) ;
    }

    /**
     * Save uploaded file, optionally make developer's copy
     * @param filePart HttpPart with file
     * @param makeDevCopy true - to make copy in project folder
     * @return saved file name / null
     */
    private String moveUploadedFile( Part filePart, boolean makeDevCopy ) {
        if( filePart.getSize() == 0 ) {
            System.err.println( "moveUploadedFile: size - 0");
            return null ;
        }
        String hostingFolder =
            this.getServletContext().getRealPath("/uploads") + "/" ;
        String devFolder = "C:\\Users\\samoylenko_d\\IdeaProjects\\Library\\web\\uploads\\" ;
        String uploadedFilename = null ;
        try {
            uploadedFilename = filePart.getSubmittedFileName() ;
        } catch( Exception ignored ) {
            String contentDisposition =
                    filePart.getHeader("content-disposition" ) ;
            if( contentDisposition != null ) {
                for( String part : contentDisposition.split( "; ") ) {
                    if( part.startsWith( "filename" ) ) {
                        uploadedFilename = part.substring( 10, part.length() - 1 ) ;
                        break ;
                    }
                }
            }
        }
        if( uploadedFilename == null ) {
            System.err.println( "moveUploadedFile: filename extracting error" ) ;
            return null ;
        }
        int extPosition =  uploadedFilename.lastIndexOf( "." ) ;
        if( extPosition == -1 ) {
            System.err.println( "moveUploadedFile: filename without extension" ) ;
            return null ;
        }
        String fileExtension = uploadedFilename.substring( extPosition ) ;
        String initFileName = uploadedFilename.substring( 0, extPosition ) ;
        String fileName ;
        int counter = 1 ;
        File file ;
        do {
            fileName = "_" + initFileName + "(" + counter + ")" + fileExtension ;
            file = new File( hostingFolder + fileName ) ;
            ++counter ;
        } while( file.exists() ) ;
        try {
            Files.copy(
                    filePart.getInputStream(),
                    file.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            ) ;
            if( makeDevCopy ) {
                Files.copy(
                        filePart.getInputStream(),
                        new File( devFolder + fileName ).toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                ) ;
            }
        } catch( IOException ex ) {
            System.err.println( "moveUploadedFile: " + ex.getMessage() ) ;
            return null ;
        }
        return fileName ;
    }
}

package step.library.servlets;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

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
            resultStatus = 1 ;
            resultMessage = author + " " + title + " " + cover.getSubmittedFileName() ;
        }

        JSONObject result = new JSONObject();
        result.put( "status",  resultStatus  ) ;
        result.put( "message", resultMessage ) ;

        resp.getWriter().print( result.toString() ) ;
    }
}

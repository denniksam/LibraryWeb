package step.library.filters;

import org.json.JSONObject;
import step.library.utils.Db;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@WebFilter( "/*" )
public class DbFilter implements Filter {

    private FilterConfig filterConfig ;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig ;
    }

    public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // Filter path is "/*", so all requests pass through
        // this filter. Such as .css, .js, etc
        String req = ((HttpServletRequest) servletRequest).getRequestURI();
        for( String ext : new String[] { ".css", ".js", ".jsp"} ) {
            if( req.endsWith( ext ) ) {
                filterChain.doFilter(servletRequest, servletResponse);
                return ;
            }
        }

        // Real file path - stores in Servlet Context
        String path =
                /*((HttpServletRequest) servletRequest)
                .getSession()*/
                filterConfig
                .getServletContext()
                .getRealPath( "/WEB-INF/" ) ;
        File config = new File( path + "config.json" ) ;
        if( config.exists() ) {
            // read file-config content to byte[]
            int len = (int) config.length() ;
            byte[] buf = new byte[ len ] ;
            try( InputStream reader = new FileInputStream( config ) ) {
                if( len != reader.read( buf ) )
                    throw new IOException( "File integrity check error" ) ;
                // Extract JSON
                JSONObject json = new JSONObject(
                        new String( buf )
                ) ;
                // Test connection
                if( Db.setConnection( json ) ) {
                    // Check for Books table
                    if( Db.getBookOrm().isTableExists() ) {
                        filterChain.doFilter(servletRequest, servletResponse);
                    } else {
                        // Install page
                        servletRequest
                                .getRequestDispatcher( "/install.jsp" )
                                .forward( servletRequest, servletResponse ) ;
                    }
                } else {
                    // Show static page
                    servletRequest
                            .getRequestDispatcher( "/static.jsp" )
                            .forward( servletRequest, servletResponse ) ;
                }
                return;
            } catch ( IOException ex ) {
                System.err.println( "DbFilter: " + ex.getMessage() ) ;
            }

        }

        System.err.println( "Config.json not found" ) ;
    }

    public void destroy() {
        this.filterConfig = null ;
    }
}
/*
Фильтр проверяет есть ли таблица Books/Literature
Если нет - переходит на страницу install.jsp
на которой кнопка "Создать БД"
 */
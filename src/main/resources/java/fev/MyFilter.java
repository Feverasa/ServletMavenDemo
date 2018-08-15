package fev;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Hello");
        PrintWriter writer =  servletResponse.getWriter();
        writer.println("<html>\n" +
                "  <head>\n" +
                "    <title>MyFilter</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "  Hello Filter\n" +
                "  </body>\n" +
                "</html>");
//        filterChain.doFilter(servletRequest,servletResponse);
    }

    public void destroy() {
        System.out.println("destroy!!");
    }
}

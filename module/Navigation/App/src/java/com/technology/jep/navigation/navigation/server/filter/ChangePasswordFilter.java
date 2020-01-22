package com.technology.jep.navigation.navigation.server.filter;

import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.CHANGE_PASSWORD_REQUEST_ATTRIBUTE_NAME;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.technology.jep.navigation.navigation.server.Util;

public class ChangePasswordFilter implements Filter {

  @Override
  public void init(FilterConfig config) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp,
      FilterChain chain) throws IOException, ServletException {
    // we are here after successful authorization only
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) resp;
    
    boolean changePassword = Util.needToChangePassword(request); 
    if (changePassword) {
      response.sendRedirect(request.getContextPath() + "/?em=ChangePassword");
    }
    else {
      chain.doFilter(req, resp);
    }
  }
  
  @Override
  public void destroy() {

  }
}

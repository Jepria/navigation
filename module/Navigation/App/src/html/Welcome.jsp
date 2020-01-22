<!DOCTYPE html>
<%@page import="com.technology.jep.navigation.navigation.server.NavigationServerConstant"%>
<%@page import="com.technology.jep.navigation.navigation.server.Util"%>
<%@page import="com.technology.jep.navigation.navigation.server.MainResource"%>
<%@page import="java.util.ResourceBundle"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html style="width: 100%; height: 100%;">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>Navigation Module</title>
    <script type="text/javascript">
      if (window.parent && typeof window.parent.navigationTreeFieldRefresh === 'function') {
        window.parent.navigationTreeFieldRefresh('<%= Util.getSecurityModule(request).getUsername() %>');
      }
    </script>
  </head>
  <body style="margin: 0px; padding: 0px; width: 100%; height: 100%;">

  <table style="border: 0px; table-layout: fixed; border-collapse: collapse; margin: 0px; padding: 0px; width: 100%;">
    <tr><td align="left" style="vertical-align: middle;"><table style="height: 20px;"><tbody><tr></tr></tbody></table></td></tr>
      <tr>
        <td align="left" style="vertical-align: middle; font: 12px Arial">
          <div class="gwt-HTML">
          <%= MainResource.getMainResource(request.getParameter(NavigationServerConstant.LOCALE_REQUEST_NAME)).getString("welcome.welcome") %>
          </div>
        </td>
      </tr>
    </table>
    
  </body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<head>
    <title>LOGIN</title>
    <script type="text/javascript" src="script/jquery.js"></script>
    <script type='text/javascript' src='script/common.js'></script>
    <script type="text/javascript">
        function loginOper() {
            if ($('#userName').val() == '') {
                alert('"账号"不能为空');
                return false;
            }

            if ($('#password').val() == '') {
                alert('"密码"不能为空');
                return false;
            }

            $.post('loginOper.do', {
                userName: $('#userName').val(),
                password: $('#password').val()
            }, function (data) {
                if (data.indexOf('system500error') != -1) {
                    alert("无法登录系统，请联系管理员");
                } else {
                    var json = stringToJson(data);
                    if (json.success) {
                        location.href = '<%=basePath%>index.do';
                    } else {
                        alert("账号和密码不一致，请重新输入");
                    }
                }

            });
        }
    </script>
</head>
<body>
<table>
    <tr>
        <td><label>账号：</label></td>
        <td><input type="text" name="userName" id="userName" value="liubei"/></td>
    </tr>
    <tr>
        <td><label>密码：</label></td>
        <td><input type="password" name="password" id="password" value="111111"/></td>
    </tr>
</table>
<div class="action">
    <input type="button" name="login" value="登录" onclick="loginOper()" style="cursor: pointer;"/>
</div>
</body>
</html>

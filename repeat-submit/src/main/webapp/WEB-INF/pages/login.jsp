<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>登录</title>
    </head>
    <body>
        <input id="login-btn" type="button" value="登录">
        <div id="div" style="display: none">
            <form action="${pageContext.request.contextPath}/user/login" method="post">
                <input type="hidden" name="token" id="token">
                <div><label for="username">用户名：</label><input type="text" name="username" id="username"></div>
                <div><label for="password">密码：</label><input type="password" name="password" id="password"></div>
                <div><input type="submit" value="登录"></div>
            </form>
        </div>
    </body>
</html>
<script src="${pageContext.request.contextPath}/webjars/jquery/3.6.0/jquery.min.js"></script>
<script>
    $(() => {
        $('#login-btn').click(() => {
            $('#div').css('display', 'block')
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/token/one',
                dataType : 'json',
                success : (data) => {
                    $('#token').val(data.token);
                }
            })
        })
    })
</script>
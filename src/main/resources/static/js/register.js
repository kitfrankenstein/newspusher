var curWwwPath = window.document.location.href;
//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
var pathName = window.document.location.pathname;
var pos = curWwwPath.indexOf(pathName);
//获取主机地址，如： http://localhost:8083
var localhostPaht = curWwwPath.substring(0, pos);
//获取带"/"的项目名，如：/uimcardprj
var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
var baseRoot = localhostPaht + projectName;

function register() {
    var phoneNumber = $("#phone").val();
    var password = $("#password").val();
    var password2 = $("#passwordagain").val();
    $.ajax({
        url: baseRoot + "/User/register",
        type: "post",
        data: JSON.stringify({
            "phoneNumber": phoneNumber,
            "password": $.md5(password),
            "password2": $.md5(password2)
        }),
        contentType: "application/json",
        dataType: "json",
        success: function (data) {
            if (data.flag === 1000) {
                alert("注册成功");
                window.location.href = baseRoot + "/view/login";
            } else {
                alert(data.msg);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
            alert(textStatus);
            alert(errorThrown);
        }
    });
}
var curWwwPath = window.document.location.href;
//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
var pathName = window.document.location.pathname;
var pos = curWwwPath.indexOf(pathName);
//获取主机地址，如： http://localhost:8083
var localhostPaht = curWwwPath.substring(0, pos);
//获取带"/"的项目名，如：/uimcardprj
var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
var baseRoot = localhostPaht + projectName;

function login() {
    var phoneNumber = $("#phone").val();
    var password = $("#password").val();
    var md5password = $.md5(password);

    $.ajax({
        url: baseRoot + "/User/login",
        type: "post",
        data: JSON.stringify({
            "phoneNumber": phoneNumber,
            "password": md5password
        }),
        contentType: "application/json",
        dataType: "json",
        success: function (data) {
            if (data.flag === 1000) {
                setCookie("phoneNumber", phoneNumber);
                setCookie("password", md5password);
                window.location.href = baseRoot + "/view/console";
            } else {
                alert(data.msg);
                window.location.href = baseRoot + "/view/login";
            }

        },
        error: function (e) {
            alert("无法连接到服务器");
        }
    });
}

function download() {
    window.open(baseRoot + "/User/download");
}

function setCookie(name, value) {
    document.cookie = name + '=' + encodeURI(value);
}

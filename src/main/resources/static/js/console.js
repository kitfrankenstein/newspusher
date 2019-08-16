var curWwwPath = window.document.location.href;
//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
var pathName = window.document.location.pathname;
var pos = curWwwPath.indexOf(pathName);
//获取主机地址，如： http://localhost:8083
var localhostPaht = curWwwPath.substring(0, pos);
//获取带"/"的项目名，如：/uimcardprj
var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
var baseRoot = localhostPaht + projectName;

var pageCount;
var ifinit = false;
var dataList;
var paths = [
    '/News/getMChinaNews',
    '/News/get163News',
    '/News/getSinaNews'];
var countPaths = [
    '/News/getMChinaCount',
    '/News/get163Count',
    '/News/getSinaCount'
];

/*
 初始化
 */
$(function () {
    setSelectAll();
    getNews(paths[0], countPaths[0])
});

function getNews(path, countPath) {
    document.getElementById('records').value = 7;
    pageCount = refreshPageCount(countPath);
    refreshPageInterface(1, path, countPath);
}

function selectChange(index) {
    getNews(paths[index], countPaths[index])
}

/*
 更新页面数量
 传入接口
 */
function refreshPageCount(path) {
    var records = getRecords();
    var pages = 0;
    $.ajax({
        url: baseRoot + path,
        type: 'post',
        data: JSON.stringify({}),
        contentType: 'application/json',
        dataType: 'json',
        async: false,
        success: function (data) {
            var count = data.result;
            pages = Math.ceil(count / parseInt(records));
        },
        error: function () {
            alert('失败');
        }
    });
    return pages;
}

/*
 刷新页面和页面栏
 */
function refreshPageInterface(pageClickedNumber, path, countPath) {
    $('#pager').pager({
        pagenumber: pageClickedNumber,
        pagecount: pageCount,
        buttonClickCallback: pageClick,
        method: countPath
    });
    getNewsList(pageClickedNumber, true, path, countPath);
}

/*
 页面标签栏点击事件
 */
function pageClick(pageClickedNumber) {
    var index = $('#select').prop('selectedIndex');
    refreshPageInterface(pageClickedNumber, paths[index], countPaths[index]);
}

/*
 自定义显示记录数量事件触发
 */
$('#records').focusout(function () {
    pageCount = refreshPageCount(countPaths[0]);
    refreshPageInterface(1, paths[0], countPaths[0]);
}).keyup(function (event) {
    if (event.keyCode === 13) {
        pageCount = refreshPageCount(countPaths[0]);
        refreshPageInterface(1, paths[0], countPaths[0]);
    }
});

/*
 获取数据
 */
function getNewsList(start, check, path, countPath) {
    var records = getRecords();
    var offset = (start - 1) * records;
    //数据请求
    $.ajax({
        url: baseRoot + path,
        type: 'post',
        data: JSON.stringify({
            "offset": offset,
            "limit": records
        }),
        contentType: 'application/json',
        dataType: 'json',
        success: function (data) {
            if (check === false) {
                pageCount = data.size;
                console.log(pageCount);
                $("#pager").pager({
                    pagenumber: offset,
                    pagecount: pageCount,
                    buttonClickCallback: pageClick,
                    method: countPath
                });
                ifinit = true;
            }
            var $table = $("#SearchTable tbody");
            $table.html("");
            var list = [];
            for (var id in data.result) {
                list.push(data.result[id]);
                var $tr = $(document.createElement('tr'));
                rendData(id, data.result, $tr);
                $table.append($tr);
            }
            $(document).on('click', '.checkTd', function () {
                e.stopPropagation();//阻止事件冒泡 避免点击复选框时，页面跳转到具体信息
            });
            $(document).on('mouseover', '.hoverTr', function () {
                $(this).addClass("deepColor");
            });
            $(document).on('mouseout', '.hoverTr', function () {
                $(this).removeClass("deepColor");
            });
            dataList = list;
        },
        error: function () {
            alert("失败");
        }
    });
}

/*
 渲染数据
 */
function rendData(i, results, $newTr) {
    //选择按钮
    var input = "<input type='checkbox' class='cbtoExcel' value='" + results[i].url + "'>";
    var label = "<label for='" + results[i].url + "'></label>";

    var div = "<td class='checkTd'><div class='checkboxfour'>" + input + label + "</div></td>";
    var $div = $(div);
    //标题
    var td_title = "<td>" + results[i].title + "</td>";
    var $td_title = $(td_title);
    //摘要
    var td_digest = "<td>" + results[i].digest + "</td>";
    var $td_digest = $(td_digest);
    //时间或来源
    var td_org = "<td>" + results[i].time + "</td>";
    var $td_org = $(td_org);
    //标签
    var td_tag = "<td>" + results[i].tag + "</td>";
    var $td_tag = $(td_tag);
    //url
    var td_url = "<td>" + results[i].url + "</td>";
    var $td_url = $(td_url);
    $newTr.append($div, $td_title, $td_digest, $td_org, $td_tag, $td_url);
    $newTr.attr('id', i);
    $newTr.attr('class', "hoverTr");

    $(document).off('click', '#' + i).on('click', '#' + i, function () {
        gotoDetails(results[i].url);
    });
}

/*
图片的点击事件方法体
 */
function gotoDetails(newsurl) {
    window.open(newsurl, "_blank");
}

function setSelectAll() {
    //全选事件
    $("#cbtoExcel").click(function () {
        var selectAll = $("#cbtoExcel").prop("checked");
        $(".cbtoExcel").prop("checked", selectAll);
    });
}

/*
 * 推送
 */
function push() {
    var pushList = [];
    var selectToExcelList = $(".cbtoExcel");
    var pushNum = 0;
    for(var i in dataList){
        if(selectToExcelList[i].checked === true) {
            pushNum = pushList.push(dataList[i])
        }
    }
    if (pushNum !== 0) {
        var phoneNumber = getCookie("phoneNumber");
        var password = getCookie("password");
        $.ajax({
            url: baseRoot + "/News/push",
            type: 'post',
            data: JSON.stringify({
                "phoneNumber": phoneNumber,
                "password": password,
                "pushList": pushList
            }),
            contentType: 'application/json',
            dataType: 'json',
            success: function (data) {
                if (data.flag === 1000) {
                    alert("发布成功")
                } else {
                    alert("发布失败" + data.message)
                }
            },
            error: function () {
                alert("失败");
            }
        });
    }
}

function getRecords() {
    var records = $("#records").val();
    if (isNaN(records)) {
        $("#records").val(10);
        return 10;
    }
    if (records > 1000) {
        $("#records").val(1000);
        return 1000;
    }
    else if (records <= 0) {
        $("#records").val(1);
        return 1;
    }
    else {
        return records;
    }
}

function getCookie(name) {
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)"); //正则匹配
    if(arr=document.cookie.match(reg)){
        return decodeURI(arr[2]);
    }
    else{
        return null;
    }
}
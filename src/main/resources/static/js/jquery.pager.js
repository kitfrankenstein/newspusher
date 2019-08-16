/*
* jQuery pager plugin
* Version 1.0 (11/20/2010)
*/
(function($) {

    $.fn.pager = function(options) {
        var opts = $.extend({}, $.fn.pager.defaults, options);

        return this.each(function() {

        // empty out the destination element and then render out the pager with the supplied options
            $(this).empty().append(renderpager(parseInt(options.pagenumber), parseInt(options.pagecount), options.buttonClickCallback, options.method));
            // specify correct cursor activity
            $('.pages li').mouseover(function() { document.body.style.cursor = "pointer"; }).mouseout(function() { document.body.style.cursor = "auto"; });
        });
    };

    // render and return the pager with the supplied options
    function renderpager(pagenumber, pagecount, buttonClickCallback, method) {
        var $pager = $('<ul class="pages"></ul>');
        $pager.append(renderButton('首页', pagenumber, pagecount, buttonClickCallback)).append(renderButton('上一页', pagenumber, pagecount, buttonClickCallback));
        var pageCountInner;
        var records = $("#records").val();
        if(isNaN(pagecount)){
            $.ajax({
                url : baseRoot+method,
                type : "post",
                data : JSON.stringify({
                }),
                contentType:"application/json",
                dataType:"json",
                async:false,
                success : function(data) {
                    var count=data.result;
                    pageCountInner = Math.ceil(count/parseInt(records));
                },
                error: function(e) {
                    alert("jquery-pager.js失败=="+baseRoot+method);
                }
            });
        }else{
            pageCountInner = pagecount;
        }
        var startPoint = 1;
        var endPoint = pageCountInner>6?5:pageCountInner;
        if (pagenumber >= 5) {
            startPoint = pagenumber - 2;
            endPoint = pagenumber + 2;
        }
        if (endPoint >= pageCountInner) {
            endPoint = pageCountInner;
        }
        if (startPoint < 1) {
            startPoint = 1;
        }
        for (var page = startPoint; page <= endPoint; page++) {
            var currentButton = $('<li class="page-number">' + (page) + '</li>');
                page == pagenumber ? currentButton.addClass('pgCurrent') : currentButton.click(function(){
                buttonClickCallback(this.firstChild.data);
            });
            currentButton.appendTo($pager);
        }
        if(pageCountInner > 1){
            var pageSpan = $("<span>(共"+pageCountInner+"页)</span>");
            $pager.append(renderButton('下一页', pagenumber, pageCountInner, buttonClickCallback)).append(renderButton('末页', pagenumber, pageCountInner, buttonClickCallback)).append(pageSpan);
        }
        return $pager;
    }

    // renders and returns a 'specialized' button, ie 'next', 'previous' etc. rather than a page number button
    function renderButton(buttonLabel, pagenumber, pagecount, buttonClickCallback) {

        var $Button = $('<li class="pgNext">' + buttonLabel + '</li>');

        var destPage = 1;

        // work out destination page for required button type
        switch (buttonLabel) {
            case "首页":
                destPage = 1;
                break;
            case "上一页":
                destPage = pagenumber - 1;
                break;
            case "下一页":
                destPage = pagenumber + 1;
                break;
            case "末页":
                destPage = pagecount;
                break;
        }

        // disable and 'grey' out buttons if not needed.
       if (buttonLabel == "首页" || buttonLabel == "上一页") {
            pagenumber <= 1 ? $Button.addClass('pgEmpty') : $Button.click(function() {
                buttonClickCallback(destPage);
            });
        }
        else {
            pagenumber >= pagecount ? $Button.addClass('pgEmpty') : $Button.click(function() {
                buttonClickCallback(destPage);
            });
        }

        return $Button;
    }
    // pager defaults. hardly worth bothering with in this case but used as placeholder for expansion in the next version
    $.fn.pager.defaults = {
        pagenumber: 1,
        pagecount: 1
    };
})(jQuery);






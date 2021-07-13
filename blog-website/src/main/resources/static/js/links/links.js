//加载页面时执行一次
changeMargin();
//监听浏览器宽度的改变
window.onresize = function () {
    changeMargin();
};

function changeMargin() {
    //获取网页可见区域宽度/
    var docWidth = document.body.clientWidth;
    if (docWidth <= 768) {
        $("#box").attr("align", "center")
    } else {
        $("#box").attr("align", "left")
    }

}

$(function () {
    function tishishow(text) {
        var isShow = $("#tishi").css("display");
        if (isShow == "none") {
            $(".tishitext").text(text);
            $("#tishi").fadeIn("fast");
            setTimeout(function () {
                $("#tishi").fadeOut("fast");
            }, 2000)
        }

    }

    $("#apply").click(function () {
        var title = $("#title").val().trim();
        if (title == "") {
            tishishow('请输入您的网站名称');
            $("#title").focus();
            return;
        }
        var link = $("#link").val().trim();
        if (link == "") {
            tishishow('请输入您的网站链接');
            $("#link").focus();
            return;
        }
        var linkPar = /(http|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/;
        if (linkPar.test(link) == false) {
            tishishow('请输入正确的网站链接');
            $("#link").focus();
            return;
        }
        var logo = $("#logoLink").val().trim();
        if (logo == "") {
            tishishow('请输入您的网站LOGO');
            $("#logoLink").focus();
            return;
        }
        var email=$("#email").val().trim();
        if (email != '') {
            var reg= /^[A-Za-zd0-9]+([-_.][A-Za-zd]+)*@([A-Za-zd]+[-.])+[A-Za-zd]{2,5}$/;
            if (!reg.test(email)) {
                tishishow('邮箱格式不正确');
                $("#email").focus();
                return;
            }
        }
        tishishow('申请成功');
        setTimeout(function () {
            $("form").submit();
        }, 1000)

    })
})
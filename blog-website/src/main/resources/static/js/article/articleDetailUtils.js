$(function () {
    init();

    function init() {
        $.ajax({
            url: '/article/getArticleContent',
            method: 'GET',
            data: {
                uuid: $('#uuid').val()
            },
            async: false,
            dataType: 'JSON',
            success: function (res) {
                if (res.resultCode == 1) {
                    $('#main').html(marked(res.resultData));
                    loadSideBar();
                }
            }
        })
    }

})

function loadSideBar() {
    var titles = $('#main').find('h1,h2,h3,h4,h5,h6')
    var html = '<ul>'
    var isH1 = false;
    var ish2 = false
    $.each(titles, function (index, item) {
        var tagName = item.tagName;
        var title = item.innerText.trim();
        var idTarget = item.attributes["id"].nodeValue;
        if (tagName == 'H1') {
            isH1 = true
            html += '<li><a class="section-link" href="#/?id=' + idTarget + '">' + title + '</a></li>'
        } else if (tagName == 'H2') {
            ish2 = true
            if (isH1) {
                html += '<ul><li><a class="section-link" href="#/?id=' + idTarget + '">' + title + '</a></li></ul>';
            } else {
                html += '<li><a class="section-link" href="#/?id=' + idTarget + '">' + title + '</a></li>';
            }
        } else if (tagName == 'H3') {
            if (ish2) {
                html += '<ul><li><a class="section-link level3" href="#/?id=' + idTarget + '">' + title + '</a></li></ul>';
            } else {
                html += '<li><a class="section-link" href="#/?id=' + idTarget + '">' + title + '</a></li>';
            }
        }
    })
    html += '</ul>'
    $('.sidebar-nav').append(html);
}

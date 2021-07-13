!function () {
    "use strict";
    var e = "dimension1",
        n = "dimension5";
    const t = function () {
        let e;
        try {
            e = window.localStorage
        } catch (e) {
        }
        return e || {}
    }();
    if (window.ga = window.ga || function () {
        (ga.q = ga.q || []).push(arguments)
    },
        ga.l = +new Date,
        ga("create", "UA-126406676-2"),
        ga("set", "transport", "beacon"),
        ga("set", e, t.webdev_isSignedIn ? 1 : 0),
        ga("set", n, 3),
        ga("send", "pageview"),
    "noModule" in HTMLScriptElement.prototype) {
        function o() {
            const e = document.createElement("script");
            // e.type = "module",
            //     e.src = "/js/app-6b613d28.js",
            //     document.head.append(e)
        }

        "complete" === document.readyState ? o() : window.addEventListener("load", o)
    } else
        !function () {
            if ("serviceWorker" in navigator)
                navigator.serviceWorker.getRegistrations().then(e => Promise.all(e.map(e => e.unregister()))).then(e => {
                    e.length && window.location.reload()
                })
        }()
}();
//# sourceMappingURL=bootstrap.js.map

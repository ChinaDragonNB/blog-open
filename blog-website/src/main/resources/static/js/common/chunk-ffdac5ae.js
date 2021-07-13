var dimensions = {
    SIGNED_IN: "dimension1",
    TRACKING_VERSION: "dimension5"
};

function n(e, t) {
    for (var n in t)
        e[n] = t[n];
    return e
}

function createStore(e) {
    var t = [];

    function o(e) {
        for (var n = [], o = 0; o < t.length; o++)
            t[o] === e ? e = null : n.push(t[o]);
        t = n
    }

    function r(o, r, a) {
        e = r ? o : n(n({}, e), o);
        for (var l = t, i = 0; i < l.length; i++)
            l[i](e, a)
    }

    return e = e || {},
        {
            action: function (t) {
                function n(e) {
                    r(e, !1, t)
                }

                return function () {
                    for (var o = arguments, r = [e], a = 0; a < arguments.length; a++)
                        r.push(o[a]);
                    var l = t.apply(this, r);
                    if (null != l)
                        return l.then ? l.then(n) : n(l)
                }
            },
            setState: r,
            subscribe: function (e) {
                return t.push(e),
                    function () {
                        o(e)
                    }
            },
            unsubscribe: o,
            getState: function () {
                return e
            }
        }
}

function getMeta(e) {
    const t = document.querySelector(`meta[name="${e}"]`);
    return t ? t.getAttribute("content") || t.getAttribute("value") : null
}

function getLocalStorage() {
    let e;
    try {
        e = window.localStorage
    } catch (e) {
    }
    return e || {}
}

const localStorage = getLocalStorage();
var isProd = !0
    , env = "prod"
    , firebaseConfig = {
    apiKey: "AIzaSyCyThSjI_ZUT1NwV9aQLtqklVcNj72gvo8",
    authDomain: "auth.web.dev",
    databaseURL: "https://web-dev-production-1.firebaseio.com",
    projectId: "web-dev-production-1",
    storageBucket: "web-dev-production-1.appspot.com",
    messagingSenderId: "1051961234704",
    appId: "1:1051961234704:web:d706ff04eb3dc39d128195",
    measurementId: "G-RY6ENK9E06"
};
const initialState = {
    checkingSignedInState: !0,
    isSignedIn: Boolean(localStorage.webdev_isSignedIn),
    user: null,
    userUrlSeen: null,
    userUrl: null,
    userUrlResultsPending: !1,
    activeLighthouseUrl: null,
    lighthouseResult: null,
    lighthouseError: null,
    currentUrl: window.location.pathname,
    isOffline: Boolean(getMeta("offline")),
    isSideNavExpanded: !1,
    isModalOpen: !1,
    isSearchExpanded: !1,
    isPageLoading: !1,
    userAcceptsCookies: !isProd,
    showingSnackbar: !1,
    snackbarType: null
};
let store, globalHandler, recentActiveUrl;

function getAnalyticsDataFromElement(e, t = "click") {
    return {
        category: e.dataset.category || void 0,
        action: e.dataset.action || t,
        label: e.dataset.label || void 0,
        value: Number(e.dataset.value) || void 0
    }
}

function trackEvent({category: e, action: t, label: n, value: o}) {
    ga("send", "event", {
        eventCategory: e,
        eventAction: t,
        eventLabel: n,
        eventValue: o
    })
}

function trackError(e, t = "", n = !1) {
    const o = t ? `${t} (${e.message})` : e.message;
    ga("send", "exception", {
        exDescription: o,
        exFatal: n
    })
}

store = createStore(initialState),
    document.addEventListener("click", e => {
            const t = e.target.closest("a[href], .gc-analytics-event");
            if (!t)
                return;
            const n = getAnalyticsDataFromElement(t);
            n.category && trackEvent(n)
        }
    ),
    store.subscribe(({isSignedIn: e}) => {
            ga("set", dimensions.SIGNED_IN, e ? 1 : 0)
        }
    );

class AbortControllerPolyfill {
    constructor() {
        let e = !1;
        const t = [];
        this.abort = () => {
            e = !0;
            const n = new CustomEvent("AbortEvent");
            t.forEach(e => e(n)),
                t.splice(0, t.length)
        }
            ,
            this.signal = Object.freeze({
                get aborted() {
                    return e
                },
                addEventListener(n, o) {
                    e || "abort" !== n || t.push(o)
                }
            })
    }
}

function getUrl() {
    return window.location.pathname + window.location.search
}

function onReplaceState(e) {
    recentActiveUrl = getUrl()
}

function onPopState(e) {
    const t = getUrl();
    recentActiveUrl !== t && (recentActiveUrl = t,
        globalHandler())
}

function scrollToHashOrTop(e) {
    if (e.startsWith("#") && (e = e.substr(1)),
        e) {
        const t = document.getElementById(e);
        if (t)
            return void t.scrollIntoView()
    }
    document.documentElement.scrollTop = 0
}

function onClick(e) {
    if (e.ctrlKey || e.metaKey || e.altKey || e.shiftKey || e.button || e.defaultPrevented)
        return;
    const t = e.target.closest("a[href]");
    !t || t.target || t.host !== location.host || t.pathname.match(/\.(jpg|png|gif|svg|webp)$/) || route(t.href) && e.preventDefault()
}

function listen(e) {
    if (!e)
        throw new Error("need handler");
    listen = () => {
        throw new Error("listen can only be called once")
    }
    ;
    let t = null;
    globalHandler = (n, o) => {
        const r = Boolean(n);
        r || (n = getUrl(),
            o = window.location.hash);
        const a = null === t;
        a || t.abort();
        const l = t = new AbortController
            , i = r ? null : window.history.state;
        let s = !1;
        const c = (e, t) => {
            if (l.signal.aborted || s)
                return !1;
            s = !0;
            const n = e + o;
            r ? window.history.pushState(t, null, n) : window.history.replaceState(t, null, n),
                recentActiveUrl = e
        }
            , u = {
            firstRun: a,
            url: n,
            signal: l.signal,
            ready: c,
            state: i
        };
        return Promise.resolve(e(u)).then(() => (c(n, i),
            l.signal.aborted)).catch(e => {
                if (!l.signal.aborted && !a)
                    throw window.location.href = n,
                        e;
                return console.warn("err loading", n, e),
                    !0
            }
        )
    }
        ,
        window.addEventListener("replacestate", onReplaceState),
        window.addEventListener("popstate", onPopState),
        window.addEventListener("click", onClick),
        globalHandler()
}

function route(e) {
    if (!globalHandler)
        throw new Error("listen() not called");
    const t = new URL(e, window.location)
        , n = t.pathname + t.search;
    return (n !== getUrl() || !t.hash) && (globalHandler(n, t.hash).then(e => {
            e || scrollToHashOrTop(t.hash)
        }
    ),
        !0)
}

function reload() {
    globalHandler()
}

window.AbortController = window.AbortController || AbortControllerPolyfill;
export {
    localStorage as a,
    route as b,
    env as e,
    firebaseConfig as f,
    listen as l,
    reload as r,
    store as s,
    trackError as t
};
//# sourceMappingURL=chunk-ffdac5ae.js.map

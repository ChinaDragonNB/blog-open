import {
    t as trackError,
    f as firebaseConfig,
    s as store,
    a as localStorage,
    b as route
} from "/js/common/chunk-ffdac5ae.js";
import {
    L as LitElement,
    h as html,
    u as until
} from "/js/common/lit-ec003724.js";
import {
    a as algoliasearch
} from "/js/common/algolia-61d8d050.js";

class BaseElement extends LitElement {
    constructor() {
        super()
    }

    createRenderRoot() {
        return this
    }
}

function styleInject(e, t) {
    void 0 === t && (t = {});
    var i = t.insertAt;
    if (e && "undefined" != typeof document) {
        var n = document.head || document.getElementsByTagName("head")[0],
            o = document.createElement("style");
        o.type = "text/css",
            "top" === i && n.firstChild ? n.insertBefore(o, n.firstChild) : n.appendChild(o),
            o.styleSheet ? o.styleSheet.cssText = e : o.appendChild(document.createTextNode(e))
    }
}

const libs = {},
    // firebasePrefix = "//www.gstatic.com/firebasejs/6.6.1";
    firebasePrefix = "";

function internalLoad(e) {
    if (e in libs)
        return libs[e];
    const t = new Promise((t, i) => {
        const n = document.createElement("script");
        // n.src = `${firebasePrefix}/firebase-${e}.js`,
        n.src = ``,
            n.async = !1,
            n.onerror = i,
            n.onload = () => t(),
            document.head.append(n)
    });
    return libs[e] = t,
        t
}

function loadFirebase(...e) {
    return Promise.all(e.map(internalLoad))
}

const firebasePromise = loadFirebase("app", "auth", "performance").then(() => window.firebase);
firebasePromise.then(initialize).catch(e => {
    // console.error("failed to load Firebase", e),
    //     trackError(e, "firebase load")
});
const firestorePromiseLoader = (() => {
    let e;
    return () => e || (e = loadFirebase("firestore").then(() => window.firebase.firestore()),
        e)
})();

function initialize(e) {
    e.initializeApp(firebaseConfig),
        e.performance();
    let t = () => {
        },
        i = null;
    const n = e => {
        let t = !1;
        const n = e.data() || {},
            o = n.currentUrl || "",
            {
                userUrl: s,
                userUrlSeen: a,
                activeLighthouseUrl: r
            } = store.getState();
        if (null !== r)
            ;
        else if (i && i !== o)
            t = !0;
        else if (s) {
            if (!i && s)
                return saveUserUrl(s, a),
                    void (i = s)
        } else
            t = !0;
        if (i = o,
            t) {
            const e = n.urls && n.urls[o] || null,
                t = e ? e.toDate() : null,
                i = Boolean(o);
            store.setState({
                userUrl: o,
                userUrlSeen: t,
                userUrlResultsPending: i
            })
        }
    };
    e.auth().onAuthStateChanged(e => {
        store.setState({
            checkingSignedInState: !1
        }),
            t(),
            e ? (store.setState({
                isSignedIn: !0,
                user: e
            }),
                i = null,
                t = function () {
                    let e = null,
                        t = !1;
                    return userRef().then(i => {
                        t || (e = i.onSnapshot(n))
                    }).catch(e => {
                        console.warn("failed to load Firestore library", e),
                            trackError(e, "firestore load")
                    }),
                        () => {
                            t = !0,
                            e && (e(),
                                e = null)
                        }
                }()) : clearSignedInState()
    })
}

async function userRef() {
    const e = store.getState();
    return e.user ? (await firestorePromiseLoader()).collection("users").doc(e.user.uid) : null
}

async function saveUserUrl(e, t = null) {
    const i = await userRef();
    if (!i)
        return null;
    const n = (await firestorePromiseLoader()).runTransaction(async n => {
        const o = (await n.get(i)).data() || {},
            s = {
                currentUrl: e
            },
            a = o.urls && o.urls[e] || null;
        if (a) {
            const e = a.toDate();
            e.getTime() && e.getTime() < t.getTime() && (t = e)
        } else
            t && t.getTime() && (s.urls = {
                [e]: t
            });
        return n.set(i, s, {
            merge: !0
        })
    });
    try {
        await n
    } catch (e) {
        console.warn("could not write URL to Firestore", e),
            trackError(e, "write URL")
    }
    return t
}

async function signIn() {
    let e = null;
    try {
        await firebasePromise;
        const t = new firebase.auth.GoogleAuthProvider;
        e = (await firebase.auth().signInWithPopup(t)).user
    } catch (e) {
        console.error("signIn error", e),
            trackError(e, "signIn")
    }
    return e
}

async function signOut() {
    try {
        await firebasePromise,
            await firebase.auth().signOut()
    } catch (e) {
        console.error("signOut error", e),
            trackError(e, "signOut")
    }
}

const LH_HOST = "https://lighthouse-dot-webdotdevsite.appspot.com/";

async function runLighthouse(e, t = !1) {
    const i = {
            url: e,
            replace: !0,
            save: t
        },
        n = await fetch(`${LH_HOST}/lh/newaudit`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(i)
        }),
        o = await n.json();
    if (!n.ok)
        throw new Error(o.errors);
    if (!o.lhrSlim)
        throw new Error("unexpected result, no lhrSlim key");
    return o
}

async function fetchReports(e, t = null) {
    const i = window.encodeURIComponent(e);
    let n = `${LH_HOST}/lh/reports?url=${i}`;
    t && (n += `&since=${t.getTime()}`);
    const o = await window.fetch(n),
        s = await o.json();
    if (!o.ok)
        throw new Error(s.errors);
    return s
}

const clearSignedInState = store.action(() => {
        const {
            isSignedIn: e
        } = store.getState();
        if (e)
            return {
                userUrlSeen: null,
                userUrl: null,
                checkingSignedInState: !1,
                isSignedIn: !1,
                user: null,
                lighthouseResult: null,
                lighthouseError: null
            }
    }),
    requestRunLighthouse = store.action((e, t) => (async () => {
        if (e.activeLighthouseUrl)
            return null;
        store.setState({
            activeLighthouseUrl: t,
            lighthouseError: null
        });
        const i = await runLighthouse(t, e.isSignedIn),
            n = new Date(i.auditedOn);
        e = store.getState();
        const o = await saveUserUrl(t, n),
            s = await fetchReports(t, o);
        return {
            userUrl: t,
            activeLighthouseUrl: null,
            lighthouseResult: {
                url: t,
                runs: s
            }
        }
    })().catch(e => {
        console.warn("failed to run Lighthouse", t, e);
        const i = {
                lighthouseError: e.toString(),
                activeLighthouseUrl: null
            },
            {
                lighthouseResult: n
            } = store.getState();
        return n && n.url !== t && (i.lighthouseResult = null),
            i
    })),
    requestFetchReports = store.action((e, t, i) => (async () => {
        const e = await fetchReports(t, i),
            {
                activeLighthouseUrl: n
            } = store.getState();
        return n ? null : {
            userUrl: t,
            userUrlSeen: i,
            activeLighthouseUrl: null,
            lighthouseResult: {
                url: t,
                runs: e
            }
        }
    })().catch(e => {
        console.warn("failed to fetch reports for", t, e);
        const {
            activeLighthouseUrl: i
        } = store.getState();
        if (i)
            return null;
        const n = {
                userUrl: t,
                lighthouseError: e.toString()
            },
            {
                lighthouseResult: o
            } = store.getState();
        return o && o.url !== t && (n.lighthouseResult = null),
            n
    })),
    expandSideNav = store.action(() => (openModal(), {
        isSideNavExpanded: !0
    })),
    collapseSideNav = store.action(() => (closeModal(), {
        isSideNavExpanded: !1
    })),
    openModal = store.action(() => {
        const e = document.querySelector("main"),
            t = document.querySelector("web-header"),
            i = document.querySelector(".w-footer");
        console.log(e)
        return document.documentElement.classList.add("web-modal__overflow-hidden"),
            e.inert = !0,
            t.inert = !0,
            i.inert = !0, {
            isModalOpen: !0
        }
    }),
    closeModal = store.action(() => {
        const e = document.querySelector("main"),
            t = document.querySelector("web-header"),
            i = document.querySelector(".w-footer");
        return document.documentElement.classList.remove("web-modal__overflow-hidden"),
            e.inert = !1,
            t.inert = !1,
            i.inert = !1, {
            isModalOpen: !1
        }
    }),
    checkIfUserAcceptsCookies = store.action(({
                                                  userAcceptsCookies: e
                                              }) => {
        if (!e)
            return localStorage["web-accepts-cookies"] ? {
                userAcceptsCookies: !0
            } : {
                showingSnackbar: !0,
                snackbarType: "cookies"
            }
    }),
    setUserAcceptsCookies = store.action(() => (localStorage["web-accepts-cookies"] = 1, {
        userAcceptsCookies: !0,
        showingSnackbar: !1
    }));

class BaseStateElement extends BaseElement {
    constructor() {
        super(),
            this.onStateChanged = this.onStateChanged.bind(this)
    }

    connectedCallback() {
        super.connectedCallback(),
            store.subscribe(this.onStateChanged),
            this.onStateChanged(store.getState())
    }

    disconnectedCallback() {
        super.disconnectedCallback(),
            store.unsubscribe(this.onStateChanged)
    }

    onStateChanged(e) {
    }
}

var css_248z = 'web-profile-switcher-container{-webkit-box-align:center;-ms-flex-align:center;align-items:center;display:-webkit-box;display:-ms-flexbox;display:flex;height:100%}@media (min-width:865px){web-profile-switcher-container{margin-left:16px}}.w-profile-signin{background:transparent;border:0;-webkit-box-shadow:none;box-shadow:none;color:#202124;cursor:pointer;font-family:Roboto,sans-serif;text-transform:uppercase;font-size:14px;font-weight:500;height:100%;outline:none;padding:0 8px}@media (min-width:865px){.w-profile-signin{padding:0 24px}}.w-profile-signin:hover{background-color:rgba(32,33,36,.04)}.w-profile-signin:focus{background-color:rgba(32,33,36,.12)}.w-profile-signin:active{background-color:rgba(32,33,36,.16)}web-profile-switcher{display:block;margin:0 0 0 16px}.w-profile-toggle{background:#f1f3f4;border:4px solid #fff;border-radius:50%;-webkit-box-sizing:content-box;box-sizing:content-box;display:-webkit-box;display:-ms-flexbox;display:flex;height:auto;-webkit-box-pack:center;-ms-flex-pack:center;justify-content:center;min-height:32px;min-width:32px;overflow:hidden;padding:0;width:auto}.w-profile-toggle,.w-profile-toggle:active,.w-profile-toggle:focus,.w-profile-toggle:hover{-webkit-box-shadow:none;box-shadow:none}.w-profile-toggle:focus{border-color:rgba(0,0,0,.2)!important}@-webkit-keyframes w-profile-toggle__appear{0%{opacity:0}to{opacity:1}}@keyframes w-profile-toggle__appear{0%{opacity:0}to{opacity:1}}.w-profile-toggle__photo{border-radius:50%;height:32px;width:32px;-webkit-animation:w-profile-toggle__appear .45s;animation:w-profile-toggle__appear .45s}.w-profile-dialog{background:#fff;border:1px solid rgba(0,0,0,.2);border-radius:2px;-webkit-box-shadow:0 2px 10px rgba(0,0,0,.2);box-shadow:0 2px 10px rgba(0,0,0,.2);display:block;overflow:hidden;position:absolute;right:15px;top:62px}@media (min-width:481px){.w-profile-dialog{right:24px}}.w-profile-dialog__user{padding:20px}@media (min-width:321px){.w-profile-dialog__user{display:-webkit-box;display:-ms-flexbox;display:flex}}.w-profile-dialog__photo-container{-webkit-box-align:center;-ms-flex-align:center;align-items:center;display:-webkit-box;display:-ms-flexbox;display:flex;-webkit-box-pack:center;-ms-flex-pack:center;justify-content:center;margin:0 0 20px}@media (min-width:321px){.w-profile-dialog__photo-container{margin:0 20px 0 0}}.w-profile-dialog__photo{border-radius:50%;display:block;height:64px;width:64px}@media (min-width:321px){.w-profile-dialog__photo{height:96px;width:96px}}.w-profile-dialog__details{font-size:13px;line-height:normal}.w-profile-dialog__name{font-weight:500;margin-bottom:1px}.w-profile-dialog__email{color:#666;overflow:hidden;text-overflow:ellipsis}.w-profile-dialog__privacy{color:#1a73e8;display:block;margin:6px 0}.w-profile-dialog__privacy:hover{text-decoration:none}.w-profile-dialog__account{background:#4d90fe;border:1px solid #3079ed;border-radius:2px;color:#fff;display:inline-block;font:13px/28px Roboto,Noto Sans,sans-serif;margin:10px 0 0;padding:0 12px;position:relative;text-align:center;white-space:nowrap;width:100%}@media (min-width:321px){.w-profile-dialog__account{width:auto}}.w-profile-dialog__account:active,.w-profile-dialog__account:focus,.w-profile-dialog__account:hover{outline:none;text-decoration:none}.w-profile-dialog__account:after{bottom:0;content:"";left:0;pointer-events:none;position:absolute;right:0;top:0;-webkit-transition:background-color .2s,border .2s;transition:background-color .2s,border .2s;z-index:1}.w-profile-dialog__account:hover:after{background-color:hsla(0,0%,100%,.08)}.w-profile-dialog__account:focus:after{background-color:hsla(0,0%,100%,.12)}.w-profile-dialog__account:active:after{background-color:hsla(0,0%,100%,.24)}.w-profile-dialog__controls{background:#f5f5f5;border-top:1px solid rgba(0,0,0,.2);display:block;padding:10px 20px}@media (min-width:321px){.w-profile-dialog__controls{-webkit-box-align:center;-ms-flex-align:center;align-items:center;display:-webkit-box;display:-ms-flexbox;display:flex;-webkit-box-pack:justify;-ms-flex-pack:justify;justify-content:space-between}}.w-profile-dialog__button{background:#f8f8f8;border:1px solid #c6c6c6;border-radius:2px;-webkit-box-shadow:none;box-shadow:none;color:#666;display:block;font:13px/28px Roboto,Noto Sans,sans-serif;height:auto;margin:0;padding:0 12px;position:relative;text-align:center;text-transform:none;width:100%}@media (min-width:321px){.w-profile-dialog__button{width:auto}}.w-profile-dialog__button:not(:last-of-type){margin-bottom:8px}@media (min-width:321px){.w-profile-dialog__button:not(:last-of-type){margin:0}}.w-profile-dialog__button:focus{background:#f8f8f8;outline:none;text-decoration:none}.w-profile-dialog__button:after{bottom:0;content:"";left:0;pointer-events:none;position:absolute;right:0;top:0;-webkit-transition:background-color .2s,border .2s;transition:background-color .2s,border .2s;z-index:1}.w-profile-dialog__button:hover:after{background-color:rgba(0,0,0,.04)}.w-profile-dialog__button:focus:after{background-color:rgba(0,0,0,.08)}.w-profile-dialog__button:active:after{background-color:rgba(0,0,0,.1)}';
styleInject(css_248z);
const emptyFrag = document.createDocumentFragment();

class ProfileSwitcher extends BaseElement {
    static get properties() {
        return {
            expanded: {
                type: Boolean
            },
            user: {
                type: Object
            },
            photoPromise: {
                type: Promise
            }
        }
    }

    render() {
        return html`
      <button
        class="w-profile-toggle"
        .disabled=${!this.user}
        @click="${() => this.expanded = !this.expanded}"
      >
        ${until(this.photoPromise)}
      </button>
      ${this.expanded && this.user ? this.expandedTemplate : ""}
    `
    }

    firstUpdated() {
        this.addEventListener("keyup", e => {
            "Escape" === e.key && this.expanded && (this.expanded = !1)
        }),
            document.addEventListener("click", e => {
                this.expanded && !this.contains(e.target) && (this.expanded = !1)
            })
    }

    shouldUpdate(e) {
        return !e.has("user") || (this.expanded = !1,
            this.user ? (this.photoPromise = new Promise(e => {
                const t = new Image;
                t.src = this.user.photoURL,
                    t.className = "w-profile-toggle__photo",
                    t.onload = () => e(t),
                    t.onerror = () => e(emptyFrag)
            }),
                !0) : (this.photoPromise = null,
                !0))
    }

    get expandedTemplate() {
        return html`
      <div class="w-profile-dialog">
        <div class="w-profile-dialog__user">
          <div class="w-profile-dialog__photo-container">
            <img class="w-profile-dialog__photo" src="${this.user.photoURL}" />
          </div>
          <div class="w-profile-dialog__details">
            <div class="w-profile-dialog__name">
              ${this.user.displayName}
            </div>
            <div class="w-profile-dialog__email">
              ${this.user.email}
            </div>
            <a
              class="w-profile-dialog__privacy"
              href="https://myaccount.google.com/privacypolicy"
              target="_blank"
            >
              Privacy
            </a>
            <a
              class="w-profile-dialog__account"
              href="https://myaccount.google.com"
              target="_blank"
            >
              Google Account
            </a>
          </div>
        </div>
        <div class="w-profile-dialog__controls">
          <button class="w-profile-dialog__button" @click="${signIn}">
            Change accounts
          </button>
          <button class="w-profile-dialog__button" @click="${signOut}">
            Sign out
          </button>
        </div>
      </div>
    `
    }
}

customElements.define("web-profile-switcher", ProfileSwitcher);

class ProfileSwitcherContainer extends BaseStateElement {
    static get properties() {
        return {
            checkingSignedInState: {
                type: Boolean
            },
            isSignedIn: {
                type: Boolean
            },
            user: {
                type: Object
            }
        }
    }

    render() {
        //     return this.isSignedIn ? html `
        //     <web-profile-switcher .user="${this.user}"></web-profile-switcher>
        //   ` : html `
        //   <button
        //     class="w-profile-signin"
        //     .disabled=${this.checkingSignedInState}
        //     @click="${signIn}"
        //   >
        //     Sign in
        //   </button>
        // `
    }

    onStateChanged({
                       checkingSignedInState: e,
                       isSignedIn: t,
                       user: i
                   }) {
        this.checkingSignedInState = e,
            this.isSignedIn = t,
            this.user = i
    }
}

customElements.define("web-profile-switcher-container", ProfileSwitcherContainer);

class Header extends HTMLElement {
    constructor() {
        super(),
            this.onStateChanged = this.onStateChanged.bind(this)
    }

    connectedCallback() {
        this.hamburgerBtn = this.querySelector(".web-header__hamburger-btn"),
            this.hamburgerBtn.classList.remove("unresolved"),
            this.hamburgerBtn.addEventListener("click", expandSideNav),
            store.subscribe(this.onStateChanged)
    }

    disconnectedCallback() {
        this.hamburgerBtn.removeEventListener("click", expandSideNav),
            store.unsubscribe(this.onStateChanged)
    }

    onStateChanged({
                       isSearchExpanded: e,
                       currentUrl: t
                   }) {
        this.classList.toggle("web-header--has-expanded-search", e),
            t = ((t = t.replace(/"/g, '\\"')).match(/^\/\w+\//) || [""])[0];
        const i = this.querySelector("[active]"),
            n = this.querySelector(`[href="${t}"]`);
        i !== n && (i && (i.removeAttribute("active"),
            i.removeAttribute("aria-current")),
        n && (n.setAttribute("active", ""),
            n.setAttribute("aria-current", "page")))
    }

    manageFocus() {
        this.hamburgerBtn.focus()
    }
}

customElements.define("web-header", Header);
const slice = Array.prototype.slice,
    matches = Element.prototype.matches || Element.prototype.msMatchesSelector,
    _focusableElementsString = ["a[href]", "area[href]", "input:not([disabled])", "select:not([disabled])", "textarea:not([disabled])", "button:not([disabled])", "details", "summary", "iframe", "object", "embed", "[contenteditable]"].join(",");

class InertRoot {
    constructor(e, t) {
        this._inertManager = t,
            this._rootElement = e,
            this._managedNodes = new Set,
            this._rootElement.hasAttribute("aria-hidden") ? this._savedAriaHidden = this._rootElement.getAttribute("aria-hidden") : this._savedAriaHidden = null,
            this._rootElement.setAttribute("aria-hidden", "true"),
            this._makeSubtreeUnfocusable(this._rootElement),
            this._observer = new MutationObserver(this._onMutation.bind(this)),
            this._observer.observe(this._rootElement, {
                attributes: !0,
                childList: !0,
                subtree: !0
            })
    }

    destructor() {
        this._observer.disconnect(),
        this._rootElement && (null !== this._savedAriaHidden ? this._rootElement.setAttribute("aria-hidden", this._savedAriaHidden) : this._rootElement.removeAttribute("aria-hidden")),
            this._managedNodes.forEach((function (e) {
                this._unmanageNode(e.node)
            }), this),
            this._observer = null,
            this._rootElement = null,
            this._managedNodes = null,
            this._inertManager = null
    }

    get managedNodes() {
        return new Set(this._managedNodes)
    }

    get hasSavedAriaHidden() {
        return null !== this._savedAriaHidden
    }

    set savedAriaHidden(e) {
        this._savedAriaHidden = e
    }

    get savedAriaHidden() {
        return this._savedAriaHidden
    }

    _makeSubtreeUnfocusable(e) {
        composedTreeWalk(e, e => this._visitNode(e));
        let t = document.activeElement;
        if (!document.body.contains(e)) {
            let i = e,
                n = void 0;
            for (; i;) {
                if (i.nodeType === Node.DOCUMENT_FRAGMENT_NODE) {
                    n = i;
                    break
                }
                i = i.parentNode
            }
            n && (t = n.activeElement)
        }
        e.contains(t) && (t.blur(),
        t === document.activeElement && document.body.focus())
    }

    _visitNode(e) {
        if (e.nodeType !== Node.ELEMENT_NODE)
            return;
        const t = e;
        t !== this._rootElement && t.hasAttribute("inert") && this._adoptInertRoot(t),
        (matches.call(t, _focusableElementsString) || t.hasAttribute("tabindex")) && this._manageNode(t)
    }

    _manageNode(e) {
        const t = this._inertManager.register(e, this);
        this._managedNodes.add(t)
    }

    _unmanageNode(e) {
        const t = this._inertManager.deregister(e, this);
        t && this._managedNodes.delete(t)
    }

    _unmanageSubtree(e) {
        composedTreeWalk(e, e => this._unmanageNode(e))
    }

    _adoptInertRoot(e) {
        let t = this._inertManager.getInertRoot(e);
        t || (this._inertManager.setInert(e, !0),
            t = this._inertManager.getInertRoot(e)),
            t.managedNodes.forEach((function (e) {
                this._manageNode(e.node)
            }), this)
    }

    _onMutation(e, t) {
        e.forEach((function (e) {
            const t = e.target;
            if ("childList" === e.type)
                slice.call(e.addedNodes).forEach((function (e) {
                    this._makeSubtreeUnfocusable(e)
                }), this),
                    slice.call(e.removedNodes).forEach((function (e) {
                        this._unmanageSubtree(e)
                    }), this);
            else if ("attributes" === e.type)
                if ("tabindex" === e.attributeName)
                    this._manageNode(t);
                else if (t !== this._rootElement && "inert" === e.attributeName && t.hasAttribute("inert")) {
                    this._adoptInertRoot(t);
                    const e = this._inertManager.getInertRoot(t);
                    this._managedNodes.forEach((function (i) {
                        t.contains(i.node) && e._manageNode(i.node)
                    }))
                }
        }), this)
    }
}

class InertNode {
    constructor(e, t) {
        this._node = e,
            this._overrodeFocusMethod = !1,
            this._inertRoots = new Set([t]),
            this._savedTabIndex = null,
            this._destroyed = !1,
            this.ensureUntabbable()
    }

    destructor() {
        if (this._throwIfDestroyed(),
        this._node && this._node.nodeType === Node.ELEMENT_NODE) {
            const e = this._node;
            null !== this._savedTabIndex ? e.setAttribute("tabindex", this._savedTabIndex) : e.removeAttribute("tabindex"),
            this._overrodeFocusMethod && delete e.focus
        }
        this._node = null,
            this._inertRoots = null,
            this._destroyed = !0
    }

    get destroyed() {
        return this._destroyed
    }

    _throwIfDestroyed() {
        if (this.destroyed)
            throw new Error("Trying to access destroyed InertNode")
    }

    get hasSavedTabIndex() {
        return null !== this._savedTabIndex
    }

    get node() {
        return this._throwIfDestroyed(),
            this._node
    }

    set savedTabIndex(e) {
        this._throwIfDestroyed(),
            this._savedTabIndex = e
    }

    get savedTabIndex() {
        return this._throwIfDestroyed(),
            this._savedTabIndex
    }

    ensureUntabbable() {
        if (this.node.nodeType !== Node.ELEMENT_NODE)
            return;
        const e = this.node;
        if (matches.call(e, _focusableElementsString)) {
            if (-1 === e.tabIndex && this.hasSavedTabIndex)
                return;
            e.hasAttribute("tabindex") && (this._savedTabIndex = e.tabIndex),
                e.setAttribute("tabindex", "-1"),
            e.nodeType === Node.ELEMENT_NODE && (e.focus = function () {
            },
                this._overrodeFocusMethod = !0)
        } else
            e.hasAttribute("tabindex") && (this._savedTabIndex = e.tabIndex,
                e.removeAttribute("tabindex"))
    }

    addInertRoot(e) {
        this._throwIfDestroyed(),
            this._inertRoots.add(e)
    }

    removeInertRoot(e) {
        this._throwIfDestroyed(),
            this._inertRoots.delete(e),
        0 === this._inertRoots.size && this.destructor()
    }
}

class InertManager {
    constructor(e) {
        if (!e)
            throw new Error("Missing required argument; InertManager needs to wrap a document.");
        this._document = e,
            this._managedNodes = new Map,
            this._inertRoots = new Map,
            this._observer = new MutationObserver(this._watchForInert.bind(this)),
            addInertStyle(e.head || e.body || e.documentElement),
            "loading" === e.readyState ? e.addEventListener("DOMContentLoaded", this._onDocumentLoaded.bind(this)) : this._onDocumentLoaded()
    }

    setInert(e, t) {
        if (t) {
            if (this._inertRoots.has(e))
                return;
            const t = new InertRoot(e, this);
            if (e.setAttribute("inert", ""),
                this._inertRoots.set(e, t),
                !this._document.body.contains(e)) {
                let t = e.parentNode;
                for (; t;)
                    11 === t.nodeType && addInertStyle(t),
                        t = t.parentNode
            }
        } else {
            if (!this._inertRoots.has(e))
                return;
            this._inertRoots.get(e).destructor(),
                this._inertRoots.delete(e),
                e.removeAttribute("inert")
        }
    }

    getInertRoot(e) {
        return this._inertRoots.get(e)
    }

    register(e, t) {
        let i = this._managedNodes.get(e);
        return void 0 !== i ? i.addInertRoot(t) : i = new InertNode(e, t),
            this._managedNodes.set(e, i),
            i
    }

    deregister(e, t) {
        const i = this._managedNodes.get(e);
        return i ? (i.removeInertRoot(t),
        i.destroyed && this._managedNodes.delete(e),
            i) : null
    }

    _onDocumentLoaded() {
        slice.call(this._document.querySelectorAll("[inert]")).forEach((function (e) {
            this.setInert(e, !0)
        }), this),
            this._observer.observe(this._document.body, {
                attributes: !0,
                subtree: !0,
                childList: !0
            })
    }

    _watchForInert(e, t) {
        const i = this;
        e.forEach((function (e) {
            switch (e.type) {
                case "childList":
                    slice.call(e.addedNodes).forEach((function (e) {
                        if (e.nodeType !== Node.ELEMENT_NODE)
                            return;
                        const t = slice.call(e.querySelectorAll("[inert]"));
                        matches.call(e, "[inert]") && t.unshift(e),
                            t.forEach((function (e) {
                                this.setInert(e, !0)
                            }), i)
                    }), i);
                    break;
                case "attributes":
                    if ("inert" !== e.attributeName)
                        return;
                    const t = e.target,
                        n = t.hasAttribute("inert");
                    i.setInert(t, n)
            }
        }), this)
    }
}

function composedTreeWalk(e, t, i) {
    if (e.nodeType == Node.ELEMENT_NODE) {
        const i = e;
        t && t(i);
        const n = i.shadowRoot;
        if (n)
            return void composedTreeWalk(n, t);
        if ("content" == i.localName) {
            const e = i,
                n = e.getDistributedNodes ? e.getDistributedNodes() : [];
            for (let e = 0; e < n.length; e++)
                composedTreeWalk(n[e], t);
            return
        }
        if ("slot" == i.localName) {
            const e = i,
                n = e.assignedNodes ? e.assignedNodes({
                    flatten: !0
                }) : [];
            for (let e = 0; e < n.length; e++)
                composedTreeWalk(n[e], t);
            return
        }
    }
    let n = e.firstChild;
    for (; null != n;)
        composedTreeWalk(n, t),
            n = n.nextSibling
}

function addInertStyle(e) {
    if (e.querySelector("style#inert-style"))
        return;
    const t = document.createElement("style");
    t.setAttribute("id", "inert-style"),
        t.textContent = "\n[inert] {\n  pointer-events: none;\n  cursor: default;\n}\n\n[inert], [inert] * {\n  user-select: none;\n  -webkit-user-select: none;\n  -moz-user-select: none;\n  -ms-user-select: none;\n}\n",
        e.appendChild(t)
}

const inertManager = new InertManager(document);
Element.prototype.hasOwnProperty("inert") || Object.defineProperty(Element.prototype, "inert", {
    enumerable: !0,
    get: function () {
        return this.hasAttribute("inert")
    },
    set: function (e) {
        inertManager.setInert(this, e)
    }
});
var css_248z$1 = 'body.web-side-nav--expanded,web-side-nav{overflow:hidden}web-side-nav{position:fixed;left:0;top:0;width:100%;height:100%;pointer-events:none;z-index:300}web-side-nav.unresolved *{display:none!important}web-side-nav:before{content:"";display:block;position:absolute;left:0;top:0;width:100%;height:100%;background:rgba(0,0,0,.4);opacity:0;will-change:opacity;-webkit-transition:opacity .2s cubic-bezier(.4,0,.2,1);transition:opacity .2s cubic-bezier(.4,0,.2,1)}web-side-nav[expanded]{pointer-events:auto;visibility:visible}web-side-nav[expanded]:before{opacity:1}.web-side-nav__container{position:relative;width:90%;max-width:268px;background:#fff;height:100%;-webkit-box-shadow:2px 0 12px rgba(0,0,0,.4);box-shadow:2px 0 12px rgba(0,0,0,.4);-webkit-transform:translateX(-110%);transform:translateX(-110%);display:-webkit-box;display:-ms-flexbox;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-ms-flex-direction:column;flex-direction:column;will-change:transform}web-side-nav[expanded] .web-side-nav__container{-webkit-transform:none;transform:none}web-side-nav[animatable] .web-side-nav__container,web-side-nav[expanded][animatable] .web-side-nav__container{-webkit-transition:-webkit-transform .2s cubic-bezier(.4,0,.2,1);transition:-webkit-transform .2s cubic-bezier(.4,0,.2,1);transition:transform .2s cubic-bezier(.4,0,.2,1);transition:transform .2s cubic-bezier(.4,0,.2,1),-webkit-transform .2s cubic-bezier(.4,0,.2,1)}.web-side-nav__header{-webkit-box-align:center;-ms-flex-align:center;align-items:center;display:-webkit-box;display:-ms-flexbox;display:flex;padding:16px;border-bottom:1px solid #dadce0}.web-side-nav__logo{height:30px;width:125px}.web-side-nav__hide{height:2.75rem;margin:0 10px 0 -6px;width:2.75rem}.web-side-nav__hide:before{-moz-osx-font-smoothing:grayscale;-webkit-font-smoothing:antialiased;font:normal normal normal 24px/1 Material Icons;-webkit-font-feature-settings:"liga";font-feature-settings:"liga";text-rendering:optimizeSpeed;text-transform:none;word-wrap:normal;content:"close"}.web-side-nav__content{-webkit-box-flex:1;-ms-flex:1;flex:1;list-style:none;padding:0;margin:0;overflow-x:hidden;overflow-y:auto;-webkit-overflow-scrolling:touch}.web-side-nav__link{border-bottom:1px solid #dadce0;color:#5f6368;font-size:14px;font-weight:500;padding:16px 24px}.web-side-nav__link[active]{-webkit-box-shadow:0 -2px 0 #3740ff inset;box-shadow:inset 0 -2px 0 #3740ff}.web-side-nav__link:active,.web-side-nav__link:focus,.web-side-nav__link:hover{text-decoration:none;outline:0}.web-side-nav__link:hover{background-color:rgba(32,33,36,.04)}.web-side-nav__link:focus{background-color:rgba(32,33,36,.12)}.web-side-nav__link:active{background-color:rgba(32,33,36,.16)}.web-side-nav__link[data-active]{color:#3740ff}';
styleInject(css_248z$1);

class SideNav extends BaseElement {
    static get properties() {
        return {
            animatable: {
                type: Boolean,
                reflect: !0
            },
            expanded: {
                type: Boolean,
                reflect: !0
            }
        }
    }

    constructor() {
        super(),
            this.inert = !0,
            this.animatable = !1,
            this.expanded_ = !1,
            this.startX_ = 0,
            this.currentX_ = 0,
            this.touchingSideNav_ = !1,
            this.prerenderedChildren_ = null,
            this.onCloseSideNav = this.onCloseSideNav.bind(this),
            this.onTouchStart = this.onTouchStart.bind(this),
            this.onTouchMove = this.onTouchMove.bind(this),
            this.onTouchEnd = this.onTouchEnd.bind(this),
            this.onTransitionEnd = this.onTransitionEnd.bind(this),
            this.drag = this.drag.bind(this),
            this.onStateChanged = this.onStateChanged.bind(this),
            this.onKeyUp = this.onKeyUp.bind(this)
    }

    render() {
        if (!this.prerenderedChildren_) {
            this.prerenderedChildren_ = [];
            for (const e of this.children)
                this.prerenderedChildren_.push(e)
        }
        //     return html `
        //   <nav @click="${this.onBlockClicks}" class="web-side-nav__container">
        //     <div class="web-side-nav__header">
        //       <button
        //         @click="${this.onCloseSideNav}"
        //         data-icon="close"
        //         class="w-button--icon w-button--round web-side-nav__hide"
        //         aria-label="Close"
        //       >
        //         <span class="w-tooltip">Close</span>
        //       </button>
        //       <a
        //         href="/"
        //         class="gc-analytics-event"
        //         data-category="Site-Wide Custom Events"
        //         data-label="Site logo"
        //       >
        //         <img
        //           class="web-side-nav__logo"
        //           src="/images/lockup.svg"
        //           alt="web.dev"
        //         />
        //       </a>
        //     </div>
        //     ${this.prerenderedChildren_}
        //   </nav>
        // `
    }

    connectedCallback() {
        super.connectedCallback(),
            this.tabIndex = -1,
            store.subscribe(this.onStateChanged)
    }

    firstUpdated() {
        this.sideNavContainerEl = this.querySelector(".web-side-nav__container"),
            this.addEventListeners(),
            this.onStateChanged(),
            this.classList.remove("unresolved")
    }

    addEventListeners() {
        this.addEventListener("click", this.onCloseSideNav),
            this.addEventListener("touchstart", this.onTouchStart, {
                passive: !0
            }),
            this.addEventListener("touchmove", this.onTouchMove, {
                passive: !0
            }),
            this.addEventListener("touchend", this.onTouchEnd)
    }

    onStateChanged({
                       currentUrl: e
                   } = {}) {
        const {
            isSideNavExpanded: t
        } = store.getState();
        if (t !== this.expanded && (this.expanded = t,
            e)) {
            e = ((e = e.replace(/"/g, '\\"')).match(/^\/\w+\//) || [""])[0];
            const t = this.querySelector("[active]"),
                i = this.querySelector(`[href="${e}"]`);
            if (t === i)
                return;
            t && (t.removeAttribute("active"),
                t.removeAttribute("aria-current")),
            i && (i.setAttribute("active", ""),
                i.setAttribute("aria-current", "page"))
        }
    }

    onTouchStart(e) {
        this.expanded && (this.startX_ = e.touches[0].pageX,
            this.currentX_ = this.startX_,
            this.touchingSideNav_ = !0,
            requestAnimationFrame(this.drag))
    }

    onTouchMove(e) {
        this.touchingSideNav_ && (this.currentX_ = e.touches[0].pageX)
    }

    onTouchEnd(e) {
        if (!this.touchingSideNav_)
            return;
        this.touchingSideNav_ = !1;
        const t = Math.min(0, this.currentX_ - this.startX_);
        this.sideNavContainerEl.style.transform = "",
        t < 0 && this.onCloseSideNav()
    }

    drag() {
        if (!this.touchingSideNav_)
            return;
        requestAnimationFrame(this.drag);
        const e = Math.min(0, this.currentX_ - this.startX_);
        this.sideNavContainerEl.style.transform = `translateX(${e}px)`
    }

    onBlockClicks(e) {
        e.target.closest("a") || e.stopPropagation()
    }

    onTransitionEnd() {
        this.animatable = !1,
            this.expanded_ ? this.focus() : document.querySelector("web-header").manageFocus(),
            this.inert = !this.expanded_
    }

    onCloseSideNav() {
        collapseSideNav()
    }

    onKeyUp(e) {
        "Escape" === e.key && (collapseSideNav(),
            document.removeEventListener("keyup", this.onKeyUp))
    }

    set expanded(e) {
        if (this.expanded_ === e)
            return;
        const t = this.expanded_;
        this.expanded_ = e,
            this.animatable = !0,
        this.expanded_ && document.addEventListener("keyup", this.onKeyUp),
            this.addEventListener("transitionend", this.onTransitionEnd, {
                once: !0
            }),
            this.requestUpdate("expanded", t)
    }

    get expanded() {
        return this.expanded_
    }

    disconnectedCallback() {
        super.disconnectedCallback(),
            store.unsubscribe(this.onStateChanged)
    }
}

customElements.define("web-side-nav", SideNav);
var css_248z$2 = "web-snackbar{visibility:hidden}web-snackbar,web-snackbar .web-snackbar__surface{-webkit-box-align:center;-ms-flex-align:center;align-items:center;display:-webkit-box;display:-ms-flexbox;display:flex}web-snackbar .web-snackbar__surface{background-color:#202124;-webkit-box-pack:start;-ms-flex-pack:start;justify-content:flex-start;opacity:0;-webkit-transform:scale(.8);transform:scale(.8);width:100vw}@media (min-width:481px){web-snackbar .web-snackbar__surface{border-radius:3px;-webkit-box-shadow:0 3px 5px -1px rgba(0,0,0,.2),0 6px 10px 0 rgba(0,0,0,.14),0 1px 18px 0 rgba(0,0,0,.12);box-shadow:0 3px 5px -1px rgba(0,0,0,.2),0 6px 10px 0 rgba(0,0,0,.14),0 1px 18px 0 rgba(0,0,0,.12);min-width:344px}}@media (min-width:865px){web-snackbar .web-snackbar__surface{max-width:400px}}web-snackbar .web-snackbar__label{color:hsla(0,0%,100%,.87);font-size:.875rem;padding:14px 16px}web-snackbar .web-snackbar__actions{margin-left:0;margin-right:8px;display:-webkit-box;display:-ms-flexbox;display:flex;-webkit-box-align:center;-ms-flex-align:center;align-items:center}web-snackbar .web-snackbar__action{font-size:.875rem;color:#3fc4ff;height:auto}web-snackbar .web-snackbar__action:active,web-snackbar .web-snackbar__action:focus,web-snackbar .web-snackbar__action:hover{-webkit-box-shadow:none;box-shadow:none}web-snackbar .web-snackbar__action:hover{background:rgba(63,196,255,.08)}web-snackbar .web-snackbar__action:focus{background:rgba(63,196,255,.12)}web-snackbar .web-snackbar__action:active{background:rgba(63,196,255,.16)}web-snackbar .web-snackbar__action+.web-snackbar__action{margin-left:8px}web-snackbar[open]{visibility:visible}web-snackbar[open] .web-snackbar__surface{-webkit-transform:scale(1);transform:scale(1);opacity:1;pointer-events:auto}web-snackbar[open][animatable] .web-snackbar__surface{-webkit-transition:opacity .15s cubic-bezier(0,0,.2,1),-webkit-transform .15s cubic-bezier(0,0,.2,1);transition:opacity .15s cubic-bezier(0,0,.2,1),-webkit-transform .15s cubic-bezier(0,0,.2,1);transition:opacity .15s cubic-bezier(0,0,.2,1),transform .15s cubic-bezier(0,0,.2,1);transition:opacity .15s cubic-bezier(0,0,.2,1),transform .15s cubic-bezier(0,0,.2,1),-webkit-transform .15s cubic-bezier(0,0,.2,1)}web-snackbar[animatable]{visibility:visible}web-snackbar[animatable] .web-snackbar__surface{-webkit-transition:opacity 75ms cubic-bezier(.4,0,1,1),-webkit-transform 75ms cubic-bezier(0,0,.2,1);transition:opacity 75ms cubic-bezier(.4,0,1,1),-webkit-transform 75ms cubic-bezier(0,0,.2,1);transition:opacity 75ms cubic-bezier(.4,0,1,1),transform 75ms cubic-bezier(0,0,.2,1);transition:opacity 75ms cubic-bezier(.4,0,1,1),transform 75ms cubic-bezier(0,0,.2,1),-webkit-transform 75ms cubic-bezier(0,0,.2,1)}web-snackbar[stacked] .web-snackbar__surface{-webkit-box-orient:vertical;-webkit-box-direction:normal;-ms-flex-direction:column;flex-direction:column;-webkit-box-align:start;-ms-flex-align:start;align-items:flex-start}web-snackbar[stacked] .web-snackbar__actions{-ms-flex-item-align:end;align-self:flex-end;margin-bottom:8px}";
styleInject(css_248z$2);
const OPENING_ANIMATION_TIME = 150,
    CLOSING_ANIMATION_TIME = 75;

class Snackbar extends BaseElement {
    static get properties() {
        return {
            animatable: {
                type: Boolean,
                reflect: !0
            },
            stacked: {
                type: Boolean,
                reflect: !0
            },
            type: {
                type: String
            },
            action: {
                type: Object
            }
        }
    }

    get open() {
        return this.hasAttribute("open")
    }

    set open(e) {
        let t;
        Boolean(e) ? (this.setAttribute("open", ""),
            t = 150) : (this.removeAttribute("open"),
            t = 75),
            this.animatable = !0,
            setTimeout(() => this.animatable = !1, t)
    }

    get cookiesTemplate() {
        return html`
      <div class="web-snackbar__label" role="status">
        We serve cookies on this site to analyze traffic, remember your
        preferences, and optimize your experience.
      </div>
      <div class="web-snackbar__actions">
        <a
          href="https://policies.google.com/technologies/cookies"
          class="w-button web-snackbar__action"
          >More details</a
        >
        <button @click=${this.action} class="w-button web-snackbar__action">
          OK
        </button>
      </div>
    `
    }

    render() {
        let e;
        switch (this.type) {
            case "cookies":
                e = this.cookiesTemplate
        }
        return html`
      <div class="web-snackbar__surface">
        ${e}
      </div>
    `
    }
}

customElements.define("web-snackbar", Snackbar);
var css_248z$3 = "web-snackbar-container{bottom:0;display:block;left:0;margin:0;pointer-events:none;position:fixed;right:0;z-index:300}@media (min-width:481px){web-snackbar-container{margin:8px}}";
styleInject(css_248z$3);

class SnackbarContainer extends BaseElement {
    static get properties() {
        return {
            open: {
                type: Boolean
            },
            type: {
                type: String
            }
        }
    }

    constructor() {
        super(),
            this.onBeforeInstallPrompt = this.onBeforeInstallPrompt.bind(this),
            this.onStateChanged = this.onStateChanged.bind(this)
    }

    connectedCallback() {
        super.connectedCallback(),
            checkIfUserAcceptsCookies(),
            store.subscribe(this.onStateChanged),
            this.onStateChanged(),
            window.addEventListener("beforeinstallprompt", this.onBeforeInstallPrompt)
    }

    disconnectedCallback() {
        super.disconnectedCallback(),
            store.unsubscribe(this.onStateChanged),
            window.removeEventListener("beforeinstallprompt", this.onBeforeInstallPrompt)
    }

    onBeforeInstallPrompt(e) {
        this.acceptedCookies || e.preventDefault()
    }

    onStateChanged() {
        const e = store.getState();
        this.open = e.showingSnackbar,
            this.type = e.snackbarType,
            this.acceptedCookies = e.userAcceptsCookies
    }

    render() {
        let e, t;
        switch (this.type) {
            case "cookies":
                e = setUserAcceptsCookies,
                    t = !0
        }
        return html`
      <web-snackbar
        .type=${this.type}
        .open=${this.open}
        .stacked=${t}
        .action="${e}"
      ></web-snackbar>
    `
    }
}

customElements.define("web-snackbar-container", SnackbarContainer);
const debounce = (e, t) => {
    if (!e)
        throw new TypeError("func is a required argument.");
    if (!t)
        throw new TypeError("wait is a required argument.");
    let i;
    return function (...n) {
        i && clearTimeout(i),
            i = setTimeout((function () {
                e(...n)
            }), t)
    }
};
var commonjsGlobal = "undefined" != typeof globalThis ? globalThis : "undefined" != typeof window ? window : "undefined" != typeof global ? global : "undefined" != typeof self ? self : {};

function createCommonjsModule(e, t) {
    return e(t = {
        exports: {}
    }, t.exports),
        t.exports
}

var focusVisible = createCommonjsModule((function (e, t) {
        !function () {
            function e(e) {
                var t = !0,
                    i = !1,
                    n = null,
                    o = {
                        text: !0,
                        search: !0,
                        url: !0,
                        tel: !0,
                        email: !0,
                        password: !0,
                        number: !0,
                        date: !0,
                        month: !0,
                        week: !0,
                        time: !0,
                        datetime: !0,
                        "datetime-local": !0
                    };

                function s(e) {
                    return !!(e && e !== document && "HTML" !== e.nodeName && "BODY" !== e.nodeName && "classList" in e && "contains" in e.classList)
                }

                function a(e) {
                    e.classList.contains("focus-visible") || (e.classList.add("focus-visible"),
                        e.setAttribute("data-focus-visible-added", ""))
                }

                function r(e) {
                    t = !1
                }

                function c() {
                    document.addEventListener("mousemove", d),
                        document.addEventListener("mousedown", d),
                        document.addEventListener("mouseup", d),
                        document.addEventListener("pointermove", d),
                        document.addEventListener("pointerdown", d),
                        document.addEventListener("pointerup", d),
                        document.addEventListener("touchmove", d),
                        document.addEventListener("touchstart", d),
                        document.addEventListener("touchend", d)
                }

                function d(e) {
                    e.target.nodeName && "html" === e.target.nodeName.toLowerCase() || (t = !1,
                        document.removeEventListener("mousemove", d),
                        document.removeEventListener("mousedown", d),
                        document.removeEventListener("mouseup", d),
                        document.removeEventListener("pointermove", d),
                        document.removeEventListener("pointerdown", d),
                        document.removeEventListener("pointerup", d),
                        document.removeEventListener("touchmove", d),
                        document.removeEventListener("touchstart", d),
                        document.removeEventListener("touchend", d))
                }

                document.addEventListener("keydown", (function (i) {
                    i.metaKey || i.altKey || i.ctrlKey || (s(e.activeElement) && a(e.activeElement),
                        t = !0)
                }), !0),
                    document.addEventListener("mousedown", r, !0),
                    document.addEventListener("pointerdown", r, !0),
                    document.addEventListener("touchstart", r, !0),
                    document.addEventListener("visibilitychange", (function (e) {
                        "hidden" == document.visibilityState && (i && (t = !0),
                            c())
                    }), !0),
                    c(),
                    e.addEventListener("focus", (function (e) {
                        var i, n, r;
                        s(e.target) && (t || (i = e.target,
                            n = i.type,
                        "INPUT" == (r = i.tagName) && o[n] && !i.readOnly || "TEXTAREA" == r && !i.readOnly || i.isContentEditable)) && a(e.target)
                    }), !0),
                    e.addEventListener("blur", (function (e) {
                        var t;
                        s(e.target) && (e.target.classList.contains("focus-visible") || e.target.hasAttribute("data-focus-visible-added")) && (i = !0,
                            window.clearTimeout(n),
                            n = window.setTimeout((function () {
                                i = !1,
                                    window.clearTimeout(n)
                            }), 100),
                        (t = e.target).hasAttribute("data-focus-visible-added") && (t.classList.remove("focus-visible"),
                            t.removeAttribute("data-focus-visible-added")))
                    }), !0),
                    e.nodeType === Node.DOCUMENT_FRAGMENT_NODE && e.host ? e.host.setAttribute("data-js-focus-visible", "") : e.nodeType === Node.DOCUMENT_NODE && document.documentElement.classList.add("js-focus-visible")
            }

            if ("undefined" != typeof window && "undefined" != typeof document) {
                var t;
                window.applyFocusVisiblePolyfill = e;
                try {
                    t = new CustomEvent("focus-visible-polyfill-ready")
                } catch (e) {
                    (t = document.createEvent("CustomEvent")).initCustomEvent("focus-visible-polyfill-ready", !1, !1, {})
                }
                window.dispatchEvent(t)
            }
            "undefined" != typeof document && e(document)
        }()
    })),
    css_248z$4 = 'web-search{--web-search-animation-time:0;-webkit-box-align:center;-ms-flex-align:center;align-items:center;display:-webkit-box;display:-ms-flexbox;display:flex;height:100%;-webkit-box-pack:end;-ms-flex-pack:end;justify-content:flex-end;min-width:50%}@media (min-width:865px){web-search{--web-search-animation-time:200ms;-webkit-box-pack:start;-ms-flex-pack:start;justify-content:flex-start}}web-search .web-search__close-btn{display:none;margin-left:8px;text-overflow:clip}web-search .web-search__close-btn,web-search .web-search__open-btn{border-radius:0;border:none;-webkit-box-shadow:none;box-shadow:none;color:#5f6368;cursor:pointer;padding:0 8px;-webkit-transition:none;transition:none;background:transparent}@media (min-width:865px){web-search .web-search__close-btn,web-search .web-search__open-btn{display:none}}web-search .web-search__close-btn:focus,web-search .web-search__close-btn:hover,web-search .web-search__open-btn:focus,web-search .web-search__open-btn:hover{background:transparent}web-search .web-search__close-btn.focus-visible,web-search .web-search__open-btn.focus-visible{-webkit-box-shadow:inset 0 0 0 1px #3fc4ff;box-shadow:inset 0 0 0 1px #3fc4ff;outline:none}web-search .web-search__open-btn:before{content:"search"}web-search .web-search__close-btn:before,web-search .web-search__open-btn:before{-moz-osx-font-smoothing:grayscale;-webkit-font-smoothing:antialiased;font:normal normal normal 24px/1 Material Icons;-webkit-font-feature-settings:"liga";font-feature-settings:"liga";text-rendering:optimizeSpeed;text-transform:none;word-wrap:normal}web-search .web-search__close-btn:before{content:"cancel"}web-search .web-search__input-wrapper{-webkit-box-align:center;-ms-flex-align:center;align-items:center;background:#fff;color:#5f6368;display:none;height:100%;width:100%}@media (min-width:865px){web-search .web-search__input-wrapper{display:-webkit-box;display:-ms-flexbox;display:flex;max-width:calc(200% - 32px);min-width:calc(200% - 32px);-webkit-transform:translateX(calc(50% - 184px));transform:translateX(calc(50% - 184px));-webkit-transition:var(--web-search-animation-time) transform;transition:var(--web-search-animation-time) transform;width:calc(200% - 32px)}}web-search .web-search__input-wrapper:before{-moz-osx-font-smoothing:grayscale;-webkit-font-smoothing:antialiased;font:normal normal normal 24px/1 Material Icons;-webkit-font-feature-settings:"liga";font-feature-settings:"liga";text-rendering:optimizeSpeed;text-transform:none;word-wrap:normal;content:"search";position:absolute;pointer-events:none;padding:8px}web-search .web-search__input{background:#f1f3f4;border:none;padding:8px 8px 8px 40px;width:100%;font:inherit;font-size:16px;line-height:20px;border-top-left-radius:2px;border-top-right-radius:2px}web-search .web-search__input:focus{outline:none;-webkit-box-shadow:0 2px 0 0 #3fc4ff;box-shadow:0 2px 0 0 #3fc4ff;border:none;padding:8px 8px 8px 40px}web-search .web_search__icon{position:absolute}web-search .web-search__icon:before{-moz-osx-font-smoothing:grayscale;-webkit-font-smoothing:antialiased;font:normal normal normal 24px/1 Material Icons;-webkit-font-feature-settings:"liga";font-feature-settings:"liga";text-rendering:optimizeSpeed;text-transform:none;word-wrap:normal;content:"search"}web-search .web-search-popout{background:#fff;border-top:1px solid #ddd;-webkit-box-shadow:0 1px 2px 0 rgba(60,64,67,.3),0 2px 6px 2px rgba(60,64,67,.15);box-shadow:0 1px 2px 0 rgba(60,64,67,.3),0 2px 6px 2px rgba(60,64,67,.15);left:0;padding:16px 0;position:absolute;top:55px;width:calc(100% - 45px)}@media (min-width:865px){web-search .web-search-popout{left:32px;width:calc(100% - 32px)}}web-search .web-search-popout__heading{color:#5f6368;font-size:12px;padding:0 24px}web-search .web-search-popout__list{list-style:none;max-height:50vh;overflow:scroll}web-search .web-search-popout__link{color:#202124;display:block;padding:8px 24px}web-search .web-search-popout__link--active,web-search .web-search-popout__link:hover{background:#e8eaed}web-search[expanded] .web-search__open-btn{display:none}web-search[expanded] .web-search__input-wrapper{display:-webkit-box;display:-ms-flexbox;display:flex}@media (min-width:865px){web-search[expanded] .web-search__input-wrapper{-webkit-transform:translate(calc(-50% + 16px));transform:translate(calc(-50% + 16px))}}web-search[expanded] .web-search__close-btn{display:-webkit-inline-box;display:-ms-inline-flexbox;display:inline-flex;-webkit-box-align:center;-ms-flex-align:center;align-items:center}@media (min-width:865px){web-search[expanded] .web-search__close-btn{display:none}}';
styleInject(css_248z$4);
const applicationID = "2JPAZHQ6K7",
    apiKey = "01ca870a3f1cad9984ed72419a12577c",
    indexName = "webdev",
    client = algoliasearch("2JPAZHQ6K7", apiKey),
    index = client.initIndex("webdev");

class Search extends BaseElement {
    static get properties() {
        return {
            expanded: {
                type: Boolean,
                reflect: !0
            },
            hits: {
                type: Object
            },
            showHits: {
                type: Boolean
            },
            cursor: {
                type: Number
            }
        }
    }

    constructor() {
        super(),
            this.hits = [],
            this.showHits = !1,
            this.cursor = -1,
            this.query = "",
            this.timeout = 0,
            this.onResize = debounce(this.onResize.bind(this), 200),
            this.search = debounce(this.search.bind(this), 200)
    }

    connectedCallback() {
        super.connectedCallback(),
            window.addEventListener("resize", this.onResize),
            this.onResize()
    }

    disconnectedCallback() {
        super.disconnectedCallback(),
            window.removeEventListener("resize", this.onResize)
    }

    render() {
        return html`
      <button
        @click="${this.onOpenSearch}"
        class="web-search__open-btn"
        aria-label="Open search"
      ></button>
      <div
        class="web-search__input-wrapper"
        role="combobox"
        aria-expanded="${this.expanded}"
        aria-owns="web-search-popout__list"
        aria-haspopup="listbox"
      >
        <input
          style="font-size:14px;"
          class="web-search__input"
          type="text"
          role="searchbox"
          autocomplete="off"
          aria-autocomplete="list"
          aria-controls="web-search-popout__list"
          aria-label="搜索"
          id="searchKey"
          placeholder="搜索"
          @keyup="${this.onKeyUp}"
          @input="${this.onInput}"
          @focusin="${this.onFocusIn}"
          @focusout="${this.onFocusOut}"
        />
      </div>
      <button
        @click="${this.onCloseSearch}"
        class="web-search__close-btn"
        aria-label="Close search"
      ></button>
      ${this.hitsTemplate}
    `
    }

    // get hitsTemplate() {
    //     if (!this.showHits)
    //         return html `
    //     <div
    //       id="web-search-popout__list"
    //       role="listbox"
    //       aria-hidden="true"
    //     ></div>
    //   `;
    //     if (!this.hits.length) {
    //         if (!this.query)
    //             return "";
    //         const e = "web.dev " + this.query.trim(),
    //             t = "https://google.com/search?q=" + window.encodeURIComponent(e);
    //         //         return html `
    //         //     <div class="web-search-popout">
    //         //       <div class="web-search-popout__heading">
    //         //         There are no suggestions for your query&mdash;try
    //         //         <a
    //         //           data-category="web.dev"
    //         //           data-label="search, open Google"
    //         //           data-action="click"
    //         //           target="_blank"
    //         //           tabindex="-1"
    //         //           href=${t}
    //         //         >
    //         //           Google search
    //         //         </a>
    //         //       </div>
    //         //     </div>
    //         //   `
    //     }
    //     return html `
    //   <div class="web-search-popout">
    //     <div class="web-search-popout__heading">Pages</div>
    //     <ul
    //       id="web-search-popout__list"
    //       class="web-search-popout__list"
    //       role="listbox"
    //     >
    //       ${this.itemsTemplate}
    //     </ul>
    //   </div>
    // `
    // }
    get itemsTemplate() {
        return this.hits.map((e, t) => html`
        <li class="web-search-popout__item">
          <a
            id="web-search-popout__link--${t}"
            class="web-search-popout__link ${t === this.cursor ? "web-search-popout__link--active" : ""}"
            aria-selected="${t === this.cursor}"
            tabindex="-1"
            href="${e.url}"
            >${e.title}</a
          >
        </li>
      `)
    }

    firstUpdated() {
        this.inputEl = this.renderRoot.querySelector(".web-search__input")
    }

    updated(e) {
        e.has("cursor") && (-1 !== this.cursor ? this.inputEl.setAttribute("aria-activedescendant", `web-search-popout__link--${this.cursor}`) : this.inputEl.removeAttribute("aria-activedescendant"))
    }

    onResize() {
        const e = getComputedStyle(this).getPropertyValue("--web-search-animation-time");
        this.animationTime = parseInt(e, 10)
    }

    onKeyUp(e) {
        switch (e.key) {
            //     case "Home":
            //         return void this.firstHit();
            //     case "End":
            //         return void this.lastHit();
            //     case "Up":
            //     case "ArrowUp":
            //         return void this.prevHit();
            //     case "Down":
            //     case "ArrowDown":
            //         return void this.nextHit();
            case "Enter":
                var searchKey = document.getElementById("searchKey").value;
                window.location.href = '/article?title=' + searchKey.trim();
                break;
            // const e = this.hits[this.cursor];
            // return void (e && this.navigateToHit(e));
            case "Esc":
            case "Escape":
                return void document.activeElement.blur()
        }
    }

    onInput(e) {
        this.search(e.target.value)
    }

    async search(e) {
        if (this.query = e,
        "" !== e)
            try {
                const {
                    hits: t
                } = await index.search(e, {
                    hitsPerPage: 10
                });
                this.query === e && (this.hits = t)
            } catch (e) {
                console.error(e),
                    console.error(e.debugData),
                    trackError("search", e)
            }
        else
            this.hits = []
    }

    firstHit() {
        this.cursor = 0,
            this.scrollHitIntoView()
    }

    lastHit() {
        this.cursor = this.hits.length - 1,
            this.scrollHitIntoView()
    }

    nextHit() {
        this.cursor = (this.cursor + 1) % this.hits.length,
            this.scrollHitIntoView()
    }

    prevHit() {
        -1 === this.cursor ? this.cursor = this.hits.length - 1 : this.cursor = (this.cursor - 1 + this.hits.length) % this.hits.length,
            this.scrollHitIntoView()
    }

    scrollHitIntoView() {
        this.requestUpdate().then(() => {
            this.renderRoot.querySelector(".web-search-popout__link--active").scrollIntoView()
        })
    }

    navigateToHit({
                      url: e
                  }) {
        route(e),
            document.activeElement.blur()
    }

    clear() {
        this.inputEl.value = "",
            this.query = ""
    }

    onOpenSearch() {
        this.expanded = !0,
            store.setState({
                isSearchExpanded: !0
            }),
            this.requestUpdate().then(() => {
                this.inputEl.focus()
            })
    }

    onCloseSearch() {
        this.expanded = !1
    }

    onFocusIn() {
        this.expanded = !0,
            window.addEventListener("scroll", () => {
                document.activeElement.blur()
            }, {
                passive: !0,
                once: !0
            }),
            this.timeout = setTimeout(() => {
                store.setState({
                    isSearchExpanded: !0
                }),
                    this.showHits = !0
            }, this.animationTime)
    }

    onFocusOut(e) {
        const {
            relatedTarget: t
        } = e;
        t && this.contains(t) && t.click(),
            clearTimeout(this.timeout),
            store.setState({
                isSearchExpanded: !1
            }),
            this.expanded = !1,
            this.showHits = !1,
            this.hits = [],
            this.cursor = -1
        // this.clear()
    }
}

customElements.define("web-search", Search);
export {
    BaseElement as B,
    LH_HOST as L,
    BaseStateElement as a,
    requestRunLighthouse as b,
    signIn as c,
    debounce as d,
    closeModal as e,
    openModal as o,
    requestFetchReports as r,
    styleInject as s
};
//# sourceMappingURL=chunk-75d47abd.js.map

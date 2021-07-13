import {
    e as env
} from "/js/common/chunk-ffdac5ae.js";
import {
    h as html,
    r as render
} from "/js/common/lit-ec003724.js";
import {
    B as BaseElement$1,
    s as styleInject,
    o as openModal,
    e as closeModal
} from "/js/common/chunk-75d47abd.js";
import "/js/common/algolia-61d8d050.js";

function isWebShareSupported() {
    if (!("share" in navigator))
        return !1;
    if ("canShare" in navigator) {
        const e = `https://${window.location.hostname}`;
        return navigator.canShare({
            url: e
        })
    }
    return !0
}

class Actions extends BaseElement$1 {
    static get properties() {
        return {
            actions: {
                type: String
            },
            authors: {
                type: String
            },
            webShareSupported: {
                type: Boolean
            }
        }
    }

    constructor() {
        super(),
            this.webShareSupported = isWebShareSupported()
    }

    onWebShare() {
        navigator.share({
            url: this.shareUrl,
            text: this.shareText
        })
    }

    onTwitterShare(e) {
        e.preventDefault(),
            window.open(e.target.href, "share-twitter", "width=550,height=235")
    }

    get shareUrl() {
        return window.location.href
    }

    get shareText() {
        let e = "";
        const t = this._splitPipes(this.authors);
        if (t.length)
            if ("ListFormat" in Intl) {
                e = ` by ${new Intl.ListFormat("en").format(t)}`
            } else
                e = ` by ${t.join(", ")}`;
        return document.title + e
    }

    get shareTemplate() {
        if (this.webShareSupported)
            return html`
        <button
          class="w-actions__fab w-actions__fab--share gc-analytics-event"
          data-category="web.dev"
          data-label="share, web"
          data-action="click"
          @click=${this.onWebShare}
        >
          <span>Share</span>
        </button>
      `;
        const e = new URL("https://twitter.com/share");
        return e.searchParams.set("url", this.shareUrl),
            e.searchParams.set("text", this.shareText),
            html`
      <a
        class="w-actions__fab w-actions__fab--share gc-analytics-event"
        data-category="web.dev"
        data-label="share, twitter"
        data-action="click"
        href="${e}"
        target="_blank"
        rel="noreferrer"
        @click=${this.onTwitterShare}
      >
        <span>Share</span>
      </a>
    `
    }

    get subscribeTemplate() {
        return html`
      <a
        class="w-actions__fab w-actions__fab--subscribe gc-analytics-event"
        data-category="web.dev"
        data-label="subscribe, newsletter"
        data-action="click"
        href="/newsletter/"
      >
        <span>Subscribe</span>
      </a>
    `
    }

    render() {
        const e = this._splitPipes(this.actions),
            t = [];
        return -1 !== e.indexOf("share") && t.push(this.shareTemplate),
        -1 !== e.indexOf("subscribe") && t.push(this.subscribeTemplate),
            html`
      <div class="w-actions">
        ${t}
      </div>
    `
    }

    _splitPipes(e) {
        return e.split(/\|/).map(e => e.trim()).filter(Boolean)
    }
}

customElements.define("web-actions", Actions);
var css_248z = 'web-question{display:-webkit-box;display:-ms-flexbox;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-ms-flex-direction:column;flex-direction:column;-webkit-box-flex:1;-ms-flex:1;flex:1;width:100%}.web-question__content{display:block;-webkit-box-flex:1;-ms-flex:1 1 0px;flex:1 1 0;height:0;padding:0 1.5rem 1.5rem;overflow-y:auto}@media (min-width:481px){.web-question__content{-webkit-box-flex:1;-ms-flex:1 1 auto;flex:1 1 auto;height:25rem}}.web-question__footer{border-top:1px solid #dadce0;display:-webkit-box;display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;-webkit-box-pack:justify;-ms-flex-pack:justify;justify-content:space-between;padding:.625rem}@media (min-width:481px){.web-question__footer{padding:1rem}}.web-question__cta{overflow:visible}.web-question__cta:before{-moz-osx-font-smoothing:grayscale;-webkit-font-smoothing:antialiased;font:normal normal normal 24px/1 Material Icons;-webkit-font-feature-settings:"liga";font-feature-settings:"liga";text-rendering:optimizeSpeed;text-transform:none;word-wrap:normal;-webkit-box-align:center;-ms-flex-align:center;align-items:center;background:#018642;border-radius:50%;color:#fff;content:"check";display:-webkit-box;display:-ms-flexbox;display:flex;font-size:18px;height:24px;-webkit-box-pack:center;-ms-flex-pack:center;justify-content:center;left:calc(-24px - .5rem);opacity:0;pointer-events:none;position:absolute;top:calc(50% - 12px);-webkit-transform:scale(0);transform:scale(0);-webkit-transition:opacity .4s ease,-webkit-transform .2s ease;transition:opacity .4s ease,-webkit-transform .2s ease;transition:opacity .4s ease,transform .2s ease;transition:opacity .4s ease,transform .2s ease,-webkit-transform .2s ease;width:24px}[state=completed] .web-question__cta:before{opacity:1;-webkit-transform:scale(1);transform:scale(1)}';
styleInject(css_248z);

class AssessmentQuestion extends BaseElement$1 {
    static get properties() {
        return {
            id: {
                type: String,
                reflect: !0
            },
            state: {
                type: String,
                reflect: !0
            },
            height: {
                type: String,
                attribute: "question-height"
            }
        }
    }

    constructor() {
        super(),
            this.state = "unanswered",
            this.prerenderedChildren = null,
            this.ctaLabel = "Check",
            this.responseComponentUpdated = this.responseComponentUpdated.bind(this),
            this.reset = this.reset.bind(this)
    }

    render() {
        if (!this.prerenderedChildren) {
            this.prerenderedChildren = [];
            for (const e of this.children)
                this.prerenderedChildren.push(e)
        }
        const e = this.height ? "height: " + this.height + ";" : "";
        return html`
      <div class="web-question__content" style="${e}">
        ${this.prerenderedChildren}
      </div>
      <div class="web-question__footer">
        <span></span>
        <button
          @click="${this.onSubmit}"
          class="w-button w-button--primary web-assessment__button web-question__cta gc-analytics-event"
          data-category="Self-assessments"
          data-label="CTA, ${this.id}"
          ?disabled="${"unanswered" === this.state}"
        >
          ${this.ctaLabel}
        </button>
      </div>
    `
    }

    firstUpdated() {
        this.addEventListener("response-update", this.responseComponentUpdated),
            this.addEventListener("question-option-select", e => {
                const {
                    detail: t,
                    target: s
                } = e;
                let o = -1;
                const i = Array.from(this.querySelectorAll("[data-role=response]"));
                for (let e = 0; e < i.length; ++e)
                    if (i[e].contains(s)) {
                        o = e;
                        break
                    }
                -
                    1 !== o && ga("send", "event", {
                    eventCategory: "Self-assessments",
                    eventAction: "click",
                    eventLabel: `${this.id}-response-${o}-option-${t}`
                })
            })
    }

    responseComponentUpdated() {
        const e = this.querySelectorAll("[data-role=response]"),
            t = Array.from(e).map(({
                                       state: e
                                   }) => e);
        t.includes("unanswered") ? this.state = "unanswered" : t.includes("answeredIncorrectly") ? this.state = "answeredIncorrectly" : this.state = "answeredCorrectly"
    }

    onSubmit(e) {
        switch (this.state) {
            case "answeredCorrectly":
                this.updateResponseComponents(),
                    this.state = "completed",
                    this.ctaLabel = this.checkNextQuestion() ? "Next" : "Reset quiz";
                break;
            case "answeredIncorrectly":
                this.updateResponseComponents(),
                    this.state = "unanswered",
                    this.ctaLabel = "Recheck";
                const e = this.closest("web-tabs"),
                    t = this.closest("web-assessment");
                e ? e.focusTab(e.activeTab) : t && t.focus();
                break;
            case "completed":
                this.checkNextQuestion() ? this.requestNextQuestionNav() : this.requestAssessmentReset()
        }
    }

    updateResponseComponents() {
        const e = this.querySelectorAll("[data-role=response]");
        for (const t of e)
            t.submitResponse()
    }

    checkNextQuestion() {
        const e = this.closest(".web-tabs__panel");
        if (e)
            return e.nextElementSibling
    }

    requestNextQuestionNav() {
        const e = new Event("request-nav-to-next");
        this.dispatchEvent(e)
    }

    requestAssessmentReset() {
        const e = new Event("request-assessment-reset", {
            bubbles: !0
        });
        this.dispatchEvent(e)
    }

    reset() {
        const e = this.querySelectorAll("[data-role=response]"),
            t = this.querySelector(".web-question__content");
        for (const t of e)
            t.reset();
        this.ctaLabel = "Check",
            t.scrollTop = 0
    }
}

customElements.define("web-question", AssessmentQuestion);
var css_248z$1 = '@media (min-width:865px){web-codelab .w-sizer.w-test{display:-webkit-box;display:-ms-flexbox;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-ms-flex-flow:column;flex-flow:column;-webkit-box-pack:justify;-ms-flex-pack:justify;justify-content:space-between}web-codelab .w-sizer.w-test:after{content:"";background:#eb0f00;height:2px}}web-codelab .w-sizer .w-aside{margin:16px}@media (min-width:865px){web-codelab .w-sizer{position:-webkit-sticky;position:sticky;top:81px;height:calc(100vh - 97px)}}web-codelab .web-codelab__headline{padding-top:0}web-codelab .web-codelab__glitch{-webkit-box-flex:1;-ms-flex:1;flex:1;-webkit-box-ordinal-group:0;-ms-flex-order:-1;order:-1}@media (min-width:865px){web-codelab .web-codelab__glitch{-webkit-box-ordinal-group:initial;-ms-flex-order:initial;order:0}}';
styleInject(css_248z$1);

class Codelab extends BaseElement$1 {
    static get properties() {
        return {
            glitch: {
                type: String
            },
            path: {
                type: String
            },
            _isDesktop: {
                type: Boolean
            }
        }
    }

    constructor() {
        super(),
            this.glitch = "",
            this.path = "index.html",
            this._mql = window.matchMedia("(min-width: 865px)"),
            this._toggleDesktop = () => this._isDesktop = this._mql.matches
    }

    connectedCallback() {
        super.connectedCallback(),
            this._mql.addListener(this._toggleDesktop),
            this._toggleDesktop()
    }

    disconnectedCallback() {
        super.connectedCallback(),
            this._mql.removeListener(this._toggleDesktop)
    }

    createRenderRoot() {
        const e = document.createElement("div");
        return e.className = "web-codelab__glitch",
            this.appendChild(e),
            e
    }

    glitchSrc(e) {
        let t = "https://glitch.com/embed/?attributionHidden=true";
        return this.path && (t += `&path=${encodeURI(this.path)}`),
        e && (t += `#!/embed/${encodeURI(this.glitch)}`),
            t
    }

    render() {
        if (!this.glitch)
            return html``;
        const e = "test" === env;
        if (!this._isDesktop || e) {
            return html`
        <div class="w-sizer ${""}">
          <div class="w-aside w-aside--warning">
            <p>
              <strong>Warning:</strong> ${"This Glitch isn't available on small screens"},
              <a target="_blank" rel="noopener" href=${this.glitchSrc(!1)}>
                open it in a new tab.</a
              >
            </p>
          </div>
        </div>
      `
        }
        return html`
      <div class="w-sizer">
        <iframe
          allow="geolocation; microphone; camera; midi; encrypted-media"
          alt="Embedded glitch ${this.glitch}"
          src="${this.glitchSrc(!0)}"
          style="height: 100%; width: 100%; border: 0;"
        >
        </iframe>
      </div>
    `
    }
}

customElements.define("web-codelab", Codelab);
const checkOverflow = (e, t) => {
    if ("height" !== t && "width" !== t)
        throw new TypeError("Can only check overflow for height or width.");
    if (!e)
        throw new TypeError("Can't check overflow on an undefined element.");
    const s = "width" === t ? e.clientWidth : e.clientHeight;
    return ("width" === t ? e.scrollWidth : e.scrollHeight) > s
};
var css_248z$2 = ".web-modal__overflow-hidden{overflow:hidden}@-webkit-keyframes fadein{0%{opacity:0}to{opacity:1}}@keyframes fadein{0%{opacity:0}to{opacity:1}}@-webkit-keyframes scaleup{0%{-webkit-transform:scale(.8);transform:scale(.8)}to{-webkit-transform:scale(1);transform:scale(1)}}@keyframes scaleup{0%{-webkit-transform:scale(.8);transform:scale(.8)}to{-webkit-transform:scale(1);transform:scale(1)}}.web-modal{-webkit-box-align:center;-ms-flex-align:center;align-items:center;background:rgba(0,0,0,.32);bottom:0;-webkit-box-pack:center;-ms-flex-pack:center;justify-content:center;left:0;opacity:0;overflow:auto;padding:1em;position:fixed;right:0;top:0;-webkit-transition:visibility 0s .15s;transition:visibility 0s .15s;visibility:hidden;z-index:275}.web-modal,.web-modal .web-modal__container{display:-webkit-box;display:-ms-flexbox;display:flex}.web-modal .web-modal__container{background:#fff;border-radius:8px;-webkit-box-shadow:0 11px 15px -7px rgba(0,0,0,.2),0 24px 38px 3px rgba(0,0,0,.14),0 9px 46px 8px rgba(0,0,0,.12);box-shadow:0 11px 15px -7px rgba(0,0,0,.2),0 24px 38px 3px rgba(0,0,0,.14),0 9px 46px 8px rgba(0,0,0,.12);-webkit-box-orient:vertical;-webkit-box-direction:normal;-ms-flex-direction:column;flex-direction:column;margin:auto;max-height:100%;max-width:35em;min-height:20em;pointer-events:auto;width:26em}.web-modal .web-modal__header{border-bottom:1px solid transparent;margin:0;padding:1.5rem 1.5rem .5rem}.web-modal .web-modal__content{-webkit-box-flex:1;-ms-flex:1;flex:1;overflow:auto;padding:0 1.5rem}.web-modal .web-modal__footer{border-top:1px solid transparent;display:grid;gap:.5rem;grid-template-columns:1fr auto;justify-items:end;padding:1rem}.web-modal .web-modal__button{height:44px}.web-modal[open]{opacity:1;-webkit-transition:visibility 0s;transition:visibility 0s;visibility:visible}.web-modal[animatable]{animation:fadein 75ms linear reverse}.web-modal[animatable][open]{-webkit-animation:fadein .15s linear;animation:fadein .15s linear}.web-modal[animatable][open] .web-modal__container{-webkit-animation:scaleup .15s cubic-bezier(0,0,.2,1),fadein 75ms linear;animation:scaleup .15s cubic-bezier(0,0,.2,1),fadein 75ms linear}.web-modal[overflow] .web-modal__footer,.web-modal[overflow] .web-modal__header{border-color:#dadce0}";
styleInject(css_248z$2);

class BaseModalElement extends BaseElement$1 {
    static get properties() {
        return {
            open: {
                type: Boolean,
                reflect: !0
            },
            animatable: {
                type: Boolean,
                reflect: !0
            },
            overflow: {
                type: Boolean,
                reflect: !0
            },
            parentModal: {
                attribute: "parent-modal",
                reflect: !0
            }
        }
    }

    constructor() {
        super(),
            this.open_ = !1,
            this.animatable = !1,
            this.inert = !0,
            this.overflow = !1,
            this._triggerElement = null,
            this._parent = null,
            this.onKeyUp = this.onKeyUp.bind(this),
            this.onResize = this.onResize.bind(this),
            this.onAnimationEnd = this.onAnimationEnd.bind(this)
    }

    connectedCallback() {
        super.connectedCallback(),
            this.addEventListener("click", this.onClick),
            this.tabIndex = -1
    }

    disconnectedCallback() {
        super.disconnectedCallback(),
            this.removeEventListener("click", this.onClick),
            window.setTimeout(() => {
                this.isConnected || (this.open = !1)
            }, 0)
    }

    set open(e) {
        if (this.open_ === e)
            return;
        const t = this.open_;
        if (this.open_ = e,
            this.open_)
            this._triggerElement = document.activeElement,
                this.addEventListener("keyup", this.onKeyUp),
                window.addEventListener("resize", this.onResize);
        else {
            const e = new Event("close-modal");
            this.dispatchEvent(e),
                window.removeEventListener("resize", this.onResize)
        }
        this.manageDocument(),
            this.animatable = !0,
            this.addEventListener("animationend", this.onAnimationEnd, {
                once: !0
            }),
            this.requestUpdate("open", t)
    }

    get open() {
        return this.open_
    }

    onClick(e) {
        e.currentTarget === e.target && (this.open = !1)
    }

    onKeyUp(e) {
        "Escape" === e.key && (this.open = !1)
    }

    onAnimationEnd() {
        this.animatable = !1,
            this.manageFocus(),
            this.open ? (this.onResize(),
                window.addEventListener("resize", this.onResize)) : (window.removeEventListener("resize", this.onResize),
                this.removeEventListener("keyup", this.onKeyUp)),
            this.inert = !this.open
    }

    onResize() {
        const e = this.querySelector(".web-modal__content");
        e && (this.overflow = checkOverflow(e, "height"))
    }

    manageDocument() {
        if (this.open) {
            openModal();
            const e = this.closest(this.parentModal);
            e && (e.inert = !0,
                this._parent = e)
        } else
            !this.open && this.parentModal ? parent && (parent.inert = !1,
                this._parent = null) : closeModal()
    }

    manageFocus() {
        this.open ? this.focus() : this._triggerElement ? (this._triggerElement.focus(),
            this._triggerElement = null) : document.body.focus()
    }
}

var css_248z$3 = '.web-assessment__launcher{display:-webkit-box;display:-ms-flexbox;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-ms-flex-direction:column;flex-direction:column;margin:3em 0}.web-assessment__button{height:3rem}web-assessment{display:none;-webkit-box-orient:vertical;-webkit-box-direction:normal;-ms-flex-direction:column;flex-direction:column}web-assessment[animatable]{animation:modal-slideup .25s ease reverse}web-assessment[animatable][open]{-webkit-animation:modal-slideup .25s ease;animation:modal-slideup .25s ease}web-assessment.unresolved *{display:none!important}@media (min-width:481px){web-assessment{display:-webkit-box;display:-ms-flexbox;display:flex;margin:5em 0!important;padding:0!important}}@-webkit-keyframes modal-slideup{0%{-webkit-transform:translateY(40%);transform:translateY(40%);opacity:0}to{-webkit-transform:none;transform:none;opacity:1}}@keyframes modal-slideup{0%{-webkit-transform:translateY(40%);transform:translateY(40%);opacity:0}to{-webkit-transform:none;transform:none;opacity:1}}.web-assessment__open,.web-assessment__open:active,.web-assessment__open:focus,.web-assessment__open:hover{background:#018642}.web-assessment__close{position:absolute;right:.5rem;top:.75rem}.web-assessment__lockup{color:#018642}.web-assessment__lockup:before{content:"school"}.web-assessment--singleton .web-assessment__header{border-bottom:1px solid #dadce0}.web-assessment__content{display:-webkit-box;display:-ms-flexbox;display:flex;-webkit-box-flex:1;-ms-flex:1;flex:1}web-assessment[animatable],web-assessment[open]{background:#fff;bottom:0;display:-webkit-box;display:-ms-flexbox;display:flex;left:0;margin:0;position:fixed;right:0;top:0;z-index:250}web-assessment[animatable] .web-assessment__header,web-assessment[open] .web-assessment__header{padding:1.5rem 3.5rem 1rem 1.5rem}web-assessment[animatable] .web-assessment__set-leader,web-assessment[open] .web-assessment__set-leader{display:none}@media (min-width:481px){.web-assessment__close,.web-assessment__launcher{display:none}.web-assessment__header{padding:1.5rem 1.5rem 1rem}}';
styleInject(css_248z$3);

class Assessment extends BaseModalElement {
    static get properties() {
        return {
            modal: {
                attribute: "aria-modal",
                reflect: !0
            }
        }
    }

    constructor() {
        super(),
            this.modal = !1,
            this._placeholder = null,
            this.breakpoint_ = matchMedia("(min-width: 481px)"),
            this.onAssessmentAnimationEnd = this.onAssessmentAnimationEnd.bind(this),
            this.onAssessmentResize = this.onAssessmentResize.bind(this),
            this.reset = this.reset.bind(this),
            this.onOpenClick = this.onOpenClick.bind(this),
            this.openAssessment = this.openAssessment.bind(this),
            this.closeAssessment = this.closeAssessment.bind(this)
    }

    render() {
        if (!this.prerenderedChildren) {
            this.prerenderedChildren = [],
                this.setLeader = [];
            for (const e of this.children)
                e.classList.contains("web-assessment__set-leader") ? this.setLeader.push(e) : this.prerenderedChildren.push(e)
        }
        return html`
      <div class="w-callout__header web-assessment__header">
        <h2 class="w-callout__lockup web-assessment__lockup">
          Check your understanding
        </h2>
        ${this.setLeader}
        <button
          @click="${this.onCloseClick}"
          class="w-button--icon w-button--round web-assessment__close"
          data-icon="close"
        >
          <span role="tooltip" class="w-tooltip">
            Close
          </span>
        </button>
      </div>
      ${this.prerenderedChildren}
    `
    }

    firstUpdated() {
        this.classList.remove("unresolved"),
            this.inert = !1,
            this.renderLauncher(),
            this.addEventListener("request-assessment-reset", this.reset);
        const e = [...document.querySelectorAll("web-assessment")].indexOf(this);
        this.id = "web-assessment-" + e,
            Array.from(this.querySelectorAll("web-question")).forEach((e, t) => {
                e.setAttribute("id", `${this.id}-question-${t}`)
            })
    }

    addUniqueID(e, t) {
        const s = [...e].indexOf(t);
        "undefined" === t.id && (t.id = s)
    }

    connectedCallback() {
        super.connectedCallback(),
            this.breakpoint_.addListener(this.onAssessmentResize)
    }

    disconnectedCallback() {
        super.disconnectedCallback(),
            this.breakpoint_.removeListener(this.onAssessmentResize)
    }

    renderLauncher() {
        const e = document.createElement("div"),
            t = (e => html`
      <div class="w-callout__header web-assessment__header">
        <h2 class="w-callout__lockup web-assessment__lockup">
          Check your understanding
        </h2>
        <div class="w-callout__blurb web-assessment__set-leader">
          ${e}
        </div>
      </div>
      <button
        @click="${this.onOpenClick}"
        class="w-button w-button--primary web-assessment__button web-assessment__open"
      >
        Open quiz
      </button>
    `)(this.setLeader[0] ? this.setLeader[0].textContent : "");
        render(t, e),
            e.className = "web-assessment__launcher",
            this.before(e)
    }

    onOpenClick() {
        this.open = !0
    }

    onCloseClick() {
        this.open = !1
    }

    updated(e) {
        e.has("open") && (this.modal = this.open,
            this.open ? this.openAssessment() : this.addEventListener("animationend", this.closeAssessment, {
                once: !0
            }))
    }

    openAssessment() {
        this._placeholder = this.previousElementSibling,
            this.setAttribute("role", "dialog"),
            this.addEventListener("animationend", this.onAssessmentAnimationEnd, {
                once: !0
            }),
            document.body.append(this)
    }

    closeAssessment() {
        this.inert = !1,
        this._placeholder && (this._placeholder.after(this),
            this._placeholder = null)
    }

    onAssessmentAnimationEnd() {
        const e = this.querySelector("web-tabs");
        e && (e.onResize(),
            e.focusTab(e.activeTab))
    }

    onAssessmentResize() {
        this.open = !1,
            this.removeAttribute("role")
    }

    reset() {
        const e = this.querySelector("web-tabs"),
            t = this.querySelectorAll("web-question");
        for (const e of t)
            e.reset();
        e && e.focusTab(0)
    }
}

customElements.define("web-assessment", Assessment);
var css_248z$4 = "[data-role=option][data-submitted],[state=completed] [data-role=option]{background:#f1f3f4;border-radius:3px;cursor:auto}[data-role=option][data-correct][data-submitted]{background:rgba(1,134,66,.08);-webkit-box-shadow:inset 0 0 0 1px #018642;box-shadow:inset 0 0 0 1px #018642}.web-response__correctness-flag{color:#5f6368;display:none;padding:.1875rem 0 .5rem}[data-role=option][data-submitted] .web-response__correctness-flag,[state=completed] .web-response__correctness-flag{display:block}[data-role=option][data-submitted] .web-response__correctness-flag{color:#e51661}[data-role=option][data-submitted][data-correct] .web-response__correctness-flag{color:#018642}.web-response__option-rationale{color:#5f6368;display:none;font-size:.833em;padding:.5rem 0 0}[data-submitted] .web-response__option-rationale,[state=completed] .web-response__option-rationale{display:block}.web-response__option-rationale>:first-child{margin-top:0}.web-response__option-rationale>:last-child{margin-bottom:0}.web-response__option-rationale p{font-size:1em;line-height:1.5}.web-response__option-rationale ol>li,.web-response__option-rationale ul>li{font-size:1em}.web-response__option-rationale ol>li:before{margin-top:.25em}";
styleInject(css_248z$4);

class BaseResponseElement extends BaseElement$1 {
    static get properties() {
        return {
            state: {
                type: String,
                reflect: !0
            },
            correctAnswer: {
                attribute: "correct-answer",
                type: String
            }
        }
    }

    constructor() {
        super(),
            this.state = "unanswered",
            this.enforceCardinality = this.enforceCardinality.bind(this),
            this.submitResponse = this.submitResponse.bind(this),
            this.reset = this.reset.bind(this)
    }

    static getSelectionRange(e) {
        let t = 1,
            s = null;
        return "1" === e || (/^\d+$/.test(e) ? (t = parseInt(e),
            s = t) : /^\d+\+$/.test(e) ? (t = parseInt(e),
            s = 0) : /^\d-\d+$/.test(e) && ([t, s] = e.split("-"),
            [t, s] = [parseInt(t), parseInt(s)])), {
            min: t,
            max: s
        }
    }

    firstUpdated() {
        this.identifyCorrectOptions()
    }

    updated() {
        this.reportUpdate()
    }

    reportUpdate() {
        const e = new CustomEvent("response-update", {
            bubbles: !0,
            detail: {
                responseState: this.state
            }
        });
        this.dispatchEvent(e)
    }

    identifyCorrectOptions() {
        if (!this.correctAnswer)
            return;
        const e = this.correctAnswer.split(",").map(Number),
            t = this.querySelectorAll("[data-role=option]");
        for (let s = 0; s < t.length; s++)
            e.includes(s) && t[s].setAttribute("data-correct", "")
    }

    enforceCardinality(e) {
        const t = this.querySelectorAll("[data-role=option]");
        let s = 0;
        for (const e of t)
            e.hasAttribute("data-selected") && s++;
        const o = this.checkIfCorrect();
        if (s >= this.minSelections && o ? this.state = "answeredCorrectly" : s >= this.minSelections && !o ? this.state = "answeredIncorrectly" : this.state = "unanswered",
        0 !== this.maxSelections && null !== this.maxSelections)
            for (const e of t) {
                const t = e.hasAttribute("data-selected"),
                    o = e.hasAttribute("data-submitted");
                s < this.maxSelections && !t && !o ? this.enableOption(e) : t || o || this.disableOption(e)
            }
    }

    checkIfCorrect() {
        const e = this.correctAnswer.split(",").map(Number),
            t = this.querySelectorAll("[data-role=option]"),
            s = [];
        return t.forEach((e, t) => {
            e.hasAttribute("data-selected") && s.push(t)
        }),
            e.every(e => s.includes(e))
    }

    submitResponse() {
        const e = this.querySelectorAll("[data-role=option]");
        for (const t of e) {
            const e = t.hasAttribute("data-selected"),
                s = t.hasAttribute("data-correct"),
                o = t.hasAttribute("data-submitted");
            "answeredIncorrectly" === this.state ? e && s ? (t.setAttribute("data-submitted", ""),
                this.disableOption(t)) : e && !s ? (t.setAttribute("data-submitted", ""),
                this.disableOption(t),
                this.deselectOption(t)) : e || o || this.enableOption(t) : "answeredCorrectly" === this.state && (this.disableOption(t),
            e && t.setAttribute("data-submitted", ""))
        }
        "answeredIncorrectly" === this.state && (this.state = "unanswered")
    }

    reset() {
        const e = this.querySelectorAll("[data-role=option]");
        this.state = "unanswered";
        for (const t of e)
            t.removeAttribute("data-submitted"),
            "function" == typeof this.deselectOption && this.deselectOption(t),
                this.enableOption(t)
    }

    disableOption(e) {
        const t = e.querySelectorAll("input, button");
        e.setAttribute("disabled", "");
        for (const e of t)
            e.disabled = !0
    }

    enableOption(e) {
        const t = e.querySelectorAll("input, button");
        e.removeAttribute("disabled");
        for (const e of t)
            e.disabled = !1
    }
}

var css_248z$5 = 'web-response-mc .web-response-mc__selector,web-response-mc input[type=radio]~.web-response-mc__selector:after{border-color:#018642}@media (hover:hover){web-response-mc .web-response-mc__input:focus~.web-response-mc__selector:before,web-response-mc .web-response-mc__option:hover .web-response-mc__selector:before{background:#e8f5e9}}web-response-mc .web-response-mc__option:active .web-response-mc__selector:before{background:#c8e6c9}web-response-mc input[type=checkbox]:checked~.web-response-mc__selector{background:#018642}.web-response-mc__option[data-submitted],[state=completed] .web-response-mc__option{-webkit-box-align:start;-ms-flex-align:start;align-items:flex-start;justify-self:stretch;padding:1rem 1rem 1.5rem}[state=completed] .web-response-mc__input[disabled]~.web-response-mc__selector{background:#80868b;border-color:#80868b;-webkit-transition:none;transition:none}.web-select-group__option[data-submitted] .web-response-mc__input~.web-response-mc__selector{background:#e51661!important;border-color:#e51661!important;-webkit-transition:none;transition:none}.web-select-group__option[data-submitted][data-correct] .web-response-mc__input~.web-response-mc__selector{background:#018642!important;border-color:#018642!important;-webkit-transition:none;transition:none}.web-select-group__option[data-submitted] .web-response-mc__selector:before,[state=completed] .web-response-mc__selector:before{background:none!important}.web-select-group__option[data-submitted] .web-response-mc__input~.web-response-mc__selector:after,[state=completed] .web-response-mc__input~.web-response-mc__selector:after{border:0;content:"remove";height:auto;-webkit-transform:initial;transform:none;-webkit-transition:none;transition:none;width:auto}.web-select-group__option[data-submitted] .web-response-mc__input~.web-response-mc__selector:after{content:"clear"}.web-select-group__option[data-submitted][data-correct] .web-response-mc__input~.web-response-mc__selector:after{content:"check"}.web-select-group__option[data-submitted][disabled],[state=completed] .web-select-group__option[disabled]{pointer-events:auto}.web-select-group__option[data-submitted][disabled] .web-select-group__option-content,[state=completed] .web-select-group__option[disabled] .web-select-group__option-content{opacity:1}';
styleInject(css_248z$5);
const generateIdSalt = e => {
    const t = Math.random().toString(36).substr(2, 9);
    return document.getElementById(e + t) ? BaseElement.generateIdSalt(e) : t
};

class ResponseMultipleChoice extends BaseResponseElement {
    static get properties() {
        return {
            id: {
                type: String,
                reflect: !0
            },
            cardinality: {
                type: String
            },
            columns: {
                type: Boolean
            }
        }
    }

    constructor() {
        super(),
            this.prerenderedChildren = null,
            this.options = null,
            this.optionContents = null,
            this.rationales = null,
            this.minSelections = null,
            this.maxSelections = null,
            this.selectType = null,
            this.onOptionInput = this.onOptionInput.bind(this),
            this.deselectOption = this.deselectOption.bind(this),
            this.updateSelections = this.updateSelections.bind(this),
            this._formName = generateIdSalt("web-response-mc-form-")
    }

    render() {
        const e = this.correctAnswer.split(",").map(Number);
        this.selectType = "1" === this.cardinality ? "radio" : "checkbox";
        const t = BaseResponseElement.getSelectionRange(this.cardinality);
        if (this.minSelections = t.min,
            this.maxSelections = t.max,
            !this.prerenderedChildren) {
            this.prerenderedChildren = [],
                this.options = [],
                this.optionContents = [],
                this.rationales = [];
            for (const e of this.children) {
                switch (e.getAttribute("data-role")) {
                    case "option":
                        this.optionContents.push(e);
                        break;
                    case "rationale":
                        this.rationales.push(e);
                        break;
                    default:
                        this.prerenderedChildren.push(e)
                }
            }
            for (let t = 0; t < this.optionContents.length; t++) {
                const s = e.includes(t);
                this.options.push(this.optionTemplate(this.optionContents[t], this.rationales[t], s))
            }
        }
        return html`
      ${this.prerenderedChildren}
      <fieldset
        class="web-select-group web-response-mc"
        ?columns="${this.columns}"
      >
        <div class="web-select-group__options-wrapper">
          ${this.options.map((e, t) => html`
                <label
                  class="web-select-group__option web-response-mc__option"
                  data-role="option"
                >
                  <input
                    @input=${this.onOptionInput}
                    @click=${this.onOptionClick}
                    class="web-select-group__input web-response-mc__input gc-analytics-event"
                    type="${this.selectType}"
                    name="web-response-mc-form-${this._formName}"
                    value="${t}"
                  />
                  <span
                    class="web-select-group__selector web-response-mc__selector"
                  ></span>
                  <span class="web-select-group__option-content">
                    ${e}
                  </span>
                </label>
              `)}
        </div>
      </fieldset>
    `
    }

    optionTemplate(e, t, s) {
        const o = document.createElement("div");
        return o.className = "web-response__correctness-flag",
            o.textContent = s ? "Correct" : "Incorrect",
            e.prepend(o),
            t.className = "web-response__option-rationale",
            e.append(t),
            e.removeAttribute("data-role"),
            e
    }

    firstUpdated() {
        super.firstUpdated()
    }

    onOptionInput(e) {
        this.updateSelections(e),
            this.enforceCardinality(e)
    }

    onOptionClick(e) {
        const {
            target: t
        } = e, s = Number(t.value), o = new CustomEvent("question-option-select", {
            detail: s,
            bubbles: !0
        });
        this.dispatchEvent(o)
    }

    updateSelections(e) {
        const t = this.querySelectorAll("[data-role=option]"),
            s = e.target.closest("[data-role=option]");
        if (e.target.checked) {
            if ("1" === this.cardinality)
                for (const e of t)
                    e.removeAttribute("data-selected");
            s.setAttribute("data-selected", "")
        } else
            s.removeAttribute("data-selected")
    }

    deselectOption(e) {
        e.removeAttribute("data-selected"),
            e.querySelector("input").checked = !1
    }
}

customElements.define("web-response-mc", ResponseMultipleChoice);
var css_248z$6 = "[state=completed] .web-response-tac__option-rationale{font:400 12px/1.5 Roboto,sans-serif;background:rgba(1,134,66,.08);border-radius:3px;color:#202124;display:block;line-height:1.778!important;padding:1.125rem}@media (min-width:241px){[state=completed] .web-response-tac__option-rationale{font:400 14px/1.5 Roboto,sans-serif}}@media (min-width:321px){[state=completed] .web-response-tac__option-rationale{font:400 16px/1.5 Roboto,sans-serif}}";
styleInject(css_248z$6);

class ResponseThinkAndCheck extends BaseResponseElement {
    constructor() {
        super(),
            this.prerenderedChildren = null,
            this.option = null,
            this.reset = this.reset.bind(this)
    }

    render() {
        if (!this.prerenderedChildren) {
            this.prerenderedChildren = [],
                this.option = [];
            for (const e of this.children)
                "rationale" === e.getAttribute("data-role") ? (e.setAttribute("data-role", "option"),
                    e.className = "web-response__option-rationale web-response-tac__option-rationale",
                    this.option.push(e)) : this.prerenderedChildren.push(e)
        }
        return html`
      ${this.prerenderedChildren} ${this.option}
    `
    }

    connectedCallback() {
        super.connectedCallback(),
            this.state = "answeredCorrectly"
    }

    reset() {
        super.reset(),
            this.state = "answeredCorrectly"
    }
}

customElements.define("web-response-tac", ResponseThinkAndCheck);
var css_248z$7 = '.web-select-group{border:0;margin:1.5rem 0;padding:0}.web-select-group__options-wrapper{display:grid;gap:1em;justify-items:start}[columns]>fieldset>.web-select-group__options-wrapper{grid-template-columns:repeat(auto-fit,minmax(30ch,1fr));justify-items:stretch}.web-select-group__option{font:400 13.5px/1.55556 Roboto,sans-serif;margin-top:27px;margin-bottom:27px;-webkit-tap-highlight-color:transparent;-webkit-box-align:center;-ms-flex-align:center;align-items:center;cursor:pointer;display:-webkit-box;display:-ms-flexbox;display:flex;line-height:1.5!important;margin:0!important;padding:0 1rem;position:relative;z-index:1}@media (min-width:241px){.web-select-group__option{font:400 15px/1.86667 Roboto,sans-serif;margin-top:31.5px;margin-bottom:31.5px}}@media (min-width:321px){.web-select-group__option{font:400 18px/1.77778 Roboto,sans-serif;margin-top:36px;margin-bottom:36px}}.w-selectgroup__option--standalone{-webkit-box-pack:center;-ms-flex-pack:center;justify-content:center}.web-select-group__option[disabled]{pointer-events:none}.web-select-group__option-content{-webkit-transition:opacity .12s cubic-bezier(0,0,.2,1);transition:opacity .12s cubic-bezier(0,0,.2,1)}.web-select-group__option[disabled] .web-select-group__option-content{opacity:.5}.web-select-group__input{cursor:inherit;left:0;margin:0;opacity:0;padding:0;position:absolute;top:0}.web-select-group__selector{-webkit-box-align:center;-ms-flex-align:center;align-items:center;border:2px solid #3740ff;border-radius:2px;display:-webkit-inline-box;display:-ms-inline-flexbox;display:inline-flex;-webkit-box-flex:0;-ms-flex:0 0 auto;flex:0 0 auto;height:18px;-webkit-box-pack:center;-ms-flex-pack:center;justify-content:center;margin:.4375rem 1.5625rem .4375rem .3125rem;position:relative;-webkit-transition:border .12s cubic-bezier(0,0,.2,1),background .12s cubic-bezier(0,0,.2,1);transition:border .12s cubic-bezier(0,0,.2,1),background .12s cubic-bezier(0,0,.2,1);width:18px}.w-selectgroup__option--standalone~.w-selectgroup__selector{margin:.4375rem}input[type=radio]~.web-select-group__selector{border-radius:50%;height:20px;margin:.375rem 1.5rem .375rem .25rem;width:20px}.w-selectgroup__option--standalone input[type=radio]~.w-selectgroup__selector{margin:.375rem}.web-select-group__selector:before{background:none;border:1px solid transparent;border-radius:50%;content:"";display:block;height:40px;left:50%;position:absolute;top:50%;-webkit-transform:translate(-50%,-50%);transform:translate(-50%,-50%);-webkit-transition:background .12s cubic-bezier(0,0,.2,1),border .12s cubic-bezier(0,0,.2,1);transition:background .12s cubic-bezier(0,0,.2,1),border .12s cubic-bezier(0,0,.2,1);width:40px;z-index:-1}.web-select-group__selector:after{-moz-osx-font-smoothing:grayscale;-webkit-font-smoothing:antialiased;font:normal normal normal 24px/1 Material Icons;-webkit-font-feature-settings:"liga";font-feature-settings:"liga";text-rendering:optimizeSpeed;text-transform:none;word-wrap:normal;color:#fff;content:"";display:block;font-size:18px}input[type=radio]~.web-select-group__selector:after{border:5px solid #3740ff;border-radius:50%;height:0;-webkit-transform:scale(0);transform:scale(0);-webkit-transition:-webkit-transform .12s cubic-bezier(0,0,.2,1);transition:-webkit-transform .12s cubic-bezier(0,0,.2,1);transition:transform .12s cubic-bezier(0,0,.2,1);transition:transform .12s cubic-bezier(0,0,.2,1),-webkit-transform .12s cubic-bezier(0,0,.2,1);width:0}@media (hover:hover){.web-select-group__input:focus~.web-select-group__selector:before,.web-select-group__option:hover .web-select-group__selector:before{background:rgba(55,64,255,.11)}}.web-select-group__option:active .web-select-group__selector:before{background:rgba(55,64,255,.26)}.web-select-group__input:focus~.web-select-group__selector:before{border-color:#3740ff}.js-focus-visible .web-select-group__input:focus:not(.focus-visible)~.web-select-group__selector:before{border-color:transparent}input[type=radio]:checked~.web-select-group__selector:after{-webkit-transform:scale(1);transform:scale(1)}input[type=checkbox]:checked~.web-select-group__selector{background:#3740ff}input[type=checkbox]:checked~.web-select-group__selector:after{content:"check"}.web-select-group__input[disabled]~.web-select-group__selector{border-color:#bdc1c6}input[type=checkbox][disabled]:checked~.web-select-group__selector{background:#bdc1c6}.web-select-group__input[disabled]~.web-select-group__selector:after{border-color:#bdc1c6}';
styleInject(css_248z$7);

class SelectGroup extends BaseElement$1 {
    static get properties() {
        return {
            type: {
                type: String
            },
            prefix: {
                type: String
            },
            columns: {
                type: Boolean
            }
        }
    }

    constructor() {
        super(),
            this.idSalt = generateIdSalt("web-select-group-"),
            this.selectors = null
    }

    render() {
        if (this.prefix || (this.prefix = ""),
            !this.selectors) {
            this.selectors = [];
            for (let e = 0; e < this.children.length; e++)
                this.selectors.push(this.selectorTemplate(e, this.children[e], this.type, this.prefix))
        }
        return html`
      <fieldset
        class="web-select-group ${this.prefix}"
        ?columns="${this.columns}"
      >
        <div class="web-select-group__options-wrapper">
          ${this.selectors}
        </div>
      </fieldset>
    `
    }

    selectorTemplate(e, t, s, o) {
        let i = "",
            n = "",
            a = "";
        return o && (i = o + "__option",
            n = o + "__input",
            a = o + "__selector"),
            html`
      <label
        class="web-select-group__option ${i}"
        data-category="Site-Wide Custom Events"
        data-label="${s}, web-select-group-${this.idSalt}-${e}"
      >
        <input
          @change="${this.onChange}"
          class="web-select-group__input ${n}"
          type="${s}"
          name="web-select-group-${this.idSalt}"
          value="${e}"
        />
        <span class="web-select-group__selector ${a}"></span>
        <span class="web-select-group__option-content">
          ${t}
        </span>
      </label>
    `
    }

    onChange() {
        this.reportSelections()
    }

    reportSelections() {
        const e = this.querySelectorAll("input");
        let t = 0;
        for (const s of e)
            s.checked && t++;
        const s = new CustomEvent("change-selections", {
            detail: {
                numSelections: t
            }
        });
        this.dispatchEvent(s)
    }
}

customElements.define("web-select-group", SelectGroup);
var css_248z$8 = 'web-tabs{-webkit-box-flex:1;-ms-flex:1;flex:1;-webkit-box-orient:vertical;-webkit-box-direction:normal;-ms-flex-direction:column;flex-direction:column}web-tabs,web-tabs .web-tabs__tablist{display:-webkit-box;display:-ms-flexbox;display:flex}web-tabs .web-tabs__tablist{border-bottom:1px solid #dadce0;overflow-x:auto;scroll-behavior:smooth;width:100%;scrollbar-color:#80868b transparent;scrollbar-width:thin}web-tabs .web-tabs__tablist::-webkit-scrollbar{height:10px;width:10px}web-tabs .web-tabs__tablist::-webkit-scrollbar-thumb{background:#80868b;background-clip:padding-box;border:2.5px solid transparent;border-radius:5px}web-tabs .web-tabs__tablist::-webkit-scrollbar-thumb:hover{background-color:#62676c}web-tabs .web-tabs__tablist::-webkit-scrollbar-thumb:active{background-color:#4a4e51}web-tabs .web-tabs__tablist::-webkit-scrollbar-track{background:transparent}web-tabs[overflow] .web-tabs__tablist{padding-left:3rem}web-tabs .web-tabs__tab{-webkit-tap-highlight-color:transparent;-webkit-box-align:center;-ms-flex-align:center;align-items:center;background:transparent;border:0;border-radius:3px;color:#3740ff;cursor:pointer;display:-webkit-inline-box;display:-ms-inline-flexbox;display:inline-flex;font:500 .875rem/2.25rem Roboto,sans-serif;height:56px;-webkit-box-pack:center;-ms-flex-pack:center;justify-content:center;letter-spacing:1px;outline:0;padding:0 16px;position:relative;text-decoration:none;text-transform:uppercase;-webkit-transition:background-color .2s,-webkit-box-shadow .2s;transition:background-color .2s,-webkit-box-shadow .2s;transition:background-color .2s,box-shadow .2s;transition:background-color .2s,box-shadow .2s,-webkit-box-shadow .2s;-moz-osx-font-smoothing:grayscale;-webkit-font-smoothing:antialiased;text-rendering:optimizeSpeed;border-radius:0;color:#000;-webkit-box-flex:1;-ms-flex:1 0 auto;flex:1 0 auto;height:3rem;margin:0;min-width:-webkit-fit-content;min-width:-moz-fit-content;min-width:fit-content;-webkit-transition:background .2s,color .2s,-webkit-box-shadow .2s;transition:background .2s,color .2s,-webkit-box-shadow .2s;transition:background .2s,box-shadow .2s,color .2s;transition:background .2s,box-shadow .2s,color .2s,-webkit-box-shadow .2s}@media (hover:hover){web-tabs .web-tabs__tab:focus,web-tabs .web-tabs__tab:hover{background:#e8f5e9}}web-tabs .web-tabs__tab:focus{-webkit-box-shadow:inset 0 0 0 1px #3740ff;box-shadow:inset 0 0 0 1px #3740ff;outline:0}web-tabs .web-tabs__tab:active{background:#c8e6c9;-webkit-box-shadow:none;box-shadow:none}web-tabs .web-tabs__tab[aria-selected=true]{color:#018642}web-tabs .web-tabs__tab:after{background:transparent;bottom:0;content:"";display:block;height:2px;left:0;position:absolute;right:0;-webkit-transition:background .2s;transition:background .2s}web-tabs .web-tabs__tab[aria-selected=true]:after{background:#018642}web-tabs .web-tabs__text-label{opacity:.6;-webkit-transition:opacity .2s;transition:opacity .2s}web-tabs .web-tabs__tab[aria-selected=true] .web-tabs__text-label{opacity:1}web-tabs .web-tabs__panel{display:-webkit-box;display:-ms-flexbox;display:flex;-webkit-box-flex:1;-ms-flex:1;flex:1;-webkit-box-orient:vertical;-webkit-box-direction:normal;-ms-flex-direction:column;flex-direction:column}web-tabs.unresolved *{display:none!important}.js-focus-visible .web-tabs__tab:focus:not(.focus-visible){-webkit-box-shadow:none;box-shadow:none}';
styleInject(css_248z$8);

class Tabs extends BaseElement$1 {
    static get properties() {
        return {
            label: {
                type: String
            },
            activeTab: {
                type: Number,
                reflect: !0
            },
            overflow: {
                type: Boolean,
                reflect: !0
            }
        }
    }

    constructor() {
        super(),
            this.activeTab = 0,
            this.overflow = !1,
            this.prerenderedChildren = null,
            this.tabs = null,
            this.idSalt = generateIdSalt("web-tab-"),
            this.onResize = this.onResize.bind(this),
            this._changeTab = this._changeTab.bind(this),
            this.focusTab = this.focusTab.bind(this),
            this.previousTab = this.previousTab.bind(this),
            this.nextTab = this.nextTab.bind(this),
            this.firstTab = this.firstTab.bind(this),
            this.lastTab = this.lastTab.bind(this)
    }

    render() {
        if (!this.prerenderedChildren) {
            this.prerenderedChildren = [],
                this.tabs = [];
            let e = 1;
            for (const t of this.children) {
                this.prerenderedChildren.push(this.panelTemplate(e, t));
                const s = t.getAttribute("data-label");
                this.tabs.push(this.tabTemplate(e, s)),
                    e++
            }
        }
        return html`
      <div class="web-tabs__tablist" role="tablist" aria-label="${this.label}">
        ${this.tabs}
      </div>
      ${this.prerenderedChildren}
    `
    }

    tabTemplate(e, t) {
        switch (t) {
            case "question":
                t = "Question " + e;
                break;
            case "sample":
                t = "Sample " + e;
                break;
            case "":
            case null:
            case "bare":
                t = e
        }
        return html`
      <button
        @click=${this.onFocus}
        @focus=${this.onFocus}
        @keydown=${this.onKeydown}
        class="web-tabs__tab gc-analytics-event"
        role="tab"
        aria-selected="false"
        id="web-tab-${this.idSalt}-${e}"
        aria-controls="web-tab-${this.idSalt}-${e}-panel"
        tabindex="-1"
        data-category="Site-Wide Custom Events"
        data-label="tab, ${t}"
      >
        <span class="web-tabs__text-label">${t}</span>
      </button>
    `
    }

    panelTemplate(e, t) {
        return html`
      <div
        id="web-tab-${this.idSalt}-${e}-panel"
        class="web-tabs__panel"
        role="tabpanel"
        aria-labelledby="web-tab-${this.idSalt}-${e}"
        hidden
      >
        ${t}
      </div>
    `
    }

    firstUpdated() {
        this.activeTab = 0,
            this.classList.remove("unresolved"),
            this.onResize();
        const e = this.querySelectorAll("web-question");
        if (e)
            for (const t of e)
                t.addEventListener("request-nav-to-next", this.nextTab)
    }

    connectedCallback() {
        super.connectedCallback(),
            window.addEventListener("resize", this.onResize)
    }

    disconnectedCallback() {
        super.disconnectedCallback(),
            window.removeEventListener("resize", this.onResize)
    }

    updated(e) {
        e.has("activeTab") && this._changeTab()
    }

    _changeTab() {
        const e = this.querySelectorAll(".web-tabs__tab"),
            t = this.querySelectorAll(".web-tabs__panel"),
            s = e[this.activeTab],
            o = t[this.activeTab];
        if (s) {
            for (const t of e)
                t.setAttribute("aria-selected", "false"),
                    t.setAttribute("tabindex", "-1");
            s.setAttribute("aria-selected", "true"),
                s.removeAttribute("tabindex")
        }
        if (o) {
            for (const e of t)
                e.hidden = !0;
            o.hidden = !1
        }
    }

    onResize() {
        const e = this.querySelector(".web-tabs__tablist");
        this.overflow = checkOverflow(e, "width")
    }

    onFocus(e) {
        const t = e.currentTarget,
            s = this.querySelectorAll(".web-tabs__tab"),
            o = Array.from(s).indexOf(t);
        t.scrollIntoView({
            behavior: "smooth",
            block: "nearest",
            inline: "center"
        }),
            this.activeTab = o
    }

    onKeydown(e) {
        const t = this.querySelectorAll(".web-tabs__tab"),
            s = 35,
            o = 36,
            i = 37,
            n = 39;
        switch (e.keyCode) {
            case n:
                e.preventDefault(),
                    this.nextTab();
                break;
            case i:
                e.preventDefault(),
                    this.previousTab();
                break;
            case o:
                e.preventDefault(),
                    this.firstTab();
                break;
            case s:
                e.preventDefault(),
                    this.lastTab()
        }
        t[this.activeTab].focus()
    }

    focusTab(e) {
        const t = this.querySelectorAll(".web-tabs__tab");
        if (!t[e])
            throw new RangeError("There is no tab at the specified index.");
        t[e].focus()
    }

    previousTab() {
        const e = this.querySelectorAll(".web-tabs__tab");
        e[this.activeTab - 1] ? this.activeTab = this.activeTab - 1 : this.activeTab = e.length - 1
    }

    nextTab() {
        const e = this.querySelectorAll(".web-tabs__tab");
        this.activeTab = (this.activeTab + 1) % e.length || 0
    }

    firstTab() {
        this.activeTab = 0
    }

    lastTab() {
        const e = this.querySelectorAll(".web-tabs__tab");
        this.activeTab = e.length - 1
    }
}

customElements.define("web-tabs", Tabs);
var css_248z$9 = ".web-copy-code__button{color:#202124;height:48px;margin:8px;opacity:0;position:absolute;right:0;width:48px}.web-copy-code__button:focus,.web-copy-code__button:hover{background-color:rgba(63,196,255,.2);opacity:1}.web-copy-code__button:active{background-color:rgba(63,196,255,.4);opacity:1}web-copy-code{display:block;position:relative}web-copy-code:active .web-copy-code__button,web-copy-code:focus .web-copy-code__button,web-copy-code:hover .web-copy-code__button{opacity:1}web-copy-code pre{min-height:64px}";
styleInject(css_248z$9);

class CopyCode extends BaseElement$1 {
    constructor() {
        super(),
            this.onCopy = this.onCopy.bind(this)
    }

    connectedCallback() {
        super.connectedCallback(),
        this.copyButton || (this.copyButton = document.createElement("button"),
            this.copyButton.className = "w-button--icon w-button--round web-copy-code__button",
            this.copyButton.setAttribute("data-icon", "file_copy"),
            this.copyButton.setAttribute("aria-label", "Copy code"),
            this.copyButton.addEventListener("click", this.onCopy),
            this.tooltip = document.createElement("span"),
            this.tooltip.className = "w-tooltip w-tooltip--right",
            this.tooltip.setAttribute("role", "tooltip"),
            this.tooltip.textContent = "Copy code",
            this.copyButton.append(this.tooltip),
            this.prepend(this.copyButton))
    }

    onCopy() {
        window.getSelection().removeAllRanges();
        const e = document.createRange();
        e.selectNode(this.querySelector("code")),
            window.getSelection().addRange(e),
            document.execCommand("copy"),
            window.getSelection().removeAllRanges()
    }
}

customElements.define("web-copy-code", CopyCode);
//# sourceMappingURL=default-4ae3dc8c.js.map

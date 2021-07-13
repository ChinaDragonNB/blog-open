/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at
 * http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at
 * http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at
 * http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at
 * http://polymer.github.io/PATENTS.txt
 */
const directives = new WeakMap
    , directive = e => (...t) => {
    const s = e(...t);
    return directives.set(s, !0),
        s
}
    , isDirective = e => "function" == typeof e && directives.has(e)
    , isCEPolyfill = void 0 !== window.customElements && void 0 !== window.customElements.polyfillWrapFlushCallback
    , reparentNodes = (e, t, s = null, r = null) => {
    for (; t !== s;) {
        const s = t.nextSibling;
        e.insertBefore(t, r),
            t = s
    }
}
    , removeNodes = (e, t, s = null) => {
    for (; t !== s;) {
        const s = t.nextSibling;
        e.removeChild(t),
            t = s
    }
}
    , noChange = {}
    , nothing = {}
    , marker = `{{lit-${String(Math.random()).slice(2)}}}`
    , nodeMarker = `\x3c!--${marker}--\x3e`
    , markerRegex = new RegExp(`${marker}|${nodeMarker}`)
    , boundAttributeSuffix = "$lit$";

class Template {
    constructor(e, t) {
        this.parts = [],
            this.element = t;
        const s = []
            , r = []
            , i = document.createTreeWalker(t.content, 133, null, !1);
        let n = 0
            , o = -1
            , a = 0;
        const {strings: l, values: {length: d}} = e;
        for (; a < d;) {
            const e = i.nextNode();
            if (null !== e) {
                if (o++,
                1 === e.nodeType) {
                    if (e.hasAttributes()) {
                        const t = e.attributes
                            , {length: s} = t;
                        let r = 0;
                        for (let e = 0; e < s; e++)
                            endsWith(t[e].name, "$lit$") && r++;
                        for (; r-- > 0;) {
                            const t = l[a]
                                , s = lastAttributeNameRegex.exec(t)[2]
                                , r = s.toLowerCase() + "$lit$"
                                , i = e.getAttribute(r);
                            e.removeAttribute(r);
                            const n = i.split(markerRegex);
                            this.parts.push({
                                type: "attribute",
                                index: o,
                                name: s,
                                strings: n
                            }),
                                a += n.length - 1
                        }
                    }
                    "TEMPLATE" === e.tagName && (r.push(e),
                        i.currentNode = e.content)
                } else if (3 === e.nodeType) {
                    const t = e.data;
                    if (t.indexOf(marker) >= 0) {
                        const r = e.parentNode
                            , i = t.split(markerRegex)
                            , n = i.length - 1;
                        for (let t = 0; t < n; t++) {
                            let s, n = i[t];
                            if ("" === n)
                                s = createMarker();
                            else {
                                const e = lastAttributeNameRegex.exec(n);
                                null !== e && endsWith(e[2], "$lit$") && (n = n.slice(0, e.index) + e[1] + e[2].slice(0, -"$lit$".length) + e[3]),
                                    s = document.createTextNode(n)
                            }
                            r.insertBefore(s, e),
                                this.parts.push({
                                    type: "node",
                                    index: ++o
                                })
                        }
                        "" === i[n] ? (r.insertBefore(createMarker(), e),
                            s.push(e)) : e.data = i[n],
                            a += n
                    }
                } else if (8 === e.nodeType)
                    if (e.data === marker) {
                        const t = e.parentNode;
                        null !== e.previousSibling && o !== n || (o++,
                            t.insertBefore(createMarker(), e)),
                            n = o,
                            this.parts.push({
                                type: "node",
                                index: o
                            }),
                            null === e.nextSibling ? e.data = "" : (s.push(e),
                                o--),
                            a++
                    } else {
                        let t = -1;
                        for (; -1 !== (t = e.data.indexOf(marker, t + 1));)
                            this.parts.push({
                                type: "node",
                                index: -1
                            }),
                                a++
                    }
            } else
                i.currentNode = r.pop()
        }
        for (const e of s)
            e.parentNode.removeChild(e)
    }
}

const endsWith = (e, t) => {
        const s = e.length - t.length;
        return s >= 0 && e.slice(s) === t
    }
    , isTemplatePartActive = e => -1 !== e.index
    , createMarker = () => document.createComment("")
    ,
    lastAttributeNameRegex = /([ \x09\x0a\x0c\x0d])([^\0-\x1F\x7F-\x9F "'>=/]+)([ \x09\x0a\x0c\x0d]*=[ \x09\x0a\x0c\x0d]*(?:[^ \x09\x0a\x0c\x0d"'`<>=]*|"[^"]*|'[^']*))$/;

/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at
 * http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at
 * http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at
 * http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at
 * http://polymer.github.io/PATENTS.txt
 */
class TemplateInstance {
    constructor(e, t, s) {
        this.__parts = [],
            this.template = e,
            this.processor = t,
            this.options = s
    }

    update(e) {
        let t = 0;
        for (const s of this.__parts)
            void 0 !== s && s.setValue(e[t]),
                t++;
        for (const e of this.__parts)
            void 0 !== e && e.commit()
    }

    _clone() {
        const e = isCEPolyfill ? this.template.element.content.cloneNode(!0) : document.importNode(this.template.element.content, !0)
            , t = []
            , s = this.template.parts
            , r = document.createTreeWalker(e, 133, null, !1);
        let i, n = 0, o = 0, a = r.nextNode();
        for (; n < s.length;)
            if (i = s[n],
                isTemplatePartActive(i)) {
                for (; o < i.index;)
                    o++,
                    "TEMPLATE" === a.nodeName && (t.push(a),
                        r.currentNode = a.content),
                    null === (a = r.nextNode()) && (r.currentNode = t.pop(),
                        a = r.nextNode());
                if ("node" === i.type) {
                    const e = this.processor.handleTextExpression(this.options);
                    e.insertAfterNode(a.previousSibling),
                        this.__parts.push(e)
                } else
                    this.__parts.push(...this.processor.handleAttributeExpressions(a, i.name, i.strings, this.options));
                n++
            } else
                this.__parts.push(void 0),
                    n++;
        return isCEPolyfill && (document.adoptNode(e),
            customElements.upgrade(e)),
            e
    }
}

/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at
 * http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at
 * http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at
 * http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at
 * http://polymer.github.io/PATENTS.txt
 */
class TemplateResult {
    constructor(e, t, s, r) {
        this.strings = e,
            this.values = t,
            this.type = s,
            this.processor = r
    }

    getHTML() {
        const e = this.strings.length - 1;
        let t = ""
            , s = !1;
        for (let r = 0; r < e; r++) {
            const e = this.strings[r]
                , i = e.lastIndexOf("\x3c!--");
            s = (i > -1 || s) && -1 === e.indexOf("--\x3e", i + 1);
            const n = lastAttributeNameRegex.exec(e);
            t += null === n ? e + (s ? marker : nodeMarker) : e.substr(0, n.index) + n[1] + n[2] + "$lit$" + n[3] + marker
        }
        return t += this.strings[e],
            t
    }

    getTemplateElement() {
        const e = document.createElement("template");
        return e.innerHTML = this.getHTML(),
            e
    }
}

class SVGTemplateResult extends TemplateResult {
    getHTML() {
        return `<svg>${super.getHTML()}</svg>`
    }

    getTemplateElement() {
        const e = super.getTemplateElement()
            , t = e.content
            , s = t.firstChild;
        return t.removeChild(s),
            reparentNodes(t, s.firstChild),
            e
    }
}

/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at
 * http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at
 * http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at
 * http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at
 * http://polymer.github.io/PATENTS.txt
 */
const isPrimitive = e => null === e || !("object" == typeof e || "function" == typeof e)
    , isIterable = e => Array.isArray(e) || !(!e || !e[Symbol.iterator]);

class AttributeCommitter {
    constructor(e, t, s) {
        this.dirty = !0,
            this.element = e,
            this.name = t,
            this.strings = s,
            this.parts = [];
        for (let e = 0; e < s.length - 1; e++)
            this.parts[e] = this._createPart()
    }

    _createPart() {
        return new AttributePart(this)
    }

    _getValue() {
        const e = this.strings
            , t = e.length - 1;
        let s = "";
        for (let r = 0; r < t; r++) {
            s += e[r];
            const t = this.parts[r];
            if (void 0 !== t) {
                const e = t.value;
                if (isPrimitive(e) || !isIterable(e))
                    s += "string" == typeof e ? e : String(e);
                else
                    for (const t of e)
                        s += "string" == typeof t ? t : String(t)
            }
        }
        return s += e[t],
            s
    }

    commit() {
        this.dirty && (this.dirty = !1,
            this.element.setAttribute(this.name, this._getValue()))
    }
}

class AttributePart {
    constructor(e) {
        this.value = void 0,
            this.committer = e
    }

    setValue(e) {
        e === noChange || isPrimitive(e) && e === this.value || (this.value = e,
        isDirective(e) || (this.committer.dirty = !0))
    }

    commit() {
        for (; isDirective(this.value);) {
            const e = this.value;
            this.value = noChange,
                e(this)
        }
        this.value !== noChange && this.committer.commit()
    }
}

class NodePart {
    constructor(e) {
        this.value = void 0,
            this.__pendingValue = void 0,
            this.options = e
    }

    appendInto(e) {
        this.startNode = e.appendChild(createMarker()),
            this.endNode = e.appendChild(createMarker())
    }

    insertAfterNode(e) {
        this.startNode = e,
            this.endNode = e.nextSibling
    }

    appendIntoPart(e) {
        e.__insert(this.startNode = createMarker()),
            e.__insert(this.endNode = createMarker())
    }

    insertAfterPart(e) {
        e.__insert(this.startNode = createMarker()),
            this.endNode = e.endNode,
            e.endNode = this.startNode
    }

    setValue(e) {
        this.__pendingValue = e
    }

    commit() {
        for (; isDirective(this.__pendingValue);) {
            const e = this.__pendingValue;
            this.__pendingValue = noChange,
                e(this)
        }
        const e = this.__pendingValue;
        e !== noChange && (isPrimitive(e) ? e !== this.value && this.__commitText(e) : e instanceof TemplateResult ? this.__commitTemplateResult(e) : e instanceof Node ? this.__commitNode(e) : isIterable(e) ? this.__commitIterable(e) : e === nothing ? (this.value = nothing,
            this.clear()) : this.__commitText(e))
    }

    __insert(e) {
        this.endNode.parentNode.insertBefore(e, this.endNode)
    }

    __commitNode(e) {
        this.value !== e && (this.clear(),
            this.__insert(e),
            this.value = e)
    }

    __commitText(e) {
        const t = this.startNode.nextSibling
            , s = "string" == typeof (e = null == e ? "" : e) ? e : String(e);
        t === this.endNode.previousSibling && 3 === t.nodeType ? t.data = s : this.__commitNode(document.createTextNode(s)),
            this.value = e
    }

    __commitTemplateResult(e) {
        const t = this.options.templateFactory(e);
        if (this.value instanceof TemplateInstance && this.value.template === t)
            this.value.update(e.values);
        else {
            const s = new TemplateInstance(t, e.processor, this.options)
                , r = s._clone();
            s.update(e.values),
                this.__commitNode(r),
                this.value = s
        }
    }

    __commitIterable(e) {
        Array.isArray(this.value) || (this.value = [],
            this.clear());
        const t = this.value;
        let s, r = 0;
        for (const i of e)
            s = t[r],
            void 0 === s && (s = new NodePart(this.options),
                t.push(s),
                0 === r ? s.appendIntoPart(this) : s.insertAfterPart(t[r - 1])),
                s.setValue(i),
                s.commit(),
                r++;
        r < t.length && (t.length = r,
            this.clear(s && s.endNode))
    }

    clear(e = this.startNode) {
        removeNodes(this.startNode.parentNode, e.nextSibling, this.endNode)
    }
}

class BooleanAttributePart {
    constructor(e, t, s) {
        if (this.value = void 0,
            this.__pendingValue = void 0,
        2 !== s.length || "" !== s[0] || "" !== s[1])
            throw new Error("Boolean attributes can only contain a single expression");
        this.element = e,
            this.name = t,
            this.strings = s
    }

    setValue(e) {
        this.__pendingValue = e
    }

    commit() {
        for (; isDirective(this.__pendingValue);) {
            const e = this.__pendingValue;
            this.__pendingValue = noChange,
                e(this)
        }
        if (this.__pendingValue === noChange)
            return;
        const e = !!this.__pendingValue;
        this.value !== e && (e ? this.element.setAttribute(this.name, "") : this.element.removeAttribute(this.name),
            this.value = e),
            this.__pendingValue = noChange
    }
}

class PropertyCommitter extends AttributeCommitter {
    constructor(e, t, s) {
        super(e, t, s),
            this.single = 2 === s.length && "" === s[0] && "" === s[1]
    }

    _createPart() {
        return new PropertyPart(this)
    }

    _getValue() {
        return this.single ? this.parts[0].value : super._getValue()
    }

    commit() {
        this.dirty && (this.dirty = !1,
            this.element[this.name] = this._getValue())
    }
}

class PropertyPart extends AttributePart {
}

let eventOptionsSupported = !1;
try {
    const e = {
        get capture() {
            return eventOptionsSupported = !0,
                !1
        }
    };
    window.addEventListener("test", e, e),
        window.removeEventListener("test", e, e)
} catch (e) {
}

class EventPart {
    constructor(e, t, s) {
        this.value = void 0,
            this.__pendingValue = void 0,
            this.element = e,
            this.eventName = t,
            this.eventContext = s,
            this.__boundHandleEvent = e => this.handleEvent(e)
    }

    setValue(e) {
        this.__pendingValue = e
    }

    commit() {
        for (; isDirective(this.__pendingValue);) {
            const e = this.__pendingValue;
            this.__pendingValue = noChange,
                e(this)
        }
        if (this.__pendingValue === noChange)
            return;
        const e = this.__pendingValue
            , t = this.value
            , s = null == e || null != t && (e.capture !== t.capture || e.once !== t.once || e.passive !== t.passive)
            , r = null != e && (null == t || s);
        s && this.element.removeEventListener(this.eventName, this.__boundHandleEvent, this.__options),
        r && (this.__options = getOptions(e),
            this.element.addEventListener(this.eventName, this.__boundHandleEvent, this.__options)),
            this.value = e,
            this.__pendingValue = noChange
    }

    handleEvent(e) {
        "function" == typeof this.value ? this.value.call(this.eventContext || this.element, e) : this.value.handleEvent(e)
    }
}

const getOptions = e => e && (eventOptionsSupported ? {
        capture: e.capture,
        passive: e.passive,
        once: e.once
    } : e.capture)/**
     * @license
     * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
     * This code may only be used under the BSD style license found at
     * http://polymer.github.io/LICENSE.txt
     * The complete set of authors may be found at
     * http://polymer.github.io/AUTHORS.txt
     * The complete set of contributors may be found at
     * http://polymer.github.io/CONTRIBUTORS.txt
     * Code distributed by Google as part of the polymer project is also
     * subject to an additional IP rights grant found at
     * http://polymer.github.io/PATENTS.txt
     */
;

class DefaultTemplateProcessor {
    handleAttributeExpressions(e, t, s, r) {
        const i = t[0];
        if ("." === i) {
            return new PropertyCommitter(e, t.slice(1), s).parts
        }
        return "@" === i ? [new EventPart(e, t.slice(1), r.eventContext)] : "?" === i ? [new BooleanAttributePart(e, t.slice(1), s)] : new AttributeCommitter(e, t, s).parts
    }

    handleTextExpression(e) {
        return new NodePart(e)
    }
}

const defaultTemplateProcessor = new DefaultTemplateProcessor;

/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at
 * http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at
 * http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at
 * http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at
 * http://polymer.github.io/PATENTS.txt
 */
function templateFactory(e) {
    let t = templateCaches.get(e.type);
    void 0 === t && (t = {
        stringsArray: new WeakMap,
        keyString: new Map
    },
        templateCaches.set(e.type, t));
    let s = t.stringsArray.get(e.strings);
    if (void 0 !== s)
        return s;
    const r = e.strings.join(marker);
    return s = t.keyString.get(r),
    void 0 === s && (s = new Template(e, e.getTemplateElement()),
        t.keyString.set(r, s)),
        t.stringsArray.set(e.strings, s),
        s
}

const templateCaches = new Map
    , parts = new WeakMap
    , render = (e, t, s) => {
        let r = parts.get(t);
        void 0 === r && (removeNodes(t, t.firstChild),
            parts.set(t, r = new NodePart(Object.assign({
                templateFactory: templateFactory
            }, s))),
            r.appendInto(t)),
            r.setValue(e),
            r.commit()
    }
;
/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at
 * http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at
 * http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at
 * http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at
 * http://polymer.github.io/PATENTS.txt
 */
/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at
 * http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at
 * http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at
 * http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at
 * http://polymer.github.io/PATENTS.txt
 */
(window.litHtmlVersions || (window.litHtmlVersions = [])).push("1.1.1");
const html = (e, ...t) => new TemplateResult(e, t, "html", defaultTemplateProcessor)
    , svg = (e, ...t) => new SVGTemplateResult(e, t, "svg", defaultTemplateProcessor)/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at
 * http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at
 * http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at
 * http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at
 * http://polymer.github.io/PATENTS.txt
 */
    , walkerNodeFilter = 133;

function removeNodesFromTemplate(e, t) {
    const {element: {content: s}, parts: r} = e
        , i = document.createTreeWalker(s, 133, null, !1);
    let n = nextActiveIndexInTemplateParts(r)
        , o = r[n]
        , a = -1
        , l = 0;
    const d = [];
    let h = null;
    for (; i.nextNode();) {
        a++;
        const e = i.currentNode;
        for (e.previousSibling === h && (h = null),
             t.has(e) && (d.push(e),
             null === h && (h = e)),
             null !== h && l++; void 0 !== o && o.index === a;)
            o.index = null !== h ? -1 : o.index - l,
                n = nextActiveIndexInTemplateParts(r, n),
                o = r[n]
    }
    d.forEach(e => e.parentNode.removeChild(e))
}

const countNodes = e => {
        let t = 11 === e.nodeType ? 0 : 1;
        const s = document.createTreeWalker(e, 133, null, !1);
        for (; s.nextNode();)
            t++;
        return t
    }
    , nextActiveIndexInTemplateParts = (e, t = -1) => {
        for (let s = t + 1; s < e.length; s++) {
            const t = e[s];
            if (isTemplatePartActive(t))
                return s
        }
        return -1
    }
;

function insertNodeIntoTemplate(e, t, s = null) {
    const {element: {content: r}, parts: i} = e;
    if (null == s)
        return void r.appendChild(t);
    const n = document.createTreeWalker(r, 133, null, !1);
    let o = nextActiveIndexInTemplateParts(i)
        , a = 0
        , l = -1;
    for (; n.nextNode();) {
        for (l++,
             n.currentNode === s && (a = countNodes(t),
                 s.parentNode.insertBefore(t, s)); -1 !== o && i[o].index === l;) {
            if (a > 0) {
                for (; -1 !== o;)
                    i[o].index += a,
                        o = nextActiveIndexInTemplateParts(i, o);
                return
            }
            o = nextActiveIndexInTemplateParts(i, o)
        }
    }
}

/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at
 * http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at
 * http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at
 * http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at
 * http://polymer.github.io/PATENTS.txt
 */
const getTemplateCacheKey = (e, t) => `${e}--${t}`;
let compatibleShadyCSSVersion = !0;
void 0 === window.ShadyCSS ? compatibleShadyCSSVersion = !1 : void 0 === window.ShadyCSS.prepareTemplateDom && (console.warn("Incompatible ShadyCSS version detected. Please update to at least @webcomponents/webcomponentsjs@2.0.2 and @webcomponents/shadycss@1.3.1."),
    compatibleShadyCSSVersion = !1);
const shadyTemplateFactory = e => t => {
        const s = getTemplateCacheKey(t.type, e);
        let r = templateCaches.get(s);
        void 0 === r && (r = {
            stringsArray: new WeakMap,
            keyString: new Map
        },
            templateCaches.set(s, r));
        let i = r.stringsArray.get(t.strings);
        if (void 0 !== i)
            return i;
        const n = t.strings.join(marker);
        if (i = r.keyString.get(n),
        void 0 === i) {
            const s = t.getTemplateElement();
            compatibleShadyCSSVersion && window.ShadyCSS.prepareTemplateDom(s, e),
                i = new Template(t, s),
                r.keyString.set(n, i)
        }
        return r.stringsArray.set(t.strings, i),
            i
    }
    , TEMPLATE_TYPES = ["html", "svg"]
    , removeStylesFromLitTemplates = e => {
        TEMPLATE_TYPES.forEach(t => {
                const s = templateCaches.get(getTemplateCacheKey(t, e));
                void 0 !== s && s.keyString.forEach(e => {
                        const {element: {content: t}} = e
                            , s = new Set;
                        Array.from(t.querySelectorAll("style")).forEach(e => {
                                s.add(e)
                            }
                        ),
                            removeNodesFromTemplate(e, s)
                    }
                )
            }
        )
    }
    , shadyRenderSet = new Set
    , prepareTemplateStyles = (e, t, s) => {
        shadyRenderSet.add(e);
        const r = s ? s.element : document.createElement("template")
            , i = t.querySelectorAll("style")
            , {length: n} = i;
        if (0 === n)
            return void window.ShadyCSS.prepareTemplateStyles(r, e);
        const o = document.createElement("style");
        for (let e = 0; e < n; e++) {
            const t = i[e];
            t.parentNode.removeChild(t),
                o.textContent += t.textContent
        }
        removeStylesFromLitTemplates(e);
        const a = r.content;
        s ? insertNodeIntoTemplate(s, o, a.firstChild) : a.insertBefore(o, a.firstChild),
            window.ShadyCSS.prepareTemplateStyles(r, e);
        const l = a.querySelector("style");
        if (window.ShadyCSS.nativeShadow && null !== l)
            t.insertBefore(l.cloneNode(!0), t.firstChild);
        else if (s) {
            a.insertBefore(o, a.firstChild);
            const e = new Set;
            e.add(o),
                removeNodesFromTemplate(s, e)
        }
    }
    , render$1 = (e, t, s) => {
        if (!s || "object" != typeof s || !s.scopeName)
            throw new Error("The `scopeName` option is required.");
        const r = s.scopeName
            , i = parts.has(t)
            , n = compatibleShadyCSSVersion && 11 === t.nodeType && !!t.host
            , o = n && !shadyRenderSet.has(r)
            , a = o ? document.createDocumentFragment() : t;
        if (render(e, a, Object.assign({
            templateFactory: shadyTemplateFactory(r)
        }, s)),
            o) {
            const e = parts.get(a);
            parts.delete(a);
            const s = e.value instanceof TemplateInstance ? e.value.template : void 0;
            prepareTemplateStyles(r, a, s),
                removeNodes(t, t.firstChild),
                t.appendChild(a),
                parts.set(t, e)
        }
        !i && n && window.ShadyCSS.styleElement(t.host)
    }
;
/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at
 * http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at
 * http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at
 * http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at
 * http://polymer.github.io/PATENTS.txt
 */
window.JSCompiler_renameProperty = (e, t) => e;
const defaultConverter = {
    toAttribute(e, t) {
        switch (t) {
            case Boolean:
                return e ? "" : null;
            case Object:
            case Array:
                return null == e ? e : JSON.stringify(e)
        }
        return e
    },
    fromAttribute(e, t) {
        switch (t) {
            case Boolean:
                return null !== e;
            case Number:
                return null === e ? null : Number(e);
            case Object:
            case Array:
                return JSON.parse(e)
        }
        return e
    }
}
    , notEqual = (e, t) => t !== e && (t == t || e == e)
    , defaultPropertyDeclaration = {
    attribute: !0,
    type: String,
    converter: defaultConverter,
    reflect: !1,
    hasChanged: notEqual
}
    , microtaskPromise = Promise.resolve(!0)
    , STATE_HAS_UPDATED = 1
    , STATE_UPDATE_REQUESTED = 4
    , STATE_IS_REFLECTING_TO_ATTRIBUTE = 8
    , STATE_IS_REFLECTING_TO_PROPERTY = 16
    , STATE_HAS_CONNECTED = 32;

class UpdatingElement extends HTMLElement {
    constructor() {
        super(),
            this._updateState = 0,
            this._instanceProperties = void 0,
            this._updatePromise = microtaskPromise,
            this._hasConnectedResolver = void 0,
            this._changedProperties = new Map,
            this._reflectingProperties = void 0,
            this.initialize()
    }

    static get observedAttributes() {
        this.finalize();
        const e = [];
        return this._classProperties.forEach((t, s) => {
                const r = this._attributeNameForProperty(s, t);
                void 0 !== r && (this._attributeToPropertyMap.set(r, s),
                    e.push(r))
            }
        ),
            e
    }

    static _ensureClassProperties() {
        if (!this.hasOwnProperty(JSCompiler_renameProperty("_classProperties", this))) {
            this._classProperties = new Map;
            const e = Object.getPrototypeOf(this)._classProperties;
            void 0 !== e && e.forEach((e, t) => this._classProperties.set(t, e))
        }
    }

    static createProperty(e, t = defaultPropertyDeclaration) {
        if (this._ensureClassProperties(),
            this._classProperties.set(e, t),
        t.noAccessor || this.prototype.hasOwnProperty(e))
            return;
        const s = "symbol" == typeof e ? Symbol() : `__${e}`;
        Object.defineProperty(this.prototype, e, {
            get() {
                return this[s]
            },
            set(t) {
                const r = this[e];
                this[s] = t,
                    this._requestUpdate(e, r)
            },
            configurable: !0,
            enumerable: !0
        })
    }

    static finalize() {
        if (this.hasOwnProperty(JSCompiler_renameProperty("finalized", this)) && this.finalized)
            return;
        const e = Object.getPrototypeOf(this);
        if ("function" == typeof e.finalize && e.finalize(),
            this.finalized = !0,
            this._ensureClassProperties(),
            this._attributeToPropertyMap = new Map,
            this.hasOwnProperty(JSCompiler_renameProperty("properties", this))) {
            const e = this.properties
                ,
                t = [...Object.getOwnPropertyNames(e), ..."function" == typeof Object.getOwnPropertySymbols ? Object.getOwnPropertySymbols(e) : []];
            for (const s of t)
                this.createProperty(s, e[s])
        }
    }

    static _attributeNameForProperty(e, t) {
        const s = t.attribute;
        return !1 === s ? void 0 : "string" == typeof s ? s : "string" == typeof e ? e.toLowerCase() : void 0
    }

    static _valueHasChanged(e, t, s = notEqual) {
        return s(e, t)
    }

    static _propertyValueFromAttribute(e, t) {
        const s = t.type
            , r = t.converter || defaultConverter
            , i = "function" == typeof r ? r : r.fromAttribute;
        return i ? i(e, s) : e
    }

    static _propertyValueToAttribute(e, t) {
        if (void 0 === t.reflect)
            return;
        const s = t.type
            , r = t.converter;
        return (r && r.toAttribute || defaultConverter.toAttribute)(e, s)
    }

    initialize() {
        this._saveInstanceProperties(),
            this._requestUpdate()
    }

    _saveInstanceProperties() {
        this.constructor._classProperties.forEach((e, t) => {
                if (this.hasOwnProperty(t)) {
                    const e = this[t];
                    delete this[t],
                    this._instanceProperties || (this._instanceProperties = new Map),
                        this._instanceProperties.set(t, e)
                }
            }
        )
    }

    _applyInstanceProperties() {
        this._instanceProperties.forEach((e, t) => this[t] = e),
            this._instanceProperties = void 0
    }

    connectedCallback() {
        this._updateState = 32 | this._updateState,
        this._hasConnectedResolver && (this._hasConnectedResolver(),
            this._hasConnectedResolver = void 0)
    }

    disconnectedCallback() {
    }

    attributeChangedCallback(e, t, s) {
        t !== s && this._attributeToProperty(e, s)
    }

    _propertyToAttribute(e, t, s = defaultPropertyDeclaration) {
        const r = this.constructor
            , i = r._attributeNameForProperty(e, s);
        if (void 0 !== i) {
            const e = r._propertyValueToAttribute(t, s);
            if (void 0 === e)
                return;
            this._updateState = 8 | this._updateState,
                null == e ? this.removeAttribute(i) : this.setAttribute(i, e),
                this._updateState = -9 & this._updateState
        }
    }

    _attributeToProperty(e, t) {
        if (8 & this._updateState)
            return;
        const s = this.constructor
            , r = s._attributeToPropertyMap.get(e);
        if (void 0 !== r) {
            const e = s._classProperties.get(r) || defaultPropertyDeclaration;
            this._updateState = 16 | this._updateState,
                this[r] = s._propertyValueFromAttribute(t, e),
                this._updateState = -17 & this._updateState
        }
    }

    _requestUpdate(e, t) {
        let s = !0;
        if (void 0 !== e) {
            const r = this.constructor
                , i = r._classProperties.get(e) || defaultPropertyDeclaration;
            r._valueHasChanged(this[e], t, i.hasChanged) ? (this._changedProperties.has(e) || this._changedProperties.set(e, t),
            !0 !== i.reflect || 16 & this._updateState || (void 0 === this._reflectingProperties && (this._reflectingProperties = new Map),
                this._reflectingProperties.set(e, i))) : s = !1
        }
        !this._hasRequestedUpdate && s && this._enqueueUpdate()
    }

    requestUpdate(e, t) {
        return this._requestUpdate(e, t),
            this.updateComplete
    }

    async _enqueueUpdate() {
        let e, t;
        this._updateState = 4 | this._updateState;
        const s = this._updatePromise;
        this._updatePromise = new Promise((s, r) => {
                e = s,
                    t = r
            }
        );
        try {
            await s
        } catch (e) {
        }
        this._hasConnected || await new Promise(e => this._hasConnectedResolver = e);
        try {
            const e = this.performUpdate();
            null != e && await e
        } catch (e) {
            t(e)
        }
        e(!this._hasRequestedUpdate)
    }

    get _hasConnected() {
        return 32 & this._updateState
    }

    get _hasRequestedUpdate() {
        return 4 & this._updateState
    }

    get hasUpdated() {
        return 1 & this._updateState
    }

    performUpdate() {
        this._instanceProperties && this._applyInstanceProperties();
        let e = !1;
        const t = this._changedProperties;
        try {
            e = this.shouldUpdate(t),
            e && this.update(t)
        } catch (t) {
            throw e = !1,
                t
        } finally {
            this._markUpdated()
        }
        e && (1 & this._updateState || (this._updateState = 1 | this._updateState,
            this.firstUpdated(t)),
            this.updated(t))
    }

    _markUpdated() {
        this._changedProperties = new Map,
            this._updateState = -5 & this._updateState
    }

    get updateComplete() {
        return this._updatePromise
    }

    shouldUpdate(e) {
        return !0
    }

    update(e) {
        void 0 !== this._reflectingProperties && this._reflectingProperties.size > 0 && (this._reflectingProperties.forEach((e, t) => this._propertyToAttribute(t, this[t], e)),
            this._reflectingProperties = void 0)
    }

    updated(e) {
    }

    firstUpdated(e) {
    }
}

UpdatingElement.finalized = !0;
/**
 @license
 Copyright (c) 2019 The Polymer Project Authors. All rights reserved.
 This code may only be used under the BSD style license found at
 http://polymer.github.io/LICENSE.txt The complete set of authors may be found at
 http://polymer.github.io/AUTHORS.txt The complete set of contributors may be
 found at http://polymer.github.io/CONTRIBUTORS.txt Code distributed by Google as
 part of the polymer project is also subject to an additional IP rights grant
 found at http://polymer.github.io/PATENTS.txt
 */
const supportsAdoptingStyleSheets = "adoptedStyleSheets" in Document.prototype && "replace" in CSSStyleSheet.prototype;

/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at
 * http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at
 * http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at
 * http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at
 * http://polymer.github.io/PATENTS.txt
 */
function arrayFlat(e, t = []) {
    for (let s = 0, r = e.length; s < r; s++) {
        const r = e[s];
        Array.isArray(r) ? arrayFlat(r, t) : t.push(r)
    }
    return t
}

(window.litElementVersions || (window.litElementVersions = [])).push("2.2.0");
const flattenStyles = e => e.flat ? e.flat(1 / 0) : arrayFlat(e);

class LitElement extends UpdatingElement {
    static finalize() {
        super.finalize(),
            this._styles = this.hasOwnProperty(JSCompiler_renameProperty("styles", this)) ? this._getUniqueStyles() : this._styles || []
    }

    static _getUniqueStyles() {
        const e = this.styles
            , t = [];
        if (Array.isArray(e)) {
            flattenStyles(e).reduceRight((e, t) => (e.add(t),
                e), new Set).forEach(e => t.unshift(e))
        } else
            e && t.push(e);
        return t
    }

    initialize() {
        super.initialize(),
            this.renderRoot = this.createRenderRoot(),
        window.ShadowRoot && this.renderRoot instanceof window.ShadowRoot && this.adoptStyles()
    }

    createRenderRoot() {
        return this.attachShadow({
            mode: "open"
        })
    }

    adoptStyles() {
        const e = this.constructor._styles;
        0 !== e.length && (void 0 === window.ShadyCSS || window.ShadyCSS.nativeShadow ? supportsAdoptingStyleSheets ? this.renderRoot.adoptedStyleSheets = e.map(e => e.styleSheet) : this._needsShimAdoptedStyleSheets = !0 : window.ShadyCSS.ScopingShim.prepareAdoptedCssText(e.map(e => e.cssText), this.localName))
    }

    connectedCallback() {
        super.connectedCallback(),
        this.hasUpdated && void 0 !== window.ShadyCSS && window.ShadyCSS.styleElement(this)
    }

    update(e) {
        super.update(e);
        const t = this.render();
        t instanceof TemplateResult && this.constructor.render(t, this.renderRoot, {
            scopeName: this.localName,
            eventContext: this
        }),
        this._needsShimAdoptedStyleSheets && (this._needsShimAdoptedStyleSheets = !1,
            this.constructor._styles.forEach(e => {
                    const t = document.createElement("style");
                    t.textContent = e.cssText,
                        this.renderRoot.appendChild(t)
                }
            ))
    }

    render() {
    }
}

LitElement.finalized = !0,
    LitElement.render = render$1;
/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at
 * http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at
 * http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at
 * http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at
 * http://polymer.github.io/PATENTS.txt
 */
const _state = new WeakMap
    , _infinity = 2147483647
    , until = directive((...e) => t => {
        let s = _state.get(t);
        void 0 === s && (s = {
            lastRenderedIndex: _infinity,
            values: []
        },
            _state.set(t, s));
        const r = s.values;
        let i = r.length;
        s.values = e;
        for (let n = 0; n < e.length && !(n > s.lastRenderedIndex); n++) {
            const o = e[n];
            if (isPrimitive(o) || "function" != typeof o.then) {
                t.setValue(o),
                    s.lastRenderedIndex = n;
                break
            }
            n < i && o === r[n] || (s.lastRenderedIndex = _infinity,
                i = 0,
                Promise.resolve(o).then(e => {
                        const r = s.values.indexOf(o);
                        r > -1 && r < s.lastRenderedIndex && (s.lastRenderedIndex = r,
                            t.setValue(e),
                            t.commit())
                    }
                ))
        }
    }
);
export {LitElement as L, html as h, render as r, svg as s, until as u};
//# sourceMappingURL=lit-ec003724.js.map

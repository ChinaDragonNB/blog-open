function createBrowserLocalStorageCache(e) {
    const t = `algoliasearch-client-js-${e.key}`;
    let r;
    const a = () => (void 0 === r && (r = e.localStorage || window.localStorage),
        r)
        , s = () => JSON.parse(a().getItem(t) || "{}");
    return {
        get: (e, t, r = {
            miss: () => Promise.resolve()
        }) => Promise.resolve().then(() => {
                const r = JSON.stringify(e)
                    , a = s()[r];
                return Promise.all([a || t(), void 0 !== a])
            }
        ).then(([e, t]) => Promise.all([e, t || r.miss(e)])).then(([e]) => e),
        set: (e, r) => Promise.resolve().then(() => {
                const o = s();
                return o[JSON.stringify(e)] = r,
                    a().setItem(t, JSON.stringify(o)),
                    r
            }
        ),
        delete: e => Promise.resolve().then(() => {
                const r = s();
                delete r[JSON.stringify(e)],
                    a().setItem(t, JSON.stringify(r))
            }
        ),
        clear: () => Promise.resolve().then(() => {
                a().removeItem(t)
            }
        )
    }
}

function createFallbackableCache(e) {
    const t = [...e.caches]
        , r = t.shift();
    return void 0 === r ? createNullCache() : {
        get: (e, a, s = {
            miss: () => Promise.resolve()
        }) => r.get(e, a, s).catch(() => createFallbackableCache({
            caches: t
        }).get(e, a, s)),
        set: (e, a) => r.set(e, a).catch(() => createFallbackableCache({
            caches: t
        }).set(e, a)),
        delete: e => r.delete(e).catch(() => createFallbackableCache({
            caches: t
        }).delete(e)),
        clear: () => r.clear().catch(() => createFallbackableCache({
            caches: t
        }).clear())
    }
}

function createNullCache() {
    return {
        get: (e, t, r = {
            miss: () => Promise.resolve()
        }) => t().then(e => Promise.all([e, r.miss(e)])).then(([e]) => e),
        set: (e, t) => Promise.resolve(t),
        delete: e => Promise.resolve(),
        clear: () => Promise.resolve()
    }
}

function createInMemoryCache(e = {
    serializable: !0
}) {
    let t = {};
    return {
        get(r, a, s = {
            miss: () => Promise.resolve()
        }) {
            const o = JSON.stringify(r);
            if (o in t)
                return Promise.resolve(e.serializable ? JSON.parse(t[o]) : t[o]);
            const n = a()
                , c = s && s.miss || (() => Promise.resolve());
            return n.then(e => c(e)).then(() => n)
        },
        set: (r, a) => (t[JSON.stringify(r)] = e.serializable ? JSON.stringify(a) : a,
            Promise.resolve(a)),
        delete: e => (delete t[JSON.stringify(e)],
            Promise.resolve()),
        clear: () => (t = {},
            Promise.resolve())
    }
}

function createAuth(e, t, r) {
    const a = {
        "x-algolia-api-key": r,
        "x-algolia-application-id": t
    };
    return {
        headers: () => e === AuthMode.WithinHeaders ? a : {},
        queryParameters: () => e === AuthMode.WithinQueryParameters ? a : {}
    }
}

function shuffle(e) {
    let t = e.length - 1;
    for (; t > 0; t--) {
        const r = Math.floor(Math.random() * (t + 1))
            , a = e[t];
        e[t] = e[r],
            e[r] = a
    }
    return e
}

function addMethods(e, t) {
    return Object.keys(void 0 !== t ? t : {}).forEach(r => {
            e[r] = t[r](e)
        }
    ),
        e
}

function encode(e, ...t) {
    let r = 0;
    return e.replace(/%s/g, () => encodeURIComponent(t[r++]))
}

const version = "4.1.0"
    , AuthMode = {
    WithinQueryParameters: 0,
    WithinHeaders: 1
};

function createMappedRequestOptions(e, t) {
    const r = e || {}
        , a = r.data || {};
    return Object.keys(r).forEach(e => {
            -1 === ["timeout", "headers", "queryParameters", "data", "cacheable"].indexOf(e) && (a[e] = r[e])
        }
    ),
        {
            data: Object.entries(a).length > 0 ? a : void 0,
            timeout: r.timeout || t,
            headers: r.headers || {},
            queryParameters: r.queryParameters || {},
            cacheable: r.cacheable
        }
}

const CallEnum = {
    Read: 1,
    Write: 2,
    Any: 3
}
    , HostStatusEnum = {
    Up: 1,
    Down: 2,
    Timeouted: 3
}
    , EXPIRATION_DELAY = 12e4;

function createStatefulHost(e, t = HostStatusEnum.Up) {
    return {
        ...e,
        status: t,
        lastUpdate: Date.now()
    }
}

function isStatefulHostUp(e) {
    return e.status === HostStatusEnum.Up || Date.now() - e.lastUpdate > 12e4
}

function isStatefulHostTimeouted(e) {
    return e.status === HostStatusEnum.Timeouted && Date.now() - e.lastUpdate <= 12e4
}

function createStatelessHost(e) {
    return {
        protocol: e.protocol || "https",
        url: e.url,
        accept: e.accept || CallEnum.Any
    }
}

const MethodEnum = {
    Delete: "DELETE",
    Get: "GET",
    Post: "POST",
    Put: "PUT"
};

function createRetryableOptions(e, t) {
    return Promise.all(t.map(t => e.get(t, () => Promise.resolve(createStatefulHost(t))))).then(e => {
            const r = e.filter(e => isStatefulHostUp(e))
                , a = e.filter(e => isStatefulHostTimeouted(e))
                , s = [...r, ...a];
            return {
                getTimeout: (e, t) => (0 === a.length && 0 === e ? 1 : a.length + 3 + e) * t,
                statelessHosts: s.length > 0 ? s.map(e => createStatelessHost(e)) : t
            }
        }
    )
}

const isNetworkError = ({isTimedOut: e, status: t}) => !e && 0 == ~~t
    , isRetryable = e => {
    const t = e.status;
    return e.isTimedOut || isNetworkError(e) || 2 != ~~(t / 100) && 4 != ~~(t / 100)
}
    , isSuccess = ({status: e}) => 2 == ~~(e / 100)
    , retryDecision = (e, t) => isRetryable(e) ? t.onRetry(e) : isSuccess(e) ? t.onSucess(e) : t.onFail(e);

function retryableRequest(e, t, r, a) {
    const s = []
        , o = serializeData(r, a)
        , n = serializeHeaders(e, a)
        , c = r.method
        , i = r.method !== MethodEnum.Get ? {} : {
        ...r.data,
        ...a.data
    }
        , l = {
        "x-algolia-agent": e.userAgent.value,
        ...e.queryParameters,
        ...i,
        ...a.queryParameters
    };
    let u = 0;
    const h = (t, i) => {
            const d = t.pop();
            if (void 0 === d)
                throw createRetryError(stackTraceWithoutCredentials(s));
            const m = {
                data: o,
                headers: n,
                method: c,
                url: serializeUrl(d, r.path, l),
                connectTimeout: i(u, e.timeouts.connect),
                responseTimeout: i(u, a.timeout)
            }
                , p = e => {
                const r = {
                    request: m,
                    response: e,
                    host: d,
                    triesLeft: t.length
                };
                return s.push(r),
                    r
            }
                , g = {
                onSucess: e => deserializeSuccess(e),
                onRetry(r) {
                    const a = p(r);
                    return r.isTimedOut && u++,
                        Promise.all([e.logger.info("Retryable failure", stackFrameWithoutCredentials(a)), e.hostsCache.set(d, createStatefulHost(d, r.isTimedOut ? HostStatusEnum.Timeouted : HostStatusEnum.Down))]).then(() => h(t, i))
                },
                onFail(e) {
                    throw p(e),
                        deserializeFailure(e, stackTraceWithoutCredentials(s))
                }
            };
            return e.requester.send(m).then(e => retryDecision(e, g))
        }
    ;
    return createRetryableOptions(e.hostsCache, t).then(e => h([...e.statelessHosts].reverse(), e.getTimeout))
}

function createTransporter(e) {
    const {hostsCache: t, logger: r, requester: a, requestsCache: s, responsesCache: o, timeouts: n, userAgent: c, hosts: i, queryParameters: l, headers: u} = e
        , h = {
        hostsCache: t,
        logger: r,
        requester: a,
        requestsCache: s,
        responsesCache: o,
        timeouts: n,
        userAgent: c,
        headers: u,
        queryParameters: l,
        hosts: i.map(e => createStatelessHost(e)),
        read(e, t) {
            const r = createMappedRequestOptions(t, h.timeouts.read)
                , a = () => retryableRequest(h, h.hosts.filter(e => 0 != (e.accept & CallEnum.Read)), e, r);
            if (!0 !== (void 0 !== r.cacheable ? r.cacheable : e.cacheable))
                return a();
            const s = {
                request: e,
                mappedRequestOptions: r,
                transporter: {
                    queryParameters: h.queryParameters,
                    headers: h.headers
                }
            };
            return h.responsesCache.get(s, () => h.requestsCache.get(s, () => h.requestsCache.set(s, a()).then(e => Promise.all([h.requestsCache.delete(s), e]), e => Promise.all([h.requestsCache.delete(s), Promise.reject(e)])).then(([e, t]) => t)), {
                miss: e => h.responsesCache.set(s, e)
            })
        },
        write: (e, t) => retryableRequest(h, h.hosts.filter(e => 0 != (e.accept & CallEnum.Write)), e, createMappedRequestOptions(t, h.timeouts.write))
    };
    return h
}

function createUserAgent(e) {
    const t = {
        value: `Algolia for JavaScript (${e})`,
        add(e) {
            const r = `; ${e.segment}${void 0 !== e.version ? ` (${e.version})` : ""}`;
            return -1 === t.value.indexOf(r) && (t.value = `${t.value}${r}`),
                t
        }
    };
    return t
}

function deserializeSuccess(e) {
    try {
        return JSON.parse(e.content)
    } catch (t) {
        throw createDeserializationError(t.message, e)
    }
}

function deserializeFailure({content: e, status: t}, r) {
    let a = e;
    try {
        a = JSON.parse(e).message
    } catch (e) {
    }
    return createApiError(a, t, r)
}

function serializeUrl(e, t, r) {
    const a = serializeQueryParameters(r);
    let s = `${e.protocol}://${e.url}/${"/" === t.charAt(0) ? t.substr(1) : t}`;
    return a.length && (s += `?${a}`),
        s
}

function serializeQueryParameters(e) {
    return Object.keys(e).map(t => {
            return encode("%s=%s", t, (r = e[t],
                "[object Object]" === Object.prototype.toString.call(r) || "[object Array]" === Object.prototype.toString.call(r) ? JSON.stringify(e[t]) : e[t]));
            var r
        }
    ).join("&")
}

function serializeData(e, t) {
    if (e.method === MethodEnum.Get || void 0 === e.data && void 0 === t.data)
        return;
    const r = Array.isArray(e.data) ? e.data : {
        ...e.data,
        ...t.data
    };
    return JSON.stringify(r)
}

function serializeHeaders(e, t) {
    const r = {
        ...e.headers,
        ...t.headers
    }
        , a = {};
    return Object.keys(r).forEach(e => {
            const t = r[e];
            a[e.toLowerCase()] = t
        }
    ),
        a
}

function stackTraceWithoutCredentials(e) {
    return e.map(e => stackFrameWithoutCredentials(e))
}

function stackFrameWithoutCredentials(e) {
    const t = e.request.headers["x-algolia-api-key"] ? {
        "x-algolia-api-key": "*****"
    } : {};
    return {
        ...e,
        request: {
            ...e.request,
            headers: {
                ...e.request.headers,
                ...t
            }
        }
    }
}

function createApiError(e, t, r) {
    return {
        name: "ApiError",
        message: e,
        status: t,
        transporterStackTrace: r
    }
}

function createDeserializationError(e, t) {
    return {
        name: "DeserializationError",
        message: e,
        response: t
    }
}

function createRetryError(e) {
    return {
        name: "RetryError",
        message: "Unreachable hosts - your application id may be incorrect. If the error persists, contact support@algolia.com.",
        transporterStackTrace: e
    }
}

const createSearchClient = e => {
    const t = e.appId
        , r = createAuth(void 0 !== e.authMode ? e.authMode : AuthMode.WithinHeaders, t, e.apiKey)
        , a = createTransporter({
        hosts: [{
            url: `${t}-dsn.algolia.net`,
            accept: CallEnum.Read
        }, {
            url: `${t}.algolia.net`,
            accept: CallEnum.Write
        }].concat(shuffle([{
            url: `${t}-1.algolianet.com`
        }, {
            url: `${t}-2.algolianet.com`
        }, {
            url: `${t}-3.algolianet.com`
        }])),
        ...e,
        headers: {
            ...r.headers(),
            "content-type": "application/x-www-form-urlencoded",
            ...e.headers
        },
        queryParameters: {
            ...r.queryParameters(),
            ...e.queryParameters
        }
    });
    return addMethods({
        transporter: a,
        appId: t,
        addAlgoliaAgent(e, t) {
            a.userAgent.add({
                segment: e,
                version: t
            })
        },
        clearCache: () => Promise.all([a.requestsCache.clear(), a.responsesCache.clear()]).then(() => {
            }
        )
    }, e.methods)
}
    , initIndex = e => (t, r = {}) => addMethods({
    transporter: e.transporter,
    appId: e.appId,
    indexName: t
}, r.methods)
    , multipleQueries = e => (t, r) => {
    const a = t.map(e => ({
        ...e,
        params: serializeQueryParameters(e.params || {})
    }));
    return e.transporter.read({
        method: MethodEnum.Post,
        path: "1/indexes/*/queries",
        data: {
            requests: a
        },
        cacheable: !0
    }, r)
}
    , multipleSearchForFacetValues = e => (t, r) => Promise.all(t.map(t => {
        const {facetName: a, facetQuery: s, ...o} = t.params;
        return initIndex(e)(t.indexName, {
            methods: {
                searchForFacetValues: searchForFacetValues
            }
        }).searchForFacetValues(a, s, {
            ...r,
            ...o
        })
    }
))
    , search = e => (t, r) => e.transporter.read({
    method: MethodEnum.Post,
    path: encode("1/indexes/%s/query", e.indexName),
    data: {
        query: t
    },
    cacheable: !0
}, r)
    , searchForFacetValues = e => (t, r, a) => e.transporter.read({
    method: MethodEnum.Post,
    path: encode("1/indexes/%s/facets/%s/query", e.indexName, t),
    data: {
        facetQuery: r
    },
    cacheable: !0
}, a)
    , LogLevelEnum = {
    Debug: 1,
    Info: 2,
    Error: 3
};

function createConsoleLogger(e) {
    return {
        debug: (t, r) => (LogLevelEnum.Debug >= e && console.debug(t, r),
            Promise.resolve()),
        info: (t, r) => (LogLevelEnum.Info >= e && console.info(t, r),
            Promise.resolve()),
        error: (e, t) => (console.error(e, t),
            Promise.resolve())
    }
}

function createBrowserXhrRequester() {
    return {
        send: e => new Promise(t => {
                const r = new XMLHttpRequest;
                r.open(e.method, e.url, !0),
                    Object.keys(e.headers).forEach(t => r.setRequestHeader(t, e.headers[t]));
                const a = (e, a) => setTimeout(() => {
                        r.abort(),
                            t({
                                status: 0,
                                content: a,
                                isTimedOut: !0
                            })
                    }
                    , 1e3 * e)
                    , s = a(e.connectTimeout, "Connection timeout");
                let o;
                r.onreadystatechange = () => {
                    r.readyState > r.OPENED && void 0 === o && (clearTimeout(s),
                        o = a(e.responseTimeout, "Socket timeout"))
                }
                    ,
                    r.onerror = () => {
                        0 === r.status && (clearTimeout(s),
                            clearTimeout(o),
                            t({
                                content: r.responseText || "Network request failed",
                                status: r.status,
                                isTimedOut: !1
                            }))
                    }
                    ,
                    r.onload = () => {
                        clearTimeout(s),
                            clearTimeout(o),
                            t({
                                content: r.responseText,
                                status: r.status,
                                isTimedOut: !1
                            })
                    }
                    ,
                    r.send(e.data)
            }
        )
    }
}

function algoliasearch(e, t, r) {
    const a = {
        appId: e,
        apiKey: t,
        timeouts: {
            connect: 1,
            read: 2,
            write: 30
        },
        requester: createBrowserXhrRequester(),
        logger: createConsoleLogger(LogLevelEnum.Error),
        responsesCache: createInMemoryCache(),
        requestsCache: createInMemoryCache({
            serializable: !1
        }),
        hostsCache: createFallbackableCache({
            caches: [createBrowserLocalStorageCache({
                key: `4.1.0-${e}`
            }), createInMemoryCache()]
        }),
        userAgent: createUserAgent("4.1.0").add({
            segment: "Browser",
            version: "lite"
        }),
        authMode: AuthMode.WithinQueryParameters
    };
    return createSearchClient({
        ...a,
        ...r,
        methods: {
            search: multipleQueries,
            searchForFacetValues: multipleSearchForFacetValues,
            multipleQueries: multipleQueries,
            multipleSearchForFacetValues: multipleSearchForFacetValues,
            initIndex: e => t => initIndex(e)(t, {
                methods: {
                    search: search,
                    searchForFacetValues: searchForFacetValues
                }
            })
        }
    })
}

algoliasearch.version = "4.1.0";
export {algoliasearch as a};
//# sourceMappingURL=algolia-61d8d050.js.map

const CACHE_NAME = "controle-semanal-shell-v1";
const APP_SHELL = [
    "/index.html",
    "/manifest.json",
    "/offline-store.js",
    "/icons/icon-192.png",
    "/icons/icon-512.png",
    "/icons/icon-maskable.png",
    "/icons/icon-16.png",
    "/icons/fone.png"
];
const OFFLINE_CDN_HOSTS = new Set(["cdn.tailwindcss.com", "cdnjs.cloudflare.com"]);

self.addEventListener("install", event => {
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => cache.addAll(APP_SHELL))
            .then(() => self.skipWaiting())
    );
});

self.addEventListener("activate", event => {
    event.waitUntil(
        caches.keys()
            .then(keys => Promise.all(
                keys.filter(key => key.startsWith("controle-semanal-shell-") && key !== CACHE_NAME)
                    .map(key => caches.delete(key))
            ))
            .then(() => self.clients.claim())
    );
});

self.addEventListener("fetch", event => {
    const request = event.request;
    if (request.method !== "GET") return;

    const url = new URL(request.url);
    if (url.origin === self.location.origin &&
        (url.pathname.startsWith("/finace") || url.pathname.startsWith("/api/users"))) {
        return; // API and session responses are never cached.
    }

    const trustedCdn = OFFLINE_CDN_HOSTS.has(url.hostname);
    if (url.origin !== self.location.origin && !trustedCdn) return;

    if (request.mode === "navigate") {
        event.respondWith(
            fetch(request)
                .then(response => {
                    if (response.ok) {
                        caches.open(CACHE_NAME).then(cache => cache.put(request, response.clone()));
                    }
                    return response;
                })
                .catch(async () => (await caches.match(request)) ||
                    (await caches.match("/index.html")) || Response.error())
        );
        return;
    }

    const cachedResponse = caches.match(request);
    const networkResponse = fetch(request).then(response => {
        if (response.ok || response.type === "opaque") {
            caches.open(CACHE_NAME).then(cache => cache.put(request, response.clone()));
        }
        return response;
    });
    event.waitUntil(networkResponse.catch(() => {}));
    event.respondWith(
        cachedResponse.then(cached => cached || networkResponse)
            .catch(() => Response.error())
    );
});
const OfflineStore = (() => {
    const DB_NAME = "controle-semanal-offline";
    const DB_VERSION = 1;
    const STORE_NAME = "records";

    function open() {
        return new Promise((resolve, reject) => {
            const request = indexedDB.open(DB_NAME, DB_VERSION);
            request.onupgradeneeded = () => {
                const db = request.result;
                if (!db.objectStoreNames.contains(STORE_NAME)) {
                    const store = db.createObjectStore(STORE_NAME, { keyPath: "key" });
                    store.createIndex("owner", "owner", { unique: false });
                }
            };
            request.onsuccess = () => resolve(request.result);
            request.onerror = () => reject(request.error);
        });
    }

    async function write(action) {
        const db = await open();
        return new Promise((resolve, reject) => {
            const tx = db.transaction(STORE_NAME, "readwrite");
            const store = tx.objectStore(STORE_NAME);
            try { action(store); } catch (error) { db.close(); reject(error); return; }
            tx.oncomplete = () => { db.close(); resolve(); };
            tx.onerror = () => { db.close(); reject(tx.error); };
            tx.onabort = () => { db.close(); reject(tx.error || new Error("IndexedDB transaction aborted")); };
        });
    }

    async function all(owner) {
        const db = await open();
        return new Promise((resolve, reject) => {
            const tx = db.transaction(STORE_NAME, "readonly");
            const request = tx.objectStore(STORE_NAME).index("owner").getAll(owner);
            request.onsuccess = () => resolve(request.result || []);
            request.onerror = () => reject(request.error);
            tx.oncomplete = () => db.close();
            tx.onerror = () => { db.close(); reject(tx.error); };
        });
    }

    const makeKey = (owner, id) => owner + ":" + id;

    async function replaceSnapshot(owner, transactions) {
        const existing = await all(owner);
        await write(store => {
            existing.filter(row => row.status === "synced").forEach(row => store.delete(row.key));
            transactions.forEach(item => store.put({
                key: makeKey(owner, item.clientId || ("server-" + item.id)),
                owner,
                status: "synced",
                item
            }));
        });
    }

    async function enqueue(owner, payload) {
        const clientId = (crypto.randomUUID ? crypto.randomUUID() :
            Date.now().toString(36) + "-" + Math.random().toString(36).slice(2));
        const item = { ...payload, clientId, id: "local-" + clientId };
        const row = { key: makeKey(owner, clientId), owner, status: "pending-create", item };
        await write(store => store.put(row));
        return row;
    }

    async function enqueueDelete(owner, id) {
        const rows = await all(owner);
        const row = rows.find(candidate => candidate.item.id === id || candidate.item.clientId === id);
        if (!row) {
            await write(store => store.put({
                key: makeKey(owner, "delete-" + id),
                owner,
                status: "pending-delete",
                item: { id }
            }));
            return true;
        }
        if (row.status === "pending-create") {
            await write(store => store.delete(row.key));
        } else {
            await write(store => store.put({ ...row, status: "pending-delete" }));
        }
        return true;
    }

    async function update(row) {
        await write(store => store.put(row));
    }

    async function remove(owner, id) {
        const rows = await all(owner);
        const row = rows.find(candidate => candidate.item.id === id || candidate.item.clientId === id);
        if (row) await write(store => store.delete(row.key));
    }

    return { all, replaceSnapshot, enqueue, enqueueDelete, update, remove };
})();

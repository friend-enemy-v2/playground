const $ = (id) => document.getElementById(id);
const status = $("status");

function token() {
    return localStorage.getItem("mini-sns-token");
}

async function api(path, options = {}) {
    const headers = {"Content-Type": "application/json", ...(options.headers || {})};

    if (token()) {
        headers.Authorization = `Bearer ${token()}`;
    }

    const response = await fetch(path, {...options, headers});
    const text = await response.text();

    if (!response.ok) {
        throw new Error(`${response.status} ${text || response.statusText}`);
    }

    return text ? JSON.parse(text) : null;
}

async function authenticate(mode) {
    const body = {
        username: $("username").value,
        password: $("password").value
    };

    if (mode === "register") {
        body.displayName = $("displayName").value;
    }

    const result = await api(`/api/auth/${mode}`, {
        method: "POST",
        body: JSON.stringify(body)
    });

    localStorage.setItem("mini-sns-token", result.token);
    status.textContent = `${mode} OK`;
    await loadTimeline();
}

async function createPost() {
    await api("/api/posts", {
        method: "POST",
        body: JSON.stringify({body: $("postBody").value})
    });
    $("postBody").value = "";
    await loadTimeline();
}

async function relation(relationName, method) {
    const username = $("targetUser").value;
    await api(`/api/users/${encodeURIComponent(username)}/${relationName}`, {method});
    status.textContent = `${method} ${relationName} ${username}: OK`;
    await loadTimeline();
}

async function loadTimeline() {
    const posts = await api("/api/timeline?limit=50&offset=0");
    $("timeline").replaceChildren(...posts.map((post) => {
        const article = document.createElement("article");
        article.className = "post";

        const meta = document.createElement("div");
        meta.className = "meta";
        meta.textContent = `@${post.username} · ${new Date(post.createdAt).toLocaleString()}`;

        const body = document.createElement("div");
        body.textContent = post.body;

        article.append(meta, body);
        return article;
    }));
}

function run(action) {
    action().catch((error) => {
        status.textContent = error.message;
    });
}

$("register").onclick = () => run(() => authenticate("register"));
$("login").onclick = () => run(() => authenticate("login"));
$("logout").onclick = () => {
    localStorage.removeItem("mini-sns-token");
    status.textContent = "logout OK";
    $("timeline").replaceChildren();
};
$("post").onclick = () => run(createPost);
$("reload").onclick = () => run(loadTimeline);

document.querySelectorAll("[data-relation]").forEach((button) => {
    button.onclick = () => run(() => relation(button.dataset.relation, button.dataset.method));
});

if (token()) {
    run(loadTimeline);
}

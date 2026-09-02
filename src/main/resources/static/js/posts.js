const PREVIEW_LENGTH = 120;

function formatDate(isoString) {
    const date = new Date(isoString);
    if (Number.isNaN(date.getTime())) {
        return '';
    }
    return date.toLocaleString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function toPreview(content) {
    if (content.length <= PREVIEW_LENGTH) {
        return content;
    }
    return content.slice(0, PREVIEW_LENGTH) + '...';
}

function showStatusMessage(message, isError) {
    const statusEl = document.getElementById('status-message');
    statusEl.textContent = message;
    statusEl.classList.toggle('error', Boolean(isError));
    statusEl.hidden = false;
}

function hideStatusMessage() {
    const statusEl = document.getElementById('status-message');
    statusEl.hidden = true;
}

function createPostItem(post) {
    const li = document.createElement('li');
    li.className = 'post-item';

    const title = document.createElement('h2');
    title.className = 'post-title';
    title.textContent = post.title;

    const meta = document.createElement('p');
    meta.className = 'post-meta';
    meta.textContent = `${post.authorNickname} · ${formatDate(post.createdAt)}`;

    const preview = document.createElement('p');
    preview.className = 'post-preview';
    preview.textContent = toPreview(post.content);

    li.append(title, meta, preview);
    return li;
}

function renderPosts(posts) {
    const listEl = document.getElementById('post-list');
    listEl.innerHTML = '';

    if (posts.length === 0) {
        showStatusMessage('게시글이 없습니다.', false);
        return;
    }

    hideStatusMessage();
    const fragment = document.createDocumentFragment();
    posts.forEach(post => fragment.appendChild(createPostItem(post)));
    listEl.appendChild(fragment);
}

async function loadPosts() {
    showStatusMessage('불러오는 중...', false);
    try {
        const posts = await getPosts();
        renderPosts(posts);
    } catch (error) {
        showStatusMessage(error.message || '게시글을 불러오는 중 오류가 발생했습니다.', true);
    }
}

document.addEventListener('DOMContentLoaded', loadPosts);

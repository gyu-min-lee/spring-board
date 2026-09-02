async function getPosts() {
    const response = await fetch('/posts', {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    });

    if (!response.ok) {
        throw new Error(`게시글을 불러오지 못했습니다. (status: ${response.status})`);
    }

    return response.json();
}

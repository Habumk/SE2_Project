let stompClient = null;
let activeRoomId = null;
let micStates = {}; // userId -> boolean
let lastParticipants = [];

function getUserId() {
    return window.KLEARN_USER_ID || null;
}

function setMicButtonLabel(micOn) {
    const btn = document.getElementById('mic-toggle-btn');
    if (!btn) return;
    btn.textContent = '🎤 Mic: ' + (micOn ? 'ON' : 'OFF');
}

function renderParticipants(participants) {
    const container = document.getElementById('participants-container');
    if (!container) return;

    const list = Array.isArray(participants) ? participants : [];
    container.innerHTML = list.map(p => {
        const uid = p.userId;
        const isOn = uid != null && micStates[uid] === true;
        const name = p.userName || ('User #' + uid);
        return `
            <div style="display:flex; align-items:center; justify-content:space-between; padding:12px 14px; border:1px solid var(--border); border-radius:12px; background: var(--bg-card);">
                <div style="font-weight:700; color: var(--text-primary);">${name}</div>
                <div style="color: var(--text-secondary); font-weight:600;">${isOn ? '🎙️ Đang nói' : '🔇 Tắt mic'}</div>
            </div>
        `;
    }).join('');
}

function ensureStompConnection() {
    if (stompClient) return Promise.resolve();

    return new Promise((resolve) => {
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, () => {
            resolve();
        }, (err) => {
            console.error('STOMP connect error:', err);
            if (typeof showToast === 'function') {
                showToast('Không kết nối WebSocket', 'error');
            }
            resolve();
        });
    });
}

function subscribeRoomTopics(roomId) {
    if (!stompClient) return;

    stompClient.subscribe(`/topic/speaking-room/${roomId}/participants`, (frame) => {
        try {
            const participants = JSON.parse(frame.body);
            lastParticipants = Array.isArray(participants) ? participants : [];
            renderParticipants(lastParticipants);
        } catch (e) {
            console.error(e);
        }
    });

    stompClient.subscribe(`/topic/speaking-room/${roomId}/mic`, (frame) => {
        try {
            const msg = JSON.parse(frame.body);
            if (msg && msg.userId != null) {
                micStates[msg.userId] = !!msg.micOn;
                // Re-render to reflect mic status immediately.
                renderParticipants(lastParticipants);
            }
        } catch (e) {
            console.error(e);
        }
    });
}

async function joinRoom(roomId) {
    activeRoomId = roomId;
    micStates = {};
    lastParticipants = [];

    document.getElementById('current-room-id').textContent = roomId;
    document.getElementById('current-room-view').style.display = 'block';

    const participantsContainer = document.getElementById('participants-container');
    if (participantsContainer) participantsContainer.innerHTML = 'Đang tải người tham gia...';

    await ensureStompConnection();
    subscribeRoomTopics(roomId);

    const res = await fetch(`/api/speaking-rooms/${roomId}/join`, { method: 'POST' });
    if (!res.ok) {
        const text = await res.text().catch(() => '');
        alert('Không thể tham gia phòng: ' + text);
        activeRoomId = null;
        micStates = {};
        lastParticipants = [];
        document.getElementById('current-room-view').style.display = 'none';
        return;
    }

    setMicButtonLabel(false);
}

async function leaveCurrentRoom() {
    if (!activeRoomId) return;
    const roomId = activeRoomId;
    await fetch(`/api/speaking-rooms/${roomId}/leave`, { method: 'POST' }).catch(() => {});

    activeRoomId = null;
    micStates = {};
    document.getElementById('current-room-view').style.display = 'none';
}

async function fetchAndRenderRooms() {
    const res = await fetch('/api/speaking-rooms');
    if (!res.ok) throw new Error('Failed to load rooms');

    const payload = await res.json();
    const rooms = payload && payload.data ? payload.data : [];

    const container = document.getElementById('speaking-rooms-container');
    container.innerHTML = rooms.length === 0
        ? '<div style="color: var(--text-secondary);">Chưa có phòng nào.</div>'
        : rooms.map(r => `
            <div class="room-card" style="margin-bottom:12px;" >
                <div class="room-card-header">
                    <span class="room-card-name">${r.name || ('Phòng #' + r.roomId)}</span>
                    <span class="room-card-code">${r.roomId}</span>
                </div>
                <div class="room-card-desc">Tối đa ${r.maxParticipants ?? 10} người</div>
                <div class="room-card-footer">
                    <button class="btn btn-primary" type="button" onclick="joinFromList(${r.roomId})">Tham gia</button>
                </div>
            </div>
        `).join('');
}

function joinFromList(roomId) {
    joinRoom(roomId);
}

async function toggleMic() {
    if (!activeRoomId) return;
    const userId = getUserId();
    if (userId == null) return;

    const next = !(micStates[userId] === true);
    micStates[userId] = next;
    setMicButtonLabel(next);

    if (stompClient && stompClient.connected) {
        stompClient.send(
            `/app/speaking-room/${activeRoomId}/mic`,
            {},
            JSON.stringify({ userId: userId, micOn: next })
        );
    }
}

function initSpeakingRoom() {
    fetchAndRenderRooms().catch(() => {
        const container = document.getElementById('speaking-rooms-container');
        if (container) container.textContent = 'Không thể tải danh sách phòng.';
    });
    window.joinFromList = joinFromList;
    window.leaveCurrentRoom = leaveCurrentRoom;
    window.toggleMic = toggleMic;
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initSpeakingRoom);
} else {
    initSpeakingRoom();
}


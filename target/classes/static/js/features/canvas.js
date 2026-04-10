// ---- Canvas ----
function initCanvas() {
    const canvas = document.getElementById('writingCanvas');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    const rect = canvas.getBoundingClientRect();

    function getPos(e) {
        const touch = e.touches ? e.touches[0] : e;
        const r = canvas.getBoundingClientRect();
        return { x: (touch.clientX - r.left) * (canvas.width / r.width), y: (touch.clientY - r.top) * (canvas.height / r.height) };
    }

    function startDraw(e) {
        e.preventDefault();
        state.isDrawing = true;
        const pos = getPos(e);
        ctx.beginPath();
        ctx.moveTo(pos.x, pos.y);
    }

    function draw(e) {
        if (!state.isDrawing) return;
        e.preventDefault();
        const pos = getPos(e);
        ctx.lineWidth = 4;
        ctx.lineCap = 'round';
        ctx.strokeStyle = '#60a5fa';
        ctx.lineTo(pos.x, pos.y);
        ctx.stroke();
    }

    function endDraw(e) {
        if (state.isDrawing) {
            state.isDrawing = false;
            state.canvasHistory.push(canvas.toDataURL());
        }
    }

    canvas.addEventListener('mousedown', startDraw);
    canvas.addEventListener('mousemove', draw);
    canvas.addEventListener('mouseup', endDraw);
    canvas.addEventListener('mouseleave', endDraw);
    canvas.addEventListener('touchstart', startDraw, { passive: false });
    canvas.addEventListener('touchmove', draw, { passive: false });
    canvas.addEventListener('touchend', endDraw);
}

function clearCanvas() {
    const canvas = document.getElementById('writingCanvas');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    state.canvasHistory = [];
}

function undoCanvas() {
    const canvas = document.getElementById('writingCanvas');
    if (!canvas || state.canvasHistory.length === 0) return;
    state.canvasHistory.pop();
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    if (state.canvasHistory.length > 0) {
        const img = new Image();
        img.onload = () => ctx.drawImage(img, 0, 0);
        img.src = state.canvasHistory[state.canvasHistory.length - 1];
    }
}
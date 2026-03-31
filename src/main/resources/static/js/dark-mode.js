const KLEARN_THEME_KEY = 'klearn_theme';

function initDarkMode() {
    const saved = localStorage.getItem(KLEARN_THEME_KEY);
    const serverTheme = window.KLEARN_INITIAL_THEME || null;
    const mediaTheme = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
    // Priority:
    // 1) localStorage (user already toggled in this browser)
    // 2) server theme preference (persisted to DB)
    // 3) system preference
    const theme = saved ? saved : (serverTheme ? serverTheme : mediaTheme);
    document.documentElement.setAttribute('data-theme', theme);
}

async function syncThemeToServer(theme) {
    try {
        await fetch('/api/users/theme', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ theme })
        });
    } catch (e) {
        // Silent fail: theme still toggles locally.
        console.warn('Failed to sync theme:', e);
    }
}

function toggleDarkMode() {
    const current = document.documentElement.getAttribute('data-theme') === 'dark' ? 'dark' : 'light';
    const next = current === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', next);
    localStorage.setItem(KLEARN_THEME_KEY, next);
    syncThemeToServer(next);
}

// Expose for inline onclick
window.toggleDarkMode = toggleDarkMode;

document.addEventListener('DOMContentLoaded', () => {
    initDarkMode();
});


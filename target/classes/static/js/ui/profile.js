// Profile Dropdown
function toggleProfileDropdown() {
    const dropdown = document.getElementById('profileDropdown');
    if (dropdown) dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
}

function handleLogout() {
    document.getElementById('logoutModal').style.display = 'flex';
}

function closeLogoutModal() {
    document.getElementById('logoutModal').style.display = 'none';
}

function isGuest() {
    return false; // No guest mode in Spring Boot version
}

// Close dropdown when clicking outside
document.addEventListener('click', (e) => {
    const wrap = document.getElementById('headerAvatarWrap');
    const dropdown = document.getElementById('profileDropdown');
    if (wrap && dropdown && dropdown.style.display === 'block') {
        if (!wrap.contains(e.target) && !dropdown.contains(e.target)) {
            dropdown.style.display = 'none';
        }
    }
});

window.toggleProfileDropdown = toggleProfileDropdown;
window.handleLogout = handleLogout;
window.closeLogoutModal = closeLogoutModal;
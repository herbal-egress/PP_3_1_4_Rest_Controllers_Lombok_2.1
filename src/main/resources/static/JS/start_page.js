// Функция загрузки списка пользователей
async function loadUsers() {
    try {
        const response = await fetch('/api/user');
        const userDTOs = await response.json();
        const tbody = document.getElementById('usersTableBody');
        tbody.innerHTML = '';
        userDTOs.forEach(userDTO => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${userDTO.id}</td>
                <td>${userDTO.name}</td>
                <td>${userDTO.age}</td>
                <td>${userDTO.email}</td>
                <td>${userDTO.roles.map(role => role.name.replace("ROLE_", "")).join(', ')}</td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error('Ошибка при загрузке пользователей:', error);
    }
}

// Инициализация после загрузки DOM
document.addEventListener("DOMContentLoaded", function () {
    loadUsers();
});
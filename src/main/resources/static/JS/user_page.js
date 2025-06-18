<!-- Скрипт для заполнения шапки и таблицы: -->
    document.addEventListener("DOMContentLoaded", function () {
    // Загрузка данных текущего пользователя при загрузке страницы
    loadCurrentUser();

    // Функция загрузки данных текущего пользователя и заполения шапки
    async function loadCurrentUser() {
    try {
    const response = await fetch('/api/user/current');
    const userDTO = await response.json();
    document.getElementById('user-email').textContent = userDTO.email;
    document.getElementById('user-roles').textContent = 'with roles: ' +
    userDTO.roles.map(role => role.name.replace("ROLE_", "")).join(', ');
    const tbody = document.getElementById('currentUserTable');
    tbody.innerHTML = '';
    const row = document.createElement('tr');
    row.innerHTML = `
                <td>${userDTO.id}</td>
                <td>${userDTO.name}</td>
                <td>${userDTO.age}</td>
                <td>${userDTO.email}</td>
                <td>${userDTO.roles.map(role => role.name.replace("ROLE_", "")).join(', ')}</td>
            `;
    tbody.appendChild(row);
} catch (error) {
    console.error('Ошибка загрузки текущего пользователя:', error);
}
}
});
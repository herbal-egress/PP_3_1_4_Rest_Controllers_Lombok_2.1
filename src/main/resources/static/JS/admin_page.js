document.addEventListener("DOMContentLoaded", function () {
    // Загрузка текущего пользователя и списка пользователей при загрузке страницы
    loadCurrentUser();
    loadUsers();

    // Инициализация чекбоксов ролей при переходе на вкладку регистрации
    document.getElementById('table2-tab').addEventListener('click', async function () {
        createRegistrationRoleCheckboxes(await loadAllRoles());
    });

    // Функция загрузки текущего пользователя и заполнение шапки
    async function loadCurrentUser() {
        try {
            const response = await fetch('/api/user/current');
            const userDTO = await response.json();
            document.getElementById('user-email').textContent = userDTO.email;
            document.getElementById('user-roles').textContent = 'with roles: ' +
                userDTO.roles.map(role => role.name.replace("ROLE_", "")).join(', ');
        } catch (error) {
            console.error('Ошибка загрузки текущего пользователя:', error);
        }
    }

    // Функция загрузки всех пользователей
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
                    <td>
                        <button class="btn btn-edit" data-bs-toggle="modal" data-bs-target="#editModal"
                            data-user-id="${userDTO.id}" data-user-name="${userDTO.name}" data-user-age="${userDTO.age}"
                            data-user-email="${userDTO.email}"
                            data-user-roles="${userDTO.roles.map(role => role.id).join(',')}">
                            Edit
                        </button>
                    </td>
                    <td>
                        <button class="btn btn-del" data-bs-toggle="modal" data-bs-target="#delUserModal"
                            data-user-id="${userDTO.id}" data-user-name="${userDTO.name}" data-user-age="${userDTO.age}"
                            data-user-email="${userDTO.email}">
                            Delete
                        </button>
                    </td>
                `;
                tbody.appendChild(row);
            });

            // Обработчики для кнопок Edit
            document.querySelectorAll('.btn-edit').forEach(button => {
                button.addEventListener('click', async function () {
                    const userId = this.getAttribute('data-user-id');
                    const userName = this.getAttribute('data-user-name');
                    const userAge = this.getAttribute('data-user-age');
                    const userEmail = this.getAttribute('data-user-email');
                    const userRoles = this.getAttribute('data-user-roles').split(',');

                    document.getElementById('editUserId').value = userId;
                    document.getElementById('editUsername').value = userName;
                    document.getElementById('editUserAge').value = userAge;
                    document.getElementById('editEmail').value = userEmail;

                    const allRoles = await loadAllRoles();
                    createRoleCheckboxes(document.getElementById('editRolesContainer'), allRoles, userRoles);
                });
            });

            // Обработчики для кнопок Delete
            document.querySelectorAll('.btn-del').forEach(button => {
                button.addEventListener('click', function () {
                    const userId = this.getAttribute('data-user-id');
                    const userName = this.getAttribute('data-user-name');
                    const userAge = this.getAttribute('data-user-age');
                    const userEmail = this.getAttribute('data-user-email');

                    document.getElementById('userDelId').value = userId;
                    document.getElementById('userDelName').value = userName;
                    document.getElementById('userDelAge').value = userAge;
                    document.getElementById('userDelEmail').value = userEmail;
                });
            });

        } catch (error) {
            console.error('Ошибка при загрузке пользователей:', error);
        }
    }

    // Функция загрузки всех ролей
    async function loadAllRoles() {
        try {
            const response = await fetch('/api/user/roles');
            return await response.json();
        } catch (error) {
            console.error('Ошибка при загрузке ролей:', error);
            return [];
        }
    }

    // Функция создания чекбоксов ролей для редактирования
    function createRoleCheckboxes(container, allRoles, userRoles) {
        container.innerHTML = '';
        allRoles.forEach(role => {
            const div = document.createElement('div');
            div.classList.add('form-check');

            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.id = `role-${role.id}`;
            checkbox.value = role.id;
            checkbox.classList.add('form-check-input');
            if (userRoles.includes(role.id.toString())) {
                checkbox.checked = true;
            }

            const label = document.createElement('label');
            label.htmlFor = `role-${role.id}`;
            label.classList.add('form-check-label');
            label.textContent = role.name.replace("ROLE_", "");

            div.appendChild(checkbox);
            div.appendChild(label);
            container.appendChild(div);
        });
    }

    // Функция создание чекбоксов ролей для регистрации
    function createRegistrationRoleCheckboxes(allRoles) {
        const container = document.getElementById('rolesContainer');
        container.innerHTML = '';

        allRoles.forEach(role => {
            const div = document.createElement('div');
            div.classList.add('form-check');

            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.id = `reg-role-${role.id}`;
            checkbox.value = role.id;
            checkbox.classList.add('form-check-input');

            const label = document.createElement('label');
            label.htmlFor = `reg-role-${role.id}`;
            label.classList.add('form-check-label');
            label.textContent = role.name.replace("ROLE_", "");

            div.appendChild(checkbox);
            div.appendChild(label);
            container.appendChild(div);
        });
    }

    // Обработчик изменений юзера
    document.getElementById('saveChangesUser').addEventListener('click', async function () {
        const userId = document.getElementById('editUserId').value;
        const userData = {
            name: document.getElementById('editUsername').value,
            age: document.getElementById('editUserAge').value,
            email: document.getElementById('editEmail').value,
            password: document.getElementById('editPassword').value,
            roles: Array.from(document.querySelectorAll('#editRolesContainer input[type="checkbox"]:checked'))
                .map(checkbox => ({
                    id: parseInt(checkbox.value),
                    name: 'ROLE_' + checkbox.nextElementSibling.textContent.trim()
                }))
        };

        try {
            const response = await fetch(`/api/admin/id?id=${userId}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            if (response.ok) {
                loadUsers(); // Обновляем таблицу без перезагрузки страницы
                bootstrap.Modal.getInstance(document.getElementById('editModal')).hide();
            } else {
                alert('Ошибка при обновлении пользователя!');
            }
        } catch (error) {
            console.error('Ошибка:', error);
            alert('Произошла ошибка при обновлении данных');
        }
    });

    // Обработчик удаления юзера
    document.getElementById('confirmDelete').addEventListener('click', async function () {
        const userId = document.getElementById('userDelId').value;

        try {
            const response = await fetch(`/api/admin/id?id=${userId}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                loadUsers(); // Обновляем таблицу без перезагрузки страницы
                bootstrap.Modal.getInstance(document.getElementById('delUserModal')).hide();
            } else {
                alert('Ошибка при удалении пользователя!');
            }
        } catch (error) {
            console.error('Ошибка:', error);
            alert('Произошла ошибка при удалении пользователя');
        }
    });

    // Обработчик регистрации нового юзера
    document.getElementById('registerBtn').addEventListener('click', async function () {
        const userData = {
            name: document.getElementById('new_name').value,
            age: document.getElementById('new_age').value,
            email: document.getElementById('new_email').value,
            password: document.getElementById('new_password').value,
            roles: Array.from(document.querySelectorAll('#rolesContainer input[type="checkbox"]:checked'))
                .map(checkbox => ({
                    id: parseInt(checkbox.value),
                    name: 'ROLE_' + checkbox.nextElementSibling.textContent.trim()
                }))
        };

        try {
            const response = await fetch('/api/admin', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            if (response.ok) {
                document.getElementById('registerForm').reset();
                loadUsers(); // Обновляем таблицу без перезагрузки страницы
                document.getElementById('table1-tab').click();
            } else {
                alert('Ошибка при регистрации пользователя!');
            }
        } catch (error) {
            console.error('Ошибка:', error);
            alert('Произошла ошибка при регистрации');
        }
    });
    // убираю ноль из поля возраста при добавлении юзера
    const ageInput = document.getElementById("new_age");
    if (ageInput && ageInput.value === "0") {
        ageInput.value = "";
    }
});



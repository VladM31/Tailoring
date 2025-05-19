const USER_ACTIVE_USER = 'https://vladyslav-bhe6d5e4arccerfv.westeurope-01.azurewebsites.net/api/v1/users/{id}/active';

async function updateUserActive(userId, active) {
    const formDataPut = new FormData();
    formDataPut.append('active', active);

    const respond = await fetch(USER_ACTIVE_USER.replace('{id}', userId), {
        method: 'PUT',
        body: formDataPut
    });

    if (respond.status > 299) {
        alert(await respond.text());
        return null;
    }

    return await respond.json();
}
const ORDER_STATUS_URL = 'http://localhost:8095/api/v1/orders/{id}/status';
const ORDER_STATUS_BUTTON = document.getElementById("order-status-button-id");
const CANCELLED_TEXT = 'CANCELLED';
const DONE_TEXT = 'DONE';
const CANCELLED_LOWWER_TEXT = 'Cancel';
const DONE_LOWWER_TEXT = 'Done';

async function changeOrderStatus() {

    const dialogAnswer = confirm('Are you sure?');

    if (dialogAnswer == false) {
        return;
    }

    const respond = await fetch(ORDER_STATUS_URL.replace('{id}', ORDER_ID), {
        method: 'PUT'
    });

    if (respond.status > 299) {
        alert(await respond.text());
        return;
    }

    const statusRespond = await respond.text();

    ORDER_STATUS_TEXT.textContent = titleCase(statusRespond).replaceAll('_', ' ');

    if (statusRespond == DONE_TEXT || statusRespond == CANCELLED_TEXT) {
        ORDER_STATUS_BUTTON.remove()
        return;
    }


    ORDER_STATUS_BUTTON.textContent = CANCELLED_LOWWER_TEXT;
}
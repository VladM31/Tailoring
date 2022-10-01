const ORDER_URL = 'http://localhost:8095/api/v1/orders';
const FROM_ADMIN = document.getElementById('admin-update-order-form-id');

const ORDER_PAYMENT_STATUS_TEXT = document.getElementById("order-payment-status-id");
const ORDER_PRICE_TEXT = document.getElementById("order-price-id");
const ORDER_END_DATE_TEXT = document.getElementById("order-end-date-id");


function getFormData() {
    const theFormData = new FormData();
    for (let i = 0; i < FROM_ADMIN.childElementCount; i++) {
        if (FROM_ADMIN[i].name) {
            theFormData.append(FROM_ADMIN[i].name,
                FROM_ADMIN[i].value.length === 0 ? null : FROM_ADMIN[i].value);
        }
    }
    return theFormData;
}

async function sendEditOrderForm() {
    const theFormData = getFormData();
    const respond = await fetch(ORDER_URL + "/" + ORDER_ID, {
        method: 'PUT',
        body: theFormData
    });

    if (respond.status > 299) {
        alert(await respond.text());
        return null;
    }

    return await respond.json();
}

async function getOrder() {
    const respond = await fetch(`${ORDER_URL}?orderId=${ORDER_ID}`);

    if (respond.status > 299) {
        alert("Order not found")
        return null;
    }

    const orders = await respond.json();

    return await orders.orderList[0];
}

async function editOrder() {
    const answer = await sendEditOrderForm();

    if (answer === null) {
        return;
    }

    alert(`Update was ${answer ? 'Successful' : 'Not successful'}`);

    const order = await getOrder();

    if (order == null) {
        return;
    }

    updateDescribeOrder(order);
}


function updateDescribeOrder(order) {
    ORDER_PAYMENT_STATUS_TEXT.textContent = titleCase(order.paymentStatus).replace('_', ' ');
    ORDER_STATUS_TEXT.textContent = titleCase(order.status).replaceAll('_', ' ');
    ORDER_PRICE_TEXT.textContent = order.cost;
    ORDER_END_DATE_TEXT.textContent = order.endDate.replaceAll('-', '.');
}
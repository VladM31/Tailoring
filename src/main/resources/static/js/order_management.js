const ORDERS_URL = 'http://localhost:8095/api/v1/orders';
const ORDER_CONTAINER = document.getElementById('order-container-id');
const ORDER_DATA_PAGE_URL = 'http://localhost:8095/api/v1/orders/{id}/show';

const PAGE = getPage('DATE_OF_CREATION');

async function getOrders() {
    const formData = await getFormDataFromFilter();
    const parameters = formDataToUrlParameter(formData);

    const respond = await fetch(ORDERS_URL + parameters + PAGE.toParameters(parameters));

    if (respond.status > 299) {
        alert(await respond.text())
        return null;
    }

    const data = await respond.json();

    return data.orderList;
}

async function updateOrderContainer() {
    const orders = await getOrders();

    if (orders == null) {
        return;
    }

    putToContainer(orders);
}

function putToContainer(orders) {
    ORDER_CONTAINER.innerHTML = '';
    for (const order of orders) {
        ORDER_CONTAINER.innerHTML += toDiv(order);
    }
}

function toDiv(order) {
    return `
    <div class="order-box">
        <h2>Number: ${order.id}</h2>
        <p>Material : <samp style="color:#520974;">${order.material.name}</samp></p>
        <p>Color : <samp style="color:#520974;">${order.color.name}</samp></p>
        <p>State : <samp style="color:#520974;">${titleCase(order.status).replaceAll('_', ' ')}</samp></p>
        <p>Payment state : <samp style="color:#520974;">${titleCase(order.paymentStatus).replaceAll('_', ' ')}</samp></p>
        <p>Address for send : <samp style="color:#520974;">${order.addressForSend}</samp></p>
        <p>Price : <samp style="color:#520974;">${order.cost} UAH</samp></p>
        <p>Count : <samp style="color:#520974;">${order.countOfOrder}</samp></p>
        <p>Date of creation : <samp style="color:#520974;">
        ${order.dateOfCreation.replaceAll('T', ' ').substring(0, 19)}
        </samp></p>
        <p>End date : <samp style="color:#520974;">${order.endDate}</samp></p>
        <p>Is from template : <samp style="color:#520974;">${order.endDate}</samp></p>
        <p>Images count : <samp style="color:#520974;">${order.images.length}</samp></p>
        <p>Past size count : <samp style="color:#520974;">${order.partSizes.length}</samp></p>
        <p>Customer firstname : <samp style="color:#520974;">
        ${order.clientOrder.firstname}</samp></p>
        <p>Customer phone number : <samp style="color:#520974;"> 
        ${order.clientOrder.phoneNumber}</samp></p>
        <p>Customer gender : <samp style="color:#520974;">
        ${order.clientOrder.isMale ? 'Man' : 'Woman'}
        </samp></p>
        <p>Customer country : <samp style="color:#520974;">
        ${order.clientOrder.country}
        </samp></p>
        <p>Customer city : <samp style="color:#520974;">
        ${order.clientOrder.city}
        </samp></p>
        <div class="link-button-box">
            <a href="${ORDER_DATA_PAGE_URL.replace('{id}', order.id)}">
                <button type="button" class="link-button">Details</button>
            </a>
        </div>

    </div>
    `;
}
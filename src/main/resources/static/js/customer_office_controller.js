const CUSTOMER_ORDER_URL = 'https://vladyslav-bhe6d5e4arccerfv.westeurope-01.azurewebsites.net/api/v1/orders/customer';
const IMAGE_ORDER_DIRECTORY = getValueWithRemoveElement('image-order-derictory-id');
const IMAGE_ORDER_FROM_TEMPLATE_DIRECTORY = getValueWithRemoveElement('image-order-from-template-directory-id');
const SHOW_ORDER_URL = 'https://vladyslav-bhe6d5e4arccerfv.westeurope-01.azurewebsites.net/api/v1/orders/{orderId}/show';

const ORDER_OFFICE = document.getElementById('short-orders-container-id');
const PAGE = getPage('DATE_OF_CREATION');

const DEFAULT_COLOR = 'yellow';
const CANCEL_COLOR = 'red';
const DONE_COLOR = 'green';

const STATUS_COLORS = {
    map: new Map(),
    getColor: function (status) {
        const color = this.map.get(status);

        if (color == undefined) {
            return DEFAULT_COLOR;
        }
        return color;
    }
}

STATUS_COLORS.map.set('CANCELLED', CANCEL_COLOR);
STATUS_COLORS.map.set('DONE', DONE_COLOR);

async function getCustomerOrders() {
    const formData = await getFormDataFromFilter();

    const paramenter = formDataToUrlParameter(formData);

    const respond = await fetch(CUSTOMER_ORDER_URL + paramenter + PAGE.toParameters(paramenter));

    if (respond.status > 299) {
        alert('Orders not found')
        return null;
    }

    return await respond.json();
}

async function updateOrderContainer() {
    const orders = await getCustomerOrders();

    if (orders == null) {
        return;
    }

    ORDER_OFFICE.innerHTML = '';

    for (const order of orders) {
        ORDER_OFFICE.innerHTML += toOrderDiv(order);
    }
}


function toOrderDiv(order) {
    return "" +
        `
    <div class="short-order-box">
        <div class="state-number-dare-order-box">
        <div class="color-state-box" style='background-color: ${STATUS_COLORS.getColor(order.status)}'></div>
        <div class="number-dare-order-box">
            <p>№ ${order.id} from ${order.dateOfCreation.replaceAll('-', '.')}</p>
            <p>${titleCase(order.status.replaceAll('_', ' '))}</p>
        </div>
        </div>

        <div class="payment-state-and-price-boxs">
            <span>Payment status</span>
            <p>${titleCase(order.paymentStatus).replaceAll('_', ' ')}</p>
        </div>

        <div class="payment-state-and-price-boxs">
            <span>Order price</span>
            <p> ${order.cost} UAH</p>
        </div>

        <div class="img-and-more-details-box">
            <img src="${getImageUrl(order)}" alt="order image">
            <a href="${SHOW_ORDER_URL.replace('{orderId}', order.id)}">></a>
        </div>
    </div>
    `;
}

function getImageUrl(order) {
    return order.isFromTemplate ? `${IMAGE_ORDER_FROM_TEMPLATE_DIRECTORY}/${order.img}` : `${IMAGE_ORDER_DIRECTORY}/${order.img}`;
}
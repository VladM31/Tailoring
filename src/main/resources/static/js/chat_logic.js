const CHAT_RESOURCE = 'https://vladyslav-bhe6d5e4arccerfv.westeurope-01.azurewebsites.net/api/v1/order/{orderId}/comments';
const FIFTEEN_SECONDS = 15000;

const chatContainer = document.getElementById('chat-container-id');
const textBox = document.getElementById('text-box-id');

const userIdBoxId = 'user-id';
const ownerIdBoxId = 'order-owner-id';
const ownerNameBoxId = 'order-owner-name-id';
const orderBoxId = 'order-id';

const chatManager = {
    userId: null,
    isCustomer: null,
    ownerName: null,
    orderId: null
}

function startChat() {
    const userIdBox = document.getElementById(userIdBoxId);
    const ownerIdBox = document.getElementById(ownerIdBoxId);
    const ownerNameBox = document.getElementById(ownerNameBoxId);
    const orderId = document.getElementById(orderBoxId);

    chatManager.userId = userIdBox.value;
    chatManager.isCustomer = ownerIdBox.value === chatManager.userId;
    chatManager.ownerName = ownerNameBox.value;
    chatManager.orderId = orderId.value;

    userIdBox.remove();
    ownerIdBox.remove();
    ownerNameBox.remove();
    orderId.remove();
}

async function getComments() {
    const respond = await fetch(CHAT_RESOURCE.replace('{orderId}', chatManager.orderId));

    if (respond.status > 299) {
        alert(await respond.text())
        return [];
    }

    return await respond.json();
}

async function setComments() {
    chatContainer.innerHTML = '';

    const comments = await getComments();

    for (let i = 0; i < comments.length; i++) {
        chatContainer.innerHTML += commentToDiv(comments[i], i);
    }

    const lastComment = document.getElementById(`comment-scrole-text-id-${comments.length - 1}`)

    lastComment.scrollIntoView(true);
}


function commentToDiv(comment, id) {
    return `
    <div class="text-box" id="comment-scrole-text-id-${id}">
          <label for="">${showName(comment)}</label>
          <p>${comment.message}</p>
          <em>${comment.dateOfCreation.replace('T', ' ').substring(0, 19)}</em>
    </div>
    `;
}

function showName(comment) {
    if (chatManager.isCustomer) {
        return chatManager.userId == comment.userId ? 'You' : 'Administratior';
    }
    return chatManager.userId == comment.userId ? 'You' : chatManager.ownerName;
}

async function sendMessage() {

    if (textBox.value.length > 200 || isBlank(textBox.value)) {
        alert("Message more than 200 or is blank")
        return;
    }

    const answer = await sendToService(textBox.value);

    if (answer == false) {
        alert("Message wasn't send")
        return;
    }

    if (answer == null) {
        return;
    }

    textBox.value = '';

    await setComments();
}

async function sendToService(text) {
    const formData2 = new FormData();

    formData2.append('userId', chatManager.userId);
    formData2.append('tailoringOrderId', chatManager.orderId);
    formData2.append('message', text);


    const respond = await fetch(CHAT_RESOURCE.replace('{orderId}', chatManager.orderId), {
        method: 'POST',
        body: formData2
    });

    if (respond.status > 299) {
        alert(await respond.text() + "\nMessage wasn't send")
        return null;
    }

    return await respond.json();
}

setInterval(setComments, FIFTEEN_SECONDS);
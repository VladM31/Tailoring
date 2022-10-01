const HIDDED_ORDER_ID = document.getElementById('hidden-order-id');
const ORDER_ID = HIDDED_ORDER_ID.value;
HIDDED_ORDER_ID.remove();
const ORDER_STATUS_TEXT = document.getElementById("order-status-id");

// ******************* Hide elements ****************
function createReplaceObject(replaceElementId, hideElement) {
    let replaceObject = document.getElementById(replaceElementId);
    let hiddenObject = hideElement;
    return function () {
        replaceObject.replaceWith(hiddenObject);
        const temp = replaceObject;
        replaceObject = hiddenObject;
        hiddenObject = temp;
    }
}

function hideElementByClick(replaceElementId, hideElement, buttonId) {

    const toReplace = createReplaceObject(replaceElementId, hideElement);

    const button = document.getElementById(buttonId);

    button.addEventListener('click', toReplace);

    const showName = 'Show';
    const hideName = 'Hide';

    button.addEventListener('click', function (e) {
        e.target.innerHTML = e.target.innerHTML == hideName ? showName : hideName;
    })
}
// ******************* Hide elements ****************
function createReplaceObject(replaceElementId, hideElement) {
    var replaceObject = document.getElementById(replaceElementId);
    var hiddenObject = hideElement;
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
var generatorIds = 0;

const idPartSizePiece = 'part-size-box=the-id-';

const containerPartSizeBoxes = document.getElementById('order-part-size-box-conteiner-id');

const partSizeMainArray = [createPartSize()];

const addButton = `<button type="button" class="create-part-size-button" id="add-button-id"
onclick="createPartSizeBoxEvent()">Add</button>`;

const maxSize = 15;

const hiddenInputForPartSize = document.getElementById('part-size-value-id');

function getIdPartSizeBox(id) {
    return idPartSizePiece + id;
}

function createPartSize() {
    return {
        id: ++generatorIds,
        name: "",
        width: null,
        length: null,
        volumn: null,
        height: null
    }
}

function setValurToPartSizeObject(id, value, fieldName) {
    const partSize = getObjectById(partSizeMainArray, id);
    partSize[fieldName] = value;
}

function setPartSizeBoxesByPartSizeMainArray() {
    containerPartSizeBoxes.innerHTML = "";
    for (let i = 0; i < partSizeMainArray.length; i++) {
        addPartSizeBox(partSizeMainArray[i]);
    }
    containerPartSizeBoxes.innerHTML += addButton;
}

function addPartSizeBox(partSize) {
    containerPartSizeBoxes.innerHTML += createPartSizeBoxByObject(partSize);
}

function createPartSizeBoxByObject(partSize) {
    return `
        <div class="part-size-box" id="${getIdPartSizeBox(partSize.id)}">
            <div class="name-with-remove-utton-box">
                <input type="text" placeholder="Write part size name" value="${partSize.name}"
                 oninput="setValurToPartSizeObject(${partSize.id},this.value,'name')">
                <button type="button" onclick="removePartSizeBox('${getIdPartSizeBox(partSize.id)}',${partSize.id})">⁻</button>
            </div>
            <div class="part-size-input-container">
                <div class="part-size-input-box">
                    <label for="">Width <em>cm</em></label>
                    <input type="number" value="${partSize.width ?? ''}" oninput="setValurToPartSizeObject(${partSize.id},this.value,'width')">
                </div>
                <div class="part-size-input-box">
                    <label for="">Length <em>cm</em></label>
                    <input type="number" value="${partSize.length ?? ''}" oninput="setValurToPartSizeObject(${partSize.id},this.value,'length')">
                </div>
                <div class="part-size-input-box">
                    <label for="">Height <em>cm</em></label>
                    <input type="number" value="${partSize.height ?? ''}" oninput="setValurToPartSizeObject(${partSize.id},this.value,'height')">
                </div>
                <div class="part-size-input-box">
                    <label for="">Volumn <em>cm</em></label>
                    <input type="number" value="${partSize.volumn ?? ''}" oninput="setValurToPartSizeObject(${partSize.id},this.value,'volumn')">
                </div>
            </div>
        </div>             
    `
}

function removePartSizeBox(partSizeBoxId, partSizeObjectId) {
    const partSizeBox = document.getElementById(partSizeBoxId);
    partSizeBox.remove();

    const indexRemove = partSizeMainArray.indexOf(getObjectById(partSizeMainArray, partSizeObjectId));

    partSizeMainArray.splice(indexRemove, 1)
}

function createPartSizeBoxEvent() {
    if (partSizeMainArray.length === maxSize) {
        alert(`Sorry, but no more than ${maxSize} boxes`);
        return;
    }
    const partSizeObject = createPartSize();
    partSizeMainArray.push(partSizeObject);
    setPartSizeBoxesByPartSizeMainArray();
}


function partSizeArrayToJsonAndSetToInput() {
    hiddenInputForPartSize.value = JSON.stringify(partSizeMainArray);
}
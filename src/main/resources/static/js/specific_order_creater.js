const FILE_INPUT = document.getElementById('image_uploads');
const COLOR_INPUT = document.getElementById('color-id');
const MATERIAL_INPUT = document.getElementById('material-id');
const ADDRESS_INPUT = document.getElementById('address-for-send-id');
const COUNT_INPUT = document.getElementById('count-order-id');
const PART_SIZE_INPUT = document.getElementById('part-size-value-id');
const WISHES_INPUT = document.getElementById('wishes-order-id');

const MAX_COUNT = 2147483647;
const SAVE_SPECIFIC_ORDER_URL = 'http://localhost:8095/api/v1/orders/create/specific';
const OFFICE_URL = 'http://localhost:8095/api/v1/orders/customer/show';

function setFiles(formData) {
    const files = FILE_INPUT.files;

    if (files.length < 1 || files.length > 10) {
        alert("Files must be between 1 and 10");
        throw "Files must be between 1 and 10";
    }

    for (const file of files) {
        formData.append("uploadFiles", file, file.name);
    }
}

function setColor(formData) {
    formData.append("colorId", COLOR_INPUT.value);
}

function setMaterial(formData) {
    formData.append("materialId", MATERIAL_INPUT.value);
}

function setDeliveryAddress(formData) {
    const address = ADDRESS_INPUT.value;
    if (isBlank(address)) {
        alert("Delivery address mustn't be blank");
        throw "Delivery address mustn't be blank";
    }

    if (address.length < 2 || address.length > 200) {
        alert("Address length must be between 2 and 200");
        throw "Address length must be between 2 and 200";
    }

    formData.append("deliveryAddress", address);
}

function setCount(formData) {
    const count = COUNT_INPUT.value;

    console.log(count);

    if (count < 1 || count > MAX_COUNT) {
        alert("Count must be more than 0");
        throw "Count must be more than 0";
    }

    formData.append("count", count);
}

function setWishes(formData) {
    const wishes = WISHES_INPUT.value;

    if (wishes.length > 200) {
        alert("Wishes length must be between 0 and 2000");
        throw "Wishes length must be between 0 and 2000";
    }

    formData.append("wishes", wishes);
}

function setPartSize(formData) {
    const partSizeJson = PART_SIZE_INPUT.value;

    checkPartSize(partSizeJson);

    formData.append("partSizeJson", partSizeJson);
}

function checkPartSize(partSizeJson) {
    const partSizes = JSON.parse(partSizeJson);

    if (partSizes.length < 1 || partSizes.length > 10) {
        alert("Part sizes must be between 1 and 10");
        throw "Part sizes must be between 1 and 10";
    }

    for (const partSize of partSizes) {
        if (isBlank(partSize.name ?? '')) {
            alert("Part size name mustn't be blank");
            throw "Part size name mustn't be blank";
        }

        if (1 > (partSize.width ?? 2) && !isBlank("" + partSize.width)) {
            alert("Width less than 1");
            throw "Width less than 1";
        }

        if (1 > (partSize.length ?? 2) && !isBlank("" + partSize.length)) {
            alert("Length less than 1");
            throw "Length less than 1";
        }

        if (1 > (partSize.volumn ?? 2) && !isBlank("" + partSize.volumn)) {
            alert("Volume less than 1");
            throw "Volume less than 1";
        }

        if (1 > (partSize.height ?? 2) && !isBlank("" + partSize.height)) {
            alert("Height less than 1");
            throw "Height less than 1";
        }

        if (partSize.width == null
            && partSize.length == null
            && partSize.volumn == null
            && partSize.height == null) {
            alert("Size wasn't select. Name = " + partSize.name);
            throw "Size wasn't select. Name = " + partSize.name;
        }
    }
}

function getFormData() {
    const formData = new FormData();

    setFiles(formData);
    setColor(formData);
    setMaterial(formData);
    setDeliveryAddress(formData);
    setCount(formData);
    setWishes(formData);
    setPartSize(formData);

    return formData;
}

async function sendOrderForm() {
    const formData = getFormData();

    const respond = await fetch(SAVE_SPECIFIC_ORDER_URL, {
        method: 'POST',
        body: formData,
    })

    if (respond.status > 299) {
        alert(await respond.text());
        return;
    }

    window.location.replace(OFFICE_URL);
    window.location.href = OFFICE_URL;
}
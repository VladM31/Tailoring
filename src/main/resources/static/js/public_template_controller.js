const PUBLIC_TEMPLATE_URL = 'https://vladyslav-bhe6d5e4arccerfv.westeurope-01.azurewebsites.net/api/v1/templates/public';
const TEMPLATE_CONTAINER = document.getElementById('template-container-id');
const IMAGES_DIRECTORY_INPUT_ID = 'images-directory-id';

const PAGE = {
    offset: null,
    limit: null,
    desc: false,
    sortColumn: 'DATA_OF_CREATION',
    toParameters: function (url) {
        const parameters = [];

        if (this.offset != null) {
            parameters.push(`offset=${this.offset}`);
        }
        if (this.limit != null) {
            parameters.push(`limit=${this.limit}`);
        }
        parameters.push(`desc=${this.desc}`);
        parameters.push(`sortColumn=${this.sortColumn}`);
        return url.length == 1 ? parameters.join('&') : '&' + parameters.join('&');
    }
};

async function sortByColumn(name) {
    PAGE.sortColumn = name;
    await toFilter();
}

async function setDirection() {
    PAGE.desc = !PAGE.desc;
    await toFilter();
}

let photoDirectoryUrl = null;

async function toFilter() {
    const formData = await getFormDataFromFilter();
    const formDataUrl = formDataToUrlParameter(formData);

    const templates = await getTemplates(formDataUrl + PAGE.toParameters(formDataUrl));

    if (templates == null) {
        return;
    }

    if (photoDirectoryUrl == null) {
        photoDirectoryUrl = await getImagesDirectory();
    }

    toDivs(templates);
}

async function getTemplates(parameters) {
    const respond = await fetch(PUBLIC_TEMPLATE_URL + parameters);

    if (respond.status > 299) {
        alert("Not found");
        return null;
    }

    return (await (await respond.json()).templates);
}

function toDivs(templates) {
    TEMPLATE_CONTAINER.innerHTML = '';

    for (const template of templates) {

        TEMPLATE_CONTAINER.innerHTML += toTemplateDiv(template);
    }
}

function toTemplateDiv(template) {
    return `
            <div class="template-box">
                <p class="cost-lable">${template.cost} UAH</p>

                <a href="https://vladyslav-bhe6d5e4arccerfv.westeurope-01.azurewebsites.net/api/v1/orders/create/based-on-template/${template.id}" > <img src="${photoDirectoryUrl}/${template.frontImage}" alt="template photo"></a>

                <div class="material-and-colors-select-box">
                    <select class="select-template">
                        <option value="" disabled selected>Materials</option>
                        ${toOptionDisabled(template.materialsName)}
                    </select>
                    <select class="select-template">
                        <option value="" disabled selected>Colors</option>
                        ${toOptionDisabled(template.colorsName)}
                    </select>
                </div>

                <p class="template-name">${template.name}</p>
            </div>
    `
}

function toOptionDisabled(stuffs) {
    let options = '';

    for (const stuff of stuffs) {
        options += ` <option disabled>${stuff}</option>`;
    }

    return options;
}

async function getImagesDirectory() {
    const imagesDirectoryInput = document.getElementById(IMAGES_DIRECTORY_INPUT_ID);

    const url = imagesDirectoryInput.value;

    imagesDirectoryInput.value = '';

    return url;
}
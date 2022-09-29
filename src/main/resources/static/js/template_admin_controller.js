const TEMPLATE_URL = 'http://localhost:8095/api/v1/templates';
const TEMPLATE_CONTAINER = document.getElementById('template-container-id');

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
    await toFilterTemplates();
}

async function setDirection() {
    PAGE.desc = !PAGE.desc;
    await toFilterTemplates();
}

async function toFilterTemplates() {
    const urlParameters = formDataToUrlParameter(
        await getFormDataFromFilter());

    const templates = await getTemplates(urlParameters,);

    if (templates == null) {
        return;
    }

    toDivs(templates);
}

async function getTemplates(parameters) {
    const respond = await fetch(TEMPLATE_URL + parameters + PAGE.toParameters(parameters));

    if (respond.status > 299) {
        alert("Not found");
        return null;
    }

    return await respond.json();
}

function toDivs(templates) {
    TEMPLATE_CONTAINER.innerHTML = '';

    for (const template of templates) {

        TEMPLATE_CONTAINER.innerHTML += toTemplateDiv(template);
    }
}

function toTemplateDiv(template) {
    return `
    <div class="order-box">
        <h2>Number: ${template.id}</h2>
        <p>Name : <samp style="color:#520974;">${template.name}</samp></p>
        <p>State : <samp style="color:#520974;">
        ${template.active ? 'Active' : 'Inactive'}</samp></p>

        <p>Price : <samp style="color:#520974;">${template.cost} UAH</samp></p>
        <p>Type : <samp style="color:#520974;">${template.typeTemplate}</samp></p>
        <p>Date of creation : <samp style="color:#520974;">
        ${template.dateOfCreation.replace('T', ' ').substring(0, 19)}</samp></p>

        <p>Images count : <samp style="color:#520974;">
        ${template.imagesUrl.length}</samp></p>
        <p>Past size count : <samp style="color:#520974;">
        ${template.partSizeForTemplates.length}</samp></p>

        <select name="" id="">
            <option value="" selected disabled>Color</option>
           ${toOptionDisabled(template.colors)}
        </select>
        <select name="" id="">
            <option value="" selected disabled>Material</option>
            ${toOptionDisabled(template.materials)}
        </select>
        <div class="link-button-box">
            <a href="http://localhost:8095/api/v1/templates/${template.id}/edit">
                <button type="button" class="link-button">Edit</button>
            </a>
        </div>

    </div>
    `
}

function toOptionDisabled(stuffs) {
    let options = '';

    for (const stuff of stuffs) {
        options += ` <option disabled>${stuff.name}</option>`;
    }

    return options;
}
function getPage(nameSortColumn) {
    return {
        offset: null,
        limit: null,
        desc: false,
        sortColumn: nameSortColumn,
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
}

async function sortByColumn(name, updateContainer) {
    PAGE.sortColumn = name;
    await updateContainer();
}

async function setDirection(updateContainer) {
    PAGE.desc = !PAGE.desc;
    await updateContainer();
}
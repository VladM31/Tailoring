const arrayReplaceElements = [];

function getOrHiddeFilter(id) {
    const element = getById(arrayReplaceElements, id);
    element.toSwap();

    element.new.replaceWith(element.now);
}

function classSwap(filter) {
    const getButton = document.createElement('button');
    getButton.classList.add('getter-filter');
    getButton.textContent = '◌⃗';
    getButton.id = filter.id;
    getButton.addEventListener('click', e => {
        getOrHiddeFilter(e.target.id);
    })

    // console.log(getButton);
    return {
        id: filter.id,
        now: filter,
        new: getButton,
        toSwap: function () {
            const temp = this.now;
            this.now = this.new;
            this.new = temp;
        }
    }
}


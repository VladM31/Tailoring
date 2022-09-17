const sortButtons = [];

function sortButton(id, hiddenInputId, desc) {
    return {
        id: id,
        button: document.getElementById(id),
        hiddenInput: document.getElementById(hiddenInputId),
        isDesc: new Boolean(desc),
        now: desc ? '▲' : '▼',
        new: !desc ? '▲' : '▼',
        toSwap: function () {
            const temp = this.now;
            this.now = this.new;
            this.new = temp;
            this.button.textContent = this.now;
            this.isDesc = !this.isDesc;
            this.hiddenInput.value = this.isDesc;
        }
    }
}

function changeDirection(id) {
    const sortBut = getById(sortButtons, id);
    sortBut.toSwap();
}

function setSymbols() {
    sortButtons.forEach(element => {
        element.button.textContent = element.now;
    })
}
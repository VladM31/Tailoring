const counterObject = {
    input: null,
    minus: function () {
        if (this.input.value > 1) {
            --this.input.value;
        }
    },
    plus: function () {
        ++this.input.value;
    }
}
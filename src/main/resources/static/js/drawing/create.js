document.addEventListener("DOMContentLoaded", (event) => {
    console.log("DOM fully loaded and parsed");
});

const createHelper = {
    init() {

    },
    pullList() {
        $.ajax({
            type: "get",
            url: "/drawing/list",
            dataType: "json"
        }).done((res) => {

        }).fail((err) => {

        });
    },
}
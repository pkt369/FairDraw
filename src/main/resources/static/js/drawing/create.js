document.addEventListener("DOMContentLoaded", (event) => {
    console.log("DOM fully loaded and parsed");
    createHelper.init();
});

const createHelper = {
    init() {
        this.pullList();
    },
    pullList() {
        $.ajax({
            type: "get",
            url: "/student/list",
            dataType: "json"
        }).done((res) => {
            console.log(res)
        }).fail((err) => {
            console.log(err)
        });
    },
}
document.addEventListener("DOMContentLoaded", (event) => {
    console.log("DOM fully loaded and parsed");
    createHelper.init();
    createHelper.addEvent();
});

const createHelper = {
    init() {
        this.pullList();
    },
    addEvent() {
        document.querySelector('#saveStudent').addEventListener('click', (event) => {
            this.createStudent();
        });
        document.querySelector('#nameSearchBtn').addEventListener('click', (event) => {
            const name = document.querySelector('#nameSearchInput').value;
            if (name === '') {
                alert('이름이 비워져 있습니다.');
                return;
            }
            this.pullListWithName(name);
        });
        document.querySelector('#modalToPageBtn').addEventListener('click', () => {

        });
    },
    createStudent() {
        $.ajax({
            type: "post",
            url: "/student/create",
            dataType: "json",
            data: getStudentParam(),
            contentType : 'application/json; charset=utf-8',
        }).done((res) => {
            console.log(res)
        }).fail((err) => {
            console.log(err)
        });
        function getStudentParam() {
            return JSON.stringify({
                "name": document.querySelector('#nameBasic').value,
                "email": document.querySelector('#emailBasic').value,
                "phone": document.querySelector('#phoneBasic').value,
                "guardianName": document.querySelector('#guardianNameBasic').value,
                "guardianPhone": document.querySelector('#guardianPhoneBasic').value,
                "birth": document.querySelector('#birthBasic').value,
                "gender": document.querySelector('#genderBasic').value,
                "disabledType": document.querySelector('#disabledTypeBasic').value,
            })
        }
    },
    pullList() {
        $.ajax({
            type: "get",
            url: "/student/list",
            dataType: "json",
            contentType : 'application/json; charset=utf-8',
        }).done((res) => {
            console.log(res)
        }).fail((err) => {
            console.log(err)
        });
    },
    pullListWithName(name) {
        $.ajax({
            type: "get",
            url: "/student/list/name",
            dataType: "json",
            data: {"name": name}
        }).done((res) => {
            let html = '';
            let index = 0
            for (const student of res) {
                const gender = student.gender === 'M' ? '남자': '여자';
                html += `<tr>
                            <td>
                                <input class="form-check-input" type="checkbox" value="" id="modalCheckboxStudnet${index}">
                                <label class="form-check-label" for="checkboxStudnet${index}"></label>
                            </td>
                            <td><i class="fab fa-angular fa-lg text-danger me-3" id="modalStudentName${index}"></i><strong>${student.name}</strong></td>
                            <td id="modalStudentBirth${index}">${student.birth}</td>
                            <td><span class="badge bg-label-primary me-1" id="modalStudentGender${index}">${gender}</span></td>
                            <td style="display: none" id="modalStudentId${index}"></td>
                        </tr>`;
                index++;
            }
            document.querySelector('#searchStudentTBody').innerHTML = html
        }).fail((err) => {
            console.log(err)
        });
    },
}
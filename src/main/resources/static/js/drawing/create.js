document.addEventListener("DOMContentLoaded", (event) => {
    console.log("DOM fully loaded and parsed");
    createHelper.init();
});

const createHelper = {
    init() {
        createHelper.pullStudents();
        createHelper.searchDrawingList();
        createHelper.addEvent();
        createHelper.addEventCheckbox();
    },

    addEvent() {
        document.querySelector('#saveStudent').addEventListener('click', (event) => {
            createHelper.createStudent();
        });
        document.querySelector('#nameSearchBtn').addEventListener('click', (event) => {
            const name = document.querySelector('#nameSearchInput').value;
            if (name === '') {
                createHelper.pullStudents();
                return;
            }
            createHelper.pullStudentsWithName(name);
        });
        document.querySelector('#modalToPageBtn').addEventListener('click', () => {
            const checked = document.querySelectorAll('.modalStudentCheckbox:checked');
            let students = [];
            checked.forEach(el => {
                const data = JSON.parse(el.parentNode.parentNode.getAttribute('data-json'));
                students.push(data);
            });
            console.log(students)
            const html = createHelper.createStudentTableRow(students);
            document.querySelector('#studentTable').innerHTML += html;
            $('#modalToggle1').modal('hide');
        });

        document.querySelector('#excelUploadBtn').addEventListener('change', () => {
            const form = document.getElementById('excelUploadForm');
            const formData = new FormData(form);
            $.ajax({
                type: "POST",
                url: "/student/create/excel/upload",
                data: formData,
                contentType: false,
                processData: false,
                success: function(res) {
                    document.querySelector('#excelExplain').style.display = 'none';
                    document.querySelector('#excelUploadTable').style.display = '';
                    document.querySelector('#excelModalToPageBtn').style.display = '';
                    document.querySelector('#excelUploadTableBody').innerHTML = createHelper.createModalStudentTableRow(res, "excelUploadCheckbox");
                    document.querySelectorAll('.excelUploadCheckbox').forEach(checkbox => {
                        checkbox.addEventListener('change', function () {
                            document.querySelector('#excelUploadCheckboxAll').checked = document.querySelectorAll('.excelUploadCheckbox').length === document.querySelectorAll('.excelUploadCheckbox:checked').length;
                        });
                    });
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error('Error:', textStatus, errorThrown);
                }
            });
        });

        document.querySelector('#excelModalToPageBtn').addEventListener('click', () => {
            const checked = document.querySelectorAll('.excelUploadCheckbox:checked');
            let students = [];
            checked.forEach(el => {
                const data = JSON.parse(el.parentNode.parentNode.getAttribute('data-json'));
                students.push(data);
            });
            const html = createHelper.createStudentTableRow(students);
            document.querySelector('#studentTable').innerHTML += html;
            $('#excelModal').modal('hide');
        });

        document.querySelector('#drawingStartBtn').addEventListener('click', () => {
            const name = document.querySelector('#drawingName').value;
            if (name.length === 0) {
                alert("이름을 입력해주세요");
                return false;
            }
            let studentIds = [];
            document.querySelectorAll('.studentTableTr').forEach(el => {
                console.log(el);
                studentIds.push(el.getAttribute('data-id'));
            });
            if (studentIds.length === 0) {
                alert("학생을 먼저 추가한 후 추첨을 해주세요.");
                return false;
            }
            const winnerCount = document.querySelector('#studentSize').value;
            if (winnerCount < 1) {
                alert("추첨 인원은 1명이상 부터 추첨이 가능합니다.");
                return false;
            }

            let param = {
                "name": name,
                "winnerCount": winnerCount,
                "studentIds": studentIds,
                "removeDrawingIds": []
            }

            $.ajax({
                type: "post",
                url: "/drawing/create",
                dataType: "json",
                data: JSON.stringify(param),
                contentType: 'application/json; charset=utf-8',
                success: function (res) {
                    console.log(res);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.error('Error:', textStatus, errorThrown);
                }
            });
        });

        document.querySelector('#beforeDrawingSearchBtn').addEventListener('click', () => {
            const name = document.querySelector('#beforeDrawingSearch').value;
            if (name.length === 0) {
                createHelper.searchDrawingList();
            } else {
                $.ajax({
                    type: "get",
                    url: "/drawing/list/name",
                    dataType: "json",
                    data: {"name": name},
                    contentType: 'application/json; charset=utf-8',
                    success: function (res) {
                        document.querySelector('#beforeDrawingTableBody').innerHTML = createHelper.createModalDrawingTableRow(res.data);
                        document.querySelectorAll('.modalDrawingCheckbox').forEach(checkbox => {
                            checkbox.addEventListener('change', function () {
                                document.querySelector('#modalDrawingCheckboxAll').checked = document.querySelectorAll('.modalDrawingCheckbox').length === document.querySelectorAll('.modalDrawingCheckbox:checked').length;
                            });
                        });
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.error('Error:', textStatus, errorThrown);
                    }
                });
            }
        });

        document.querySelector('#beforeModalDrawingToPageBtn').addEventListener('click', () => {
            //drawingTable
            document.querySelectorAll('.modalDrawingTableTr').forEach(el => {
                // el.
            });
        });
    },

    createStudent() {
        const param = getStudentParam();
        $.ajax({
            type: "post",
            url: "/student/create",
            dataType: "json",
            data: JSON.stringify(param),
            contentType : 'application/json; charset=utf-8',
        }).done((res) => {
            document.querySelector('#nameSearchInput').value = param.name;
            document.querySelector('#backToModal1Btn').click();
            document.querySelector('#nameSearchBtn').click();
        }).fail((err) => {
            console.log(err)
        });
        function getStudentParam() {
            return {
                "name": document.querySelector('#nameBasic').value,
                "email": document.querySelector('#emailBasic').value,
                "phone": document.querySelector('#phoneBasic').value,
                "guardianName": document.querySelector('#guardianNameBasic').value,
                "guardianPhone": document.querySelector('#guardianPhoneBasic').value,
                "birth": document.querySelector('#birthBasic').value,
                "gender": document.querySelector('#genderBasic').value,
                "disabledType": document.querySelector('#disabledTypeBasic').value,
            };
        }
    },

    pullStudents() {
        $.ajax({
            type: "get",
            url: "/student/list",
            dataType: "json",
            contentType: 'application/json; charset=utf-8',
            success: function (res) {
                document.querySelector('#searchStudentTBody').innerHTML = createHelper.createModalStudentTableRow(res, "modalStudentCheckbox")
                document.querySelectorAll('.modalStudentCheckbox').forEach(checkbox => {
                    checkbox.addEventListener('change', function () {
                        document.querySelector('#modalStudentCheckboxAll').checked = document.querySelectorAll('.modalStudentCheckbox').length === document.querySelectorAll('.modalStudentCheckbox:checked').length;
                    });
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error('Error:', textStatus, errorThrown);
            }
        });
    },

    createModalStudentTableRow(res, checkboxName) {
        let html = '';
        let index = 0
        for (const student of res.data) {
            const gender = student.gender === 'M' ? '남자' : '여자';
            html += `<tr id="createStudent${index}" data-json='${JSON.stringify(student)}'>
                            <td>
                                <input class="form-check-input ${checkboxName}" type="checkbox" value="" id="modalCheckboxStudnet${index}">
                                <label class="form-check-label" for="checkboxStudnet${index}"></label>
                            </td>
                            <td><i class="fab fa-angular fa-lg text-danger me-3" id="modalStudentName${index}"></i><strong>${student.name}</strong></td>
                            <td id="modalStudentBirth${index}">${student.birth}</td>
                            <td><span class="badge bg-label-primary me-1" id="modalStudentGender${index}">${gender}</span></td>
                            <td style="display: none" id="modalStudentId${index}"></td>
                        </tr>`;
            index++;
        }
        return html;
    },

    pullStudentsWithName(name) {
        $.ajax({
            type: "get",
            url: "/student/list/name",
            dataType: "json",
            data: {"name": name},
            success: function (res) {
                if (res.statusCode !== 200) {
                    alert("에러가 발생했습니다.");
                    return
                }
                document.querySelector('#searchStudentTBody').innerHTML = createHelper.createModalStudentTableRow(res, "modalStudentCheckbox");
                document.querySelectorAll('.modalStudentCheckbox').forEach(checkbox => {
                    checkbox.addEventListener('change', function () {
                        document.querySelector('#modalStudentCheckboxAll').checked = document.querySelectorAll('.modalStudentCheckbox').length === document.querySelectorAll('.modalStudentCheckbox:checked').length;
                    });
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error('Error:', textStatus, errorThrown);
            }
        });
    },

    createStudentTableRow(students) {
        let html = '';
        students.forEach(student => {
            const gender = student.gender === 'M' ? '남자' : '여자';
            html += `<tr class="studentTableTr" data-id="${student.id}">
                    <td><i class="fab fa-angular fa-lg text-danger me-3"></i><strong>${student.name}</strong></td>
                    <td>${student.birth}</td>
                    <td><span class="badge bg-label-primary me-1">${gender}</span></td>
                    <td>${StringNullThenHyphen(student.phone)}</td>
                    <td>${StringNullThenHyphen(student.guardianName)}</td>
                    <td>${StringNullThenHyphen(student.guardianPhone)}</td>
                    <td>${disabledType[student.disabledType]}</td>
                    <td>
                        <div class="dropdown">
                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                <i class="bx bx-dots-vertical-rounded"></i>
                            </button>
                            <div class="dropdown-menu">
                                <a class="dropdown-item" href="javascript:void(0);"><i class="bx bx-edit-alt me-1"></i> Edit</a>
                                <a class="dropdown-item" href="javascript:void(0);"><i class="bx bx-trash me-1"></i> Delete</a>
                            </div>
                        </div>
                    </td>
                </tr>`;
        });
        return html;

        function StringNullThenHyphen(s) {
            if (s === '') {
                return '-'
            }
            return s;
        }
    },

    addEventCheckbox() {
        document.querySelector('#excelUploadCheckboxAll').addEventListener('change', function () {
            if (this.checked) {
                document.querySelectorAll('.excelUploadCheckbox').forEach(checkbox => checkbox.checked = true);
                return;
            }
            document.querySelectorAll('.excelUploadCheckbox').forEach(checkbox => checkbox.checked = false);
        });

        document.querySelector('#modalStudentCheckboxAll').addEventListener('change', function () {
            if (this.checked) {
                document.querySelectorAll('.modalStudentCheckbox').forEach(checkbox => checkbox.checked = true);
                return;
            }
            document.querySelectorAll('.modalStudentCheckbox').forEach(checkbox => checkbox.checked = false);
        });

        document.querySelector('#modalDrawingCheckboxAll').addEventListener('change', function () {
            if (this.checked) {
                document.querySelectorAll('.modalDrawingCheckbox').forEach(checkbox => checkbox.checked = true);
                return;
            }
            document.querySelectorAll('.modalDrawingCheckbox').forEach(checkbox => checkbox.checked = false);
        });
    },

    createModalDrawingTableRow(drawings) {
        let html = '';
        let index = 0;
        for (const drawing of drawings) {
            html += `<tr class="modalDrawingTableTr" data-id="${drawing.id}">
                        <td>
                            <input class="form-check-input modalDrawingCheckbox" type="checkbox" value="" id="modalCheckboxDrawing${index}">
                            <label class="form-check-label" for="modalCheckboxDrawing${index}"></label>
                        </td>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i><strong>${drawing.name}</strong></td>
                        <td>${drawing.createdAt}</td>
                        <td>${drawing.winnerCount}</td>
                    </tr>`;
            index++;
        }
        return html;
    },

    searchDrawingList() {
        $.ajax({
            type: "get",
            url: "/drawing/list",
            dataType: "json",
            contentType: 'application/json; charset=utf-8',
            success: function (res) {
                document.querySelector('#beforeDrawingTableBody').innerHTML = createHelper.createModalDrawingTableRow(res.data);
                document.querySelectorAll('.modalDrawingCheckbox').forEach(checkbox => {
                    checkbox.addEventListener('change', function () {
                        document.querySelector('#modalDrawingCheckboxAll').checked = document.querySelectorAll('.modalDrawingCheckbox').length === document.querySelectorAll('.modalDrawingCheckbox:checked').length;
                    });
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error('Error:', textStatus, errorThrown);
            }
        });
    },
}

const disabledType = {
    NONE: "비장애",
    INTELLECTUAL: "지적장애",
    AUTISM: "자폐",
    LANGUAGE: "언어장애",
    DEVELOPMENTAL_DELAY: "발달장애"
}
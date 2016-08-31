  $(document).ready(function () {

                // 1st replace first column header text with checkbox

                $("#checkableGrid th").each(function () {
                    if ($.trim($(this).text().toString().toLowerCase()) === "{checkall}") {
                        $(this).text('');
                        $("<input/>", { type: "checkbox", id: "cbSelectAll", value: "" }).appendTo($(this));
                        $(this).append("<span> Select All </span>");
                    }
                });

                //2nd click event for header checkbox for select /deselect all
                $("#cbSelectAll").click( function () {
                    var ischecked = this.checked;
                    $('#checkableGrid').find("input:checkbox").each(function () {
                        this.checked = ischecked;
                    });
                });


                //3rd click event for checkbox of each row
                $("input[name='ids']").click(function () {
                    var totalRows = $("#checkableGrid td :checkbox").length;
                    var checked = $("#checkableGrid td :checkbox:checked").length;

                    if (checked == totalRows) {
                        $("#checkableGrid").find("input:checkbox").each(function () {
                            this.checked = true;
                        });
                    }
                    else {
                        $("#cbSelectAll").removeAttr("checked");
                    }
                });

            });
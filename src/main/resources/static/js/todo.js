/**
 *
 */
$(function() {

    //完了済みの個数取得・表示
    let doneCount = $('#donetodes').children("tr").length;
    $('#done_count').text(doneCount);

    //更新処理
    $('#todes input').change(function() {
        const todo = $(this).parents('.todo');
        const id = todo.find('input[name="id"]');
        const title = todo.find('input[name="title"]');
        const timeLimit = todo.find('input[name="work_plan_day"]');
        const isDone = todo.find('input[name="done_flg"]').prop("checked");
        let doneFlg;
        if (isDone == true) {
            doneFlg = true;
        } else {
            doneFlg = false;
        }

        const params = {
            id: id.val(),
            title: title.val(),
            work_plan_day: timeLimit.val(),
            done_flg: doneFlg
        }
        $.post("/update", params)
            .done(function(response) {
                console.log("【チェック成否】成功しました！データ:", response);
            })
            .fail(function(xhr) {
                console.error("【チェック成否】失敗しました。ステータス:", xhr.status);
            });

        $.post("/update", params);

        //完了ボタンを押した際の処理
        doneCount = $('#done_count').text();

        if ($(this).prop('name') == "done_flg") {
            if (isDone == true) {
                $(todo).appendTo('#donetodes');
                todo.find('span').css('text-decoration', 'line-through')
                todo.find('input[name="work_plan_day"]').hide();
                doneCount++;
            } else {
                $(todo).appendTo('#todes');
                todo.find('span').css('text-decoration', 'none')
                todo.find('input[name="work_plan_day"]').show()
                doneCount--;
            }

            $("#done_count").text(doneCount);
        }


    })

    //完了済みタスク表示/非表示切り替え
    $('.button_for_show').click(function() {
        let showState = $('#done_table').css('display');
        if (showState == "none") {
            $('#done_table').show();
            $(this).css({ transform: ' rotate(225deg)', 'bottom': '-4px' });
        } else {
            $('#done_table').hide();
            $(this).css({ transform: ' rotate(45deg)', 'bottom': '4px' });
        }
    })

    //追加処理
    $('#add').click(function() {
        $('#addTitleError').text('').hide();
        const params = $('#add_form').serialize();

        $.ajax({
            url: "/add",
            type: "POST",
            data: params,
            dataType: "json"
        }).done(function(json) {
            console.log("add response =", json);

            if (json.success) {
                location.href = "/detail?id=" + json.id;
                return;
            }

            if (json.errors) {
                if (json.errors.title) {
                    $('#addTitleError').text(json.errors.title).show();
                }
            }

        });
    });

    $('#cancelAdd').click(function() {
        $('#modal').modal('hide');
        $('#add_form')[0].reset();
        $('#addTitleError').text('').hide();
    });



    // 子タスク追加処理
    $('#childAddSaveBtn').click(function() {
        $('#childAddTitleError').text('').hide();
        const params = $('#child_add_form').serialize();

        $.ajax({
            url: "/add",
            type: "POST",
            data: params,
            dataType: "json"
        }).done(function(json) {
            console.log("child_add response =", json);

            if (json.success && json.id != null) {
                location.href = "/child/detail?id=" + json.id;
				return;
            }
			if(json.errors){
				if(json.errors.title){
					$('#childAddTitleError').text(json.errors.title).show();
				}
			}

        });
    });

    $('#childAddCancelBtn').click(function() {
        $('#childAddModal').modal('hide');
        $('#child_add_form')[0].reset();
		$('#childAddTitleError').text('').hide();
    });






    //削除処理
    $('#delete').click(function() {
        $.post("/delete").done(function() {
            $('#donetodes').empty();
            $('#done_count').text(0);
        })
        $('#delete').click(function() {
            $.post("/delete")
                .done(function(response) {
                    console.log("【削除成否】成功しました！データ:", response);
                    $('#donetodes').empty();
                    $('#done_count').text(0);
                })
                .fail(function(xhr) {
                    console.error("【削除成否】失敗しました。ステータス:", xhr.status);
                });
        });
    })

    //詳細画面へ

    //    $(".detail-btn").on("click", function() {
    //        const id = $(this).data("id");
    //        location.href = "/detail?id=" + id;
    //    });

    // 保存ボタン
    $("#saveBtn").on("click", function() {
        // checkbox の状態を hidden に入れて送信
        $("#doneFlgValue").val($("#doneFlgCheck").prop("checked"));
        $("#detailForm").submit();
    });

    // 戻るボタン
    $("#backBtn").on("click", function() {
        location.href = $(this).data("url");
    });

    // 保存ボタン(子タスク)
    $("#childSaveBtn").on("click", function() {
        // checkbox の状態を hidden に入れて送信
        $("#childDoneFlgValue").val($("#childDoneFlgCheck").prop("checked"));
        $("#childDetailForm").submit();
    });

    // 戻るボタン(子タスク)
    $('#childBackBtn').click(function() {
        location.href = $(this).data('url');
    });

});

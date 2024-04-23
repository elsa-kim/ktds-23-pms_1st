$().ready(function(){
    $("#search-btn").on("click", function () {
        search(0);
    });

    $("#list-size").on("change", function () {
        search(0);
    });

    /* 
    엔터키 눌렀을 때 form 전송이 안되도록 함
    
     */
    $("form")
    .find("input")
    .on("keydown", function (event) {
        if(event.keyCode === 13){
            var noSubmit = $(this).data("no-submit");
            if(noSubmit !== undefined){
                event.preventDefault();
            }
        }
    });
    

    $(".product-item").on("click", function(){
        var id = $(this).data("product")
        console.log(id)
        location.href = "/product/manage/view?prdtId=" + id
    });

    $(".create-product").on("click", function () {
        location.href = "/product/manage/add";
    });

    $(".add-product-count").on("click", function(){
        $("#add-modal")[0].showModal()
        var productName = $(this).data("productname")
        var productId = $(this).data("productid")
        $(".add-product").text(productId +" ("+productName+")")

        $(".add-count").val(1)

        console.log($(".add-product").text().split(" ")[0])
    })

    $("#cancel-btn").on("click", function(){
        location.reload()
    })

    $("#add-count-btn").on("click", function(){
       if($(".buy-day").val()==""||$(".buy-price").val()==""){
            alert("빈칸에 값을 입력해주세요")
        }else{
            $.post("/ajax/product/manage/list/add",{
                prdtId:$(".add-product").text().split(" ")[0],
                prdtPrice:$(".buy-price").val(),
                buyDt:$(".buy-day").val(),
                "productVO.prdtId" :$(".add-product").text().split(" ")[0],
                "productVO.curStr" :$(".add-count").val()
                
            }, function(res){
                if (res.data.result){
                    alert("정상적으로 추가되었습니다.")
                }else{
                    alert("오류가 발생되었습니다.")

                }
                location.href = res.data.next
            })
        }
    })

    $(".remove-product").on("click", function(){
        var id = $(this).data("product")
        $.get("/product/manage/list/iscandel?prdtId="+id, function(res){
            if(res.data.canDel){
                if(confirm("정말 삭제하시겠습니까?")){
                    $.get("/product/manage/list/del?prdtId="+id,function(res){
                        if(res.data.result){
                            alert("삭제가 완료되었습니다.")
                        }else{
                            alert("삭제 중 오류가 발생했습니다.")
                        }
                        location.href = res.data.next
                    })

                }
            }else{
                alert("해당 비품은 상세 품목이 존재하여 삭제할 수 없습니다.")
            }
        })
    })
})
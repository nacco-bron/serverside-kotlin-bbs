function changeArticleSubmit(type,owner){
    const changeArticleForm = owner.form
    const check = document.getElementsByName("article_check")
    let articleId = null

    for(let index = 0; index < check.length; index++){
        if(checks[index].checked){
            articleId = checks[index].getAttribute("data-id")
            break
        }
    }
    if(articleId == null){
        alert("記事を選択してください。")
        return
    }

    switch(type){
        case "update":
            changeArticleForm.action = "/edit/" + articleId
            changeArticleForm.submit()
            break
        default:
            break
    }
}
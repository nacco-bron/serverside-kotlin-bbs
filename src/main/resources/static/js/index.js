function changeArticleSubmit(type,owner,aid){
    const changeArticleForm = owner.form
    const checks = document.getElementsByName("article_check")
    let articleId = aid

    if(articleId == null){
        alert("記事を更新できません。")
        return
    }

    switch(type){
        case "update":
            changeArticleForm.action = "/edit/" + articleId
            changeArticleForm.submit()
            break
        case "delete":
            changeArticleForm.action = "/delete/confirm/" + articleId
            changeArticleForm.submit()
            break
        default:
            break
    }
}
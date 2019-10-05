package com.example.app.bbs.app.controller

import com.example.app.bbs.app.request.ArticleRequest
import com.example.app.bbs.domain.entity.Article
import com.example.app.bbs.domain.repository.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.concurrent.fixedRateTimer

@Controller
class ArticleController {

    @Autowired
    lateinit var articleRepository: ArticleRepository

    //投稿機能
    @PostMapping("/")
    fun registerArticle(@ModelAttribute articleRequest: ArticleRequest): String{
        articleRepository.save(
                Article(
                        articleRequest.id,
                        articleRequest.name,
                        articleRequest.title,
                        articleRequest.contents,
                        articleRequest.articleKey
                )
        )
        return "redirect:/"
    }

    //投稿一覧表示
    @GetMapping("/")
    fun getArticleList(model: Model): String{
        model.addAttribute("articles", articleRepository.findAll())

        return "index"
    }

    //更新画面への遷移
    @GetMapping("/edit/{id}")
    fun getArticleEdit(@PathVariable id: Int,model: Model): String{

        if (!articleRepository.existsById(id)) return "redirect:/"

        model.addAttribute("article", articleRepository.findById(id))
        return "edit"
    }

    //更新機能
    @PostMapping("/update")
    fun updateArticle(articleRequest: ArticleRequest): String{
        if (!articleRepository.existsById(articleRequest.id))
            return "redirect:/"

        val article: Article = articleRepository.findById(articleRequest.id).get()

        if (articleRequest.articleKey != article.articleKey)
            return "redirect:/edit/${articleRequest.id}"

        article.name = articleRequest.name
        article.title = articleRequest.title
        article.contents = articleRequest.contents
        article.updateAt = Date()

        articleRepository.save(article)

        return "redirect:/"
    }

    //削除確認画面
    @GetMapping("/delete/confirm/{id}")
    fun getDeleteConfirm(@PathVariable id: Int,model: Model): String{
        if (!articleRepository.existsById(id))
            return "redirect:/"

        model.addAttribute("article", articleRepository.findById(id).get())

        return "delete_confirm"
    }

    //削除機能
    @PostMapping("/delete")
    fun deleteArticle(@ModelAttribute articleRequest: ArticleRequest): String{
        if (!articleRepository.existsById(articleRequest.id))
            return "redirect:/"

        val article: Article = articleRepository.findById(articleRequest.id).get()

        if (articleRequest.articleKey != article.articleKey)
            return "redirect:/delete/confirm/${article.id}"

        articleRepository.deleteById(articleRequest.id)

        return "redirect:/"
    }


}
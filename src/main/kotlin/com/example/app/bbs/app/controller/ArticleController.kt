package com.example.app.bbs.app.controller

import com.example.app.bbs.app.request.ArticleRequest
import com.example.app.bbs.domain.entity.Article
import com.example.app.bbs.domain.repository.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
class ArticleController {

    val MESSAGE_REGISTER_NORMAL = "投稿しました。"
    val MESSAGE_ARTICLE_DOES_NOT_EXISTS = "対象の記事が見つかりませんでした。"
    val MESSAGE_UPDATE_NORMAL = "更新しました。"
    val MESSAGE_ARTICLE_KEY_UNMATCH = "投稿KEYが一致しません。"
    val ALERT_CLASS_SUCCESS = "alert-success"
    val ALERT_CLASS_ERROR = "alert-danger"
    val MESSAGE_DELETE_NORMAL = "削除しました。"
    val PAGE_SIZE: Int = 10

    @Autowired
    lateinit var articleRepository: ArticleRepository

    //テストデータ登録
    @GetMapping("/seed")
    @ResponseBody
    fun seed(): String{
        for (i in 1..50){
            var article = Article()
            article.name = "name_$i"
            article.title = "title_$i"
            article.contents = "contents_$i"
            article.articleKey = "1234"
            articleRepository.save(article)
        }

        return "Finish"
    }



    //投稿機能
    @PostMapping("/")
    fun registerArticle(@Validated @ModelAttribute articleRequest: ArticleRequest,
                        result: BindingResult,
                        redirectAttributes: RedirectAttributes): String{

        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", result)
            redirectAttributes.addFlashAttribute("request", articleRequest)

            return "redirect:/"
        }

        articleRepository.save(
                Article(
                        articleRequest.id,
                        articleRequest.name,
                        articleRequest.title,
                        articleRequest.contents,
                        articleRequest.articleKey
                )
        )
        redirectAttributes.addFlashAttribute("message", MESSAGE_REGISTER_NORMAL)
        redirectAttributes.addFlashAttribute("alert_class",ALERT_CLASS_SUCCESS)

        return "redirect:/"
    }

    //投稿一覧表示
    @GetMapping("/")
    fun getArticleList(@ModelAttribute articleRequest: ArticleRequest,
                       @RequestParam(
                               value = "page",
                               defaultValue = "0",
                               required = false
                       ) page: Int,
                       model: Model): String{

        val pageable: Pageable = PageRequest.of(
                page,
                this.PAGE_SIZE,
                Sort(Sort.Direction.DESC,"updateAt")
                        .and(Sort(Sort.Direction.ASC,"id"))
        )

        model.addAttribute("articles", articleRepository.findAll())

        if (model.containsAttribute("errors")){
            val key: String = BindingResult.MODEL_KEY_PREFIX + "articleRequest"
            model.addAttribute(key, model.asMap()["errors"])
        }

        if (model.containsAttribute("request")){
            model.addAttribute("articleRequest", model.asMap()["request"])
        }

        val articles: Page<Article> = articleRepository.findAll(pageable)
        model.addAttribute("page",articles)

        return "index"
    }

    //更新画面への遷移
    @GetMapping("/edit/{id}")
    fun getArticleEdit(@PathVariable id: Int,model: Model,redirectAttributes: RedirectAttributes): String{

        if (!articleRepository.existsById(id)){
            redirectAttributes.addFlashAttribute("message",MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute("alert_class",ALERT_CLASS_ERROR)
            return "redirect:/"
        }

        if (model.containsAttribute("request")){
            model.addAttribute("article", model.asMap()["request"])
        } else {
            model.addAttribute("article", articleRepository.findById(id).get())
        }

        if (model.containsAttribute("errors")){
            val key: String = BindingResult.MODEL_KEY_PREFIX + "article"
            model.addAttribute(key, model.asMap()["errors"])
        }

        return "edit"
    }

    //更新機能
    @PostMapping("/update")
    fun updateArticle(@Validated articleRequest: ArticleRequest,
                      result: BindingResult,
                      redirectAttributes: RedirectAttributes): String{

        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", result)
            redirectAttributes.addFlashAttribute("request", articleRequest)

            return "redirect:/edit/${articleRequest.id}"
        }

        if (!articleRepository.existsById(articleRequest.id)){
            redirectAttributes.addFlashAttribute("message",MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute("alert_class",ALERT_CLASS_ERROR)
            return "redirect:/"
        }

        val article: Article = articleRepository.findById(articleRequest.id).get()

        if (articleRequest.articleKey != article.articleKey){
            redirectAttributes.addFlashAttribute("message",MESSAGE_ARTICLE_KEY_UNMATCH)
            redirectAttributes.addFlashAttribute("alert_class",ALERT_CLASS_ERROR)
            return "redirect:/edit/${articleRequest.id}"
        }

        article.name = articleRequest.name
        article.title = articleRequest.title
        article.contents = articleRequest.contents
        article.updateAt = Date()

        articleRepository.save(article)

        redirectAttributes.addFlashAttribute("message",MESSAGE_UPDATE_NORMAL)
        redirectAttributes.addFlashAttribute("alert_class", ALERT_CLASS_SUCCESS)

        return "redirect:/"
    }

    //削除確認画面
    @GetMapping("/delete/confirm/{id}")
    fun getDeleteConfirm(@PathVariable id: Int,model: Model, redirectAttributes: RedirectAttributes): String{
        if (!articleRepository.existsById(id)){
            redirectAttributes.addFlashAttribute("message",MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute("alert_class",ALERT_CLASS_ERROR)
            return "redirect:/"
        }

        model.addAttribute("article", articleRepository.findById(id).get())

        val key: String = BindingResult.MODEL_KEY_PREFIX + "article"
        if (model.containsAttribute("errors")){
            model.addAttribute(key, model.asMap()["errors"])
        }

        return "delete_confirm"
    }

    //削除機能
    @PostMapping("/delete")
    fun deleteArticle(@Validated @ModelAttribute articleRequest: ArticleRequest,
                      result: BindingResult,
                      redirectAttributes: RedirectAttributes): String{

        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", result)
            redirectAttributes.addFlashAttribute("request", articleRequest)

            return "redirect:/delete/confirm/${articleRequest.id}"
        }

        if (!articleRepository.existsById(articleRequest.id)){
            redirectAttributes.addFlashAttribute("message",MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute("alert_class",ALERT_CLASS_ERROR)
            return "redirect:/"
        }

        val article: Article = articleRepository.findById(articleRequest.id).get()

        if (articleRequest.articleKey != article.articleKey){
            redirectAttributes.addFlashAttribute("message",MESSAGE_ARTICLE_KEY_UNMATCH)
            redirectAttributes.addFlashAttribute("alert_class",ALERT_CLASS_ERROR)
            return "redirect:/delete/confirm/${article.id}"
        }

        articleRepository.deleteById(articleRequest.id)
        redirectAttributes.addFlashAttribute("message",MESSAGE_DELETE_NORMAL)
        redirectAttributes.addFlashAttribute("alert_class", ALERT_CLASS_SUCCESS)

        return "redirect:/"
    }


}
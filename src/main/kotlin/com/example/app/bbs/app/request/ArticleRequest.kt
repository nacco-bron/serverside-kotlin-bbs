package com.example.app.bbs.app.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class ArticleRequest(
        var id : Int = 0,
        @field:NotBlank(message = "入力必須です。")
        @field:Size(min = 1, max = 50, message = "{max}文字以内で入力してください。")
        var name : String = "",
        @field:NotBlank(message = "入力必須です。")
        @field:Size(min = 1, max = 50, message = "{max}文字以内で入力してください。")
        var title : String = "",
        @field:NotBlank(message = "入力必須です。")
        @field:Size(min = 1, max = 500, message = "{max}文字以内で入力してください。")
        var contents : String = "",
        @field:NotBlank(message = "入力必須です。")
        @field:Size(min = 1, max = 4, message = "{max}文字以内で入力してください。")
        var articleKey : String = ""
)
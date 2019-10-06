package com.example.app.bbs.app.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.validation.Validator

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun getValidator(): Validator? {
        val validator = LocalValidatorFactoryBean()
        val messageSource = ReloadableResourceBundleMessageSource()
        // 任意のファイル名に変更
        messageSource.setBasename("classpath:ValidationMessages_ja")
        // 任意の文字コードに変更
        messageSource.setDefaultEncoding("UTF-8")
        validator.setValidationMessageSource(messageSource)
        return validator
    }

}
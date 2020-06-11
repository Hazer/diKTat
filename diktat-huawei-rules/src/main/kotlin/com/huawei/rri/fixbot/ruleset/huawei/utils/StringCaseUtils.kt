package com.huawei.rri.fixbot.ruleset.huawei.utils

import com.google.common.base.CaseFormat

/**
 * checking that string looks like: PascalCaseForClassName
 */
fun String.isPascalCase(): Boolean = this.matches("([A-Z][a-z0-9]+)+".toRegex())

/**
 * checking that string looks like: lowerCaseOfVariable
 */
fun String.isLowerCamelCase(): Boolean = this.matches("[a-z]([a-z0-9])*([A-Z][a-z0-9]+)*".toRegex())

/**
 * checking that string looks like: CORRECT_CASE_FOR_CONSTANTS
 */
fun String.isUpperSnakeCase(): Boolean = this.matches("(([A-Z]+)_*)+[A-Z]*".toRegex())

/**
 * checking that string looks like: lower_case_for_script_names
 */
fun String.isLowerSnakeCase(): Boolean = this.matches("(([a-z]+)_*)+[a-z]*".toRegex())

/**
 * detecting the case of _this_ String and converting it to the right UpperSnakeCase (UPPER_UNDERSCORE) case
 */
fun String.toUpperCase(): String {
    // PascalCase -> PASCAL_CASE
    if (this.isPascalCase()) return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this)
    // lower -> LOWER
    if (this.all { it.isLowerCase() }) return this.toUpperCase()
    // lowerCamel -> LOWER_CAMEL
    if (this.isLowerCamelCase()) return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this)
    // lower_snake -> LOWER_SNAKE
    if (this.isLowerSnakeCase()) return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, this)

    return this
}

/**
 * detecting the case of _this_ String and converting it to the right UpperSnakeCase (UPPER_UNDERSCORE) case
 */
fun String.toLowerCamelCase(): String {
    // PascalCase -> PASCAL_CASE
    if (this.isPascalCase()) return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this)
    // lower -> LOWER
    if (this.all { it.isUpperCase() }) return this.toLowerCase()
    // lowerCamel -> LOWER_CAMEL
    if (this.isUpperSnakeCase()) return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, this)
    // lower_snake -> LOWER_SNAKE
    if (this.isLowerSnakeCase()) return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, this)

    return this
}

/**
 * detecting the case of _this_ String and converting it to the right PascalCase (UpperCamel) case
 */
fun String.toPascalCase(): String {
    // all letters UPPER -> Upper
    if (this.all { it.isUpperCase() }) return this[0] + this.substring(1).toLowerCase()
    // all letters lower -> Lower
    if (this.all { it.isLowerCase() }) return this[0].toUpperCase() + this.substring(1)
    // lowerCamel -> LowerCamel
    if (this.isUpperSnakeCase()) return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this)
    // lower_snake -> LowerSnake
    if (this.isLowerSnakeCase()) return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this)

    // any other format -> PascalCase
    // changing first letter to uppercase and replacing several uppercase letters in raw to lowercase:
    // example of change: [p]a[SC]a[_]l -> [P]a[Sc]a[L]
    // FixMe: there is some discussion on how PascalN_Case should be resolved: to PascalNcase or to PascalnCase or PascalNCase (current version)
    var isPreviousLetterCapital = true
    var isPreviousLetterUnderscore = false
    return this[0].toUpperCase().toString() + this.substring(1).map {
        return@map if (it.isUpperCase()) {
            val result = if (isPreviousLetterCapital && !isPreviousLetterUnderscore) it.toLowerCase() else it
            isPreviousLetterCapital = true
            isPreviousLetterUnderscore = false
            result
        } else {
            val result = if (it == '_') {
                isPreviousLetterUnderscore = true
                ""
            } else if (isPreviousLetterUnderscore) {
                isPreviousLetterCapital = true
                isPreviousLetterUnderscore = false
                it.toUpperCase()
            } else {
                isPreviousLetterCapital = false
                isPreviousLetterUnderscore = false
                it
            }
            result
        }
    }.joinToString("")
}
package com.moony.calc.model

class Category() {
    var idCategory:Int=0
    var title:String="Empty"
    var iconUrl:String=""

    constructor(title: String, iconUrl: String):this() {
        this.title = title
        this.iconUrl = iconUrl
    }
}
package com.moony.calc.utils

import android.content.Context
import com.moony.calc.R
import kotlinx.coroutines.*

object AssetFolderManager {
    val assetPath = "//android_asset/categories/"
    lateinit var context: Context
    val imageMap = HashMap<Int, String>()
    fun addItemToMap() {
        imageMap[R.string.education] = "education/"
        imageMap[R.string.electronics] = "electronics/"
        imageMap[R.string.entertainment] = "entertainment/"
        imageMap[R.string.family] = "family/"
        imageMap[R.string.fitness] = "fitness/"
        imageMap[R.string.food] = "food/"
        imageMap[R.string.furniture] = "furniture/"
        imageMap[R.string.income] = "income/"
        imageMap[R.string.life] = "life/"
        imageMap[R.string.medical] = "medical/"
        imageMap[R.string.shopping] = "shopping/"
        imageMap[R.string.transportation] = "transportation/"
    }

     fun getAllCategories(path: String): Deferred<List<String>?> =
        GlobalScope.async(Dispatchers.IO) {
            val myPath=path.replace('/',' ').trim()
            val iconName = context.assets.list("categories/$myPath")?.toMutableList()
            val links = ArrayList<String>()
            iconName?.forEach { links.add("categories/$path$it") }
            links
        }

}
package com.wlm.wanandroid2.bean

import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.db.History
import java.io.Serializable
import java.util.*


data class User(
    val admin: Boolean,
    val chapterTops: List<Any>,
    val collectIds: List<Any>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
)

data class ArticleList(
    val curPage: Int,
    val datas: MutableList<Article>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

data class Article(
    val apkLink: String,
    val audit: Int,
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    var collect: Boolean,
    val courseId: Int,
    val desc: String?,
    val envelopePic: String?,
    val fresh: Boolean,
    val id: Int,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val originId: Int,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String?,
    val tags: List<Any>,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int,
    var isTop: Boolean = false,
    var bannerList: List<BannerData>?
): Serializable {
    fun createHistory(): History {
        val history = History(-1, id, title, link, desc, envelopePic, type, collect, Date().time)
        val userString = Constant.userString
        if (userString.isNotEmpty()) {
            history.visitUserId = userString.split(",")[2].toInt()
        }
        return history
    }
}


data class BannerData(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
): Serializable {
    fun createHistory(): History {
        val history = History(-1, id, title, url, desc, imagePath, type, false, Date().time)
        val userString = Constant.userString
        if (userString.isNotEmpty()) {
            history.visitUserId = userString.split(",")[2].toInt()
        }
        return history
    }
}


data class Knowledge(
    val children: List<Knowledge>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
): Serializable

data class Navigation(
    val articles: List<Article>,
    val cid: Int,
    val name: String
)

data class HotKey(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
)

data class TodoList(
    val curPage: Int,
    val datas: List<Todo>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

data class Todo(
    val completeDate: Long,
    val completeDateStr: String,
    val content: String,
    val date: Long,
    val dateStr: String,
    val id: Int,
    val priority: Int,
    val status: Int,
    val title: String,
    val type: Int,
    val userId: Int
)

data class ShareList(
    val coinInfo: CoinInfo,
    val shareArticles: ShareArticles
)

data class CoinInfo(
    val coinCount: Int,
    val level: Int,
    val nickname: String,
    val rank: String,
    val userId: Int,
    val username: String
)

data class ShareArticles(
    val curPage: Int,
    val datas: List<Article>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

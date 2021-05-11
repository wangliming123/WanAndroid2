package com.wlm.wanandroid2.repository

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wlm.baselib.common.Listing
import com.wlm.wanandroid2.bean.*
import com.wlm.wanandroid2.common.RetrofitManager
import com.wlm.wanandroid2.datasource.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ArticleRepository {

    fun getArticleListing(
        pageSize: Int,
        sourceFactory: HomeDataSourceFactory
    ): Listing<Article> {
        val pageList = LivePagedListBuilder(
            sourceFactory,
            PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build()
        ).build()
        return Listing(
            pageList,
            refresh = { sourceFactory.source.value?.invalidate() })
    }

    fun getNavigationList(
        pageSize: Int,
        sourceFactory: NavigationDataSourceFactory
    ): Listing<Navigation> {
        val pagedList = LivePagedListBuilder(
            sourceFactory,
            PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build()
        ).build()
        return Listing(pagedList, refresh = { sourceFactory.source.value?.invalidate() })
    }

    fun getKnowledgeList(
        pageSize: Int,
        sourceFactory: KnowledgeDataSourceFactory
    ): Listing<Article> {
        val pagedList = LivePagedListBuilder(
            sourceFactory, PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .build()
        ).build()
        return Listing(pagedList, refresh = { sourceFactory.source.value?.invalidate() })
    }


    fun getKnowledgeTreeList(
        pageSize: Int,
        sourceFactory: KnowledgeTreeDataSourceFactory
    ): Listing<Knowledge> {
        val pagedList = LivePagedListBuilder(
            sourceFactory, PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build()
        ).build()
        return Listing(pagedList, refresh = { sourceFactory.source.value?.invalidate() })
    }


    fun getSquareListing(
        pageSize: Int,
        sourceFactory: SquareDataSourceFactory
    ): Listing<Article> {
        val pagedList = LivePagedListBuilder(
            sourceFactory, PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .build()
        ).build()
        return Listing(pagedList, refresh = { sourceFactory.source.value?.invalidate() })
    }


    fun getCollectListing(
        pageSize: Int,
        sourceFactory: CollectDataSourceFactory
    ): Listing<Article>? {
        val pagedList = LivePagedListBuilder(
            sourceFactory, PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .build()
        ).build()
        return Listing(pagedList, refresh = { sourceFactory.source.value?.invalidate() })
    }


    fun getShareListing(
        pageSize: Int,
        sourceFactory: ShareDataSourceFactory
    ): Listing<Article> {
        val pageList = LivePagedListBuilder(
            sourceFactory,
            PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build()
        ).build()
        return Listing(pageList, refresh = { sourceFactory.source.value?.invalidate() })
    }

    suspend fun getArticleList(page: Int): HttpResponse<ArticleList> {
        return withContext(Dispatchers.IO) { RetrofitManager.service.getArticles(page) }

    }

    suspend fun getBanners(): HttpResponse<List<BannerData>> {
        return withContext(Dispatchers.IO) { RetrofitManager.service.getBanners() }
    }

    suspend fun getTop(): HttpResponse<List<Article>> {
        return withContext(Dispatchers.IO) { RetrofitManager.service.getTop() }
    }


    suspend fun collect(id: Int): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.collect(id)
        }
    }

    suspend fun unCollect(id: Int): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.unCollect(id)
        }
    }


    suspend fun getNavigation(): HttpResponse<List<Navigation>> {
        return withContext(Dispatchers.IO) { RetrofitManager.service.getNavigation() }
    }

    suspend fun getWxList(): HttpResponse<List<Knowledge>> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getWxList()
        }
    }


    suspend fun searchArticles(page: Int, knowledgeId: Int): HttpResponse<ArticleList> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getKnowledgeItem(page, knowledgeId)
        }
    }

    suspend fun getProjectItem(page: Int, knowledgeId: Int): HttpResponse<ArticleList> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getProjectItem(page, knowledgeId)
        }
    }

    suspend fun getWxArticles(page: Int, knowledgeId: Int): HttpResponse<ArticleList> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getWxArticles(knowledgeId, page)
        }
    }


    suspend fun getKnowledgeTree(): HttpResponse<List<Knowledge>> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getKnowledgeTree()
        }
    }

    suspend fun getProjectTree(): HttpResponse<List<Knowledge>> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getProjectTree()
        }
    }

    suspend fun getSquareArticle(page: Int): HttpResponse<ArticleList> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getSquareArticle(page)
        }
    }

    suspend fun shareArticle(title: String, link: String): HttpResponse<Any> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.shareArticle(title, link)
        }
    }

    suspend fun getCollectArticles(page: Int): HttpResponse<ArticleList> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getCollectArticles(page)
        }
    }

    suspend fun getMyShareList(page: Int): HttpResponse<ShareList> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getMyShareList(page)
        }
    }

    suspend fun deleteShare(id: Int): HttpResponse<Any> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.deleteShare(id)
        }
    }

}
package com.ditclear.paonet.view.home.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import com.ditclear.paonet.helper.extens.*
import com.ditclear.paonet.model.data.Category
import com.ditclear.paonet.model.data.User
import com.ditclear.paonet.model.remote.api.PaoService
import com.ditclear.paonet.viewmodel.BaseViewModel
import javax.inject.Inject

/**
 * 页面描述：MainViewModel
 *
 * Created by ditclear on 2017/10/27.
 */

class MainViewModel @Inject constructor(val repo: PaoService) : BaseViewModel() {

    val user = MutableLiveData<User>().init(User())
    val categories = ObservableArrayList<CategoryItemViewModel>()

    val cateVisible=MutableLiveData<Boolean>().init(false)

    val face = MutableLiveData<String>()

    var qianming = MutableLiveData<String>()
    var navHeaderName = MutableLiveData<String>()
    var loginBtnText = MutableLiveData<String>().init("LOG IN")

    val exitEvent = ObservableBoolean(false)

    fun exit()=exitEvent.toFlowable().async(2000).doOnNext { exitEvent.set(false) }

    init {
        user.toFlowable()
                .doOnNext {
                    face.set(user.get()?.face)
                    qianming.set(user.get()?.qianming)
                    navHeaderName.set(user.get()?.nickname ?: "")
                    loginBtnText.set( if (user.get()?.nickname == null) "LOG IN" else "LOG OUT")
                }.subscribe()
    }


    //////////////////bind view/////////////
    /**
     * 获取代码分类
     */
    fun getCodeCategories() = repo.getCodeCategory().async()
            .map {
                categories.add(CategoryItemViewModel(Category("全部(All)")))
                categories.addAll(it.map { CategoryItemViewModel(it) }) }

    fun toggleCategory(){
        val visible = cateVisible.get()?:false
        cateVisible.set(!visible)
    }
}
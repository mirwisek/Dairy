package com.app.dairy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ViewModel : ViewModel() {

    val list = MutableLiveData<List<DairyItem>>().apply {
        viewModelScope.launch(Dispatchers.IO) {
            Firebase.firestore.collection("data").document("dairy")
                .addSnapshotListener { value, error ->
                    viewModelScope.launch(Dispatchers.IO) {
                        value?.let {  doc ->
                            val data = doc.toObject(DairyItems::class.java)
                            postValue(data?.items)
                        }

                        error?.printStackTrace()
                    }
                }
        }
    }

    val selectedItem = MutableLiveData<DairyItem>()

    val addResult = MutableLiveData<Result<String>>()

    fun addToFb(item: DairyItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val fb = Firebase.firestore

            val doc = fb.collection("data").document("dairy")

            fb.runTransaction { t ->

                val data = t.get(doc).toObject(DairyItems::class.java)
                data?.let { dairyItems ->
                    dairyItems.items.add(item)
                    t.set(doc, dairyItems)
                }
            }.addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    addResult.postValue(Result.success("Successfully added record"))
                } else {
                    if(task.exception == null) {
                        addResult.postValue(Result.failure(Exception("Couldn't add record server error")))
                    } else {
                        addResult.postValue(Result.failure(task.exception!!))
                    }
                }
            }
        }

    }

}
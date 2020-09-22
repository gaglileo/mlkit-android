package com.google.mlkit.codelab.translate.main.face

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class FaceViewModel(application: Application) : AndroidViewModel(application) {

    val isSmiling = MutableLiveData<Boolean>()
    val openEyes = MutableLiveData<String>()

}
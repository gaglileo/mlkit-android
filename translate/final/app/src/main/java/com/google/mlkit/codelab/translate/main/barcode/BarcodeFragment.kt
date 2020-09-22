package com.google.mlkit.codelab.translate.main.barcode

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.mlkit.codelab.translate.R

class BarcodeFragment : Fragment() {

    companion object {
        fun newInstance() = BarcodeFragment()
    }

    private val viewModel: BarcodeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_barcode, container, false)
    }

}
package com.bottotop.home

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bottotop.home.databinding.QrBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class QrDialog(viewModel: HomeViewModel) : BottomSheetDialogFragment(){

    private var _binding: QrBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val viewModel =viewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.qr_bottom_sheet, container, false)
        _binding?.apply {
            lifecycleOwner = this@QrDialog
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.companyName.observe(viewLifecycleOwner,{
            createQRCode(it)
        })
    }

    fun createQRCode(name : String){
        val qrCode = QRCodeWriter()
        val bitMtx = qrCode.encode(
            name ,
            BarcodeFormat.QR_CODE,
            350,
            350
        )
        val bitmap: Bitmap = Bitmap.createBitmap(bitMtx.width, bitMtx.height, Bitmap.Config.RGB_565)
        for(i in 0 .. bitMtx.width-1){
            for(j in 0 .. bitMtx.height-1){
                var color = 0
                if(bitMtx.get(i, j)){
                    color = Color.BLACK
                }else{
                    color = Color.WHITE
                }
                bitmap.setPixel(i, j, color)
            }
        }
        binding.qrIv.setImageBitmap(bitmap)
    }
}
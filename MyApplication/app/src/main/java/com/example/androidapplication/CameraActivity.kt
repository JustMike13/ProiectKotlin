package com.example.androidapplication
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.androidapplication.databinding.ActivityCameraBinding
//import com.example.scopedstorageyt.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*


class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var isReadPermissionGranted = false
    private var isWritePermissionGranted = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->

            isReadPermissionGranted = permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermissionGranted
            isWritePermissionGranted = permissions[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: isWritePermissionGranted

        }

        requestPermission()

        val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){

            lifecycleScope.launch {

                if (isWritePermissionGranted){

                    if (savePhotoToExternalStorage(UUID.randomUUID().toString(),it)){
                        Toast.makeText(this@CameraActivity,"Photo Saved Successfully",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@CameraActivity,"Failed to Save photo",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@CameraActivity,"Permission not Granted",Toast.LENGTH_SHORT).show()
                }

            }

        }

        binding.saveButton.setOnClickListener {
            takePhoto.launch()
        }

        init()
    }

    private fun sdkCheck() : Boolean{

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            return true
        }

        return false

    }

    private fun requestPermission(){

        val isReadPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val isWritePermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val minSdkLevel = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        isReadPermissionGranted = isReadPermission
        isWritePermissionGranted = isWritePermission || minSdkLevel

        val permissionRequest = mutableListOf<String>()
        if (!isWritePermissionGranted){
            permissionRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!isReadPermissionGranted){
            permissionRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissionRequest.isNotEmpty())
        {
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }

    }

    private fun savePhotoToExternalStorage(name : String, bmp : Bitmap?) : Boolean{

        val imageCollection : Uri = if (sdkCheck()){
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }else{
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {

            put(MediaStore.Images.Media.DISPLAY_NAME,"$name.jpg")
            put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")
            if (bmp != null){
                put(MediaStore.Images.Media.WIDTH,bmp.width)
                put(MediaStore.Images.Media.HEIGHT,bmp.height)
            }

        }

        return try{

            contentResolver.insert(imageCollection,contentValues)?.also {

                contentResolver.openOutputStream(it).use { outputStream ->

                    if (bmp != null){

                        if(!bmp.compress(Bitmap.CompressFormat.JPEG,95,outputStream)){

                            throw IOException("Failed to save Bitmap")
                        }
                    }
                }

            } ?: throw IOException("Failed to create Media Store entry")
            true
        }catch (e: IOException){

            e.printStackTrace()
            false
        }

    }

    //take photo from gallery
    private fun init(){

        val pickPhoto = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {

            binding.pickedImage.setImageURI(it)


        }

        binding.btnGallery.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                pickPhoto.launch("image/*")

            }else{

                Toast.makeText(this,"Read Permission is not Granted", Toast.LENGTH_SHORT).show()

            }

        }


    }




}
package org.wit.myweather.firebase

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

object FirebaseStorageManager {
    var storage = FirebaseStorage.getInstance().reference
    var imageUri = MutableLiveData<Uri>()

    fun checkforexistingimage(imageName: String,extension:String){
        val imageRef = storage.child("${imageName}.${extension}")
        imageRef.metadata.addOnSuccessListener { //File Exists

              imageRef.downloadUrl.addOnCompleteListener { task ->
                imageUri.value = task.result!!

            }

            //File Doesn't Exist
        }.addOnFailureListener {
            imageUri.value = Uri.EMPTY
        }
    }


    fun uploadImageToFirebase(userid: String, bitmap: Bitmap, updating : Boolean) {
        // Get the data from an ImageView as bytes
        val imageRef = storage.child("${userid}.jpg")
        //val bitmap = (imageView as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        lateinit var uploadTask: UploadTask

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        imageRef.metadata.addOnSuccessListener { //File Exists
            if(updating) // Update existing Image
            {
                uploadTask = imageRef.putBytes(data)
                uploadTask.addOnSuccessListener { ut ->
                    ut.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                        imageUri.value = task.result!!
                        //FirebaseDBManager.updateImageRef(userid,imageUri.value.toString())
                    }
                }
            }
        }.addOnFailureListener { //File Doesn't Exist
            uploadTask = imageRef.putBytes(data)
            uploadTask.addOnSuccessListener { ut ->
                ut.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                    imageUri.value = task.result!!
                }
            }
        }
    }

    fun updateUserImage(userid: String, imageUri : Uri?, imageView: ImageView, updating : Boolean) {
        Picasso.get().load(imageUri)
            .resize(200, 200)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .centerCrop()
            .into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?,
                                            from: Picasso.LoadedFrom?
                ) {

                    uploadImageToFirebase(userid, bitmap!!,updating)
                    imageView.setImageBitmap(bitmap)
                }

                override fun onBitmapFailed(e: java.lang.Exception?,
                                            errorDrawable: Drawable?) {

                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }
            })
    }



}
package com.test.listfilterex.ui

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.test.listfilterex.MainActivity
import com.test.listfilterex.Post
import com.test.listfilterex.R
import com.test.listfilterex.databinding.FragmentInputBinding
import com.unsplash.pickerandroid.photopicker.UnsplashPhotoPicker
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import com.unsplash.pickerandroid.photopicker.presentation.UnsplashPickerActivity
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class InputFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var binding: FragmentInputBinding

    val categoryList = arrayOf(
        "영화", "음악", "사진"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        binding = FragmentInputBinding.inflate(layoutInflater)

        binding.run {
            spinnerInputCategory.run {
                val a1 = ArrayAdapter(
                    mainActivity,
                    android.R.layout.simple_spinner_item,
                    categoryList
                )
                a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapter = a1
            }

            buttonInputSave.setOnClickListener {
                startActivityForResult(
                    UnsplashPickerActivity.getStartingIntent(
                        mainActivity,
                        false
                    ), 0
                )
//                val category = categoryList[spinnerInputCategory.selectedItemPosition]
//                val title = if (editTextInputTitle.text.toString().isEmpty()) "${mainActivity.postId}"
//                            else editTextInputTitle.text.toString()
//                val post = Post(mainActivity.postId, category, title)
//                mainActivity.postId += 1
//                mainActivity.postList.add(post)
//                mainActivity.onBackPressedDispatcher.onBackPressed()
            }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            val photos: ArrayList<UnsplashPhoto>? = data?.getParcelableArrayListExtra(UnsplashPickerActivity.EXTRA_PHOTOS)
            Log.d("brudenell", "${photos?.get(0)?.urls?.thumb}")
            thread {
                val url = URL(photos?.get(0)?.urls?.thumb)
                val conn = url.openConnection() as HttpURLConnection
                conn.doInput = true
                conn.connect()
                val inputStream = conn.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)

                binding.run {
                    val category = categoryList[spinnerInputCategory.selectedItemPosition]
                    val title = if (editTextInputTitle.text.toString().isEmpty()) "${mainActivity.postId}"
                    else editTextInputTitle.text.toString()
                    val post = Post(mainActivity.postId, category, title, bitmap)
                    mainActivity.postId += 1
                    mainActivity.postList.add(post)
                }

                mainActivity.runOnUiThread {
                    mainActivity.onBackPressedDispatcher.onBackPressed()
                }
            }
            //mainActivity.onBackPressedDispatcher.onBackPressed()
        }
    }
}
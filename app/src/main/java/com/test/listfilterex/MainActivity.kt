package com.test.listfilterex

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.listfilterex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    var postList = mutableListOf<Post>()

    var selectedCategory = "전체"
    var postId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

data class Post(
    val id: Int,
    val category: String,
    val title: String,
    val bitmap: Bitmap
)
package com.example.map

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.map.databinding.ActivityFavoriteBinding

class Favorite : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    var i = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.list.setOnClickListener{
            if(i==0){
                binding.list.setBackgroundResource(R.drawable.find_way)
                i++
            }
            else if(i==1){
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
package com.example.map

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.map.databinding.ActivityStartBinding

class Start : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    var i = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener{
            Log.d("knu","click")
            if(i==0){
                binding.start.setBackgroundResource(R.drawable.logintdisplay)
                i++
            } else if(i==1){
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        }
    }
}
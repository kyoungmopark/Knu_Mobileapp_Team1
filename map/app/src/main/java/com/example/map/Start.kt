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
            Log.d("cc","click")
            if(i==0){
                binding.start.setBackgroundResource(R.drawable.start2)
                i++
            }
            else if(i==1){
                binding.start.setBackgroundResource(R.drawable.start3)
                i++
            }
            else if(i==2){
                binding.start.setBackgroundResource(R.drawable.start4)
                i++
            }
            else if(i==3){
                binding.start.setBackgroundResource(R.drawable.login)
                i++
            }
            else if(i==4){
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)

            }

        }
    }
}
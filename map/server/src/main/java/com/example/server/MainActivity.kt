package com.example.server

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.server.databinding.ActivityMainBinding
import com.example.server.mapdata.*
import com.example.server.rawdata.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var dataConverter: DataConverter
    private lateinit var bukguDataService: DataService<BukguData>
    private lateinit var jungguDataService: DataService<JungguData>
    private lateinit var suseongguDataService: DataService<SuseongguData>

    private val channel = Channel<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataConverter = DataConverter(this)

        bukguDataService = DataService(
            RawDataService(BukguData::class, getString(R.string.bukgu_url_base), getString(R.string.service_key)),
            dataConverter,
            MapDataService(getString(R.string.bukgu_collection_name))
        )
        jungguDataService = DataService(
            RawDataService(JungguData::class, getString(R.string.junggu_url_base), getString(R.string.service_key)),
            dataConverter,
            MapDataService(getString(R.string.junggu_collection_name))
        )
        suseongguDataService = DataService(
            RawDataService(SuseongguData::class, getString(R.string.suseonggu_url_base), getString(R.string.service_key)),
            dataConverter,
            MapDataService(getString(R.string.suseonggu_collection_name))
        )

        binding.updateButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                withContext(Dispatchers.Main) {
                    binding.updateButton.isEnabled = false
                    binding.textView.text = getString(R.string.update_in_prograss)
                }

                bukguDataService.update()
                jungguDataService.update()
                suseongguDataService.update()

                withContext(Dispatchers.Main) {
                    binding.updateButton.isEnabled = true
                    binding.textView.text = getString(R.string.update_completed)
                }
            }
        }
    }
}
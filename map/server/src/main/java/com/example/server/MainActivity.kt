package com.example.server

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.server.data.BukguData
import com.example.server.data.DeserializedData
import com.example.server.data.JungguData
import com.example.server.data.SuseongguData
import com.example.server.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var bukguDataUpdater: DataUpdater<BukguData>
    private lateinit var jungguDataUpdater: DataUpdater<JungguData>
    private lateinit var suseongguDataUpdater: DataUpdater<SuseongguData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bukguDataUpdater = DataUpdater(
            BukguData::class,
            getString(R.string.bukgu),
            getString(R.string.bukgu_json_url),
            getString(R.string.json_key),
            this
        )
        jungguDataUpdater = DataUpdater(
            JungguData::class,
            getString(R.string.junggu),
            getString(R.string.junggu_json_url),
            getString(R.string.json_key),
            this
        )
        suseongguDataUpdater = DataUpdater(
            SuseongguData::class,
            getString(R.string.suseonggu),
            getString(R.string.suseonggu_json_url),
            getString(R.string.json_key),
            this
        )

        binding.updateButton.setOnClickListener {
            binding.updateButton.isEnabled = false
            update(bukguDataUpdater, binding.bukguProgress)
            update(jungguDataUpdater, binding.jungguProgress)
            update(suseongguDataUpdater, binding.suseongguProgress)
        }
    }

    private fun<T: DeserializedData> update(dataUpdater: DataUpdater<T>, progressBar: ProgressBar) {
        progressBar.isVisible = true
        CoroutineScope(Dispatchers.IO).launch {
            dataUpdater.setOnGeocodeProgressListener { progress, max ->
                progressBar.progress = progress
                progressBar.max = max
            }
            dataUpdater.update()
            withContext(Dispatchers.Main) {
                progressBar.isVisible = false
            }
        }
    }
}
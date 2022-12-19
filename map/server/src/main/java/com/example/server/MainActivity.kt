package com.example.server

import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.server.data.BukguData
import com.example.server.data.JungguData
import com.example.server.data.SuseongguData
import com.example.server.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var bukguDataUpdater: DataUpdater<BukguData>
    private lateinit var jungguDataUpdater: DataUpdater<JungguData>
    private lateinit var suseongguDataUpdater: DataUpdater<SuseongguData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val geocoder = Geocoder(this, Locale.KOREA)

        bukguDataUpdater = DataUpdater(
            BukguData::class,
            getString(R.string.bukgu),
            getString(R.string.bukgu_json_url),
            getString(R.string.json_key),
            geocoder
        ).apply {
            setOnStartListener {
                setOnStartListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.bukguResult.text = getString(R.string.update_wait)
                    }
                }
                setOnCompleteListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.bukguResult.text = getString(R.string.update_completed)
                    }
                }
                setOnGeocodeStartListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.bukguProgress.visibility = View.VISIBLE
                        binding.bukguProgress.max = it
                    }
                }
                setOnGeocodeProgressListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.bukguProgress.progress = it
                    }
                }
                setOnGeocodeCompleteListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.bukguProgress.visibility = View.GONE
                    }
                }
            }
        }

        jungguDataUpdater = DataUpdater(
            JungguData::class,
            getString(R.string.junggu),
            getString(R.string.junggu_json_url),
            getString(R.string.json_key),
            geocoder
        ).apply {
            setOnStartListener {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.jungguResult.text = getString(R.string.update_wait)
                }
            }
            setOnCompleteListener {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.jungguResult.text = getString(R.string.update_completed)
                }
            }
            setOnGeocodeStartListener {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.jungguProgress.visibility = View.VISIBLE
                    binding.jungguProgress.max = it
                }
            }
            setOnGeocodeProgressListener {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.jungguProgress.progress = it
                }
            }
            setOnGeocodeCompleteListener {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.jungguProgress.visibility = View.GONE
                }
            }
        }

        suseongguDataUpdater = DataUpdater(
            SuseongguData::class,
            getString(R.string.suseonggu),
            getString(R.string.suseonggu_json_url),
            getString(R.string.json_key),
            geocoder
        ).apply {
            setOnStartListener {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.suseongguResult.text = getString(R.string.update_wait)
                }
            }
            setOnCompleteListener {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.suseongguResult.text = getString(R.string.update_completed)
                }
            }
            setOnGeocodeStartListener {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.suseongguProgress.visibility = View.VISIBLE
                    binding.suseongguProgress.max = it
                }
            }
            setOnGeocodeProgressListener {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.suseongguProgress.progress = it
                }
            }
            setOnGeocodeCompleteListener {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.suseongguProgress.visibility = View.GONE
                }
            }
        }

        binding.updateButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                // 클릭이 두 번 이상 되는 것을 막기 위해 아예 isEnabled를 false로 한다
                withContext(Dispatchers.Main) {
                    binding.updateButton.isEnabled = false
                }
                // 각 지역별로 데이터 업데이트를 동시에 한다
                listOf(
                    async { bukguDataUpdater.update() },
                    async { jungguDataUpdater.update() },
                    async { suseongguDataUpdater.update() }
                ).awaitAll()
                withContext(Dispatchers.Main) {
                    binding.updateButton.isEnabled = true
                }
            }
        }
    }
}
package com.example.gfdh

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.gfdh.databinding.ActivityMainBinding
import kotlin.random.Random

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var score = 0
    var handler = Handler()
    var buttonArray = arrayOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonArray = arrayOf(
            binding.btnfirst,
            binding.btnsecond,
            binding.btnthird,
            binding.btnfourth,
            binding.btnfifth,
            binding.btnsixth,
            binding.btnseventh,
            binding.btneighth,
            binding.btnninth
        )

        InvisibleButton()
        setupTimer()
        startRandomButtonVisibility()
        retryGame()
        enableEdgeToEdge()
    }

    private fun InvisibleButton() {
        for (button in buttonArray) {
            button.visibility = View.INVISIBLE
        }
    }

    private fun startRandomButtonVisibility() {
        val buttonVisibleRunnable = object : Runnable {
            override fun run() {
                val randomButton = buttonArray[Random.nextInt(buttonArray.size)]
                randomButton.visibility = View.VISIBLE

                handler.postDelayed({
                    randomButton.visibility = View.INVISIBLE
                    handler.postDelayed(this, 500)  // Repeat every 500ms (adjust as needed)
                }, 500)  // Button visible duration
            }
        }
        handler.post(buttonVisibleRunnable)
    }

    private fun setupTimer() {
        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.left.text = "Left: ${millisUntilFinished / 1000}"
                binding.finish.visibility = View.INVISIBLE
            }

            override fun onFinish() {
                binding.left.visibility = View.INVISIBLE
                binding.finish.visibility = View.VISIBLE
                showAlert()
            }
        }.start()
    }

    fun onButtonClick(view: View) {
        score++
        binding.scoreval.text = score.toString()
    }

    private fun showAlert() {
        AlertDialog.Builder(this@MainActivity)
            .setTitle("Finish!")
            .setMessage("Congratulations! You gathered score: $score. Do you want to play again?")
            .setPositiveButton("Yes") { dialog, which ->
                restartGame()
                setupTimer()
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun retryGame() {
        binding.retry.setOnClickListener {
            restartGame()
        }
    }

    private fun restartGame() {
        handler.removeCallbacksAndMessages(null)
        finish()
        startActivity(intent)
    }
}

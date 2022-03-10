package ru.kudesnik.aateam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.kudesnik.aateam.databinding.FragmentSpinBinding
import java.util.*

class SpinFragment : Fragment() {
    private var _binding: FragmentSpinBinding? = null
    private val binding get() = _binding!!
    private val sRandom: Random = Random()
    private lateinit var wheelImage: ImageView
    private var lastAngle = -1
    private var scoreP: TextView? = null
    private var scoreRes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scoreRes = savedInstanceState?.getInt("counter", 0) ?: 0
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("counter", scoreRes)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        score.text = scoreRes.toString()
        wheelImage = imageviewWheel
        buttonSpin.setOnClickListener { spinBottle() }
        scoreP = score
    }

    private fun spinBottle() {
        val angle = sRandom.nextInt(3600 - 360) + 360
        // Центр вращения
        val pivotX = (wheelImage.width / 2).toFloat()
        val pivotY = (wheelImage.height / 2).toFloat()
        val animation: Animation = RotateAnimation(
            (if (lastAngle == -1) 0 else lastAngle).toFloat(),
            angle.toFloat(), pivotX, pivotY
        )
        lastAngle = angle
        scoreRes += angle
        animation.duration = 2500
        animation.fillAfter = true

        wheelImage.startAnimation(animation)

        scoreP?.text = scoreRes.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SpinFragment()
    }
}

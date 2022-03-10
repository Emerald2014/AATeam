package ru.kudesnik.aateam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.fragment.app.Fragment
import java.util.*


class SpinFragment : Fragment() {
    val sRandom: Random = Random()
    private var mBottleImageView: ImageView? = null
    private var lastAngle = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBottleImageView = activity?.findViewById<ImageView>(R.id.imageview_bottle)

        mBottleImageView!!.setOnClickListener(View.OnClickListener { spinBottle() })
    }
    private fun spinBottle() {

        val angle = sRandom.nextInt(3600 - 360) + 360
        // Центр вращения
        val pivotX = (mBottleImageView!!.width / 2).toFloat()
        val pivotY = (mBottleImageView!!.height / 2).toFloat()
        val animation: Animation = RotateAnimation(
            (if (lastAngle == -1) 0 else lastAngle).toFloat(),
            angle.toFloat(), pivotX, pivotY
        )
        lastAngle = angle
        animation.duration = 2500
        animation.fillAfter = true
        mBottleImageView!!.startAnimation(animation)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SpinFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = SpinFragment()
    }
}

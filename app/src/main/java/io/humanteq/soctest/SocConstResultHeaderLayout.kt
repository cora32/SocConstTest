package io.humanteq.soctest

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import io.humanteq.soctest.databinding.SocItemLayoutBinding

class SocConstResultHeaderLayout : ConstraintLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr)

    private val iconHeight by lazy { resources.getDimension(R.dimen.soc_friend_height) }

    fun setHeader(group: List<MainFragment.SocFriend>) {
        val child = getChildAt(0)
        child.post {
            val xCenter = child.x + child.width / 2f
            val yCenter = child.y - iconHeight + iconHeight / 4f

            group.take(20).forEach {
                val binding = SocItemLayoutBinding.inflate(
                        LayoutInflater.from(context), this, false)

                binding.model = it
                this.addView(binding.root)

                binding.root.post {
                    binding.root.visibility = View.VISIBLE
                    binding.root.x = xCenter - (binding.root.width / 2f) + (-100..100).random()
                    binding.root.y = yCenter + (-100..100).random()
                }
            }
        }
    }
}
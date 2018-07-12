package io.humanteq.soctest

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Handler
import android.support.animation.DynamicAnimation
import android.support.animation.SpringAnimation
import android.support.animation.SpringForce
import android.support.v4.content.ContextCompat
import android.support.v4.widget.CircularProgressDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.flexbox.FlexboxLayout
import io.humanteq.soctest.databinding.SocItemLayoutBinding
import org.jetbrains.anko.forEachChild
import java.util.*

fun Context.placeholder(): CircularProgressDrawable = CircularProgressDrawable(this)
        .apply {
            setColorSchemeColors(
                    ContextCompat.getColor(this@placeholder, R.color.colorAccent),
                    ContextCompat.getColor(this@placeholder, R.color.WT_BG_5),
                    ContextCompat.getColor(this@placeholder, R.color.red),
                    ContextCompat.getColor(this@placeholder, R.color.call_quest_color_2))
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

class SocFlexboxLayout : FlexboxLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr)

    private val socFriendWidth = context.resources.getDimension(R.dimen.soc_friend_width)
    private val socFriendHeight = context.resources.getDimension(R.dimen.soc_friend_height)
    private val xCenter = getScreenWidth(context) / 2f - socFriendWidth / 2f
    private val yCenter = getScreenHeight(context) / 2f - socFriendHeight / 2f
    //    private val yCenter by lazy { (parent as ViewGroup).height / 2f - socFriendHeight / 2f }
    private var queue: Queue<List<MainFragment.SocFriend>> = ArrayDeque<List<MainFragment.SocFriend>>()
    private var onFinish: (() -> Unit)? = null

    fun onGroupNamed(groupName: String) {
        collapse()
        slideToLeft()
    }

    private fun slideToLeft() {
        Handler().postDelayed({
            forEachChild {
                val springForceX: SpringForce by lazy(LazyThreadSafetyMode.NONE) {
                    SpringForce(-500f).apply {
                        stiffness = SpringForce.STIFFNESS_LOW
                        dampingRatio = 0.5f
                    }
                }

                SpringAnimation(it, DynamicAnimation.X).setSpring(springForceX).start()
            }
        }, 600L)

        Handler().postDelayed({ slideFromRight() }, 800L)
    }

    private fun slideFromRight() {
        removeAllViews()
        if (bind(nextFriendList())) {
            Handler().postDelayed({
                forEachChild {
                    it.x = getScreenWidth(context) + 500f
                    it.y = yCenter
                }
            }, 400L)
            Handler().postDelayed({ collapse() }, 500L)
            Handler().postDelayed({ expand() }, 1100L)
        }
    }

    private fun nextFriendList(): List<MainFragment.SocFriend>? = queue.poll()

    fun start(list: List<List<MainFragment.SocFriend>>) {
        queue.addAll(list)
        if (bind(nextFriendList())) {
            Handler().postDelayed({ center(); collapse() }, 1000L)
            Handler().postDelayed({ expand() }, 1500L)
        }
    }

    private fun bind(friends: List<MainFragment.SocFriend>?): Boolean {
        if (friends == null) {
            onFinish?.invoke()
            return false
        }

        removeAllViews()
        friends.forEach { achievement ->
            val binding = SocItemLayoutBinding.inflate(
                    LayoutInflater.from(context), this, false)

            binding.model = achievement
            this.addView(binding.root)

            binding.root.post {
                binding.root.tag = Pair(binding.root.x, binding.root.y)
            }
        }

        return true
    }

    private fun center() {
        forEachChild {
            it.x = xCenter + (-100..100).random()
            it.y = yCenter + (-100..100).random()
            it.visibility = View.VISIBLE
        }
    }

    private fun collapse() {
        forEachChild {
            it.visibility = View.VISIBLE
            val springForceX: SpringForce by lazy(LazyThreadSafetyMode.NONE) {
                SpringForce(xCenter + (-100..100).random()).apply {
                    stiffness = SpringForce.STIFFNESS_LOW
                    dampingRatio = 0.5f
                }
            }
            val springForceY: SpringForce by lazy(LazyThreadSafetyMode.NONE) {
                SpringForce(yCenter + (-100..100).random()).apply {
                    stiffness = SpringForce.STIFFNESS_MEDIUM
                    dampingRatio = 0.5f
                }
            }
            (it.findViewById<TextView>(R.id.name)).animate().alpha(0f)
            SpringAnimation(it, DynamicAnimation.X).setSpring(springForceX).start()
            SpringAnimation(it, DynamicAnimation.Y).setSpring(springForceY).start()
        }
    }

    private fun expand() {
        forEachChild {
            val springForceX: SpringForce by lazy(LazyThreadSafetyMode.NONE) {
                SpringForce((it.tag as Pair<Float, Float>).first).apply {
                    stiffness = SpringForce.STIFFNESS_LOW
                    dampingRatio = 0.5f
                }
            }
            val springForceY: SpringForce by lazy(LazyThreadSafetyMode.NONE) {
                SpringForce((it.tag as Pair<Float, Float>).second).apply {
                    stiffness = SpringForce.STIFFNESS_MEDIUM
                    dampingRatio = 0.5f
                }
            }

            (it.findViewById<TextView>(R.id.name)).animate().alpha(1f)
            SpringAnimation(it, DynamicAnimation.X).setSpring(springForceX)
                    .addEndListener { _, _, _, _ -> onCompleteAnimation?.invoke() }.start()
            SpringAnimation(it, DynamicAnimation.Y).setSpring(springForceY).start()
        }
    }

    fun getScreenHeight(context: Context): Int {
        val size = Point()
        (context as Activity).windowManager.defaultDisplay.getSize(size)
        return size.y
    }

    fun getScreenWidth(context: Context): Int {
        val size = Point()
        (context as Activity).windowManager.defaultDisplay.getSize(size)
        return size.x
    }

    fun setOnFinish(function: () -> Unit) {
        onFinish = function
    }

    private var onCompleteAnimation: (() -> Unit)? = null
    fun onCompleteAnimation(function: () -> Unit) {
        onCompleteAnimation = function
    }
}

fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) + start
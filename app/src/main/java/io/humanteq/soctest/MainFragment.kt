package io.humanteq.soctest

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.soc_main_layout.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async


fun <T> Any.bg(block: suspend CoroutineScope.() -> T): Deferred<T> =
        async(CommonPool, block = block)

fun <T> Any.ui(block: suspend CoroutineScope.() -> T): Deferred<T> = async(UI, block = block)

class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.soc_main_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flexbox.setOnFinish {
            showResult()
        }

        bg { setFlexboxData() }

//        flexbox.setOnClickListener{
//            onNext()
//        }

        namer.setOnEditorActionListener(TextView.OnEditorActionListener { textView, id, keyEvent ->
            if (id == 1337 || id == EditorInfo.IME_NULL) {
                namer.setText("")
                namer.hideKeyboard()

                Handler().postDelayed({ onNext(textView.text.toString()) }, 400L)
                return@OnEditorActionListener true
            }
            false
        })

        flexbox.onCompleteAnimation {
            //            namer.showKeyboard()
        }
    }

    private fun onNext(text: String = "") {
        scroll.scrollTo(0, 0)
        flexbox.onGroupNamed(text)
    }

    private fun setFlexboxData() {
        val list = getMockList()

        ui {
            Handler().postDelayed({
                TransitionManager.beginDelayedTransition(main_root as ViewGroup)
                pb.visibility = View.GONE
                flexbox.start(list)
            }, 500L)
        }
    }

    private fun getMockList(): List<List<SocFriend>> = arrayListOf<List<SocFriend>>().apply {
        repeat(3) {
            add(getMockFriendList())
        }
    }

    val arr = arrayListOf<String>("https://78.media.tumblr.com/eea15d0aa4d66091cdca37045e71f147/tumblr_nj4cqt6VO41u9c74ro1_400.jpg",
            "https://www.wikihow.com/images/thumb/f/f6/Know-if-You%27re-Falling-for-Your-Best-Friend-Step-1.jpg/aid642022-v4-728px-Know-if-You%27re-Falling-for-Your-Best-Friend-Step-1.jpg",
            "https://data.whicdn.com/images/165825107/superthumb.jpg?t=1425208972",
            "https://orig00.deviantart.net/2516/f/2012/091/9/5/anime_girl_icon_by_pinkangelx12-d4una2u.png",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxupqvXzRya2_sphbiEmFjySRXjxvSTULRK8Ft35wGbIyjjXgC")

    private fun getMockFriendList() = arrayListOf<SocFriend>().apply {
        repeat(20) {
            add(SocFriend(" friend ff$it",
                    arr.getRandom(),
                    it))
        }
        add(SocFriend(" special ff 20!",
                "https://www.wikihow.com/images/thumb/f/f6/Know-if-You%27re-Falling-for-Your-Best-Friend-Step-1.jpg/aid642022-v4-728px-Know-if-You%27re-Falling-for-Your-Best-Friend-Step-1.jpg",
                20))
        sortBy { it.likes }
    }

    data class SocFriend(val name: String, val imageUrl: String, val likes: Int)

    private fun showResult() {
        fragmentManager?.beginTransaction()
                ?.setCustomAnimations(R.animator.frag_enter, R.animator.frag_exit)
                ?.replace(R.id.container, ResultFragment())
                ?.commit()
    }
}

private fun EditText.hideKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

private fun EditText.showKeyboard() {
    setText("")
    requestFocus()
    val inputMethodManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    (this.context as Activity).currentFocus?.let {
        it.windowToken?.let {
            inputMethodManager.toggleSoftInputFromWindow(it, InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }

}

private fun <E> ArrayList<E>.getRandom(): E = this[(0..size).random()]

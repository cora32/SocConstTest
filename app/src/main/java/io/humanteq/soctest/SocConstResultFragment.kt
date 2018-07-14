package io.humanteq.soctest

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.soc_result_layout.*

class SocConstResultFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.soc_result_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        headerTv.text = "friends"
        header.setHeader(getMockFriendList())
    }

    private fun getMockList(): List<List<MainFragment.SocFriend>> = arrayListOf<List<MainFragment.SocFriend>>().apply {
        repeat(1) {
            add(getMockFriendList())
        }
    }

    val arr = arrayListOf<String>("https://78.media.tumblr.com/eea15d0aa4d66091cdca37045e71f147/tumblr_nj4cqt6VO41u9c74ro1_400.jpg",
            "https://www.wikihow.com/images/thumb/f/f6/Know-if-You%27re-Falling-for-Your-Best-Friend-Step-1.jpg/aid642022-v4-728px-Know-if-You%27re-Falling-for-Your-Best-Friend-Step-1.jpg",
            "https://data.whicdn.com/images/165825107/superthumb.jpg?t=1425208972",
            "https://orig00.deviantart.net/2516/f/2012/091/9/5/anime_girl_icon_by_pinkangelx12-d4una2u.png",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxupqvXzRya2_sphbiEmFjySRXjxvSTULRK8Ft35wGbIyjjXgC")

    private fun getMockFriendList() = arrayListOf<MainFragment.SocFriend>().apply {
        repeat(20) {
            add(MainFragment.SocFriend(" friend ff$it",
                    arr.getRandom(),
                    it))
        }
        add(MainFragment.SocFriend(" special ff 20!",
                "https://www.wikihow.com/images/thumb/f/f6/Know-if-You%27re-Falling-for-Your-Best-Friend-Step-1.jpg/aid642022-v4-728px-Know-if-You%27re-Falling-for-Your-Best-Friend-Step-1.jpg",
                20))
        sortBy { it.likes }
    }
}
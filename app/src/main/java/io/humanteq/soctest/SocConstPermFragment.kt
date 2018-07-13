package io.humanteq.soctest

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.soc_perm_layout.*

class SocConstPermFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.soc_perm_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn.setOnClickListener {
            fragmentManager?.beginTransaction()
                    ?.setCustomAnimations(R.animator.frag_enter, R.animator.frag_exit)
                    ?.replace(R.id.container, MainFragment(), "MAIN")
                    ?.addToBackStack("MAIN")
                    ?.commit()
        }
    }
}
package com.example.blizzard.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blizzard.R
import com.example.blizzard.util.FavouriteFragmentAdapter
import com.example.blizzard.views.extensions.initialiseAdapter
import kotlinx.android.synthetic.main.fragment_favourites.*
import kotlinx.coroutines.launch
import java.util.*

class FavouritesFragment : Fragment() {
    var adapter: FavouriteFragmentAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        rv_fav.layoutManager = layoutManager
        adapter = FavouriteFragmentAdapter(requireContext(), ArrayList())
        rv_fav.adapter = adapter
        lifecycleScope.launch {
            initialiseAdapter()
        }
    }
}
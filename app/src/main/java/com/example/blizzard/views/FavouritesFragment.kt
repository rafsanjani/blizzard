package com.example.blizzard.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blizzard.databinding.FragmentFavouritesBinding
import com.example.blizzard.util.FavouriteFragmentAdapter
import com.example.blizzard.views.extensions.initialiseAdapter
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*

class FavouritesFragment : Fragment() {
    lateinit var favouritesAdapter: FavouriteFragmentAdapter
    private var FavouritesBinding : FragmentFavouritesBinding? = null
    val binding get() = FavouritesBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        FavouritesBinding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        binding.listFavourites.layoutManager = layoutManager
        favouritesAdapter = FavouriteFragmentAdapter(ArrayList())
        binding.listFavourites.adapter = favouritesAdapter
        lifecycleScope.launch(IO) {
            initialiseAdapter()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FavouritesBinding = null
        favouritesAdapter.destroyBinding()
    }

    override fun onStop() {
        super.onStop()
        favouritesAdapter.clearEntities()
    }
}
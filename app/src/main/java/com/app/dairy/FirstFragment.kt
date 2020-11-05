package com.app.dairy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FirstFragment : Fragment() {

    private lateinit var vm: ViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        vm = ViewModelProvider(requireActivity()).get(ViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        val rv = view.findViewById<RecyclerView>(R.id.recyclerView)
        val hint = view.findViewById<TextView>(R.id.textHint)

        val adapter = RecyclerAdapter(requireContext())
        rv.adapter = adapter

        vm.list.observe(viewLifecycleOwner) { list ->

            if(list == null || list.isEmpty()) {
                hint.visibility = View.VISIBLE
                rv.visibility = View.INVISIBLE
            } else {
                hint.visibility = View.INVISIBLE
                rv.visibility = View.VISIBLE
                adapter.updateList(list)
            }
        }

        adapter.setOnItemClickListener(object: RecyclerAdapter.OnItemClickListener {
            override fun onClicked(dairyItem: DairyItem) {

                vm.selectedItem.postValue(dairyItem)
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment,
                Bundle().apply { putBoolean(SecondFragment.KEY_VIEW, true) })

                requireActivity().findViewById<FloatingActionButton>(R.id.fabAdd).visibility = View.GONE
            }
        })

        vm.addResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                it.fold(onSuccess = { msg ->
                    requireContext().toast(msg)
                }, onFailure = { ex ->
                    requireContext().toast("Error: ${ex.message}")
                    ex.printStackTrace()
                })
            }
        }

    }
}
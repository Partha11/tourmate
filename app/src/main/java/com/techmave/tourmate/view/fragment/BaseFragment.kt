package com.techmave.tourmate.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.techmave.tourmate.listener.FragmentListener
import com.techmave.tourmate.view.fragment.auth.LoginFragment
import com.techmave.tourmate.view.fragment.auth.RegisterFragment

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<VB: ViewBinding, VM: ViewModel>(private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB): Fragment() {

    abstract val viewModel: VM

    private var _binding: ViewBinding? = null

    protected val binding: VB

        get() = _binding as VB

    protected var listener: FragmentListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = bindingInflater.invoke(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroy() {

        super.onDestroy().also { _binding = null }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val host: MenuHost = requireActivity()

        host.addMenuProvider(object: MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) = menu.clear()

            override fun onMenuItemSelected(menuItem: MenuItem) = false
        })

        initialize()
    }

    override fun onStart() {

        super.onStart().also {

            if (this is LoginFragment || this is RegisterFragment) {

                listener?.showHideBottomNav(false)

            } else {

                listener?.showHideBottomNav(true)
            }
        }
    }

    override fun onResume() {

        super.onResume().also {

            if (this is LoginFragment || this is RegisterFragment) {

                (activity as AppCompatActivity).supportActionBar?.hide()

            } else {

                (activity as AppCompatActivity).supportActionBar?.show()
            }
        }
    }

    override fun onAttach(context: Context) {

        super.onAttach(context).also {

            if (context is FragmentListener) {

                listener = context
            }
        }
    }

    protected abstract fun initialize()
}
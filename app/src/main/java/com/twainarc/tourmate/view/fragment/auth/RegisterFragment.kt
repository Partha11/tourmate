package com.twainarc.tourmate.view.fragment.auth

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.twainarc.tourmate.R
import com.twainarc.tourmate.databinding.FragmentRegisterBinding
import com.twainarc.tourmate.utils.view.Extensions.makeLinks
import com.twainarc.tourmate.view.fragment.BaseFragment
import com.twainarc.tourmate.viewmodel.AuthViewModel

class RegisterFragment: BaseFragment<FragmentRegisterBinding, AuthViewModel>(FragmentRegisterBinding::inflate) {

    override val viewModel: AuthViewModel by viewModels()

    override fun initialize() {

        binding.signInText.makeLinks(Pair(resources.getString(R.string.sign_in), View.OnClickListener {

            findNavController().navigate(RegisterFragmentDirections.actionRegisterToLogin())
        }))
    }
}
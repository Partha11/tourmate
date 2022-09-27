package com.techmave.tourmate.view.fragment.auth

import androidx.fragment.app.viewModels
import com.techmave.tourmate.databinding.FragmentLoginBinding
import com.techmave.tourmate.view.fragment.BaseFragment
import com.techmave.tourmate.viewmodel.AuthViewModel

class LoginFragment: BaseFragment<FragmentLoginBinding, AuthViewModel>(FragmentLoginBinding::inflate) {

    override val viewModel: AuthViewModel by viewModels()

    override fun initialize() {

    }
}
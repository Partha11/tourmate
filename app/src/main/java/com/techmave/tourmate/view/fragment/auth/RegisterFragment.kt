package com.techmave.tourmate.view.fragment.auth

import androidx.fragment.app.viewModels
import com.techmave.tourmate.databinding.FragmentRegisterBinding
import com.techmave.tourmate.view.fragment.BaseFragment
import com.techmave.tourmate.viewmodel.AuthViewModel

class RegisterFragment: BaseFragment<FragmentRegisterBinding, AuthViewModel>(FragmentRegisterBinding::inflate) {

    override val viewModel: AuthViewModel by viewModels()

    override fun initialize() {

    }
}
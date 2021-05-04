package com.patagonian.lyrics.util

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.patagonian.lyrics.R

fun Fragment.setup(navController: NavController) {
    navController.setGraph(R.navigation.nav_graph)
    Navigation.setViewNavController(requireView(), navController)
}
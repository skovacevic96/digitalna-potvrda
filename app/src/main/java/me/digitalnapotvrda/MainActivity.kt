package me.digitalnapotvrda

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import me.digitalnapotvrda.utils.updateNavBarColor


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val viewModel by viewModels<MainViewModel>()

    private val permissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it) finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DigitalnaPotvrda)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        instantiateNavController()
        permissions.launch(Manifest.permission.CAMERA)
    }

    private fun instantiateNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        navGraph.startDestination = when {
            viewModel.isQrCodeAvailable() -> R.id.qrCodeFragment
            else -> R.id.scanFragment
        }

        navController.graph = navGraph

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.scanFragment -> {
                    updateNavBarColor(R.color.black, false)
                }
                else -> {
                    updateNavBarColor(R.color.white, true)
                }
            }
        }
    }
}
package com.example.humblr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.humblr.auth.AuthorizationViewModel
import com.example.humblr.auth.LocalStorage
import com.example.humblr.data.TokensRepository
import com.example.humblr.databinding.ActivityMainBinding
import com.example.humblr.onboarding.OnboardingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val viewModel: AuthorizationViewModel by viewModels()
    @Inject lateinit var tokensRepository: TokensRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*val intent = Intent(this, AuthorizationActivity::class.java)
        startActivity(intent)*/

        if (LocalStorage.logout == true) {
            viewModel.logout()
        }

        if(tokensRepository.getAccessTokenFromLocalStorage() == null) {
            Log.d("TokenAccess", "${tokensRepository.getAccessTokenFromLocalStorage()}")
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
        } else {
            LocalStorage.accessToken = tokensRepository.getAccessTokenFromLocalStorage()
            LocalStorage.refreshToken = tokensRepository.getRefreshTokenFromLocalStorage()
            viewModel.getMyName()
            Log.d("Token", "${LocalStorage.accessToken}")
            Log.d("Token", "${LocalStorage.refreshToken}")
            viewModel.refreshToken()

        }

        lifecycleScope.launch {
            viewModel.isRefreshing.collect() {
                if (!it) {
                    binding = ActivityMainBinding.inflate(layoutInflater)
                    setContentView(binding.root)

                    val navView: BottomNavigationView = binding.navView
                    val navHostFragment =
                        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                    navController = navHostFragment.navController
                    // Passing each menu ID as a set of Ids because each
                    // menu should be considered as top level destinations.
                    val appBarConfiguration = AppBarConfiguration(
                        setOf(
                            R.id.navigation_posts, R.id.navigation_favorite, R.id.navigation_user
                        )
                    )
                    setupActionBarWithNavController(navController, appBarConfiguration)
                    navView.setupWithNavController(navController)

                    navController.addOnDestinationChangedListener { _, _, _ ->
                        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow)
                    }
                }
            }
        }


    }


    //Для работы кнопки назад в ActionBar.
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
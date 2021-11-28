package com.bottotop.myalba

import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bottotop.core.global.ShowLoading
import com.bottotop.myalba.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import com.bottotop.core.global.SharedViewModel
import com.bottotop.core.global.NavigationRouter
import com.bottotop.core.global.NavigationTable
import com.bottotop.core.global.SocialInfo
import java.util.*
import android.net.*
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import com.bottotop.core.ext.*
import com.bottotop.core.navigation.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ToFlowNavigation, ShowLoading {

    private val navigator: Navigator = Navigator()
    private var networkCheck: Boolean = true
    lateinit var binding: ActivityMainBinding
    private val viewModel: SharedViewModel by viewModels()
    private var backPressedTime: Long = 0
    lateinit var cm: ConnectivityManager
    lateinit var mNetworkCallback: NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        networkCheck()
        initBottomNavigation()
        setSupportActionBar(binding.appToolbar)
    }

    override fun onResume() {
        observeNetwork()
        super.onResume()
    }

    override fun onPause() {
        cm.unregisterNetworkCallback(mNetworkCallback)
        super.onPause()
    }

    override fun navigateToFlow(flow: NavigationFlow) {
        navigator.navigateToFlow(flow)
    }

    override fun showLoading(isLoading: Boolean) {
        if(isLoading) binding.mainLayout.alpha = 0.5f
        else binding.mainLayout.alpha = 1f
        binding.mainLayout.isClickable = isLoading
        binding.loadingView.isInProgress = isLoading
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.schedule_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
            R.id.menu_right1 -> super.onOptionsItemSelected(item)
            R.id.setting -> {
                navigator.navController.deepLinkNavigateTo(DeepLinkDestination.Setting(SocialInfo.id))
                super.onOptionsItemSelected(item)
            }
            R.id.mypage -> {
                navigator.navController.deepLinkNavigateTo(DeepLinkDestination.Info(SocialInfo.id))
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun initBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navigator.navController = navController
        binding.navViewFragment.setupWithNavController(navController)
        setBottomNavAction(navController)
    }

    private fun setBottomNavAction(navController: NavController) {
        binding.navViewFragment.setOnItemSelectedListener {
            binding.ToolbarLayout.setExpanded(true)
            when (it.itemId) {
                R.id.member_flow -> if (NavigationRouter.currentState != NavigationTable.Member) navigateToFlow(NavigationFlow.MemberFlow("test"))
                R.id.community_flow -> if (NavigationRouter.currentState != NavigationTable.Community) navigateToFlow(NavigationFlow.CommunityFlow("test"))
                R.id.home_flow -> if (NavigationRouter.currentState != NavigationTable.Home) navigateToFlow(NavigationFlow.HomeFlow("test"))
                R.id.schedule_flow -> if (NavigationRouter.currentState != NavigationTable.Schedule) navigateToFlow(NavigationFlow.ScheduleFlow("test"))
                R.id.asset_flow -> if (NavigationRouter.currentState != NavigationTable.Asset) navigateToFlow(NavigationFlow.AssetFlow("test"))
            }
            return@setOnItemSelectedListener true
        }
        observeNavDirection(navController)
    }

    private fun observeNavDirection(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            NavigationRouter.saveState(destination.label as String)
            when (NavigationRouter.currentState) {
                NavigationTable.Member ->{
                    showBar()
                    binding.appToolbar.title = "직원관리"
                }
                NavigationTable.Community -> {
                    showBar()
                    binding.appToolbar.title = "커뮤니티"
                }
                NavigationTable.Home -> {
                    showBar()
                    binding.appToolbar.title = "내알빠"
                }
                NavigationTable.Schedule -> {
                    showBar()
                    binding.appToolbar.title = "일정관리"
                }
                NavigationTable.Asset -> {
                    showBar()
                    binding.appToolbar.title = "자산관리"
                }
                NavigationTable.Setting -> {
                    binding.appToolbar.isVisible()
                    binding.navViewFragment.isGone()
                    binding.appToolbar.title = "설정"
                }
                NavigationTable.Info -> {
                    binding.appToolbar.isVisible()
                    binding.navViewFragment.isGone()
                    binding.appToolbar.title = "정보"
                }
                else -> hideBar()
            }
        }
    }

    private fun showBar(){
        binding.appToolbar.isVisible()
        binding.navViewFragment.isVisible()
    }

    private fun hideBar(){
        binding.appToolbar.isGone()
        binding.navViewFragment.isGone()
    }

    private fun networkCheck() {
        cm = getSystemService(ConnectivityManager::class.java)
        val check = cm.activeNetworkInfo?.isConnectedOrConnecting
        if (check == false || check == null) {
            showToast("네트워크 연결을 확인해주세요")
            finish()
        }
    }

    private fun observeNetwork() {
        mNetworkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                networkCheck = true
            }
            override fun onLost(network: Network) {
                networkCheck = false
                withDelayOnMain(1000){
                    if (networkCheck) {
                        Log.e(TAG, "onLost: 연결되있음")
                    } else {
                        showToast("네트워크 연결이 끊어졌습니다.")
                    }
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cm.registerDefaultNetworkCallback(mNetworkCallback)
        }
    }


    override fun onBackPressed() {
        when (NavigationRouter.currentState) {
            is NavigationTable.Asset, NavigationTable.Community ,
                NavigationTable.Member, NavigationTable.Schedule -> navigateToFlow(NavigationFlow.HomeFlow("back"))
            is NavigationTable.Login, NavigationTable.Register , NavigationTable.Home -> {
                if (System.currentTimeMillis() > backPressedTime + 3000) {
                    backPressedTime = System.currentTimeMillis()
                    Toast.makeText(this, "한번더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
                } else if (System.currentTimeMillis() <= backPressedTime + 3000) {
                    finish()
                }
            }
            else -> super.onBackPressed()
        }
    }

    companion object {
        const val TAG = "메인액티비티"
    }
}

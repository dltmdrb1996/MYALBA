package com.bottotop.myalba

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.*
import android.net.ConnectivityManager.NetworkCallback
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bottotop.core.ext.*
import com.bottotop.core.global.*
import com.bottotop.core.global.PreferenceHelper.get
import com.bottotop.core.navigation.*
import com.bottotop.model.repository.DataRepository
import com.bottotop.myalba.databinding.ActivityMainBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.BadgeUtils.attachBadgeDrawable
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ToFlowNavigation, ShowLoading {

    private val navigator: Navigator = Navigator()
    private var networkCheck: Boolean = true
    lateinit var binding: ActivityMainBinding
    private val viewModel: SharedViewModel by viewModels()
    private var backPressedTime: Long = 0
    lateinit var cm: ConnectivityManager
    lateinit var mNetworkCallback: NetworkCallback
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    @Inject lateinit var dataRepository: DataRepository
    lateinit var mPref: SharedPreferences
    lateinit var badgeDrawable : BadgeDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        mPref = PreferenceHelper.defaultPrefs(applicationContext)
        if(mPref["dark", false]) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        badgeDrawable = BadgeDrawable.create(binding.appToolbar.context)
        networkCheck()
        initBottomNavigation()
        setSupportActionBar(binding.appToolbar)
        recodeStartApp()
    }

    private fun recodeStartApp() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
            param(FirebaseAnalytics.Param.ITEM_ID, "앱시작")
            param(FirebaseAnalytics.Param.ITEM_NAME, "앱시작")
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "앱시작")
        }
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
            R.id.menu_expend -> super.onOptionsItemSelected(item)
            R.id.setting -> {
                if(NavigationRouter.currentState != NavigationTable.Setting ) {
                    navigator.navController.deepLinkNavigateTo(DeepLinkDestination.Setting(SocialInfo.id))
                }
                super.onOptionsItemSelected(item)
            }
            R.id.mypage -> {
                if(NavigationRouter.currentState != NavigationTable.Info ) {
                    navigator.navController.deepLinkNavigateTo(DeepLinkDestination.Info(SocialInfo.id))
                }
                super.onOptionsItemSelected(item)
            }
            R.id.menu_notification -> {
                if(NavigationRouter.currentState != NavigationTable.Notification ) {
                    navigator.navController.deepLinkNavigateTo(DeepLinkDestination.Notification(SocialInfo.id))
                }
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
            when (it.itemId) {
                R.id.member_flow -> if (NavigationRouter.currentState != NavigationTable.Member) navigateToFlow(NavigationFlow.MemberFlow("member"))
                R.id.community_flow -> if (NavigationRouter.currentState != NavigationTable.Community) navigateToFlow(NavigationFlow.CommunityFlow("community"))
                R.id.home_flow -> if (NavigationRouter.currentState != NavigationTable.Home) navigateToFlow(NavigationFlow.HomeFlow("home"))
                R.id.schedule_flow -> if (NavigationRouter.currentState != NavigationTable.Schedule) navigateToFlow(NavigationFlow.ScheduleFlow("schedule"))
                R.id.asset_flow -> if (NavigationRouter.currentState != NavigationTable.Asset) navigateToFlow(NavigationFlow.AssetFlow("asset"))

            }
            return@setOnItemSelectedListener true
        }
        observeNavDirection(navController)
    }

    private fun observeNavDirection(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            NavigationRouter.saveState(destination.label as String)
            if(arguments?.get("msg") != null) NavigationRouter.arg = arguments["msg"]?.toString()!!
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
                NavigationTable.Notification -> {
                    binding.appToolbar.isVisible()
                    binding.navViewFragment.isGone()
                    binding.appToolbar.title = "알림"
                }
                else -> hideBar()
            }

        }
    }

    private fun showBar(){
        binding.appToolbar.isVisible()
        binding.navViewFragment.isVisible()
        CoroutineScope(Dispatchers.IO).launch {
            makeBadge()
        }
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
            override fun onCapabilitiesChanged(network : Network, networkCapabilities : NetworkCapabilities) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    Timber.e("1 = ${networkCapabilities.ownerUid}")
//                    Timber.e("2 = ${networkCapabilities.transportInfo}")
//                    Timber.e("3 = ${networkCapabilities.networkSpecifier}")
//                    Timber.e("4 = ${networkCapabilities.signalStrength}")
                }
            }

            @RequiresApi(Build.VERSION_CODES.R)
            override fun onLinkPropertiesChanged(network : Network, linkProperties : LinkProperties) {
//                Timber.e("5 = ${linkProperties.linkAddresses}")
//                Timber.e("6 = ${linkProperties.dhcpServerAddress}")
//                Timber.e("7 = ${linkProperties.dnsServers}")
//                Timber.e("8 = ${linkProperties.domains}")
//                Timber.e("9 = ${linkProperties.interfaceName}")
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cm.registerDefaultNetworkCallback(mNetworkCallback)
        }
    }


    override fun onBackPressed() {
        if(NavigationRouter.arg == "back") {
            super.onBackPressed()
            return
        }

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


    @SuppressLint("UnsafeOptInUsageError")
    private suspend fun makeBadge(){
        val count = mPref["badge", 0]
        if(count!=0){
            badgeDrawable.number = count
            badgeDrawable.backgroundColor = Color.RED
            BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.appToolbar, R.id.menu_notification)
        } else {
            BadgeUtils.detachBadgeDrawable(badgeDrawable , binding.appToolbar , R.id.menu_notification)
        }
    }

    companion object {
        const val TAG = "메인액티비티"
    }
}

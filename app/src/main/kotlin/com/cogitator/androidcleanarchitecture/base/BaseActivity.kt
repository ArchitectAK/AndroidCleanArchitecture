package com.cogitator.androidcleanarchitecture.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.arch.lifecycle.ViewModelProvider
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.transition.Transition
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import com.cogitator.androidcleanarchitecture.CleanAndroidApp
import com.cogitator.androidcleanarchitecture.R
import com.cogitator.androidcleanarchitecture.utils.invisble
import com.cogitator.androidcleanarchitecture.utils.visible

/**
 * @author Ankit Kumar on 01/10/2018
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>(private val mViewModelClass: Class<VM>) : AppCompatActivity(), BaseFragment.Callback {

    // Initialization of ViewModelFactory
    var viewModelFactory: ViewModelProvider.Factory = CleanAndroidApp.component.factory()

    @LayoutRes
    abstract fun getLayoutRes(): Int

    val binding by lazy {
        DataBindingUtil.setContentView(this, getLayoutRes()) as DB
    }

    protected fun <T : BaseViewModel> getViewModel(clazz: Class<T>, viewModelFactory: ViewModelProvider.Factory): T {
        return ViewModelProviders.of(this, viewModelFactory)
                .get(clazz)
    }

    private fun getViewModel(viewmodel: Class<VM>): VM {
        return ViewModelProviders.of(this, viewModelFactory).get(viewmodel)
    }

    val viewModel by lazy {
        getViewModel(mViewModelClass)
    }

    /**
     * If you want to inject Dependency Injection
     * on your activity, you can override this method.
     */
    open fun onInject() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        initViewModel(viewModel)
        super.onCreate(savedInstanceState)
        onInject()
    }

    /**
     *
     *  You need to override this method.
     *  And you need to set viewModel to binding: binding.viewModel = viewModel
     *
     */
    abstract fun initViewModel(viewModel: VM)

    val isNetworkConnected: Boolean
        get() = NetworkUtils.isNetworkConnected(applicationContext)

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        //do nothing
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    fun getLastNotNull(list: List<Fragment>): Fragment? {
        list.indices.reversed()
                .map { list[it] }
                .forEach {
                    return it
                }
        return null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getTopFragment()?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * function to show the fragment
     *
     * @param name fragment to be shown
     */
    fun showFragment(name: Fragment) {
        val fragmentManager = supportFragmentManager
        // check if the fragment is in back stack
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flContent, name, name.javaClass.name)
        fragmentTransaction.addToBackStack(name.javaClass.name)
        fragmentTransaction.commitAllowingStateLoss()
    }

    /**
     * function to show the fragment
     *
     * @param current current visible fragment
     * @param newFragment next visible fragment
     * @param sharedView view which show the transition
     *
     */
    fun showFragmentWithTransition(current: Fragment, newFragment: Fragment, sharedView: View) {
        val fragmentManager = supportFragmentManager
        // check if the fragment is in back stack
        val fragmentPopped = fragmentManager.popBackStackImmediate(newFragment.javaClass.name, 0)
        if (fragmentPopped) {
            // fragment is pop from backStack
        } else {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setReorderingAllowed(true)
            fragmentTransaction.add(R.id.flContent, newFragment, newFragment.javaClass.name)
            fragmentTransaction.addToBackStack(newFragment.javaClass.name)
            fragmentTransaction.addSharedElement(sharedView, sharedView.transitionName)
            fragmentTransaction.commitAllowingStateLoss()
//            newFragment.view?.rootView?.let { fragmentCircularReveal(sharedView, it) }
        }
    }

    /**
     * function to get the transition of the list item
     *
     * @param itemView view which show the transition
     *
     */
    fun getListFragmentExitTransition(itemView: View) {
        val epicCenterRect = Rect()
        //itemView is the full-width inbox item's view
        itemView.getGlobalVisibleRect(epicCenterRect)
        // Set Epic center to a imaginary horizontal full width line under the clicked item, so the explosion happens vertically away from it
        epicCenterRect.top = epicCenterRect.bottom
        val exitTransition = Explode()
        exitTransition.epicenterCallback = object : Transition.EpicenterCallback() {
            override fun onGetEpicenter(transition: Transition): Rect {
                return epicCenterRect
            }
        }
    }

    /**
     * function to show the fragment with default transition
     *
     * @param newFragment next visible fragment
     *
     */

    fun showFragmentWithOutTransition(newFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        // check if the fragment is in back stack
        val fragmentPopped = fragmentManager.popBackStackImmediate(newFragment.javaClass.name, 0)
        if (fragmentPopped) {
            // fragment is pop from backStack
        } else {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.flContent, newFragment, newFragment.javaClass.name)
            fragmentTransaction.addToBackStack(newFragment.javaClass.name)
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    /**
     * function to retrieve top fragment from the back stack entry
     *
     */

    fun getTopFragment(): Fragment? {
        if (supportFragmentManager.backStackEntryCount == 0) {
            return null
        }
        val fragmentTag = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name
        return supportFragmentManager.findFragmentByTag(fragmentTag)
    }

    fun animateIt(view: View) {
        view.invisble()
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val revealX = if (view.width > 0) view.width / 2 else width / 2
        val revealY = if (view.height > 0) view.height / 2 else height / 2
        val viewTreeObserver = view.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    revealActivity(revealX, revealY, view)
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }

    private fun revealActivity(x: Int, y: Int, view: View) {
        val finalRadius: Float = ((Math.max(view.rootView.width, view.rootView.height) * 1.1).toFloat())

        // create the animator for this view (the start radius is zero)
        val circularReveal = ViewAnimationUtils.createCircularReveal(view, x, y, 0f, finalRadius)
        circularReveal.duration = 600
        circularReveal.interpolator = AccelerateInterpolator()

        // make the view visible and start the animation
        view.visible()
        circularReveal.start()
//        finish()
    }

    fun exitReveal(view: View) {

        // get the center for the clipping circle
        val cx = view.measuredWidth / 2
        val cy = view.measuredHeight / 2

        // get the initial radius for the clipping circle
        val initialRadius = view.width / 2

        // create the animation (the final radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius.toFloat(), 0f)

        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.invisble()
            }
        })

        // start the animation
        anim.start()
    }

    fun fragmentCircularReveal(sharedView: View, view: View) {
        val x = sharedView.measuredWidth / 2
        val y = sharedView.measuredHeight / 2

        val finalRadius: Float = ((Math.max(view.width, view.height) * 1.1).toFloat())

        // create the animator for this view (the start radius is zero)
        val circularReveal = ViewAnimationUtils.createCircularReveal(sharedView, x, y, 0f, finalRadius)
        circularReveal.duration = 400
        circularReveal.interpolator = AccelerateInterpolator()

        // make the view visible and start the animation
        view.visible()
        circularReveal.start()
    }
}
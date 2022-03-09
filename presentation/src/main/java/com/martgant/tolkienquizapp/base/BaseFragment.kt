package com.martgant.tolkienquizapp.base

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

// Since we know have two methods that can be potentially reused in all our fragments - let's create BaseFragment.
// A better use for it will have place later.
abstract class BaseFragment : Fragment() {

    // protected operator makes sure that only BaseFragment and all clases that extend it can use this method.
    // Take a look also at "duration: Int = Toast.LENGTH_LONG" - the part after equals sign is a default value for that parameter
    // meaning you don't have to provide it if you want the duration to be Toast.LENGTH_LONG
    protected fun showToast(@StringRes textRes: Int, duration: Int = Toast.LENGTH_LONG) {
        context?.let {
            Toast.makeText(it, textRes, duration).show()
        }
    }

    @StringRes
    protected fun getStringByName(stringName: String): Int {
        return resources.getIdentifier(stringName, "string", activity?.packageName)
    }
}

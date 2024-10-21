package com.leic52dg17.chimp

import android.app.Application

const val TAG = "CHIMP"

interface DependenciesContainer {

}

class ChimpApplication: Application(), DependenciesContainer {
}
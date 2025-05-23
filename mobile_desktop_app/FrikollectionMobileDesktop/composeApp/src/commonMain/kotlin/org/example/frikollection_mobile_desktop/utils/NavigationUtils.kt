package org.example.frikollection_mobile_desktop.utils

import org.example.frikollection_mobile_desktop.BottomMenuItem
import org.example.frikollection_mobile_desktop.Screen

fun BottomMenuItem.toScreen(): Screen = when (this) {
    BottomMenuItem.Home -> Screen.Home
    BottomMenuItem.Discover -> Screen.Discover
    BottomMenuItem.Search -> Screen.Search
    BottomMenuItem.Lists -> Screen.Collection
    BottomMenuItem.Account -> Screen.Account
}
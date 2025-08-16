package com.joshayoung.notemark

import com.joshayoung.notemark.core.navigation.Destination

class AuthService {
    companion object Companion {
        fun notInAuthRoutes(path: String?): Boolean {
            val route = path?.split('.')?.lastOrNull()
            val authRoutes = listOf(
                null,
                Destination.Login.toString(),
                Destination.StartScreen.toString(),
                Destination.Registration.toString()
            )

            return !authRoutes.contains(route)
        }
    }
}
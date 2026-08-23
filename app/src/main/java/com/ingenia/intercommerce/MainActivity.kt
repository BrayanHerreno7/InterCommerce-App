package com.ingenia.intercommerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ingenia.intercommerce.core.theme.InterCommerceTheme
import com.ingenia.intercommerce.feature.cart.presentation.CartScreen
import com.ingenia.intercommerce.feature.catalog.presentation.CatalogScreen
import com.ingenia.intercommerce.feature.productdetail.presentation.ProductDetailScreen
import com.ingenia.intercommerce.navigation.CartRoute
import com.ingenia.intercommerce.navigation.CatalogRoute
import com.ingenia.intercommerce.navigation.ProductDetailRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InterCommerceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = CatalogRoute
                    ) {
                        composable<CatalogRoute> {
                            CatalogScreen(
                                onProductClick = { productId ->
                                    navController.navigate(ProductDetailRoute(productId))
                                },
                                onCartClick = {
                                    navController.navigate(CartRoute)
                                }
                            )
                        }

                        composable<ProductDetailRoute> {
                            ProductDetailScreen(
                                onBackClick = { navController.popBackStack() },
                                onCartClick = { navController.navigate(CartRoute) }
                            )
                        }

                        composable<CartRoute> {
                            CartScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

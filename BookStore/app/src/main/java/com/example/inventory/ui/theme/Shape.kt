package com.example.inventory.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    //Bo tròn bốn góc
    extraSmall = RoundedCornerShape(4.dp), // Bo góc nhỏ với bán kính 4 dp
    small = RoundedCornerShape(8.dp), // Bo góc vừa với bán kính 8 dp
    medium = RoundedCornerShape(16.dp), // Bo góc lớn với bán kính 16 dp
    large = RoundedCornerShape(24.dp) // Bo góc rất lớn với bán kính 24 dp
)
